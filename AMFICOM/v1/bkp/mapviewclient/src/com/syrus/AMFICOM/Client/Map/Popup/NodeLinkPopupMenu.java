package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class NodeLinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	
	private MapNodeLinkElement link;
	
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
	
	public void setMapElement(MapElement me)
	{
		this.link = (MapNodeLinkElement )me;
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

		getLogicalNetLayer().repaint();
	}
}
