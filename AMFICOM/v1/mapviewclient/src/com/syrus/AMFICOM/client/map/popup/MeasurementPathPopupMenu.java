/*-
 * $$Id: MeasurementPathPopupMenu.java,v 1.18 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.action.CreateMarkerCommandAtomic;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * @version $Revision: 1.18 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MeasurementPathPopupMenu extends MapPopupMenu {

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

	@Override
	public void setElement(Object me) {
		this.path = (MeasurementPath )me;

		this.addMarkerMenuItem.setVisible(this.netMapViewer.getLogicalNetLayer().getContext()
				.getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_USE_MARKER));
	}

	private void jbInit() {
		this.addMarkerMenuItem.setText(LangModelMap.getString(MapEditorResourceKeys.POPUP_ADD_MARKER));
		this.addMarkerMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMarker();
			}
		});
		this.add(this.addMarkerMenuItem);
	}

	void addMarker() {
		CreateMarkerCommandAtomic command = new CreateMarkerCommandAtomic(
				this.path,
				this.point);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}
}
