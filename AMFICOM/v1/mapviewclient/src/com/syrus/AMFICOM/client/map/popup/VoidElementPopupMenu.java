/*-
 * $$Id: VoidElementPopupMenu.java,v 1.24 2005/09/30 16:08:40 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * @version $Revision: 1.24 $, $Date: 2005/09/30 16:08:40 $
 * @author $Author: krupenn $
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
			e.printStackTrace();
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
		this.addSiteMenuItem.setText(LangModelMap.getString(MapEditorResourceKeys.POPUP_ADD_SITE));
		this.addSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSite();
			}
		});
		this.add(this.addSiteMenuItem);
	}

	void addSite() {
		SiteNodeType siteNodeType = super.selectSiteNodeType();

		if(siteNodeType != null) {
			super.createSiteNode(siteNodeType, this.point);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
