//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Класс хранения отображения БД на клиентскую часть          * //
// *           задача модуля - для минимизации трафика клиент-сервер      * //
// *           хранить подгружаемые с сервера объекты, так что при        * //
// *           последующем запуске клиентской части проверяется образ     * //
// *           на наличие необходимых объектов, и в случае их отсутствия  * //
// *           они подгружаются с сервера                                 * //
// *                                                                      * //
// * Тип: Java 1.4.0                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 24 mar 2003                                                      * //
// * Расположение: ISM\prog\java\AMFICOM\com\syrus\AMFICOM\Client\        * //
// *        Resource\DataSourceImage.java                                 * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 9.0.3.9.93                       * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.0)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptorSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.Alarm.EventSource;
import com.syrus.AMFICOM.Client.Resource.Alarm.SystemEvent;
import com.syrus.AMFICOM.Client.Resource.Result.Analysis;
import com.syrus.AMFICOM.Client.Resource.Result.Evaluation;
import com.syrus.AMFICOM.Client.Resource.Result.GlobalParameterType;
import com.syrus.AMFICOM.Client.Resource.Result.Modeling;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Result.ResultSet;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.AMFICOM.Client.Resource.Result.TestArgumentSet;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;
import com.syrus.AMFICOM.Client.Resource.Test.EvaluationType;
import com.syrus.AMFICOM.Client.Resource.Test.ModelingType;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class SurveyDataSourceImage extends DataSourceImage
{
	protected SurveyDataSourceImage()
	{
	}

	public SurveyDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void LoadParameterTypes()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(GlobalParameterType.typ);

//		Pool.removeHash(GlobalParameterType.typ);

		load(GlobalParameterType.typ);
		Vector ids = filter(GlobalParameterType.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadParameterTypes(id_s);
			save(GlobalParameterType.typ);
		}
	}

	public void LoadTestTypes()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(TestType.typ);

//		Pool.removeHash(TestType.typ);

		load(TestType.typ);
		Vector ids = filter(TestType.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadTestTypes(id_s);
			save(TestType.typ);
		}
	}

	public void LoadAnalysisTypes()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(AnalysisType.typ);

//		Pool.removeHash(AnalysisType.typ);

		load(AnalysisType.typ);
		Vector ids = filter(AnalysisType.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadAnalysisTypes(id_s);
			save(AnalysisType.typ);
		}
	}

	public void LoadEvaluationTypes()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(EvaluationType.typ);

//		Pool.removeHash(EvaluationType.typ);

		load(EvaluationType.typ);
		Vector ids = filter(EvaluationType.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadEvaluationTypes(id_s);
			save(EvaluationType.typ);
		}
	}

	public void LoadModelingTypes()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(ModelingType.typ);

//		Pool.removeHash(ModelingType.typ);

		load(ModelingType.typ);
		Vector ids = filter(ModelingType.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadModelingTypes(id_s);
			save(ModelingType.typ);
		}
	}

	public int GetAlarms()
	{
		return GetAlarms(null);
	}

	public int GetAlarms(String filter_id)
	{
		if(!di.getSession().isOpened())
			return 0;

//		System.out.print("GetAlarms...");
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Alarm.typ);

		Vector res_ids_to_load = new Vector();
		Vector an_ids_to_load = new Vector();
		Vector ev_ids_to_load = new Vector();
		Vector test_ids_to_load = new Vector();
		String []res_ids_to_load2;
		String []an_ids_to_load2;
		String []ev_ids_to_load2;
		String []test_ids_to_load2;

		Boolean bool = (Boolean )DataSourceImage.loaded_catalog.get("alarm");
		boolean first_time = (bool == null || bool.equals(Boolean.FALSE));

//		loadFromPool(Alarm.typ);
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

		int len = ids.size();
		if(first_time)
			len = desc.length;
		String id;

		for(int i = 0; i < len; i++)
		{
			id = (first_time) ? desc[i].resource_id : (String )ids.get(i);
			Alarm alarm = (Alarm )Pool.get(Alarm.typ, id);
			if(alarm != null)
			{
				SystemEvent ev = alarm.getEvent();
				if(ev != null)
					if (ev.type_id.equals(Alarm.TEST_ALARM_EVENT)
						 || ev.type_id.equals(Alarm.TEST_WARNING_EVENT))
						res_ids_to_load.add(ev.descriptor);
			}
		}
		res_ids_to_load2 = new String[res_ids_to_load.size()];
		res_ids_to_load.copyInto(res_ids_to_load2);
