package com.syrus.AMFICOM.analysis.dadara;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmDescriptor;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.io.*;

public class OpticalAlarmDescriptor extends AlarmDescriptor
{
	ReflectogramAlarm []ra;
	private double deltaX;

	public OpticalAlarmDescriptor(Alarm alarm)
	{
		super(alarm);

		if(	alarm.type_id.equals("rtutestalarm") ||
			alarm.type_id.equals("rtutestwarning"))
		{
			try
			{
				Result res = (Result)MeasurementStorableObjectPool.getStorableObject(new Identifier(event.descriptor), true);
				Test test = (Test)MeasurementStorableObjectPool.getStorableObject(res.getMeasurement().getTestId(), true);

				MonitoredElement me = test.getMonitoredElement();
				if(!me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH))
					return;

				ResultSortCondition condition = new ResultSortCondition(res.getMeasurement(), ResultSort.RESULT_SORT_ANALYSIS);
				List resIds = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);

				BellcoreStructure bs = null;
				for(Iterator it = resIds.iterator(); it.hasNext(); )
				{
					Result tres = (Result)it.next();
					SetParameter[] parameters = tres.getParameters();
					for(int i = 0; i < parameters.length; i++)
					{
						SetParameter param = parameters[i];
						ParameterType type = (ParameterType)param.getType();
						if(type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
						{
							bs = new BellcoreReader().getData(param.getValue());
							break;
						}
					}
				}
				if(bs == null)
				{
					return;
				}

				deltaX = bs.getResolution();

				SetParameter[] parameters = res.getParameters();
				for(int i = 0; i < parameters.length; i++)
				{
					SetParameter param = parameters[i];
					ParameterType type = (ParameterType)param.getType();
					if(type.getCodename().equals(ParameterTypeCodenames.DADARA_ALARMS))
					{
						ra = ReflectogramAlarm.fromByteArray(param.getValue());
						for(int j = 0; j < ra.length; j++)
						{
							add(new OpticalAlarmDescriptorEvent(test.getMonitoredElement().getId(), deltaX, ra[i]));
						}
						break;
					}
				}
			}
			catch(ApplicationException ex)
			{
				ex.printStackTrace();
			}


//			new SurveyDataSourceImage(dataSource).GetResult(event.descriptor);// подгрузили резулт
//			Result res = (Result)Pool.get(Result.TYPE, event.descriptor); // получили резулт
//			String id = new SurveyDataSourceImage(dataSource).GetTestForEvaluation(res.getEvaluationId());
//			if(id == null)
//				return;
//			Test test = (Test )Pool.get("test", id);
//			dataSource.LoadTestArgumentSets(new String[] {test.getTestArgumentSetId()});
//			TestArgumentSet tas = (TestArgumentSet )Pool.get(TestArgumentSet.TYPE, test.getTestArgumentSetId());
//			Evaluation eval = (Evaluation)Pool.get(Evaluation.TYPE, res.getEvaluationId());
//			MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, eval.getMonitoredElementId());
//			if(!me.elementType.equals("path"))
//			{
//				return;
//			}

//			deltaX = 1;

//			new SurveyDataSourceImage(dataSource).GetTestResult(id);

//			Result tres = null;
//			String[] r_ids = eval.getResultIds();
//			for(int i = 0; i < r_ids.length; i++)
//				if(r_ids[i].equals(event.descriptor))
//				{
//					new SurveyDataSourceImage(dataSource).GetResult(test.getResultIds()[i]);
//					tres = (Result )Pool.get(Result.TYPE, test.getResultIds()[i]);
//					break;
//				}
//
//			if(tres == null)
//				return;
//
//			BellcoreStructure bs = null;
//
//			java.util.Iterator it = tres.getParameterList().iterator();
//			while (it.hasNext())
//			{
//				Parameter param = (Parameter)it.next();
//				if (param.getGpt().getId().equals(AnalysisUtil.REFLECTOGRAMM))
//				 bs = new BellcoreReader().getData(param.getValue());
//			}
//			if (bs == null)
//				 return;
//
//			deltaX = bs.getDeltaX();
/*
			for(Enumeration en = tas.arguments.elements(); en.hasMoreElements();)
			{
				Parameter param = (Parameter )en.nextElement();
				if(param.codename.equals("ref_res"))
				{
					try
					{
						deltaX = new ByteArray(param.value).toDouble();
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
					}
					break;
				}
			}
*/
//			Iterator parameters = res.getParameterList().iterator();
//
//			while (parameters.hasNext())
//			{
//				Parameter param = (Parameter)parameters.next();
//				String codename = "";
//				String name = "";
//				String data_type = param.getGpt().getValueType();
//				if(param.getApt() != null)
//				{
//					codename = param.getApt().getCodename();
//					name = param.getApt().getName();
//				}
//				else if(param.getGpt() != null)
//				{
//					codename = param.getGpt().getCodename();
//					name = param.getGpt().getName();
//				}
//
//				if (codename.equals("dadara_alarm_array"))
//				{
//					ra = ReflectogramAlarm.fromByteArray(param.getValue());
//					for(int i = 0; i < ra.length; i++)
//						add(new OpticalAlarmDescriptorEvent(test.getMonitoredElementId(), deltaX, ra[i]));
//					break;
//				}
//			}
		}
	}
}