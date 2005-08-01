/*-
 * $Id: SchemeMarqueeHandler.java,v 1.16 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.awt.Cursor;
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
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.16 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
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
	public transient JButton ugr = new JButton(); //ungroupKey
	public transient JButton undo = new JButton(); //undoKey
	public transient JButton redo = new JButton(); //redoKey
	public transient JButton zi = new JButton(); //zoomInKey
	public transient JButton zo = new JButton(); //zoomOutKey
	public transient JButton za = new JButton(); //zoomActualKey
	public transient JButton del = new JButton(); //deleteKey
	public transient JButton ugo = new JButton(); //createTopLevelElementKey
	public transient JButton scheme_ugo = new JButton(); //createTopLevelSchemeKey
	public transient JButton bp = new JButton(); //blockPortKey
	public transient JButton bSize = new JButton(); //backgroundSize
	public transient JToggleButton pathButt = new JToggleButton(); //PATH_MODE
	public transient JToggleButton linkButt = new JToggleButton(); //LINK_MODE
	public transient JButton topModeButt = new JButton(); //TOP_LEVEL_SCHEME_MODE

	// from GPMarqueeHandler
	protected Point start, current;
	protected Rectangle bounds;
	protected PortView port, firstPort, lastPort;
	
	protected Rectangle devBounds;
	protected Point settingPoint;
	private int crossSize = 4;
	
	private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	private static final Cursor CROSSHAIR_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
	
//	private transient SchemeProtoElement settingProto = null;
	//	private transient boolean sendEvents = true;

	// Update Undo/Redo Button State based on Undo Manager
	public void updateHistoryButtons(GraphUndoManager undoManager) {
		SchemeGraph graph = pane.getGraph();
		boolean b1 = undoManager.canUndo(graph.getGraphLayoutCache());
		undo.setEnabled(b1);
		redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
		
		pane.setGraphChanged(b1);
	}
	
	/* Return true if this handler should be preferred over other handlers. */
	public boolean isForceMarqueeEvent(MouseEvent event) {
		return !s.isSelected()
			|| isPopupTrigger(event)
			|| super.isForceMarqueeEvent(event);
	}
	
	protected boolean isPopupTrigger(MouseEvent event) {
		return SwingUtilities.isRightMouseButton(event) && !event.isShiftDown();
	}

	public void updateButtonsState(Object[] cells) {
		SchemeGraph graph = pane.getGraph();
		ugo.setEnabled((graph.getAll().length == 0) ? false : true);
		del.setEnabled(cells.length != 0 && !GraphActions.hasGroupedParent(cells[0]));
		ugr.setEnabled(false);
		gr.setEnabled(false);
		gr2.setEnabled(false);
		p1.setEnabled(false);
		p2.setEnabled(false);
		bp.setEnabled(false);

		int ports = 0;
		int cablePorts = 0;
		int devices = 0;
		DeviceCell device = null;
		int groups = 0;
		for (int j = 0; j < cells.length; j++) {
			if (cells[j] instanceof DeviceCell) {
				devices++;
				device = (DeviceCell) cells[j];
			} 
			else if (cells[j] instanceof DeviceGroup) {
				groups++;
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
					bp.setEnabled(true);
			} 
			else if (devices == 1) {
				if (!GraphActions.hasGroupedParent(device)) {
					p1.setEnabled(true);
					p2.setEnabled(true);
					if (device.getChildCount() > 1) {
						gr.setEnabled(true);
						gr2.setEnabled(true);
					}
				}
			}
		} else if (groups == 1) {
			// ugo.setEnabled(true);
		}
		if (groups > 0)
			ugr.setEnabled(true);
		if (groups > 1) {
			gr.setEnabled(true);
			gr2.setEnabled(true);
		}
	}

	public void mousePressed(MouseEvent event) {
		SchemeGraph graph = (SchemeGraph)event.getSource();
		if (graph.isEditable()) {
			if (p1.isSelected() || p2.isSelected()) {
				createPort(graph, graph.snap(event.getPoint()));
				devBounds = null;
				settingPoint = null;
				graph.repaint();
				event.consume();
			} else if (ce.isSelected() && firstPort != null)
				start = graph.toScreen(firstPort.getLocation(null));
		}
		
		// from GPMarqueeHandler
		if (!isPopupTrigger(event) && !event.isConsumed() && !s.isSelected()) {
			start = graph.snap(event.getPoint());
			firstPort = port;
			if (e.isSelected() && firstPort != null)
				start = graph.toScreen(firstPort.getLocation(null));
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
	}

	public void mouseDragged(MouseEvent event) {
		SchemeGraph graph = (SchemeGraph)event.getSource();
		if (!graph.isEditable())
			return;

		if (p1.isSelected() || p2.isSelected())
			event.consume();
		// from GPMarqueeHandler
		else if (!event.isConsumed() && !s.isSelected()) {
			Graphics g = graph.getGraphics();
			Color bg = graph.getBackground();
			Color fg = Color.black;
			g.setColor(fg);
			g.setXORMode(bg);
			overlay(graph, g);
			current = graph.snap(event.getPoint());
			if (e.isSelected() || l.isSelected() || ce.isSelected()) {
				port = getPortViewAt(graph, event.getX(), event.getY(), !event.isShiftDown());
				if (port != null) {
					Map map = graph.getModel().getAttributes(port.getCell());
					if (map != null) {
						if (!GraphConstants.isConnectable(map))
							port = null;
						else
							current = graph.toScreen(port.getLocation(null));
					}
				}
			}
			bounds = new Rectangle(start).union(new Rectangle(current));
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
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), Constants.ERROR_GROUPED_DEVICE, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
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
			
			boolean isCable = !p1.isSelected();
			
			StorableObjectCondition condition = new TypicalCondition(
					isCable ? PortTypeKind._PORT_KIND_CABLE : PortTypeKind._PORT_KIND_SIMPLE,
					0, OperationSort.OPERATION_EQUALS,
					ObjectEntities.PORT_TYPE_CODE, PortTypeWrapper.COLUMN_KIND);
			Set types = Collections.EMPTY_SET;
			try {
				types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			} catch (ApplicationException e1) {
				Log.errorException(e1);
			}
			if (types.isEmpty()) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), Constants.ERROR_PORTTYPE_NOT_FOUND, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
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
				
				Color color = SchemeActions.determinePortColor(schemePort);

				if (!isCable) { //port
					PortCell cell = SchemeActions.createPort(graph, deviceCell, 
							graph.fromScreen(graph.snap(p)), name, directionType, color, schemePort.getId());
				} else {
					CablePortCell cell = SchemeActions.createCablePort(graph, deviceCell, 
							graph.fromScreen(graph.snap(p)), name, directionType, color, schemePort.getId());
				}
			} catch (ApplicationException e1) {
				Log.errorException(e1);
			}
		}
	}

	public void mouseReleased(MouseEvent event) {
		SchemeGraph graph = (SchemeGraph)event.getSource();
		if (!graph.isEditable()) {
			event.consume();
			graph.setCursor(DEFAULT_CURSOR);
		}
			
		if (SwingUtilities.isLeftMouseButton(event)) {
			if (event != null && !event.isConsumed() && bounds != null && !s.isSelected()) {
				if (dev.isSelected()) {
					graph.fromScreen(bounds);
//					bounds.width += 2;
//					bounds.height += 2;
//					bounds.width ++;
//					bounds.height ++;
					try {
						SchemeDevice device = SchemeObjectsFactory.createSchemeDevice(Constants.DEVICE + System.currentTimeMillis());
						DeviceCell cell = SchemeActions.createDevice(graph, "", bounds, device.getId());  //$NON-NLS-1$
						cell.setSchemeDeviceId(device.getId());
					} catch (ApplicationException e1) {
						Log.errorException(e1);
					}
				} else if (r.isSelected())
					graph.addVertex("", bounds, false, Color.black); //$NON-NLS-1$
				else if (c.isSelected())
					graph.addEllipse("", bounds); //$NON-NLS-1$
				else if (ce.isSelected()) {
					if (start == null || current == null) {
						event.consume();
					} else {
						StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
						Set types = Collections.EMPTY_SET;
						try {
							types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
						} catch (ApplicationException e1) {
							Log.errorException(e1);
						}
						if (types.isEmpty()) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), Constants.ERROR_CABLELINKTYPE_NOT_FOUND, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
							return;
						}
						CableLinkType type = (CableLinkType)types.iterator().next();
						
						Scheme scheme = null;
						UgoPanel panel = pane.getCurrentPanel();
						if (panel instanceof SchemePanel)
							scheme = ((SchemePanel)panel).getSchemeResource().getScheme();
						if (scheme != null) {
						
							try {
								SchemeCableLink link = SchemeObjectsFactory.createSchemeCableLink("cable" + System.currentTimeMillis(), scheme);
								link.setAbstractLinkType(type);
								DefaultCableLink cell = SchemeActions.createCableLink(graph,
										firstPort, port, graph.fromScreen(new Point(start)), 
										graph.fromScreen(new Point(current)), link.getId());
								link.setName((String)cell.getUserObject());
								Notifier.notify(graph, pane.aContext, link);
							} catch (ApplicationException e1) {
								Log.errorException(e1);
							}
						}
					}
				} else if (e.isSelected()) {
					if (start == null || current == null) {
						event.consume();
					} else {
						StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
						Set types = Collections.EMPTY_SET;
						try {
							types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
						} catch (ApplicationException e1) {
							Log.errorException(e1);
						}
						if (types.isEmpty()) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), Constants.ERROR_LINKTYPE_NOT_FOUND, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
							return;
						}
						LinkType type = (LinkType)types.iterator().next();
						
						SchemeLink link;
						try {
							link = SchemeObjectsFactory.createSchemeLink("link" + System.currentTimeMillis());
							link.setAbstractLinkType(type);
							
							UgoPanel panel = pane.getCurrentPanel();
							if (panel instanceof SchemePanel) {
								SchemeResource res = ((SchemePanel)panel).getSchemeResource();
								if (res.getCellContainerType() == SchemeResource.SCHEME)
									link.setParentScheme(res.getScheme());
								else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT)
									link.setParentSchemeElement(res.getSchemeElement());
								else if (res.getCellContainerType() == SchemeResource.SCHEME_PROTO_ELEMENT)
									link.setParentSchemeProtoElement(res.getSchemeProtoElement());
							}
							
							DefaultLink cell = SchemeActions.createLink(graph,
									firstPort, port, graph.fromScreen(new Point(start)), 
									graph.fromScreen(new Point(current)), link.getId());
							link.setName((String)cell.getUserObject());
							Notifier.notify(graph, pane.aContext, link);
						} catch (ApplicationException e1) {
							Log.errorException(e1);
							return;
						}
						
						UgoPanel panel = pane.getCurrentPanel();
						if (panel instanceof ElementsPanel) {
							SchemeResource res = ((ElementsPanel)panel).getSchemeResource();
							if (res.getCellContainerType() == SchemeResource.SCHEME) {
								link.setParentScheme(res.getScheme());
							} else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
								link.setParentSchemeElement(res.getSchemeElement());
							} else if (res.getCellContainerType() == SchemeResource.SCHEME_PROTO_ELEMENT) {
								if (res.getSchemeProtoElement() != null)
									link.setParentSchemeProtoElement(res.getSchemeProtoElement());
							}
						}
					}
				} 
				else if (l.isSelected()) {
					List list = new ArrayList();
					list.add(graph.fromScreen(new Point(start)));
					list.add(graph.toScreen(new Point(current)));
					Map map = GraphConstants.createMap();
					GraphConstants.setPoints(map, list);
					GraphConstants.setLineEnd(map, GraphConstants.ARROW_NONE);
					GraphConstants.setEndFill(map, true);
					Map viewMap = new HashMap();
					DefaultEdge cell = new DefaultEdge(""); //$NON-NLS-1$
					viewMap.put(cell, map);
					Object[] insert = new Object[] { cell };
					ConnectionSet cs = new ConnectionSet();
					if (firstPort != null)
						cs.connect(cell, firstPort.getCell(), true);
					if (port != null)
						cs.connect(cell, port.getCell(), false);
					graph.getModel().insert(insert, viewMap, cs, null, null);
				} 
				else if (t.isSelected()) {
					DefaultGraphCell cell = GraphActions.addVertex(graph, LangModelGraph.getString(Constants.TEXT), bounds, true, false, false, null);
					graph.startEditingAtCell(cell);
				}
				event.consume();
			}
		} else { // right mouse button pressed
			if (!graph.isSelectionEmpty()) {
				Object cell = graph.getSelectionCell();
				DeviceGroup group = null;

				if (cell instanceof DeviceGroup)
					group = (DeviceGroup) cell;
				else if (cell instanceof DeviceCell
						&& ((DeviceCell) cell).getParent() instanceof DeviceGroup)
					group = (DeviceGroup) ((DeviceCell) cell).getParent();

				if (group != null) {
					JPopupMenu pop = SchemeActions.createElementPopup(pane.aContext, graph, group);
					if (pop.getSubElements().length != 0)
						pop.show(graph, event.getX(), event.getY());
				}
			}
		}
		if (!s.isSelected())
			s.doClick();
		firstPort = null;
		port = null;
		start = null;
		current = null;
		bounds = null;
		super.mouseReleased(event);
		
		graph.repaint();
