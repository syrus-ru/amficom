package com.syrus.AMFICOM.Client.Map.UI.Display;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.util.Vector;

//A0A
public class MapPhysicalLinkElementDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext;
	
	public MapPhysicalLinkElementDisplayModel()
	{
		this(new ApplicationContext());
	}

	public MapPhysicalLinkElementDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;
	}
	
	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("id");
		cols.add("name");
		cols.add("type_id");
//		cols.add("color");
//		cols.add("style");
		cols.add("length");
//		cols.add("thickness");
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("type_id"))
	//    if(col_id.equals("link_type_id"))
			return "Тип";
		if(col_id.equals("color"))
			return "Цвет";
		if(col_id.equals("style"))
			return "Стиль";
		if(col_id.equals("thickness"))
			return "Толщина";
		if(col_id.equals("length"))
			return "Длина";
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
		if ( aContext.getApplicationModel().isEnabled("mapActionEditProperties"))
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
			if(col_id.equals("length"))
				return false;
			if(col_id.equals("color"))
				return true;
			if(col_id.equals("line_type"))
				return true;
			if(col_id.equals("line_size"))
				return true;
		}
		return false;
	}
*/
	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
/*
		MapPhysicalLinkElement link = (MapPhysicalLinkElement )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(link.getID());
		if(col_id.equals("name"))
			return new TextFieldEditor(link.name);
		if(col_id.equals("length"))
			return new TextFieldEditor(String.valueOf( (int) link.getSizeInDouble()));
	//		if(col_id.equals("codename"))
	//			return new JTextField(codename);
		if(col_id.equals("owner_id"))
			return new ObjectResourceComboBox("user", link.owner_id);
		if(col_id.equals("type_id"))
			return new ObjectResourceComboBox("maplinkproto", link.type_id);
	//    if(col_id.equals("link_type_id"))
	//      return new ObjectResourceComboBox("maplinkproto", link_type_id);
		if(col_id.equals("color"))
		{
			link.colorComboBox.setSelected( link.colorComboBox.getReturnMyColor().color);
			return link.colorComboBox;
		}
		if(col_id.equals("line_type"))
		{
			link.lineComboBox.setSelected( link.lineComboBox.getReturnMyLine().text);
			return link.lineComboBox;
		}
		if(col_id.equals("line_size"))
			return new TextFieldEditor(String.valueOf(link.lineSize));
*/
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}

}