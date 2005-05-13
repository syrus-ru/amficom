/*
 * $Id: ServerModel.java,v 1.4 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.Resource.System;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.ServerPane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.general.StorableObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/13 19:05:47 $
 * @module generalclient_v1
 */
public class ServerModel extends ObjectResourceModel
{
  Server server;

  public ServerModel(Server server)
  {
    this.server = server;
  }

	public ObjectResourcePropertiesPane getPropertyPane() {
		ServerPane serverPane = ServerPane.getInstance();
		serverPane.setObjectResource(server);
		return serverPane;
	}

  public String getColumnValue(String col_id)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    String s = "";
    try
    {
      if(col_id.equals("id"))
        s = server.getId();
      if(col_id.equals("name"))
        s = server.name;
      if(col_id.equals("created"))
        s = sdf.format(new Date(server.created));
      if(col_id.equals("modified"))
        s = sdf.format(new Date(server.modified));
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

  public Enumeration getChildTypes()
  {
    return new Vector().elements();
  }

  public Class getChildClass(String type)
  {
    return StorableObject.class;
  }
}
