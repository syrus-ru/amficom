package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Alarm.AlarmTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Alarm.AlarmType_Transferable;
import com.syrus.AMFICOM.CORBA.Alarm.AlertingMessageSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Alarm.AlertingMessageUserSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Alarm.AlertingMessageUser_Transferable;
import com.syrus.AMFICOM.CORBA.Alarm.AlertingMessage_Transferable;
import com.syrus.AMFICOM.CORBA.Alarm.EventSourceTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Alarm.EventSourceType_Transferable;
import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.Alarm.AlarmType;
import com.syrus.AMFICOM.Client.Resource.Alarm.AlertingMessage;
import com.syrus.AMFICOM.Client.Resource.Alarm.AlertingMessageUser;
import com.syrus.AMFICOM.Client.Resource.Alarm.EventSourceType;

import java.util.Vector;

public class RISDMaintenanceDataSource 
		extends RISDMapDataSource
		implements DataSourceInterface 
{
	protected RISDMaintenanceDataSource()
	{
		super();
	}

	public RISDMaintenanceDataSource(SessionInterface si)
	{
		super(si);
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
			Pool.put("alarmtype", at.getId(), at);
	    }

	}

	public void LoadMaintenanceData()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		EventSourceTypeSeq_TransferableHolder esth = new EventSourceTypeSeq_TransferableHolder();
		EventSourceType_Transferable ests[];
		EventSourceType est;
		AlertingMessageSeq_TransferableHolder amh = new AlertingMessageSeq_TransferableHolder();
		AlertingMessage_Transferable ams[];
		AlertingMessage am;
		AlertingMessageUserSeq_TransferableHolder amuh = new AlertingMessageUserSeq_TransferableHolder();
		AlertingMessageUser_Transferable amus[];
		AlertingMessageUser amu;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.LoadMaintenanceData(si.accessIdentity, esth, amh, amuh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting network catalogue: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadNet! status = " + ecode);
			return;
		}

		ests = esth.value;
		count = ests.length;
		System.out.println("...Done! " + count + " EventSourceType(s) fetched");
	    for (i = 0; i < count; i++)
		{
			est = new EventSourceType(ests[i]);
			Pool.put(EventSourceType.typ, est.getId(), est);
			loaded_objects.add(est);
	    }

		ams = amh.value;
		count = ams.length;
		System.out.println("...Done! " + count + " AlertingMessage(s) fetched");
	    for (i = 0; i < count; i++)
		{
			am = new AlertingMessage(ams[i]);
			Pool.put(AlertingMessage.typ, am.getId(), am);
			loaded_objects.add(am);
	    }

		amus = amuh.value;
		count = amus.length;
		System.out.println("...Done! " + count + " AlertingMessageUser(s) fetched");
	    for (i = 0; i < count; i++)
		{
			amu = new AlertingMessageUser(amus[i]);
			Pool.put(AlertingMessageUser.typ, amu.getId(), amu);
			loaded_objects.add(amu);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}
	
	public void SaveMaintenanceData(String []am_id, String []amu_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		AlertingMessage_Transferable am_t[] = new AlertingMessage_Transferable[am_id.length];
		for(int i = 0; i < am_id.length; i++)
		{
			AlertingMessage am = (AlertingMessage )Pool.get(AlertingMessage.typ, am_id[i]);
			am.setTransferableFromLocal();
			am_t[i] = (AlertingMessage_Transferable )am.getTransferable();
		}

		AlertingMessageUser_Transferable amu_t[] = new AlertingMessageUser_Transferable[amu_id.length];
		for(int i = 0; i < amu_id.length; i++)
		{
			AlertingMessageUser amu = (AlertingMessageUser )Pool.get(AlertingMessageUser.typ, amu_id[i]);
			amu.setTransferableFromLocal();
			amu_t[i] = (AlertingMessageUser_Transferable )amu.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveMaintenanceData(si.accessIdentity, am_t, amu_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving MaintenanceData: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveMaintenanceData! status = " + ecode);
			return;
		}
	}

	public void RemoveMaintenanceData(String amu_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveMaintenanceData(si.accessIdentity, amu_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error RemoveMaintenanceData: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveMaintenanceData! status = " + ecode);
			return;
		}

		Pool.remove(AlertingMessageUser.typ, amu_id);
	}
}