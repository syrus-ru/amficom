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

public class RISDTestSetupDataSource 
		extends RISDSurveyTypeDataSource
		implements DataSourceInterface 
{
	protected RISDTestSetupDataSource()
	{
	}

	public RISDTestSetupDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadCriteriaSets()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadCriteriaSets(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ClientCriteriaSet_Transferable cs_t;
		CriteriaSet cs;
		
	    for (int i = 0; i < ids.length; i++)
		{
			try
			{
				cs_t = si.ci.server.getCriteriaSet(si.accessIdentity, ids[i]);
				cs = new CriteriaSet(cs_t);
				Pool.put(CriteriaSet.typ, cs.getId(), cs);
			}
			catch (Exception ex)
			{
				System.err.print("Error getting CriteriaSet: " + ex.getMessage());
				ex.printStackTrace();
				return;
			}
		}
	}

	public void saveCriteriaSet(String cs_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		CriteriaSet cs = (CriteriaSet )Pool.get(CriteriaSet.typ, cs_id);
		cs.setTransferableFromLocal();
		ClientCriteriaSet_Transferable cs_t = (ClientCriteriaSet_Transferable )cs.getTransferable();
		try
		{
			si.ci.server.createCriteriaSet(si.accessIdentity, cs_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saveCriteriaSet: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

	}

	public void attachCriteriaSetToME(String cs_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		try
		{
			si.ci.server.attachCriteriaSetToME(si.accessIdentity, cs_id, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error attachCriteriaSetToME: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public String[] getCriteriaSetsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		String ids[];
		ClientCriteriaSet_Transferable css[];
		CriteriaSet cs;

		try
		{
			css = si.ci.server.getCriteriaSetsByME(si.accessIdentity, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getCriteriaSetsByME: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = css.length;
		ids = new String[count];
		System.out.println("...Done! " + count + " Evaluation(s) fetched");
	    for (i = 0; i < count; i++)
		{
			cs = new CriteriaSet(css[i]);
			Pool.put(CriteriaSet.typ, cs.getId(), cs);
			cs.updateLocalFromTransferable();
			ids[i] = cs.getId();
	    }
		return ids;
	}

	public void LoadThresholdSets()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadThresholdSets(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ClientThresholdSet_Transferable ths_t;
		ThresholdSet ths;
		
	    for (int i = 0; i < ids.length; i++)
		{
			try
			{
				ths_t = si.ci.server.getThresholdSet(si.accessIdentity, ids[i]);
				ths = new ThresholdSet(ths_t);
				Pool.put(ThresholdSet.typ, ths.getId(), ths);
			}
			catch (Exception ex)
			{
				System.err.print("Error getting ThresholdSet: " + ex.getMessage());
				ex.printStackTrace();
				return;
			}
		}
	}

	public void saveThresholdSet(String ts_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ThresholdSet ts = (ThresholdSet )Pool.get(ThresholdSet.typ, ts_id);
		ts.setTransferableFromLocal();
		ClientThresholdSet_Transferable ts_t = (ClientThresholdSet_Transferable )ts.getTransferable();
		try
		{
			si.ci.server.createThresholdSet(si.accessIdentity, ts_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saveThresholdSet: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void attachThresholdSetToME(String ths_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		try
		{
			si.ci.server.attachThresholdSetToME(si.accessIdentity, ths_id, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error attachThresholdSetToME: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public String[] getThresholdSetsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		String ids[];
		ClientThresholdSet_Transferable tss[];
		ThresholdSet ts;

		try
		{
			tss = si.ci.server.getThresholdSetsByME(si.accessIdentity, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getThresholdSetsByME: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = tss.length;
		ids = new String[count];
		System.out.println("...Done! " + count + " Threshold(s) fetched");
	    for (i = 0; i < count; i++)
		{
			ts = new ThresholdSet(tss[i]);
			Pool.put(ThresholdSet.typ, ts.getId(), ts);
			ts.updateLocalFromTransferable();
			ids[i] = ts.getId();
	    }
		return ids;
	}

	public void LoadEtalons()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadEtalons(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ClientEtalon_Transferable e_t;
		Etalon e;
		
	    for (int i = 0; i < ids.length; i++)
		{
			try
			{
				e_t = si.ci.server.getEtalon(si.accessIdentity, ids[i]);
				e = new Etalon(e_t);
				Pool.put(Etalon.typ, e.getId(), e);
			}
			catch (Exception ex)
			{
				System.err.print("Error getting Etalon: " + ex.getMessage());
				ex.printStackTrace();
				return;
			}
		}
	}

	public String getEtalonByMEAndTime(String me_id, long time)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";

		ClientEtalon_Transferable e_t;
		Etalon e;

		try
		{
			e_t = si.ci.server.getEtalonByMEandTime(si.accessIdentity, me_id, time);
			e = new Etalon(e_t);
			Pool.put(Etalon.typ, e.getId(), e);
			return e.getId();
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Etalon: " + ex.getMessage());
			ex.printStackTrace();
			return "";
		}
	}
	
	public void saveEtalon(String e_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		Etalon ts = (Etalon )Pool.get(Etalon.typ, e_id);
		ts.setTransferableFromLocal();
		ClientEtalon_Transferable ts_t = (ClientEtalon_Transferable )ts.getTransferable();
		try
		{
			si.ci.server.createEtalon(si.accessIdentity, ts_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saveEtalon: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void attachEtalonToME(String e_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		try
		{
			si.ci.server.attachEtalonToME(si.accessIdentity, e_id, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error attachEtalonToME: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public String[] getEtalonsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		String ids[];
		ClientEtalon_Transferable tss[];
		Etalon ts;

		try
		{
			tss = si.ci.server.getEtalonsByME(si.accessIdentity, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getEtalonsByME: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = tss.length;
		ids = new String[count];
		System.out.println("...Done! " + count + " Etalon(s) fetched");
	    for (i = 0; i < count; i++)
		{
			ts = new Etalon(tss[i]);
			Pool.put(Etalon.typ, ts.getId(), ts);
			ts.updateLocalFromTransferable();
			ids[i] = ts.getId();
	    }
		return ids;
	}

	public void LoadTestArgumentSets(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ClientTestArgumentSet_Transferable tas_t;
		TestArgumentSet tas;
		
	    for (int i = 0; i < ids.length; i++)
		{
			try
			{
				tas_t = si.ci.server.getTestArgumentSet(si.accessIdentity, ids[i]);
				tas = new TestArgumentSet(tas_t);
				Pool.put(TestArgumentSet.typ, tas.getId(), tas);
			}
			catch (Exception ex)
			{
				System.err.print("Error getting TestArgumentSet: " + ex.getMessage());
				ex.printStackTrace();
				return;
			}
		}
	}
	
	public void saveTestArgumentSet(String as_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		TestArgumentSet as = (TestArgumentSet )Pool.get(TestArgumentSet.typ, as_id);
		as.setTransferableFromLocal();
		ClientTestArgumentSet_Transferable as_t = (ClientTestArgumentSet_Transferable )as.getTransferable();
		try
		{
			si.ci.server.createTestArgumentSet(si.accessIdentity, as_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saveTestArgumentSet: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void attachTestArgumentSetToME(String as_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		try
		{
			si.ci.server.attachTestArgumentSetToME(si.accessIdentity, as_id, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error attachTestArgumentSetToME: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public String[] getTestArgumentSetsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		String ids[];
		ClientTestArgumentSet_Transferable ass[];
		TestArgumentSet as;

		try
		{
			ass = si.ci.server.getTestArgumentSetsByME(si.accessIdentity, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getTestArgumentSetsByME: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = ass.length;
		ids = new String[count];
		System.out.println("...Done! " + count + " TestArgument(s) fetched");
	    for (i = 0; i < count; i++)
		{
			as = new TestArgumentSet(ass[i]);
			Pool.put(TestArgumentSet.typ, as.getId(), as);
			as.updateLocalFromTransferable();
			ids[i] = as.getId();
	    }
		return ids;
	}

	public void saveTestSetup(String ts_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		TestSetup ts = (TestSetup )Pool.get(TestSetup.typ, ts_id);
		ts.setTransferableFromLocal();
		TestSetup_Transferable ts_t = (TestSetup_Transferable )ts.getTransferable();
		try
		{
			si.ci.server.createTestSetup(si.accessIdentity, ts_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saveTestSetup: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public String[] getTestSetupsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		String ids[];
		TestSetup_Transferable tss[];
		TestSetup ts;

		try
		{
			tss = si.ci.server.getTestSetupsByME(si.accessIdentity, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getTestSetupsByME: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = tss.length;
		ids = new String[count];
		System.out.println("...Done! " + count + " TestSetup(s) fetched");
	    for (i = 0; i < count; i++)
		{
			ts = new TestSetup(tss[i]);
			Pool.put(TestSetup.typ, ts.getId(), ts);
			ts.updateLocalFromTransferable();
			ids[i] = ts.getId();
	    }
		return ids;
	}

	public String[] getTestSetupsByTestType(String test_type_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int count;
		String ids[];
		TestSetup_Transferable tss[];
		TestSetup ts;

		try
		{
			tss = si.ci.server.getTestSetupsByTestType(si.accessIdentity, test_type_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getTestSetupsByTestType: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}

		count = tss.length;
		ids = new String[count];
		System.out.println("...Done! " + count + " TestSetup(s) fetched");
	    for (i = 0; i < count; i++)
		{
			ts = new TestSetup(tss[i]);
			Pool.put(TestSetup.typ, ts.getId(), ts);
			ts.updateLocalFromTransferable();
			ids[i] = ts.getId();
	    }
		return ids;
	}

	public String[] getTestSetupIdsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		String ids[];
		try
		{
			ids = si.ci.server.getTestSetupIdsByME(si.accessIdentity, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getTestSetupsByME: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}
		return ids;
	}

	public String[] getTestSetupIdsByTestType(String test_type_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		String ids[];
		try
		{
			ids = si.ci.server.getTestSetupIdsByTestType(si.accessIdentity, test_type_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting getTestSetupsByTestType: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}
		return ids;
	}

	public void attachTestSetupToME(String ts_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		try
		{
			si.ci.server.attachTestSetupToME(si.accessIdentity, ts_id, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error attachTestSetupToME: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void detachTestSetupFromME(String ts_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;


		try
		{
			si.ci.server.detachTestSetupFromME(si.accessIdentity, ts_id, me_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error detachTestSetupToME: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void loadTestSetup(String ts_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		TestSetup_Transferable ts_t;
		TestSetup ts;

		try
		{
			ts_t = si.ci.server.getTestSetup(si.accessIdentity, ts_id);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting TestSetup: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		ts = new TestSetup(ts_t);
		Pool.put(TestSetup.typ, ts.getId(), ts);
		ts.updateLocalFromTransferable();
	}

}

