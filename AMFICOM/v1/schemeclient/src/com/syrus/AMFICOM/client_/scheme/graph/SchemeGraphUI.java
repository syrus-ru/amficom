/*-
 * $Id: SchemeGraphUI.java,v 1.5 2005/06/17 12:44:17 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import com.jgraph.graph.*;
import com.jgraph.pad.GPGraphUI;
import com.jgraph.plaf.basic.BasicGraphUI;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/17 12:44:17 $
 * @module schemeclient_v1
 */

public class SchemeGraphUI extends GPGraphUI {
	private Object selected;
	
	public boolean isConstrainedMoveEvent(MouseEvent event) {
		/*	if (event != null)
		 return event.isShiftDown();
		 else*/
		return false;
	}

	public CellHandle createHandle(GraphContext context) {
		if (context != null && !context.isEmpty() && graph.isEnabled())
			return new SchemeRootHandle(context);
		return null;
	}

	protected MouseListener createMouseListener() {
		return new SchemeMouseHandler();
	}

	class SchemeGraphDropTargetListener extends BasicGraphUI.GraphDropTargetListener {
		SchemeGraphDropTargetListener() {
			DropTarget dt = new DropTarget(graph, this);
			dt.setActive(true);
		}

		public void drop(DropTargetDropEvent e) {
			//			Point p = e.getLocation();
			DataFlavor[] df = e.getCurrentDataFlavors();

			if (df[0].getHumanPresentableName().equals("ProtoElementLabel")) {
				try {
					SchemeProtoElement proto = (SchemeProtoElement) e.getTransferable()
							.getTransferData(df[0]);
					e.acceptDrop(DnDConstants.ACTION_MOVE);
					e.getDropTargetContext().dropComplete(true);
					((SchemeGraph) graph).aContext.getDispatcher().firePropertyChange(
							new SchemeEvent(this, proto, SchemeEvent.OPEN_PROTOELEMENT));
				} catch (UnsupportedFlavorException ex) {
					e.getDropTargetContext().dropComplete(false);
				} catch (IOException ex) {
					e.getDropTargetContext().dropComplete(false);
				}
			}

		}
	}

	protected void installListeners() {
		super.installListeners();
		DropTarget dropTarget = graph.getDropTarget();
		try {
			if (dropTarget != null) {
				defaultDropTargetListener = new SchemeGraphDropTargetListener();
				dropTarget.addDropTargetListener(defaultDropTargetListener);
			}
		} catch (TooManyListenersException tmle) {
			// should not happen... swing drop target is multicast
		}
	}

	public class SchemeMouseHandler extends BasicGraphUI.MouseHandler {
		public void mousePressed(MouseEvent e) {
			handler = null;
			if (!e.isConsumed())// && graph.isEnabled())
			{
				graph.requestFocus();
				int s = graph.getTolerance();
				Rectangle r = graph.fromScreen(new Rectangle(e.getX() - s,
						e.getY() - s, 2 * s, 2 * s));
				Point point = graph.fromScreen(new Point(e.getPoint()));

				focus = (focus != null && focus.intersects(graph.getGraphics(), r)) ? focus : null;
				cell = graph.getNextViewAt(focus, point.x, point.y);

				if (focus == null)
					focus = cell;

				completeEditing();
				if (!isForceMarqueeEvent(e)) {
					if (e.getClickCount() == graph.getEditClickCount() && focus != null
							&& focus.isLeaf() && focus.getParentView() == null) {
						// Start Editing
						handleEditTrigger(focus.getCell());
						e.consume();
						cell = null;
					} else if (!isToggleSelectionEvent(e)) {
						// Immediate Selection
						if (handle != null) {
							handle.mousePressed(e);
							handler = handle;
						}
						if (!e.isConsumed() && cell != null && !graph.isCellSelected(cell)) {
							selectCellForEvent(cell.getCell(), e);
							focus = cell;
							if (handle != null) {
								handle.mousePressed(e);
								handler = handle;
							}
							cell = null;
						}
					}
				}

				//Marquee Selection
				if (!e.isConsumed() && (!isToggleSelectionEvent(e) || focus == null)) {
					if (marquee != null) {
						marquee.mousePressed(e);
						handler = marquee;
					}
				}
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (graph.isEditable())
				super.mouseDragged(e);
		}
		
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			((SchemeGraph)graph).selectionNotify();
		}
	}

