package com.syrus.AMFICOM.Client.Configure.Map.Popup;

/**
 * <p>Title: </p>
 * <p>Description: Проект: Интегрированной Системы Мониторинга</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Syrus Systems</p>
 * @author Markin E. N/
 * @version 1.0
 */
import com.ofx.geometry.*;
import com.ofx.mapViewer.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class NodeLinkPopupMenu
		extends JPopupMenu 
		implements MyPopupMenu
{
  JMenuItem menuChoose = new JMenuItem();
  JMenuItem menuDelete = new JMenuItem();
  JMenuItem menuLook = new JMenuItem();
  JMenuItem menuProperties = new JMenuItem();

  JFrame mainFrame;
  MapNodeLinkElement nodeLink;
  ApplicationContext aContext;

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

  public NodeLinkPopupMenu(JFrame myFrame, MapNodeLinkElement nodelink)
  {
    super();
    try
    {
      mainFrame = myFrame;
      nodeLink = nodelink;
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
      menuChoose = new JMenuItem(LangModelMap.Text("NodeLinkPopupMenuChoose"));
      menuDelete = new JMenuItem(LangModelMap.Text("NodeLinkPopupMenuDelete"));
      menuLook = new JMenuItem(LangModelMap.Text("NodeLinkPopupMenuLook"));
      menuProperties = new JMenuItem(LangModelMap.Text("NodeLinkPopupMenuProperties"));

//    this.add(menuChoose);
//    this.add(menuDelete);
//    this.addSeparator();
//    this.add(menuLook);
//    this.addSeparator();
//    this.add(menuProperties);
  }


}