package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class NodeLinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	
	private NodeLink link;
	
	private static NodeLinkPopupMenu instance = new NodeLinkPopupMenu();

	private NodeLinkPopupMenu()
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
	
	public static NodeLinkPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.link = (NodeLink )me;
	}

	private void jbInit()
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeNodeLink();
				}
			});
		this.add(removeMenuItem);
	}

	private void removeNodeLink()
	{
		super.removeMapElement(link);

		getLogicalNetLayer().repaint(false);
	}
}
