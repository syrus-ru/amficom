
// Copyright (c) Syrus Systems 2000-2003 Syrus Systems
package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class Optimize
{
  ApplicationContext aContext = new ApplicationContext();
  //-------------------------------------------------------------------------------------------------------------
  public Optimize(OptimizeApplicationModelFactory factory)
  {
    if(!Environment.canRun(Environment.MODULE_OPTIMIZE))
  return;
    aContext.setApplicationModel(factory.create());

    Frame frame = new OptimizeMDIMain(aContext);
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
    {  frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {  frameSize.width = screenSize.width;
    }
    //frame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
    frame.setLocation((screenSize.width - frameSize.width)/2, 0);
    frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });

    frame.setVisible(true);
	}

}