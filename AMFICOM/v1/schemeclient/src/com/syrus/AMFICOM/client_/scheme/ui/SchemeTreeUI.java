/*-
 * $Id: SchemeTreeUI.java,v 1.17 2005/09/20 13:04:29 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.17 $, $Date: 2005/09/20 13:04:29 $
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
								StorableObjectPool.delete(proto.getId());
								TreePath parentPath = selectedPath.getParentPath();
								SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
								updateRecursively((Item)parentPath.getLastPathComponent());
								StorableObjectPool.flush(proto, LoginManager.getUserId(), true);
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
									StorableObjectPool.delete(group.getId());
									TreePath parentPath = selectedPath.getParentPath();
									SchemeTreeUI.this.treeUI.getTree().setSelectionPath(parentPath);
									updateRecursively((Item)parentPath.getLastPathComponent());
									StorableObjectPool.flush(group, LoginManager.getUserId(), true);
								} catch (ApplicationException e1) {
									Log.errorException(e1);
								}
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