//		System.out.print("Results(" + res_ids_to_load.size() + ")...");
		GetResults(res_ids_to_load2);

		for(int i = 0; i < len; i++)
		{
			id = (first_time) ? desc[i].resource_id : (String )ids.get(i);
			Alarm alarm = (Alarm )Pool.get(Alarm.typ, id);
			if(alarm != null)
			{
				SystemEvent ev = alarm.getEvent();
				if(ev != null)
				{
					Result res = (Result )Pool.get(Result.typ, ev.descriptor);
					if(res != null)
					{
						if(res.result_type.equals("evaluation"))
							ev_ids_to_load.add(res.action_id);
						else
						if(res.result_type.equals("analysis"))
							an_ids_to_load.add(res.action_id);
						else
						if(res.result_type.equals("test"))
							test_ids_to_load.add(res.action_id);
					}
				}
			}
		}
		ev_ids_to_load2 = new String[ev_ids_to_load.size()];
		ev_ids_to_load.copyInto(ev_ids_to_load2);
//		System.out.print("Evaluations(" + ev_ids_to_load.size() + ")...");
		GetEvaluations(ev_ids_to_load2);

		an_ids_to_load2 = new String[an_ids_to_load.size()];
		an_ids_to_load.copyInto(an_ids_to_load2);
//		System.out.print("Analysis(" + an_ids_to_load.size() + ")...");
		GetAnalysis(an_ids_to_load2);

		test_ids_to_load2 = new String[test_ids_to_load.size()];
		test_ids_to_load.copyInto(test_ids_to_load2);
//		System.out.print("Tests(" + test_ids_to_load.size() + ")...");
		GetTests(test_ids_to_load2);

//		System.out.print("Update(" + len + ")...");
		Vector ids_to_return = new Vector();
		for(int i = 0; i < len; i++)
		{
			id = (first_time) ? desc[i].resource_id : (String )ids.get(i);
			Alarm alarm = (Alarm )Pool.get(Alarm.typ, id);
			if(alarm == null)
			{
//				ids.remove(i);
//				i--;
				continue;
			}
			SystemEvent ev = alarm.getEvent();
			if(ev == null)
			{
//				ids.remove(i);
//				i--;
				continue;
			}
			if (ev.type_id.equals(Alarm.TEST_ALARM_EVENT)
						 || ev.type_id.equals(Alarm.TEST_WARNING_EVENT))
			{
				Result res = (Result )Pool.get(Result.typ, ev.descriptor);
				if(res == null)
				{
//					ids.remove(i);
//					i--;
					continue;
				}
				res.updateLocalFromTransferable();
			}
			ids_to_return.add(id);
		}
/*
			for(int i = 0; i < ids.size(); i++)
			{
				Alarm alarm = (Alarm )Pool.get(Alarm.typ, (String )ids.get(i));
				if(alarm == null)
				{
					ids.remove(i);
					i--;
					continue;
				}
				SystemEvent ev = alarm.getEvent();
				if(ev == null)
				{
					ids.remove(i);
					i--;
					continue;
				}
				if (ev.type_id.equals(Alarm.TEST_ALARM_EVENT)
						 || ev.type_id.equals(Alarm.TEST_WARNING_EVENT))
				{
//					GetResult(ev.descriptor);
					Result res = (Result )Pool.get(Result.typ, ev.descriptor);
					if(res == null)
					{
						ids.remove(i);
						i--;
						continue;
					}
					if(res.result_type.equals("evaluation"))
						GetEvaluation(res.action_id);
					else
					if(res.result_type.equals("analysis"))
						GetAnalysis(res.action_id);
					else
					if(res.result_type.equals("test"))
						GetTests(new String[] { res.action_id } );
					res.updateLocalFromTransferable();
				}
			}
*/
//		System.out.println("Ok!");
		return ids_to_return.size();
	}

	public void GetTests()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Test.typ);

		load(Test.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Test.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.GetTests(id_s);
			save(Test.typ);
		}
	}
