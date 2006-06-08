/*-
 * $$Id: CablePathPopupMenu.java,v 1.31 2006/06/08 12:32:53 stas Exp $$
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

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CablePathPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 7578207099905441307L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private CablePath cablePath;

	private static CablePathPopupMenu instance;

	private CablePathPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static CablePathPopupMenu getInstance() {
		if (instance == null) {
			instance = new CablePathPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object object) {
		this.cablePath = (CablePath) object;

		final boolean editable = isEditable();		
		this.removeMenuItem.setVisible(editable);
		
		boolean canGenerate = false;
		if (editable) {
			try {
				for(PhysicalLink link : this.cablePath.getLinks()) {
					if(link instanceof UnboundLink) {
						canGenerate = true;
						break;
					}
				}
			} catch(ApplicationException e) {
				Log.errorMessage(e);
			}
		}
		this.generateMenuItem.setVisible(canGenerate);
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeCablePath();
			}
		});
		this.generateMenuItem
				.setText(I18N.getString(MapEditorResourceKeys.POPUP_GENERATE_CABLING));
		this.generateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateCabling();
			}
		});
		this.add(this.removeMenuItem);
		this.add(this.generateMenuItem);
	}

	void removeCablePath() {
		if (confirmDelete()) {
			super.removeMapElement(this.cablePath);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void generateCabling() {
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			super.generatePathCabling(this.cablePath, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
