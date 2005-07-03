package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.command.action.CreateMarkerCommandAtomic;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.mapview.MeasurementPath;

public final class MeasurementPathPopupMenu extends MapPopupMenu {
	private JMenuItem propertiesMenuItem = new JMenuItem();

	private JMenuItem addMarkerMenuItem = new JMenuItem();

	private MeasurementPath path;

	private static MeasurementPathPopupMenu instance = new MeasurementPathPopupMenu();

	private MeasurementPathPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static MeasurementPathPopupMenu getInstance() {
		return instance;
	}

	public void setElement(Object me) {
		this.path = (MeasurementPath )me;

		this.addMarkerMenuItem.setVisible(this.netMapViewer.getLogicalNetLayer().getContext()
				.getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_USE_MARKER));
	}

	private void jbInit() {
		this.propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		this.propertiesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProperties();
			}
		});
		this.addMarkerMenuItem.setText(LangModelMap.getString("AddMarker"));
		this.addMarkerMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMarker();
			}
		});
		this.add(this.addMarkerMenuItem);
		this.addSeparator();
		this.add(this.propertiesMenuItem);
	}

	void showProperties() {
		super.showProperties(this.path);
	}

	void addMarker() {
		CreateMarkerCommandAtomic command = new CreateMarkerCommandAtomic(
				this.path,
				this.point);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();

		try {this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
