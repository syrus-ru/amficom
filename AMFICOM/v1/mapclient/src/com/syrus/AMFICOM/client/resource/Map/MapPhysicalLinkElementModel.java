package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Map.Popup.*;
import com.syrus.AMFICOM.Client.Map.Strategy.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.UI.TrueFalseComboBox;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

//A0A
public class MapPhysicalLinkElementModel extends MapLinkElementModel
{
	MapPhysicalLinkElement link;
	public MapPhysicalLinkElementModel(MapPhysicalLinkElement link)
	{
		super(link);
		this.link = link;
	}

	public String getColumnValue(String col_id)
	{
		try
		{
			if(col_id.equals("id"))
				return link.id;
			if(col_id.equals("name"))
				return link.name;
			if(col_id.equals("owner_id"))
				return Pool.getName("user", link.owner_id);
			if(col_id.equals("type_id"))
				return Pool.getName(MapPhysicalLinkProtoElement.typ, link.type_id);
			if(col_id.equals("link_id"))
				return link.LINK_ID;
//				return Pool.getName(SchemeCableLink.typ, link.LINK_ID);
			if(col_id.equals("length"))
				return String.valueOf( MiscUtil.fourdigits(link.getSizeInDoubleLt()));
		}
		catch(Exception e)
		{
//		  System.out.println("error gettin field value - MapPhysicalLink");
		}
		return "";
	}

	public void setColumnValue(String col_id, Object val)
	{
		if(col_id.equals("id"))
			link.setId((String )val);
		if(col_id.equals("name"))
			link.name = (String)val;
		if(col_id.equals("owner_id"))
			link.owner_id = (String)val;
		if(col_id.equals("type_id"))
		{
			link.type_id = (String )val;
			MapPhysicalLinkProtoElement mplpe =
					(MapPhysicalLinkProtoElement )Pool.get(
							MapPhysicalLinkProtoElement.typ,
							link.type_id);
			link.attributes = (Hashtable )ResourceUtil.copyAttributes(null, mplpe.attributes);
		}
		if(col_id.equals("link_id"))
		{
			link.LINK_ID = (String )val;
		}
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("id"))
			return new TextFieldEditor(link.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(link.name);
		if(col_id.equals("length"))
			return new TextFieldEditor(String.valueOf( (int) link.getSizeInDoubleLt()));
	//		if(col_id.equals("codename"))
	//			return new JTextField(codename);
		if(col_id.equals("owner_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox("user", link.owner_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("type_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox("maplinkproto", link.type_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("link_id"))
		{
			MapPhysicalLinkProtoElement mplpe =
					(MapPhysicalLinkProtoElement )Pool.get(
							MapPhysicalLinkProtoElement.typ,
							link.type_id);
			if(mplpe == null)
				return null;

			Scheme scheme = (Scheme )Pool.get(Scheme.typ, link.getMapContext().scheme_id);
			ObjectResourceComboBox orcb =  new ObjectResourceComboBox(SchemeCableLink.typ, link.LINK_ID);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			Hashtable ht = Pool.getHash(SchemeCableLink.typ);
			Hashtable ht2 = new Hashtable();
			for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
			{
				SchemeCableLink se = (SchemeCableLink )enum.nextElement();
//				if(scheme.cablelinks.contains(se))
				if(scheme.getTopologicalCableLinks().contains(se))
					if(mplpe.cabletype_ids.contains(se.cable_link_type_id))
						ht2.put(se.getId(), se);
			}
			orcb.setContents(ht2, true);
			orcb.setSelected(link.LINK_ID);
			return orcb;
		}
		return null;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}

	public PropertiesPanel getPropertyPane()
	{
		return new MapLinkPane(link);
	}

	List cols = new LinkedList();
	{
//		cols.add("id");
		cols.add("name");
		cols.add("length");
		cols.add("physical_length");
		cols.add("optical_length");
//		cols.add("owner_id");
		cols.add("type_id");
		cols.add("link_id");
	}

	public List getPropertyColumns()
	{
		List retcols = new LinkedList();
		List cols2 = super.getPropertyColumns();
		retcols.addAll(cols);
		retcols.addAll(cols2);
		return retcols;
	}

	public String getPropertyName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("length"))
			return "Топологическая длина";
		if(col_id.equals("physical_length"))
			return "Строительная длина";
		if(col_id.equals("optical_length"))
			return "Оптическая длина";
		if(col_id.equals("owner_id"))
			return "Владелец";
		if(col_id.equals("type_id"))
			return "Тип";
		if(col_id.equals("link_id"))
			return "Кабель";
		return super.getPropertyName(col_id);
	}

