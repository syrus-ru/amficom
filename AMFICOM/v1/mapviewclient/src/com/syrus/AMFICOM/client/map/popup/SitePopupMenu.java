package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;

public final class SitePopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem attachCableInletMenuItem = new JMenuItem();

	private SiteNode site;

	private static SitePopupMenu instance = new SitePopupMenu();

	private SitePopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static SitePopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.site = (SiteNode )me;
		SiteNodeTypeSort sort = this.site.getType().getSort();
		this.attachCableInletMenuItem.setVisible(
				sort.value() == SiteNodeTypeSort._ATS
				|| sort.value() == SiteNodeTypeSort._BUILDING);
	}

	private void jbInit() {
		this.attachCableInletMenuItem.setText(LangModelMap.getString(MapEditorResourceKeys.POPUP_ATTACH_CABLE_INLET));
		this.attachCableInletMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					attachCableInlet();
				} catch(MapException ex) {
					ex.printStackTrace();
				}
			}
		});
		this.removeMenuItem.setText(LangModelMap.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSite();
			}
		});
		this.add(this.attachCableInletMenuItem);
		this.add(this.removeMenuItem);
	}

	protected void attachCableInlet() throws MapConnectionException, MapDataException {
		SiteNodeType siteNodeType = super.selectAttachedSiteNodeType();

		if(siteNodeType != null) {
			super.createAttachedSiteNode(this.site, siteNodeType);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeSite() {
		super.removeMapElement(this.site);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}
}
