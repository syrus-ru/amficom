package com.syrus.AMFICOM.Client.General.Command.Survey;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class SurveyViewAllWithSchemeCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public SurveyViewAllWithSchemeCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SurveyViewAllWithSchemeCommand(dispatcher, desktop, aContext);
	}

	public void execute()
	{
		SurveySchemeOpenCommand schemeView = new SurveySchemeOpenCommand(desktop, aContext);
		schemeView.execute();
		if (schemeView.getResult() == Command.RESULT_OK)
		{
			new OpenAlarmsCommand(dispatcher, desktop, aContext).execute();
			new OpenResultsCommand(dispatcher, desktop, aContext).execute();
		}
	}
}
