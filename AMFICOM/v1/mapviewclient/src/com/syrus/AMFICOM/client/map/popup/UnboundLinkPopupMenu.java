package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceSelectionDialog;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundLinkToPhysicalLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteNodeCommandBundle;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;

public class UnboundLinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private MapUnboundLinkElement unbound;

	private static UnboundLinkPopupMenu instance = new UnboundLinkPopupMenu();

	private UnboundLinkPopupMenu()
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
	
	public static UnboundLinkPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.unbound = (MapUnboundLinkElement )me;
	}

	private void jbInit() 
	{
		bindMenuItem.setText(LangModelMap.getString("Bind"));
		bindMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					bind();
				}
			});
		generateMenuItem.setText(LangModelMap.getString("GenerateCabling"));
		generateMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					generateCabling();
				}
			});
		this.add(bindMenuItem);
		this.add(generateMenuItem);
	}

	private void bind()
	{
		MapPhysicalLinkElement link = super.selectPhysicalLinkAt(unbound);
		if(link != null)
		{
			BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unbound, link);
			command.setLogicalNetLayer(logicalNetLayer);
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
		}
	}

	private void generateCabling()
	{
		super.convertUnboundLinkToPhysicalLink(unbound);
	}
}

