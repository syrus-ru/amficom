package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.SurveyEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmFrame;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JDesktopPane;

public class OpenAlarmsCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public AlarmFrame frame;

	public OpenAlarmsCommand()
	{
		// nothing
	}

	public OpenAlarmsCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
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

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.alarmsObserving))
		{
			return;
		}

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
      {
				frame = (AlarmFrame)comp;
        dispatcher.notify(
          new OperationEvent(frame,0,SurveyEvent.ALARM_FRAME_DISPLAYED));
        break;        
      }
		}
		if (frame == null)
		{
			frame = new AlarmFrame(aContext);
			desktop.add(frame);
      
      dispatcher.notify(
        new OperationEvent(frame,0,SurveyEvent.ALARM_FRAME_DISPLAYED));
		}
		Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
		frame.setLocation(0, 0);
		frame.setSize(dim.width * 3 / 5, dim.height / 4);
		frame.show();
		frame.toFront();
	}

}

