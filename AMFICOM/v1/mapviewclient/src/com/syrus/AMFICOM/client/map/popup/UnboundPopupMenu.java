package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteNodeCommandBundle;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class UnboundPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private MapUnboundNodeElement unbound;

	private static UnboundPopupMenu instance = new UnboundPopupMenu();

	private UnboundPopupMenu()
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
	
	public static UnboundPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.unbound = (MapUnboundNodeElement )me;
	}

	private void jbInit() 
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeUnbound();
				}
			});
		propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		bindMenuItem.setText(LangModelMap.getString("Bind"));
		bindMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					bind();
				}
			});
		generateMenuItem.setText(LangModelMap.getString("GenerateSite"));
		generateMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					generateSite();
				}
			});
		this.add(removeMenuItem);
		this.add(propertiesMenuItem);
		this.add(bindMenuItem);
		this.add(generateMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(unbound);
	}

	private void removeUnbound()
	{
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(unbound);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint();
	}

	private void bind()
	{
		MapSiteNodeElement site = super.selectSiteNode();
		if(site != null)
		{
			BindUnboundNodeToSiteCommandBundle command = new BindUnboundNodeToSiteCommandBundle(unbound, site);
			command.setLogicalNetLayer(logicalNetLayer);
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
		}
	}

	private void generateSite()
	{
		MapNodeProtoElement proto = super.selectNodeProto();
		if(proto != null)
		{
			super.convertUnboundNodeToSite(unbound, proto);
		}
	}
}

