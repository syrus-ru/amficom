package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Survey.Alarm.*;

public class OpenAlarmsCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public AlarmFrame frame;

	public OpenAlarmsCommand()
	{
		// nothing
	}

	public OpenAlarmsCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new OpenAlarmsCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.alarmsObserving))
		{
			return;
		}

		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

//		DataSourceInterface dataSource = aC.getDataSourceInterface();
//		if(dataSource == null)
//			return;
				System.out.println("Starting Alarms frame");

//		new SurveyDataSourceImage(dataSource).GetAlarms();

		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if (comp instanceof AlarmFrame)
				frame = (AlarmFrame)comp;
		}
		if (frame == null)
		{
			frame = new AlarmFrame(aC);
			desktop.add(frame);
		}
		Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
		frame.setLocation(0, 0);
		frame.setSize(dim.width * 3 / 5, dim.height / 4);
		frame.show();
		frame.toFront();
	}

}