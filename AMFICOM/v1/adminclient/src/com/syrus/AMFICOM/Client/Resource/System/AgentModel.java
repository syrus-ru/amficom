package com.syrus.AMFICOM.Client.Resource.System;

import java.text.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;



public class AgentModel extends ObjectResourceModel
{
  Agent agent;

  public AgentModel(Agent agent)
  {
    this.agent = agent;
  }


  public PropertiesPanel getPropertyPane()
  {
    return new AgentPane(agent);
  }

  public String getColumnValue(String col_id)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    String s = "";
    try
    {
      if(col_id.equals("id"))
        s = agent.id;
      if(col_id.equals("name"))
        s = agent.name;
      if(col_id.equals("created"))
        s = sdf.format(new Date(agent.created));
      if(col_id.equals("modified"))
        s = sdf.format(new Date(agent.modified));
    }
    catch(Exception e)
    {
//      System.out.println("error gettin field value - Domain");
      s = "";
    }
    if(s == null)
      s = "";

    return s;
  }

  public Enumeration getChildren(String key)
  {
    return new Vector().elements();
  }

  public Enumeration getChildTypes()
  {
    return new Vector().elements();
  }

  public Class getChildClass(String type)
  {
    return ObjectResource.class;
  }
}