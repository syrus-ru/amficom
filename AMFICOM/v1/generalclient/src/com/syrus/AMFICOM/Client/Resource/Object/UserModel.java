package com.syrus.AMFICOM.Client.Resource.Object;

import java.text.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class UserModel extends ObjectResourceModel
{
  public User user;

  public UserModel(User user)
  {
    this.user = user;
  }

  public Enumeration getChildren(String key)
  {
    return new Vector().elements();
  }

  public Enumeration getChildTypes(String key)
  {
    return new Vector().elements();
  }

  public Class getChildClass(String type)
  {
    return ObjectResource.class;
  }


  public  PropertiesPanel getPropertyPane()
  {
	  return new UserPane(user);
  }

  public String getColumnValue(String col_id)
  {
	  SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	  String s = "";
	  try
	  {
//      if(col_id.equals("id"))
//        s = user.id;
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