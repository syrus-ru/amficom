package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceSelectionDialog;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteNodeCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.InsertSiteCommand;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import javax.swing.JMenuItem;

public final class NodePopupMenu extends MapPopupMenu 
{
	private JMenuItem placeSiteMenuItem = new JMenuItem();
	private JMenuItem removeMenuItem = new JMenuItem();
	
	private MapPhysicalNodeElement node;

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
	
	public void setMapElement(MapElement me)
	{
		this.node = (MapPhysicalNodeElement )me;
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

		getLogicalNetLayer().repaint();
	}
	
	private void placeSite()
	{
		MapNodeProtoElement proto = super.selectNodeProto();
		if(proto != null)
		{
			super.insertSiteInPlaceOfANode(node, proto);
			
			getLogicalNetLayer().repaint();
		}
	}
}
