/*
 * $Id: RISDDataSource.java,v 1.6 2004/10/19 13:45:51 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.io.Rewriter;
import java.util.Vector;
import org.omg.CORBA.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/10/19 13:45:51 $
 * @module generalclient_v1
 */
public class RISDDataSource 
		extends EmptyDataSource
		implements DataSourceInterface 
{
	RISDSessionInfo si;

	protected RISDDataSource()
	{
	}

	public RISDDataSource(SessionInterface si)
	{
		if(si instanceof RISDSessionInfo)
			this.si = (RISDSessionInfo)si;
	}

	public void setSession(SessionInterface si)
	{
		if(si instanceof RISDSessionInfo)
			this.si = (RISDSessionInfo)si;
	}

	public SessionInterface getSession()
	{
		return si;
	}
	
	public boolean ChangePassword(String oldpwd, String newpwd)
	{
		if(si == null)
			return false;
		if(!si.isOpened())
			return false;

		try
		{
			si.ci.getServer().ChangePassword(
					si.getAccessIdentity(), 
					Rewriter.write(oldpwd), 
					Rewriter.write(newpwd));
		}
		catch(Exception e)
		{
			System.out.println("Error changing password - " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String GetUId(String type)
	{
		try
		{
			StringHolder idholder = new StringHolder();
			si.ci.getServer().GetUId(si.getAccessIdentity(), type, idholder);
			return idholder.value;
		}
		catch(Exception e)
		{
			System.out.println("Error retreiving UId - " + e.getMessage());
			e.printStackTrace();
			return super.GetUId(type);
		}
	}

	public void LoadImages(String[] id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ImageResourceSeq_TransferableHolder imholder = new ImageResourceSeq_TransferableHolder();
		try
		{
			si.ci.getServer().GetImages(si.getAccessIdentity(), id, imholder);
		}
		catch(Exception e)
		{
			System.out.println("Error retreiving images - " + e.getMessage());
			e.printStackTrace();
		}

		for(int i = 0; i < imholder.value.length; i++)
		{
			ImageResource ir = new ImageResource(imholder.value[i]);
			ImageCatalogue.add(ir.getId(), ir);
		}
	}
	
	public void LoadMapProtoElements()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadMapProtoElements(Vector gids, Vector eids, Vector lids, Vector pids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveMapProtoElements(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveMapProtoElements(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void SaveMapProtoGroups(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveMapProtoGroups(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadSchemeProto()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadSchemeProto(Vector ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveSchemeProtos(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveSchemeProtos(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadSchemes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadSchemes(Vector ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveScheme(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveScheme(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveFromScheme(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadMapDescriptors()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadJMapDescriptors()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadMaps()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadMaps(Vector ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadJMaps()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadMap(String map_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadJMap(String map_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveMap(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveJMap(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveMaps()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadMapViews()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadMapViews(Vector mv_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadMapView(String mv_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveMapView(String mv_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveMapView(String mv_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveFromMapView(String mv_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadAttributeTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadAttributeTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void ReloadAttributes(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void ReloadJAttributes(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetObjects()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetObjects(String[] cat_ids, String[] grp_ids, String[] prof_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadExecs()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		CommandPermissionAttributesSeq_TransferableHolder eh = new CommandPermissionAttributesSeq_TransferableHolder();
		CommandPermissionAttributes_Transferable execs[];
		CommandPermissionAttributes exec;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.getServer().GetExecDescriptors(si.getAccessIdentity(), eh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting execs: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetExecDescriptors! status = " + ecode);
			return;
		}

		execs = eh.value;
		count = execs.length;
		System.out.println("...Done! " + count + " exec(s) fetched");

	    for (i = 0; i < count; i++)
		{
			exec = new CommandPermissionAttributes(execs[i]);
			Pool.put(CommandPermissionAttributes.typ,
				 exec.getId(), exec);
			loaded_objects.add(exec);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}
	
	public String[] GetLoggedUserIds()
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];

		int i;
		int ecode = 0;
		int count;

		WStringSeqHolder userids = new WStringSeqHolder();

		try
		{
			ecode = si.ci.getServer().GetLoggedUserIds(si.getAccessIdentity(), userids);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting logged users: " + ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}
		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetLoggedUserIds! status = " + ecode);
			return new String[0];
		}

		return userids.value;
	}

	public void SaveObjects()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveDomain(String domain_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveCategory(String cat_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveUser(String user_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveGroup(String group_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveOperatorProfile(String operator_profile_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveExec(String exec_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveDomain(String[] domain_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveUser(String[] user_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveGroup(String[] group_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveOperatorProfile(String[] operator_profile_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveExec(String[] exec_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadUserDescriptors()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;
		DomainSeq_TransferableHolder dh = new DomainSeq_TransferableHolder();
		Domain_Transferable domains[];
		Domain domain;
		UserSeq_TransferableHolder uh = new UserSeq_TransferableHolder();
		User_Transferable users[];
		User user;

		try
		{
			ecode = si.ci.getServer().GetUserDescriptors(si.getAccessIdentity(), ih, dh, uh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting user descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetUserDescriptors! status = " + ecode);
			return;
		}
		domains = dh.value;
		count = domains.length;
		System.out.println("...Done! " + count + " domain(s) fetched");
	    for (i = 0; i < count; i++)
		{
			domain = new Domain(domains[i]);
			Pool.put(Domain.typ, domain.getId(), domain);
	    }

		users = uh.value;
		count = users.length;
		System.out.println("...Done! " + count + " user(s) fetched");

	    for (i = 0; i < count; i++)
		{
			user = new User(users[i]);
			Pool.put(User.typ, user.getId(), user);
	    }
	}

	public void RemoveMap(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveFromMap(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveJMap(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveFromJMap(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadMaintenanceData()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void SaveMaintenanceData(String []am_id, String []amu_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveMaintenanceData(String amu_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetAlarmTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetAlarms()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetAlarms(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void GetAlarms(String[] ids, String filter_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void SetAlarm(String alarm_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void DeleteAlarm(String alarm_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SetUserAlarm(String source_id, String descriptor)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public ResourceDescriptor_Transferable[] GetAlarmsForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public void GetMessages()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetResults()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetResults(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void GetTests()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetTests(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void GetRequests()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetAnalysis()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetAnalysis(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetModelings()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetModelings(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetEvaluations()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetEvaluations(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public ResourceDescriptor_Transferable[] GetTestsForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public ResourceDescriptor_Transferable[] GetAnalysisForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public ResourceDescriptor_Transferable[] GetModelingsForSP(String sp_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public ResourceDescriptor_Transferable[] GetEvaluationsForME(String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public ResourceDescriptor_Transferable GetTestIdForEvaluation(String evaluation_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}

	public ResourceDescriptor_Transferable GetTestIdForAnalysis(String analysis_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}

	public ResourceDescriptor_Transferable GetEvaluationIdForTest(String test_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}

	public ResourceDescriptor_Transferable GetAnalysisIdForTest(String test_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}

	public void GetAnalysis(String id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void GetEvaluation(String id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void GetModeling(String id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public String GetTestForAnalysis(String id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";
		return "";
	}
	
	public String GetTestForEvaluation(String id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";
		return "";
	}
	
	public void GetRequestTests(String request_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RequestTest(String request_id, String test_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RequestTest(String request_id, String[] test_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public String GetLastResult(String path_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}

	public String GetTestResult(String test_id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";
		return "";
	}
	
	public String GetAnalysisResult(String analysis_id)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";
		return "";
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
		return new String[0];
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
		return new String[0];
	}

	public String GetModelingResult(String modeling_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}
	
	public String GetEvaluationResult(String evaluation_id)
	{
		return "";
	}

	public ResourceDescriptor_Transferable GetLastResultId(String path_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}

	public ResourceDescriptor_Transferable[] GetTestResultIds(String test_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public ResourceDescriptor_Transferable[] GetAnalysisResultIds(String analysis_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public ResourceDescriptor_Transferable GetModelingResultId(String modeling_id)
	{
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;
		return null;
	}

	public ResourceDescriptor_Transferable[] GetEvaluationResultIds(String evaluation_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public void RemoveTests(String[] test_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void UpdateTests(String[] test_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public ResourceDescriptor_Transferable[] getAlarmedTests()
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public void GetResult(String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveAnalysis(String analysis_id, String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveModeling(String modeling_id, String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveEvaluation(String evaluation_id, String result_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void createAnalysis(String analysis_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void createEvaluation(String evaluation_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadResultSets()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadResultSets(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id, String me_id)
	{
		if(si == null)
			return new ResourceDescriptor_Transferable[0];
		if(!si.isOpened())
			return new ResourceDescriptor_Transferable[0];
		return new ResourceDescriptor_Transferable[0];
	}

	public void LoadKISDescriptors()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadNet()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadISM()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadNet(
			Vector p_ids, 
			Vector cp_ids,
			Vector eq_ids,
			Vector l_ids,
			Vector cl_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadISM(
		Vector k_ids,
		Vector ap_ids,
		Vector me_ids,
		Vector t_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveNet()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveISM()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadParameterTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadParameterTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadTestTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadAnalysisTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadEvaluationTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadModelingTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadTestTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadAnalysisTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadEvaluationTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadModelingTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadCriteriaSets()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadThresholdSets()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadEtalons()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadTestArgumentSets(String[] ids)
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
	}

	public void LoadThresholdSets(String[] ids)
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
	}

	public void saveCriteriaSet(String cs_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void saveThresholdSet(String ts_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public String[] getCriteriaSetsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public String[] getThresholdSetsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public void attachCriteriaSetToME(String cs_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void attachThresholdSetToME(String ths_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void saveEtalon(String e_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public String[] getEtalonsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public String getEtalonByMEAndTime(String me_id, long time)
	{
		if(si == null)
			return "";
		if(!si.isOpened())
			return "";
		return "";
	}
	
	public void attachEtalonToME(String e_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void saveTestArgumentSet(String as_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public String[] getTestArgumentSetsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public void attachTestArgumentSetToME(String as_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void saveTestSetup(String ts_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public String[] getTestSetupsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public String[] getTestSetupsByTestType(String test_type_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public String[] getTestSetupIdsByME(String me_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public String[] getTestSetupIdsByTestType(String test_type_id)
	{
		if(si == null)
			return new String[0];
		if(!si.isOpened())
			return new String[0];
		return new String[0];
	}

	public void attachTestSetupToME(String ts_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void detachTestSetupFromME(String ts_id, String me_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void loadTestSetup(String ts_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadNetDirectory()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadISMDirectory()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadNetDirectory(
			Vector pt_ids, 
			Vector eqt_ids,
			Vector lt_ids,
			Vector cht_ids,
			Vector cpt_ids,
			Vector clt_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadISMDirectory(
		Vector kt_ids,
		Vector apt_ids,
		Vector pt_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveEquipmentTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SavePortTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveCablePortTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveLinkTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveCableLinkTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveEquipment(String equipment_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveKIS(String kis_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveLink(String link_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveCableLink(String clink_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SavePath(String path_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SavePort(String port_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveCablePort(String cport_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveAccessPort(String port_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveEquipments(String[] equipment_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemovePorts(String[] port_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveCablePorts(String[] cport_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveLinks(String[] link_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveCableLinks(String[] clink_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemovePaths(String[] path_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveKISs(String[] kis_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveAccessPorts(String[] port_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void GetAdminObjects()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void GetAdminObjects(String[] ser_ids, String[] cli_ids, String[] ag_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveServer(String server_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveClient(String client_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveAgent(String agent_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}


	public void RemoveServer(String[] server_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveClient(String[] client_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveAgent(String[] agent_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveReportTemplates(String[] report_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadReportTemplates(String[] report_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void RemoveReportTemplates(String[] report_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveSchemeOptimizeInfo(String soi_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadSchemeOptimizeInfo()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveSchemeOptimizeInfo(String soi_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveSchemeMonitoringSolutions(String sol_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void LoadSchemeMonitoringSolutions()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
	public void RemoveSchemeMonitoringSolution(String sol_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}
	
}
