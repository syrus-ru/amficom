/*-
 * $$Id: UnboundLinkPopupMenu.java,v 1.24 2005/10/26 11:07:01 bass Exp $$
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
import com.syrus.AMFICOM.client.map.command.action.BindUnboundLinkToPhysicalLinkCommandBundle;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 * @version $Revision: 1.24 $, $Date: 2005/10/26 11:07:01 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class UnboundLinkPopupMenu extends MapPopupMenu {
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private UnboundLink unbound;

	private static UnboundLinkPopupMenu instance = new UnboundLinkPopupMenu();

	private UnboundLinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static UnboundLinkPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.unbound = (UnboundLink)me;
		final AbstractNode startNode = this.unbound.getStartNode();
		final AbstractNode endNode = this.unbound.getEndNode();
		this.generateMenuItem.setVisible(!(startNode instanceof UnboundNode
				|| endNode instanceof UnboundNode));
	}

	private void jbInit() {
		this.bindMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_BIND));
		this.bindMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bind();
			}
		});
		this.generateMenuItem
				.setText(I18N.getString(MapEditorResourceKeys.POPUP_GENERATE_CABLING));
		this.generateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateCabling();
			}
		});
		this.add(this.bindMenuItem);
		this.add(this.generateMenuItem);
	}

	void bind() {
		PhysicalLink link = super.selectPhysicalLinkAt(this.unbound);
		if(link != null) {
			BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(
					this.unbound,
					link);
			command.setNetMapViewer(this.netMapViewer);
			this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
			this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
			this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
		}
	}

	void generateCabling() {
		super.convertUnboundLinkToPhysicalLink(this.unbound);
	}
}

