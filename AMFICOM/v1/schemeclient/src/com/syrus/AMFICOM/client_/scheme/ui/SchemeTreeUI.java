/*-
 * $Id: SchemeTreeUI.java,v 1.39 2006/05/02 07:22:01 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.ElementsPermissionManager;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.39 $, $Date: 2006/05/02 07:22:01 $
 * @module schemeclient
 */

public class SchemeTreeUI extends IconedTreeUI {
	ApplicationContext aContext;
	JToolBar toolBar;
	
	public SchemeTreeUI(Item rootItem, ApplicationContext aContext) {
		super(rootItem);
		this.treeUI.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		new SchemeTreeSelectionListener(this, aContext);
		setContext(aContext);
	}
	
	void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	@Override
	public JToolBar getToolBar() {
		if (this.toolBar == null) {
			this.toolBar = super.getToolBar();
			final JButton deleteButton = new JButton();
			deleteButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_DELETE));
			deleteButton.setToolTipText(I18N.getString("Delete"));
			deleteButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TreePath[] selectedPaths = SchemeTreeUI.this.treeUI.getTree().getSelectionModel().getSelectionPaths();
					TreePath selectedPath1 = SchemeTreeUI.this.treeUI.getTree().getSelectionModel().getSelectionPath();
										
					for (TreePath selectedPath : selectedPaths) {
						Item item = (Item)selectedPath.getLastPathComponent();
						Object object = item.getObject();
						if (object instanceof Scheme) {
							if (!SchemePermissionManager.isDeletionAllowed()) {
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								Log.debugMessage("Scheme deletion is not allowed", Level.FINER);
								return;
							}
							
							Scheme scheme = (Scheme)object;
							try {
								LinkedIdsCondition condition = new LinkedIdsCondition(scheme.getId(), 
										ObjectEntities.MAPVIEW_CODE);
								Set<MapView> views = StorableObjectPool.getStorableObjectsByCondition(condition, false);
								
								if (views.isEmpty() && scheme.getParentSchemeElement() == null) {
									int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
											LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
											LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
											JOptionPane.OK_CANCEL_OPTION);
									if (res == JOptionPane.CANCEL_OPTION) {
										return;
									}
									
									// отцепляем линки
									for (SchemeCableLink link : scheme.getSchemeCableLinks(false)) {
										link.setSourceAbstractSchemePort(null);
										link.setTargetAbstractSchemePort(null);
									}
									for (SchemeLink link : scheme.getSchemeLinks(false)) {
										link.setSourceAbstractSchemePort(null);
										link.setTargetAbstractSchemePort(null);
									}

									SchemeTreeUI.this.aContext.getDispatcher().firePropertyChange(
											new SchemeEvent(this, scheme.getId(), SchemeEvent.DELETE_OBJECT));
									
									Set<Identifiable> ids = scheme.getReverseDependencies(false);
									StorableObjectPool.delete(ids);
									
									Identifier userId = LoginManager.getUserId();
									StorableObjectPool.flush(ids, userId, false);
								} else {
									JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
											LangModelScheme.getString("Message.error.delete.scheme"),
											LangModelScheme.getString("Message.error"),
											JOptionPane.OK_OPTION);
									Log.debugMessage("Can not delete Scheme as there is parent Scheme or MapView", Level.WARNING);
									return;
								}
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof SchemePath) {
							if (!SchemePermissionManager.isEditionAllowed()) {
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								Log.debugMessage("SchemePath deletion is not allowed", Level.FINER);
								return;
							}
							
							try {
								int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
										JOptionPane.OK_CANCEL_OPTION);
								if (res == JOptionPane.CANCEL_OPTION) {
									return;
								}
								
								SchemePath path = (SchemePath)object;
								Set<Identifiable> ids = path.getReverseDependencies(false);
								
								SchemeTreeUI.this.aContext.getDispatcher().firePropertyChange(
										new SchemeEvent(this, path.getId(), SchemeEvent.DELETE_OBJECT));
								
								StorableObjectPool.delete(ids);
								StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof SchemeProtoElement) {
							if (!ElementsPermissionManager.isEditionAllowed()) {
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								Log.debugMessage("SchemeProtoElement deletion is not allowed", Level.FINER);
								return;
							}
							
							try {
								int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
										JOptionPane.OK_CANCEL_OPTION);
								if (res == JOptionPane.CANCEL_OPTION) {
									return;
								}
								
								SchemeProtoElement proto = (SchemeProtoElement)object;
								Set<Identifiable> ids = proto.getReverseDependencies(false);

								SchemeTreeUI.this.aContext.getDispatcher().firePropertyChange(
										new SchemeEvent(this, proto.getId(), SchemeEvent.DELETE_OBJECT));
								
								StorableObjectPool.delete(ids);
								StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof SchemeProtoGroup) {
							if (!ElementsPermissionManager.isEditionAllowed()) {
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								Log.debugMessage("SchemeProtoGroup deletion is not allowed", Level.FINER);
								return;
							}
							
							SchemeProtoGroup group = (SchemeProtoGroup)object;
							try {
								if (group.getSchemeProtoElements(false).isEmpty() && 
									group.getSchemeProtoGroups(false).isEmpty()) {
									int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
											LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
											LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
											JOptionPane.OK_CANCEL_OPTION);
									if (res == JOptionPane.CANCEL_OPTION) {
										return;
									}
									
									Set<Identifiable> ids = group.getReverseDependencies(false);
									
									SchemeTreeUI.this.aContext.getDispatcher().firePropertyChange(
											new SchemeEvent(this, group.getId(), SchemeEvent.DELETE_OBJECT));
									
									StorableObjectPool.delete(ids);
									StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
								} else {
									JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
											LangModelScheme.getString("Message.error.delete.proto_group"),
											LangModelScheme.getString("Message.error"),
											JOptionPane.OK_OPTION);
									Log.debugMessage("Can not delete ProtoGroup as there are PropoElement(s) or ProtoGroup(s) in it", Level.WARNING);
								}
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof ProtoEquipment) {
							if (!ElementsPermissionManager.isTypeEditionAllowed()) {
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								Log.debugMessage("ProtoEquipment deletion is not allowed", Level.FINER);
								return;
							}
							
							ProtoEquipment protoEq = (ProtoEquipment)object;
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(protoEq.getId(), ObjectEntities.SCHEMEPROTOELEMENT_CODE);
								Set<SchemeProtoElement> protos = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (protos.isEmpty()) {
									LinkedIdsCondition condition2 = new LinkedIdsCondition(protoEq.getId(), ObjectEntities.SCHEMEELEMENT_CODE);
									Set<SchemeElement> schemeElements = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
									if (schemeElements.isEmpty()) {
										int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
												LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
												LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
												JOptionPane.OK_CANCEL_OPTION);
										if (res == JOptionPane.CANCEL_OPTION) {
											return;
										}

										StorableObjectPool.delete(protoEq.getId());
										StorableObjectPool.flush(protoEq, LoginManager.getUserId(), false);
									} else {
										JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
												LangModelScheme.getString("Message.error.delete.proto_equipment"),
												LangModelScheme.getString("Message.error"),
												JOptionPane.OK_OPTION);
										Log.debugMessage("Can not delete ProtoEquipmet as there are PropoElements with such type", Level.WARNING);
									}
								} else {
									JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
											LangModelScheme.getString("Message.error.delete.proto_equipment"),
											LangModelScheme.getString("Message.error"),
											JOptionPane.OK_OPTION);
									Log.debugMessage("Can not delete ProtoEquipmet as there are PropoElements with such type", Level.WARNING);
									return;
								}
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof LinkType) {
							if (!ElementsPermissionManager.isTypeEditionAllowed()) {
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								Log.debugMessage("LinkType deletion is not allowed", Level.FINER);
								return;
							}
							
							LinkType type = (LinkType)object;
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(type.getId(), ObjectEntities.SCHEMELINK_CODE);
								Set<SchemeLink> links = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (links.isEmpty()) {
									int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
											LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
											LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
											JOptionPane.OK_CANCEL_OPTION);
									if (res == JOptionPane.CANCEL_OPTION) {
										return;
									}

									StorableObjectPool.delete(type.getId());
									StorableObjectPool.flush(type, LoginManager.getUserId(), false);
								} else {
									JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
											LangModelScheme.getString("Message.error.delete.link_type"),
											LangModelScheme.getString("Message.error"),
											JOptionPane.OK_OPTION);
									Log.debugMessage("Can not delete LinkType as there are SchemeLinks with such type", Level.WARNING);
									return;
								}
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof CableLinkType) {
							if (!ElementsPermissionManager.isTypeEditionAllowed()) {
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								Log.debugMessage("CableLinkType deletion is not allowed", Level.FINER);
								return;
							}
							
							CableLinkType type = (CableLinkType)object;
							
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(type.getId(), ObjectEntities.SCHEMECABLELINK_CODE);
								Set<SchemeCableLink> links = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (links.isEmpty()) {
									int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
											LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
											LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
											JOptionPane.OK_CANCEL_OPTION);
									if (res == JOptionPane.CANCEL_OPTION) {
										return;
									}
									
									Set<CableThreadType> threadTypes = type.getCableThreadTypes(false);
									Set<Identifier> ids = new HashSet<Identifier>();
									for (CableThreadType ctt : threadTypes) {
										ids.add(ctt.getId());
									}
									ids.add(type.getId());
									StorableObjectPool.delete(ids);
									StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
								} else {
									JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
											LangModelScheme.getString("Message.error.delete.cable_link_type"),
											LangModelScheme.getString("Message.error"),
											JOptionPane.OK_OPTION);
									Log.debugMessage("Can not delete CableLinkType as there are SchemeCableLinks with such type", Level.WARNING);
									return;
								}
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof PortType) {
							if (!ElementsPermissionManager.isTypeEditionAllowed()) {
								Log.debugMessage("PortType deletion is not allowed", Level.FINER);
								JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
										LangModelScheme.getString("Message.error.access_denied"),  //$NON-NLS-1$ 
										LangModelScheme.getString("Message.error"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							PortType type = (PortType)object;
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(type.getId(), ObjectEntities.SCHEMEPORT_CODE);
								Set<SchemePort> ports = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (ports.isEmpty()) {
									LinkedIdsCondition condition2 = new LinkedIdsCondition(type.getId(), ObjectEntities.SCHEMECABLEPORT_CODE);
									Set<SchemeCablePort> cablePorts = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
									if (cablePorts.isEmpty()) {
										int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
												LangModelScheme.getString("Message.confirmation.sure_delete"),  //$NON-NLS-1$ 
												LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
												JOptionPane.OK_CANCEL_OPTION);
										if (res == JOptionPane.CANCEL_OPTION) {
											return;
										}
										StorableObjectPool.delete(type.getId());
										StorableObjectPool.flush(type, LoginManager.getUserId(), false);
									} else {
										JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
												LangModelScheme.getString("Message.error.delete.port_type"),
												LangModelScheme.getString("Message.error"),
												JOptionPane.OK_OPTION);
										Log.debugMessage("Can not delete PortType as there are SchemeCablePorts with such type", Level.WARNING);
										return;
									}
								} else {
									JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
											LangModelScheme.getString("Message.error.delete.port_type"),
											LangModelScheme.getString("Message.error"),
											JOptionPane.OK_OPTION);
									Log.debugMessage("Can not delete PortType as there are SchemePorts with such type", Level.WARNING);
									return;
								}
							} catch (ApplicationException e1) {
								Log.errorMessage(e1);
							}
						} else if (object instanceof MeasurementType
									|| object instanceof MeasurementPortType) {
							JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
									LangModelScheme.getString("Message.error.object_undeletable"),  //$NON-NLS-1$ 
									LangModelScheme.getString("Message.error"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							Log.debugMessage("Measurement objects deletion is not allowed", Level.FINER);
							return;
						}
					}
					if (selectedPath1 != null) {
						TreePath parentPath = selectedPath1.getParentPath();
						SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
						updateRecursively((Item)parentPath.getLastPathComponent());
					}
				}
			});
			this.toolBar.add(deleteButton);
		}
		return this.toolBar;
	}
}
