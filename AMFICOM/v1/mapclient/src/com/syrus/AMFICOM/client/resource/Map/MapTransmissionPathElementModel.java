package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

/// 777
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.UI.TrueFalseComboBox;
import com.syrus.AMFICOM.Client.Map.Strategy.*;
import com.syrus.AMFICOM.Client.Map.Popup.*;
import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Map.UI.MapPathGeneralPanel;

//A0A
public class MapTransmissionPathElementModel extends MapLinkElementModel
{
	MapTransmissionPathElement path;
	
	public MapTransmissionPathElementModel(MapTransmissionPathElement path)
	{
		super(path);
		this.path = path;
	}

	public PropertiesPanel getPropertyPane()
	{
//		return new MapPathGeneralPanel(path);
		return new MapPathPane(path);
	}

	public String getColumnValue(String col_id)
	{
		try
		{
			if(col_id.equals("id"))
				return path.id;
			if(col_id.equals("name"))
				return path.name;
			if(col_id.equals("owner_id"))
				return Pool.getName("user", path.owner_id);
			if(col_id.equals("type_id"))
				return Pool.getName(MapTransmissionPathProtoElement.typ, path.type_id);
			if(col_id.equals("path_id"))
				return path.PATH_ID;
			if(col_id.equals("length"))
				return String.valueOf( (int)path.getSizeInDoubleLt());
			if(col_id.equals("startnode"))
				return path.startNode.getId();;
			if(col_id.equals("endnode"))
				return path.endNode.getId();
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
			path.setId((String )val);
		if(col_id.equals("name"))
			path.name = (String )val;
		if(col_id.equals("owner_id"))
			path.owner_id = (String )val;
		if(col_id.equals("type_id"))
		{
			path.type_id = (String )val;
			MapTransmissionPathProtoElement mppe =
					(MapTransmissionPathProtoElement )Pool.get(
							MapTransmissionPathProtoElement.typ,
							path.type_id);
			path.attributes = (Hashtable )ResourceUtil.copyAttributes(null, mppe.attributes);
	//		type_id = (String )mppe.ID;
		}
		if(col_id.equals("path_id"))
			path.PATH_ID = (String )val;
	}

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("id"))
			return new TextFieldEditor(path.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(path.name);
		if(col_id.equals("type_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox("mappathproto", path.type_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("path_id"))
		{
			MapTransmissionPathProtoElement mtppe =
					(MapTransmissionPathProtoElement )Pool.get(
							MapTransmissionPathProtoElement.typ,
							path.type_id);
			if(mtppe == null)
				return null;

			Scheme scheme = (Scheme )Pool.get(Scheme.typ, path.getMapContext().scheme_id);
			ObjectResourceComboBox orcb =  new ObjectResourceComboBox(SchemePath.typ, path.PATH_ID);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			Hashtable ht = Pool.getHash(SchemePath.typ);
			Hashtable ht2 = new Hashtable();
			for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
			{
				SchemePath sp = (SchemePath )enum.nextElement();
//				if(scheme.paths.contains(sp))
				if(scheme.getTopologicalPaths().contains(sp))
					if(mtppe.pathtype_ids.contains(sp.type_id))
						ht2.put(sp.getId(), sp);
			}
			orcb.setContents(ht2, true);
			orcb.setSelected(path.PATH_ID);
			return orcb;
		}
//			return new TextFieldEditor(path.PATH_ID);
		if(col_id.equals("owner_id"))
		{
			ObjectResourceComboBox orcb = new ObjectResourceComboBox("user", path.owner_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
			return orcb;
		}
		if(col_id.equals("length"))
			return new TextFieldEditor(String.valueOf( MiscUtil.fourdigits(path.getSizeInDoubleLt())));
		if(col_id.equals("startnode"))
		{
			MapContext mc = (MapContext )Pool.get(MapContext.typ, path.mapContextID);
			Hashtable ht2 = new Hashtable();
			for(Iterator it = mc.getNodes().iterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				ht2.put(or.getId(), or);
			}
			ObjectResourceComboBox orcb =  new ObjectResourceComboBox(ht2, path.startNode_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
//			orcb.setContents(ht2, true);
//			orcb.setSelected(path.startNode);
			return orcb;
		}
		if(col_id.equals("endnode"))
		{
			MapContext mc = (MapContext )Pool.get(MapContext.typ, path.mapContextID);
			Hashtable ht2 = new Hashtable();
			for(Iterator it = mc.getNodes().iterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				ht2.put(or.getId(), or);
			}
			ObjectResourceComboBox orcb =  new ObjectResourceComboBox(ht2, path.endNode_id);
			orcb.setFontSize(ObjectResourceComboBox.SMALL_FONT);
//			orcb.setContents(ht2, true);
//			orcb.setSelected(path.endNode);
			return orcb;
		}
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
		cols.add("length");
		cols.add("physical_length");
		cols.add("optical_length");
//		cols.add("owner_id");
		cols.add("type_id");
		cols.add("path_id");
		cols.add("startnode");
		cols.add("endnode");
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
		if(col_id.equals("path_id"))
			return "Элемент схемы";
		if(col_id.equals("startnode"))
			return "Начальный узел";
		if(col_id.equals("endnode"))
			return "Конечный узел";
		return super.getPropertyName(col_id);
	}

	public String physical_length()
	{
		SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
		if(sp != null)
			return String.valueOf(sp.getPhysicalLength());
		return "0";
	}

	public String physical_length1()
	{
		double length = 0;
		double d1;
		String l1;
		Iterator e = path.getMapContext().getPhysicalLinksInTransmissiionPath(path.getId()).iterator();
		while( e.hasNext())
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
			l1 = link.getModel().getPropertyValue("physical_length");
			try
			{
				d1 = Double.parseDouble(l1);
				length = length + d1;
			}
			catch(Exception ex)
			{
				System.out.println("Не задана строительная длина линии " + link.getName() + " [" +link.getId() + "]");
			}
			
		}
		return String.valueOf(length);
	}

	public String optical_length()
	{
		SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
		if(sp != null)
			return String.valueOf(sp.getOpticalLength());
		return "0";
	}

	public String optical_length1()
	{
		double length = 0;
		double d1;
		String l1;
		Iterator e = path.getMapContext().getPhysicalLinksInTransmissiionPath(path.getId()).iterator();
		while( e.hasNext())
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
			l1 = link.getModel().getPropertyValue("optical_length");
//			length = length + Double.parseDouble(l1);
			try
			{
				d1 = Double.parseDouble(l1);
				length = length + d1;
			}
			catch(Exception ex)
			{
				System.out.println("Не задана оптическая длина линии " + link.getName() + " [" +link.getId() + "]");
			}
		}
		return String.valueOf(length);
	}

	public String getPropertyValue(String col_id)
	{
		if(col_id.equals("physical_length"))
			return physical_length();
		if(col_id.equals("optical_length"))
			return optical_length();
			
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
		if(col_id.equals("type_id"))
			return false;
		if(col_id.equals("path_id"))
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