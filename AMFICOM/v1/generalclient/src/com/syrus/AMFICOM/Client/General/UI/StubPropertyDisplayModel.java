/*
 * $Id: StubPropertyDisplayModel.java,v 1.6 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;

import java.awt.Color;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/13 19:05:47 $
 * @module generalclient_v1
 */
public class StubPropertyDisplayModel implements ObjectResourceDisplayModel
{
	StorableObject or;
	
	public StubPropertyDisplayModel()
	{
	}
	
	public StubPropertyDisplayModel(StorableObject or)
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

	public PropertyRenderer getColumnRenderer(StorableObject or, String col_id)
    {
		if(this.or == null)
			return null;
		ObjectResourceModel mod = this.or.getModel();
		if(mod == null)
			return null;
		return (PropertyRenderer )mod.getPropertyRenderer(col_id);
	}

	public PropertyEditor getColumnEditor(StorableObject or, String col_id)
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
	
	public Color getColumnColor(StorableObject or, String col_id)
	{
		return Color.white;
	}
}
