package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class MarkPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	
	private Mark mark;
	
	private static MarkPopupMenu instance = new MarkPopupMenu();

	private MarkPopupMenu()
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
	
	public static MarkPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.mark = (Mark)me;
	}

	private void jbInit()
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeMark();
				}
			});
		this.add(removeMenuItem);
	}

	private void removeMark()
	{
		super.removeMapElement(mark);

		getLogicalNetLayer().repaint(false);
	}
}
