
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Configure;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class Configure extends Object
{
	ApplicationContext aContext = new ApplicationContext();

	public Configure(ConfigApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_CONFIGURE))
			return;

		aContext.setApplicationModel(factory.create());

		Frame frame = new ConfigureMDIMain(aContext);
		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
//		frame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
//		frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });

//		Environment.addWindow(frame);
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/config_mini.gif"));

		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		Environment.initialize();
		try 
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		LangModelConfig.initialize();
		new Configure(new DefaultConfigApplicationModelFactory());
	}
}

 