	public String getPropertyValue(String col_id)
	{
		String value = "";
		if(col_id.equals("physical_length"))
		{
			if(link.LINK_ID == null || link.LINK_ID.equals(""))
				return value;
			SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
			if(scl == null)
				return value;
			value = String.valueOf(scl.getPhysicalLength());
/*				
			ElementAttribute ea = (ElementAttribute )scl.attributes.get("length");
			if(ea != null)
				value = ea.value;
			if(scl.cable_link_id == null || scl.cable_link_id.equals(""))
				return value;
			CableLink cl = (CableLink )Pool.get(CableLink.typ, scl.cable_link_id);
			if(cl == null)
				return value;
			Characteristic ch = (Characteristic )cl.characteristics.get("cable_length");
			if(ch != null)
				value = ch.value;
*/				
			return value;
		}
		if(col_id.equals("optical_length"))
		{
//			value = getPropertyValue("physical_length");
			if(link.LINK_ID == null || link.LINK_ID.equals(""))
				return value;
			SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
			if(scl == null)
				return value;
			value = String.valueOf(scl.getOpticalLength());
/*
			if(scl.cable_link_id == null || scl.cable_link_id.equals(""))
				return value;
			CableLink cl = (CableLink )Pool.get(CableLink.typ, scl.cable_link_id);
			if(cl == null)
				return value;
			Characteristic ch = (Characteristic )cl.characteristics.get("optical_length");
			if(ch != null)
				value = ch.value;
*/
			return value;
		}
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
		if(col_id.equals("physical_length"))
		{
			if(link.LINK_ID == null || link.LINK_ID.equals(""))
				return;
			SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);

			if(scl != null)
			{
				try
				{
					double d = Double.parseDouble((String )val);
					scl.setPhysicalLength(d);
				}
				catch(Exception ex)
				{
				}
			}
/*
			if(scl != null && scl.cable_link_id != null && !scl.cable_link_id.equals(""))
			{
				CableLink cl = (CableLink )Pool.get(CableLink.typ, scl.cable_link_id);
				if(cl != null)
				{
					Characteristic ch = (Characteristic )cl.characteristics.get("cable_length");
					if(ch != null)
					{
						ch.value = (String )val;
						return;
					}
				}
			}
			ElementAttribute ea = (ElementAttribute )scl.attributes.get("length");
			if(ea != null)
				ea.value = (String )val;
*/
			return;
		}
		if(col_id.equals("optical_length"))
		{
			if(link.LINK_ID == null || link.LINK_ID.equals(""))
				return;
			SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);

			if(scl != null)
			{
				try
				{
					double d = Double.parseDouble((String )val);
					scl.setOpticalLength(d);
				}
				catch(Exception ex)
				{
				}
			}
/*
			if(scl != null && scl.cable_link_id != null && !scl.cable_link_id.equals(""))
			{
				CableLink cl = (CableLink )Pool.get(CableLink.typ, scl.cable_link_id);
				if(cl != null)
				{
					Characteristic ch = (Characteristic )cl.characteristics.get("optical_length");
					if(ch != null)
					{
						ch.value = (String )val;
						return;
					}
				}
			}
*/
			return;
		}
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
		if(col_id.equals("optical_length"))
			return true;
		if(col_id.equals("physical_length"))
			return true;
		if(col_id.equals("length"))
			return false;
		if(col_id.equals("owner_id"))
			return false;
		if(col_id.equals("type_id"))
			return false;
		if(col_id.equals("link_id"))
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