/*
	public void GetAnalysises()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Analysis.typ);

		load(Analysis.typ);
		Vector ids = filter(Analysis.typ, desc, true);
		if(ids.size() > 0)
		{
			for(int i = 0; i < ids.size(); i++)
				di.GetAnalysis((String )ids.get(i));
			save(Analysis.typ);
		}
	}

	public void GetEvaluations()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Evaluation.typ);

		load(Evaluation.typ);
		Vector ids = filter(Evaluation.typ, desc, true);
		if(ids.size() > 0)
		{
			for(int i = 0; i < ids.size(); i++)
				di.GetEvaluation((String )ids.get(i));
			save(Evaluation.typ);
		}
	}

	public void GetModelings()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Modeling.typ);

		load(Modeling.typ);
		Vector ids = filter(Modeling.typ, desc, true);
		if(ids.size() > 0)
		{
			for(int i = 0; i < ids.size(); i++)
				di.GetModeling((String )ids.get(i));
			save(Modeling.typ);
		}
	}
*/
	public void GetTestsWithTestArgumentSets()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Test.typ);

		load(Test.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Test.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.GetTests(id_s);
			save(Test.typ);
			for(int i = 0; i < ids.size(); i++)
			{
				Test test = (Test )Pool.get(Test.typ, (String )ids.get(i));
				TestArgumentSet tas = (TestArgumentSet )Pool.get(TestArgumentSet.typ, test.test_argument_set_id);
				if(tas == null)
					di.LoadTestArgumentSets(new String[] {test.test_argument_set_id});
			}
		}
	}

	public void LoadResultSets()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(ResultSet.typ);

		load(ResultSet.typ);
		Vector ids = filter(ResultSet.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadResultSets(id_s);
			save(ResultSet.typ);
		}
	}

	public String[] GetTests(long t1, long t2)
	{
		ResourceDescriptor_Transferable[] desc = getTestDiapazonDescriptors(t1, t2);

		load(Test.typ);
//		loadFromPool(Test.typ);
		String test_ids[] = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			test_ids[i] = desc[i].resource_id;

		Vector ids = filter(Test.typ, desc, false);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.GetTests(id_s);
			save(Test.typ);
		}

		return test_ids;
	}
/*
	public String[] GetOneTimeTests(long t1, long t2)
	{
		ResourceDescriptor_Transferable[] desc = getOneTimeTestDiapazonDescriptors(t1, t2);

		load(Test.typ);
//		loadFromPool(Test.typ);
		String test_ids[] = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			test_ids[i] = desc[i].resource_id;

		Vector ids = filter(Test.typ, desc, false);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.GetTests(id_s);
			save(Test.typ);
		}

		return test_ids;
	}
*/
	public String[] getTestSetupByME(String me_id)
	{
		String[] id_s = di.getTestSetupIdsByME(me_id);

		ResourceDescriptor_Transferable[] desc = GetDescriptorsByIds(TestSetup.typ, id_s);
/*
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[id_s.length];
		for(int i = 0; i < id_s.length; i++)
			desc[i] = GetDescriptor(TestSetup.typ, id_s[i]);
*/
		load(TestSetup.typ);
		Vector ids = filter(TestSetup.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			for(int i = 0; i < id_s2.length; i++)
				di.loadTestSetup(id_s2[i]);
			save(TestSetup.typ);
		}

		return id_s;
	}

	public String[] getTestSetupByTestType(String tt_id)
	{
		String[] id_s = di.getTestSetupIdsByTestType(tt_id);
		ResourceDescriptor_Transferable[] desc = GetDescriptorsByIds(TestSetup.typ, id_s);
/*
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[id_s.length];
		for(int i = 0; i < id_s.length; i++)
			desc[i] = GetDescriptor(TestSetup.typ, id_s[i]);
*/
		load(TestSetup.typ);
		Vector ids = filter(TestSetup.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			for(int i = 0; i < id_s2.length; i++)
				di.loadTestSetup(id_s2[i]);
			save(TestSetup.typ);
		}

		return id_s;
	}

	public void GetAnalysis(String analysis_id)
	{
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];
		desc[0] = GetDescriptor(Analysis.typ, analysis_id);

		load(Analysis.typ);
