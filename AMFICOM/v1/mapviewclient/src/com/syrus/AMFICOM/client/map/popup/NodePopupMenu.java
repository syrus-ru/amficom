/*-
 * $$Id: NodePopupMenu.java,v 1.22 2005/10/31 15:29:31 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * @version $Revision: 1.22 $, $Date: 2005/10/31 15:29:31 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class NodePopupMenu extends MapPopupMenu {
	private JMenuItem placeSiteMenuItem = new JMenuItem();

	private JMenuItem removeMenuItem = new JMenuItem();

	private TopologicalNode node;

	private static NodePopupMenu instance = new NodePopupMenu();

	private NodePopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static NodePopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.node = (TopologicalNode )me;
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeNode();
			}
		});

		this.placeSiteMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_PLACE_SITE));
		this.placeSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				placeSite();
			}
		});

		this.add(this.removeMenuItem);
		this.add(this.placeSiteMenuItem);
	}

	void removeNode() {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		super.removeMapElement(this.node);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void placeSite() {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			super.insertSiteInPlaceOfANode(this.node, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
