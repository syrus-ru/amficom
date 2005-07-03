/*
 * $Id: ObjectResourceDisplayModel.java,v 1.4 2005/05/05 11:04:47 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Color;
import java.util.List;

import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;

/**
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2005/05/05 11:04:47 $
 * @module generalclient_v1
 */
public interface ObjectResourceDisplayModel
{
	List getColumns();

	String getColumnName(String col_id);

	int getColumnSize(String col_id);

	boolean isColumnEditable(String col_id);

	boolean isColumnColored(String col_id);

	PropertyRenderer getColumnRenderer(ObjectResource or, String col_id);

	PropertyEditor getColumnEditor(ObjectResource or, String col_id);

	Color getColumnColor(ObjectResource or, String col_id);
}
