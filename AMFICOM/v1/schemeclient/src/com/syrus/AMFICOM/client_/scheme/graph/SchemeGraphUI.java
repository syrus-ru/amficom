/*-
 * $Id: SchemeGraphUI.java,v 1.26 2005/10/21 16:46:20 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.logging.Level;

import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jgraph.JGraph;
import com.jgraph.graph.AbstractCellView;
import com.jgraph.graph.CellHandle;
import com.jgraph.graph.CellView;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphContext;
import com.jgraph.graph.GraphLayoutCache;
import com.jgraph.pad.GPGraphUI;
import com.jgraph.plaf.basic.BasicGraphUI;
import com.jgraph.plaf.basic.TransferHandler;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.PopupFactory;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortEdge;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.26 $, $Date: 2005/10/21 16:46:20 $
 * @module schemeclient
 */

public class SchemeGraphUI extends GPGraphUI {
	private static final long serialVersionUID = 2929624792764978924L;
	private Object selected;
	
	static {
		BasicGraphUI.MAXCELLS = 2;
	}
	
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

	@Override
	protected TransferHandler createTransferHandler() {
		return new SchemeGraphTransferHandler();
	}
	
	class SchemeGraphDropTargetListener extends BasicGraphUI.GraphDropTargetListener {
		private static final long serialVersionUID = 6170350766676553728L;

		SchemeGraphDropTargetListener() {
			DropTarget dt = new DropTarget(SchemeGraphUI.this.graph, this);
			dt.setActive(true);
		}

