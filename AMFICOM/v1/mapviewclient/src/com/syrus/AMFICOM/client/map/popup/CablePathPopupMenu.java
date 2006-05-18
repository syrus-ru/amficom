/*-
 * $$Id: CablePathPopupMenu.java,v 1.30 2006/02/15 11:12:25 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.30 $, $Date: 2006/02/15 11:12:25 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CablePathPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 7578207099905441307L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private CablePath cablePath;

	private static CablePathPopupMenu instance = new CablePathPopupMenu();

	private CablePathPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static CablePathPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object object) {
		this.cablePath = (CablePath) object;

		boolean canGenerate = false;
		try {
			for(Iterator it = this.cablePath.getLinks().iterator(); it.hasNext();) {
				Object link = it.next();
				if(link instanceof UnboundLink)
					canGenerate = true;
			}
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
		this.generateMenuItem.setVisible(canGenerate);
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeCablePath();
			}
		});
		this.generateMenuItem
				.setText(I18N.getString(MapEditorResourceKeys.POPUP_GENERATE_CABLING));
		this.generateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateCabling();
			}
		});
		this.add(this.removeMenuItem);
		this.add(this.generateMenuItem);
	}

	void removeCablePath() {
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
		super.removeMapElement(this.cablePath);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(
				MapEvent.MAP_CHANGED);
	}

	void generateCabling() {
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
			super.generatePathCabling(this.cablePath, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(
					MapEvent.MAP_CHANGED);
		}
	}

}
