package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import org.omg.CORBA.*;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.General.*;

public class RISDSurveyTypeDataSource
		extends RISDMapDataSource
		implements DataSourceInterface
{
	protected RISDSurveyTypeDataSource()
	{
	}

	public RISDSurveyTypeDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadParameterTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		GlobalParameterTypeSeq_TransferableHolder pth = new GlobalParameterTypeSeq_TransferableHolder();
		GlobalParameterType_Transferable paramtypes[];
		GlobalParameterType paramtype;

		try
		{
			ecode = si.ci.server.LoadGlobalParameterTypes(si.accessIdentity, pth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting parameter type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadParameterTypes! status = " + ecode);
			return;
		}

		paramtypes = pth.value;
		count = paramtypes.length;
		System.out.println("...Done! " + count + " paramtype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			paramtype = new GlobalParameterType(paramtypes[i]);
			Pool.put(GlobalParameterType.typ, paramtype.getId(), paramtype);
		 }
	}

	public void LoadParameterTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		GlobalParameterTypeSeq_TransferableHolder pth = new GlobalParameterTypeSeq_TransferableHolder();
		GlobalParameterType_Transferable paramtypes[];
		GlobalParameterType paramtype;

		try
		{
			ecode = si.ci.server.LoadStatedGlobalParameterTypes(si.accessIdentity, ids, pth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting parameter type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadParameterTypes! status = " + ecode);
			return;
		}

		paramtypes = pth.value;
		count = paramtypes.length;
		System.out.println("...Done! " + count + " paramtype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			paramtype = new GlobalParameterType(paramtypes[i]);
			Pool.put(GlobalParameterType.typ, paramtype.getId(), paramtype);
		 }
	}

	public void LoadTestTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		TestTypeSeq_TransferableHolder tth = new TestTypeSeq_TransferableHolder();
		TestType_Transferable testtypes[];
		TestType testtype;

		try
		{
			ecode = si.ci.server.LoadTestTypes(si.accessIdentity, tth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting test type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadTestTypes! status = " + ecode);
			return;
		}

		testtypes = tth.value;
		count = testtypes.length;
		System.out.println("...Done! " + count + " testtype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			testtype = new TestType(testtypes[i]);
			Pool.put("testtype", testtype.getId(), testtype);
//			Pool.putName("testtype", testtype.getId(), testtype.getName());
		 }
	}

	public void LoadTestTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		TestTypeSeq_TransferableHolder tth = new TestTypeSeq_TransferableHolder();
		TestType_Transferable testtypes[];
		TestType testtype;

		try
		{
			ecode = si.ci.server.LoadStatedTestTypes(si.accessIdentity, ids, tth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting test type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadTestTypes! status = " + ecode);
			return;
		}

		testtypes = tth.value;
		count = testtypes.length;
		System.out.println("...Done! " + count + " testtype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			testtype = new TestType(testtypes[i]);
			Pool.put("testtype", testtype.getId(), testtype);
//			Pool.putName("testtype", testtype.getId(), testtype.getName());
		 }
	}

	public void LoadAnalysisTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		AnalysisTypeSeq_TransferableHolder ath = new AnalysisTypeSeq_TransferableHolder();
		AnalysisType_Transferable atypes[];
		AnalysisType atype;

		try
		{
			ecode = si.ci.server.LoadAnalysisTypes(si.accessIdentity, ath);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Analysis type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadAnalysisTypes! status = " + ecode);
			return;
		}

		atypes = ath.value;
		count = atypes.length;
		System.out.println("...Done! " + count + " Analysistype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			atype = new AnalysisType(atypes[i]);
			Pool.put(AnalysisType.typ, atype.getId(), atype);
		 }
	}

	public void LoadEvaluationTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		EvaluationTypeSeq_TransferableHolder eth = new EvaluationTypeSeq_TransferableHolder();
		EvaluationType_Transferable etypes[];
		EvaluationType etype;

		try
		{
			ecode = si.ci.server.LoadEvaluationTypes(si.accessIdentity, eth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Evaluation type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadEvaluationTypes! status = " + ecode);
			return;
		}

		etypes = eth.value;
		count = etypes.length;
		System.out.println("...Done! " + count + " Evaluationtype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			etype = new EvaluationType(etypes[i]);
			Pool.put(EvaluationType.typ, etype.getId(), etype);
		 }
	}

	public void LoadAnalysisTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		AnalysisTypeSeq_TransferableHolder ath = new AnalysisTypeSeq_TransferableHolder();
		AnalysisType_Transferable atypes[];
		AnalysisType atype;

		try
		{
			ecode = si.ci.server.LoadStatedAnalysisTypes(si.accessIdentity, ids, ath);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Analysis type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadAnalysisTypes! status = " + ecode);
			return;
		}

		atypes = ath.value;
		count = atypes.length;
		System.out.println("...Done! " + count + " Analysistype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			atype = new AnalysisType(atypes[i]);
			Pool.put(AnalysisType.typ, atype.getId(), atype);
		 }
	}

	public void LoadEvaluationTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		EvaluationTypeSeq_TransferableHolder eth = new EvaluationTypeSeq_TransferableHolder();
		EvaluationType_Transferable etypes[];
		EvaluationType etype;

		try
		{
			ecode = si.ci.server.LoadStatedEvaluationTypes(si.accessIdentity, ids, eth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Evaluation type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadEvaluationTypes! status = " + ecode);
			return;
		}

		etypes = eth.value;
		count = etypes.length;
		System.out.println("...Done! " + count + " Evaluationtype(s) fetched");
		 for (i = 0; i < count; i++)
		{
			etype = new EvaluationType(etypes[i]);
			Pool.put(EvaluationType.typ, etype.getId(), etype);
		 }
	}

	public void LoadModelingTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ModelingTypeSeq_TransferableHolder mth = new ModelingTypeSeq_TransferableHolder();
		ModelingType_Transferable mtypes[];
		ModelingType mtype;

		try
		{
			ecode = si.ci.server.LoadModelingTypes(si.accessIdentity, mth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Modeling type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadModelingTypes! status = " + ecode);
			return;
		}

		mtypes = mth.value;
		count = mtypes.length;
		System.out.println("...Done! " + count + " Modeling type(s) fetched");
		 for (i = 0; i < count; i++)
		{
			mtype = new ModelingType(mtypes[i]);
			Pool.put(ModelingType.typ, mtype.getId(), mtype);
		 }
	}

	public void LoadModelingTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ModelingTypeSeq_TransferableHolder mth = new ModelingTypeSeq_TransferableHolder();
		ModelingType_Transferable mtypes[];
		ModelingType mtype;

		try
		{
			ecode = si.ci.server.LoadStatedModelingTypes(si.accessIdentity, ids, mth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Modeling type descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadModelingTypes! status = " + ecode);
			return;
		}

		mtypes = mth.value;
		count = mtypes.length;
		System.out.println("...Done! " + count + " Modeling type(s) fetched");
		 for (i = 0; i < count; i++)
		{
			mtype = new ModelingType(mtypes[i]);
			Pool.put(ModelingType.typ, mtype.getId(), mtype);
		 }
	}

}