package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.NodeLink;

public final class NodeLinkPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();

	private NodeLink link;

	private static NodeLinkPopupMenu instance = new NodeLinkPopupMenu();

	private NodeLinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static NodeLinkPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.link = (NodeLink )me;
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap.getString("Delete")); //$NON-NLS-1$
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeNodeLink();
			}
		});
		this.add(this.removeMenuItem);
	}

	void removeNodeLink() {
		super.removeMapElement(this.link);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}
}
