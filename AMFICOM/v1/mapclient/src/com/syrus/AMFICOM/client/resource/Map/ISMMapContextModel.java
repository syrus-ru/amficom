package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import com.ofx.geometry.*;
import java.text.*;

import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Configure.Map.Strategy.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.ISMMapContextPane;

//Данный класс используется для описания содержимого карты (её элементов и свойств)


public class ISMMapContextModel extends ObjectResourceModel
{
	ISMMapContext ismmap;

	public ISMMapContextModel(ISMMapContext ismmap)
	{
		this.ismmap = ismmap;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
		if(col_id.equals("id"))
			s = ismmap.ISM_id;
		if(col_id.equals("name"))
			s = ismmap.ISM_name;
		if(col_id.equals("codename"))
			s = ismmap.ISM_codename;
		if(col_id.equals("user_id"))
			s = ismmap.ISM_user_id;
		if(col_id.equals("map_id"))
			s = ismmap.id;
		if(col_id.equals("created"))
			s = sdf.format(new Date(ismmap.created));
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - MapContext");
			s = "";
		}

		return s;
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("map_id"))
		{
			ObjectResourceModel mod = ismmap.getModel();
//			return new ObjectResourceComboBox("mapcontext", mod.getColumnValue(col_id));
			return new JLabelRenderer(Pool.getName("mapcontext", mod.getColumnValue(col_id)));
		}
		if(col_id.equals("user_id"))
		{
			ObjectResourceModel mod = ismmap.getModel();
//			return new ObjectResourceComboBox("user", mod.getColumnValue(col_id));
			return new JLabelRenderer(Pool.getName("user", mod.getColumnValue(col_id)));
		}
		return null;//super.getColumnRenderer(col_id);
	}

	public PropertiesPanel getPropertyPane()
	{
		return new ISMMapContextPane(ismmap);
	}
	
	public Vector getPropertyColumns()
	{
		Vector cols = super.getPropertyColumns();
		cols.add("id");
		cols.add("name");
		cols.add("codename");
		cols.add("user_id");
		cols.add("map_id");
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
		if(col_id.equals("map_id"))
			return "Схема сети";
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
		if(col_id.equals("map_id"))
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