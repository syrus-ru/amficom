package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.UI.MapElementLabel;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class ProtoPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	
	private SiteNodeType proto;
	
	private MapElementLabel lab;

	private static ProtoPopupMenu instance = new ProtoPopupMenu();

	private ProtoPopupMenu()
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
	
	public static ProtoPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.proto = (SiteNodeType )me;
	}

	public void setElementLabel(MapElementLabel lab)
	{
		this.lab = lab;
	}

	private void jbInit() 
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeProto();
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
		this.add(removeMenuItem);
		this.addSeparator();
		this.add(propertiesMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(proto);
		lab.updateIcon();
	}

	private void removeProto()
	{
	}
}
