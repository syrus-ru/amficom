/*
 * $Id: RISDObjectDataSource.java,v 1.3 2004/10/19 13:45:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;
import java.util.Vector;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/10/19 13:45:52 $
 * @module admin_v1
 */
public class RISDObjectDataSource
		extends RISDReportDataSource
		implements DataSourceInterface
{
	protected RISDObjectDataSource()
	{
		super();
	}

	public RISDObjectDataSource(SessionInterface si)
	{
		super(si);
	}

	public void GetObjects()
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
		OperatorCategorySeq_TransferableHolder och = new OperatorCategorySeq_TransferableHolder();
		OperatorCategory_Transferable categories[];
		OperatorCategory category;
		OperatorGroupSeq_TransferableHolder ogh = new OperatorGroupSeq_TransferableHolder();
		OperatorGroup_Transferable groups[];
		OperatorGroup group;
		OperatorProfileSeq_TransferableHolder oprh = new OperatorProfileSeq_TransferableHolder();
		OperatorProfile_Transferable profiles[];
		OperatorProfile profile;
		CommandPermissionAttributesSeq_TransferableHolder eh = new CommandPermissionAttributesSeq_TransferableHolder();
		CommandPermissionAttributes_Transferable execs[];
		CommandPermissionAttributes exec;
		UserSeq_TransferableHolder uh = new UserSeq_TransferableHolder();
		User_Transferable users[];
		User user;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.getServer().GetObjects(si.getAccessIdentity(), ih, dh, och, ogh, oprh, eh, uh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting objects: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetObjects! status = " + ecode);
			return;
		}
		domains = dh.value;
		count = domains.length;
		System.out.println("...Done! " + count + " domain(s) fetched");
	    for (i = 0; i < count; i++)
		{
			domain = new Domain(domains[i]);
			Pool.put(Domain.typ, domain.getId(), domain);
			loaded_objects.add(domain);
	    }

		categories = och.value;
		count = categories.length;
		System.out.println("...Done! " + count + " category(ies) fetched");
	    for (i = 0; i < count; i++)
		{
			category = new OperatorCategory(categories[i]);
			Pool.put(OperatorCategory.typ, category.getId(), category);
			loaded_objects.add(category);
	    }

		groups = ogh.value;
		count = groups.length;
		System.out.println("...Done! " + count + " group(s) fetched");
	    for (i = 0; i < count; i++)
		{
			group = new OperatorGroup(groups[i]);
			Pool.put(OperatorGroup.typ, group.getId(), group);
			loaded_objects.add(group);
	    }
		profiles = oprh.value;
		count = profiles.length;
		System.out.println("...Done! " + count + " profile(s) fetched");
	    for (i = 0; i < count; i++)
		{
			profile = new OperatorProfile(profiles[i]);
			Pool.put(OperatorProfile.typ, profile.getId(), profile);
			loaded_objects.add(profile);
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

		users = uh.value;
		count = users.length;
		System.out.println("...Done! " + count + " user(s) fetched");

	    for (i = 0; i < count; i++)
		{
			user = new User(users[i]);
			Pool.put(User.typ, user.getId(), user);
			loaded_objects.add(user);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void GetObjects(String[] cat_ids, String[] grp_ids, String[] prof_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		OperatorCategorySeq_TransferableHolder och = new OperatorCategorySeq_TransferableHolder();
		OperatorCategory_Transferable categories[];
		OperatorCategory category;
		OperatorGroupSeq_TransferableHolder ogh = new OperatorGroupSeq_TransferableHolder();
		OperatorGroup_Transferable groups[];
		OperatorGroup group;
		OperatorProfileSeq_TransferableHolder oprh = new OperatorProfileSeq_TransferableHolder();
		OperatorProfile_Transferable profiles[];
		OperatorProfile profile;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.getServer().GetStatedObjects(si.getAccessIdentity(), cat_ids, grp_ids, prof_ids, och, ogh, oprh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting objects: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetObjects! status = " + ecode);
			return;
		}

		categories = och.value;
		count = categories.length;
		System.out.println("...Done! " + count + " category(ies) fetched");
	    for (i = 0; i < count; i++)
		{
			category = new OperatorCategory(categories[i]);
			Pool.put(OperatorCategory.typ, category.getId(), category);
			loaded_objects.add(category);
	    }

		groups = ogh.value;
		count = groups.length;
		System.out.println("...Done! " + count + " group(s) fetched");
	    for (i = 0; i < count; i++)
		{
			group = new OperatorGroup(groups[i]);
			Pool.put(OperatorGroup.typ, group.getId(), group);
			loaded_objects.add(group);
	    }

		profiles = oprh.value;
		count = profiles.length;
		System.out.println("...Done! " + count + " profile(s) fetched");
	    for (i = 0; i < count; i++)
		{
			profile = new OperatorProfile(profiles[i]);
			Pool.put(OperatorProfile.typ, profile.getId(), profile);
			loaded_objects.add(profile);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
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

		int ecode = 0;
		Domain domain = (Domain )Pool.get(Domain.typ, domain_id);
		domain.setTransferableFromLocal();

		ImageResource_Transferable images[] = new ImageResource_Transferable[0];
		Domain_Transferable domain_t[] = new Domain_Transferable[1];
		domain_t[0] = (Domain_Transferable )domain.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveObjects(
					si.getAccessIdentity(),
					images,
					domain_t,
					new OperatorCategory_Transferable[0],
					new OperatorGroup_Transferable[0],
					new OperatorProfile_Transferable[0],
					new CommandPermissionAttributes_Transferable[0],
					new User_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Domain: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveDomain! status = " + ecode);
			return;
		}
	}

	public void SaveCategory(String cat_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		OperatorCategory cat = (OperatorCategory )Pool.get(OperatorCategory.typ, cat_id);
		cat.setTransferableFromLocal();

		ImageResource_Transferable images[] = new ImageResource_Transferable[0];
		OperatorCategory_Transferable cat_t[] = new OperatorCategory_Transferable[1];
		cat_t[0] = (OperatorCategory_Transferable )cat.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveObjects(
					si.getAccessIdentity(),
					images,
					new Domain_Transferable[0],
					cat_t,
					new OperatorGroup_Transferable[0],
					new OperatorProfile_Transferable[0],
					new CommandPermissionAttributes_Transferable[0],
					new User_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving category: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveCategory! status = " + ecode);
			return;
		}
	}

	public void SaveUser(String user_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		User user = (User )Pool.get(User.typ, user_id);
		if(user == null)
                System.out.println("The User is not set onto the pool!!!");
		user.setTransferableFromLocal();

		ImageResource_Transferable images[] = new ImageResource_Transferable[0];
		User_Transferable user_t[] = new User_Transferable[1];
		user_t[0] = (User_Transferable )user.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveObjects(
					si.getAccessIdentity(),
					images,
					new Domain_Transferable[0],
					new OperatorCategory_Transferable[0],
					new OperatorGroup_Transferable[0],
					new OperatorProfile_Transferable[0],
					new CommandPermissionAttributes_Transferable[0],
					user_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving user: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveUser! status = " + ecode);
			return;
		}
	}

	public void SaveGroup(String group_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		OperatorGroup group = (OperatorGroup )Pool.get(OperatorGroup.typ, group_id);
		group.setTransferableFromLocal();

		ImageResource_Transferable images[] = new ImageResource_Transferable[0];
		OperatorGroup_Transferable group_t[] = new OperatorGroup_Transferable[1];
		group_t[0] = (OperatorGroup_Transferable )group.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveObjects(
					si.getAccessIdentity(),
					images,
					new Domain_Transferable[0],
					new OperatorCategory_Transferable[0],
					group_t,
					new OperatorProfile_Transferable[0],
					new CommandPermissionAttributes_Transferable[0],
					new User_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Group: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveGroup! status = " + ecode);
			return;
		}
	}

	public void SaveOperatorProfile(String operator_profile_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
		int ecode = 0;
		OperatorProfile profile = (OperatorProfile )Pool.get(OperatorProfile.typ, operator_profile_id);
		profile.setTransferableFromLocal();

		ImageResource_Transferable images[] = new ImageResource_Transferable[0];
		OperatorProfile_Transferable profile_t[] = new OperatorProfile_Transferable[1];
		profile_t[0] = (OperatorProfile_Transferable )profile.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveObjects(
					si.getAccessIdentity(),
					images,
					new Domain_Transferable[0],
					new OperatorCategory_Transferable[0],
					new OperatorGroup_Transferable[0],
					profile_t,
					new CommandPermissionAttributes_Transferable[0],
					new User_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Profile: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveOperatorProfile! status = " + ecode);
			return;
		}
	}

	public void SaveExec(String exec_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		CommandPermissionAttributes exec = (CommandPermissionAttributes )Pool.get(CommandPermissionAttributes.typ, exec_id);
		exec.setTransferableFromLocal();

		ImageResource_Transferable images[] = new ImageResource_Transferable[0];
		CommandPermissionAttributes_Transferable exec_t[] = new CommandPermissionAttributes_Transferable[1];
		exec_t[0] = (CommandPermissionAttributes_Transferable )exec.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveObjects(
					si.getAccessIdentity(),
					images,
					new Domain_Transferable[0],
					new OperatorCategory_Transferable[0],
					new OperatorGroup_Transferable[0],
					new OperatorProfile_Transferable[0],
					exec_t,
					new User_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving exec: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveExec! status = " + ecode);
			return;
		}
	}

	public void RemoveDomain(String[] domain_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveObjects(
					si.getAccessIdentity(),
					domain_ids,
					new String[] {},
					new String[] {},
					new String[] {},
					new String[] {},
					new String[] {});
		}
		catch (Exception ex)
		{
			System.err.print("Error removing domain_idss: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Removedomain_idss! status = " + ecode);
			return;
		}
	}

	public void RemoveUser(String[] user_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveObjects(
					si.getAccessIdentity(),
					new String[] {},
					new String[] {},
					new String[] {},
					new String[] {},
					new String[] {},
					user_ids);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing user_idss: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Removeuser_idss! status = " + ecode);
			return;
		}
	}

	public void RemoveGroup(String[] group_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveObjects(
					si.getAccessIdentity(),
					new String[] {},
					new String[] {},
					group_ids,
					new String[] {},
					new String[] {},
					new String[] {});
		}
		catch (Exception ex)
		{
			System.err.print("Error removing group_idss: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Removegroup_idss! status = " + ecode);
			return;
		}
	}

	public void RemoveOperatorProfile(String[] operator_profile_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveObjects(
					si.getAccessIdentity(),
					new String[] {},
					new String[] {},
					new String[] {},
					operator_profile_ids,
					new String[] {},
					new String[] {});
		}
		catch (Exception ex)
		{
			System.err.print("Error removing operator_profile_idss: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Removeoperator_profile_idss! status = " + ecode);
			return;
		}
	}

	public void RemoveExec(String[] exec_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveObjects(
					si.getAccessIdentity(),
					new String[] {},
					new String[] {},
					new String[] {},
					new String[] {},
					exec_ids,
					new String[] {});
		}
		catch (Exception ex)
		{
			System.err.print("Error removing exec_idss: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Removeexec_idss! status = " + ecode);
			return;
		}
	}

	public void GetAdminObjects()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ServerSeq_TransferableHolder sh = new ServerSeq_TransferableHolder();
		Server_Transferable servers[];
		Server server;
		ClientSeq_TransferableHolder ch = new ClientSeq_TransferableHolder();
		Client_Transferable clients[];
		Client client;
		AgentSeq_TransferableHolder ah = new AgentSeq_TransferableHolder();
		Agent_Transferable agents[];
		Agent agent;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.getServer().GetAdminObjects(si.getAccessIdentity(), sh, ch, ah);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting admin objects: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetAdminObjects! status = " + ecode);
			return;
		}

		servers = sh.value;
		count = servers.length;
		System.out.println("...Done! " + count + " server(s) fetched");
	    for (i = 0; i < count; i++)
		{
			server = new Server(servers[i]);
			Pool.put(Server.typ, server.getId(), server);
			loaded_objects.add(server);
	    }

		clients = ch.value;
		count = clients.length;
		System.out.println("...Done! " + count + " client(s) fetched");
	    for (i = 0; i < count; i++)
		{
			client = new Client(clients[i]);
			Pool.put(Client.typ, client.getId(), client);
			loaded_objects.add(client);
	    }

		agents = ah.value;
		count = agents.length;
		System.out.println("...Done! " + count + " agent(s) fetched");
	    for (i = 0; i < count; i++)
		{
			agent = new Agent(agents[i]);
			Pool.put(Agent.typ, agent.getId(), agent);
			loaded_objects.add(agent);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void GetAdminObjects(String[] ser_ids, String[] cli_ids, String[] ag_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ServerSeq_TransferableHolder sh = new ServerSeq_TransferableHolder();
		Server_Transferable servers[];
		Server server;
		ClientSeq_TransferableHolder ch = new ClientSeq_TransferableHolder();
		Client_Transferable clients[];
		Client client;
		AgentSeq_TransferableHolder ah = new AgentSeq_TransferableHolder();
		Agent_Transferable agents[];
		Agent agent;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.getServer().GetStatedAdminObjects(si.getAccessIdentity(), ser_ids, cli_ids, ag_ids, sh, ch, ah);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting admin objects: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetAdminObjects! status = " + ecode);
			return;
		}

		servers = sh.value;
		count = servers.length;
		System.out.println("...Done! " + count + " server(s) fetched");
	    for (i = 0; i < count; i++)
		{
			server = new Server(servers[i]);
			Pool.put(Server.typ, server.getId(), server);
			loaded_objects.add(server);
	    }

		clients = ch.value;
		count = clients.length;
		System.out.println("...Done! " + count + " client(s) fetched");
	    for (i = 0; i < count; i++)
		{
			client = new Client(clients[i]);
			Pool.put(Client.typ, client.getId(), client);
			loaded_objects.add(client);
	    }

		agents = ah.value;
		count = agents.length;
		System.out.println("...Done! " + count + " agent(s) fetched");
	    for (i = 0; i < count; i++)
		{
			agent = new Agent(agents[i]);
			Pool.put(Agent.typ, agent.getId(), agent);
			loaded_objects.add(agent);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveServer(String server_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		Server server = (Server )Pool.get(Server.typ, server_id);
		if(server == null)
            System.out.println("The Server is not set onto the pool!!!");
		server.setTransferableFromLocal();

		Server_Transferable server_t[] = new Server_Transferable[1];
		server_t[0] = (Server_Transferable )server.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveAdminObjects(
					si.getAccessIdentity(),
					server_t,
					new Client_Transferable[0],
					new Agent_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving server: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Saveserver! status = " + ecode);
			return;
		}
	}

	public void SaveClient(String client_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		Client client = (Client )Pool.get(Client.typ, client_id);
		if(client == null)
            System.out.println("The Client is not set onto the pool!!!");
		client.setTransferableFromLocal();

		Client_Transferable client_t[] = new Client_Transferable[1];
		client_t[0] = (Client_Transferable )client.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveAdminObjects(
					si.getAccessIdentity(),
					new Server_Transferable[0],
					client_t,
					new Agent_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving client: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Saveclient! status = " + ecode);
			return;
		}
	}

	public void SaveAgent(String agent_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		Agent agent = (Agent )Pool.get(Agent.typ, agent_id);
		if(agent == null)
            System.out.println("The Agent is not set onto the pool!!!");
		agent.setTransferableFromLocal();

		Agent_Transferable agent_t[] = new Agent_Transferable[1];
		agent_t[0] = (Agent_Transferable )agent.getTransferable();

		try
		{
			ecode = si.ci.getServer().SaveAdminObjects(
					si.getAccessIdentity(),
					new Server_Transferable[0],
					new Client_Transferable[0],
					agent_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving agent: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Saveagent! status = " + ecode);
			return;
		}
	}


	public void RemoveServer(String[] server_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveAdminObjects(
					si.getAccessIdentity(),
					server_ids,
					new String[] {},
					new String[] {});
		}
		catch (Exception ex)
		{
			System.err.print("Error removing server: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Removeserver! status = " + ecode);
			return;
		}
	}

	public void RemoveClient(String[] client_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveAdminObjects(
					si.getAccessIdentity(),
					new String[] {},
					client_ids,
					new String[] {});
		}
		catch (Exception ex)
		{
			System.err.print("Error removing client: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveClient! status = " + ecode);
			return;
		}
	}

	public void RemoveAgent(String[] agent_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;

		try
		{
			ecode = si.ci.getServer().RemoveAdminObjects(
					si.getAccessIdentity(),
					new String[] {},
					new String[] {},
					agent_ids);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing agent: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Removeagent! status = " + ecode);
			return;
		}
	}

}