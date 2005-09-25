package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNodeType;

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
