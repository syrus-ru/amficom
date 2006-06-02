/*-
 * $Id: SchemeTabbedPane.java,v 1.46 2006/06/02 17:23:57 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.PortView;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.ProcessingDialogDummy;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeElementPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePropertiesManager;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.46 $, $Date: 2006/06/02 17:23:57 $
 * @module schemeclient
 */

public class SchemeTabbedPane extends ElementsTabbedPane {
	private static final long serialVersionUID = 2083767734784404078L;
	JTabbedPane tabs;
	Map<JScrollPane, ElementsPanel> graphPanelsMap;
	
	private static ElementsPanel stubPanel;

	public SchemeTabbedPane(ApplicationContext aContext) {
		super(aContext);
	}
	
	@Override
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		}
		super.setContext(aContext);
		stubPanel.setContext(aContext);
		if (aContext != null) {
			this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		}
	}

	@Override
	protected JComponent createPanel() {
		this.tabs = new JTabbedPane(SwingConstants.BOTTOM);
		this.tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ElementsPanel p = getCurrentPanel();
				if (p != null) {
					SchemeMarqueeHandler handler = getMarqueeHandler();
					if (!handler.s.isSelected())
						handler.s.setSelected(true);
					handler.updateButtonsState(p.getGraph().getSelectionCells());
					
					SchemeResource res = p.getSchemeResource();
					try {
						switch (res.getCellContainerType()) {
							case SchemeResource.SCHEME:
								Scheme scheme = res.getScheme();
								if (scheme != null) {
									SchemeTabbedPane.this.aContext.getDispatcher().firePropertyChange(
											new ObjectSelectedEvent(this, 
													scheme, 
													SchemePropertiesManager.getInstance(SchemeTabbedPane.this.aContext), 
													ObjectSelectedEvent.SCHEME));
								}
								break;
							case SchemeResource.SCHEME_ELEMENT:
								SchemeElement SchemeElement = res.getSchemeElement();
								if (SchemeElement != null) {
									SchemeTabbedPane.this.aContext.getDispatcher().firePropertyChange(
											new ObjectSelectedEvent(this, 
													SchemeElement, 
													SchemeElementPropertiesManager.getInstance(SchemeTabbedPane.this.aContext), 
													ObjectSelectedEvent.SCHEME_ELEMENT));
								}
								break;
						}
					} catch (ApplicationException e1) {
						Log.errorMessage(e1);
					}
				}
			}
		});
		
		this.tabs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && SchemeTabbedPane.this.tabs.getTabCount() > 1) {
					JPopupMenu popup = new JPopupMenu();
					JMenuItem close = new JMenuItem(new AbstractAction() {
						private static final long serialVersionUID = 3655699666572424829L;

						public void actionPerformed(ActionEvent ae) {
							removePanel(getCurrentPanel(), true);
						}
					});
					close.setText(LangModelScheme.getString("Button.close") + " '" + SchemeTabbedPane.this.tabs.getTitleAt(SchemeTabbedPane.this.tabs.getSelectedIndex()) + "'");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
					popup.add(close);
					popup.show(SchemeTabbedPane.this.tabs, e.getX(), e.getY());
				}
			}
		});
		setLayout(new BorderLayout());
		
		stubPanel = new ElementsPanel(this.aContext);
		SchemeGraph graph = stubPanel.getGraph();
		graph.setEditable(false);
		graph.setGridVisible(false);
		graph.setActualSize(Constants.A4);
				
		super.panel = stubPanel;
