package com.syrus.AMFICOM.Client.Resource.Object;

import java.util.*;


import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;


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

    Hashtable h = Pool.getHash(OperatorCategory.typ);
    if(h == null)
      h = new Hashtable();
    OperatorCategory oc;

    Vector v = new Vector();
    for(Enumeration e = h.elements(); e.hasMoreElements();)
    {
      oc = (OperatorCategory)e.nextElement();
      v.add(oc.codename);
    }

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
      OperatorCategory opCat = new OperatorCategory(OperatorCategory.admin, "Администратор",
          OperatorCategory.admin, "", new Vector());
      opCat.modified = date.getTime();
      vec.add(opCat);
    }

    if(!v.contains(OperatorCategory.sysadmin))
    {
      OperatorCategory opCat = new OperatorCategory(OperatorCategory.sysadmin, "Системный администратор",
          OperatorCategory.sysadmin, "", new Vector());
      opCat.modified = date.getTime();
      vec.add(opCat);
    }

    if(!v.contains(OperatorCategory.designer))
    {
      OperatorCategory opCat = new OperatorCategory(OperatorCategory.designer, "Проектировщик",
          OperatorCategory.designer, "", new Vector());
      opCat.modified = date.getTime();
      vec.add(opCat);
    }

    if(!v.contains(OperatorCategory.analyst))
    {
      OperatorCategory opCat = new OperatorCategory(OperatorCategory.analyst, "Аналитик",
          OperatorCategory.analyst, "", new Vector());
      opCat.modified = date.getTime();
      vec.add(opCat);
    }


    if(!v.contains(OperatorCategory.operator))
    {
      OperatorCategory opCat = new OperatorCategory(OperatorCategory.operator, "Оператор",
          OperatorCategory.operator, "", new Vector());
      opCat.modified = date.getTime();
      vec.add(opCat);
    }

    if(!v.contains(OperatorCategory.spec))
    {
      OperatorCategory opCat = new OperatorCategory(OperatorCategory.spec, "Специалист",
          OperatorCategory.spec, "", new Vector());
      opCat.modified = date.getTime();
      vec.add(opCat);
    }

    if(!v.contains(OperatorCategory.subscriber))
    {
      OperatorCategory opCat = new OperatorCategory(OperatorCategory.subscriber, "Абонент",
          OperatorCategory.subscriber, "", new Vector());
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