//		loadFromPool(Analysis.typ);
		Vector ids = filter(Analysis.typ, desc, false);

		if(ids.size() > 0)
		{
			di.GetAnalysis((String )ids.get(0));
			save(Analysis.typ);
		}
	}

	public void GetModeling(String modeling_id)
	{
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];
		desc[0] = GetDescriptor(Modeling.typ, modeling_id);

		load(Modeling.typ);
//		loadFromPool(Modeling.typ);
		Vector ids = filter(Modeling.typ, desc, false);

		if(ids.size() > 0)
		{
			di.GetModeling((String )ids.get(0));
			save(Modeling.typ);
		}
	}

	public void GetEvaluation(String evaluation_id)
	{
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];
		desc[0] = GetDescriptor(Evaluation.typ, evaluation_id);

		load(Evaluation.typ);
//		loadFromPool(Evaluation.typ);
		Vector ids = filter(Evaluation.typ, desc, false);

		if(ids.size() > 0)
		{
			di.GetEvaluation((String )ids.get(0));
			save(Evaluation.typ);
		}
	}

	public void GetTest(String test_id)
	{
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];
		desc[0] = GetDescriptor(Test.typ, test_id);

		load(Test.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Test.typ, desc, false);

		if(ids.size() > 0)
		{
			di.GetTests(new String[] {(String )ids.get(0)});
			save(Test.typ);
		}
	}

	public void GetTests(String[] test_ids)
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptorsByIds(Test.typ, test_ids);
/*
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[test_ids.length];
		for(int i = 0; i < test_ids.length; i++)
			desc[i] = GetDescriptor(Test.typ, test_ids[i]);
*/
		load(Test.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Test.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetTests(id_s2);
			save(Test.typ);
		}
	}

	public void GetAnalysis(String[] analysis_ids)
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptorsByIds(Analysis.typ, analysis_ids);
		load(Analysis.typ);
//		loadFromPool(Analysis.typ);
		Vector ids = filter(Analysis.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetAnalysis(id_s2);
			save(Analysis.typ);
		}
	}

	public void GetModelings(String[] modeling_ids)
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptorsByIds(Modeling.typ, modeling_ids);
		load(Modeling.typ);
//		loadFromPool(Modeling.typ);
		Vector ids = filter(Modeling.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetModelings(id_s2);
			save(Modeling.typ);
		}
	}

	public void GetEvaluations(String[] evaluation_ids)
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptorsByIds(Evaluation.typ, evaluation_ids);
		load(Evaluation.typ);
//		loadFromPool(Evaluation.typ);
		Vector ids = filter(Evaluation.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetEvaluations(id_s2);
			save(Evaluation.typ);
		}
	}

	public String GetLastResult(String path_id)
	{
		ResourceDescriptor_Transferable[] id_s = new ResourceDescriptor_Transferable[1];
		id_s[0] = di.GetLastResultId(path_id);
//		GetResult(id_s);
		LoadResultsByDescriptors(id_s);

		return id_s[0].resource_id;
	}

	public String GetAnalysisResult(String analysis_id)
	{
		ResourceDescriptor_Transferable[] id_s = di.GetAnalysisResultIds(analysis_id);
		LoadResultsByDescriptors(id_s);
/*
		for(int i = 0; i < id_s.length; i++)
			GetResult(id_s[i]);
*/
		if(id_s.length == 0)
			return null;
		return id_s[id_s.length - 1].resource_id;
	}

	public String GetModelingResult(String modeling_id)
	{
		ResourceDescriptor_Transferable[] id_s = new ResourceDescriptor_Transferable[1];
		id_s[0] = di.GetModelingResultId(modeling_id);
//		GetResult(id_s);
		LoadResultsByDescriptors(id_s);

		return id_s[0].resource_id;
	}

	public String GetEvaluationResult(String evaluation_id)
	{
		ResourceDescriptor_Transferable[] id_s = di.GetEvaluationResultIds(evaluation_id);
		LoadResultsByDescriptors(id_s);
/*
		for(int i = 0; i < id_s.length; i++)
			GetResult(id_s[i]);
*/
		if(id_s.length == 0)
			return null;
		return id_s[id_s.length - 1].resource_id;
	}

	public String GetTestResult(String test_id)
	{
		ResourceDescriptor_Transferable[] id_s = di.GetTestResultIds(test_id);
		LoadResultsByDescriptors(id_s);
/*
		for(int i = 0; i < id_s.length; i++)
			GetResult(id_s[i]);
*/
		if(id_s.length == 0)
			return null;
		return id_s[id_s.length - 1].resource_id;
	}


	public void GetResult(String result_id)
	{
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];
		desc[0] = GetDescriptor(Result.typ, result_id);

		load(Result.typ);