		@Override
		public void drop(DropTargetDropEvent e) {
			Point p = e.getLocation();
			DataFlavor[] df = e.getCurrentDataFlavors();

			if (df[0].getHumanPresentableName().equals(LogicalTreeUI.TRANSFERABLE_OBJECTS)) {
				try {
					List transferableObjects = (List)e.getTransferable().getTransferData(df[0]);
					e.acceptDrop(DnDConstants.ACTION_MOVE);
					e.getDropTargetContext().dropComplete(true);
					if (transferableObjects.size() > 0) {
						final Object transferableObject = transferableObjects.iterator().next();
						if (transferableObject instanceof Identifiable) {
							Identifier transferable = ((Identifiable)transferableObject).getId();
							boolean editable = SchemeGraphUI.this.graph.isEditable();
							if (transferable.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE) {
								ApplicationContext aContext = ((SchemeGraph)SchemeGraphUI.this.graph).aContext; 
								aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, transferable, p, SchemeEvent.INSERT_SCHEME_CABLELINK));
							} else if (transferable.getMajor() == ObjectEntities.SCHEMEPROTOELEMENT_CODE) {
								SchemeGraph sgraph = (SchemeGraph)SchemeGraphUI.this.graph;
								JPopupMenu pop = PopupFactory.createProtoOpenPopup(sgraph.aContext, transferable, p);
								if (pop.getSubElements().length != 0) {
									pop.addSeparator();
									pop.add(PopupFactory.createCancelMenuItem());
									pop.show(SchemeGraphUI.this.graph, p.x, p.y);
								}
							} else if (transferable.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
								JPopupMenu pop = PopupFactory.createSEOpenPopup(((SchemeGraph)SchemeGraphUI.this.graph).aContext,
										transferable, p, SchemeEvent.INSERT_SCHEMEELEMENT, editable);
								if (pop.getSubElements().length != 0) {
									pop.addSeparator();
									pop.add(PopupFactory.createCancelMenuItem());
									pop.show(SchemeGraphUI.this.graph, p.x, p.y);
								}
							} else if (transferable.getMajor() == ObjectEntities.SCHEME_CODE) {
								JPopupMenu pop = PopupFactory.createSEOpenPopup(((SchemeGraph)SchemeGraphUI.this.graph).aContext,
										transferable, p, SchemeEvent.INSERT_SCHEME, editable);
								if (pop.getSubElements().length != 0) {
									pop.addSeparator();
									pop.add(PopupFactory.createCancelMenuItem());
									pop.show(SchemeGraphUI.this.graph, p.x, p.y);
								}
							}
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
			if (!e.isConsumed() && SwingUtilities.isLeftMouseButton(e))// && graph.isEnabled())
			{
				JGraph graph2 = SchemeGraphUI.this.graph;
				graph2.requestFocus();
				int s = graph2.getTolerance();
				Rectangle r = graph2.fromScreen(new Rectangle(e.getX() - s,
						e.getY() - s, 2 * s, 2 * s));
				Point point = graph2.snap(graph2.fromScreen(new Point(e.getPoint())));

				SchemeGraphUI.this.focus = (SchemeGraphUI.this.focus != null && SchemeGraphUI.this.focus.intersects(graph2.getGraphics(), r)) ? SchemeGraphUI.this.focus : null;
				this.cell = graph2.getNextViewAt(SchemeGraphUI.this.focus, point.x, point.y);

				
				if (this.cell != null && this.cell.getCell() instanceof DefaultGraphCell) {
//					if (this.cell.getCell() instanceof PortEdge) { // transfer focus to port
//						PortEdge portEdge = (PortEdge)this.cell.getCell();
//						Object sourceParent = ((DefaultPort)portEdge.getSource()).getParent();
//						if (sourceParent instanceof PortCell
//								|| sourceParent instanceof CablePortCell) {
//							this.cell = SchemeGraphUI.this.graph.getGraphLayoutCache().getMapping(sourceParent, true);
//							SchemeGraphUI.this.focus = this.cell;
//						} else {
//							Object targetParent = ((DefaultPort)portEdge.getTarget()).getParent();
//							if (targetParent instanceof PortCell
//									|| targetParent instanceof CablePortCell) {
//								this.cell = SchemeGraphUI.this.graph.getGraphLayoutCache().getMapping(targetParent, true);
//								SchemeGraphUI.this.focus = this.cell;
//							}
//						}
//					} else {
						
						Map attributes = ((DefaultGraphCell)this.cell.getCell()).getAttributes();
						if (attributes.containsKey(Constants.SELECTABLE) 
								&& attributes.get(Constants.SELECTABLE).equals(Boolean.FALSE)) {
							this.cell = SchemeGraphUI.this.focus;
							e.consume();
						}
//					}
				}
				
				if (SchemeGraphUI.this.focus == null)
					SchemeGraphUI.this.focus = this.cell;

				if (!e.isConsumed()) {
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
				}

				//Marquee Selection 
				if (// XXX unremark this to move cells without <CTRL> pressed						
						!e.isConsumed() &&   
						(!isToggleSelectionEvent(e) || SchemeGraphUI.this.focus == null)) {
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
			try {
				if (e != null && !e.isConsumed()) {
					if (handler == marquee) {
//						marquee.mouseReleased(e);
					}
					else if (handler == handle && handle != null)
						handle.mouseReleased(e);
					if (isDescendant(cell, focus) && e.getModifiers() != 0) {
						// Do not switch to parent if Special Selection
						cell = focus;
					}
					if (!e.isConsumed() && cell != null) {
						Object tmp = cell.getCell();
						boolean wasSelected = graph.isCellSelected(tmp);
						selectCellForEvent(tmp, e);
						focus = cell;
						if (wasSelected && graph.isCellSelected(tmp)) {
							Object root =
								((DefaultMutableTreeNode) tmp).getRoot();
							selectCellForEvent(root, e);
							focus = graphLayoutCache.getMapping(root, false);
						}
					}
//					if (handler != marquee)
						marquee.mouseReleased(e);
				}
			} finally {
				handler = null;
				cell = null;
			}
		}
		
	/*	public void mouseReleased(MouseEvent e) {
			try {
				if (e != null && !e.isConsumed()) {
					if (this.cell != null) {
						Object tmp = this.cell.getCell();
						boolean wasSelected = SchemeGraphUI.this.graph.isCellSelected(tmp);
						selectCellForEvent(tmp, e);
						SchemeGraphUI.this.focus = this.cell;
						if (wasSelected && SchemeGraphUI.this.graph.isCellSelected(tmp)) {
							Object root =
								((DefaultMutableTreeNode) tmp).getRoot();
							selectCellForEvent(root, e);
							SchemeGraphUI.this.focus = SchemeGraphUI.this.graphLayoutCache.getMapping(root, false);
						}
					}
					
					if (this.handler == SchemeGraphUI.this.marquee) {
						marquee.mouseReleased(e);
					} else if (this.handler == SchemeGraphUI.this.handle && SchemeGraphUI.this.handle != null) {
						SchemeGraphUI.this.handle.mouseReleased(e);
					}
					
					if (isDescendant(this.cell, SchemeGraphUI.this.focus) && e.getModifiers() != 0) {
						// Do not switch to parent if Special Selection
						this.cell = SchemeGraphUI.this.focus;
					}
					
				}
			} finally {
				this.handler = null;
				this.cell = null;
			}
		}*/
	}

	public class SchemeRootHandle extends BasicGraphUI.RootHandle {
		private static final long serialVersionUID = 6395641791384643788L;
		
		public SchemeRootHandle(GraphContext ctx) {
			super(ctx);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
//			if (SchemeGraph.getMode().equals(Constants.PATH_MODE)) {
//				Object cell = graph.getSelectionCell();
//				graph.getMarqueeHandler().mousePressed(e);
//			}
		}
		
		@Override // fixes bug when move cell in scale differ from 1
		public void mouseDragged(MouseEvent event) {
			boolean constrained = isConstrainedMoveEvent(event);
			SchemeGraph schemeGraph = (SchemeGraph)SchemeGraphUI.this.graph;
			Rectangle dirty = null;
			if (this.firstDrag && schemeGraph.isDoubleBuffered() && this.cachedBounds == null) {
				long st = System.currentTimeMillis();
				initOffscreen();
				Log.debugMessage("Init offline graphUI took " + (System.currentTimeMillis() - st) + "ms", Level.FINEST);
				this.firstDrag = false;
			}

			if (event != null && !event.isConsumed()) {
				GraphLayoutCache layoutCache = SchemeGraphUI.this.graphLayoutCache;
				
				if (this.activeHandle != null) {// Paint Active Handle
					this.activeHandle.mouseDragged(event);
				} // Invoke Mouse Dragged
				else if (this.start != null) { // Move Cells
					
					Graphics g = (this.offgraphics != null) ? this.offgraphics : schemeGraph.getGraphics();
					Point point = new Point(event.getPoint());
					point.translate(-this._mouseToViewDelta_x, -this._mouseToViewDelta_y);
//					Point snapCurrent = schemeGraph.snap(schemeGraph.fromScreen(point));
					Point snapCurrent = schemeGraph.snap(point);
					Point current = snapCurrent;
					int thresh = schemeGraph.getMinimumMove();
					int dx = current.x - this.start.x;
					int dy = current.y - this.start.y;
					
					if (this.isMoving || Math.abs(dx) > thresh || Math.abs(dy) > thresh) {
						boolean overlayed = false;
						this.isMoving = true;
						if (this.disconnect == null && schemeGraph.isDisconnectOnMove())
							this.disconnect = this.context.disconnect(
									layoutCache.getAllDescendants(this.views));
						// Constrained movement
						if (constrained && this.cachedBounds == null) {
							int totDx = current.x - this.start.x;
							int totDy = current.y - this.start.y;
							if (Math.abs(totDx) < Math.abs(totDy)) {
								dx = 0;
								dy = totDy;
							} else {
								dx = totDx;
								dy = 0;
							}
						} else {
							dx = current.x - this.snapLast.x;
							dy = current.y - this.snapLast.y;
						}
//						double scale = schemeGraph.getScale();
						dx = schemeGraph.snap(dx);
						dy = schemeGraph.snap(dy);
						
						g.setColor(schemeGraph.getForeground());
						g.setXORMode(schemeGraph.getBackground());
						// Start Drag and Drop
						if (schemeGraph.isDragEnabled() && !this.isDragging) {
							startDragging(event);
						}
						if (dx != 0 || dy != 0) {
							if (!this.snapLast.equals(this.snapStart)
								&& (this.offscreen != null || !this.blockPaint)) {
								overlay(g);
								overlayed = true;
							}
							this.isContextVisible = !event.isControlDown() || !schemeGraph.isCloneable();
							this.blockPaint = false;
							if (this.offscreen != null) {
								dirty = schemeGraph.toScreen(
										AbstractCellView.getBounds(this.views));
								Rectangle t = schemeGraph.toScreen(
										AbstractCellView.getBounds(this.contextViews));
								if (t != null) {
									dirty.add(t);
								}
							}
							if (constrained && this.cachedBounds == null) {
								//Reset Initial Positions
								CellView[] all = layoutCache.getAllDescendants(this.views);
								for (int i = 0; i < all.length; i++) {
									CellView orig = layoutCache.getMapping(
											all[i].getCell(), false);
									Map attr = orig.getAllAttributes();
									all[i].setAttributes(GraphConstants.cloneMap(attr));
									all[i].refresh(false);
								}
							}
							if (this.cachedBounds != null) {
								this.cachedBounds.translate(dx, dy);
//										schemeGraph.snap((int) (dy * scale)));
							}
							else {
								// Translate
								GraphLayoutCache.translateViews(this.views, dx, dy);
								
								if (this.views != null) {
									layoutCache.update(this.views);
								}
								if (this.contextViews != null) {
									layoutCache.update(this.contextViews);
								}
							}
							if (!snapCurrent.equals(this.snapStart)) {
								overlay(g);
								overlayed = true;
							}
							if (constrained) {
								this.last = new Point(this.start);
							}
							this.last.translate(
									schemeGraph.snap(schemeGraph.toScreen(dx)),
									schemeGraph.snap(schemeGraph.toScreen(dy)));
							// It is better to translate <code>last<code> by a scaled dx/dy
							// instead of making it to be the <code>current<code> (as in prev version),
							// so that the view would be catching up with a mouse pointer
							this.snapLast = snapCurrent;
							if (overlayed && this.offscreen != null) {
								dirty.add(schemeGraph.toScreen(
										AbstractCellView.getBounds(this.views)));
								Rectangle t = schemeGraph.toScreen(
										AbstractCellView.getBounds(this.contextViews));
								if (t != null) {
									dirty.add(t);
								}
								dirty.grow(2, 2);
								int sx1 = Math.max(0, dirty.x);
								int sy1 = Math.max(0, dirty.y);
								int sx2 = sx1 + dirty.width;
								int sy2 = sy1 + dirty.height;
								if (this.isDragging) // JDK BUG!
									return;
								schemeGraph.getGraphics().drawImage(this.offscreen, 
										sx1, sy1, sx2, sy2, sx1, sy1, sx2, sy2, schemeGraph);
							}
						}
					} // end if (isMoving or ...)
				} // end if (start != null)
			} else if (event == null)
				schemeGraph.repaint();
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
							Point tmp = graph.snap(SchemeGraphUI.this.graph.fromScreen(new Point(dx, dy)));
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
			super.mouseReleased(event);
		}
	}

//	@Override
	@Override
	protected void paintBackground(Graphics g) {
//		super.paintBackground(g);

		if (this.graph instanceof SchemeGraph) {
			int w = this.graph.getPreferredSize().width;
			int h = this.graph.getPreferredSize().height;

			int gs = ((SchemeGraph)this.graph).toScreen((int) (this.graph.getGridSize() * this.graph.getScale()));
			if (((SchemeGraph) this.graph).isBorderVisible()) {
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
			if (this.graph.isGridVisible()) {
				paintGrid(gs, g, this.graph.getPreferredSize());
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

	protected void paintGrid(int gs, Graphics g, Dimension r) {
		
		if (gs > 0) {
			int w = r.width;
			int h = r.height;

			Rectangle r1 = this.graph.getVisibleRect();
			int x0 = (r1.x / gs + 1) * gs; // - r1.x
			int y0 = (r1.y / gs + 1) * gs;
			int xe = r1.x + r1.width;
			int ye = r1.y + r1.height;
			if (this.graph instanceof SchemeGraph) {
//				gs = ((SchemeGraph)this.graph).toScreen(gs);
				if (((SchemeGraph) this.graph).isBorderVisible()) {
					x0 = Math.max(x0, 10 * gs);
					y0 = Math.max(y0, 2 * gs);
					xe = Math.min(xe, (w / gs - 2) * gs);
					ye = Math.min(ye, (h / gs - 2) * gs);
				}
			} else {
				gs = (int)(this.graph.getScale() * gs);
			}
//			g.setColor(this.graph.getGridColor());
			g.setColor(Color.lightGray);
			
			for (int x = x0; x <= xe; x += gs)
				for (int y = y0; y <= ye; y += gs)
					g.drawLine(x, y, x, y);
		}
	} 
}
