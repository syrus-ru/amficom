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
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Test.*;

public class SurveyViewAllSchemeCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public SurveyViewAllSchemeCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SurveyViewAllSchemeCommand(dispatcher, desktop, aContext);
	}

	public void execute()
	{
		SurveySchemeOpenCommand schemeView = new SurveySchemeOpenCommand(desktop, aContext);
		schemeView.execute();
		if (schemeView.opened)
		{
			new OpenAlarmsCommand(dispatcher, desktop, aContext, new DefaultAlarmApplicationModelFactory()).execute();
			new OpenResultsCommand(dispatcher, desktop, aContext, new DefaultResultApplicationModelFactory()).execute();
 //     Scheme sc = (Scheme )Pool.get(Scheme.typ, schemeView.scheme_id);
//      dispatcher.notify(new OperationEvent(sc, 0, "schemeopenevent"));
		}
	}
}
