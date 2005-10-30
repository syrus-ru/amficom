/*-
 * $Id: SchemeGraphTransferHandler.java,v 1.5 2005/10/30 15:20:56 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JComponent;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.plaf.basic.TransferHandler;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

public class SchemeGraphTransferHandler extends TransferHandler {
	private static final long serialVersionUID = -7753106340392538166L;
	private static final String TRANSFERABLE_OBJECTS = "graph_cell_transferable"; //$NON-NLS-1$

	DataFlavor flavor;
	private static int action;
	private Object[] insertedCells;

	public SchemeGraphTransferHandler() {
		this.flavor = new DataFlavor(ArrayList.class, TRANSFERABLE_OBJECTS);
	}

	@Override
	public void exportToClipboard(JComponent c, Clipboard clipboard, int action1) throws IllegalStateException {
		if (c instanceof SchemeGraph) {
			action = action1;
			
			final SchemeGraph graph = (SchemeGraph) c;
			final Object[] cells = graph.getSelectionCells();

			final ArrayList<Object> alist = (ArrayList)graph.getArchiveableState(cells);
			
			if (action == MOVE) {
				GraphActions.removeCells(graph, DefaultGraphModel.getDescendants(graph.getModel(), cells).toArray());
				
				final SchemeMarqueeHandler marquee = (SchemeMarqueeHandler)graph.getMarqueeHandler();
				try {
					if (marquee.pane instanceof SchemeTabbedPane) {
						SchemeResource res = ((SchemeTabbedPane)marquee.pane).getCurrentPanel().getSchemeResource();
						if (res.getCellContainerType() == SchemeResource.SCHEME) {
							graph.aContext.getDispatcher().firePropertyChange(new SchemeEvent(graph, res.getScheme().getId(), SchemeEvent.UPDATE_OBJECT));
						} else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
							graph.aContext.getDispatcher().firePropertyChange(new SchemeEvent(graph, res.getSchemeElement().getId(), SchemeEvent.UPDATE_OBJECT));
						}
					}
				} catch (ApplicationException e) {
					assert Log.errorMessage(e);
				}
			}
			clipboard.setContents(new GraphCellTransferable(alist), null);
		}
	}

	@Override
	public boolean importData(JComponent c, Transferable transferable) {
		if (c instanceof SchemeGraph) {
			Object data;
			Object[] cells;
			try {
				data = transferable.getTransferData(SchemeGraphTransferHandler.this.flavor);
				if (data instanceof List) {
					List v = (List)data;
					cells = (Object[]) v.get(0);
				} else {
					assert Log.debugMessage("Unsupported data for paste in SchemeGraph", Level.FINE); //$NON-NLS-1$ 
					return false;
				}
			} catch (UnsupportedFlavorException e) {
				assert Log.debugMessage("Unsupported object for paste in SchemeGraph", Level.WARNING); //$NON-NLS-1$
				return false;
			} catch (IOException e) {
				assert Log.errorMessage(e);
				return false;
			}
			
			final SchemeGraph graph = (SchemeGraph) c;
			final SchemeMarqueeHandler marquee = (SchemeMarqueeHandler)graph.getMarqueeHandler();
			if (marquee.pane instanceof SchemeTabbedPane) {
				SchemeResource res = ((SchemeTabbedPane)marquee.pane).getCurrentPanel().getSchemeResource();
				if (res.getCellContainerType() == SchemeResource.SCHEME) {
					try {
						Scheme newParent = res.getScheme(); 
						// just set new parent
						if (action == MOVE) {
							graph.setFromArchivedState(data);
							assingToNewScheme(cells, newParent);
							this.insertedCells = cells;
							action = 0;
							return true;
						} else if (action == COPY) {
							Map<DefaultGraphCell, DefaultGraphCell> clones = graph.copyFromArchivedState(data, null, false);
							this.insertedCells = clones.values().toArray();
						}
					} catch (ApplicationException e) {
						assert Log.errorMessage(e);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		if (c instanceof SchemeGraph && action != 0) {
			for (DataFlavor flavor1 : flavors) {
				if (flavor1.equals(SchemeGraphTransferHandler.this.flavor)) {
					return true;
				}
			}
		}
		return false;
	}

	public Object[] getInsertedCells() {
		Object[] cells = this.insertedCells;
		this.insertedCells = null;
		return cells;
	}

	private void assingToNewScheme(Object[] cells, Scheme scheme) {
		try {
			for (SchemeElement schemeElement : SchemeActions.getSchemeElements(cells)) {
				schemeElement.setParentScheme(scheme, false);
			}
			for (SchemeCableLink schemeCableLink : SchemeActions.getSchemeCableLinks(cells)) {
				schemeCableLink.setParentScheme(scheme, false);
			}
			for (SchemeLink schemeLink : SchemeActions.getSchemeLinks(cells)) {
				schemeLink.setParentScheme(scheme, false);
			}
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
		}
	}

	@Override
	public int getSourceActions(final JComponent c) {
		return COPY_OR_MOVE;
	}

	public class GraphCellTransferable implements Transferable {
		private List data;

		public GraphCellTransferable(final List alist) {
			this.data = alist;
		}

		public Object getTransferData(DataFlavor flavor1) throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor1)) {
				throw new UnsupportedFlavorException(flavor1);
			}
			return this.data;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { SchemeGraphTransferHandler.this.flavor };
		}

		public boolean isDataFlavorSupported(final DataFlavor flavor1) {
			if (SchemeGraphTransferHandler.this.flavor.equals(flavor1)) {
				return true;
			}
			return false;
		}
	}
}
