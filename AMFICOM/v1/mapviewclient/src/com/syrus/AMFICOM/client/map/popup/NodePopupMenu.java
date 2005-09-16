package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;

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
		this.removeMenuItem.setText(LangModelMap.getString("Delete")); //$NON-NLS-1$
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeNode();
			}
		});

		this.placeSiteMenuItem.setText(LangModelMap.getString("PlaceSite")); //$NON-NLS-1$
		this.placeSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				placeSite();
			}
		});

		this.add(this.removeMenuItem);
		this.add(this.placeSiteMenuItem);
	}

	void removeNode() {
		super.removeMapElement(this.node);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void placeSite() {
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			super.insertSiteInPlaceOfANode(this.node, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
