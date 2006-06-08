/*-
 * $$Id: NodePopupMenu.java,v 1.24 2006/06/08 12:32:53 stas Exp $$
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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class NodePopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = -1954022729018754016L;

	private JMenuItem placeSiteMenuItem = new JMenuItem();

	private JMenuItem removeMenuItem = new JMenuItem();

	private TopologicalNode node;

	private static NodePopupMenu instance;

	private NodePopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static NodePopupMenu getInstance() {
		if(instance == null) {
			instance = new NodePopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.node = (TopologicalNode )me;
		
		final boolean editable = isEditable();		
		this.placeSiteMenuItem.setVisible(editable);
		this.removeMenuItem.setVisible(editable);
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeNode();
			}
		});

		this.placeSiteMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_PLACE_SITE));
		this.placeSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				placeSite();
			}
		});

		this.add(this.removeMenuItem);
		this.add(this.placeSiteMenuItem);
	}

	void removeNode() {
		if (confirmDelete()) {
			super.removeMapElement(this.node);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void placeSite() {
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			super.insertSiteInPlaceOfANode(this.node, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
