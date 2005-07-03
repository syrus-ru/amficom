
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Survey;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.DefaultSurveyApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.SurveyApplicationModelFactory;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.UIManager;

public class Survey
{
	ApplicationContext aContext = new ApplicationContext();

	public Survey(SurveyApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_OBSERVE))
			return;

		aContext.setApplicationModel(factory.create());

		Frame frame = new SurveyMainFrame(aContext);
/*
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
*/
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/observe_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		new Survey(new DefaultSurveyApplicationModelFactory());
	}
}

