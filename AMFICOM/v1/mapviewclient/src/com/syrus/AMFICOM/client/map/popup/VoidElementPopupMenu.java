package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.SiteNodeType;

public class VoidElementPopupMenu extends MapPopupMenu {

	private JMenuItem mapPropertiesMenuItem = new JMenuItem();
	private JMenuItem mapViewPropertiesMenuItem = new JMenuItem();
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
		this.mapPropertiesMenuItem.setText(LangModelMap.getString("MapProperties"));
		this.mapPropertiesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMapProperties();
			}
		});
		this.mapViewPropertiesMenuItem.setText(LangModelMap.getString("MapViewProperties"));
		this.mapViewPropertiesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMapViewProperties();
			}
		});
		this.addSiteMenuItem.setText(LangModelMap.getString("AddSite"));
		this.addSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSite();
			}
		});
		this.add(this.addSiteMenuItem);
		this.addSeparator();
		this.add(this.mapPropertiesMenuItem);
		this.add(this.mapViewPropertiesMenuItem);
	}

	void showMapProperties() {
		super.showProperties(this.netMapViewer.getLogicalNetLayer().getMapView().getMap());
	}

	void showMapViewProperties() {
		super.showProperties(this.netMapViewer.getLogicalNetLayer().getMapView());
	}

	void addSite() {
		SiteNodeType proto = super.selectNodeProto();

		if(proto != null) {
			CreateSiteCommandAtomic command = 
				new CreateSiteCommandAtomic(proto, this.point);
			command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
			this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
			this.netMapViewer.getLogicalNetLayer().getCommandList().execute();

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
}
