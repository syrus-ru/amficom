package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class VoidElementPopupMenu extends MapPopupMenu 
{

	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem addSiteMenuItem = new JMenuItem();

	private static VoidElementPopupMenu instance = new VoidElementPopupMenu();

	private VoidElementPopupMenu()
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
	
	public static VoidElementPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
	}
	
	private void jbInit()
	{
		propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		addSiteMenuItem.setText(LangModelMap.getString("AddSite"));
		addSiteMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addSite();
				}
			});
		this.add(addSiteMenuItem);
		this.addSeparator();
		this.add(propertiesMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(VoidMapElement.getInstance(getLogicalNetLayer().getMapView()));
	}

	private void addSite()
	{
		SiteNodeType proto = super.selectNodeProto();
		
		if(proto != null)
		{
			CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(proto, point);
			command.setLogicalNetLayer(logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();

			getLogicalNetLayer().repaint(false);
		}
	}
}