package com.syrus.AMFICOM.Client.Resource;

import java.util.*;
import java.io.*;

import org.omg.CORBA.*;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Filter.*;

public class RISDSurveyDataSource
		extends RISDResultDataSource
		implements DataSourceInterface
{
	protected RISDSurveyDataSource()
	{
	}

	public RISDSurveyDataSource(SessionInterface si)
	{
		super(si);
	}

	/**
	 * @deprecated
	 */
	public void GetMessages()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

	}

	public void GetAlarmTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;


		int i;
		int ecode = 0;
		int count;
		AlarmTypeSeq_TransferableHolder ath = new AlarmTypeSeq_TransferableHolder();
		AlarmType_Transferable ats[];
		AlarmType at;

		try
		{
			ecode = si.ci.server.GetAlarmTypes(si.accessIdentity, ath);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Alarms type: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetAlarmTypes! status = " + ecode);
			return;
		}

		ats = ath.value;
		count = ats.length;
		if(count != 0)
			System.out.println("...Done! " + count + " alarm type(s) fetched");
	    for (i = 0; i < count; i++)
		{
			at = new AlarmType(ats[i]);
			Pool.put(AlarmType.typ, at.getId(), at);
	    }

	}

	public void GetAlarms()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		AlarmSeq_TransferableHolder ah = new AlarmSeq_TransferableHolder();
		Alarm_Transferable alarms[];
		Alarm alarm;
		EventSeq_TransferableHolder eh = new EventSeq_TransferableHolder();
		Event_Transferable events[];
		SystemEvent event;
		EventSourceSeq_TransferableHolder esh = new EventSourceSeq_TransferableHolder();
		EventSource_Transferable eventsources[];
		EventSource eventsource;

		try
		{
			ecode = si.ci.server.GetAlarms(si.accessIdentity, ah, esh, eh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Alarms: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetAlarms! status = " + ecode);
			return;
		}

		alarms = ah.value;
		count = alarms.length;
		if(count != 0)
			System.out.println("...Done! " + count + " alarm(s) fetched");
		 for (i = 0; i < count; i++)
		{
			alarm = new Alarm(alarms[i]);
			Pool.put("alarm", alarm.getId(), alarm);
//			Pool.putName("alarm", alarm.getId(), alarm.getName());
		 }

		events = eh.value;
		count = events.length;
		if(count != 0)
			System.out.println("...Done! " + count + " event(s) fetched");
		 for (i = 0; i < count; i++)
		{
			event = new SystemEvent(events[i]);
			Pool.put("event", event.getId(), event);
//			Pool.putName("event", event.getId(), event.getName());
		 }

		eventsources = esh.value;
		count = eventsources.length;
//		System.out.println("...Done! " + count + " eventsource(s) fetched");
		 for (i = 0; i < count; i++)
		{
			eventsource = new EventSource(eventsources[i]);
			Pool.put("eventsource", eventsource.getId(), eventsource);
//			Pool.putName("eventsource", eventsource.getId(), eventsource.getName());
		 }
	}

	public void GetAlarms(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		AlarmSeq_TransferableHolder ah = new AlarmSeq_TransferableHolder();
		Alarm_Transferable alarms[];
		Alarm alarm;
		EventSeq_TransferableHolder eh = new EventSeq_TransferableHolder();
		Event_Transferable events[];
		SystemEvent event;
		EventSourceSeq_TransferableHolder esh = new EventSourceSeq_TransferableHolder();
		EventSource_Transferable eventsources[];
		EventSource eventsource;

		try
		{
			ecode = si.ci.server.GetStatedAlarms(si.accessIdentity, ids, ah, esh, eh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Alarms: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetAlarms! status = " + ecode);
			return;
		}

		alarms = ah.value;
		count = alarms.length;
		if(count != 0)
			System.out.println("...Done! " + count + " alarm(s) fetched");
		 for (i = 0; i < count; i++)
		{
			alarm = new Alarm(alarms[i]);
			Pool.put("alarm", alarm.getId(), alarm);
//			Pool.putName("alarm", alarm.getId(), alarm.getName());
		 }

		events = eh.value;
		count = events.length;
		if(count != 0)
			System.out.println("...Done! " + count + " event(s) fetched");
		 for (i = 0; i < count; i++)
		{
			event = new SystemEvent(events[i]);
			Pool.put("event", event.getId(), event);
//			Pool.putName("event", event.getId(), event.getName());
		 }

		eventsources = esh.value;
		count = eventsources.length;
		if(count != 0)
			System.out.println("...Done! " + count + " eventsource(s) fetched");
		 for (i = 0; i < count; i++)
		{
			eventsource = new EventSource(eventsources[i]);
			Pool.put("eventsource", eventsource.getId(), eventsource);
//			Pool.putName("eventsource", eventsource.getId(), eventsource.getName());
		 }
	}

	public void GetAlarms(String[] ids, String filter_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		if(filter_id == null)
		{
			GetAlarms(ids);
			return;
		}

		ObjectResourceFilter orfilter = (ObjectResourceFilter )Pool.get("filter", filter_id);
		if(orfilter == null)
		{
			GetAlarms(ids);
			return;
		}

		byte[] logic_scheme = new byte[0];
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			orfilter.logicScheme.writeObject(oos);
			oos.flush();
			logic_scheme = baos.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Filter_Transferable filter = new Filter_Transferable();
		filter.resource_typ = orfilter.resource_typ;
		filter.logic_scheme = logic_scheme;
		filter.id = "";

//test
/*
				com.syrus.AMFICOM.filter.LogicScheme_yo ls = new com.syrus.AMFICOM.filter.LogicScheme_yo(new com.syrus.AMFICOM.server.AlarmFilter());

				try
				{
				  ByteArrayInputStream bis = new ByteArrayInputStream(logic_scheme);
				 ObjectInputStream in = new ObjectInputStream(bis);
				 ls.readObject(in);
				}
				catch(Exception ex)
				{
					System.out.println("cannot read logic");
				}
*/
/// test finish

		int i;
		int ecode = 0;
		int count;
		AlarmSeq_TransferableHolder ah = new AlarmSeq_TransferableHolder();
		Alarm_Transferable alarms[];
		Alarm alarm;
		EventSeq_TransferableHolder eh = new EventSeq_TransferableHolder();
		Event_Transferable events[];
		SystemEvent event;
		EventSourceSeq_TransferableHolder esh = new EventSourceSeq_TransferableHolder();
		EventSource_Transferable eventsources[];
		EventSource eventsource;

		try
		{
			ecode = si.ci.server.GetStatedAlarmsFiltered(si.accessIdentity, ids, filter, ah, esh, eh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Alarms: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetAlarms! status = " + ecode);
			return;
		}

		alarms = ah.value;
		count = alarms.length;
		if(count != 0)
			System.out.println("...Done! " + count + " alarm(s) fetched");
		 for (i = 0; i < count; i++)
		{
			alarm = new Alarm(alarms[i]);
			Pool.put("alarm", alarm.getId(), alarm);
//			Pool.putName("alarm", alarm.getId(), alarm.getName());
		 }

		events = eh.value;
		count = events.length;
		if(count != 0)
			System.out.println("...Done! " + count + " event(s) fetched");
		 for (i = 0; i < count; i++)
		{
			event = new SystemEvent(events[i]);
			Pool.put("event", event.getId(), event);
//			Pool.putName("event", event.getId(), event.getName());
		 }

		eventsources = esh.value;
		count = eventsources.length;
		if(count != 0)
			System.out.println("...Done! " + count + " eventsource(s) fetched");
		 for (i = 0; i < count; i++)
		{
			eventsource = new EventSource(eventsources[i]);
			Pool.put("eventsource", eventsource.getId(), eventsource);
//			Pool.putName("eventsource", eventsource.getId(), eventsource.getName());
		 }
	}

	public void SetAlarm(String alarm_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		Alarm alarm = (Alarm )Pool.get(Alarm.typ, alarm_id);

		alarm.setTransferableFromLocal();
		Alarm_Transferable alarm_t = (Alarm_Transferable )alarm.getTransferable();

		try
		{
			ecode = si.ci.server.SetAlarm(
					si.accessIdentity,
					alarm_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Alarm result: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SetAlarm! status = " + ecode);
			return;
		}
	}

	public void DeleteAlarm(String alarm_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.server.DeleteAlarm(
					si.accessIdentity,
					alarm_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error deleting Alarm result: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed DeleteAlarm! status = " + ecode);
			return;
		}
	}

	public void SetUserAlarm(String source_id, String descriptor)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.server.SetUserAlarm(
					si.accessIdentity,
					source_id,
					descriptor);
		}
		catch (Exception ex)
		{
			System.err.print("Error settung user Alarm result: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SetUserAlarm! status = " + ecode);
			return;
		}
	}

	public ResourceDescriptor_Transferable[] GetAlarmsForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder tih = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetAlarmIdsForMonitoredElement(si.accessIdentity, me_id, tih);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Alarm: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return tih.value;
	}
}

