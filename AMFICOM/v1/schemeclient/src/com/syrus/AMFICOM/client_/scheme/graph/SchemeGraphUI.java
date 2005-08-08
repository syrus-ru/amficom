/*-
 * $Id: SchemeGraphUI.java,v 1.13 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jgraph.JGraph;
import com.jgraph.graph.CellHandle;
import com.jgraph.graph.CellView;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphContext;
import com.jgraph.graph.GraphLayoutCache;
import com.jgraph.pad.GPGraphUI;
import com.jgraph.plaf.basic.BasicGraphUI;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortEdge;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemeGraphUI extends GPGraphUI {
	private static final long serialVersionUID = 2929624792764978924L;
	private Object selected;
	
	@Override
	public boolean isConstrainedMoveEvent(MouseEvent event) {
		/*	if (event != null)
		 return event.isShiftDown();
		 else*/
		return false;
	}

	@Override
	public CellHandle createHandle(GraphContext context) {
		if (context != null && !context.isEmpty() && this.graph.isEnabled())
			return new SchemeRootHandle(context);
		return null;
	}

	@Override
	protected MouseListener createMouseListener() {
		return new SchemeMouseHandler();
	}

	class SchemeGraphDropTargetListener extends BasicGraphUI.GraphDropTargetListener {
		private static final long serialVersionUID = 6170350766676553728L;

		SchemeGraphDropTargetListener() {
			DropTarget dt = new DropTarget(SchemeGraphUI.this.graph, this);
			dt.setActive(true);
		}

		@Override
		public void drop(DropTargetDropEvent e) {
			//			Point p = e.getLocation();
			DataFlavor[] df = e.getCurrentDataFlavors();

			if (df[0].getHumanPresentableName().equals(LogicalTreeUI.TRANSFERABLE_OBJECTS)) {
				try {
					List transferableObjects = (List)e.getTransferable().getTransferData(df[0]);
					e.acceptDrop(DnDConstants.ACTION_MOVE);
					e.getDropTargetContext().dropComplete(true);
					if (transferableObjects.size() > 0) {
						final Object transferable = transferableObjects.iterator().next();
						long actionType = 0;
						if (transferable instanceof SchemeProtoElement) {
							actionType = SchemeEvent.INSERT_PROTOELEMENT;
						} else if (transferable instanceof SchemeElement) {
							actionType = SchemeEvent.INSERT_SCHEMEELEMENT;
						} else if (transferable instanceof Scheme) {
							actionType = SchemeEvent.INSERT_SCHEME;
						}
						if (actionType != 0) {
							final long actionType1 = actionType;
							JPopupMenu pop = new JPopupMenu();
							JMenuItem menu1 = new JMenuItem(new AbstractAction() {
								private static final long serialVersionUID = 7254016041683457753L;

								public void actionPerformed(ActionEvent ev) {
									((SchemeGraph) SchemeGraphUI.this.graph).aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, transferable, actionType1));									
								}
							});
							menu1.setText(LangModelGraph.getString("insert")); //$NON-NLS-1$
							pop.add(menu1);
							JMenuItem menu2 = new JMenuItem(new AbstractAction() {
								private static final long serialVersionUID = 1023861610666047648L;

								public void actionPerformed(ActionEvent ev) {
									long actionType2;
									if (actionType1 == SchemeEvent.INSERT_PROTOELEMENT)
										actionType2 = SchemeEvent.OPEN_PROTOELEMENT;
									else if (actionType1 == SchemeEvent.INSERT_SCHEMEELEMENT)
										actionType2 = SchemeEvent.OPEN_SCHEMEELEMENT;
									else
										actionType2 = SchemeEvent.OPEN_SCHEME;
									((SchemeGraph) SchemeGraphUI.this.graph).aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, transferable, actionType2));									
								}
							});
							menu2.setText(LangModelGraph.getString("open")); //$NON-NLS-1$
							pop.add(menu2);
							JMenuItem menu3 = new JMenuItem();
							menu3.setText(LangModelGraph.getString("cancel")); //$NON-NLS-1$
							pop.addSeparator();
							pop.add(menu3);
							
							pop.show(SchemeGraphUI.this.graph, e.getLocation().x, e.getLocation().y);
						}
					}
				} catch (UnsupportedFlavorException ex) {
					e.getDropTargetContext().dropComplete(false);
				} catch (IOException ex) {
					e.getDropTargetContext().dropComplete(false);
				}
			}

		}
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		DropTarget dropTarget = this.graph.getDropTarget();
		try {
			if (dropTarget != null) {
				this.defaultDropTargetListener = new SchemeGraphDropTargetListener();
				dropTarget.addDropTargetListener(this.defaultDropTargetListener);
			}
		} catch (TooManyListenersException tmle) {
			// should not happen... swing drop target is multicast
		}
	}

	public class SchemeMouseHandler extends BasicGraphUI.MouseHandler {
		private static final long serialVersionUID = 4087747949953155093L;

		@Override
		public void mousePressed(MouseEvent e) {
			this.handler = null;
			if (!e.isConsumed())// && graph.isEnabled())
			{
				JGraph graph2 = SchemeGraphUI.this.graph;
				graph2.requestFocus();
				int s = graph2.getTolerance();
				Rectangle r = graph2.fromScreen(new Rectangle(e.getX() - s,
						e.getY() - s, 2 * s, 2 * s));
				Point point = graph2.fromScreen(new Point(e.getPoint()));

				SchemeGraphUI.this.focus = (SchemeGraphUI.this.focus != null && SchemeGraphUI.this.focus.intersects(graph2.getGraphics(), r)) ? SchemeGraphUI.this.focus : null;
				this.cell = graph2.getNextViewAt(SchemeGraphUI.this.focus, point.x, point.y);

				if (SchemeGraphUI.this.focus == null)
					SchemeGraphUI.this.focus = this.cell;

				completeEditing();
				if (!isForceMarqueeEvent(e)) {
					if (e.getClickCount() == graph2.getEditClickCount() && SchemeGraphUI.this.focus != null
							&& SchemeGraphUI.this.focus.isLeaf() && SchemeGraphUI.this.focus.getParentView() == null) {
						// Start Editing
						handleEditTrigger(SchemeGraphUI.this.focus.getCell());
						e.consume();
						this.cell = null;
					} else if (!isToggleSelectionEvent(e)) {
						// Immediate Selection
						if (SchemeGraphUI.this.handle != null) {
							SchemeGraphUI.this.handle.mousePressed(e);
							this.handler = SchemeGraphUI.this.handle;
						}
						if (!e.isConsumed() && this.cell != null && !graph2.isCellSelected(this.cell)) {
							selectCellForEvent(this.cell.getCell(), e);
							SchemeGraphUI.this.focus = this.cell;
							if (SchemeGraphUI.this.handle != null) {
								SchemeGraphUI.this.handle.mousePressed(e);
								this.handler = SchemeGraphUI.this.handle;
							}
							this.cell = null;
						}
					}
				}

				//Marquee Selection
				if (!e.isConsumed() && (!isToggleSelectionEvent(e) || SchemeGraphUI.this.focus == null)) {
					if (SchemeGraphUI.this.marquee != null) {
						SchemeGraphUI.this.marquee.mousePressed(e);
						this.handler = SchemeGraphUI.this.marquee;
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (SchemeGraphUI.this.graph.isEditable())
				super.mouseDragged(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			((SchemeGraph)SchemeGraphUI.this.graph).selectionNotify();
		}
	}

	public class SchemeRootHandle extends BasicGraphUI.RootHandle {
		private static final long serialVersionUID = 6395641791384643788L;

		public SchemeRootHandle(GraphContext ctx) {
			super(ctx);
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			try {
				if (event != null && !event.isConsumed()) {
					if (this.activeHandle != null) {
						this.activeHandle.mouseReleased(event);
						this.activeHandle = null;
					} else if (this.isMoving && !event.getPoint().equals(this.start)) {
						if (this.cachedBounds != null) {
							int dx = event.getX() - this.start.x;
							int dy = event.getY() - this.start.y;
							Point tmp = SchemeGraphUI.this.graph.fromScreen(SchemeGraphUI.this.graph.snap(new Point(dx, dy)));
							GraphLayoutCache.translateViews(this.views, tmp.x, tmp.y);
						}
						CellView[] all = SchemeGraphUI.this.graphLayoutCache.getAllDescendants(this.views);
						
						// TODO make graph cells clonable
						/*
						 if (event.isControlDown() && graph.isCloneable()) { // Clone Cells
						 Object[] cells = graph.getDescendants(context.getCells());
						 ConnectionSet cs = ConnectionSet.create(graphModel, cells, false);
						 cs.addConnections(all);
						 Map attributes = GraphConstants.createAttributes(all, null);
						 insertCells(context.getCells(), attributes, cs, true, 0, 0);
						 } else */
						if (SchemeGraphUI.this.graph.isMoveable()) {
							Map attributes = GraphConstants.createAttributes(all, null);
							SchemeGraphUI.this.graph.getGraphLayoutCache().edit(attributes, this.disconnect, null,
									null);
						}
						event.consume();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.isDragging = false;
				this.disconnect = null;
				this.offscreen = null;
				this.firstDrag = true;
				this.start = null;
			}
			super.mouseDragged(event);
		}
	}

	@Override
	protected void paintBackground(Graphics g) {
		super.paintBackground(g);

		if (this.graph instanceof SchemeGraph) {
			int w = this.graph.getPreferredSize().width;
			int h = this.graph.getPreferredSize().height;

			if (((SchemeGraph) this.graph).isBorderVisible()) {
				int gs = (int) (this.graph.getGridSize() * this.graph.getScale());
				if (gs > 0) {

					g.setColor(Color.lightGray);
					int x0, xe, y0, ye;
					x0 = 10 * gs;
					y0 = 2 * gs;
					xe = (w / gs - 2) * gs;
					ye = (h / gs - 2) * gs;

					g.drawLine(x0, y0, x0, ye);
					g.drawLine(x0, y0, xe, y0);
					g.drawLine(xe, y0, xe, ye);
					g.drawLine(x0, ye, xe, ye);
				}
			}
		}
	}

	@Override
	protected boolean startEditing(Object cell, MouseEvent event) {
		this.selected = cell;
		boolean b;
		try {
			b = super.startEditing(cell, event);
		} catch (Exception ex) {
			b = false;
		}
		return b;
	}

	@Override
	protected void completeEditing() {
		super.completeEditing();
		if (this.selected instanceof PortEdge) {
			String name = (String) ((DefaultEdge) this.selected).getUserObject();
			DefaultPort p = (DefaultPort) ((DefaultEdge) this.selected).getTarget();
			if (p != null) {
				if (p.getParent() instanceof PortCell) {
					PortCell port = (PortCell) p.getParent();
					if (port.getSchemePort() != null)
						port.getSchemePort().setName(name);
				} else if (p.getParent() instanceof CablePortCell) {
					CablePortCell port = (CablePortCell) p.getParent();
					if (port.getSchemeCablePort() != null)
						port.getSchemeCablePort().setName(name);
				}
			}
		} else if (this.selected instanceof DefaultLink) {
			DefaultLink link = (DefaultLink) this.selected;
			if (link.getSchemeLink() != null)
				link.getSchemeLink().setName((String) link.getUserObject());
		} else if (this.selected instanceof DefaultCableLink) {
			DefaultCableLink link = (DefaultCableLink) this.selected;
			if (link.getSchemeCableLink() != null)
				link.getSchemeCableLink().setName((String) link.getUserObject());
		}
	}

	@Override
	protected void paintGrid(int gs, Graphics g, Rectangle r) {
		
		if (gs > 0) {
			int w = this.graph.getPreferredSize().width;
			int h = this.graph.getPreferredSize().height;

			gs = (int) (gs * this.graph.getScale());
			Rectangle r1 = this.graph.getVisibleRect();
			int x0 = (r1.x / gs + 1) * gs; // - r1.x
			int y0 = (r1.y / gs + 1) * gs;
			int xe = r1.x + r1.width;
			int ye = r1.y + r1.height;
			if (this.graph instanceof SchemeGraph) {
				if (((SchemeGraph) this.graph).isBorderVisible()) {
					x0 = Math.max(x0, 10 * gs);
					y0 = Math.max(y0, 2 * gs);
					xe = Math.min(xe, (w / gs - 2) * gs);
					ye = Math.min(ye, (h / gs - 2) * gs);
				}
			}
			g.setColor(this.graph.getGridColor());
			
			for (int x = x0; x <= xe; x += gs)
				for (int y = y0; y <= ye; y += gs)
					g.drawLine(x, y, x, y);
		}
	} 
}
