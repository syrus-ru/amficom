package com.syrus.AMFICOM.Client.Resource.Object;
import java.util.*;


import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;


public class UserDisplayModel extends StubDisplayModel
{

  public UserDisplayModel()
  {
  }

    List cols = new LinkedList();
	{
//	  	vec.add("id");
		cols.add("name");
		cols.add("sessions");
		cols.add("last_login");
	}

	public List getColumns()
	{
		return cols;
	}

  public String getColumnName(String col_id)
  {
	  String s = "";
//	  if(col_id.equals("id"))
//		  s = "�������������";
	  if(col_id.equals("name"))
		  s = "��������";
	  if(col_id.equals("sessions"))
		  s = "������";
	  if(col_id.equals("last_login"))
		  s = "��������� �����";
	  return s;
  }





  public PropertyRenderer getColumnRenderer(StorableObject or, String col_id)
  {
	ObjectResourceModel mod = or.getModel();
	return new JLabelRenderer(mod.getColumnValue(col_id));
  }
}
