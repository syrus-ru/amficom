package com.syrus.AMFICOM.Client.General.Command.Admin;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Event.*;

public class OpenWhoAmIFrameCommand extends VoidCommand
{
  JDesktopPane desktop;
  ApplicationContext aContext;
  WhoAmIFrame wmi;

  public OpenWhoAmIFrameCommand()
  {
  }


  public OpenWhoAmIFrameCommand(JDesktopPane desktop, ApplicationContext aContext)
  {
    this.desktop = desktop;
    this.aContext = aContext;
  }

  public void setDesktop(JDesktopPane desktop)
  {
    this.desktop = desktop;
  }

  public void setApplicationContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
  }

  public Object clone()
  {
    return new OpenWhoAmIFrameCommand(desktop, aContext);
  }


  public void execute()
  {
    Environment.the_dispatcher.notify(new StatusMessageEvent("Открытие окна <О себе>"));
    wmi = (WhoAmIFrame)Pool.get("WhoAmI", "WhoAmIobject");
    if(wmi!=null)
    {
      Pool.remove(wmi);
      wmi.dispose();
    }

    wmi =
        new WhoAmIFrame(this.aContext);
    Dimension d = desktop.getSize();

    int y = (int)d.getHeight();
    int x = (int)d.getWidth();

    int pointX = 0;
    int pointY = (int)(y*0.75);

    int sizeX = (int)(x*0.3);
    int sizeY = (int)(y*0.25);


    wmi.setBounds(pointX, pointY, sizeX, sizeY);
    desktop.add(wmi, null);
    wmi.show();

    wmi.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage
                                   ("¤")));

    Pool.put("WhoAmI", "WhoAmIobject", wmi);
    Environment.the_dispatcher.notify(new StatusMessageEvent(" "));
  }

}