package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.Iterator;

import com.syrus.AMFICOM.CORBA.Scheme.PathElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;

public class PathElement extends StubResource implements Serializable
{
	public static final int SCHEME_ELEMENT = 1;
	public static final int CABLE_LINK = 2;
	public static final int LINK = 3;

	protected int type = 0;

	private static final long serialVersionUID = 01L;
	public int n;

	/**
	 * @deprecated use type
	 **/
	public boolean is_cable = false;

	public String link_id = "";
	public String thread_id = "";

	public String start_port_id = "";
	public String end_port_id = "";

	public String scheme_element_id = "";
	public String scheme_id = "";

	public PathElement()
	{
	}

	public PathElement(PathElement_Transferable transferable)
	{
		n = transferable.n;
		is_cable = transferable.is_cable;
		link_id = transferable.link_id;
		thread_id = transferable.thread_id;
	}

	public Object clone(DataSourceInterface dataSource)
	{
		PathElement pe = new PathElement();
		pe.n = n;
		pe.type = type;
		pe.link_id = link_id.length() == 0 ? "" : (String)Pool.get("clonedids", link_id);
		pe.scheme_id = scheme_id.length() == 0 ? "" : (String)Pool.get("clonedids", scheme_id);
		pe.scheme_element_id = scheme_element_id.length() == 0 ? "" : (String)Pool.get("clonedids", scheme_element_id);
		pe.thread_id = thread_id.length() == 0 ? "" : (String)Pool.get("clonedids", thread_id);
		pe.start_port_id = start_port_id.length() == 0 ? "" : (String)Pool.get("clonedids", start_port_id);
		pe.end_port_id = end_port_id.length() == 0 ? "" : (String)Pool.get("clonedids", end_port_id);
		return pe;
	}

	static public ObjectResourceSorter getSorter()
	{
		return new ObjectResourcePathSorter();
	}

	public int getType()
	{
		if (type != 0)
			return type;
		if (is_cable)
			return CABLE_LINK;
		return LINK;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append("type = ").append(type).append("\n");
		str.append("scheme_element_id = ").append(scheme_element_id).append("\n");
		str.append("link_id = ").append(link_id).append("\n");
		str.append("thread_id = ").append(thread_id).append("\n");
		if (type == SCHEME_ELEMENT)
			str.append("name = ").append(((SchemeElement)Pool.get(SchemeElement.typ, scheme_element_id)).getName()).append("\n");
		if (type == CABLE_LINK)
			str.append("name = ").append(((SchemeCableLink)Pool.get(SchemeCableLink.typ, link_id)).getName()).append("\n");
		if (type == LINK)
			str.append("name = ").append(((SchemeLink)Pool.get(SchemeLink.typ, link_id)).getName()).append("\n");
		return str.toString();
	}

	public String getName()
	{
		if (link_id.length() == 0)
		{
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, scheme_element_id);
			if (Pool.get(Equipment.typ, se.equipment_id) == null)
				return se.getName();
			else
				return ((Equipment)Pool.get(Equipment.typ, se.equipment_id)).getName();
		}
		else if (is_cable)
		{
			SchemeCableLink scl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, link_id);
			if (Pool.get(CableLink.typ, scl.cable_link_id) == null)//(scl.cable_link_id.equals(""))
				return scl.getName();
			else
				return ((CableLink)Pool.get(CableLink.typ, scl.cable_link_id)).getName();
		}
		else
		{
			SchemeLink sl = (SchemeLink)Pool.get(SchemeLink.typ, link_id);
			if (Pool.get(Link.typ, sl.link_id) == null)//(sl.link_id.equals(""))
				return sl.getName();
			else
				return ((Link)Pool.get(Link.typ, sl.link_id)).getName();
		}
	}

	public Object getTransferable()
	{
		return new PathElement_Transferable(n, is_cable, link_id, thread_id);
	}

	public ObjectResourceModel getModel()
	{
		return new PathElementModel(this);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(n);
		out.writeBoolean(is_cable);
		out.writeObject(link_id);
		out.writeObject(thread_id);
		out.writeObject(scheme_element_id);
		out.writeObject(scheme_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		n = in.readInt();
		is_cable = in.readBoolean();
		link_id = (String )in.readObject();
		thread_id = (String )in.readObject();
		scheme_element_id = (String )in.readObject();
		scheme_id = (String )in.readObject();
	}
}

class PathElementModel extends ObjectResourceModel
{
	PathElement pe;

	public PathElementModel(PathElement pe)
	{
		this.pe = pe;
	}
	public String getColumnValue(String col_id)
	{
		if(col_id.equals("thread"))
		{
			if(pe.is_cable)
			{
				SchemeCableLink sc = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				for(Iterator it = sc.cable_threads.iterator(); it.hasNext();)
				{
					SchemeCableThread sct = (SchemeCableThread)it.next();
					if (sct.getId().equals(pe.thread_id))
						return sct.getName();
				}
			}
			else
				return pe.getName();
		}
		else
		if(col_id.equals("cable"))
		{
			if(pe.is_cable)
				return pe.getName();
		}
		else
			if(col_id.equals("num"))
			{
				return String.valueOf(pe.n);
			}
		return "";
	}

	public void setColumnValue(String col_id, Object val)
	{
			if(col_id.equals("num"))
			{
				try
				{
					pe.n = Integer.parseInt((String )val);
				}
				catch (NumberFormatException ex)
				{
				}
			}
	}

}

class ObjectResourcePathSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"num", "long"},
		{"cable", "string"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column)
	{
		if (or instanceof PathElement)
		{
			PathElement pe = (PathElement)or;
			if (column.equals("cable"))
			{
				if (pe.is_cable)
					return ((SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id)).getName();
				else
					return ((SchemeLink)Pool.get(SchemeLink.typ, pe.link_id)).getName();
			}
		}
		return "";
	}

	public long getLong(ObjectResource or, String column)
	{
		if (or instanceof PathElement)
			return ((PathElement)or).n;
		return 0;
	}
}

