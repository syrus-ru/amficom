package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.Setup.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.UI.*;

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