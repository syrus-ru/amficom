package com.syrus.AMFICOM.Client.Configure.Map.Popup;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.syrus.AMFICOM.Client.Configure.Map.UI.MapElementPropertiesDialog;
import com.syrus.AMFICOM.Client.Configure.Map.MapMainFrame;

import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

public class VoidMapElementPopupMenu
		extends JPopupMenu 
		implements MyPopupMenu
{
	JMenuItem menuDelete = new JMenuItem();
	JMenuItem menuChoose = new JMenuItem();
	JMenuItem menuCopy = new JMenuItem();
	JMenuItem menuProperties = new JMenuItem();

	JFrame mainFrame;
	MapContext mc;

  ApplicationContext aContext;

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public VoidMapElementPopupMenu(JFrame myFrame, MapContext mc)
	{
		super();
		try
		{
			mainFrame = myFrame;
			this.mc = mc;
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		menuDelete = new JMenuItem(LangModelMap.Text("VoidPopupMenuDelete"));
		menuChoose = new JMenuItem(LangModelMap.Text("VoidPopupMenuChoose"));
		menuCopy = new JMenuItem(LangModelMap.Text("VoidPopupMenuCopy"));
		menuProperties = new JMenuItem(LangModelMap.Text("VoidPopupMenuProperties"));

//		this.add(menuDelete);
//		this.add(menuChoose);
//		this.add(menuCopy);
		menuProperties.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuProperties_actionPerformed(e);
				}
			});
		this.add(menuProperties);
	}

	private void menuProperties_actionPerformed(ActionEvent e)
	{
		MapElementPropertiesDialog dialog = new MapElementPropertiesDialog(
			mainFrame, 
			aContext, 
			"Hello",
			true, 
			mc);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		dialog.show();
	}

}