//		add(this.tabs, BorderLayout.CENTER);
//		addPanel(new SchemePanel(aContext));
		return stubPanel.getGraph();
	}
	
	@Override
	protected JComponent createToolBar() {
		this.toolBar = new SchemeToolBar(this, this.aContext);
		return this.toolBar;
	}

	
	@Override
	public Set<ElementsPanel> getAllPanels() {
		Object[] comp = this.tabs.getComponents();
		Set<ElementsPanel> panels = new HashSet<ElementsPanel>(comp.length);
		for (int i = 0; i < comp.length; i++)
			panels.add(this.graphPanelsMap.get(comp[i]));
		return panels;
	}
	
	@Override
	public ElementsPanel getCurrentPanel() {
		if (this.tabs.getSelectedIndex() != -1)
			return this.graphPanelsMap.get(this.tabs.getComponentAt(this.tabs.getSelectedIndex()));
		return (ElementsPanel)this.panel;
//		SchemePanel newPanel = new SchemePanel(aContext);
//		addPanel(newPanel);
//		return newPanel;
	}
	
	public void addPanel(ElementsPanel p) {
		if (this.tabs.getParent() == null) {
			remove(stubPanel.getGraph());
			add(this.tabs, BorderLayout.CENTER);
		}
		
		SchemeGraph graph = p.getGraph();
		graph.setMarqueeHandler(this.marqueeHandler);
		graph.addKeyListener(this.keyListener);
		JScrollPane graphView = new JScrollPane(graph);
		
		if (this.graphPanelsMap == null)
			this.graphPanelsMap = new HashMap<JScrollPane, ElementsPanel>();
		this.graphPanelsMap.put(graphView, p);
		
		this.tabs.addTab("", new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/close_unchanged.gif")), graphView);
		this.tabs.setSelectedComponent(graphView);
		setGraphChanged(false);
		graph.setEditable(this.editable);
	}
	
	public void selectPanel(ElementsPanel p) {
		Object[] comp = this.tabs.getComponents();
		for (int i = 0; i < comp.length; i++) {
			UgoPanel p1 = this.graphPanelsMap.get(comp[i]);
			if (p1.equals(p)) {
				this.tabs.setSelectedIndex(i);
				return;
			}
		}
	}

	public void updateTitle(String title) {
		this.tabs.setTitleAt(this.tabs.getSelectedIndex(), title);
	}

	public boolean removePanel(UgoPanel p, boolean undo) {
		if (SchemePermissionManager.isSavingAllowed() && !confirmUnsavedChanges(p)) {
			return false;
		}
		
		// undo changes
		if (undo && SchemePermissionManager.isSavingAllowed() && hasUnsavedChanges(p)) {
			try {
				if (p instanceof ElementsPanel) {
					SchemeResource res = ((ElementsPanel)p).getSchemeResource();
					if (res.getCellContainerType() == SchemeResource.SCHEME) {
						Scheme scheme = res.getScheme();
						if (!scheme.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
							scheme.discardChanges();
						}
					} else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
						SchemeElement schemeElement = res.getSchemeElement();
						if (!schemeElement.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
							schemeElement.discardChanges();
						}
					} else if (res.getCellContainerType() == SchemeResource.SCHEME_PROTO_ELEMENT) {
						SchemeProtoElement schemeProtoElement = res.getSchemeProtoElement();
						if (!schemeProtoElement.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
							schemeProtoElement.discardChanges();
						}
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
		
		Object[] comp = this.tabs.getComponents();
		for (int i = 0; i < comp.length; i++) {
			UgoPanel p1 = this.graphPanelsMap.get(comp[i]);
			if (p1.equals(p)) {
				this.tabs.removeTabAt(i);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean confirmUnsavedChanges() {
		if (!SchemePermissionManager.isSavingAllowed()) {
			return true;
		}
		
		Object[] comp = this.tabs.getComponents();
		// check for unsaved changes
		for (int i = 0; i < comp.length; i++) {
			UgoPanel p = this.graphPanelsMap.get(comp[i]);
			if (super.hasUnsavedChanges(p)) {
				String text = LangModelScheme.getString("Message.confirmation.object_changed");  //$NON-NLS-1$
				return ClientUtils.showConfirmDialog(text);
			}
		}
		return true;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			try {
				SchemeEvent see = (SchemeEvent) ae;
				if (see.isType(SchemeEvent.OPEN_SCHEME)) {
					Scheme scheme = (Scheme)see.getStorableObject();
					openScheme(scheme);
				} else if (see.isType(SchemeEvent.OPEN_SCHEMEELEMENT)) {
					SchemeElement schemeElement = (SchemeElement)see.getStorableObject();
					openSchemeElement(schemeElement);
				} else if (see.isType(SchemeEvent.INSERT_SCHEME)) {
					Scheme scheme = (Scheme)see.getStorableObject();
					
					if (scheme.getUgoCell() == null) {
						JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
								LangModelScheme.getString("Message.error.scheme_insert_empty"),  //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					SchemeElement parent = scheme.getParentSchemeElement();
					
					ElementsPanel panel1 = getCurrentPanel();
					
					if (panel1.getSchemeResource().getCellContainerType() == SchemeResource.SCHEME) {
						Scheme parentScheme = panel1.getSchemeResource().getScheme();
						if (scheme.equals(parentScheme)) {
							Log.debugMessage("Try to insert scheme into itself " + scheme.getId(), Level.INFO); //$NON-NLS-1$
							JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
									LangModelScheme.getString("Message.error.scheme_insert_itself"),  //$NON-NLS-1$
									LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						if (parent == null || parentScheme.equals(parent.getParentScheme())) {
							SchemeElement schemeElement = parent == null 
									? SchemeObjectsFactory.createSchemeElement(parentScheme, scheme)
							    : parent;
							SchemeGraph graph = panel1.getGraph();
							SchemeActions.insertSEbyS(graph, schemeElement, see.getInsertionPoint(), true);
							graph.selectionNotify();
							setLinkMode();
						} else {
							Log.debugMessage("Try to insert already inserted scheme " + scheme.getId(), Level.INFO); //$NON-NLS-1$
							JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
									scheme.getName() + " " + LangModelScheme.getString("Message.error.scheme_already_inserted") + " " + parent.getParentScheme().getName(),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} else {
						Log.debugMessage("Try to insert schemeElement into component " + scheme.getId(), Level.INFO); //$NON-NLS-1$
						JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
								LangModelScheme.getString("Message.error.scheme_insert_component"),  //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
				} else if (see.isType(SchemeEvent.INSERT_SCHEMEELEMENT)) {
					SchemeElement schemeElement = (SchemeElement)see.getStorableObject();
					
					ElementsPanel panel1 = getCurrentPanel();
					if (panel1.getSchemeResource().getCellContainerType() == SchemeResource.SCHEME_ELEMENT &&
							schemeElement.equals(panel1.getSchemeResource().getSchemeElement())) {
						Log.debugMessage("Try to insert schemeElement into itself " + schemeElement.getId(), Level.INFO); //$NON-NLS-1$
						JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
								LangModelScheme.getString("Message.error.schemeelement_insert_itself"),  //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					SchemeResource res = panel1.getSchemeResource();
					if ((res.getCellContainerType() == SchemeResource.SCHEME &&
							res.getScheme().equals(schemeElement.getParentScheme())) ||
							(res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT &&
							res.getSchemeElement().equals(schemeElement.getParentSchemeElement()))) {
						SchemeImageResource image = schemeElement.getUgoCell();
						if (image == null) {
							image = schemeElement.getSchemeCell();
						}
						if (image == null) {
							Log.debugMessage("Try to insert schemeElement with empty imageresource" + schemeElement.getId(), Level.SEVERE); //$NON-NLS-1$
							return;
						}
						SchemeGraph graph = panel1.getGraph();
						SchemeActions.openSchemeImageResource(graph, image, true, see.getInsertionPoint(), false);
						setLinkMode();						
					} else {
						JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
								LangModelScheme.getString("Message.error.insert_component_to_other_parent"),  //$NON-NLS-1$
								LangModelScheme.getString("Message.error"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (see.isType(SchemeEvent.INSERT_PROTOELEMENT)) {
					SchemeProtoElement proto = (SchemeProtoElement)see.getStorableObject();
					ElementsPanel panel1 = getCurrentPanel();
					try {
						SchemeElement schemeElement = null;
						if (panel1.getSchemeResource().getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
							SchemeElement se = panel1.getSchemeResource().getSchemeElement();
							schemeElement = SchemeObjectsFactory.createSchemeElement(se, proto);
						} else if (panel1.getSchemeResource().getCellContainerType() == SchemeResource.SCHEME) {
							Scheme scheme = panel1.getSchemeResource().getScheme();
							schemeElement = SchemeObjectsFactory.createSchemeElement(scheme, proto);
						} else {
							Log.debugMessage(getClass().getSimpleName() + " | Unsupported CellContainerType " + panel1.getSchemeResource().getCellContainerType(), Level.FINER);
							return;
						}
						SchemeGraph graph = panel1.getGraph();
						SchemeActions.insertSEbyPE(graph, schemeElement, schemeElement.getClonedIdMap(), see.getInsertionPoint(), true);
						graph.selectionNotify();
					} catch (CreateObjectException e) {
						Log.errorMessage(e);
					}
					setLinkMode();
					return;
				} else if (see.isType(SchemeEvent.OPEN_PROTOELEMENT)) {
					// open proto is not allowed
					return;
				} else if (see.isType(SchemeEvent.INSERT_SCHEME_CABLELINK)) {
					SchemeCableLink schemeCableLink = (SchemeCableLink)see.getStorableObject();
					
					ElementsPanel panel1 = getCurrentPanel();
					SchemeResource res = panel1.getSchemeResource();
					if (res.getCellContainerType() != SchemeResource.SCHEME) {
						Log.debugMessage("Try to insert SchemeCableLink into SchemeElement " + schemeCableLink.getId(), Level.INFO); //$NON-NLS-1$
						JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
								LangModelScheme.getString("Message.error.schemecablelink_insert_schemeelement"),  //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (res.getScheme().equals(schemeCableLink.getParentScheme())) {
						SchemeGraph graph = panel1.getGraph();
						
						SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
						SchemeCablePort targetPort = schemeCableLink.getTargetAbstractSchemePort();
						CablePortCell sourcePortCell = null;
						CablePortCell targetPortCell = null;
						PortView sourceView = null;
						PortView targetView = null;
						if (sourcePort != null) {
							SchemeActions.performAutoCommutation(sourcePort, schemeCableLink, true);
							
							sourcePortCell = SchemeActions.findCablePortCellById(graph, sourcePort.getId());
							try {
								DefaultPort source = SchemeActions.getSuitablePort(sourcePortCell, schemeCableLink.getId());
								sourceView = (PortView)graph.getGraphLayoutCache().getMapping(source, false);
							} catch (CreateObjectException e) {
								Log.errorMessage(e.getMessage());
								return;
							}
						}
						if (targetPort != null) {
							SchemeActions.performAutoCommutation(targetPort, schemeCableLink, true);
							
							targetPortCell = SchemeActions.findCablePortCellById(graph, targetPort.getId());
							try {
								DefaultPort target = SchemeActions.getSuitablePort(targetPortCell, schemeCableLink.getId());
								targetView = (PortView)graph.getGraphLayoutCache().getMapping(target, false);
							} catch (CreateObjectException e) {
								Log.errorMessage(e.getMessage());
								return;
							}
						}
						
						Point p = see.getInsertionPoint();
						int d = graph.getGridSize();
						Point p1 = sourceView == null ? new Point(p.x - 2 * d, p.y) : sourceView.getBounds().getLocation();
						Point p2 = targetView == null ? new Point(p.x + 2 * d, p.y) : targetView.getBounds().getLocation();
						try {
							DefaultCableLink cell = SchemeActions.createCableLink(graph,
									sourceView, targetView, graph.snap(graph.fromScreen(p1)), 
									graph.snap(graph.fromScreen(p2)), schemeCableLink.getId(), true);
							cell.setUserObject(schemeCableLink.getName());
							if (sourcePortCell != null) {
								GraphActions.setObjectBackColor(graph, sourcePortCell, SchemeActions.determinePortColor(sourcePort, schemeCableLink));
							}
							if (targetPortCell != null) {
								GraphActions.setObjectBackColor(graph, targetPortCell, SchemeActions.determinePortColor(targetPort, schemeCableLink));
							}
						} catch (CreateObjectException e) {
							Log.errorMessage(e.getMessage());
						}
					} else {
						JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
								LangModelScheme.getString("Message.error.insert_cable_to_other_parent"),  //$NON-NLS-1$
								LangModelScheme.getString("Message.error"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
					}
				}	else if (see.isType(SchemeEvent.UPDATE_OBJECT)) {
					Identifier id = see.getIdentifier();
					if (id.getMajor() == ObjectEntities.SCHEME_CODE) {
						Scheme scheme = (Scheme)see.getStorableObject();
						
						Object[] comp = this.tabs.getComponents();
						for (int i = 0; i < comp.length; i++) {
							ElementsPanel p = this.graphPanelsMap.get(comp[i]);
							if (scheme.equals(p.getSchemeResource().getScheme())) {
								p.getGraph().setActualSize(new Dimension(scheme.getWidth(), scheme.getHeight()));
								this.tabs.setTitleAt(i, scheme.getName());
								setGraphChanged(p.getGraph(), true);
								break;
							}							
						}
					}
				}
				else if (see.isType(SchemeEvent.DELETE_OBJECT)) {
					Identifier id = see.getIdentifier();
					if (id.getMajor() == ObjectEntities.SCHEME_CODE) {
						Scheme scheme = (Scheme)see.getStorableObject();
						
						Object[] comp = this.tabs.getComponents();
						for (int i = 0; i < comp.length; i++) {
							ElementsPanel p = this.graphPanelsMap.get(comp[i]);
							if (scheme.equals(p.getSchemeResource().getScheme())) {
								this.tabs.remove(i);
								if (this.tabs.getTabCount() == 0) {
									if (this.tabs.getParent() != null) {
										remove(this.tabs);
										add(stubPanel.getGraph(), BorderLayout.CENTER);
									}
//									ApplicationModel aModel = this.aContext.getApplicationModel();
//									aModel.getCommand("menuSchemeNew").execute();
								}
								break;
							}							
						}
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		} else if (ae.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ose = (ObjectSelectedEvent) ae;
			if (ose.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				setPathMode();
				try {
					SchemePath path = StorableObjectPool.getStorableObject(ose.getSelectedObject().getId(), false);
					SchemeResource.setSchemePath(path, SchemeResource.isPathEditing());
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
		super.propertyChange(ae);
	}
	

	
	public void setPathMode() {
		AbstractButton b = this.toolBar.commands.get(Constants.PATH_MODE);
		if (!b.isSelected()) {
			b.doClick();
		}
	}
	
	public void setLinkMode() {
		AbstractButton b = this.toolBar.commands.get(Constants.LINK_MODE);
		if (!b.isSelected()) {
			b.doClick();
		}
	}

	static int counter = 0;
	public Map<DefaultGraphCell, DefaultGraphCell> openScheme(final Scheme sch) throws ApplicationException {

//		if (counter == 0 && sch.getName().startsWith("UCM")) {
//			try {
//				counter++;
//				SchemeActions.putToGraph(sch, this);
//				return null;
//			} catch (Exception e) {
//				Log.errorMessage(e);
//			}
//		}
		
		Map<DefaultGraphCell, DefaultGraphCell> clones = Collections.emptyMap();
		Set panels = getAllPanels();
		for (Iterator it = panels.iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			if (p instanceof SchemePanel) {
				SchemePanel panel1 = (SchemePanel)p;
				if (sch.equals(panel1.getSchemeResource().getScheme())) {
					selectPanel(panel1);
//					if (p.getGraph().isGraphChanged()) {
//						int ret = JOptionPane.showConfirmDialog(
//								AbstractMainFrame.getActiveMainFrame(), "Схема " + sch.getName()
//									+ " уже открыта. Открыть сохраненную ранее версию?",
//									"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
//						if (ret == JOptionPane.YES_OPTION) {
//							panel1.getSchemeResource().setScheme(sch);
//							SchemeGraph graph = panel1.getGraph();
//							clones = SchemeActions.openSchemeImageResource(graph, sch.getSchemeCell(), false);
//						}		
//					}
					return clones;
				}
			}				
		}

		SchemePanel panel1 = new SchemePanel(this.aContext);
		addPanel(panel1);
		panel1.getSchemeResource().setScheme(sch);
		updateTitle(sch.getName());
		final SchemeGraph graph = panel1.getGraph();
		graph.addMouseWheelListener(this.marqueeHandler);
		
		new ProcessingDialogDummy(new Runnable() {
			public void run() {
				SchemeActions.openSchemeImageResource(graph, sch.getSchemeCell(), false);		
			}
		}, LangModelScheme.getString("Message.information.open_scheme"));
		
		panel1.setGraphSize(new Dimension(sch.getWidth(), sch.getHeight()));
		
		setEditable(SchemePermissionManager.isEditionAllowed());
		
		return clones;
	}
	
	public Map<DefaultGraphCell, DefaultGraphCell> openSchemeElement(SchemeElement se) throws ApplicationException {
		Map<DefaultGraphCell, DefaultGraphCell> clones = Collections.emptyMap();
		Set panels = getAllPanels();
		for (Iterator it = panels.iterator(); it.hasNext();) {
			UgoPanel p1 = (UgoPanel)it.next();
			if (p1 instanceof ElementsPanel) {
				ElementsPanel panel1 = (ElementsPanel)p1;
				if (se.equals(panel1.getSchemeResource().getSchemeElement())) {
					selectPanel(panel1);
					if (panel1.getGraph().isGraphChanged()) {
						int ret = JOptionPane.showConfirmDialog(
								AbstractMainFrame.getActiveMainFrame(), "Элемент " + se.getName()
								+ " уже открыт. Открыть сохраненную ранее версию?",
								"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							panel1.getSchemeResource().setSchemeElement(se);
							SchemeGraph graph = panel1.getGraph();
							clones = SchemeActions.openSchemeImageResource(graph, se.getSchemeCell(), false);
						}
					}
					return clones;
				}
			}
		}
		ElementsPanel panel1 = new ElementsPanel(this.aContext);
		addPanel(panel1);
		panel1.getSchemeResource().setSchemeElement(se);
		updateTitle(se.getName());
		SchemeGraph graph = panel1.getGraph();
		clones = SchemeActions.openSchemeImageResource(graph, se.getSchemeCell(), false);
		
		setEditable(SchemePermissionManager.isEditionAllowed());
		return clones;
	}
	/*
	public boolean removeScheme(Scheme sch) {
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getScheme() != null && sch != null
				&& sch.equals(graph.getScheme())) {
			removePanel(getPanel());
			if (tabs.getTabCount() == 0)
				setGraphChanged(false);
			return true;
		}
		return false;
	}

	public boolean removeSchemeElement(SchemeElement se) {
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getSchemeElement() != null && se != null
				&& se.equals(graph.getSchemeElement())) {
			removePanel(getPanel());
			if (tabs.getTabCount() == 0)
				setGraphChanged(false);
			return true;
		}
		return false;
	}*/
	
	public void setGraphChanged(SchemeGraph graph, boolean b) {
		for (int i = 0; i < this.tabs.getTabCount(); i++) {
			UgoPanel p = this.graphPanelsMap.get(this.tabs.getComponentAt(i));
			if (graph.equals(p.getGraph())) {
				graph.setGraphChanged(b);
				this.tabs.setIconAt(i, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							b ? "images/close_changed.gif" : "images/close_unchanged.gif")));
				return;
			}
		}
	}
	
	@Override
	public void setEditable(boolean b) {
		super.setEditable(b);
		
		String[] editableButtons = new String[] {
				Constants.RACK, Constants.CABLE, Constants.TOP_LEVEL_MODE
		};
		
		for (String key : editableButtons) {
			AbstractButton button = this.toolBar.commands.get(key);
			button.setVisible(b);
		}
	}
	
	@Override
	public void setGraphChanged(boolean b) {
		SchemeGraph graph = getGraph();
		if (!graph.isEditable() || (b && graph.isGraphChanged()))
			return;

		super.setGraphChanged(b);
		this.tabs.setIconAt(this.tabs.getSelectedIndex(), new ImageIcon(Toolkit
				.getDefaultToolkit().getImage(b ? "images/close_changed.gif" : "images/close_unchanged.gif")));
	}
}
