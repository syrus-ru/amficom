package com.syrus.AMFICOM.analysis.dadara;

import java.util.Enumeration;

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

			String id = new SurveyDataSourceImage(dataSource).GetTestForEvaluation(res.evaluation_id);
			if(id == null)
				return;
			Test test = (Test )Pool.get("test", id);
			dataSource.LoadTestArgumentSets(new String[] {test.test_argument_set_id});
			TestArgumentSet tas = (TestArgumentSet )Pool.get(TestArgumentSet.typ, test.test_argument_set_id);
			Evaluation eval = (Evaluation)Pool.get(Evaluation.typ, res.evaluation_id);
			MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, eval.monitored_element_id);
			if(!me.element_type.equals("path"))
			{
				return;
//				TransmissionPath path = (TransmissionPath )Pool.get(TransmissionPath.typ, me.element_id);
//				path.links.elements();
			}

			delta_x = 1;

//			new SurveyDataSourceImage(dataSource).GetTestResult(id);

			Result tres = null;
			for(int i = 0; i < eval.result_ids.length; i++)
				if(eval.result_ids[i].equals(event.descriptor))
				{
					new SurveyDataSourceImage(dataSource).GetResult(test.result_ids[i]);
					tres = (Result )Pool.get(Result.typ, test.result_ids[i]);
					break;
				}

			if(tres == null)
				return;

			BellcoreStructure bs = null;

			java.util.Enumeration enum = tres.parameters.elements();
			while (enum.hasMoreElements())
			{
				Parameter param = (Parameter)enum.nextElement();
				if (param.gpt.id.equals("reflectogramm"))
				 bs = new BellcoreReader().getData(param.value);
			}
			if (bs == null)
				 return;

			int n = bs.dataPts.TNDP;
			delta_x = (double )(bs.fxdParams.AR - bs.fxdParams.AO) * 3d /
						((double )n * (double )bs.fxdParams.GI / 1000d);
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
			Enumeration  parameters = res.parameters.elements();

			while (parameters.hasMoreElements())
			{
				Parameter param = (Parameter )parameters.nextElement();
				String codename = "";
				String name = "";
				String data_type = param.gpt.value_type;
				if(param.apt != null)
				{
					codename = param.apt.codename;
					name = param.apt.name;
				}
				else if(param.gpt != null)
				{
					codename = param.gpt.codename;
					name = param.gpt.name;
				}

				if (codename.equals("dadara_alarm_array"))
				{
					ra = ReflectogramAlarm.fromByteArray(param.value);
					for(int i = 0; i < ra.length; i++)
						add(new OpticalAlarmDescriptorEvent(test.monitored_element_id, delta_x, ra[i]));
					break;
				}
			}
		}
	}
}