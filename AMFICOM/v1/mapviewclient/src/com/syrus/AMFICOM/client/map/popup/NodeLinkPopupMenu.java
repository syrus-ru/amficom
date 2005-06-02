package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.map.NodeLink;

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
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeNodeLink();
				}
			});
		this.add(this.removeMenuItem);
	}

	void removeNodeLink()
	{
		super.removeMapElement(this.link);

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
