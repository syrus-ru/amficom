package com.syrus.AMFICOM.Client.Configure.Map.Popup;

/**
 * <p>Title: </p>
 * <p>Description: Проект: Интегрированной Системы Мониторинга</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Syrus Systems</p>
 * @author Markin E. N/
 * @version 1.0
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.*;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.syrus.AMFICOM.Client.Configure.Map.UI.MapElementPropertiesDialog;

public class KISElementPopupMenu
		extends JPopupMenu 
		implements MyPopupMenu
{
	JMenuItem menuDelete = new JMenuItem();
	JMenuItem menuChoose = new JMenuItem();
	JMenuItem menuCopy = new JMenuItem();
	JMenuItem menuProperties = new JMenuItem();
	JMenuItem menuToCatalogue = new JMenuItem();

	JFrame mainFrame;
	MapNodeElement myNode;

  ApplicationContext aContext;

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public KISElementPopupMenu(JFrame myFrame, MapNodeElement node)
	{
		super();
		try
		{
			mainFrame = myFrame;
			myNode = node;
			jbInit();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception 
	{
		menuDelete = new JMenuItem(LangModelMap.Text("KISElementPopupMenuDelete"));
		menuChoose = new JMenuItem(LangModelMap.Text("KISElementPopupMenuChoose"));
		menuCopy = new JMenuItem(LangModelMap.Text("KISElementPopupMenuCopy"));
		menuProperties = new JMenuItem(LangModelMap.Text("KISElementPopupMenuProperties"));
		menuToCatalogue = new JMenuItem(LangModelMap.Text("KISElementPopupMenuToCatalogue"));

		menuProperties.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuProperties_actionPerformed(e);
			}
		});
		menuToCatalogue.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuToCatalogue_actionPerformed(e);
				}
			});
//		this.add(menuDelete);
//		this.add(menuChoose);
//		this.add(menuCopy);
		this.add(menuProperties);
//		this.add(menuToCatalogue);
	}

	void menuProperties_actionPerformed(ActionEvent e)
	{
		//Вызываем диалоговое оено
		MapElementPropertiesDialog dialog = new MapElementPropertiesDialog(
			mainFrame, 
			aContext, 
			"Hello",
			true, 
			myNode);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		dialog.show();
	}

	void menuToCatalogue_actionPerformed(ActionEvent e)
	{

	}

}