package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Map.Popup.PhysicalNodeElementPopupMenu;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

//A0A
public class MapPhysicalNodeElementModel extends MapNodeElementModel 
{
	MapPhysicalNodeElement node;

	public MapPhysicalNodeElementModel(MapPhysicalNodeElement node)
	{
		super(node);
		this.node = node;
	}

	public String getColumnValue(String col_id)
	{
		try
		{
			if(col_id.equals("id"))
				return node.getId();
			if(col_id.equals("name"))
				return node.getName();
			if(col_id.equals("owner_id"))
				return Pool.getName("user", node.owner_id);
			if(col_id.equals("typ"))
				return node.getTyp();
			if(col_id.equals("longitude"))
				return String.valueOf(MyUtil.fourdigits(node.getAnchor().x)) ;
			if(col_id.equals("latitude"))
				return String.valueOf(MyUtil.fourdigits(node.getAnchor().y));
			if(col_id.equals("type_id"))
				return LangModel.getString("node" + MapPhysicalNodeElement.typ);
		}
		catch(Exception e)
		{
//		  System.out.println("error gettin field value - MapNodeLink");
		}
		return "";
	}

	public void setColumnValue(String col_id, Object val)
	{
		try
		{
			if(col_id.equals("id"))
				node.setId((String )val);
			if(col_id.equals("name"))
				node.name = (String)val;
			if(col_id.equals("owner_id"))
				node.owner_id = (String)val;
			if(col_id.equals("longitude"))
				node.getAnchor().x = Double.parseDouble((String)val);
			if(col_id.equals("latitude"))
				node.getAnchor().y = Double.parseDouble((String)val);
		}
		catch(Exception e)
		{
		  JOptionPane.showMessageDialog( node.getLogicalNetLayer().mainFrame , "Возможно неправельно было введино значение !!!", "", JOptionPane.ERROR_MESSAGE);
		}
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("id"))
			return new TextFieldEditor(node.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(node.name);
		if(col_id.equals("owner_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox("user", node.owner_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("typ"))
			return new TextFieldEditor( node.getTyp() );
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(node.getAnchor().x)) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(node.getAnchor().y)) );
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
		cols.add("longitude");
		cols.add("latitude");
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
		if(col_id.equals("longitude"))
			return "Долгота";
		if(col_id.equals("latitude"))
			return "Широта";
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
		if(col_id.equals("longitude"))
			return false;
		if(col_id.equals("latitude"))
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