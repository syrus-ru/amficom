package com.syrus.AMFICOM.Client.Resource.Object;

import java.text.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class DomainModel extends ObjectResourceModel
{
  public Domain dom;

  public DomainModel(Domain dom)
  {
    this.dom = dom;
  }

  public PropertiesPanel getPropertyPane()
  {
    return new DomainPaneConfig(dom);
  }

  public String getColumnValue(String col_id)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    String s = "";
    try
    {
//      if(col_id.equals("id"))
//        s = dom.id;
      if(col_id.equals("name"))
        s = dom.name;
      if(col_id.equals("owner_id"))
        s = dom.owner_id;
      if(col_id.equals("modified"))
        s = sdf.format(new Date(dom.modified));
      if(col_id.equals("domain_id"))
        s = dom.domain_id;
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
    if(key.equals(Domain.typ))
    {
      return dom.domains.elements();
    }
    return new Vector().elements();
  }

  public Enumeration getChildTypes()
  {
    Vector ret = new Vector();
    ret.add(Domain.typ);
    return ret.elements();
  }

  public Class getChildClass(String type)
  {
    if(type.equals(Domain.typ))
      return Domain.class;
    return ObjectResource.class;
  }


}
