package com.syrus.AMFICOM.Client.Resource.Object;
import java.util.*;


import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DomainDisplayModel extends StubDisplayModel
{

  public DomainDisplayModel()
  {
  }

    List cols = new LinkedList();
	{
//    	vec.add("id");
		cols.add("name");
//    	vec.add("owner_id");
		cols.add("modified");
		cols.add("domain_id");
	}

	public List getColumns()
	{
		return cols;
	}

  public String getColumnName(String col_id)
  {
    String s = "";
//    if(col_id.equals("id"))
//      s = "�������������";
    if(col_id.equals("name"))
      s = "��������";
    if(col_id.equals("owner_id"))
      s = "��������";
    if(col_id.equals("modified"))
      s = "�������";
    if(col_id.equals("domain_id"))
      s = "�����";
    return s;
  }

  public PropertyRenderer getColumnRenderer(StorableObject or, String col_id)
  {
    ObjectResourceModel mod = or.getModel();
    return new JLabelRenderer(mod.getColumnValue(col_id));
  }

}
