package com.syrus.AMFICOM.Client.Administrate;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class Administrate
{
  ApplicationContext aContext = new ApplicationContext();

  public Administrate(AdminApplicationModelFactory factory)
  {
	if(!Environment.canRun(Environment.MODULE_ADMINISTRATE))
		return;
		
    aContext.setApplicationModel(factory.create());

    Frame frame = new AdministrateMDIMain(aContext);
/*
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();

    frame.setSize(screenSize.width-40, screenSize.height-40);

//    if (frameSize.height > screenSize.height)
//    {
//      frameSize.height = screenSize.height;
//    }
//    if (frameSize.width > screenSize.width)
//    {
//      frameSize.width = screenSize.width;
//    }
    frame.setLocation(20, 0);
//    frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });
*/
//		Environment.addWindow(frame);
    frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/administrate_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH));

    frame.setVisible(true);

  }

  public static void main(String[] args)
  {
//		System.out.println(LangModel.resourceBundle + " vs " + LangModelAdmin.resourceBundle);
    Environment.initialize();
    try
    {
		UIManager.setLookAndFeel(Environment.getLookAndFeel());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
//		System.out.println(LangModel.resourceBundle + " vs " + LangModelAdmin.resourceBundle);
    LangModelAdmin.initialize();

//		System.out.println(LangModel.resourceBundle + " vs " + LangModelAdmin.resourceBundle);

    // test!!!
//		new com.syrus.AMFICOM.Client.Test.LoadTestAdmin();

    new Administrate(new DefaultAdminApplicationModelFactory());
  }
}
