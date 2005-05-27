package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
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

	public void setElement(Object me) {
		this.node = (TopologicalNode )me;
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeNode();
			}
		});

		this.placeSiteMenuItem.setText(LangModelMap.getString("PlaceSite"));
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
// DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node);
//		command.setLogicalNetLayer(logicalNetLayer);
//		getLogicalNetLayer().getCommandList().add(command);
//		getLogicalNetLayer().getCommandList().execute();

		try {
			getLogicalNetLayer().repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void placeSite() {
		SiteNodeType proto = super.selectNodeProto();
		if(proto != null) {
			super.insertSiteInPlaceOfANode(this.node, proto);

			try {
				getLogicalNetLayer().repaint(false);
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
