package com.syrus.AMFICOM.Client.Map.UI.Display;

import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.util.Vector;

//A0A
public class MapNodeLinkElementDisplayModel extends StubDisplayModel
{
	public MapNodeLinkElementDisplayModel()
	{
		super();
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("id");
		cols.add("name");
	//    cols.put("codename", "Кодовое имя");
		cols.add("owner_id");
	//        	cols.put("type", ""},
		cols.add("length");
		cols.add("physical_link_id");
		cols.add("show_length");
	//    cols.put("color", "Цвет");
	//    cols.put("line_type", "Стиль");
	//          	cols.put("line_size", "Толщина");
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
		if(col_id.equals("length"))
			return "Длина";
		if(col_id.equals("physical_link_id"))
			return "Линия связи";
		if(col_id.equals("show_length"))
			return "Показывать длину";
		if(col_id.equals("color"))
			return "Цвет";
		if(col_id.equals("line_type"))
			return "Стиль";
		if(col_id.equals("line_size"))
			return "Толщина";
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

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
/*
		MapNodeLinkElement nl = (MapNodeLinkElement )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(nl.getID());
		if(col_id.equals("name"))
			return new TextFieldEditor(nl.name);
	//		if(col_id.equals("codename"))
	//			return new JTextField(codename);
		if(col_id.equals("owner_id"))
			return new ObjectResourceComboBox("user", nl.owner_id);
		if(col_id.equals("length"))
			return new TextFieldEditor(nl.getSize());
		if(col_id.equals("physical_link_id"))
			return new TextFieldEditor( nl.getMapContext().getPhysicalLinkbyNodeLink( nl.getID()).getID());
		if(col_id.equals("show_length"))
		{
	//00                  myTrueFalseComboBox.setSelected( this.showSizeContext);//.getState());
			return nl.myTrueFalseComboBox;
		}
		if(col_id.equals("color"))
		{
	//00                  colorComboBox.setSelected(this.nodeLinkColor.color);
			return nl.colorComboBox;
		}
		if(col_id.equals("line_type"))
		{
	//00                  lineComboBox.setSelected(this.nodLinkStroke.text);
			return nl.lineComboBox;
		}
		if(col_id.equals("line_size"))
			return new TextFieldEditor(String.valueOf(nl.lineSize));
*/			
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}