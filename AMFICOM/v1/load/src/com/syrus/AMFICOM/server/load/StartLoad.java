
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.server.load;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StartLoad
{
	LoadMessageFrame frame;

	/**
	 * Constructor
	 */
	public StartLoad()
	{
		frame = new LoadMessageFrame();
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
		frame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new StartLoad();
	}
}

 