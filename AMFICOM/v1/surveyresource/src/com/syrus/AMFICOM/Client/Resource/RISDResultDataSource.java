package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.General.*;

public class RISDResultDataSource 
		extends RISDActionDataSource
		implements DataSourceInterface 
{
	protected RISDResultDataSource()
	{
		//
	}

	public RISDResultDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadResultSets()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ResultSetSeq_TransferableHolder rsh = new ResultSetSeq_TransferableHolder();
		ResultSet_Transferable resultsets[];
		ResultSet resultset;

		try
		{
			ecode = si.ci.server.GetResultSets(si.accessIdentity, rsh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting ResultSet descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadResultSets! status = " + ecode);
			return;
		}

		resultsets = rsh.value;
		count = resultsets.length;
		System.out.println("...Done! " + count + " ResultSet(s) fetched");
	    for (i = 0; i < count; i++)
		{
			resultset = new ResultSet(resultsets[i]);
			Pool.put("resultset", resultset.getId(), resultset);
	    }
	}

	public void LoadResultSets(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ResultSetSeq_TransferableHolder rsh = new ResultSetSeq_TransferableHolder();
		ResultSet_Transferable resultsets[];
		ResultSet resultset;

		try
		{
			ecode = si.ci.server.GetStatedResultSets(si.accessIdentity, ids, rsh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting ResultSet descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadResultSets! status = " + ecode);
			return;
		}

		resultsets = rsh.value;
		count = resultsets.length;
		System.out.println("...Done! " + count + " ResultSet(s) fetched");
	    for (i = 0; i < count; i++)
		{
			resultset = new ResultSet(resultsets[i]);
			Pool.put("resultset", resultset.getId(), resultset);
	    }
	}

	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder sh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetResultSetResultIds(si.accessIdentity, result_set_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetResultSetResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return sh.value;
	}

	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id, String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder sh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetResultSetResultMEIds(si.accessIdentity, result_set_id, me_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetResultSetResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return sh.value;

	}

	public void GetResults()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		//ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
		ClientResultSeq_TransferableHolder reh = new ClientResultSeq_TransferableHolder();
		ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetResults(si.accessIdentity, reh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResults! status = " + ecode);
			return;
		}

		results = reh.value;
		count = results.length;
		System.out.println("...Done! " + count + " result(s) fetched");
	    for (i = 0; i < count; i++)
		{
			result = new Result(results[i]);
			Pool.put("result", result.getId(), result);
			result.updateLocalFromTransferable();
		}

	}

	public String GetLastResult(String me_id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

//		int i;
		int ecode = 0;
//		int count;
//		ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
		ClientResult_TransferableHolder reh = new ClientResult_TransferableHolder();
//		ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetLastResult(si.accessIdentity, me_id, reh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResult! status = " + ecode);
			return "";
		}

		result = new Result(reh.value);
		Pool.put("result", result.getId(), result);
//		Pool.putName("result", result.getId(), result.getName());
		result.updateLocalFromTransferable();

		return result.getId();
	}

	public String GetTestResult(String test_id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

		int i;
		int ecode = 0;
		int count;
//		ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
		ClientResultSeq_TransferableHolder reh = new ClientResultSeq_TransferableHolder();
		ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetTestResults(si.accessIdentity, test_id, reh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResult! status = " + ecode);
			return "";
		}

		results = reh.value;
		count = results.length;
		System.out.println("...Done! " + count + " result(s) fetched");
	    for (i = 0; i < count; i++)
		{
			result = new Result(results[i]);
			Pool.put("result", result.getId(), result);
			result.updateLocalFromTransferable();
		}

		if(results.length == 0)
			return "";

		return results[results.length - 1].id;
	}
	
	public String GetAnalysisResult(String analysis_id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

		int i;
		int ecode = 0;
		int count;
		ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
//		ClientResult_TransferableHolder reh = new ClientResult_TransferableHolder();
		ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetAnalysisResults(si.accessIdentity, analysis_id, rh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResult! status = " + ecode);
			return "";
		}

		results = rh.value;
		count = results.length;
		System.out.println("...Done! " + count + " result(s) fetched");
	    for (i = 0; i < count; i++)
		{
			result = new Result(results[i]);
			Pool.put("result", result.getId(), result);
			result.updateLocalFromTransferable();
		}

		if(results.length == 0)
			return "";

		return results[results.length - 1].id;
/*
		result = new Result(reh.value);
		Pool.put("result", result.getId(), result);
//		Pool.putName("result", result.getId(), result.getName());
		result.updateLocalFromTransferable();

		return result.getId();
*/
	}
	
	public String[] GetAnalysisResultsForStatistics(
			String monitored_element_id,
			long from,
			long to)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		ClientResult_Transferable results[];
		Result result;
		Vector vec = new Vector();
		String[] ids;

		try
		{
			results = si.ci.server.GetStatisticsAnalysisResults(si.accessIdentity, monitored_element_id, from, to);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = results.length;
		System.out.println("...Done! " + count + " result(s) fetched");
	    for (i = 0; i < count; i++)
		{
			result = new Result(results[i]);
			Pool.put("result", result.getId(), result);
			result.updateLocalFromTransferable();
			vec.add(result.getId());
		}

		ids = new String[vec.size()];
		vec.copyInto(ids);
		return ids;
	}

	public String[] GetAnalysisResultsForStatistics(
			String monitored_element_id,
			long from,
			long to,
			String test_setup_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		ClientResult_Transferable results[];
		Result result;
		Vector vec = new Vector();
		String[] ids;

		try
		{
			results = si.ci.server.GetStatisticsAnalysisResultsByTS(si.accessIdentity, monitored_element_id, from, to, test_setup_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = results.length;
		System.out.println("...Done! " + count + " result(s) fetched");
	    for (i = 0; i < count; i++)
		{
			result = new Result(results[i]);
			Pool.put("result", result.getId(), result);
			result.updateLocalFromTransferable();
			vec.add(result.getId());
		}

		ids = new String[vec.size()];
		vec.copyInto(ids);
		return ids;
	}

	public String GetModelingResult(String modeling_id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

		//int i;
		int ecode = 0;
		//int count;
		//ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
		ClientResult_TransferableHolder reh = new ClientResult_TransferableHolder();
		//ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetModelingResult(si.accessIdentity, modeling_id, reh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResult! status = " + ecode);
			return "";
		}

		result = new Result(reh.value);
		Pool.put("result", result.getId(), result);
//		Pool.putName("result", result.getId(), result.getName());
		result.updateLocalFromTransferable();

		return result.getId();
	}
	
	public String GetEvaluationResult(String evaluation_id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

		int i;
		int ecode = 0;
		int count;
		ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
//		ClientResult_TransferableHolder reh = new ClientResult_TransferableHolder();
		ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetEvaluationResults(si.accessIdentity, evaluation_id, rh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetEvaluationResult! status = " + ecode);
			return "";
		}

		results = rh.value;
		count = results.length;
		System.out.println("...Done! " + count + " result(s) fetched");
	    for (i = 0; i < count; i++)
		{
			result = new Result(results[i]);
			Pool.put("result", result.getId(), result);
			result.updateLocalFromTransferable();
		}

		if(results.length == 0)
			return "";

		return results[results.length - 1].id;
/*
		result = new Result(reh.value);
		Pool.put("result", result.getId(), result);
//		Pool.putName("result", result.getId(), result.getName());
		result.updateLocalFromTransferable();
		return result.getId();
*/
	}
	

	public void GetResult(String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

//		int i;
		int ecode = 0;
//		int count;
//		ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
		ClientResult_TransferableHolder reh = new ClientResult_TransferableHolder();
//		ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetResult(si.accessIdentity, result_id, reh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResult! status = " + ecode);
			return;
		}

		result = new Result(reh.value);
		System.out.println("...Done! Result \'" + result.getId() + "\' fetched");
		Pool.put("result", result.getId(), result);
//		Pool.putName("result", result.getId(), result.getName());
		result.updateLocalFromTransferable();

	}

	public void GetResults(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ClientResultSeq_TransferableHolder rh = new ClientResultSeq_TransferableHolder();
		ClientResult_Transferable results[];
		Result result;

		try
		{
			ecode = si.ci.server.GetStatedResults(si.accessIdentity, ids, rh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Results: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetResult! status = " + ecode);
			return;
		}

		results = rh.value;
		count = results.length;
		System.out.println("...Done! " + count + " result(s) fetched");
	    for (i = 0; i < count; i++)
		{
			result = new Result(results[i]);
			Pool.put("result", result.getId(), result);
			result.updateLocalFromTransferable();
		}
	}
	
	public ResourceDescriptor_Transferable GetLastResultId(String path_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		ResourceDescriptor_TransferableHolder sh = new ResourceDescriptor_TransferableHolder();

		try
		{
			si.ci.server.GetLastResultId(si.accessIdentity, path_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetLastResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		return sh.value;
	}

	public ResourceDescriptor_Transferable[] GetTestResultIds(String test_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder sh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetTestResultIds(si.accessIdentity, test_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetTestResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return sh.value;
	}

	public ResourceDescriptor_Transferable[] GetAnalysisResultIds(String analysis_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder sh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetAnalysisResultIds(si.accessIdentity, analysis_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetAnalysisResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return sh.value;
	}

	public ResourceDescriptor_Transferable GetModelingResultId(String modeling_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		ResourceDescriptor_TransferableHolder sh = new ResourceDescriptor_TransferableHolder();

		try
		{
			si.ci.server.GetModelingResultId(si.accessIdentity, modeling_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetModelingResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		return sh.value;
	}

	public ResourceDescriptor_Transferable[] GetEvaluationResultIds(String evaluation_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];

		ResourceDescriptorSeq_TransferableHolder sh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			si.ci.server.GetEvaluationResultIds(si.accessIdentity, evaluation_id, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting GetEvaluationResultIds: " + ex.getMessage());
			ex.printStackTrace();
			return new ResourceDescriptor_Transferable[0];
		}

		return sh.value;
	}


}

