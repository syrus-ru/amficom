/*
 * $Id: StubPropertyDisplayModel.java,v 1.5 2004/09/25 19:50:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import java.awt.Color;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/25 19:50:00 $
 * @module generalclient_v1
 */
public class StubPropertyDisplayModel implements ObjectResourceDisplayModel
{
	ObjectResource or;
	
	public StubPropertyDisplayModel()
	{
	}
	
	public StubPropertyDisplayModel(ObjectResource or)
	{
		this.or = or;
	}
	
	static LinkedList empty = new LinkedList();
	
	public List getColumns()
	{
		if(or == null)
			return empty;
		ObjectResourceModel mod = or.getModel();
		if(mod == null)
			return empty;
		return mod.getPropertyColumns();
	}
	
	public String getColumnName(String col_id)
	{
		if(or == null)
			return "";
		ObjectResourceModel mod = or.getModel();
		if(mod == null)
			return "";
		return mod.getPropertyName(col_id);
	}
	
	public int getColumnSize(String col_id)
	{
		return 100;
	}
	
	public boolean isColumnEditable(String col_id)
	{
		if(or == null)
			return false;
		ObjectResourceModel mod = or.getModel();
		if(mod == null)
			return false;
		return mod.isPropertyEditable(col_id);
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
    {
		if(this.or == null)
			return null;
		ObjectResourceModel mod = this.or.getModel();
		if(mod == null)
			return null;
		return (PropertyRenderer )mod.getPropertyRenderer(col_id);
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
    {
		if(this.or == null)
			return null;
		ObjectResourceModel mod = this.or.getModel();
		if(mod == null)
			return null;
		return (PropertyEditor )mod.getPropertyEditor(col_id);
	}

	public boolean isColumnColored(String col_id)
	{
		return false;
	}
	
	public Color getColumnColor(ObjectResource or, String col_id)
	{
		return Color.white;
	}
}
