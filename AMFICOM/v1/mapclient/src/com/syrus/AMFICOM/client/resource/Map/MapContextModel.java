package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;

import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Event.*;

import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.MapContextPane;

//Данный класс используется для описания содержимого карты (её элементов и свойств)

public class MapContextModel extends ObjectResourceModel
{
	public MapContext map;

	public MapContextModel(MapContext map)
	{
		this.map = map;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
			if(col_id.equals("id"))
				s = map.id;
			if(col_id.equals("name"))
				s = map.name;
			if(col_id.equals("codename"))
				s = map.codename;
			if(col_id.equals("user_id"))
				s = map.user_id;
			if(col_id.equals("scheme_id"))
				s = map.scheme_id;
			if(col_id.equals("created"))
				s = sdf.format(new Date(map.created));
			if(col_id.equals("modified"))
				s = sdf.format(new Date(map.modified));
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - MapContext");
			s = "";
		}

		return s;
	}

	public void setColumnValue(String col_id, Object val)
	{
		if(col_id.equals("name"))
			map.name = (String )val;
		if(col_id.equals("codename"))
			map.codename = (String )val;
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("user_id"))
		{
			ObjectResourceModel mod = map.getModel();
//			return new ObjectResourceComboBox("user", mod.getColumnValue(col_id));
			return new JLabelRenderer(Pool.getName("user", mod.getColumnValue(col_id)));
		}
		if(col_id.equals("scheme_id"))
		{
			ObjectResourceModel mod = map.getModel();
			return new JLabelRenderer(Pool.getName(Scheme.typ, mod.getColumnValue(col_id)));
		}
/*
		if(col_id.equals("codename"))
		{
			return new MyJButton("Ля-ля-ля");
		}
*/
		return null;
	}

	public PropertiesPanel getPropertyPane()
	{
		return new MapContextPane(map);
	}
	
	public Vector getPropertyColumns()
	{
		Vector cols = super.getPropertyColumns();
//		cols.add("id");
		cols.add("name");
//		cols.add("codename");
		cols.add("user_id");
		cols.add("scheme_id");
		cols.add("created");
		cols.add("modified");
		return cols;
	}

	public String getPropertyName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("codename"))
			return "Код";
		if(col_id.equals("user_id"))
			return "Владелец";
		if(col_id.equals("scheme_id"))
			return "Схема";
		if(col_id.equals("created"))
			return "Время создания";
		if(col_id.equals("modified"))
			return "Время изменения";
		return super.getPropertyName(col_id);
	}

	public String getPropertyValue(String col_id)
	{
		return getColumnValue(col_id);
	}

	public void setPropertyValue(String col_id, Object val)
	{
		setColumnValue(col_id, val);
	}

	public boolean isPropertyEditable(String col_id)
	{
		if(col_id.equals("id"))
			return false;
		if(col_id.equals("name"))
			return true;
		if(col_id.equals("codename"))
			return true;
		if(col_id.equals("user_id"))
			return false;
		if(col_id.equals("scheme_id"))
			return false;
		return super.isPropertyEditable(col_id);
	}

	public Component getPropertyRenderer(String col_id)
	{
		Component c = super.getPropertyRenderer(col_id);
		if(c == null)
			c = getColumnRenderer(col_id);
		return c;
	}

	public Component getPropertyEditor(String col_id)
	{
		Component c = super.getPropertyEditor(col_id);
		if(c == null)
			c = null;//getColumnEditor(col_id);
		return c;
	}
}