package com.syrus.AMFICOM.Client.Map.UI.Display;

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import com.ofx.geometry.*;

import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Map.Strategy.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.ISMMapContextPane;

//Данный класс используется для описания содержимого карты (её элементов и свойств)


public class ISMMapContextDisplayModel extends MapContextDisplayModel
{
	public ISMMapContextDisplayModel()
	{
		super();
	}

	public Vector getColumns()
	{
		Vector cols = super.getColumns();
//		cols.add("id");
		cols.add("name");
		cols.add("created");
		cols.add("user_id");
		cols.add("map_id");
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("map_id"))
			return "Структура сети";
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("codename"))
			return "Кодовое имя";
		if(col_id.equals("user_id"))
			return "Владелец";
		if(col_id.equals("created"))
			return "Дата создания";
		return "";
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("map_id"))
			return 100;
		if(col_id.equals("id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("codename"))
			return 100;
		if(col_id.equals("user_id"))
			return 100;
		return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		if(col_id.equals("user_id"))
		{
			ObjectResourceModel mod = or.getModel();
//			return new ObjectResourceComboBox("user", mod.getColumnValue(col_id));
			return new JLabelRenderer(Pool.getName("user", mod.getColumnValue(col_id)));
		}
		if(col_id.equals("map_id"))
		{
			ObjectResourceModel mod = or.getModel();
//			return new ObjectResourceComboBox("mapcontext", mod.getColumnValue(col_id));
			return new JLabelRenderer(Pool.getName("mapcontext", mod.getColumnValue(col_id)));
		}
		return null;
//		return super.getColumnRenderer(or, col_id);
	}

}


