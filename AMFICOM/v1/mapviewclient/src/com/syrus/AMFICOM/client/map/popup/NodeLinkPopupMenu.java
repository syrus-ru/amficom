/*-
 * $$Id: NodeLinkPopupMenu.java,v 1.19 2006/02/08 15:15:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;

/**
 * @version $Revision: 1.19 $, $Date: 2006/02/08 15:15:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class NodeLinkPopupMenu extends MapPopupMenu {
	private JMenuItem addPointItem = new JMenuItem();
	private JMenuItem removeMenuItem = new JMenuItem();

	private NodeLink link;

	private static NodeLinkPopupMenu instance = new NodeLinkPopupMenu();

	private NodeLinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static NodeLinkPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.link = (NodeLink )me;
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
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		super.removeMapElement(this.link);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}
}