//		graph.selectionNotify();
	}

	public void mouseMoved(MouseEvent event) {
		SchemeGraph graph = (SchemeGraph)event.getSource();
		if (!graph.isEditable())
			return;

		if (p1.isSelected() || p2.isSelected()) {
			if (devBounds == null) {
				DeviceCell deviceCell = getOnlySelectedDevice(graph);
				if (deviceCell != null) {
					Map m = graph.getModel().getAttributes(deviceCell);
					devBounds = GraphConstants.getBounds(m);
					settingPoint = graph.snap(event.getPoint());
					graph.setCursor(CROSSHAIR_CURSOR);
				}
			}
			Point p = graph.snap(event.getPoint());
			int minX = Math.min(devBounds.x, Math.min(p.x, settingPoint.x) - crossSize) - 1;
			int maxX = Math.max(devBounds.x + devBounds.width, Math.max(p.x, settingPoint.x + crossSize)) + 1;
			int minY = Math.min(devBounds.y, Math.min(p.y, settingPoint.y - crossSize)) - 1;
			int maxY = Math.max(devBounds.y + devBounds.height, Math.max(p.y, settingPoint.y + crossSize)) + 1;
			graph.repaint(minX, minY, maxX - minX, maxY - minY);
			settingPoint = p;
			event.consume();
		}
		
		if (!s.isSelected() && !event.isConsumed()) {
			graph.setCursor(CROSSHAIR_CURSOR);
			event.consume();
			if (e.isSelected() || l.isSelected() || ce.isSelected()) {
				PortView oldPort = port;
				PortView newPort = getPortViewAt(graph, event.getX(), event.getY(), !event.isShiftDown());

				if (newPort == null || (oldPort != newPort && 
						GraphConstants.isConnectable(graph.getModel().getAttributes(newPort.getCell())))) {
					Graphics g = graph.getGraphics();
					Color bg = graph.getBackground();
					Color fg = graph.getMarqueeColor();
					g.setColor(fg);
					g.setXORMode(bg);
					overlay(graph, g);
					port = newPort;
					g.setColor(bg);
					g.setXORMode(fg);
					overlay(graph, g);
				}
			}
		}
		super.mouseMoved(event);
	}

	public void overlay(SchemeGraph graph, Graphics g) {
		if (marqueeBounds != null)
			g.drawRect(marqueeBounds.x, marqueeBounds.y, marqueeBounds.width,
					marqueeBounds.height);
		paintPort(graph, graph.getGraphics());
		if (bounds != null && start != null) {
			if (i.isSelected() || z.isSelected())
				((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			else if (c.isSelected())
				g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
			else if ((l.isSelected() || e.isSelected() || ce.isSelected())
					&& current != null)
				g.drawLine(start.x, start.y, current.x, current.y);
			else if (!s.isSelected())
				g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}
	
	public void overlay(Graphics g) {
		if (marqueeBounds != null) {
			g.drawRect(marqueeBounds.x, marqueeBounds.y, marqueeBounds.width, marqueeBounds.height);
		}
		if (bounds != null && start != null) {
			if (i.isSelected() || z.isSelected())
				((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			else if (c.isSelected())
				g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
			else if ((l.isSelected() || e.isSelected() || ce.isSelected())
					&& current != null)
				g.drawLine(start.x, start.y, current.x, current.y);
			else if (!s.isSelected())
				g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		if ((p1.isSelected() || p2.isSelected()) && devBounds != null && settingPoint != null) {
			if (settingPoint.y > devBounds.y && settingPoint.y < devBounds.y + devBounds.height - 1) {
				g.setColor(Color.GRAY);
				if (settingPoint.x < devBounds.x) {
					g.drawLine(settingPoint.x, settingPoint.y, devBounds.x, settingPoint.y);
					g.drawLine(settingPoint.x - crossSize, settingPoint.y - crossSize, 
							settingPoint.x + crossSize, settingPoint.y + crossSize);
					g.drawLine(settingPoint.x - crossSize, settingPoint.y + crossSize, 
							settingPoint.x + crossSize, settingPoint.y - crossSize);
				} else if (settingPoint.x > devBounds.x + devBounds.width) { 
					g.drawLine(settingPoint.x, settingPoint.y, devBounds.x + devBounds.width, settingPoint.y);
					g.drawLine(settingPoint.x - crossSize, settingPoint.y - crossSize, 
							settingPoint.x + crossSize, settingPoint.y + crossSize);
					g.drawLine(settingPoint.x - crossSize, settingPoint.y + crossSize, 
							settingPoint.x + crossSize, settingPoint.y - crossSize);
				}
			}
		}
	}
	
	protected void paintPort(SchemeGraph graph, Graphics g) {
		if (port != null) {
			boolean offset =
				(GraphConstants.getOffset(port.getAllAttributes()) != null);
			Rectangle rect =
				(offset)
					? port.getBounds()
					: port.getParentView().getBounds();
			rect = graph.toScreen(new Rectangle(rect));
			int s1 = 3;
			rect.translate(-s1, -s1);
			rect.setSize(rect.width + 2 * s1, rect.height + 2 * s1);
			GraphUI ui = graph.getUI();
			ui.paintCell(g, port, rect, true);
		}
	}
}
