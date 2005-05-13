/*
 * $Id: ServerDisplayModel.java,v 1.4 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.Resource.System;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;

import java.util.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/13 19:05:47 $
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
      s = "�������������";
    if(col_id.equals("name"))
      s = "���";
    if(col_id.equals("created"))
      s = "������";
    if(col_id.equals("modified"))
      s = "�������";
    return s;
  }

  public PropertyRenderer getColumnRenderer(StorableObject or, String col_id)
  {
    ObjectResourceModel mod = or.getModel();
    return new JLabelRenderer(mod.getColumnValue(col_id));
  }

}
