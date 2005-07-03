package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.Alarm.EventSource;
import com.syrus.AMFICOM.Client.Resource.Alarm.SystemEvent;

import java.util.Vector;

public class SurveyDataSourceImage extends DataSourceImage
{
	protected SurveyDataSourceImage(){
		//
	}

	public SurveyDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public int GetAlarms()
	{
		return GetAlarms(null);
	}

	public int GetAlarms(String filter_id)
	{
		if(!di.getSession().isOpened())
			return 0;

		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Alarm.typ);

		Boolean bool = (Boolean )DataSourceImage.loaded_catalog.get("alarm");
		boolean first_time = (bool == null || bool.equals(Boolean.FALSE));

		load(Alarm.typ);
		load(EventSource.typ);
		load(SystemEvent.typ);
		Vector ids = filter(Alarm.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.GetAlarms(id_s, filter_id);
			save(Alarm.typ);
			save(EventSource.typ);
			save(SystemEvent.typ);
		}

		return ids.size();
	}

	public String[] GetAlarmsForME(String me_id)
	{
		ResourceDescriptor_Transferable[] desc = di.GetAlarmsForME(me_id);
		String[] is = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			is[i] = desc[i].resource_id;

		load(Alarm.typ);
//		loadFromPool(Test.TYPE);
		Vector ids = filter(Alarm.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetAlarms(id_s2);
			save(Alarm.typ);
		}
		return is;
	}

	public String[] LoadResultSetResultIds(String result_set_id)
	{
		ResourceDescriptor_Transferable[] desc = di.LoadResultSetResultIds(result_set_id);
		String[] ids = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			ids[i] = desc[i].resource_id;

		return ids;
	}

	public String[] LoadResultSetResultIds(String result_set_id, String me_id)
	{
		ResourceDescriptor_Transferable[] desc = di.LoadResultSetResultIds(result_set_id, me_id);
		String[] ids = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			ids[i] = desc[i].resource_id;

		return ids;
	}

}

