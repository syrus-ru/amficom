package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.Component;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;

//A0A
public class MapMarkElementModel extends ObjectResourceModel
{
	MapMarkElement mark;

	public MapMarkElementModel(MapMarkElement mark)
	{
		this.mark = mark;
	}

	public Vector getColumns()
	{
//		System.out.println("marker cols");
		Vector cols = new Vector();
//		cols.add("id");
		cols.add("name");
		cols.add("longitude");
		cols.add("latitude");
		cols.add("Length_1");
		cols.add("Length_2");
		return cols;
	}

	public String getColumnName(String col_id)
	{
//		System.out.println("marker cols name");
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("longitude"))
			return "Долгота";
		if(col_id.equals("latitude"))
			return "Широта";
		if(col_id.equals("Length_1"))
			return "Длина от начала";
		if(col_id.equals("Length_2"))
			return "Длина от конца";
		return "";
	}

	public int getColumnSize(String col_id)
	{
		return 100;
	}

	public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("id"))
			return false;
		if(col_id.equals("name"))
			return true;
		if(col_id.equals("Length_1"))
			return true;
		if(col_id.equals("Length_2"))
			return false;
		if(col_id.equals("longitude"))
			return false;
		if(col_id.equals("latitude"))
			return false;
		return false;
	}

	public String getColumnValue(String col_id)
	{
//		System.out.println("marker cols val");
		java.text.DecimalFormat df2 = new java.text.DecimalFormat("###,#0.0");

		try
		{
			if(col_id.equals("id"))
				return mark.getId();
			if(col_id.equals("name"))
				return mark.getName();
			if(col_id.equals("longitude"))
				return String.valueOf(MyUtil.fourdigits(mark.getAnchor().x)) ;
			if(col_id.equals("latitude"))
				return String.valueOf(MyUtil.fourdigits(mark.getAnchor().y));
			if(col_id.equals("Length_1"))
				return df2.format(MyUtil.fourdigits(mark.getFromStartLengthLt()));
			if(col_id.equals("Length_2"))
				return df2.format(MyUtil.fourdigits(mark.getFromEndLengthLt()));
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - MapNodeLink");
		}
		return "";
	}

	public void setColumnValue(String col_id, Object val)
	{
//		System.out.println("marker cols set val");
		try
		{
			if(col_id.equals("id"))
				mark.setId((String )val);
			if(col_id.equals("name"))
				mark.name = (String )val;
			if(col_id.equals("longitude"))
				mark.getAnchor().x = Double.parseDouble((String )val);
			if(col_id.equals("latitude"))
				mark.getAnchor().y = Double.parseDouble((String )val);
			if(col_id.equals("Length_1"))
				mark.moveToFromStart(Double.parseDouble((String )val));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog( mark.getLogicalNetLayer().mainFrame , "Возможно неправельно было введино значение !!!", "", JOptionPane.ERROR_MESSAGE);
		}
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("id"))
			return new TextFieldEditor(mark.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(mark.name);
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(mark.getAnchor().x)) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(mark.getAnchor().y)) );
		if(col_id.equals("Length_1"))
			return new TextFieldEditor( String.valueOf((int)mark.getFromStartLengthLt()) );
		if(col_id.equals("Length_2"))
			return new TextFieldEditor( String.valueOf((int)mark.getFromEndLengthLt()) );
		return null;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}

	
	public Vector getPropertyColumns()
	{
//		System.out.println("marker prop cols");
		Vector cols = getColumns();//new Hashtable();
		for(Enumeration enum = mark.attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			cols.add(ea.getId());
		}
		return cols;
	}

	public String getPropertyName(String col_id)
	{
//		System.out.println("marker prop name");
		ElementAttribute ea = (ElementAttribute )mark.attributes.get(col_id);
		if(ea == null)
			return getColumnName(col_id);
		return ea.getName();
	}

	public String getPropertyValue(String col_id)
	{
//		System.out.println("marker prop val");
		ElementAttribute ea = (ElementAttribute )mark.attributes.get(col_id);
		if(ea == null)
			return getColumnValue(col_id);
		return ea.value;
	}

	public void setPropertyValue(String col_id, Object val)
	{
//		System.out.println("marker prop set val");
		ElementAttribute ea = (ElementAttribute )mark.attributes.get(col_id);
		if(ea == null)
			setColumnValue(col_id, val);
		else
			ea.setValue(val);
	}

	public boolean isPropertyEditable(String col_id)
	{
		ElementAttribute ea = (ElementAttribute )mark.attributes.get(col_id);
		if(ea == null)
			return isColumnEditable(col_id);
		return ea.isEditable();
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