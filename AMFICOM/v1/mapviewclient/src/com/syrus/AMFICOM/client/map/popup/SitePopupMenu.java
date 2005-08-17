package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.SiteNode;

public final class SitePopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();

	private JMenuItem propertiesMenuItem = new JMenuItem();

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
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSite();
			}
		});
		this.propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		this.propertiesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProperties();
			}
		});
		this.add(this.removeMenuItem);
		// this.addSeparator();
		// this.add(this.propertiesMenuItem);
	}

	void showProperties() {
		super.showProperties(this.site);
		this.netMapViewer.getLogicalNetLayer().getContext().getDispatcher().firePropertyChange(
				new MapEvent(this.netMapViewer.getLogicalNetLayer(), MapEvent.MAP_ELEMENT_CHANGED, this.site));
	}

	void removeSite() {
		super.removeMapElement(this.site);
		// DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(site);
		// command.setLogicalNetLayer(logicalNetLayer);
		// getLogicalNetLayer().getCommandList().add(command);
		// getLogicalNetLayer().getCommandList().execute();

		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
