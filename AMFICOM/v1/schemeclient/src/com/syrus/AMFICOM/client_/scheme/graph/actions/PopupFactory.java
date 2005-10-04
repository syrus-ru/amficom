/*-
 * $Id: PopupFactory.java,v 1.14 2005/10/04 08:14:15 stas Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import static java.util.logging.Level.SEVERE;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.PortView;
import com.jgraph.plaf.basic.TransferHandler;
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathBuilder;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.LangModelGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraphTransferHandler;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.TopLevelElement;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

public class PopupFactory {
	private PopupFactory() {
		// no instance allowed
	}
	
	public static JPopupMenu createOpenPopup(final ApplicationContext aContext, final Identifier transferable, final Point p, final long actionType) {
		JPopupMenu pop = new JPopupMenu();
		pop.add(createInsertMenuItem(aContext, transferable, p, actionType));
		pop.add(createOpenMenuItem(aContext, transferable, actionType));
		pop.addSeparator();
		pop.add(createCancelMenuItem());
		return pop;
	}
	
	public static JPopupMenu createElementPopup(final ApplicationContext aContext, SchemeTabbedPane pane, DeviceGroup group, final Point p) {
		try {
			JPopupMenu pop = new JPopupMenu();
			if (group.getType() == DeviceGroup.SCHEME_ELEMENT) {
				final SchemeElement se = group.getSchemeElement();
				if (se.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
					pop.add(createOpenSchemeMenuItem(aContext, se.getScheme(false)));
					pop.addSeparator();
					pop.add(createCutMenuItem(pane));
					pop.add(createPasteMenuItem(pane, p));
					pop.addSeparator();
					pop.add(createCancelMenuItem());
				} else {
					JMenuItem item = createOpenSchemeElementMenuItem(aContext, se);
					if (item != null) {
						pop.add(item);
					}
					pop.add(createCutMenuItem(pane));
					pop.add(createPasteMenuItem(pane, p));
					pop.addSeparator();
					pop.add(createCancelMenuItem());
				}
			}
			return pop;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}
	
	public static JPopupMenu createTopElementPopup(final ApplicationContext aContext, TopLevelElement group) {
		JPopupMenu pop = new JPopupMenu();
		JMenuItem item = createOpenSchemeMenuItem(aContext, group.getScheme());
		if (item != null) {
			pop.add(item);
			pop.addSeparator();
			pop.add(createCancelMenuItem());
		}
		return pop;
	}
	
	public static JPopupMenu createCablePopup(final ApplicationContext aContext, SchemeTabbedPane pane, DefaultCableLink cell, final Point p) {
		JPopupMenu pop = new JPopupMenu();
		
		JMenuItem i1 = createMuffMenuItem(aContext, pane.getGraph(), cell);
		if (i1 != null) {
			pop.add(i1);
			pop.addSeparator();
		}
		pop.add(createCutMenuItem(pane));
		pop.add(createPasteMenuItem(pane, p));
		pop.addSeparator();
		pop.add(createCancelMenuItem());
//		pop.add(createCableSchemeMenuItem(aContext, graph, cell));
		return pop;
	}
	
	public static JPopupMenu createCopyPastePopup(final ApplicationContext aContext, final SchemeTabbedPane pane, final Point p) {
		JPopupMenu pop = new JPopupMenu();
		
		pop.add(createCutMenuItem(pane));
		pop.add(createPasteMenuItem(pane, p));
		pop.addSeparator();
		pop.add(createCancelMenuItem());

		return pop;
	}
	
	public static JMenuItem createCutMenuItem(final SchemeTabbedPane pane) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 4132259664875195748L;
			public void actionPerformed(ActionEvent e) {
				SchemeGraph graph = pane.getGraph();
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				graph.getTransferHandler().exportToClipboard(graph, clipboard, TransferHandler.MOVE);
			}
		});
		SchemeGraph graph = pane.getGraph();
		if (graph.getSelectionCells().length == 0) {
			menuItem.setEnabled(false);
		}
		menuItem.setText(LangModelScheme.getString("Menu.scheme.cut")); //$NON-NLS-1$
		return menuItem;
	}
	
	public static JMenuItem createPasteMenuItem(final SchemeTabbedPane pane, final Point p) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 4132259664875195748L;
			public void actionPerformed(ActionEvent e) {
				SchemeGraph graph = pane.getGraph();
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				TransferHandler handler = graph.getTransferHandler();
				if (handler.importData(graph, clipboard.getContents(graph))) {
					if (handler instanceof SchemeGraphTransferHandler) {
						SchemeGraphTransferHandler handler1 = (SchemeGraphTransferHandler)graph.getTransferHandler();
						Object[] flat = DefaultGraphModel.getDescendants(graph.getModel(), handler1.getInsertedCells()).toArray();
						GraphActions.move(graph, flat, p, false);
					}
				}
			}
		});
		SchemeGraph graph = pane.getGraph();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (!graph.getTransferHandler().canImport(graph, clipboard.getAvailableDataFlavors())) {
			menuItem.setEnabled(false);
		}
		menuItem.setText(LangModelScheme.getString("Menu.scheme.paste")); //$NON-NLS-1$
		return menuItem;
	}
		
	public static JPopupMenu createPathPopup(final ApplicationContext aContext,
			final SchemeResource res, Object cell) {
				
		final Identifier id;
		if (cell instanceof DeviceGroup) {
			id = ((DeviceGroup)cell).getElementId();
		} else if (cell instanceof DefaultLink) {
			id = ((DefaultLink)cell).getSchemeLinkId();
		} else if (cell instanceof DefaultCableLink) {
			id = ((DefaultCableLink)cell).getSchemeCableLinkId();
		} else {
//		no popup for other elements
			return null;
		}
		
//		final SchemePath path = res.getSchemePath();
		final SortedSet<Identifier> pmIds = res.getCashedPathElementIds();
		JPopupMenu pop = new JPopupMenu();
		
		if (pmIds.contains(id)) { // already added to path
			pop.add(createPathRemoveMenuItem(cell, aContext));
			if (res.getCashedPathStart() != null && res.getCashedPathEnd() != null) {
				pop.addSeparator();
				pop.add(createPathExploreMenuItem(res));
			}
		} else { // not added to path yet
			if (pmIds.isEmpty()) { // add only start
				if (id.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
					try {
						SchemeElement se = (SchemeElement)StorableObjectPool.getStorableObject(id, true);
						if (se.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
							pop.add(createOpenSchemeMenuItem(aContext, se.getScheme(false)));
							pop.addSeparator();
							pop.add(createCancelMenuItem());
						} else {
							JMenuItem item = createOpenSchemeElementMenuItem(aContext, se);
							if (item != null) {
								pop.add(item);
								pop.addSeparator();
								pop.add(createCancelMenuItem());
							} else {
								pop.add(createPathStartMenuItem(res, id));
								pop.addSeparator();
								pop.add(createCancelMenuItem());							
							}
						}
					} catch (ApplicationException e) {
						Log.errorException(e);
					}
				}
			} else { // add "add" and "end"
				if (res.getCashedPathStart() != null && res.getCashedPathEnd() != null) {
					pop.add(createPathExploreMenuItem(res));
				}
				
				try {
					if (id.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
						SchemeElement se = (SchemeElement)StorableObjectPool.getStorableObject(id, true);
						if (se.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
							pop.add(createOpenSchemeMenuItem(aContext, se.getScheme(false)));
							pop.add(createPathAddMenuItem(aContext, res, id));
							pop.addSeparator();
							pop.add(createCancelMenuItem());
						} else {
							JMenuItem item = createOpenSchemeElementMenuItem(aContext, se);
							if (item != null) {
								pop.add(item);
								pop.add(createPathAddMenuItem(aContext, res, id));
								pop.addSeparator();
								pop.add(createCancelMenuItem());
							} else {
								pop.add(createPathAddMenuItem(aContext, res, id));
								pop.add(createPathEndMenuItem(res, id));
								pop.addSeparator();
								pop.add(createCancelMenuItem());
							}
						}
					} else {
						pop.add(createPathAddMenuItem(aContext, res, id));
						pop.addSeparator();
						pop.add(createCancelMenuItem());
					}
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
		}
		return pop;
	}
	
	private static JMenuItem createCancelMenuItem() {
		JMenuItem menu = new JMenuItem();
		menu.setText(LangModelGraph.getString("cancel")); //$NON-NLS-1$
		return menu;
	}
	
	private static JMenuItem createMuffMenuItem(final ApplicationContext aContext, final SchemeGraph graph, final DefaultCableLink cell) {
		JMenu menu = new JMenu(LangModelScheme.getString("Menu.path.insert_muff")); //$NON-NLS-1$
		try {
			TypicalCondition condition1 = new TypicalCondition(EquipmentType.MUFF, OperationSort.OPERATION_EQUALS, ObjectEntities.PROTOEQUIPMENT_CODE, StorableObjectWrapper.COLUMN_TYPE_CODE);
			Set<ProtoEquipment> protoEqs = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
			Set<Identifier> protoEqIds = new HashSet<Identifier>(); 
			for (ProtoEquipment protoEq : protoEqs) {
				protoEqIds.add(protoEq.getId());
			}
			if (protoEqIds.isEmpty()) {
				return null;
			}
			LinkedIdsCondition condition2 = new LinkedIdsCondition(protoEqIds, ObjectEntities.SCHEMEPROTOELEMENT_CODE);
			Set<SchemeProtoElement> protos = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			final SchemeCableLink cableLink = cell.getSchemeCableLink();
			final Scheme parentScheme = cableLink.getParentScheme();
			
			for (final SchemeProtoElement proto : protos) {
				JMenuItem menu1 = new JMenuItem(new AbstractAction() {
					private static final long serialVersionUID = 5111911535154715944L;

					public void actionPerformed(ActionEvent e) {
						try {
							SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), proto, parentScheme);
							EdgeView view = (EdgeView)graph.getGraphLayoutCache().getMapping(cell, true);
							
							DefaultCableLink[] newCells = SchemeActions.splitCableLink(graph, cell);
							if (newCells != null) {
								
								Point p = getCenterPoint(graph, view);
								SchemeActions.insertSEbyPE(graph, schemeElement, schemeElement.getClonedIdMap(), p, true);
								Set<SchemeCablePort> cablePorts = schemeElement.getSchemeCablePortsRecursively(false);
								CablePortCell inPort = null;
								CablePortCell outPort = null;

								for (SchemeCablePort cport : cablePorts) {
									if (cport.getDirectionType() == IdlDirectionType._IN) {
										if (inPort == null) {
											inPort = SchemeActions.findCablePortCellById(graph, cport.getId());
										}
									} else {
										if (outPort == null) {
											outPort = SchemeActions.findCablePortCellById(graph, cport.getId());
										}
									}
								}
								
								if (inPort != null) {
									GraphActions.connect(graph, newCells[0], inPort, false);
								}
								if (outPort != null) {
									GraphActions.connect(graph, newCells[1], outPort, true);
								}
							}
						} catch (ApplicationException e1) {
							Log.errorException(e1);
						}
					}
				});
				menu1.setText(proto.getName());
				menu.add(menu1);
			}
		} catch (ApplicationException e1) {
			Log.errorException(e1);
		}		
		return menu;
	}
	
	private static JMenuItem createCableSchemeMenuItem(final ApplicationContext aContext, final SchemeGraph graph, final DefaultCableLink cell) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				final SchemeCableLink cableLink = cell.getSchemeCableLink();
				final Scheme parentScheme = cableLink.getParentScheme();
				
				try {
					Scheme internalScheme = Scheme.createInstance(LoginManager.getUserId(), cableLink.getName(), IdlKind.CABLE_SUBNETWORK, LoginManager.getDomainId());
					
					
					SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), internalScheme, parentScheme);

					DefaultCableLink[] newCells = SchemeActions.splitCableLink(graph, cell);
					if (newCells != null) {
						
					}
				} catch (ApplicationException e1) {
					Log.errorException(e1);
				}
			}
		});
		
		menuItem.setText(LangModelScheme.getString("Menu.path.insert_cable_scheme")); //$NON-NLS-1$
		return menuItem;
	}
	
	static Point getCenterPoint(SchemeGraph graph, EdgeView view) {
		List points = view.getPoints();
		
		Object leftObj = points.get(0);
		Object rightObj = points.get(points.size() - 1);
		Point left = graph.snap(leftObj instanceof Point ? (Point)leftObj : ((PortView)leftObj).getLocation(view));
		Point right = graph.snap(rightObj instanceof Point ? (Point)rightObj : ((PortView)rightObj).getLocation(view));
		
		int x = (left.x + right.x) / 2;
		int y = (left.y + right.y) / 2;
		return new Point(x, y);
	}
	
	private static JMenuItem createInsertMenuItem(final ApplicationContext aContext, final Identifier transferable, final Point p, final long actionType) {
		JMenuItem menu = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 7254016041683457753L;

			public void actionPerformed(ActionEvent ev) {
				aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, transferable, p, actionType));									
			}
		});
		menu.setText(LangModelGraph.getString("insert")); //$NON-NLS-1$
		return menu;
	}
	
	private static JMenuItem createOpenMenuItem(final ApplicationContext aContext, final Identifier transferable, final long actionType) {
		JMenuItem menu = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1023861610666047648L;

			public void actionPerformed(ActionEvent ev) {
				long actionType2;
				if (actionType == SchemeEvent.INSERT_PROTOELEMENT)
					actionType2 = SchemeEvent.OPEN_PROTOELEMENT;
				else if (actionType == SchemeEvent.INSERT_SCHEMEELEMENT)
					actionType2 = SchemeEvent.OPEN_SCHEMEELEMENT;
				else
					actionType2 = SchemeEvent.OPEN_SCHEME;
				aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, transferable, actionType2));									
			}
		});
		menu.setText(LangModelGraph.getString("open")); //$NON-NLS-1$
		return menu;
	}

	private static JMenuItem createPathStartMenuItem(final SchemeResource res, final Identifier id) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -2925941385039796591L;
			public void actionPerformed(ActionEvent ev) {
				res.setCashedPathStart(id);
				SchemePath path = res.getSchemePath();
				if (id.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
					try {
						SchemeElement se = StorableObjectPool.getStorableObject(id, false);
						PathBuilder.createPEbySE(path, se);
					} catch (ApplicationException e) {
						Log.errorException(e);
					}
				}
			}
		});
		menuItem.setText(LangModelScheme.getString("Menu.path.set_start")); //$NON-NLS-1$
		return menuItem;
	}
	
	private static JMenuItem createPathRemoveMenuItem(final Object object, final ApplicationContext aContext) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PathElement pe = SchemeActions.getSelectedPathElement(object);
				if (pe != null) { 
					SchemePath path = pe.getParentPathOwner();
					pe.setParentPathOwner(null, true);
					aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, path.getId(), SchemeEvent.UPDATE_OBJECT));
				}
			}
		});
		menuItem.setText(LangModelScheme.getString("Menu.path.remove")); //$NON-NLS-1$
		return menuItem;
	}
	
	private static JMenuItem createPathAddMenuItem(final ApplicationContext aContext, final SchemeResource res, final Identifier id) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -471712824699016078L;
			public void actionPerformed(ActionEvent ev) {
				SchemePath path = res.getSchemePath();
				if (id.getMajor() == ObjectEntities.SCHEMELINK_CODE) {
					try {
						SchemeLink link = StorableObjectPool.getStorableObject(id, false);
						if (PathBuilder.createPEbySL(path, link) == null) {
							Log.debugMessage("Can't add to path " + link.getName(), Level.WARNING); //$NON-NLS-1$
						}
					} catch (ApplicationException e) {
						Log.errorException(e);
					}
				} else if (id.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE) {
					try {
						SchemeCableLink link = StorableObjectPool.getStorableObject(id, false);
						if (PathBuilder.createPEbySCL(path, link) == null) {
							Log.debugMessage("Can't add to path " + link.getName(), Level.WARNING); //$NON-NLS-1$
						}
					} catch (ApplicationException e) {
						Log.errorException(e);
					}
				} else if (id.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
					try {
						SchemeElement se = StorableObjectPool.getStorableObject(id, false);
						if (PathBuilder.createPEbySE(path, se) == null) {
							Log.debugMessage("Can't add to path " + se.getName(), Level.WARNING); //$NON-NLS-1$
						}
					} catch (ApplicationException e) {
						Log.errorException(e);
					}
				}
				aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, path.getId(), SchemeEvent.UPDATE_OBJECT));
			}
		});
		menuItem.setText(LangModelScheme.getString("Menu.path.add")); //$NON-NLS-1$
		return menuItem;
	}
	
	private static JMenuItem createPathEndMenuItem(final SchemeResource res, final Identifier id) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = -2610269229583452112L;
			public void actionPerformed(ActionEvent ev) {
				res.setCashedPathEnd(id);
			}
		});
		menuItem.setText(LangModelScheme.getString("Menu.path.set_end")); //$NON-NLS-1$
		return menuItem;
	}
	
	private static JMenuItem createPathExploreMenuItem(final SchemeResource res) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 3306649391548325840L;
			public void actionPerformed(ActionEvent ev) {
				SchemePath path = res.getSchemePath();
				try {
					PathBuilder.explore(path, res.getCashedPathStart(), res.getCashedPathEnd());
				} catch (ApplicationException e) {
					Log.errorException(e);
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							e.getMessage(), 
							LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		menuItem.setText(LangModelScheme.getString("Menu.path.explore")); //$NON-NLS-1$
		return menuItem;
	}
	
	private static JMenuItem createOpenSchemeMenuItem(final ApplicationContext aContext, final Scheme sc) {
		JMenuItem menu = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 8641829415106895132L;
			public void actionPerformed(ActionEvent ev) {
				aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, sc.getId(), SchemeEvent.OPEN_SCHEME));
				
				try {
					SchemeElement se = sc.getParentSchemeElement();
					if (se != null && se.isAlarmed()) {
						aContext.getDispatcher().firePropertyChange(
								new SchemeEvent(this, se.getId(), SchemeEvent.CREATE_ALARMED_LINK));
					}
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
		});
		menu.setText(LangModelGraph.getString("open_scheme")); //$NON-NLS-1$
		return menu;
	}
	
	private static JMenuItem createOpenSchemeElementMenuItem(final ApplicationContext aContext, final SchemeElement se) {
		List v = null;
		if (se.getUgoCell() != null) {
			v = se.getUgoCell().getData();
		}
		try {
			if ((v != null && v.size() != 0 && ((Object[]) v.get(0)).length != 0) && !se.getSchemeElements(false).isEmpty()) {
				JMenuItem menu1 = new JMenuItem(new AbstractAction() {
					private static final long serialVersionUID = 7612382099522511230L;
					
					public void actionPerformed(ActionEvent ev) {
						aContext.getDispatcher().firePropertyChange(
								new SchemeEvent(this, se.getId(), SchemeEvent.OPEN_SCHEMEELEMENT));
						
						if (se.isAlarmed()) {
							aContext.getDispatcher().firePropertyChange(
									new SchemeEvent(this, se.getId(), SchemeEvent.CREATE_ALARMED_LINK));
						}
					}
				});
				menu1.setText(LangModelGraph.getString("open_component")); //$NON-NLS-1$
				return menu1;
			}
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		return null;
	}
}
