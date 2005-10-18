/*-
 * $$Id: CablePathPopupMenu.java,v 1.28 2005/10/18 07:21:13 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * @version $Revision: 1.28 $, $Date: 2005/10/18 07:21:13 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CablePathPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private CablePath cablePath;

	private static CablePathPopupMenu instance = new CablePathPopupMenu();

	private CablePathPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static CablePathPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object object) {
		this.cablePath = (CablePath) object;

		boolean canGenerate = false;
		try {
			for(Iterator it = this.cablePath.getLinks().iterator(); it.hasNext();) {
				Object link = it.next();
				if(link instanceof UnboundLink)
					canGenerate = true;
			}
		} catch(ApplicationException e) {
			e.printStackTrace();
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
		super.removeMapElement(this.cablePath);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(
				MapEvent.MAP_CHANGED);
	}

	void generateCabling() {
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			super.generatePathCabling(this.cablePath, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(
					MapEvent.MAP_CHANGED);
		}
	}

}
