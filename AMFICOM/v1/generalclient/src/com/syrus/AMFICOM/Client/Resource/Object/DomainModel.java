/*
 * $Id: DomainModel.java,v 1.5 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.DomainPaneConfig;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.StorableObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/13 19:05:47 $
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
//        s = dom.getId();
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
    if(key.equals(Domain.class.getName()))
    {
      return dom.domains.values();
    }
    return new Vector();
  }

  public Enumeration getChildTypes()
  {
    Vector ret = new Vector();
    ret.add(Domain.class.getName());
    return ret.elements();
  }

  public Class getChildClass(String type)
  {
    if(type.equals(Domain.class.getName()))
      return Domain.class;
    return StorableObject.class;
  }


}
