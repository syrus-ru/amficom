/*
 * $Id: ServerDisplayModel.java,v 1.1 2004/08/06 12:14:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.System;

import com.syrus.AMFICOM.Client.General.UI.JLabelRenderer;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.util.Vector;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/08/06 12:14:19 $
 * @module generalclient_v1
 */
public class ServerDisplayModel extends StubDisplayModel
{

  public ServerDisplayModel()
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
