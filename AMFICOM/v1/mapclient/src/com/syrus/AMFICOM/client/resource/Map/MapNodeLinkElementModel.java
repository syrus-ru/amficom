package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.UI.JLabelRenderer;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.General.UI.TrueFalseComboBox;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

//A0A
public class MapNodeLinkElementModel extends MapLinkElementModel
{
	MapNodeLinkElement nl;

	public MapNodeLinkElementModel(MapNodeLinkElement nl)
	{
		super(nl);
		this.nl = nl;
	}

	public String getColumnValue(String col_id)
	{
		try
		{
			if(col_id.equals("id"))
				return nl.id;
			if(col_id.equals("name"))
				return nl.name;
			if(col_id.equals("owner_id"))
				return Pool.getName("user", nl.owner_id);
			if(col_id.equals("length"))
				return nl.getSize();
			if(col_id.equals("physical_link_id"))
				return nl.getMapContext().getPhysicalLinkbyNodeLink( nl.getId()).getId();
			if(col_id.equals("type_id"))
				return LangModel.getString("node" + MapNodeLinkElement.typ);
/*
			if(col_id.equals("show_length"))
//				return nl.myTrueFalseComboBox.returnValue.getStateText();
				return String.valueOf(nl.show_metric);
			if(col_id.equals("color"))
//				return nl.colorComboBox.returnMyColor.text;
				return nl.color.toString();
			if(col_id.equals("line_type"))
				return nl.style;
//				return nl.colorComboBox.returnMyColor.text;
			if(col_id.equals("line_size"))
				return String.valueOf(nl.lineSize);
*/
		}
		catch(Exception e)
		{
//		  System.out.println("error gettin field value - MapNodeLink");
		}
		return "";
	}

	public void setColumnValue(String col_id, Object val)
	{
		if(col_id.equals("id"))
			nl.setId((String )val);
		if(col_id.equals("name"))
			nl.name = (String)val;
	//		if(col_id.equals("codename"))
	//			codename = (String )val;
		if(col_id.equals("owner_id"))
			nl.owner_id = (String)val;
/*
		if(col_id.equals("show_length"))
			nl.setShowSizeContext( ((TrueFalseComboBox.CurentState)val).getState());
		if(col_id.equals("color"))
			nl.setColor((Color)val);
		if(col_id.equals("line_type"))
		{
			nl.stroke = ((LineComboBox.MyLine)val).basicStroke;
			nl.style = ((LineComboBox.MyLine)val).text;
		}
		if(col_id.equals("line_size"))
			nl.lineSize = ((Integer )val).intValue();
*/
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("id"))
			return new TextFieldEditor(nl.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(nl.name);
	//		if(col_id.equals("codename"))
	//			return new JTextField(codename);
		if(col_id.equals("owner_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox("user", nl.owner_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("length"))
			return new TextFieldEditor(nl.getSize());
		if(col_id.equals("physical_link_id"))
//			return new TextFieldEditor( nl.getMapContext().getPhysicalLinkbyNodeLink( nl.getId()).getId());
			return new JLabelRenderer( nl.getMapContext().getPhysicalLinkbyNodeLink( nl.getId()).getName());
/*
		if(col_id.equals("show_length"))
		{
	//00                  myTrueFalseComboBox.setSelected( this.showSizeContext);//.getState());
			TrueFalseComboBox myTrueFalseComboBox = new TrueFalseComboBox();
			TrueFalseComboBox.CurentState cs = myTrueFalseComboBox.returnValue;// TrueFalseComboBox.CurentState();
			cs.setState(nl.show_metric);
			myTrueFalseComboBox.setState(cs);
			return myTrueFalseComboBox;
		}
		if(col_id.equals("color"))
		{
	//00                  colorComboBox.setSelected(this.nodeLinkColor.color);
			ColorComboBox colorComboBox = new ColorComboBox();
			colorComboBox.setSelected(nl.color);
			return colorComboBox;
		}
		if(col_id.equals("line_type"))
		{
	//00                  lineComboBox.setSelected(this.nodLinkStroke.text);
			LineComboBox lineComboBox = new LineComboBox();
			lineComboBox.setSelected(nl.style);
			return lineComboBox;
		}
		if(col_id.equals("line_size"))
		{
			LineThickComboBox lineThickComboBox = new LineThickComboBox();
			lineThickComboBox.setSelected(new Integer(nl.lineSize));
			return lineThickComboBox;
//			return new TextFieldEditor(String.valueOf(nl.lineSize));
		}
*/
		return null;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}

	List cols = new LinkedList();
	{
//		cols.add("id");
		cols.add("name");
//		cols.add("owner_id");
		cols.add("length");
		cols.add("physical_link_id");
	}
	
	public List getPropertyColumns()
	{
		List retcols = new LinkedList();
		List cols2 = super.getPropertyColumns();
		retcols.addAll(cols);
		retcols.addAll(cols2);
		return cols;
	}

	public String getPropertyName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("owner_id"))
			return "Владелец";
		if(col_id.equals("length"))
			return "Длина";
		if(col_id.equals("physical_link_id"))
			return "Линия связи";
		return super.getPropertyName(col_id);
	}

	public String getPropertyValue(String col_id)
	{
		String value;
		ElementAttribute ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
			value = getColumnValue(col_id);
		else
			value = super.getPropertyValue(col_id);
//		if(value == null)
		return value;
	}

	public void setPropertyValue(String col_id, Object val)
	{
		ElementAttribute ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
			setColumnValue(col_id, val);
		else
			super.setPropertyValue(col_id, val);
	}

	public boolean isPropertyEditable(String col_id)
	{
		if(col_id.equals("id"))
			return false;
		if(col_id.equals("name"))
			return true;
		if(col_id.equals("length"))
			return false;
		if(col_id.equals("owner_id"))
			return false;
		if(col_id.equals("physical_link_id"))
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
			c = getColumnEditor(col_id);
		return c;
	}
}