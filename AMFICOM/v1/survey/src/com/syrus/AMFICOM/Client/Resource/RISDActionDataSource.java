package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

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

public class RISDActionDataSource 
		extends RISDTestSetupDataSource
		implements DataSourceInterface 
{
	protected RISDActionDataSource()
	{
	}

	public RISDActionDataSource(SessionInterface si)
	{
		super(si);
	}

	public void RequestTest(String request_id, String test_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		TestRequest request = (TestRequest )Pool.get(TestRequest.typ, request_id);
		Test test = (Test )Pool.get(Test.typ, test_id);

		request.setTransferableFromLocal();
		ClientTestRequest_Transferable request_t = (ClientTestRequest_Transferable )request.getTransferable();
		test.setTransferableFromLocal();
		ClientTest_Transferable test_t = (ClientTest_Transferable )test.getTransferable();
		ClientTest_Transferable test_ts[] = new ClientTest_Transferable[1];
		test_ts[0] = (ClientTest_Transferable )test.getTransferable();

		try
		{
			ecode = si.ci.server.RequestTest(
					si.accessIdentity,
					request_t,
					test_ts);
		}
		catch (Exception ex)
		{
			System.err.print("Error requesting tests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RequestTest! status = " + ecode);
			return;
		}

	}

	public void RequestTest(String request_id, String[] test_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		TestRequest request = (TestRequest )Pool.get(TestRequest.typ, request_id);
		request.setTransferableFromLocal();
		ClientTestRequest_Transferable request_t = (ClientTestRequest_Transferable )request.getTransferable();

		ClientTest_Transferable test_ts[] = new ClientTest_Transferable[test_ids.length];
		for(int i = 0; i < test_ids.length; i++)
		{
			Test test = (Test )Pool.get(Test.typ, test_ids[i]);
			test.setTransferableFromLocal();
			test_ts[i] = (ClientTest_Transferable )test.getTransferable();
		}

		try
		{
			ecode = si.ci.server.RequestTest(
					si.accessIdentity,
					request_t,
					test_ts);
		}
		catch (Exception ex)
		{
			System.err.print("Error requesting tests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RequestTest! status = " + ecode);
			return;
		}

	}

	public void SaveAnalysis(String analysis_id, String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		Analysis analysis = (Analysis )Pool.get(Analysis.typ, analysis_id);
		Result result = (Result )Pool.get(Result.typ, result_id);

		analysis.setTransferableFromLocal();
		ClientAnalysis_Transferable analysis_t = (ClientAnalysis_Transferable )analysis.getTransferable();
		result.setTransferableFromLocal();
		ClientResult_Transferable result_t = (ClientResult_Transferable )result.getTransferable();

		try
		{
			ecode = si.ci.server.SaveAnalysis(
					si.accessIdentity,
					analysis_t,
					result_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving analysis result: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveAnalysis! status = " + ecode);
			return;
		}
	}

	public void SaveModeling(String modeling_id, String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		Modeling modeling = (Modeling )Pool.get(Modeling.typ, modeling_id);
		Result result = (Result )Pool.get(Result.typ, result_id);

		modeling.setTransferableFromLocal();
		ClientModeling_Transferable modeling_t = (ClientModeling_Transferable )modeling.getTransferable();
		result.setTransferableFromLocal();
		ClientResult_Transferable result_t = (ClientResult_Transferable )result.getTransferable();

		try
		{
			ecode = si.ci.server.SaveModeling(
					si.accessIdentity,
					modeling_t,
					result_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving modeling result: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveModeling! status = " + ecode);
			return;
		}
	}

	public void SaveEvaluation(String evaluation_id, String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		Evaluation evaluation = (Evaluation )Pool.get(Evaluation.typ, evaluation_id);
		Result result = (Result )Pool.get(Result.typ, result_id);

		evaluation.setTransferableFromLocal();
		ClientEvaluation_Transferable evaluation_t = (ClientEvaluation_Transferable )evaluation.getTransferable();
		result.setTransferableFromLocal();
		ClientResult_Transferable result_t = (ClientResult_Transferable )result.getTransferable();

		try
		{
			ecode = si.ci.server.SaveEvaluation(
					si.accessIdentity,
					evaluation_t,
					result_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Evaluation result: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveEvaluation! status = " + ecode);
			return;
		}
	}

	public void createAnalysis(String analysis_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String a_id = "";

		Analysis analysis = (Analysis )Pool.get(Analysis.typ, analysis_id);

		analysis.setTransferableFromLocal();
		ClientAnalysis_Transferable analysis_t = (ClientAnalysis_Transferable )analysis.getTransferable();

		try
		{
			a_id = si.ci.server.createAnalysis(
					si.accessIdentity,
					analysis_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error creating analysis: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

	}
	
	public void createEvaluation(String evaluation_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String e_id = "";

		Evaluation evaluation = (Evaluation )Pool.get(Evaluation.typ, evaluation_id);

		evaluation.setTransferableFromLocal();
		ClientEvaluation_Transferable evaluation_t = (ClientEvaluation_Transferable )evaluation.getTransferable();

		try
		{
			e_id = si.ci.server.createEvaluation(
					si.accessIdentity,
					evaluation_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error creating Evaluation: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}
	
	public void RemoveTests(String[] test_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.server.RemoveTests(si.accessIdentity, test_ids);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing tests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveTests! status = " + ecode);
			return;
		}
	}
	
	public void UpdateTests(String[] test_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		ClientTest_Transferable test_ts[] = new ClientTest_Transferable[test_ids.length];
		for(int i = 0; i < test_ids.length; i++)
		{
			Test test = (Test )Pool.get(Test.typ, test_ids[i]);
			test.setTransferableFromLocal();
			test_ts[i] = (ClientTest_Transferable )test.getTransferable();
		}

		try
		{
			ecode = si.ci.server.UpdateTests(si.accessIdentity, test_ts);
		}
		catch (Exception ex)
		{
			System.err.print("Error updating tests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed UpdateTests! status = " + ecode);
			return;
		}
	}

	public void GetTests()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientTestSeq_TransferableHolder th = new ClientTestSeq_TransferableHolder();
		ClientTest_Transferable tests[];
		Test test;

		try
		{
			ecode = si.ci.server.GetTests(si.accessIdentity, th);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting tests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetTests! status = " + ecode);
			return;
		}

		tests = th.value;
		count = tests.length;
		System.out.println("...Done! " + count + " test(s) fetched");
	    for (i = 0; i < count; i++)
		{
			test = new Test(tests[i]);
			Pool.put("test", test.getId(), test);
//			Pool.putName("test", test.getId(), test.getName());
			test.updateLocalFromTransferable();
	    }
	}

	public void GetTests(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientTestSeq_TransferableHolder th = new ClientTestSeq_TransferableHolder();
		ClientTest_Transferable tests[];
		Test test;

		try
		{
			ecode = si.ci.server.GetStatedTests(si.accessIdentity, ids, th);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting tests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetTests! status = " + ecode);
			return;
		}

		tests = th.value;
		count = tests.length;
		System.out.println("...Done! " + count + " test(s) fetched");
	    for (i = 0; i < count; i++)
		{
			test = new Test(tests[i]);
			Pool.put("test", test.getId(), test);
//			Pool.putName("test", test.getId(), test.getName());
			test.updateLocalFromTransferable();
	    }
	}

	public ResourceDescriptor_Transferable[] getAlarmedTests()
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int i;
		int ecode = 0;
		int count;
		ResourceDescriptorSeq_TransferableHolder tih = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetAlarmedTests(si.accessIdentity, tih);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting alarmed tests: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetAlarmedTests! status = " + ecode);
			return new ResourceDescriptor_Transferable[0];
		}
		return tih.value;
	}

	public void GetRequests()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientTestRequestSeq_TransferableHolder trh = new ClientTestRequestSeq_TransferableHolder();
		ClientTestRequest_Transferable treqs[];
		TestRequest treq;

		try
		{
			ecode = si.ci.server.GetRequests(si.accessIdentity, trh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting TestRequests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetTestRequests! status = " + ecode);
			return;
		}

		treqs = trh.value;
		count = treqs.length;
		System.out.println("...Done! " + count + " treq(s) fetched");
	    for (i = 0; i < count; i++)
		{
			treq = new TestRequest(treqs[i]);
			Pool.put("testrequest", treq.getId(), treq);
//			Pool.putName("testrequest", treq.getId(), treq.getName());
			treq.updateLocalFromTransferable();
	    }
	}

	public void GetRequestTests(String request_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientTestSeq_TransferableHolder th = new ClientTestSeq_TransferableHolder();
		ClientTest_Transferable tests[];
		Test test;

		try
		{
			ecode = si.ci.server.GetRequestTests(si.accessIdentity, request_id, th);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting tests: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetRequestTests! status = " + ecode);
			return;
		}

		tests = th.value;
		count = tests.length;
		System.out.println("...Done! " + count + " test(s) fetched");
	    for (i = 0; i < count; i++)
		{
			test = new Test(tests[i]);
			Pool.put("test", test.getId(), test);
//			Pool.putName("test", test.getId(), test.getName());
			test.updateLocalFromTransferable();
	    }
	}

	public void GetAnalysis()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientAnalysisSeq_TransferableHolder ah = new ClientAnalysisSeq_TransferableHolder();
		ClientAnalysis_Transferable analysiss[];
		Analysis analysis;

		try
		{
			ecode = si.ci.server.GetAnalysis(si.accessIdentity, ah);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Analysiss: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadTestTypes! status = " + ecode);
			return;
		}

		analysiss = ah.value;
		count = analysiss.length;
		System.out.println("...Done! " + count + " analysis(s) fetched");
	    for (i = 0; i < count; i++)
		{
			analysis = new Analysis(analysiss[i]);
			Pool.put("analysis", analysis.getId(), analysis);
//			Pool.putName("analysis", analysis.getId(), analysis.getName());
			analysis.updateLocalFromTransferable();
	    }
	}

	public void GetAnalysis(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientAnalysisSeq_TransferableHolder ah = new ClientAnalysisSeq_TransferableHolder();
		ClientAnalysis_Transferable analysiss[];
		Analysis analysis;

		try
		{
			ecode = si.ci.server.LoadStatedAnalysis(si.accessIdentity, ids, ah);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Analysiss: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadAnalysis! status = " + ecode);
			return;
		}

		analysiss = ah.value;
		count = analysiss.length;
		System.out.println("...Done! " + count + " analysis(s) fetched");
	    for (i = 0; i < count; i++)
		{
			analysis = new Analysis(analysiss[i]);
			Pool.put("analysis", analysis.getId(), analysis);
//			Pool.putName("analysis", analysis.getId(), analysis.getName());
			analysis.updateLocalFromTransferable();
	    }
	}

	public void GetModelings()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientModelingSeq_TransferableHolder mh = new ClientModelingSeq_TransferableHolder();
		ClientModeling_Transferable modelings[];
		Modeling modeling;

		try
		{
			ecode = si.ci.server.GetModelings(si.accessIdentity, mh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Modelings: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetModelings! status = " + ecode);
			return;
		}

		modelings = mh.value;
		count = modelings.length;
		System.out.println("...Done! " + count + " modeling(s) fetched");
	    for (i = 0; i < count; i++)
		{
			modeling = new Modeling(modelings[i]);
			Pool.put("modeling", modeling.getId(), modeling);
//			Pool.putName("modeling", modeling.getId(), modeling.getName());
			modeling.updateLocalFromTransferable();
	    }
	}

	public void GetModelings(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientModelingSeq_TransferableHolder mh = new ClientModelingSeq_TransferableHolder();
		ClientModeling_Transferable modelings[];
		Modeling modeling;

		try
		{
			ecode = si.ci.server.LoadStatedModelings(si.accessIdentity, ids, mh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Modelings: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetModelings! status = " + ecode);
			return;
		}

		modelings = mh.value;
		count = modelings.length;
		System.out.println("...Done! " + count + " modeling(s) fetched");
	    for (i = 0; i < count; i++)
		{
			modeling = new Modeling(modelings[i]);
			Pool.put("modeling", modeling.getId(), modeling);
//			Pool.putName("modeling", modeling.getId(), modeling.getName());
			modeling.updateLocalFromTransferable();
	    }
	}

	public void GetEvaluations()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientEvaluationSeq_TransferableHolder eh = new ClientEvaluationSeq_TransferableHolder();
		ClientEvaluation_Transferable evaluations[];
		Evaluation evaluation;

		try
		{
			ecode = si.ci.server.GetEvaluations(si.accessIdentity, eh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Evaluation: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetEvaluations! status = " + ecode);
			return;
		}

		evaluations = eh.value;
		count = evaluations.length;
		System.out.println("...Done! " + count + " Evaluation(s) fetched");
	    for (i = 0; i < count; i++)
		{
			evaluation = new Evaluation(evaluations[i]);
			Pool.put("evaluation", evaluation.getId(), evaluation);
//			Pool.putName("evaluation", evaluation.getId(), evaluation.getName());
			evaluation.updateLocalFromTransferable();
	    }
	}

	public void GetEvaluations(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientEvaluationSeq_TransferableHolder eh = new ClientEvaluationSeq_TransferableHolder();
		ClientEvaluation_Transferable evaluations[];
		Evaluation evaluation;

		try
		{
			ecode = si.ci.server.LoadStatedEvaluations(si.accessIdentity, ids, eh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Evaluation: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetEvaluations! status = " + ecode);
			return;
		}

		evaluations = eh.value;
		count = evaluations.length;
		System.out.println("...Done! " + count + " Evaluation(s) fetched");
	    for (i = 0; i < count; i++)
		{
			evaluation = new Evaluation(evaluations[i]);
			Pool.put("evaluation", evaluation.getId(), evaluation);
//			Pool.putName("evaluation", evaluation.getId(), evaluation.getName());
			evaluation.updateLocalFromTransferable();
	    }
	}

	public ResourceDescriptor_Transferable[] GetTestsForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder tih = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetTestIdsForMonitoredElement(si.accessIdentity, me_id, tih);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting tests: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return tih.value;
	}

	public ResourceDescriptor_Transferable[] GetAnalysisForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder tih = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetAnalysisIdsForMonitoredElement(si.accessIdentity, me_id, tih);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Analysiss: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return tih.value;
	}

	public ResourceDescriptor_Transferable[] GetModelingsForSP(String sp_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder tih = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetModelingIdsForSchemePath(si.accessIdentity, sp_id, tih);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Modelings: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return tih.value;
	}

	public ResourceDescriptor_Transferable[] GetEvaluationsForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder tih = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetEvaluationIdsForMonitoredElement(si.accessIdentity, me_id, tih);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Evaluation: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return tih.value;
	}

	public void GetAnalysis(String id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
		ClientAnalysis_Transferable analysis_t;
		Analysis analysis;

		try
		{
			analysis_t = si.ci.server.getAnalysisById(si.accessIdentity, id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Analysis: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		analysis = new Analysis(analysis_t);
		Pool.put("analysis", analysis.getId(), analysis);
		analysis.updateLocalFromTransferable();
	}
	
	public void GetEvaluation(String id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ClientEvaluation_Transferable evaluation_t;
		Evaluation evaluation;

		try
		{
			evaluation_t = si.ci.server.getEvaluationById(si.accessIdentity, id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Evaluation: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		evaluation = new Evaluation(evaluation_t);
		Pool.put("evaluation", evaluation.getId(), evaluation);
		evaluation.updateLocalFromTransferable();
	}
	
	public void GetModeling(String id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ClientModeling_Transferable modeling_t;
		Modeling modeling;

		try
		{
			modeling_t = si.ci.server.getModelingById(si.accessIdentity, id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Modeling: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		modeling = new Modeling(modeling_t);
		Pool.put("modeling", modeling.getId(), modeling);
		modeling.updateLocalFromTransferable();
	}
	
	public String GetTestForAnalysis(String id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

		ClientTest_Transferable test_t;
		Test test;

		try
		{
			test_t = si.ci.server.getTestByAnalysis(si.accessIdentity, id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Test for Analysis: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}
		if(test_t == null)
			return "";
			
		test = new Test(test_t);
		Pool.put("test", test.getId(), test);
		test.updateLocalFromTransferable();
		return test.getId();
	}
	
	public String GetTestForEvaluation(String id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

		ClientTest_Transferable test_t;
		Test test;

		try
		{
			test_t = si.ci.server.getTestByEvaluation(si.accessIdentity, id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Test for Evaluation: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}
		if(test_t == null)
			return "";
			
		test = new Test(test_t);
		Pool.put("test", test.getId(), test);
		test.updateLocalFromTransferable();
		return test.getId();
	}

	public ResourceDescriptor_Transferable GetTestIdForEvaluation(String id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		try
		{
			ResourceDescriptor_Transferable rh = si.ci.server.getTestIdByEvaluation(si.accessIdentity, id);
			return rh;
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetTestIdForEvaluation: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	public ResourceDescriptor_Transferable GetTestIdForAnalysis(String id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		try
		{
			ResourceDescriptor_Transferable rh = si.ci.server.getTestIdByAnalysis(si.accessIdentity, id);
			return rh;
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetTestIdForAnalysis: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	public ResourceDescriptor_Transferable GetEvaluationIdForTest(String id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		try
		{
			ResourceDescriptor_Transferable rh = si.ci.server.getEvaluationIdByTest(si.accessIdentity, id);
			return rh;
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetEvaluationIdForTest: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	public ResourceDescriptor_Transferable GetAnalysisIdForTest(String id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		try
		{
			ResourceDescriptor_Transferable rh = si.ci.server.getAnalysisIdByTest(si.accessIdentity, id);
			return rh;
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetAnalysisIdForTest: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}


}