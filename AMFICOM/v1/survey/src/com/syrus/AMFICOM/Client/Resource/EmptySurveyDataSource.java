package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.Result.Analysis;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;

public class EmptySurveyDataSource
		extends EmptyDataSource
		implements DataSourceInterface
{
	protected EmptySurveyDataSource()
	{
	}

	public EmptySurveyDataSource(SessionInterface si)
	{
		super(si);
	}

	public void SaveAnalysis(String analysis_id, String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
/*
		int ecode = 0;

		Analysis analysis = (Analysis )Pool.get(Analysis.typ, analysis_id);
		Result result = (Result )Pool.get(Result.typ, result_id);

		analysis.setTransferableFromLocal();
		Analysis_Transferable analysis_t = (Analysis_Transferable )analysis.getTransferable();
		result.setTransferableFromLocal();
		Result_Transferable result_t = (Result_Transferable )result.getTransferable();
*/
	}

	public void GetTests()
	{
	}

	public String GetUId(String str)
	{
		return str + String.valueOf((int)(Math.random()*1000));
	}


/*	public void GetResults()
	{
		String id = "c:\\DEMO_AB.SOR";
		BellcoreStructure bs = new TraceReader().getData(new File(id));
		byte[] data = new BellcoreWriter().write(bs);
		bs.id = id;

		Parameter param = new Parameter();
		param.type_id = "reflectogram";
		param.value = data;

		Result result = new Result("jo", "analysis", "", "", "");
		result.parameters.add(param);

		System.out.println("put:"+bs);
		Pool.put("result", result.getId(), result);
	}

	public String GetUId(String str)
	{
		return str + String.valueOf((int)(Math.random()*1000));
	}
	*/

	public void GetAlarms()
	{
/*	
	    Alarm alarm;

		alarm = new Alarm();
		alarm.id = "id1";
		alarm.generated = System.currentTimeMillis();
		alarm.source_id = "source1";
		alarm.source_name = "Источник 1";
		alarm.fixed_when = 0;
		alarm.appointed = 0;
		alarm.comments = "комментарии";
		alarm.time_to_fix = 0;
		alarm.alarm_type_name = "Типа, аларм!";
		alarm.fixed_by = "user1";
		alarm.alarm_rule_name = "Правило";
		alarm.status = Constants.ALARM_GENERATED;
		alarm.appointed_to = "user1";
		alarm.reacted = 0;

		Pool.put("alarm", alarm.getId(), alarm);
//		Pool.putName("alarm", alarm.getId(), alarm.getName());
		
		alarm = new Alarm();
		alarm.id = "id2";
		alarm.generated = System.currentTimeMillis();
		alarm.source_id = "source2";
		alarm.source_name = "Источник второй";
		alarm.fixed_when = 0;
		alarm.appointed = System.currentTimeMillis() + 100;
		alarm.comments = "опять комментарии";
		alarm.time_to_fix = 0;
		alarm.alarm_type_name = "Все плохо!";
		alarm.fixed_by = "user2";
		alarm.alarm_rule_name = "Самое правило";
		alarm.status = Constants.ALARM_ASSIGNED;
		alarm.appointed_to = "user2";
		alarm.reacted = 0;

		Pool.put("alarm", alarm.getId(), alarm);
//		Pool.putName("alarm", alarm.getId(), alarm.getName());
		
		alarm = new Alarm();
		alarm.id = "id3";
		alarm.generated = System.currentTimeMillis();
		alarm.source_id = "source3";
		alarm.source_name = "Опять источник";
		alarm.fixed_when = System.currentTimeMillis() + 3000;
		alarm.appointed = System.currentTimeMillis() + 100;
		alarm.comments = "Ок";
		alarm.time_to_fix = 0;
		alarm.alarm_type_name = "Задолбало все!";
		alarm.fixed_by = "user3";
		alarm.alarm_rule_name = "Некое правило";
		alarm.status = Constants.ALARM_FIXED;
		alarm.appointed_to = "user3";
		alarm.reacted = System.currentTimeMillis() + 2000;

		Pool.put("alarm", alarm.getId(), alarm);
//		Pool.putName("alarm", alarm.getId(), alarm.getName());
*/		
	}

	public void SetAlarm(String alarm_id)
	{
	}

	public void GetMessages()
	{
	}

	public void LoadTestTypes()
	{
		TestType testtype;
		TestType_Transferable testtypet;

		testtypet = new TestType_Transferable(
				"ttype1", 
				"Рефлектометрические измерения", 
				"", 
				System.currentTimeMillis(),
				new ActionParameterType_Transferable[0], 
				new ActionParameterType_Transferable[0],
				new String[0],
				new String[0]);
		testtype = new TestType(testtypet);

		Pool.put("testtype", testtype.getId(), testtype);
//		Pool.putName("testtype", testtype.getId(), testtype.getName());

		testtypet = new TestType_Transferable(
				"ttype2", 
				"Другие измерения", 
				"", 
				System.currentTimeMillis(),
				new ActionParameterType_Transferable[0], 
				new ActionParameterType_Transferable[0],
				new String[0],
				new String[0]);
		testtype = new TestType(testtypet);

		Pool.put("testtype", testtype.getId(), testtype);
//		Pool.putName("testtype", testtype.getId(), testtype.getName());
/*
		MapPathElement_Transferable mpet = 
				new MapPathElement_Transferable(
    "path1",
    "path1",
    "path1",
	"type",
    "description",
    "user9",
    "map_id",
    "ism_map_id",
    "startNode_id",
    "endNode_id",
    new ElementAttribute_Transferable[0],
    "font_id",
    "metric",
    true,
    1,
    1,
    "style",
    "selected_style",
    1,
    1,
    "alarmed_style",
    true,
    true,
    "path_id",
	false,
    new String[0]);
		MapTransmissionPathElement mpe = new MapTransmissionPathElement(mpet);
		
		Pool.put("mappathelement", mpe.getId(), mpe);
//		Pool.putName("mappathelement", mpe.getId(), mpe.getName());

		MapKISElement_Transferable mket = new MapKISElement_Transferable(
    "kis1",
    "kisname",
    "type_id",
    "description",
    "user9",
    "0",
    "0",
    "map_id",
    "ism_map_id",
    "symbol_id",
    "KIS_id",
    "KIS_type_id",
    "codename",
    false,
    "scheme_kis_element_id",
    new ElementAttribute_Transferable[0],
    new String[0]);
//		MapKISNodeElement mke = new MapKISNodeElement(mket);
		
//		Pool.put("mapkiselement", mke.getId(), mke);
//		Pool.putName("mapkiselement", mke.getId(), mke.getName());
*/
	}

	public void RequestTest(String request_id, String test_id)
	{
//		TestRequest request = (TestRequest )Pool.get(TestRequest.typ, request_id);
//		Test test = (Test )Pool.get(Test.typ, test_id);

//		request.setTransferableFromLocal();
//		TestRequest_Transferable request_t = (TestRequest_Transferable )request.getTransferable();
//		test.setTransferableFromLocal();
//		Test_Transferable test_t = (Test_Transferable )test.getTransferable();
//		Test_Transferable test_ts[] = new Test_Transferable[1];
//		test_ts[0] = (Test_Transferable )test.getTransferable();
	}


}

