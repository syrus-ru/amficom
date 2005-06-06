package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.command.action.BindUnboundLinkToPhysicalLinkCommandBundle;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

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

	public void setElement(Object me) {
		this.unbound = (UnboundLink)me;
		this.generateMenuItem.setVisible( !(this.unbound.getStartNode() instanceof UnboundNode)
			&& !(this.unbound.getEndNode() instanceof UnboundNode));
	}

	private void jbInit() {
		this.bindMenuItem.setText(LangModelMap.getString("Bind"));
		this.bindMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bind();
			}
		});
		this.generateMenuItem
				.setText(LangModelMap.getString("GenerateCabling"));
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
			command.setLogicalNetLayer(this.logicalNetLayer);
			this.logicalNetLayer.getCommandList().add(command);
			this.logicalNetLayer.getCommandList().execute();

			try {
				getLogicalNetLayer().repaint(false);
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void generateCabling() {
		super.convertUnboundLinkToPhysicalLink(this.unbound);
	}
}

