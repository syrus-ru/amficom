/*
 * $Id: ViewObjectNavigatorCommand.java,v 1.2 2004/09/27 16:32:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command.Admin;

import com.syrus.AMFICOM.Client.Administrate.Object.AdministrateTreeModel;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import java.awt.*;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/27 16:32:17 $
 * @module admin_v1
 */
public class ViewObjectNavigatorCommand extends VoidCommand
{
  ApplicationContext aContext;
  Dispatcher dispatcher;
  JDesktopPane desktop;
  String title;
  JInternalFrame frame;



  public ViewObjectNavigatorCommand(Dispatcher dispatcher, JDesktopPane desktop,
                                    String title, ApplicationContext aContext)
  {
    this.aContext = aContext;
    this.dispatcher = dispatcher;
    this.desktop = desktop;
    this.title = title;
  }

  public Object clone()
  {
    return new ViewObjectNavigatorCommand(dispatcher, desktop, title, aContext);
  }


	public void execute() {
		Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Открытие окна навигации объектов"));

    frame = (JInternalFrame)Pool.get("Navigator", "ObjectNavigator");
    if(frame != null)
    {
      Pool.remove(frame);
      frame.dispose();
    }

		AdministrateTreeModel atm = new AdministrateTreeModel(this.aContext.getDataSource());
    UniTreePanel utp = new UniTreePanel(this.dispatcher, this.aContext, atm);

    frame = new JInternalFrame();
    JScrollPane jScrollPane1 = new JScrollPane();
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    frame.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(utp, null);

    frame.setTitle("Навигатор объектов");
    frame.setResizable(true);
    frame.setMaximizable(true);
    frame.setEnabled(true);




    Dimension d = desktop.getSize();

    int y = (int)d.getHeight();
    int x = (int)d.getWidth();

    int pointX = 0;
    int pointY = 0;

    int sizeX = (int)(x*0.3);
    int sizeY = (int)(y*0.75);

    frame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage
                                     ("¤")));
    frame.setClosable(true);
    frame.setMaximizable(true);
    frame.setIconifiable(true);
    desktop.add(frame, null);
    this.frame.setBounds(pointX, pointY, sizeX, sizeY);
    frame.setVisible(true);

    Pool.put("Navigator", "ObjectNavigator", frame);

		Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, " "));
	}
}
