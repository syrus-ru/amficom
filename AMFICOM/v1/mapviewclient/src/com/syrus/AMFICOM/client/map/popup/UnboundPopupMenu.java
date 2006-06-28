/*-
 * $$Id: UnboundPopupMenu.java,v 1.31 2006/06/08 12:32:53 stas Exp $$
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
import com.syrus.AMFICOM.client.map.command.action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.DeleteNodeCommandBundle;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class UnboundPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 6304763844465550703L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private UnboundNode unbound;

	private static UnboundPopupMenu instance;

	private UnboundPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static UnboundPopupMenu getInstance() {
		if (instance == null) {
			instance = new UnboundPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.unbound = (UnboundNode )me;
		
		final boolean editable = isEditable();		
		this.removeMenuItem.setVisible(editable);
		this.bindMenuItem.setVisible(editable);
		this.generateMenuItem.setVisible(editable);
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeUnbound();
			}
		});
		this.bindMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_BIND));
		this.bindMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bind();
			}
		});
		this.generateMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_GENERATE_SITE));
		this.generateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateSite();
			}
		});
		this.add(this.removeMenuItem);
		this.add(this.bindMenuItem);
		this.add(this.generateMenuItem);
	}

	void removeUnbound() {
		if (confirmDelete()) {
			DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(this.unbound);
			command.setNetMapViewer(this.netMapViewer);
			this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
			this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
			if(!command.isUndoable()) {
				this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
			}
		}
	}

	void bind() {
		SiteNode site = super.selectSiteNode();
		if(site != null) {
			BindUnboundNodeToSiteCommandBundle command = 
				new BindUnboundNodeToSiteCommandBundle(this.unbound, site);
			command.setNetMapViewer(this.netMapViewer);
			this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
			this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
			if(!command.isUndoable()) {
				this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
			}
		}
	}

	void generateSite() {
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			super.convertUnboundNodeToSite(this.unbound, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
