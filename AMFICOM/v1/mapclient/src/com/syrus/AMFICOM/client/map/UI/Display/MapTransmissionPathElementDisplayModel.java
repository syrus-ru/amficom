package com.syrus.AMFICOM.Client.Map.UI.Display;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

//A0A
public class MapTransmissionPathElementDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext;
	
	public MapTransmissionPathElementDisplayModel()
	{
		this(new ApplicationContext());
	}

	public MapTransmissionPathElementDisplayModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	List cols = new LinkedList();
	{
		cols.add("id");
		cols.add("name");
		cols.add("type_id");
		cols.add("length");
		cols.add("startnode");
		cols.add("endnode");
	}
	
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
		if(col_id.equals("codename"))
			return "Кодовое имя";
		if(col_id.equals("owner_id"))
			return "Владелец";
		if(col_id.equals("type_id"))
			return "Тип";
		if(col_id.equals("path_id"))
			return "Волокно";
		if(col_id.equals("color"))
			return "Цвет";
		if(col_id.equals("line_type"))
			return "Стиль";
		if(col_id.equals("length"))
			return "Длина";
		if(col_id.equals("startnode"))
			return "Начальный узел";
		if(col_id.equals("endnode"))
			return "Конечный узел";
		return "";
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("codename"))
			return 100;
		if(col_id.equals("owner_id"))
			return 100;
		return 100;
	}
/*
	public boolean isColumnEditable(String col_id)
	{
		if (aContext.getApplicationModel().isEnabled("mapActionEditProperties"))
		{
			if(col_id.equals("id"))
				return false;
			if(col_id.equals("name"))
				return true;
			if(col_id.equals("codename"))
				return true;
			if(col_id.equals("length"))
				return false;
			if(col_id.equals("owner_id"))
				return false;
			if(col_id.equals("type_id"))
				return true;
			if(col_id.equals("path_id"))
				return true;
			if(col_id.equals("color"))
				return true;
			if(col_id.equals("line_type"))
				return true;
		}
		return false;
	}
*/
	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
/*
		MapTransmissionPathElement path = (MapTransmissionPathElement )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(path.getID());
		if(col_id.equals("name"))
			return new TextFieldEditor(path.name);
		if(col_id.equals("type_id"))
			return new ObjectResourceComboBox("mappathproto", path.type_id);
		if(col_id.equals("path_id"))
			return new TextFieldEditor(path.PATH_ID);
		if(col_id.equals("owner_id"))
			return new ObjectResourceComboBox("user", path.owner_id);
		if(col_id.equals("length"))
			return new TextFieldEditor(String.valueOf( path.getSizeInDouble()));
		if(col_id.equals("color"))
		{
			path.colorComboBox.setSelected( path.colorComboBox.getReturnMyColor().color);
			return path.colorComboBox;
		}
		if(col_id.equals("line_type"))
		{
			path.lineComboBox.setSelected( path.lineComboBox.getReturnMyLine().text);
			return path.lineComboBox;
		}
*/		
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}