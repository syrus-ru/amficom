package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.Component;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;

//A0A
public class MapMarkerModel extends ObjectResourceModel
{
	MapMarker marker;

	public MapMarkerModel(MapMarker marker)
	{
		this.marker = marker;
	}

	public Vector getColumns()
	{
//		System.out.println("marker cols");
		Vector cols = new Vector();
//		cols.add("id");
		cols.add("name");
//		cols.add("codename");
//		cols.add("owner_id");
		cols.add("type_id");
		cols.add("longitude");
		cols.add("latitude");
		cols.add("Length_1");
		cols.add("Length_2");
		cols.add("path_id");
		return cols;
	}

	public String getColumnName(String col_id)
	{
//		System.out.println("marker cols name");
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
			return "Путь";
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
		if(col_id.equals("codename"))
			return true;
		if(col_id.equals("owner_id"))
			return false;
		if(col_id.equals("Length_1"))
			return true;
		if(col_id.equals("Length_2"))
			return false;
		if(col_id.equals("type_id"))
			return false;
		if(col_id.equals("longitude"))
			return true;
		if(col_id.equals("latitude"))
			return true;
		return false;
	}

	public String getColumnValue(String col_id)
	{
//		System.out.println("marker cols val");
		try
		{
			if(col_id.equals("id"))
				return marker.getId();
			if(col_id.equals("name"))
				return marker.getName();
			if(col_id.equals("owner_id"))
				return Pool.getName("user", marker.owner_id);
			if(col_id.equals("typ"))
				return marker.getTyp();
			if(col_id.equals("type_id"))
				return LangModel.getString("node" + marker.getTyp());
			if(col_id.equals("path"))
				return marker.path_id;
			if(col_id.equals("longitude"))
				return String.valueOf(MyUtil.fourdigits(marker.getAnchor().x)) ;
			if(col_id.equals("latitude"))
				return String.valueOf(MyUtil.fourdigits(marker.getAnchor().y));
			if(col_id.equals("Length_1"))
				return String.valueOf(MyUtil.fourdigits(marker.getFromStartLengthLf()));
			if(col_id.equals("Length_2"))
				return String.valueOf(MyUtil.fourdigits(marker.getFromEndLengthLf()));
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
				marker.setId((String )val);
			if(col_id.equals("name"))
				marker.name = (String )val;
			if(col_id.equals("owner_id"))
				marker.owner_id = (String )val;
			if(col_id.equals("longitude"))
				marker.getAnchor().x = Double.parseDouble((String )val);
			if(col_id.equals("latitude"))
				marker.getAnchor().y = Double.parseDouble((String )val);
			if(col_id.equals("Length_1"))
			{
				marker.moveToFromStartLf(Double.parseDouble((String )val));
				marker.sendMessage_Marker_Moved();
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog( marker.getLogicalNetLayer().mainFrame , "Возможно неправельно было введино значение !!!", "", JOptionPane.ERROR_MESSAGE);
		}
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("id"))
			return new TextFieldEditor(marker.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(marker.name);
		if(col_id.equals("owner_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox("user", marker.owner_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("typ"))
			return new TextFieldEditor( marker.getTyp() );
		if(col_id.equals("path_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox(MapTransmissionPathElement.typ, marker.path_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(marker.getAnchor().x)) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(marker.getAnchor().y)) );
		if(col_id.equals("Length_1"))
			return new TextFieldEditor( String.valueOf((int)marker.getFromStartLengthLf()) );
		if(col_id.equals("Length_2"))
			return new TextFieldEditor( String.valueOf((int)marker.getFromEndLengthLf()) );
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
		for(Enumeration enum = marker.attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			cols.add(ea.getId());
		}
		return cols;
	}

	public String getPropertyName(String col_id)
	{
//		System.out.println("marker prop name");
		ElementAttribute ea = (ElementAttribute )marker.attributes.get(col_id);
		if(ea == null)
			return getColumnName(col_id);
		return ea.getName();
	}

	public String getPropertyValue(String col_id)
	{
//		System.out.println("marker prop val");
		ElementAttribute ea = (ElementAttribute )marker.attributes.get(col_id);
		if(ea == null)
			return getColumnValue(col_id);
		return ea.value;
	}

	public void setPropertyValue(String col_id, Object val)
	{
//		System.out.println("marker prop set val");
		ElementAttribute ea = (ElementAttribute )marker.attributes.get(col_id);
		if(ea == null)
			setColumnValue(col_id, val);
		else
			ea.setValue(val);
	}

	public boolean isPropertyEditable(String col_id)
	{
		ElementAttribute ea = (ElementAttribute )marker.attributes.get(col_id);
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