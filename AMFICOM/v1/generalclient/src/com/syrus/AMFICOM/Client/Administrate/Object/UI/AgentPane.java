/*
 * $Id: AgentPane.java,v 1.2 2004/08/17 15:02:50 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;
import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2004/08/17 15:02:50 $
 * @module generalclient_v1
 */
public class AgentPane extends PropertiesPanel
{
  Agent agent;

  ApplicationContext aContext = new ApplicationContext();
  BorderLayout borderLayout1 = new BorderLayout();
  User user;

  Checker checker;

  AgentPanel agentPanel = new AgentPanel();

  public AgentPane()
  {
    try
    {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public AgentPane(ObjectResource or)
  {
    this();
    setObjectResource(or);
  }

  void jbInit() throws Exception
  {
    this.setPreferredSize(new Dimension(500, 500));
    this.setLayout(borderLayout1);

    this.setBorder(BorderFactory.createRaisedBevelBorder());

    this.add(agentPanel, BorderLayout.CENTER);
//    this.add(pac, BorderLayout.SOUTH);
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
    aContext.getDataSourceInterface().SaveAgent(agent.id);
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
    String id = aContext.getDataSourceInterface().GetUId(Agent.typ);

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
    aContext.getDataSourceInterface().RemoveAgent(s);
    Pool.remove(Agent.typ, agent.id);

    return true;
  }

  public boolean modify()
  {
    return  save();
  }
}
