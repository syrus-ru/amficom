package com.syrus.AMFICOM.Client.Map.UI.Display;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.Map.MapKISNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.util.Vector;

//A0A
public class MapKISNodeElementDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();

	public MapKISNodeElementDisplayModel()
	{
		this(new ApplicationContext());
	}

	public MapKISNodeElementDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;
	}

//Далее функции иполузуются для отображения свойств класса в таблице
	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("id");
		cols.add("name");
		cols.add("type_id");
		cols.add("kis_id");
		cols.add("longitude");
		cols.add("latitude");
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
		if(col_id.equals("kis_id"))
			return "КИС";
		if(col_id.equals("longitude"))
			return "Долгота";
		if(col_id.equals("latitude"))
			return "Широта";
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
		if(col_id.equals("type_id"))
			return 100;
		if(col_id.equals("kis_id"))
			return 100;
		if(col_id.equals("longitude"))
			return 100;
		if(col_id.equals("latitude"))
			return 100;
		return 100;
	}
/*
	public boolean isColumnEditable(String col_id)
	{
		if(aContext.getApplicationModel().isEnabled("mapActionEditProperties"))
		{
			if(col_id.equals("id"))
				return false;
			if(col_id.equals("name"))
				return true;
			if(col_id.equals("codename"))
				return true;
			if(col_id.equals("owner_id"))
				return false;
			if(col_id.equals("type_id"))
				return true;
			if(col_id.equals("kis_id"))
				return true;
			if(col_id.equals("longitude"))
				return true;
			if(col_id.equals("latitude"))
				return true;
		}
		return false;
	}
*/
	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		MapKISNodeElement node = (MapKISNodeElement )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(node.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(node.name);
		if(col_id.equals("owner_id"))
			return new ObjectResourceComboBox("user", node.owner_id);
		if(col_id.equals("type_id"))
			return new ObjectResourceComboBox("mapkisproto", node.type_id);
		if(col_id.equals("kis_id"))
			return new TextFieldEditor(node.element_id);
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(node.getAnchor().x) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(node.getAnchor().y) );
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}


}