/*
 * $Id: Categories.java,v 1.4 2004/09/27 15:44:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 15:44:10 $
 * @module generalclient_v1
 */
public class Categories
{

  DataSourceInterface dsi;
  User user;

  public Categories(DataSourceInterface dsi)
  {
    this.dsi = dsi;
    user = (User)Pool.get(User.typ, dsi.getSession().getUserId());
    setCategories();
  }


	private void setCategories()
	{
		Map map = Pool.getMap(OperatorCategory.typ);
		if (map == null)
			map = new Hashtable();
		Vector v = new Vector();
		for (Iterator iterator = map.values().iterator(); iterator.hasNext();)
			v.add(((OperatorCategory) (iterator.next())).codename);
		Date date = new Date();
//Here, we define categories with next codenames:
    //  1) admin;
    //  2) sysadmin;
    //  3) designer;
    //  4) analyst;
    //  5) operator;
    //  6) spec;
    //  7) subscriber;
		Vector vec = new Vector();
		if(!v.contains(OperatorCategory.admin))
		{
			OperatorCategory opCat = new OperatorCategory(OperatorCategory.admin, "Администратор", OperatorCategory.admin, "", new Vector());
			opCat.modified = date.getTime();
			vec.add(opCat);
		}
		if(!v.contains(OperatorCategory.sysadmin))
		{
			OperatorCategory opCat = new OperatorCategory(OperatorCategory.sysadmin, "Системный администратор", OperatorCategory.sysadmin, "", new Vector());
			opCat.modified = date.getTime();
			vec.add(opCat);
		}
		if(!v.contains(OperatorCategory.designer))
		{
			OperatorCategory opCat = new OperatorCategory(OperatorCategory.designer, "Проектировщик", OperatorCategory.designer, "", new Vector());
			opCat.modified = date.getTime();
			vec.add(opCat);
		}
		if(!v.contains(OperatorCategory.analyst))
		{
			OperatorCategory opCat = new OperatorCategory(OperatorCategory.analyst, "Аналитик", OperatorCategory.analyst, "", new Vector());
			opCat.modified = date.getTime();
			vec.add(opCat);
		}
		if (!v.contains(OperatorCategory.operator))
		{
			OperatorCategory opCat = new OperatorCategory(OperatorCategory.operator, "Оператор", OperatorCategory.operator, "", new Vector());
			opCat.modified = date.getTime();
			vec.add(opCat);
		}
		if (!v.contains(OperatorCategory.spec))
		{
			OperatorCategory opCat = new OperatorCategory(OperatorCategory.spec, "Специалист", OperatorCategory.spec, "", new Vector());
			opCat.modified = date.getTime();
			vec.add(opCat);
		}
		if (!v.contains(OperatorCategory.subscriber))
		{
			OperatorCategory opCat = new OperatorCategory(OperatorCategory.subscriber, "Абонент", OperatorCategory.subscriber, "", new Vector());
			opCat.modified = date.getTime();
			vec.add(opCat);
		}
		saveCategories(vec);
	}

  private void saveCategories(Vector vec)
  {
    OperatorCategory oc;
    for(Enumeration e = vec.elements(); e.hasMoreElements(); )
    {
      oc = (OperatorCategory)e.nextElement();
      oc.setTransferableFromLocal();
      Pool.put(OperatorCategory.typ, oc.id, oc);
      try
      {
        dsi.SaveCategory(oc.id);
        System.out.println("Category "+oc.getName()+" saved.");

      }
      catch (Exception ex)
      {
        System.err.println("Error saving caegory "+oc.getName());
      }
    }
  }

}