//		loadFromPool(Result.typ);
		Vector ids = filter(Result.typ, desc, false);

		if(ids.size() > 0)
		{
			di.GetResult((String )ids.get(0));
			save(Result.typ);
		}
	}

	public void GetResults(String[] result_ids)
	{
		ResourceDescriptor_Transferable[] desc = GetResultDescriptorsByIds(result_ids);
		load(Result.typ);
//		loadFromPool(Result.typ);
		Vector ids = filter(Result.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetResults(id_s2);
			save(Result.typ);
		}
	}

	void LoadResultsByDescriptors(ResourceDescriptor_Transferable[] desc)
	{
		load(Result.typ);
//		loadFromPool(Result.typ);
		Vector ids = filter(Result.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetResults(id_s2);
			save(Result.typ);
		}
	}

	public String[] GetTestsForME(String me_id)
	{
		ResourceDescriptor_Transferable[] desc = di.GetTestsForME(me_id);
		String[] is = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			is[i] = desc[i].resource_id;

		load(Test.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Test.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetTests(id_s2);
			save(Test.typ);
		}

		return is;
	}

	public String[] GetAnalysisForME(String me_id)
	{
		ResourceDescriptor_Transferable[] desc = di.GetAnalysisForME(me_id);
		String[] is = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			is[i] = desc[i].resource_id;

		load(Analysis.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Analysis.typ, desc, false);

		if(ids.size() > 0)
		{
			for(int i = 0; i < ids.size(); i++)
			{
				String id_s2 = (String )ids.get(i);
				di.GetAnalysis(id_s2);
			}
			save(Analysis.typ);
		}

		return is;
	}

	public String[] GetModelingsForSP(String sp_id)
	{
		ResourceDescriptor_Transferable[] desc = di.GetModelingsForSP(sp_id);
		String[] is = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			is[i] = desc[i].resource_id;

		load(Modeling.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Modeling.typ, desc, false);

		if(ids.size() > 0)
		{
			for(int i = 0; i < ids.size(); i++)
			{
				String id_s2 = (String )ids.get(i);
				di.GetModeling(id_s2);
			}
			save(Modeling.typ);
		}

		return is;
	}

	public String[] GetEvaluationsForME(String me_id)
	{
		ResourceDescriptor_Transferable[] desc = di.GetEvaluationsForME(me_id);
		String[] is = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			is[i] = desc[i].resource_id;

		load(Evaluation.typ);
//		loadFromPool(Test.typ);
		Vector ids = filter(Evaluation.typ, desc, false);

		if(ids.size() > 0)
		{
			for(int i = 0; i < ids.size(); i++)
			{
				String id_s2 = (String )ids.get(i);
				di.GetEvaluation(id_s2);
			}
			save(Evaluation.typ);
		}

		return is;
	}

	public String[] GetAlarmsForME(String me_id)
	{
		ResourceDescriptor_Transferable[] desc = di.GetAlarmsForME(me_id);
		String[] is = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			is[i] = desc[i].resource_id;

		load(Alarm.typ);
//		loadFromPool(Test.typ);
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

		LoadResultsByDescriptors(desc);
		return ids;
	}

	public String[] LoadResultSetResultIds(String result_set_id, String me_id)
	{
		ResourceDescriptor_Transferable[] desc = di.LoadResultSetResultIds(result_set_id, me_id);
		String[] ids = new String[desc.length];
		for(int i = 0; i < desc.length; i++)
			ids[i] = desc[i].resource_id;

		LoadResultsByDescriptors(desc);
		return ids;
	}

	public String GetTestForEvaluation(String id)
	{
		String test_id = "";

		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];

		load(Test.typ);

		boolean found = false;
		Hashtable h = Pool.getHash(Test.typ);
		if(h != null)
			for(Enumeration e = h.elements(); e.hasMoreElements();)
			{
				Test te = (Test )e.nextElement();
				if(te.evaluation_id.equals(id))
				{
					test_id = te.id;
					desc[0] = GetDescriptor(Test.typ, te.id);
					found = true;
					break;
				}
			}
		if(!found)
		{
			desc[0] = di.GetTestIdForEvaluation(id);
			test_id = desc[0].resource_id;
		}

