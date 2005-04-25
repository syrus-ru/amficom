/*-
 * $Id: RISDDataSource.java,v 1.9 2005/04/25 09:59:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource;

import java.util.Vector;

import org.omg.CORBA.WStringSeqHolder;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.Admin.CommandPermissionAttributesSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Admin.CommandPermissionAttributes_Transferable;
import com.syrus.AMFICOM.CORBA.Admin.DomainSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Admin.Domain_Transferable;
import com.syrus.AMFICOM.CORBA.Admin.UserSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Admin.User_Transferable;
import com.syrus.AMFICOM.CORBA.Resource.ImageResourceSeq_TransferableHolder;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.Object.CommandPermissionAttributes;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.Object.User;
import com.syrus.io.Rewriter;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/04/25 09:59:18 $
 * @module generalclient_v1
 */
public class RISDDataSource implements DataSourceInterface {
	RISDSessionInfo si;

	public RISDDataSource(SessionInterface si) {
		if (si instanceof RISDSessionInfo)
			this.si = (RISDSessionInfo) si;
	}

	public boolean ChangePassword(String oldpwd, String newpwd) {
		if (si == null)
			return false;
		if (!si.isOpened())
			return false;

		try {
			si.ci.getServer().ChangePassword(
					si.getAccessIdentity(),
					Rewriter.write(oldpwd),
					Rewriter.write(newpwd));
		} catch (Exception e) {
			System.out.println("Error changing password - "
					+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void GetAdminObjects(String[] ser_ids, String[] cli_ids,
			String[] ag_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public String[] GetLoggedUserIds() {
		if (si == null)
			return new String[0];
		if (!si.isOpened())
			return new String[0];

		int i;
		int ecode = 0;
		int count;

		WStringSeqHolder userids = new WStringSeqHolder();

		try {
			ecode = si.ci.getServer().GetLoggedUserIds(
					si.getAccessIdentity(), userids);
		} catch (Exception ex) {
			System.err.print("Error getting logged users: "
					+ ex.getMessage());
			ex.printStackTrace();
			return new String[0];
		}
		if (ecode != Constants.ERROR_NO_ERROR) {
			System.out.println("Failed GetLoggedUserIds! status = "
					+ ecode);
			return new String[0];
		}

		return userids.value;
	}

	public void GetObjects(String[] cat_ids, String[] grp_ids,
			String[] prof_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public SessionInterface getSession() {
		return si;
	}

	public String GetUId(final String type) {
		return String.valueOf(System.currentTimeMillis());
	}

	public void LoadExecs() {
		if (si == null)
			return;
		if (!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		CommandPermissionAttributesSeq_TransferableHolder eh = new CommandPermissionAttributesSeq_TransferableHolder();
		CommandPermissionAttributes_Transferable execs[];
		CommandPermissionAttributes exec;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try {
			ecode = si.ci.getServer().GetExecDescriptors(
					si.getAccessIdentity(), eh);
		} catch (Exception ex) {
			System.err.print("Error getting execs: "
					+ ex.getMessage());
			ex.printStackTrace();
			return;
		}
		if (ecode != Constants.ERROR_NO_ERROR) {
			System.out
					.println("Failed GetExecDescriptors! status = "
							+ ecode);
			return;
		}

		execs = eh.value;
		count = execs.length;
		System.out.println("...Done! " + count + " exec(s) fetched");

		for (i = 0; i < count; i++) {
			exec = new CommandPermissionAttributes(execs[i]);
			Pool.put(CommandPermissionAttributes.typ, exec.getId(),
					exec);
			loaded_objects.add(exec);
		}

		// update loaded objects
		count = loaded_objects.size();
		for (i = 0; i < count; i++) {
			or = (ObjectResource) loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadReportTemplates(String[] report_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void LoadUserDescriptors() {
		if (si == null)
			return;
		if (!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		DomainSeq_TransferableHolder dh = new DomainSeq_TransferableHolder();
		Domain_Transferable domains[];
		Domain domain;
		UserSeq_TransferableHolder uh = new UserSeq_TransferableHolder();
		User_Transferable users[];
		User user;

		try {
			ecode = si.ci.getServer().GetUserDescriptors(
					si.getAccessIdentity(), ih, dh, uh);
		} catch (Exception ex) {
			System.err.print("Error getting user descriptors: "
					+ ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR) {
			System.out
					.println("Failed GetUserDescriptors! status = "
							+ ecode);
			return;
		}
		domains = dh.value;
		count = domains.length;
		System.out.println("...Done! " + count + " domain(s) fetched");
		for (i = 0; i < count; i++) {
			domain = new Domain(domains[i]);
			Pool.put(Domain.typ, domain.getId(), domain);
		}

		users = uh.value;
		count = users.length;
		System.out.println("...Done! " + count + " user(s) fetched");

		for (i = 0; i < count; i++) {
			user = new User(users[i]);
			Pool.put(User.typ, user.getId(), user);
		}
	}

	public void RemoveAgent(String[] agent_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void RemoveClient(String[] client_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void RemoveDomain(String[] domain_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void RemoveGroup(String[] group_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void RemoveOperatorProfile(String[] operator_profile_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void RemoveServer(String[] server_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void RemoveUser(String[] user_ids) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveAgent(String agent_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveCategory(String cat_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveClient(String client_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveDomain(String domain_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveExec(String exec_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveGroup(String group_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveOperatorProfile(String operator_profile_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveServer(String server_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}

	public void SaveUser(String user_id) {
		if (si == null)
			return;
		if (!si.isOpened())
			return;
	}
}
