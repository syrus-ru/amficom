/*
 * $Id: AgentPane.java,v 1.3 2004/09/27 13:22:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.User;
import com.syrus.AMFICOM.Client.Resource.System.Agent;
import java.awt.*;
import java.util.Date;
import javax.swing.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 13:22:58 $
 * @module generalclient_v1
 */
public final class AgentPane extends JPanel implements ObjectResourcePropertiesPane {
	private static AgentPane instance = null;

  Agent agent;

  ApplicationContext aContext = new ApplicationContext();
  BorderLayout borderLayout1 = new BorderLayout();
  User user;

  Checker checker;

  AgentPanel agentPanel = new AgentPanel();

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public AgentPane() {
		jbInit();
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public AgentPane(ObjectResource or) {
		this();
		setObjectResource(or);
	}

	private void jbInit() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(borderLayout1);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(agentPanel, BorderLayout.CENTER);
	}

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    String userId = aContext.getSessionInterface().getUserId();
    user = (User)Pool.get(User.typ, userId);
    checker = new Checker(user);
  }


  public void setObjectResource(ObjectResource or)
  {
    if(!checker.checkCommand(Checker.readAgentInfo))
      return;

    if(or == null)
      return;
    agent = (Agent)or;
    agentPanel.setObjectResource(agent);
  }


  public ObjectResource getObjectResource()
  {
    return agent;
  }


  public boolean save()
  {
    if(!checker.checkCommand(Checker.modifyAgentInfo))
      return false;

    agent = (Agent)agentPanel.getModifiedObjectResource();
    Date d = new Date();
    agent.modified = d.getTime();

    if(!NewUpDater.checkName(agent))
      return false;

   Pool.put(Agent.typ, agent.id, agent);
    aContext.getDataSource().SaveAgent(agent.id);
    return true;
  }

  public boolean create()
  {
    if(true)
    {
      String error = "Агент не может быть создан.";
      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
      return false;
    }

    if(!checker.checkCommand(Checker.createAgent))
      return false;
    String id = aContext.getDataSource().GetUId(Agent.typ);

    agent = new Agent();
    agent.id = id;
    Date d = new Date();
    agent.created = d.getTime();
    agent.modified = d.getTime();

    Pool.put(Agent.typ, agent.id, agent);

    setObjectResource(agent);

    save();
    return true;
  }


  public boolean open()
  {
    return true;
  }


  public boolean delete()
  {
    if(true)
    {
      String error = "Агент не может быть удален.";
      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
      return false;
    }


    if(!checker.checkCommand(Checker.deleteAgent))
      return false;

    String []s = new String[1];

    s[0] = agent.id;
    aContext.getDataSource().RemoveAgent(s);
    Pool.remove(Agent.typ, agent.id);

    return true;
  }

  public boolean modify()
  {
    return  save();
  }

	public boolean cancel() {
		return false;
	}

	public static AgentPane getInstance() {
		if (instance == null)
			synchronized (AgentPane.class) {
				if (instance == null)
					instance = new AgentPane();
			}
		return instance;
	}
}
