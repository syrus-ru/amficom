package com.syrus.AMFICOM.Client.Map.UI.Display;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Event.*;

import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.MapContextPane;

//Данный класс используется для описания содержимого карты (её элементов и свойств)

public class MapContextDisplayModel extends StubDisplayModel
{
	public MapContextDisplayModel()
	{
		super();
	}

	List cols = new LinkedList();
	{
//		cols.add("id");
		cols.add("name");
		cols.add("scheme_id");
		cols.add("user_id");
		cols.add("domain_id");
	}

//Далее функции иполузуются для отображения свойств класса в таблице
	public List getColumns()
	{
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("scheme_id"))
			return "Схема";
		if(col_id.equals("user_id"))
			return "Владелец";
		if(col_id.equals("domain_id"))
			return "Домен";
		return "";
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("scheme_id"))
			return 100;
		if(col_id.equals("user_id"))
			return 100;
		return 100;
	}
/*
	public boolean isColumnEditable(String col_id)
	{

		if(col_id.equals("name"))
			return true;
		if(col_id.equals("codename"))
			return true;

		return false;
	}
*/
	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		if(col_id.equals("user_id"))
		{
			ObjectResourceModel mod = or.getModel();
//			return new ObjectResourceComboBox("user", mod.getColumnValue(col_id));
			return new JLabelRenderer(Pool.getName("user", mod.getColumnValue(col_id)));
		}
		if(col_id.equals("scheme_id"))
		{
			ObjectResourceModel mod = or.getModel();
//			return new ObjectResourceComboBox("user", mod.getColumnValue(col_id));
			return new JLabelRenderer(Pool.getName("scheme", mod.getColumnValue(col_id)));
		}
		if(col_id.equals("domain_id"))
		{
			ObjectResourceModel mod = or.getModel();
			return new JLabelRenderer(Pool.getName("domain", mod.getColumnValue(col_id)));
		}
		return null;
	}

}