/*-
 * $Id: SchemeMarqueeHandler.java,v 1.39 2005/10/31 12:30:29 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;

import com.jgraph.graph.BasicMarqueeHandler;
import com.jgraph.graph.CellView;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphUndoManager;
import com.jgraph.graph.PortView;
import com.jgraph.plaf.GraphUI;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.PopupFactory;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.39 $, $Date: 2005/10/31 12:30:29 $
 * @module schemeclient
 */

public class SchemeMarqueeHandler extends BasicMarqueeHandler {
	UgoTabbedPane pane;
	
	public SchemeMarqueeHandler (UgoTabbedPane pane) {
		this.pane = pane;
	}
	
	// from GPMarqueeHandler
	public transient JToggleButton s = new JToggleButton(); //marqueeTool
	public transient JToggleButton r = new JToggleButton(); //rectangleTool
	public transient JToggleButton c = new JToggleButton(); //ellipseTool
	public transient JToggleButton t = new JToggleButton(); //textTool
	public transient JToggleButton i = new JToggleButton(); //iconTool
	public transient JToggleButton l = new JToggleButton(); //lineTool
	public transient JToggleButton e = new JToggleButton(); //linkTool
	public transient JToggleButton z = new JToggleButton(); //zoomTool
	
	public transient JToggleButton ce = new JToggleButton(); //cableTool
	public transient JToggleButton dev = new JToggleButton(); //deviceTool
	public transient JToggleButton p1 = new JToggleButton(); //portOutKey
	public transient JToggleButton p2 = new JToggleButton(); //portInKey
	public transient JToggleButton s_cell = new JToggleButton();
	public transient JButton gr = new JButton(); //groupKey
	public transient JButton gr2 = new JButton(); //groupSEKey
	public transient JButton gr3 = new JButton(); //rack
	public transient JButton ugr = new JButton(); //ungroupKey
	public transient JButton undo = new JButton(); //undoKey
	public transient JButton redo = new JButton(); //redoKey
	public transient JToggleButton zb = new JToggleButton(); //zoomBoxKey
	public transient JButton zi = new JButton(); //zoomInKey
	public transient JButton zo = new JButton(); //zoomOutKey
	public transient JButton za = new JButton(); //zoomActualKey
	public transient JButton del = new JButton(); //deleteKey
	public transient JButton ugo = new JButton(); //createTopLevelElementKey
	public transient JButton scheme_ugo = new JButton(); //createTopLevelSchemeKey
	public transient JButton bp = new JButton(); //blockPortKey
	public transient JButton bSize = new JButton(); //backgroundSize
	public transient JToggleButton rackButt = new JToggleButton(); //RACK_MODE
	public transient JToggleButton pathButt = new JToggleButton(); //PATH_MODE
	public transient JToggleButton linkButt = new JToggleButton(); //LINK_MODE
	public transient JButton topModeButt = new JButton(); //TOP_LEVEL_SCHEME_MODE

	// from GPMarqueeHandler
	protected Point start, current;
	protected Rectangle bounds;
	protected PortView port, firstPort, lastPort;
	
	protected Rectangle devBounds;
	protected Point settingPoint;
	private static final int CROSS_SIZE = 4;
	
	private static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private static final Cursor CROSSHAIR_CURSOR = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	
//	private transient SchemeProtoElement settingProto = null;
	//	private transient boolean sendEvents = true;

	// Update Undo/Redo Button State based on Undo Manager
	public void updateHistoryButtons(GraphUndoManager undoManager) {
		SchemeGraph graph = this.pane.getGraph();
		boolean b1 = undoManager.canUndo(graph.getGraphLayoutCache());
		this.undo.setEnabled(b1);
		this.redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
		
		this.pane.setGraphChanged(b1);
	}
	
	/* Return true if this handler should be preferred over other handlers. */
	@Override
	public boolean isForceMarqueeEvent(MouseEvent event) {
		return !this.s.isSelected()
			|| isPopupTrigger(event)
			|| super.isForceMarqueeEvent(event);
	}
	
