/*
 * $Id: ServerDisplayModel.java,v 1.3 2004/09/27 14:30:54 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.System;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 14:30:54 $
 * @module generalclient_v1
 */
public class ServerDisplayModel extends StubDisplayModel
{

  public ServerDisplayModel()
  {
  }

    List cols = new LinkedList();
	{
//		cols.add("id");
		cols.add("name");
		cols.add("created");
		cols.add("modified");
	}
	
	public List getColumns()
	{
		return cols;
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
