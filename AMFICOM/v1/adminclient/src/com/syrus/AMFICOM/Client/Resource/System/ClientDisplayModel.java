package com.syrus.AMFICOM.Client.Resource.System;

import java.util.*;


import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;


public class ClientDisplayModel extends StubDisplayModel
{

  public ClientDisplayModel()
  {
  }

  public Vector getColumns()
  {
    Vector vec = new Vector();
    vec.add("id");
    vec.add("name");
    vec.add("created");
    vec.add("modified");
    return vec;
  }

  public String getColumnName(String col_id)
  {
    String s = "";
    if(col_id.equals("id"))
      s = "Идентификатор";
    if(col_id.equals("name"))
      s = "Имя";
    if(col_id.equals("created"))
      s = "Создан";
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