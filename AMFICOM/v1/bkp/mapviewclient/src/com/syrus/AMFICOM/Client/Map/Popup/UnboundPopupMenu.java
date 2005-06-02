package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteNodeCommandBundle;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.UnboundNode;

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
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeUnbound();
				}
			});
		this.bindMenuItem.setText(LangModelMap.getString("Bind"));
		this.bindMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					bind();
				}
			});
		this.generateMenuItem.setText(LangModelMap.getString("GenerateSite"));
		this.generateMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					generateSite();
				}
			});
		this.add(this.removeMenuItem);
		this.add(this.bindMenuItem);
		this.add(this.generateMenuItem);
	}

	void removeUnbound()
	{
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(this.unbound);
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		try
		{
			getLogicalNetLayer().repaint(false);
		}
		catch(MapConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(MapDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void bind()
	{
		SiteNode site = super.selectSiteNode();
		if(site != null)
		{
			BindUnboundNodeToSiteCommandBundle command = new BindUnboundNodeToSiteCommandBundle(this.unbound, site);
			command.setLogicalNetLayer(this.logicalNetLayer);
			this.logicalNetLayer.getCommandList().add(command);
			this.logicalNetLayer.getCommandList().execute();

			try
			{
				getLogicalNetLayer().repaint(false);
			}
			catch(MapConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void generateSite()
	{
		SiteNodeType proto = super.selectNodeProto();
		if(proto != null)
		{
			super.convertUnboundNodeToSite(this.unbound, proto);
		}
	}
}

