package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteNodeCommandBundle;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class UnboundPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private UnboundNode unbound;

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
	
	public void setElement(Object me)
	{
		this.unbound = (UnboundNode)me;
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
		this.add(bindMenuItem);
		this.add(generateMenuItem);
	}

	private void removeUnbound()
	{
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(unbound);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint(false);
	}

	private void bind()
	{
		SiteNode site = super.selectSiteNode();
		if(site != null)
		{
			BindUnboundNodeToSiteCommandBundle command = new BindUnboundNodeToSiteCommandBundle(unbound, site);
			command.setLogicalNetLayer(logicalNetLayer);
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();

			getLogicalNetLayer().repaint(false);
		}
	}

	private void generateSite()
	{
		SiteNodeType proto = super.selectNodeProto();
		if(proto != null)
		{
			super.convertUnboundNodeToSite(unbound, proto);
		}
	}
}

