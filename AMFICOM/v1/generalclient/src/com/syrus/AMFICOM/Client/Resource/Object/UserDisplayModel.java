package com.syrus.AMFICOM.Client.Resource.Object;
import java.util.*;


import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;


public class UserDisplayModel extends StubDisplayModel
{

  public UserDisplayModel()
  {
  }
  public Vector getColumns()
  {
	  Vector vec = new Vector();
//	  vec.add("id");
	  vec.add("name");
	  vec.add("sessions");
	  vec.add("last_login");
		  return vec;
  }

  public String getColumnName(String col_id)
  {
	  String s = "";
//	  if(col_id.equals("id"))
//		  s = "Идентификатор";
	  if(col_id.equals("name"))
		  s = "Название";
	  if(col_id.equals("sessions"))
		  s = "Сессий";
	  if(col_id.equals("last_login"))
		  s = "Последний логин";
	  return s;
  }





  public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
  {
	ObjectResourceModel mod = or.getModel();
	return new JLabelRenderer(mod.getColumnValue(col_id));
  }
}