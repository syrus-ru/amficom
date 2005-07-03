package com.syrus.AMFICOM.Client.General.Command.Survey;

import javax.swing.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class SurveyViewAllWithMapViewCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public SurveyViewAllWithMapViewCommand()
	{
		// nothing
	}

	public SurveyViewAllWithMapViewCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SurveyViewAllWithMapViewCommand(dispatcher, desktop, aContext);
	}

	public void execute()
	{
		SurveyMapViewOpenCommand mapView = new SurveyMapViewOpenCommand(desktop, aContext);
		mapView.execute();
		if (mapView.getResult() == Command.RESULT_OK)
		{
			new OpenAlarmsCommand(dispatcher, desktop, aContext).execute();
			new OpenResultsCommand(dispatcher, desktop, aContext).execute();
		}
	}
}

