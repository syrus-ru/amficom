package com.syrus.AMFICOM.Client.General.Command.Admin;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Administrate.Object.AdministrateTreeModel;

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


  public void execute()
  {
    Environment.the_dispatcher.notify(new StatusMessageEvent("Открытие окна навигации объектов"));

    frame = (JInternalFrame)Pool.get("Navigator", "ObjectNavigator");
    if(frame != null)
    {
      Pool.remove(frame);
      frame.dispose();
    }

    AdministrateTreeModel atm =
        new AdministrateTreeModel(this.aContext.getDataSourceInterface());
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

    Environment.the_dispatcher.notify(new StatusMessageEvent(" "));
  }
}



