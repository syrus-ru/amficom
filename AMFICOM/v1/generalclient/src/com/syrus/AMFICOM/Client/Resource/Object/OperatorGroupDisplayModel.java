/*
 * $Id: OperatorGroupDisplayModel.java,v 1.1 2004/08/06 12:14:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Object;
import java.util.*;


import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/08/06 12:14:19 $
 * @module generalclient_v1
 */
public class OperatorGroupDisplayModel extends StubDisplayModel
{

  public OperatorGroupDisplayModel()
  {
  }

  public Vector getColumns()
  {
	  Vector vec = new Vector();
//	  vec.add("id");
	  vec.add("name");
//	  vec.add("owner_id");
	  vec.add("modified");
		  return vec;
  }

  public String getColumnName(String col_id)
  {
	  String s = "";
//	  if(col_id.equals("id"))
//		  s = "Идентификатор";
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
