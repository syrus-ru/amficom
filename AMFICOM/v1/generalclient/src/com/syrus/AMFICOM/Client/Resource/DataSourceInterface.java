package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.General.SessionInterface;

import java.util.Vector;

public interface DataSourceInterface
{
	public void setSession(SessionInterface si);
	public SessionInterface getSession();

	public boolean ChangePassword(String oldpwd, String newpwd);
	
	public void LoadMaps();
	public void LoadMaps(Vector ids);
	public void LoadJMaps();
	public void LoadMap(String map_id);
	public void LoadJMap(String map_id);
	public void LoadMapDescriptors();
	public void LoadJMapDescriptors();
	public void SaveMap(String mc_id);
	public void SaveJMap(String mc_id);
	public void SaveMaps();
	public void RemoveMap(String mc_id);
	public void RemoveFromMap(String mc_id);
	public void RemoveJMap(String mc_id);
	public void RemoveFromJMap(String mc_id);

	public void LoadMapProtoElements();
	public void LoadMapProtoElements(Vector gids, Vector eids, Vector lids, Vector pids);
	public void SaveMapProtoElements(String[] ids);
	public void RemoveMapProtoElements(String[] ids);
	public void SaveMapProtoGroups(String[] ids);
	public void RemoveMapProtoGroups(String[] ids);

	public void LoadAttributeTypes();
	public void LoadAttributeTypes(String[] ids);

	public void LoadSchemes();
	public void LoadSchemes(Vector ids);
	public void SaveScheme(String mc_id);
	public void RemoveScheme(String mc_id);
	public void RemoveFromScheme(String mc_id);

	public void LoadSchemeProto();
	public void LoadSchemeProto(Vector ids);
	public void SaveSchemeProtos(String[] ids);
	public void RemoveSchemeProtos(String[] ids);

	public void ReloadAttributes(String mc_id);
	public void ReloadJAttributes(String mc_id);

	public String GetUId(String type);
	public void LoadImages(String[] id);
	public void GetAlarmTypes();
	public void GetAlarms();
	public void GetAlarms(String[] ids);
	public void GetAlarms(String[] ids, String filter_id);
	public void GetMessages();
	public void LoadMaintenanceData();
	public void SaveMaintenanceData(String []am_id, String []amu_id);
	public void RemoveMaintenanceData(String amu_id);

	public void SetUserAlarm(String source_id, String descriptor);

	public void SetAlarm(String alarm_id);
	public void DeleteAlarm(String alarm_id);
	public ResourceDescriptor_Transferable[] GetAlarmsForME(String me_id);

	public void GetResults();
	public void GetResults(String[] ids);
	public void GetTests();
	public void GetTests(String[] ids);
	public void GetRequests();
	public void GetAnalysis();
	public void GetAnalysis(String[] ids);
	public void GetModelings();
	public void GetModelings(String[] ids);
	public void GetEvaluations();
	public void GetEvaluations(String[] ids);

	public ResourceDescriptor_Transferable[] GetTestsForME(String me_id);
	public ResourceDescriptor_Transferable[] GetAnalysisForME(String me_id);
	public ResourceDescriptor_Transferable[] GetModelingsForSP(String sp_id);
	public ResourceDescriptor_Transferable[] GetEvaluationsForME(String me_id);

	public ResourceDescriptor_Transferable GetTestIdForEvaluation(String evaluation_id);
	public ResourceDescriptor_Transferable GetTestIdForAnalysis(String analysis_id);
	public ResourceDescriptor_Transferable GetEvaluationIdForTest(String test_id);
	public ResourceDescriptor_Transferable GetAnalysisIdForTest(String test_id);

	public void GetAnalysis(String id);
	public void GetEvaluation(String id);
	public void GetModeling(String id);

	public String GetTestForAnalysis(String id);
	public String GetTestForEvaluation(String id);

	public void GetRequestTests(String request_id);

	public void GetResult(String result_id);
	public String GetLastResult(String path_id);
	public String GetTestResult(String test_id);
	public String GetAnalysisResult(String analysis_id);
	public String GetModelingResult(String modeling_id);
	public String GetEvaluationResult(String evaluation_id);

	public ResourceDescriptor_Transferable GetLastResultId(String path_id);
	public ResourceDescriptor_Transferable[] GetTestResultIds(String test_id);
	public ResourceDescriptor_Transferable[] GetAnalysisResultIds(String analysis_id);
	public ResourceDescriptor_Transferable GetModelingResultId(String modeling_id);
	public ResourceDescriptor_Transferable[] GetEvaluationResultIds(String evaluation_id);

	public String[] GetAnalysisResultsForStatistics(
			String monitored_element_id,
			long from,
			long to);

	public String[] GetAnalysisResultsForStatistics(
			String monitored_element_id,
			long from,
			long to,
			String test_setup_id);

	public void RequestTest(String request_id, String test_id);
	public void RequestTest(String request_id, String[] test_ids);

	public void RemoveTests(String[] test_ids);
	public void UpdateTests(String[] test_ids);

	public ResourceDescriptor_Transferable[] getAlarmedTests();

	public void SaveAnalysis(String analysis_id, String result_id);
	public void SaveModeling(String modeling_id, String result_id);
	public void SaveEvaluation(String evaluation_id, String result_id);

	public void createAnalysis(String analysis_id);
	public void createEvaluation(String evaluation_id);

