package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Setup.OfxControlsFrame;

import java.awt.Dimension;

import java.util.Vector;

import javax.swing.JDesktopPane;

//A0A
public class SurveyViewMapSetupCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public OfxControlsFrame frame;

	public SurveyViewMapSetupCommand()
	{
	}

	public SurveyViewMapSetupCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SurveyViewMapSetupCommand(desktop, aContext);
	}

	public void execute()
	{
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				OfxControlsFrame comp = (OfxControlsFrame )desktop.getComponent(i);
				// уже есть окно карты
				frame = comp;
				break;
			}
			catch(Exception ex)
			{
			}
		}

		if(frame == null)
		{
			frame = new OfxControlsFrame(null, aContext);

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(0, dim.height / 4);
			frame.setSize(dim.width / 5, 3 * dim.height / 4);
		}

		frame.setVisible(true);
	}

}

