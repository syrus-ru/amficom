/*-
 * $$Id: MarkerPopupMenu.java,v 1.18 2006/02/15 11:12:25 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $, $Date: 2006/02/15 11:12:25 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MarkerPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();

	private Marker marker;

	private static MarkerPopupMenu instance = new MarkerPopupMenu();

	private MarkerPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static MarkerPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.marker = (Marker )me;
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeMark();
			}
		});
		this.add(this.removeMenuItem);
	}

	void removeMark() {
		super.removeMapElement(this.marker);
		
		MarkerController markerController = (MarkerController)this.netMapViewer.getLogicalNetLayer().getMapViewController().getController(this.marker);
		markerController.notifyMarkerDeleted(this.marker);

		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			Log.errorMessage(e);
		} catch(MapDataException e) {
			Log.errorMessage(e);
		}
	}
}
