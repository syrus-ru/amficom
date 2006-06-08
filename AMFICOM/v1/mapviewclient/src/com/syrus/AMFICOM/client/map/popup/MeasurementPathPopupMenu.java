/*-
 * $$Id: MeasurementPathPopupMenu.java,v 1.21 2006/06/08 12:32:53 stas Exp $$
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
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.21 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MeasurementPathPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = -6711101474302904058L;

	private JMenuItem addMarkerMenuItem = new JMenuItem();

	private MeasurementPath path;

	private static MeasurementPathPopupMenu instance;

	private MeasurementPathPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static MeasurementPathPopupMenu getInstance() {
		if (instance == null) {
			instance = new MeasurementPathPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.path = (MeasurementPath )me;

		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		this.addMarkerMenuItem.setVisible(
				aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER));
	}

	private void jbInit() {
		this.addMarkerMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_MARKER));
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
