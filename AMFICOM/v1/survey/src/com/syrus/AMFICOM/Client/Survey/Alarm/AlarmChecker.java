package com.syrus.AMFICOM.Client.Survey.Alarm;

import java.util.*;
import java.awt.*;
import java.text.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.analysis.dadara.*;

/**
 * @deprecated
 */
public class AlarmChecker extends Thread
{
	private Dispatcher dispatcher;
	private DataSourceInterface dataSource;
	public ApplicationContext aContext = new ApplicationContext();
	JFrame parent;
	JDesktopPane desktop;

	private boolean is_running = true;

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	protected AlarmChecker(JFrame parent)
	{
		this(new Dispatcher(), new ApplicationContext(), parent, null);
	}

	public AlarmChecker(Dispatcher dispathcer, ApplicationContext aContext, JFrame parent, JDesktopPane desktop)
	{
		this.parent = parent;
		this.desktop = desktop;
		this.dispatcher = Environment.the_dispatcher;
		setContext(aContext);

	}

	void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
//		aContext.setDispatcher(dispatcher);
	}

	public void stop_running()
	{
		is_running = false;
	}
	
	public void run()
	{
		while (is_running)
		{
			dataSource = aContext.getDataSourceInterface();

			if (dataSource != null)
			{
				int c = new SurveyDataSourceImage(dataSource).GetAlarms();
				if(c != 0)
					dispatcher.notify(new AlarmReceivedEvent(this));
				dataSource.GetMessages();
				if (Pool.getHash("message") != null)
				{
					Enumeration messages = Pool.getHash("message").elements();
					if(messages != null)
						while (messages.hasMoreElements())
						{
							Message message = (Message)messages.nextElement(); // получили месагу
							//MessageDialog mDialog = new MessageDialog(parent, "Message", true, message);
							//mDialog.show();
							Pool.remove("message", message.id);

							SystemEvent event = (SystemEvent)Pool.get(SystemEvent.typ, message.eventId); // получили аларм
							Alarm alarm = null;
							for(Enumeration en = Pool.getHash(Alarm.typ).elements(); en.hasMoreElements();)
							{
								Alarm al = (Alarm)en.nextElement();
								if( al.event_id.equals(message.eventId))
								{
									alarm = al;
									break;
								}
							}

							if(alarm == null)
								continue;

							AlarmDescriptor ad = null;
//							Alarm alarm = (Alarm)Pool.get(Alarm.typ, message.alarm_id); // получили аларм
							if(alarm.type_id.equals("rtutestalarm") ||
								 alarm.type_id.equals("rtutestwarning"))
							{
								ad = new OpticalAlarmDescriptor(alarm, dataSource);
							}
							if(ad != null)
							{
								AlarmPopupFrame f = new AlarmPopupFrame(
										"Отклонение в " +
											Pool.getName(MonitoredElement.typ, alarm.getMonitoredElementId()) + 
											" возникло " + 
											sdf.format(new Date(ad.getAlarmTime())),
										alarm,
										aContext);

								Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
								f.setLocation(0, 0);
								f.setSize(dim.width * 3 / 5, dim.height / 4);
								f.set(ad);
								desktop.add(f);
								f.show();
								f.toFront();
							}
						}
				}
			}
			try
			{
				sleep(5000);
			}
			catch (Exception e)
			{
				System.out.println("Блин! Поспать не дадут!");
			}
		}
	}
}

