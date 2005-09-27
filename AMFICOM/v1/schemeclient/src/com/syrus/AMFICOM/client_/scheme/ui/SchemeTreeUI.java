/*-
 * $Id: SchemeTreeUI.java,v 1.19 2005/09/27 06:50:45 stas Exp $
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
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.19 $, $Date: 2005/09/27 06:50:45 $
 * @module schemeclient
 */

public class SchemeTreeUI extends IconedTreeUI {
	ApplicationContext aContext;
	JToolBar toolBar;
	
	public SchemeTreeUI(Item rootItem, ApplicationContext aContext) {
		super(rootItem);
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
			deleteButton.setToolTipText(LangModelGeneral.getString("Delete"));
			deleteButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TreePath selectedPath = SchemeTreeUI.this.treeUI.getTree().getSelectionModel().getSelectionPath();
					if (selectedPath != null) {
						Item item = (Item)selectedPath.getLastPathComponent();
						Object object = item.getObject();
						if (object instanceof Scheme) {
							Scheme scheme = (Scheme)object;
							try {
								if (scheme.getParentSchemeElement() == null) {
									// отцепляем линки
									for (SchemeCableLink link : scheme.getSchemeCableLinks()) {
										link.setSourceAbstractSchemePort(null);
										link.setTargetAbstractSchemePort(null);
									}
									for (SchemeLink link : scheme.getSchemeLinks()) {
										link.setSourceAbstractSchemePort(null);
										link.setTargetAbstractSchemePort(null);
									}
									
									Set<Identifiable> ids = scheme.getReverseDependencies();
									StorableObjectPool.delete(ids);
									TreePath parentPath = selectedPath.getParentPath();
									SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
									updateRecursively((Item)parentPath.getLastPathComponent());
									
									Identifier userId = LoginManager.getUserId();
									for (Identifiable id : ids) {
										StorableObjectPool.flush(id, userId, false);
									}
								}
							} catch (ApplicationException e1) {
								Log.errorException(e1);
							}
						}
						else if (object instanceof SchemeProtoElement) {
							try {
								SchemeProtoElement proto = (SchemeProtoElement)object;
								Set<Identifiable> ids = proto.getReverseDependencies();
								StorableObjectPool.delete(ids);
								StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
								TreePath parentPath = selectedPath.getParentPath();
								SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
								updateRecursively((Item)parentPath.getLastPathComponent());
							} catch (ApplicationException e1) {
								Log.errorException(e1);
							}
						} else if (object instanceof SchemeProtoGroup) {
							SchemeProtoGroup group = (SchemeProtoGroup)object;
							if (group.getSchemeProtoElements().isEmpty() && 
									group.getSchemeProtoGroups().isEmpty()) {
								try {
									Set<Identifiable> ids = group.getReverseDependencies();
									StorableObjectPool.delete(ids);
									StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
									TreePath parentPath = selectedPath.getParentPath();
									SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
									updateRecursively((Item)parentPath.getLastPathComponent());
								} catch (ApplicationException e1) {
									Log.errorException(e1);
								}
							}
						} else if (object instanceof EquipmentType) {
							EquipmentType eqt = (EquipmentType)object;
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(eqt.getId(), ObjectEntities.SCHEMEPROTOELEMENT_CODE);
								Set<SchemeProtoElement> protos = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (protos.isEmpty()) {
									LinkedIdsCondition condition2 = new LinkedIdsCondition(eqt.getId(), ObjectEntities.SCHEMEELEMENT_CODE);
									Set<SchemeElement> schemeElements = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
									if (schemeElements.isEmpty()) {
										StorableObjectPool.delete(eqt.getId());
										StorableObjectPool.flush(eqt, LoginManager.getUserId(), false);
										
										TreePath parentPath = selectedPath.getParentPath();
										SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
										updateRecursively((Item)parentPath.getLastPathComponent());
									} else {
										Log.debugMessage("Can not delete EquipmetType as there are SchemeElements with such type", Level.WARNING);
									}
								} else {
									Log.debugMessage("Can not delete EquipmetType as there are PropoElements with such type", Level.WARNING);
								}
							} catch (ApplicationException e1) {
								Log.errorException(e1);
							}
						} else if (object instanceof LinkType) {
							LinkType type = (LinkType)object;
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(type.getId(), ObjectEntities.SCHEMELINK_CODE);
								Set<SchemeLink> links = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (links.isEmpty()) {
									StorableObjectPool.delete(type.getId());
									StorableObjectPool.flush(type, LoginManager.getUserId(), false);
									
									TreePath parentPath = selectedPath.getParentPath();
									SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
									updateRecursively((Item)parentPath.getLastPathComponent());
								} else {
									Log.debugMessage("Can not delete LinkType as there are SchemeLinks with such type", Level.WARNING);
								}
							} catch (ApplicationException e1) {
								Log.errorException(e1);
							}
						} else if (object instanceof CableLinkType) {
							CableLinkType type = (CableLinkType)object;
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(type.getId(), ObjectEntities.SCHEMECABLELINK_CODE);
								Set<SchemeCableLink> links = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (links.isEmpty()) {
									Set<CableThreadType> threadTypes = type.getCableThreadTypes(false);
									Set<Identifier> ids = new HashSet<Identifier>();
									for (CableThreadType ctt : threadTypes) {
										ids.add(ctt.getId());
									}
									ids.add(type.getId());
									StorableObjectPool.delete(ids);
									StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
									
									TreePath parentPath = selectedPath.getParentPath();
									SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
									updateRecursively((Item)parentPath.getLastPathComponent());
								} else {
									Log.debugMessage("Can not delete CableLinkType as there are SchemeCableLinks with such type", Level.WARNING);
								}
							} catch (ApplicationException e1) {
								Log.errorException(e1);
							}
						} else if (object instanceof PortType) {
							PortType type = (PortType)object;
							try {
								LinkedIdsCondition condition1 = new LinkedIdsCondition(type.getId(), ObjectEntities.SCHEMEPORT_CODE);
								Set<SchemePort> ports = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
								if (ports.isEmpty()) {
									StorableObjectPool.delete(type.getId());
									StorableObjectPool.flush(type, LoginManager.getUserId(), false);
									
									TreePath parentPath = selectedPath.getParentPath();
									SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
									updateRecursively((Item)parentPath.getLastPathComponent());
								} else {
									Log.debugMessage("Can not delete PortType as there are SchemePorts with such type", Level.WARNING);
								}
							} catch (ApplicationException e1) {
								Log.errorException(e1);
							}
						}
					}
				}
			});
			this.toolBar.add(deleteButton);
		}
		return this.toolBar;
	}
}
