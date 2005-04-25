package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.SessionInterface;

public interface DataSourceInterface {
	public boolean ChangePassword(String oldpwd, String newpwd);

	public void GetAdminObjects(String[] ser_ids, String[] cli_ids,
			String[] ag_ids);

	public String[] GetLoggedUserIds();

	public void GetObjects(String[] cat_ids, String[] grp_ids,
			String[] prof_ids);

	public SessionInterface getSession();

	public String GetUId(String type);

	public void LoadExecs();

	public void LoadReportTemplates(String[] report_ids);

	public void LoadUserDescriptors();

	public void RemoveAgent(String[] agent_ids);

	public void RemoveClient(String[] client_ids);

	public void RemoveDomain(String[] domain_ids);

	public void RemoveGroup(String[] group_ids);

	public void RemoveOperatorProfile(String[] operator_profile_ids);

	public void RemoveServer(String[] server_ids);

	public void RemoveUser(String[] user_ids);

	public void SaveAgent(String agent_id);

	public void SaveCategory(String cat_id);

	public void SaveClient(String client_id);

	public void SaveDomain(String domain_id);

	public void SaveExec(String exec_id);

	public void SaveGroup(String group_id);

	public void SaveOperatorProfile(String operator_profile_id);

	public void SaveServer(String server_id);

	public void SaveUser(String user_id);
}