	public class SchemeRootHandle extends BasicGraphUI.RootHandle {
		public SchemeRootHandle(GraphContext ctx) {
			super(ctx);
		}

		public void mouseReleased(MouseEvent event) {
			try {
				if (event != null && !event.isConsumed()) {
					if (activeHandle != null) {
						activeHandle.mouseReleased(event);
						activeHandle = null;
					} else if (isMoving && !event.getPoint().equals(start)) {
						if (cachedBounds != null) {
							int dx = event.getX() - start.x;
							int dy = event.getY() - start.y;
							Point tmp = graph.fromScreen(graph.snap(new Point(dx, dy)));
							GraphLayoutCache.translateViews(views, tmp.x, tmp.y);
						}
						CellView[] all = graphLayoutCache.getAllDescendants(views);
						
						// TODO make graph cells clonable
						/*
						 if (event.isControlDown() && graph.isCloneable()) { // Clone Cells
						 Object[] cells = graph.getDescendants(context.getCells());
						 ConnectionSet cs = ConnectionSet.create(graphModel, cells, false);
						 cs.addConnections(all);
						 Map attributes = GraphConstants.createAttributes(all, null);
						 insertCells(context.getCells(), attributes, cs, true, 0, 0);
						 } else */
						if (graph.isMoveable()) {
							Map attributes = GraphConstants.createAttributes(all, null);
							graph.getGraphLayoutCache().edit(attributes, disconnect, null,
									null);
						}
						event.consume();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isDragging = false;
				disconnect = null;
				offscreen = null;
				firstDrag = true;
				start = null;
			}
			super.mouseDragged(event);
		}
	}

	protected void paintBackground(Graphics g) {
		super.paintBackground(g);

		if (graph instanceof SchemeGraph) {
			int w = graph.getPreferredSize().width;
			int h = graph.getPreferredSize().height;

			if (((SchemeGraph) graph).isBorderVisible()) {
				int gs = (int) (graph.getGridSize() * graph.getScale());
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

	protected boolean startEditing(Object cell, MouseEvent event) {
		this.selected = cell;
		boolean b = false;
		try {
			b = super.startEditing(cell, event);
		} catch (Exception ex) {
		}

		return b;
	}

	protected void completeEditing() {
		super.completeEditing();
		if (selected instanceof PortEdge) {
			String name = (String) ((DefaultEdge) selected).getUserObject();
			DefaultPort p = (DefaultPort) ((DefaultEdge) selected).getTarget();
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
		} else if (selected instanceof DefaultLink) {
			DefaultLink link = (DefaultLink) selected;
			if (link.getSchemeLink() != null)
				link.getSchemeLink().setName((String) link.getUserObject());
		} else if (selected instanceof DefaultCableLink) {
			DefaultCableLink link = (DefaultCableLink) selected;
			if (link.getSchemeCableLink() != null)
				link.getSchemeCableLink().setName((String) link.getUserObject());
		}
	}

	protected void paintGrid(int gs, Graphics g, Rectangle r) {
		if (gs > 0) {
			int w = graph.getPreferredSize().width;
			int h = graph.getPreferredSize().height;

			gs = (int) (gs * graph.getScale());

			int x0 = 0;
			int y0 = 0;
			int xe = graph.getWidth();
			int ye = graph.getHeight();
			if (graph instanceof SchemeGraph) {
				if (((SchemeGraph) graph).isBorderVisible()) {
					x0 = 10 * gs;
					y0 = 2 * gs;
					xe = (w / gs - 2) * gs;
					ye = (h / gs - 2) * gs;
				}
			}

			g.setColor(graph.getGridColor());
			for (int x = x0; x <= xe; x += gs)
				for (int y = y0; y <= ye; y += gs)
					g.drawLine(x, y, x, y);
		}
	}
}
