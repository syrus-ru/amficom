/*-
 * $$Id: NodeLinkPopupMenu.java,v 1.22 2006/06/08 12:32:53 stas Exp $$
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
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.22 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class NodeLinkPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = -5585985644912948567L;
	private JMenuItem addPointItem = new JMenuItem();
	private JMenuItem removeMenuItem = new JMenuItem();

	private NodeLink link;

	private static NodeLinkPopupMenu instance;

	private NodeLinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static NodeLinkPopupMenu getInstance() {
		if (instance == null) {
			instance = new NodeLinkPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.link = (NodeLink )me;
		
		final boolean editable = isEditable();		
		this.addPointItem.setVisible(editable);
		this.removeMenuItem.setVisible(editable);
	}

	private void jbInit() {
		this.addPointItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_SITE));
		this.addPointItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPoint();
			}
		});
		this.add(this.addPointItem);
		
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeNodeLink();
			}
		});
		this.add(this.removeMenuItem);
	}
	
	void addPoint() {
		if(this.link.getPhysicalLink().getType().getSort().value() != PhysicalLinkTypeSort._INDOOR) {
			CreatePhysicalNodeCommandBundle command = new CreatePhysicalNodeCommandBundle(this.link, super.point);
			command.setNetMapViewer(super.netMapViewer);
			LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			if(!command.isUndoable()) {
				logicalNetLayer.getCommandList().flush();
			}
			logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeNodeLink() {
		if (confirmDelete()) {
			super.removeMapElement(this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
