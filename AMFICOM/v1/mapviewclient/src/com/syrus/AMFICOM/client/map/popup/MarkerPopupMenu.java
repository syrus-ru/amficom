package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.mapview.Marker;

public final class MarkerPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();

	private Marker marker;

	private static MarkerPopupMenu instance = new MarkerPopupMenu();

	private MarkerPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static MarkerPopupMenu getInstance() {
		return instance;
	}

	public void setElement(Object me) {
		this.marker = (Marker )me;
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeMark();
			}
		});
		this.add(this.removeMenuItem);
	}

	void removeMark() {
		super.removeMapElement(this.marker);
		
		MarkerController markerController = (MarkerController)getLogicalNetLayer().getMapViewController().getController(this.marker);
		markerController.notifyMarkerDeleted(this.marker);

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
