/*
 * $Id: DomainModel.java,v 1.4 2004/09/27 15:50:35 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.DomainPaneConfig;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 15:50:35 $
 * @module generalclient_v1
 */
public class DomainModel extends ObjectResourceModel
{
  public Domain dom;

  public DomainModel(Domain dom)
  {
    this.dom = dom;
  }

	public ObjectResourcePropertiesPane getPropertyPane() {
		DomainPaneConfig domainPaneConfig = DomainPaneConfig.getInstance();
		domainPaneConfig.setObjectResource(dom);
		return domainPaneConfig;
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

  public Collection getChildren(String key)
  {
    if(key.equals(Domain.typ))
    {
      return dom.domains.values();
    }
    return new Vector();
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