//		loadFromPool(Test.typ);
		Vector ids = filter(Test.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetTests(id_s2);
			save(Test.typ);
		}

		return test_id;
	}

	public String GetTestForAnalysis(String id)
	{
		String test_id = "";

		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];

		load(Test.typ);

		boolean found = false;
		Hashtable h = Pool.getHash(Test.typ);
		if(h != null)
			for(Enumeration e = h.elements(); e.hasMoreElements();)
			{
				Test te = (Test )e.nextElement();
				if(te.analysis_id.equals(id))
				{
					test_id = te.id;
					desc[0] = GetDescriptor(Test.typ, te.id);
					found = true;
					break;
				}
			}
		if(!found)
		{
			desc[0] = di.GetTestIdForAnalysis(id);
			test_id = desc[0].resource_id;
		}

//		loadFromPool(Test.typ);
		Vector ids = filter(Test.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetTests(id_s2);
			save(Test.typ);
		}

		return test_id;
	}

	public String GetEvaluationForTest(String id)
	{
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];

		load(Evaluation.typ);

		boolean found = false;
		Test te = (Test )Pool.get(Test.typ, id);
		if(te != null)
		{
			Evaluation ev = (Evaluation )Pool.get(Evaluation.typ, te.evaluation_id);
			if(ev != null)
			{
				desc[0] = GetDescriptor(Evaluation.typ, ev.getId());
				found = true;
			}
		}
		if(!found)
			desc[0] = di.GetEvaluationIdForTest(id);

//		loadFromPool(Test.typ);
		Vector ids = filter(Evaluation.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetEvaluations(id_s2);
			save(Evaluation.typ);
		}

		return desc[0].resource_id;
	}

	public String GetAnalysisForTest(String id)
	{
		ResourceDescriptor_Transferable[] desc = new ResourceDescriptor_Transferable[1];

		load(Analysis.typ);

		boolean found = false;
		Test te = (Test )Pool.get(Test.typ, id);
		if(te != null)
		{
			Analysis an = (Analysis )Pool.get(Analysis.typ, te.analysis_id);
			if(an != null)
			{
				desc[0] = GetDescriptor(Analysis.typ, an.getId());
				found = true;
			}
		}
		if(!found)
			desc[0] = di.GetAnalysisIdForTest(id);

//		loadFromPool(Test.typ);
		Vector ids = filter(Analysis.typ, desc, false);

		if(ids.size() > 0)
		{
			String [] id_s2 = new String[ids.size()];
			ids.copyInto(id_s2);
			di.GetAnalysis(id_s2);
			save(Analysis.typ);
		}

		return desc[0].resource_id;
	}

	public ResourceDescriptor_Transferable[] getTestDiapazonDescriptors(long t1, long t2)
	{
		RISDSessionInfo si = (RISDSessionInfo )di.getSession();

		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptorSeq_TransferableHolder rdh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetTestIdsInDiapazon(si.accessIdentity, t1, t2, rdh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetDescriptors! status = " + ecode);
			return null;
		}
		return rdh.value;
	}
/*
	public ResourceDescriptor_Transferable[] getOneTimeTestDiapazonDescriptors(long t1, long t2)
	{
		RISDSessionInfo si = (RISDSessionInfo )di.getSession();

		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptorSeq_TransferableHolder rdh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetOneTimeTestIdsInDiapazon(si.accessIdentity, t1, t2, rdh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetDescriptors! status = " + ecode);
			return null;
		}
		return rdh.value;
	}
*/
	protected ResourceDescriptor_Transferable[] GetResultDescriptorsByIds(String[] ids)
	{
		if(!(di.getSession() instanceof RISDSessionInfo))
			return null;

		RISDSessionInfo si = (RISDSessionInfo )di.getSession();

		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptorSeq_TransferableHolder rdh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetResultDescriptorsByIds(si.accessIdentity, ids, rdh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting result descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResultDescriptorsByIds! status = " + ecode);
			return null;
		}
		return rdh.value;
	}
}

