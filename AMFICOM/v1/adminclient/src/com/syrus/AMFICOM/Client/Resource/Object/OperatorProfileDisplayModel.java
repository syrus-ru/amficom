package com.syrus.AMFICOM.Client.Resource.Object;
import java.util.*;


import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class OperatorProfileDisplayModel extends StubDisplayModel
{

  public OperatorProfileDisplayModel()
  {
  }
  public Vector getColumns()
  {
    Vector vec = new Vector();
//    vec.add("id");
    vec.add("name");
    vec.add("owner_id");
    vec.add("modified");
    return vec;
  }

  public String getColumnName(String col_id)
  {
    String s = "";
//    if(col_id.equals("id"))
//      s = "Идентификатор";
    if(col_id.equals("name"))
      s = "Название";
    if(col_id.equals("owner_id"))
      s = "Владелец";
    if(col_id.equals("modified"))
      s = "Изменен";
    return s;
  }

  public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
  {
    ObjectResourceModel mod = or.getModel();
    return new JLabelRenderer(mod.getColumnValue(col_id));
  }
}