/*-
 * $$Id: UnboundLinkPopupMenu.java,v 1.27 2006/06/08 12:32:53 stas Exp $$
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class UnboundLinkPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 8470223999024380697L;
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private UnboundLink unbound;

	private static UnboundLinkPopupMenu instance;

	private UnboundLinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static UnboundLinkPopupMenu getInstance() {
		if (instance == null) {
			instance = new UnboundLinkPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.unbound = (UnboundLink)me;
		
		final boolean editable = isEditable();		
		this.bindMenuItem.setVisible(editable);
		
		boolean generatable = false;
		if (editable) {
			final AbstractNode startNode = this.unbound.getStartNode();
			final AbstractNode endNode = this.unbound.getEndNode();
			generatable = !(startNode instanceof UnboundNode
					|| endNode instanceof UnboundNode);
		}
		this.generateMenuItem.setVisible(generatable);
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

