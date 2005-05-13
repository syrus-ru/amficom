/*
 * $Id: UserModel.java,v 1.5 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.UserPane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/13 19:05:47 $
 * @module generalclient_v1
 */
public class UserModel extends ObjectResourceModel
{
  public User user;

  public UserModel(User user)
  {
    this.user = user;
  }

  public Enumeration getChildTypes(String key)
  {
    return new Vector().elements();
  }

  public Class getChildClass(String type)
  {
    return StorableObject.class;
  }

	public  ObjectResourcePropertiesPane getPropertyPane() {
		UserPane userPane = UserPane.getInstance();
		userPane.setObjectResource(user);
		return userPane;
	}

  public String getColumnValue(String col_id)
  {
	  SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	  String s = "";
	  try
	  {
//      if(col_id.equals("id"))
//        s = user.getId();
      if(col_id.equals("name"))
        s = user.login;
      if(col_id.equals("sessions"))
        s = String.valueOf(user.sessions);
      if(col_id.equals("last_login"))
        s = sdf.format(new Date(user.last_login));
	  }
	  catch(Exception e)
	  {
//		  System.out.println("error gettin field value - User");
		  s = "";
	  }
	  if(s == null)
		  s = "";

	  return s;
	}
}
