/*-
 * $$Id: VoidElementPopupMenu.java,v 1.28 2006/06/08 12:32:53 stas Exp $$
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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.28 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class VoidElementPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 8357726734946724923L;

	private JMenuItem addSiteMenuItem = new JMenuItem();

	private static VoidElementPopupMenu instance;

	private VoidElementPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static VoidElementPopupMenu getInstance() {
		if (instance == null) {
			instance = new VoidElementPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		final boolean editable = isEditable();		
		this.addSiteMenuItem.setVisible(editable);
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
		SiteNodeType siteNodeType = super.selectSiteNodeType();
		if(siteNodeType != null) {
			super.createSiteNode(siteNodeType, this.point);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
