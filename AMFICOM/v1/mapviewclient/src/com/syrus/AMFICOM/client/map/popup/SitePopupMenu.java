/*-
 * $$Id: SitePopupMenu.java,v 1.26 2006/06/08 12:32:53 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class SitePopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = -773545416471282757L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem attachCableInletMenuItem = new JMenuItem();

	private SiteNode site;

	private static SitePopupMenu instance;

	private SitePopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static SitePopupMenu getInstance() {
		if (instance == null) {
			instance = new SitePopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.site = (SiteNode )me;
		
		final boolean editable = isEditable();		
		this.removeMenuItem.setVisible(editable);
		
		boolean attachable = false;
		if (editable) {
			SiteNodeTypeSort sort = this.site.getType().getSort();
			attachable = sort == SiteNodeTypeSort.ATS || sort == SiteNodeTypeSort.BUILDING;
		}
		this.attachCableInletMenuItem.setVisible(attachable);
	}

	private void jbInit() {
		this.attachCableInletMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ATTACH_CABLE_INLET));
		this.attachCableInletMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					attachCableInlet();
				} catch(MapException ex) {
					Log.errorMessage(ex);
				}
			}
		});
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSite();
			}
		});
		this.add(this.attachCableInletMenuItem);
		this.add(this.removeMenuItem);
	}

	protected void attachCableInlet() throws MapConnectionException, MapDataException {
		SiteNodeType siteNodeType = super.selectAttachedSiteNodeType();
		if(siteNodeType != null) {
			super.createAttachedSiteNode(this.site, siteNodeType);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeSite() {
		if (confirmDelete()) {
			super.removeMapElement(this.site);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
