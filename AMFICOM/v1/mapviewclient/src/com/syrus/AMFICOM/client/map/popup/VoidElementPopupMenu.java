/*-
 * $$Id: VoidElementPopupMenu.java,v 1.27 2006/02/15 11:12:25 stas Exp $$
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27 $, $Date: 2006/02/15 11:12:25 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class VoidElementPopupMenu extends MapPopupMenu {

	private JMenuItem addSiteMenuItem = new JMenuItem();

	private static VoidElementPopupMenu instance = new VoidElementPopupMenu();

	private VoidElementPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static VoidElementPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		// empty
	}

	private void jbInit() {
		this.addSiteMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_SITE));
		this.addSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSite();
			}
		});
		this.add(this.addSiteMenuItem);
	}

	void addSite() {
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
		SiteNodeType siteNodeType = super.selectSiteNodeType();

		if(siteNodeType != null) {
			super.createSiteNode(siteNodeType, this.point);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
