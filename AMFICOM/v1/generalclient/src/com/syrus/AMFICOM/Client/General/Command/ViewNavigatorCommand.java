package com.syrus.AMFICOM.Client.General.Command;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;

public class ViewNavigatorCommand extends VoidCommand
{
	public Dispatcher dispatcher;
	public ApplicationContext aContext;
	public JDesktopPane desktop;
	public String title;
//	public String root_name;
//	public String[] root_objects;
//	public String[] filters;
	public ViewNavigatorFrame frame;

	ObjectResourceTreeModel ortm;

	public ViewNavigatorCommand(Dispatcher dispatcher, JDesktopPane desktop, String title)//, String root_name, String[] root_objects, String[] filters)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.title = title;
//		this.root_name = root_name;
//		this.root_objects = root_objects;
//		this.filters = filters;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		if(field.equals("treemodel"))
			ortm = (ObjectResourceTreeModel )value;
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ViewNavigatorCommand(dispatcher, desktop, title);//, root_name, root_objects, filters);
	}

	public void execute()
	{

/*
		ObjectResourceTreePanel panel = new ObjectResourceTreePanel(dispatcher);
		panel.createTree (root_name, root_objects, filters);
*/
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if (comp instanceof ViewNavigatorFrame)
			{
				frame = (ViewNavigatorFrame)comp;
				break;
			}
		}
		UniTreePanel panel = new UniTreePanel(
					dispatcher, 
					aContext, 
					ortm);
		if (frame == null)
		{
			frame = new ViewNavigatorFrame();
			frame.setTitle(title);
			desktop.add(frame);
		}
		frame.setPanel(panel);

		frame.setVisible(true);
		frame.toFront();
		frame.grabFocus();
	}
}
