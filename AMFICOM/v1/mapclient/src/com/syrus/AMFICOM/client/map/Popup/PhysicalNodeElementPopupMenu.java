package com.syrus.AMFICOM.Client.Map.Popup;

/**
 * <p>Title: </p>
 * <p>Description: Проект: Интегрированной Системы Мониторинга</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Syrus Systems</p>
 * @author Markin E. N/
 * @version 1.0
 */

import javax.swing.*;
import java.awt.event.*;
import  com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class PhysicalNodeElementPopupMenu
		extends JPopupMenu 
		implements MyPopupMenu
{
  JMenuItem menuDelete = new JMenuItem();
  JMenuItem menuChoose = new JMenuItem();
  JMenuItem menuCopy = new JMenuItem();
  JMenuItem menuProperties = new JMenuItem();

  JFrame mainFrame;

  ApplicationContext aContext;

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

  public PhysicalNodeElementPopupMenu(JFrame myFrame)
  {
    super();
    try
    {
    mainFrame = myFrame;
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
      menuDelete = new JMenuItem(LangModelMap.Text("PhysicalNodeElementPopupMenuDelete"));
      menuChoose = new JMenuItem(LangModelMap.Text("PhysicalNodeElementPopupMenuChoose"));
      menuCopy = new JMenuItem(LangModelMap.Text("PhysicalNodeElementPopupMenuCopy"));
      menuProperties = new JMenuItem(LangModelMap.Text("PhysicalNodeElementPopupMenuProperties"));

//    this.add(menuDelete);
//    this.add(menuChoose);
//    this.add(menuCopy);
//    this.add(menuProperties);
  }

}