package com.syrus.AMFICOM.analysis.dadara;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
import com.syrus.AMFICOM.Client.Resource.Result.Evaluation;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.AMFICOM.Client.Resource.Result.TestArgumentSet;

import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmDescriptor;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

public class OpticalAlarmDescriptor extends AlarmDescriptor
{
	ReflectogramAlarm []ra;
	double delta_x;

//----------------------------------
	public OpticalAlarmDescriptor(Alarm alarm, DataSourceInterface dataSource)
	{
		super(alarm);

		if(	alarm.type_id.equals("rtutestalarm") ||
			alarm.type_id.equals("rtutestwarning"))
		{
			new SurveyDataSourceImage(dataSource).GetResult(event.descriptor);// подгрузили резулт

			Result res = (Result)Pool.get(Result.typ, event.descriptor); // получили резулт

			String id = new SurveyDataSourceImage(dataSource).GetTestForEvaluation(res.getEvaluationId());
			if(id == null)
				return;
			Test test = (Test )Pool.get("test", id);
			dataSource.LoadTestArgumentSets(new String[] {test.test_argument_set_id});
			TestArgumentSet tas = (TestArgumentSet )Pool.get(TestArgumentSet.typ, test.test_argument_set_id);
			Evaluation eval = (Evaluation)Pool.get(Evaluation.typ, res.getEvaluationId());
			MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, eval.getMonitoredElementId());
			if(!me.element_type.equals("path"))
			{
				return;
//				TransmissionPath path = (TransmissionPath )Pool.get(TransmissionPath.typ, me.element_id);
//				path.links.elements();
			}

			delta_x = 1;

//			new SurveyDataSourceImage(dataSource).GetTestResult(id);

			Result tres = null;
			String[] r_ids = eval.getResultIds();
			for(int i = 0; i < r_ids.length; i++)
				if(r_ids[i].equals(event.descriptor))
				{
					new SurveyDataSourceImage(dataSource).GetResult(test.result_ids[i]);
					tres = (Result )Pool.get(Result.typ, test.result_ids[i]);
					break;
				}

			if(tres == null)
				return;

			BellcoreStructure bs = null;

			java.util.Iterator it = tres.getParameterList().iterator();
			while (it.hasNext())
			{
				Parameter param = (Parameter)it.next();
				if (param.getGpt().getId().equals(AnalysisUtil.REFLECTOGRAMM))
				 bs = new BellcoreReader().getData(param.getValue());
			}
			if (bs == null)
				 return;

			delta_x = bs.getDeltaX();
/*
			for(Enumeration en = tas.arguments.elements(); en.hasMoreElements();)
			{
				Parameter param = (Parameter )en.nextElement();
				if(param.codename.equals("ref_res"))
				{
					try
					{
						delta_x = new ByteArray(param.value).toDouble();
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
					}
					break;
				}
			}
*/
			Iterator parameters = res.getParameterList().iterator();

			while (parameters.hasNext())
			{
				Parameter param = (Parameter)parameters.next();
				String codename = "";
				String name = "";
				String data_type = param.getGpt().getValueType();
				if(param.getApt() != null)
				{
					codename = param.getApt().getCodename();
					name = param.getApt().getName();
				}
				else if(param.getGpt() != null)
				{
					codename = param.getGpt().getCodename();
					name = param.getGpt().getName();
				}

				if (codename.equals("dadara_alarm_array"))
				{
					ra = ReflectogramAlarm.fromByteArray(param.getValue());
					for(int i = 0; i < ra.length; i++)
						add(new OpticalAlarmDescriptorEvent(test.monitored_element_id, delta_x, ra[i]));
					break;
				}
			}
		}
	}
}