	protected boolean isPopupTrigger(MouseEvent event) {
		return SwingUtilities.isRightMouseButton(event) && !event.isShiftDown();
	}

	public void updateButtonsState(Object[] cells) {
		SchemeGraph graph = this.pane.getGraph();
		this.ugo.setEnabled((graph.getAll().length == 0) ? false : true);
		this.del.setEnabled(cells.length != 0 && !GraphActions.hasGroupedParent(cells[0]));
		this.ugr.setEnabled(false);
		this.gr.setEnabled(false);
		this.gr2.setEnabled(false);
		this.gr3.setEnabled(false);
		this.p1.setEnabled(false);
		this.p2.setEnabled(false);
		this.bp.setEnabled(false);

		int ports = 0;
		int cablePorts = 0;
		int devices = 0;
		int racks = 0;
		DeviceCell device = null;
		int groups = 0;
		for (int j = 0; j < cells.length; j++) {
			if (cells[j] instanceof DeviceCell) {
				devices++;
				device = (DeviceCell) cells[j];
			} 
			else if (cells[j] instanceof DeviceGroup && !GraphActions.hasGroupedParent(cells[j])) {
				groups++;
			}
			else if (cells[j] instanceof Rack) {
				racks++;
			} 
			else if (cells[j] instanceof PortCell) {
				ports++;
			} 
			else if (cells[j] instanceof CablePortCell) {
				cablePorts++;
			}
		}
		if (groups == 0) {
			if (devices == 0) {
				if (ports + cablePorts == 1)
					this.bp.setEnabled(true);
			} 
			else if (devices == 1) {
				if (!GraphActions.hasGroupedParent(device)) {
					this.p1.setEnabled(true);
					this.p2.setEnabled(true);
					if (device.getChildCount() > 1) {
						this.gr.setEnabled(true);
						this.gr2.setEnabled(true);
					}
				}
			}
		} else if (groups == 1) {
			// ugo.setEnabled(true);
		}
		if (racks > 0 || groups > 0) {
			this.ugr.setEnabled(true);
		}
		if (groups > 1) {
			this.gr.setEnabled(true);
		}
		if (groups + racks > 1 && racks < 2) {
			this.gr3.setEnabled(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		SchemeGraph graph = (SchemeGraph)event.getSource();
		if (SwingUtilities.isLeftMouseButton(event)) {
			if (graph.isEditable()) {
				if (this.p1.isSelected() || this.p2.isSelected()) {
					createPort(graph, graph.snap(event.getPoint()));
					this.devBounds = null;
					this.settingPoint = null;
					graph.repaint();
					event.consume();
				} else if (this.ce.isSelected() && this.firstPort != null)
					this.start = graph.toScreen(this.firstPort.getLocation(null));
			} else if (!SchemeGraph.getMode().equals(Constants.PATH_MODE)) {
				graph.clearSelection();
			}
			
			// select SchemePath
			if (SchemeGraph.getMode().equals(Constants.PATH_MODE)) {
				Object cell = graph.getSelectionCell();
				if (this.pane instanceof SchemeTabbedPane) {
					
					PathElement pe = SchemeActions.getSelectedPathElement(cell);
					SchemePath path = pe != null ? pe.getParentPathOwner() : 
						SchemeResource.getSchemePath();
					
					if (path != null) {
						Notifier.notify(graph, graph.aContext, path);
					} else {
						try {
							Notifier.notify(graph, graph.aContext,
									((SchemeTabbedPane)this.pane).getCurrentPanel().getSchemeResource().getScheme());
						} catch (ApplicationException e) {
							Log.errorMessage(e);
						}
					}
					
					/*SortedSet<PathElement> pathElements = path != null ? path.getPathMembers() : null;
					 
					 SortedSet<Identifier> ids = null;
					 
					 if (pathElements != null) {
					 ids = new TreeSet<Identifier>();
					 for (PathElement pe1 : pathElements) {
					 ids.add(pe1.getAbstractSchemeElement().getId());
					 }
					 }
					 
					 // select path objects
					  if (pathElements != null) {
					  Object[] pathObjects = SchemeActions.getPathObjects(ids, graph);
					  graph.setSelectionCells(pathObjects);
					  if (path != null && ((SchemeTabbedPane)this.pane).getCurrentPanel().getSchemeResource() == null) {
					  Notifier.notify(graph, graph.aContext, path);
					  }
					  }
					  
					  for (ElementsPanel panel : ((SchemeTabbedPane)this.pane).getAllPanels()) {
					  panel.getSchemeResource().setSchemePath(path);
					  panel.getSchemeResource().setCashedPathMemberIds(ids);
					  }*/
					
					this.start = graph.snap(event.getPoint());
					this.firstPort = this.port;
					event.consume();
				}
			}
			
			// from GPMarqueeHandler
			if (!isPopupTrigger(event) && !event.isConsumed() && !this.s.isSelected()) {
				this.start = graph.snap(event.getPoint());
				this.firstPort = this.port;
				if (this.e.isSelected() && this.firstPort != null)
					this.start = graph.toScreen(this.firstPort.getLocation(null));
				event.consume();
			}
			if (!isPopupTrigger(event))
				super.mousePressed(event);
			else {
				boolean selected = false;
				Object[] cells = graph.getSelectionCells();
				for (int j = 0; j < cells.length && !selected; j++)
					selected = graph.getCellBounds(cells[j]).contains(event.getPoint());
				if (!selected)
					graph.setSelectionCell(graph.getFirstCellForLocation(event.getX(), event.getY()));
				event.consume();
			}
		} else {
			event.consume();
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		SchemeGraph graph = (SchemeGraph)event.getSource();
		if (!graph.isEditable())
			return;

		if (this.p1.isSelected() || this.p2.isSelected())
			event.consume();
		// from GPMarqueeHandler
		else if (!event.isConsumed() && !this.s.isSelected()) {
			Graphics g = graph.getGraphics();
			Color bg = graph.getBackground();
			Color fg = Color.black;
			g.setColor(fg);
			g.setXORMode(bg);
			overlay(graph, g);
			this.current = graph.snap(event.getPoint());
			if (this.e.isSelected() || this.l.isSelected() || this.ce.isSelected()) {
				this.port = getPortViewAt(graph, event.getX(), event.getY(), !event.isShiftDown());
				if (this.port != null) {
					Map map = graph.getModel().getAttributes(this.port.getCell());
					if (map != null) {
						if (!GraphConstants.isConnectable(map))
							this.port = null;
						else
							this.current = graph.toScreen(this.port.getLocation(null));
					}
				}
			}
			this.bounds = new Rectangle(this.start).union(new Rectangle(this.current));
			g.setColor(bg);
			g.setXORMode(fg);
			overlay(graph, g);
			event.consume();
		}
		super.mouseDragged(event);
	}
	
	public PortView getPortViewAt(SchemeGraph graph, int x, int y, boolean jump) {
		Point sp = graph.fromScreen(new Point(x, y));
		PortView portView = graph.getPortViewAt(sp.x, sp.y);
		// Shift Jumps to "Default" Port (child index 0)
		if (portView == null && jump) {
			Object cell = graph.getFirstCellForLocation(x, y);
			if (graph.isVertex(cell)) {
				Object firstChild = graph.getModel().getChild(cell, 0);
				CellView firstChildView =
					graph.getGraphLayoutCache().getMapping(firstChild, false);
				if (firstChildView instanceof PortView)
					portView = (PortView) firstChildView;
			}
		}
		return portView;
	}
		
	private DeviceCell getOnlySelectedDevice(SchemeGraph graph) {
		DeviceCell deviceCell = null;
		Object[] cells = graph.getSelectionCells();
		int counter = 0;
		for (int j = 0; j < cells.length; j++) {
			if (cells[j] instanceof DeviceCell) {
				deviceCell = (DeviceCell) cells[j];
				counter++;
			}
		}
		if (counter > 1)
			deviceCell = null;
		return deviceCell;
	}
	
	private IdlDirectionType getDirectionType(Rectangle dev_bounds, Point p) {
	if (p.y > dev_bounds.y && p.y < dev_bounds.y + dev_bounds.height - 1) {
			if (p.x < dev_bounds.x )
				return IdlDirectionType._IN;
			else if (p.x > dev_bounds.x + dev_bounds.width)
				return IdlDirectionType._OUT;
			else {
				Log.errorMessage("can't create PortCell in horizontal bounds of DeviceCell"); //$NON-NLS-1$
				return null;
			}
		}
		Log.errorMessage("can't create PortCell out of vertical bounds of DeviceCell"); //$NON-NLS-1$
		return null;
	}
	
	private void createPort(SchemeGraph graph, Point p) {
		DeviceCell deviceCell = getOnlySelectedDevice(graph);

		if (deviceCell != null) {
			/**
			 * @todo possibility to add ports to grouped element
			 */
			if (GraphActions.hasGroupedParent(deviceCell)) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
						LangModelGraph.getString("error_grouped_device"),  //$NON-NLS-1$
						LangModelGraph.getString("error"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				Log.errorMessage("can't create PortCell as DeviceCell has parent group"); //$NON-NLS-1$
				return;
			}
			if (deviceCell.getSchemeDeviceId() == null) {
				Log.errorMessage("can't create PortCell as DeviceCell has null SchemeDevice"); //$NON-NLS-1$
				return;
			}
			
			Map m = graph.getModel().getAttributes(deviceCell);
			Rectangle dev_bounds = GraphConstants.getBounds(m);	
			IdlDirectionType directionType = getDirectionType(dev_bounds, p);
			if (directionType == null)
				return;
			
			boolean isCable = !this.p1.isSelected();
			
			StorableObjectCondition condition = new TypicalCondition(
					isCable ? PortTypeKind._PORT_KIND_CABLE : PortTypeKind._PORT_KIND_SIMPLE,
					0, OperationSort.OPERATION_EQUALS,
					ObjectEntities.PORT_TYPE_CODE, PortTypeWrapper.COLUMN_KIND);
			Set types = Collections.EMPTY_SET;
			try {
				types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
			}
			if (types.isEmpty()) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
						LangModelGraph.getString("error_porttype_not_found"),  //$NON-NLS-1$
						LangModelGraph.getString("error"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			PortType type = (PortType)types.iterator().next();
		
			String name = String.valueOf(deviceCell.getChildCount());
			
			try {
				AbstractSchemePort schemePort;
				if (!isCable) { //port
					schemePort = SchemeObjectsFactory.createSchemePort(name, directionType, deviceCell.getSchemeDevice());
				} else {
					schemePort = SchemeObjectsFactory.createSchemeCablePort(name, directionType, deviceCell.getSchemeDevice());
				}
				schemePort.setPortType(type);
				
				Color color = SchemeActions.determinePortColor(schemePort, null);

				if (!isCable) { //port
					SchemeActions.createPort(graph, deviceCell, 
							graph.snap(graph.fromScreen(p)), name, directionType, color, schemePort.getId());
				} else {
					SchemeActions.createCablePort(graph, deviceCell, 
							graph.snap(graph.fromScreen(p)), name, directionType, color, schemePort.getId());
				}
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event == null) {
			super.mouseReleased(event);
			return;
		}
		
		SchemeGraph graph = (SchemeGraph)event.getSource();
		boolean notify = true;
				
		if (!graph.isEditable()) {
			graph.selectionNotify();
			event.consume();
		}
			
		if (SwingUtilities.isLeftMouseButton(event)) {
			if (event != null && !event.isConsumed() && this.bounds != null && !this.s.isSelected()) {
				graph.snap(graph.fromScreen(this.bounds));
//				this.bounds.x++;
//				this.bounds.y++;
				
				if (this.zb.isSelected()) {
					if (this.bounds.width != 0 && this.bounds.height != 0) {
						Rectangle visible = graph.fromScreen(graph.getVisibleRect());
						double sc = ((double)visible.width / (double)this.bounds.width + 
								(double)visible.height / (double)this.bounds.height) / 2;
						graph.setScale(graph.getScale() * sc);
						Dimension size = graph.getPreferredSize();
						graph.setPreferredSize(new Dimension((int) (size.width * sc), (int) (size.height * sc)));
						Rectangle bounds2 = new Rectangle(this.bounds);
						graph.toScreen(bounds2);
						graph.setLocation(-bounds2.x, -bounds2.y);
						if (graph.getScale() >= .5)
							graph.setGridVisible(graph.isGridVisibleAtActualSize());
					}
				} else if (this.dev.isSelected()) {
					
//					bounds.width += 2;
//					bounds.height += 2;
//					this.bounds.width ++;
//					this.bounds.height ++;
					try {
						SchemeDevice device = SchemeObjectsFactory.createSchemeDevice(Constants.DEVICE + System.currentTimeMillis());
						DeviceCell cell = SchemeActions.createDevice(graph, "", this.bounds, device.getId());  //$NON-NLS-1$
						cell.setSchemeDeviceId(device.getId());
					} catch (ApplicationException e1) {
						Log.errorMessage(e1);
					}
				} else if (this.r.isSelected()) {
					graph.addVertex("", this.bounds, false, Color.black); //$NON-NLS-1$
				}
				else if (this.c.isSelected())
					graph.addEllipse("", this.bounds); //$NON-NLS-1$
				else if (this.ce.isSelected()) {
					if (this.start == null || this.current == null) {
						event.consume();
					} else {
						StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
						Set types = Collections.EMPTY_SET;
						try {
							types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
						} catch (ApplicationException e1) {
							Log.errorMessage(e1);
						}
						if (types.isEmpty()) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
									LangModelGraph.getString("error_cablelinktype_not_found"), //$NON-NLS-1$
									LangModelGraph.getString("error"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						CableLinkType type = (CableLinkType)types.iterator().next();
						
						Scheme scheme = null;
						UgoPanel panel = this.pane.getCurrentPanel();
						if (panel instanceof SchemePanel) {
							try {
								scheme = ((SchemePanel)panel).getSchemeResource().getScheme();
							} catch (ApplicationException e2) {
								Log.errorMessage(e2);
							}
						}
						if (scheme != null) {
							try {
								SchemeCableLink link = SchemeObjectsFactory.createSchemeCableLink("TEMPORARY", scheme);
								link.setAbstractLinkTypeExt(type, LoginManager.getUserId(), false);
								try {
									DefaultCableLink cell = SchemeActions.createCableLink(graph,
											this.firstPort, this.port, graph.snap(graph.fromScreen(new Point(this.start))), 
											graph.snap(graph.fromScreen(new Point(this.current))), link.getId(), false);
									
									link.setName((String)cell.getUserObject());
									Notifier.notify(graph, this.pane.aContext, link);
								} catch (CreateObjectException e1) {
									Log.errorMessage(e1.getMessage());
									link.setParentScheme(null, false);
									JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
											LangModelGraph.getString("Error.create_cablelink"), //$NON-NLS-1$
											LangModelGraph.getString("error"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
								}
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						}
					}
				} else if (this.e.isSelected()) {
					if (this.start == null || this.current == null) {
						event.consume();
					} else {
						StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
						Set types = Collections.EMPTY_SET;
						try {
							types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
						} catch (ApplicationException e1) {
							Log.errorMessage(e1);
						}
						if (types.isEmpty()) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
									LangModelGraph.getString("error_linktype_not_found"), //$NON-NLS-1$
									LangModelGraph.getString("error"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						LinkType type = (LinkType)types.iterator().next();
						
						SchemeLink link;
						try {
							link = SchemeObjectsFactory.createSchemeLink("TEMPORARY");
							
							try {
								DefaultLink cell = SchemeActions.createLink(graph,
										this.firstPort, this.port, graph.snap(graph.fromScreen(new Point(this.start))), 
										graph.snap(graph.fromScreen(new Point(this.current))), link.getId(), false);
								
								link.setName((String)cell.getUserObject());
								link.setAbstractLinkType(type);
								
								UgoPanel panel = this.pane.getCurrentPanel();
								if (panel instanceof ElementsPanel) {
									SchemeResource res = ((ElementsPanel)panel).getSchemeResource();
									if (res.getCellContainerType() == SchemeResource.SCHEME) {
										final Scheme scheme = res.getScheme();
										if (scheme != null) {
											link.setParentScheme(scheme, false);
										}
									} else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
										final SchemeElement schemeElement = res.getSchemeElement();
										if (schemeElement != null) {
											link.setParentSchemeElement(schemeElement, false);
										}
									}
									else if (res.getCellContainerType() == SchemeResource.SCHEME_PROTO_ELEMENT) {
										final SchemeProtoElement schemeProtoElement = res.getSchemeProtoElement();
										if (schemeProtoElement != null) {
											link.setParentSchemeProtoElement(schemeProtoElement, false);
										}
									}
								}
								Notifier.notify(graph, this.pane.aContext, link);
							} catch (CreateObjectException e1) {
								Log.errorMessage(e1.getMessage());
								link.setParentSchemeProtoElement(null, false);
								JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
										LangModelGraph.getString("Error.create_link"), //$NON-NLS-1$
										LangModelGraph.getString("error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (ApplicationException e1) {
							Log.errorMessage(e1);
							return;
						}
					}
				} 
				else if (this.l.isSelected()) {
					List<Point> list = new ArrayList<Point>();
					list.add(graph.snap(graph.fromScreen(new Point(this.start))));
					list.add(graph.snap(graph.fromScreen(new Point(this.current))));
					Map map = GraphConstants.createMap();
					GraphConstants.setPoints(map, list);
					GraphConstants.setLineEnd(map, GraphConstants.ARROW_NONE);
					GraphConstants.setEndFill(map, true);
					Map<DefaultEdge, Map> viewMap = new HashMap<DefaultEdge, Map>();
					DefaultEdge cell = new DefaultEdge(""); //$NON-NLS-1$
					viewMap.put(cell, map);
					Object[] insert = new Object[] { cell };
					ConnectionSet cs = new ConnectionSet();
					if (this.firstPort != null)
						cs.connect(cell, this.firstPort.getCell(), true);
					if (this.port != null)
						cs.connect(cell, this.port.getCell(), false);
					graph.getModel().insert(insert, viewMap, cs, null, null);
				} 
				else if (this.t.isSelected()) {
					DefaultGraphCell cell = GraphActions.addVertex(graph, LangModelGraph.getString(Constants.TEXT), this.bounds, true, false, false, null);
					graph.startEditingAtCell(cell);
				}
				event.consume();
			}
		} else { // right mouse button pressed
				boolean editable = graph.isEditable();
				Object[] cells = graph.getSelectionCells();
				if (cells.length != 1) {
					if (this.pane instanceof SchemeTabbedPane && event.isControlDown()) {
						JPopupMenu pop = new JPopupMenu();
						PopupFactory.createCopyPastePopup(pop, (SchemeTabbedPane)this.pane, event, editable);
						MenuElement[] items = pop.getSubElements();
						if (items.length != 0) {
							pop.addSeparator();
							pop.add(PopupFactory.createCancelMenuItem());
						}
						if (pop.getSubElements().length != 0) {
							pop.show(graph, event.getX(), event.getY());
							event.consume();
						}
					}
				} else {
					if (this.pane instanceof SchemeTabbedPane) {
					Object cell = cells[0];
					
					if (SchemeGraph.getMode().equals(Constants.LINK_MODE)
							|| SchemeGraph.getMode().equals(Constants.RACK_MODE)) {
						JPopupMenu pop = new JPopupMenu();
						PopupFactory.getSuitablePopup(pop, cell, (SchemeTabbedPane)this.pane, event, editable);
						MenuElement[] items = pop.getSubElements();
						if (items.length != 0) {
							pop.addSeparator();
							pop.add(PopupFactory.createCancelMenuItem());
						}
						if (pop != null && pop.getSubElements().length != 0) {
							pop.show(graph, event.getX(), event.getY());
							event.consume();
						}
					} else if (SchemeGraph.getMode().equals(Constants.PATH_MODE)) {
						SchemeTabbedPane pane1 = (SchemeTabbedPane)this.pane; 
						
						JPopupMenu pop = new JPopupMenu();
						if (SchemeResource.getSchemePath() != null && SchemeResource.isPathEditing()) {
							PopupFactory.createPathPopup(pop, pane1.getContext(), cell);
//							notify = false;
						}
						if (pop.getSubElements().length != 0) {
							pop.addSeparator();
						}
						PopupFactory.getSuitablePopup(pop, cell, (SchemeTabbedPane)this.pane, event, editable);
						MenuElement[] items = pop.getSubElements();
						if (items.length != 0) {
							pop.addSeparator();
							pop.add(PopupFactory.createCancelMenuItem());
						}
						pop.show(graph, event.getX(), event.getY());
						event.consume();
						}
					}
				}
		}
		if (!this.s.isSelected())
			this.s.doClick();
		this.firstPort = null;
		this.port = null;
		this.start = null;
		this.current = null;
		this.bounds = null;
		super.mouseReleased(event);
		
		graph.repaint();
		graph.setCursor(DEFAULT_CURSOR);
		if (!graph.getMode().equals(Constants.PATH_MODE) && notify) {
			graph.selectionNotify();
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		SchemeGraph graph = (SchemeGraph)event.getSource();
		if (!graph.isEditable())
			return;

		if (this.p1.isSelected() || this.p2.isSelected()) {
			if (this.devBounds == null) {
				DeviceCell deviceCell = getOnlySelectedDevice(graph);
				if (deviceCell != null) {
					Map m = graph.getModel().getAttributes(deviceCell);
					this.devBounds = GraphConstants.getBounds(m);
					this.settingPoint = graph.snap(event.getPoint());
					graph.setCursor(CROSSHAIR_CURSOR);
				}
			}
			Point p = graph.snap(event.getPoint());
			int minX = Math.min(this.devBounds.x, Math.min(p.x, this.settingPoint.x) - CROSS_SIZE) - 1;
			int maxX = Math.max(this.devBounds.x + this.devBounds.width, Math.max(p.x, this.settingPoint.x + CROSS_SIZE)) + 1;
			int minY = Math.min(this.devBounds.y, Math.min(p.y, this.settingPoint.y - CROSS_SIZE)) - 1;
			int maxY = Math.max(this.devBounds.y + this.devBounds.height, Math.max(p.y, this.settingPoint.y + CROSS_SIZE)) + 1;
			graph.repaint(minX, minY, maxX - minX, maxY - minY);
			this.settingPoint = p;
			event.consume();
		}
		
		if (!this.s.isSelected() && !event.isConsumed()) {
			graph.setCursor(CROSSHAIR_CURSOR);
			event.consume();
			if (this.e.isSelected() || this.l.isSelected() || this.ce.isSelected()) {
				PortView oldPort = this.port;
				PortView newPort = getPortViewAt(graph, event.getX(), event.getY(), !event.isShiftDown());

				if (newPort == null || (oldPort != newPort && 
						GraphConstants.isConnectable(graph.getModel().getAttributes(newPort.getCell())))) {
					Graphics g = graph.getGraphics();
					Color bg = graph.getBackground();
					Color fg = graph.getMarqueeColor();
					g.setColor(fg);
					g.setXORMode(bg);
					overlay(graph, g);
					this.port = newPort;
					g.setColor(bg);
					g.setXORMode(fg);
					overlay(graph, g);
				}
			}
		}
		super.mouseMoved(event);
	}

	public void overlay(SchemeGraph graph, Graphics g) {
		if (this.marqueeBounds != null) {
			g.drawRect(this.marqueeBounds.x, this.marqueeBounds.y, this.marqueeBounds.width, this.marqueeBounds.height);
		}
		paintPort(graph, graph.getGraphics());
		if (this.bounds != null && this.start != null) {
			if (this.i.isSelected() || this.z.isSelected()) {
				((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			} else if (this.c.isSelected()) {
				g.drawOval(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
			} else if ((this.l.isSelected() || this.e.isSelected() || this.ce.isSelected()) && this.current != null) {
					g.drawLine(this.start.x, this.start.y, this.current.x, this.current.y);
			}	else if (!this.s.isSelected()) {
					g.drawRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
			}
		}
	}
	
	@Override
	public void overlay(Graphics g) {
		if (this.marqueeBounds != null) {
			g.drawRect(this.marqueeBounds.x, this.marqueeBounds.y, this.marqueeBounds.width, this.marqueeBounds.height);
		}
		if (this.bounds != null && this.start != null) {
			if (this.i.isSelected() || this.z.isSelected()) {
				((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			} else if (this.c.isSelected()) {
				g.drawOval(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
			} else if ((this.l.isSelected() || this.e.isSelected() || this.ce.isSelected()) && this.current != null) {
				g.drawLine(this.start.x, this.start.y, this.current.x, this.current.y);
			} else if (!this.s.isSelected()) {
				g.drawRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
			}
		}
		if ((this.p1.isSelected() || this.p2.isSelected()) && this.devBounds != null && this.settingPoint != null) {
			if (this.settingPoint.y > this.devBounds.y && this.settingPoint.y < this.devBounds.y + this.devBounds.height - 1) {
				g.setColor(Color.GRAY);
				if (this.settingPoint.x < this.devBounds.x) {
					g.drawLine(this.settingPoint.x, this.settingPoint.y, this.devBounds.x, this.settingPoint.y);
					g.drawLine(this.settingPoint.x - CROSS_SIZE, this.settingPoint.y - CROSS_SIZE, 
							this.settingPoint.x + CROSS_SIZE, this.settingPoint.y + CROSS_SIZE);
					g.drawLine(this.settingPoint.x - CROSS_SIZE, this.settingPoint.y + CROSS_SIZE, 
							this.settingPoint.x + CROSS_SIZE, this.settingPoint.y - CROSS_SIZE);
				} else if (this.settingPoint.x > this.devBounds.x + this.devBounds.width) { 
					g.drawLine(this.settingPoint.x, this.settingPoint.y, this.devBounds.x + this.devBounds.width, this.settingPoint.y);
					g.drawLine(this.settingPoint.x - CROSS_SIZE, this.settingPoint.y - CROSS_SIZE, 
							this.settingPoint.x + CROSS_SIZE, this.settingPoint.y + CROSS_SIZE);
					g.drawLine(this.settingPoint.x - CROSS_SIZE, this.settingPoint.y + CROSS_SIZE, 
							this.settingPoint.x + CROSS_SIZE, this.settingPoint.y - CROSS_SIZE);
				}
			}
		}
	}
	
	protected void paintPort(SchemeGraph graph, Graphics g) {
		if (this.port != null) {
			boolean offset =
				(GraphConstants.getOffset(this.port.getAllAttributes()) != null);
			Rectangle rect =
				(offset)
					? this.port.getBounds()
					: this.port.getParentView().getBounds();
			rect = graph.toScreen(new Rectangle(rect));
			int s1 = 3;
			rect.translate(-s1, -s1);
			rect.setSize(rect.width + 2 * s1, rect.height + 2 * s1);
			GraphUI ui = graph.getUI();
			ui.paintCell(g, this.port, rect, true);
		}
	}
}
