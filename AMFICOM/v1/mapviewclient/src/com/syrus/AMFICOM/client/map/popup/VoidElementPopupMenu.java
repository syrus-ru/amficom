package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommandAtomic;
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
		super.showProperties(getLogicalNetLayer().getMapView().getMap());
	}

	void showMapViewProperties() {
		super.showProperties(getLogicalNetLayer().getMapView());
	}

	void addSite() {
		SiteNodeType proto = super.selectNodeProto();

		if(proto != null) {
			CreateSiteCommandAtomic command = 
				new CreateSiteCommandAtomic(proto, this.point);
			command.setLogicalNetLayer(this.logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();

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
