package com.syrus.AMFICOM.Client.Resource.Map;

import javax.swing.*;
import com.ofx.geometry.SxDoublePoint;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

/// 777
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;

import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.*;
import com.syrus.AMFICOM.Client.Configure.Map.Popup.*;
import com.syrus.AMFICOM.Client.Configure.Map.Strategy.*;
import com.syrus.AMFICOM.Client.General.UI.*;

//A0A
public class MapEquipmentNodeElementModel extends MapNodeElementModel
{
	MapEquipmentNodeElement node;

	public MapEquipmentNodeElementModel(MapEquipmentNodeElement node)
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
			if(col_id.equals("type_id"))
			{
				if(node.type_id.equals("map_scheme_proto"))
					return Pool.getName(MapProtoElement.typ, node.type_id);
				return Pool.getName(ProtoElement.typ, node.element_type_id);
			}
			if(col_id.equals("element_id"))
				return node.element_id;
			if(col_id.equals("longitude"))
				return String.valueOf(MyUtil.fourdigits(node.getAnchor().x));
			if(col_id.equals("latitude"))
				return String.valueOf(MyUtil.fourdigits(node.getAnchor().y));
			if(col_id.equals("optimizeattribute"))
				return node.optimizerAttribute;
		}
	    catch(Exception e)
		{
//			System.out.println("error gettin field value - MapNodeLink");
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
			if(col_id.equals("type_id"))
				  node.element_type_id = (String )val;
			if(col_id.equals("element_id"))
				  node.element_id = (String )val;
			if(col_id.equals("longitude"))
				node.getAnchor().x = Double.parseDouble((String )val);
			if(col_id.equals("latitude"))
				node.getAnchor().y = Double.parseDouble((String )val);
			if(col_id.equals("optimizeattribute"))
				node.optimizerAttribute = (String)val;
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
			return new ObjectResourceComboBox("user", node.owner_id);
		if(col_id.equals("type_id"))
		{
			if(node.type_id.equals("map_scheme_proto"))
			{
				ObjectResourceComboBox orcb = new ObjectResourceComboBox(MapProtoElement.typ);
				Hashtable h = new Hashtable();
				MapProtoElement mpe = (MapProtoElement )Pool.get(MapProtoElement.typ, node.type_id);
				h.put((String )mpe.getId(), mpe);
				orcb.setContents(h, false);
				orcb.setSelected(node.type_id);
				return orcb;
			}
			ObjectResourceComboBox orcb = new ObjectResourceComboBox(ProtoElement.typ);
			Hashtable h = new Hashtable();
			MapProtoElement mpe = (MapProtoElement )Pool.get(MapProtoElement.typ, node.type_id);
			for(int i = 0; i < mpe.pe_ids.size(); i++)
			{
				ProtoElement pe = (ProtoElement )Pool.get(ProtoElement.typ, (String )mpe.pe_ids.get(i));
				if(pe != null)
					h.put((String )mpe.pe_ids.get(i), pe);
			}
			orcb.setContents(h, false);
//			if(node.element_type_id != null && !node.element_type_id.equals(""))
				orcb.setSelected(node.element_type_id);
			return orcb;
//			return new ObjectResourceComboBox("mapequipmentproto", node.type_id);
		}
		if(col_id.equals("element_id"))
		{
			if(node.type_id.equals("map_scheme_proto"))
			{
				Scheme scheme = (Scheme )Pool.get(Scheme.typ, node.getMapContext().scheme_id);
				ObjectResourceComboBox orcb =  new ObjectResourceComboBox(SchemeElement.typ, node.element_id);
				Hashtable ht = Pool.getHash(SchemeElement.typ);
				Hashtable ht2 = new Hashtable();
				for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
				{
					SchemeElement se = (SchemeElement )enum.nextElement();
					if(se.scheme_id != null && !se.scheme_id.equals(""))
						ht2.put(se.getId(), se);
				}
				orcb.setContents(ht2, true);
				orcb.setSelected(node.element_id);
				return orcb;
			}
			Scheme scheme = (Scheme )Pool.get(Scheme.typ, node.getMapContext().scheme_id);
			ObjectResourceComboBox orcb =  new ObjectResourceComboBox(SchemeElement.typ, node.element_id);
			Hashtable ht = Pool.getHash(SchemeElement.typ);
			Hashtable ht2 = new Hashtable();
			for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
			{
				SchemeElement se = (SchemeElement )enum.nextElement();
//				if(scheme.elements.contains(se))
				if(scheme.getTopologicalElements().contains(se))
					if(se.proto_element_id.equals(node.element_type_id))
						ht2.put(se.getId(), se);
			}
			orcb.setContents(ht2, true);
			orcb.setSelected(node.element_id);
			return orcb;
		}
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

	public PropertiesPanel getPropertyPane()
	{
		return new MapEquipmentPane(node);
	}
	
	public Vector getPropertyColumns()
	{
		Vector cols = new Vector();
		Vector cols2 = super.getPropertyColumns();
//		cols.add("id");
		cols.add("name");
//		cols.add("owner_id");
		cols.add("type_id");
		cols.add("element_id");
		cols.add("longitude");
		cols.add("latitude");
		cols.addAll(cols2);
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
		if(col_id.equals("type_id"))
			return "Тип";
		if(col_id.equals("element_id"))
			return "Элемент схемы";
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
		if(col_id.equals("owner_id"))
			return false;
		if(col_id.equals("type_id"))
			return false;
		if(col_id.equals("element_id"))
			return false;
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