	public void LoadResultSets();
	public void LoadResultSets(String[] ids);
	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id);
	public ResourceDescriptor_Transferable[] LoadResultSetResultIds(String result_set_id, String me_id);

	public void LoadKISDescriptors();
	public void LoadNet();
	public void LoadISM();
	public void LoadNet(
			Vector p_ids, 
			Vector cp_ids,
			Vector eq_ids,
			Vector l_ids,
			Vector cl_ids);
	public void LoadISM(
		Vector k_ids,
		Vector ap_ids,
		Vector me_ids,
		Vector t_ids);

	public void SaveNet();
	public void SaveISM();

	public void LoadParameterTypes();
	public void LoadParameterTypes(String[] ids);

	public void LoadTestTypes();
	public void LoadAnalysisTypes();
	public void LoadEvaluationTypes();
	public void LoadModelingTypes();

	public void LoadTestTypes(String[] ids);
	public void LoadAnalysisTypes(String[] ids);
	public void LoadEvaluationTypes(String[] ids);
	public void LoadModelingTypes(String[] ids);

	public void LoadCriteriaSets();
	public void LoadThresholdSets();
	public void LoadEtalons();

	public void LoadCriteriaSets(String[] ids);
	public void LoadThresholdSets(String[] ids);
	public void LoadEtalons(String[] ids);
	public void LoadTestArgumentSets(String[] ids);

	public void saveCriteriaSet(String cs_id);
	public void saveThresholdSet(String ts_id);
	public void saveEtalon(String e_id);
	public String[] getCriteriaSetsByME(String me_id);
	public String[] getThresholdSetsByME(String me_id);
	public String[] getEtalonsByME(String me_id);
	public void attachCriteriaSetToME(String cs_id, String me_id);
	public void attachThresholdSetToME(String ths_id, String me_id);
	public void attachEtalonToME(String e_id, String me_id);

	public String getEtalonByMEAndTime(String me_id, long time);

	public void saveTestArgumentSet(String as_id);
	public String[] getTestArgumentSetsByME(String me_id);
	public void attachTestArgumentSetToME(String as_id, String me_id);

	public void saveTestSetup(String ts_id);
	public String[] getTestSetupsByME(String me_id);
	public String[] getTestSetupsByTestType(String test_type_id);
	public void attachTestSetupToME(String ts_id, String me_id);
	public void detachTestSetupFromME(String ts_id, String me_id);
	public void loadTestSetup(String ts_id);

	public String[] getTestSetupIdsByME(String me_id);
	public String[] getTestSetupIdsByTestType(String test_type_id);

	public void LoadNetDirectory();
	public void LoadISMDirectory();
	public void LoadNetDirectory(
			Vector pt_ids, 
			Vector eqt_ids,
			Vector lt_ids,
			Vector cht_ids,
			Vector cpt_ids,
			Vector clt_ids);
	public void LoadISMDirectory(
		Vector kt_ids,
		Vector apt_ids,
		Vector pt_ids);

	public void SaveEquipmentTypes(String[] ids);
	public void SavePortTypes(String[] ids);
	public void SaveCablePortTypes(String[] ids);
	public void SaveLinkTypes(String[] ids);
	public void SaveCableLinkTypes(String[] ids);
	
//	public void ();
	public void SaveEquipment(String equipment_id);
	public void SavePort(String port_id);
	public void SaveCablePort(String cport_id);
	public void SaveLink(String link_id);
	public void SaveCableLink(String clink_id);
	public void SavePath(String path_id);

	public void SaveKIS(String kis_id);
	public void SaveAccessPort(String port_id);

	public void RemoveEquipments(String[] equipment_ids);
	public void RemovePorts(String[] port_ids);
	public void RemoveCablePorts(String[] cport_ids);
	public void RemoveLinks(String[] link_ids);
	public void RemoveCableLinks(String[] clink_ids);
	public void RemovePaths(String[] path_ids);

	public void RemoveKISs(String[] kis_ids);
	public void RemoveAccessPorts(String[] port_ids);

	public void LoadUserDescriptors();
	public String[] GetLoggedUserIds();
	public void GetObjects();
	public void GetObjects(String[] cat_ids, String[] grp_ids, String[] prof_ids);
	public void LoadExecs();
	public void SaveObjects();

	public void SaveDomain(String domain_id);
	public void SaveCategory(String cat_id);
	public void SaveUser(String user_id);
	public void SaveGroup(String group_id);
	public void SaveOperatorProfile(String operator_profile_id);
	public void SaveExec(String exec_id);

	public void RemoveDomain(String[] domain_ids);
	public void RemoveUser(String[] user_ids);
	public void RemoveGroup(String[] group_ids);
	public void RemoveOperatorProfile(String[] operator_profile_ids);
	public void RemoveExec(String[] exec_ids);

	public void GetAdminObjects();
	public void GetAdminObjects(String[] ser_ids, String[] cli_ids, String[] ag_ids);
	public void SaveServer(String server_id);
	public void SaveClient(String client_id);
	public void SaveAgent(String agent_id);

	public void RemoveServer(String[] server_ids);
	public void RemoveClient(String[] client_ids);
	public void RemoveAgent(String[] agent_ids);

	public void SaveReportTemplates(String[] report_ids);
	public void LoadReportTemplates(String[] report_ids);
	public void RemoveReportTemplates(String[] report_ids);

	public void SaveSchemeOptimizeInfo(String soi_id);
	public void LoadSchemeOptimizeInfo();
	public void RemoveSchemeOptimizeInfo(String soi_id);

	public void SaveSchemeMonitoringSolutions(String sol_id);
	public void LoadSchemeMonitoringSolutions();
	public void RemoveSchemeMonitoringSolution(String sol_id);
}
