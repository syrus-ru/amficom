/*
 * $Id: ObjectResourceDisplayModel.java,v 1.3 2004/09/27 06:02:30 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.awt.Color;
import java.util.List;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 06:02:30 $
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
