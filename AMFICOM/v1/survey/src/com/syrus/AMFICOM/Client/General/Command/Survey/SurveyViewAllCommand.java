package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.io.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Test.*;

public class SurveyViewAllCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public SurveyViewAllCommand()
	{
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