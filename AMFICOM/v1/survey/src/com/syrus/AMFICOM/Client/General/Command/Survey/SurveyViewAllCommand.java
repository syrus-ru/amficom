package com.syrus.AMFICOM.Client.General.Command.Survey;

import javax.swing.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class SurveyViewAllCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public SurveyViewAllCommand()
	{
		// nothing
	}

	public SurveyViewAllCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SurveyViewAllCommand(dispatcher, desktop, aContext);
	}

	public void execute()
	{
		SurveyNewMapViewCommand mapView = new SurveyNewMapViewCommand(dispatcher, desktop, aContext, new MapSurveyNetApplicationModelFactory());
		mapView.execute();
		if (mapView.opened)
		{
			new OpenAlarmsCommand(dispatcher, desktop, aContext, new DefaultAlarmApplicationModelFactory()).execute();
			new OpenResultsCommand(dispatcher, desktop, aContext, new DefaultResultApplicationModelFactory()).execute();
		}
	}
}

