package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class NodePopupMenu extends MapPopupMenu 
{
	private JMenuItem placeSiteMenuItem = new JMenuItem();
	private JMenuItem removeMenuItem = new JMenuItem();
	
	private TopologicalNode node;

	private static NodePopupMenu instance = new NodePopupMenu();

	private NodePopupMenu()
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static NodePopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.node = (TopologicalNode )me;
	}

	private void jbInit()
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeNode();
				}
			});

		placeSiteMenuItem.setText(LangModelMap.getString("PlaceSite"));
		placeSiteMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeSite();
				}
			});

		this.add(removeMenuItem);
		this.add(placeSiteMenuItem);
	}

	private void removeNode()
	{
		super.removeMapElement(node);
//		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node);
//		command.setLogicalNetLayer(logicalNetLayer);
//		getLogicalNetLayer().getCommandList().add(command);
//		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint(false);
	}
	
	private void placeSite()
	{
		SiteNodeType proto = super.selectNodeProto();
		if(proto != null)
		{
			super.insertSiteInPlaceOfANode(node, proto);
			
			getLogicalNetLayer().repaint(false);
		}
	}
}
