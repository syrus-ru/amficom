package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.Iterator;

import com.syrus.AMFICOM.CORBA.Scheme.PathElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;

public class PathElement extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;

	public static final int SCHEME_ELEMENT = 1;
	public static final int CABLE_LINK = 2;
	public static final int LINK = 3;

	protected int type = 0;
	public int n;

	private SchemeLink scheme_link;
	private SchemeCableLink scheme_cable_link;
	private SchemeElement scheme_element;
	private String obj_id = "";

	public String thread_id = "";
	public String start_port_id = "";
	public String end_port_id = "";
//	public String scheme_element_id = "";
	public String scheme_id = "";

	PathElement_Transferable transferable;

	public PathElement()
	{
	}

	public PathElement(PathElement_Transferable transferable)
	{
		this.transferable = transferable;
		n = transferable.n;
		type = transferable.is_cable ? CABLE_LINK : LINK;
		obj_id = transferable.link_id;
	}

	public void updateLocalFromTransferable()
	{
		setObject(type, obj_id);
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}

/*
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
*/
	static public ObjectResourceSorter getSorter()
	{
		return new ObjectResourcePathSorter();
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getName()
	{
		switch (type)
		{
			case PathElement.SCHEME_ELEMENT:
				if (scheme_element.equipment_id.length() == 0)
					return scheme_element.getName();
				else
					return ((Equipment)Pool.get("kisequipment", scheme_element.equipment_id)).getName();
			case PathElement.CABLE_LINK:
				if (scheme_cable_link.cable_link_id.length() == 0)
					return scheme_cable_link.getName();
				else
					return ((CableLink)Pool.get(CableLink.typ, scheme_cable_link.cable_link_id)).getName();
			case PathElement.LINK:
				if (scheme_link.link_id.length() == 0)
					return scheme_link.getName();
				else
					return ((Link)Pool.get(Link.typ, scheme_link.link_id)).getName();
			default:
				return "";
		}
	}

	public String getObjectId()
	{
		switch (type)
		{
			case PathElement.SCHEME_ELEMENT:
					return scheme_element.getId();
			case PathElement.CABLE_LINK:
					return scheme_cable_link.getId();
			case PathElement.LINK:
					return scheme_link.getId();
			default:
				return "";
		}
	}

	public void setObject(int type, String obj_id)
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				scheme_cable_link = (SchemeCableLink) Pool.get(SchemeCableLink.typ, obj_id);
				break;
			case PathElement.LINK:
				scheme_link = (SchemeLink) Pool.get(SchemeLink.typ, obj_id);
				break;
			case PathElement.SCHEME_ELEMENT:
				scheme_element = (SchemeElement) Pool.get(SchemeElement.typ, obj_id);
		}
	}

	public double getOpticalLength()
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				return scheme_cable_link.getOpticalLength();
			case PathElement.LINK:
				return scheme_link.getOpticalLength();
			default:
				return 0;
		}
	}

	public void setOpticalLength(double d)
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				scheme_cable_link.setOpticalLength(d);
				break;
			case PathElement.LINK:
				scheme_link.setOpticalLength(d);
		}
	}

	public double getPhysicalLength()
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				return scheme_cable_link.getPhysicalLength();
			case PathElement.LINK:
				return scheme_link.getPhysicalLength();
			default:
				return 0;
		}
	}

	public void setPhysicalLength(double d)
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				scheme_cable_link.setPhysicalLength(d);
				break;
			case PathElement.LINK:
				scheme_link.setPhysicalLength(d);
		}
	}

	public double getKu()
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				return scheme_cable_link.getOpticalLength() / scheme_cable_link.getPhysicalLength();
			case PathElement.LINK:
				return scheme_link.getOpticalLength() / scheme_link.getPhysicalLength();
			default:
				return 1;
		}
	}

	public Object getTransferable()
	{
		return new PathElement_Transferable(n, type == CABLE_LINK, "", thread_id);
	}

//	public ObjectResourceModel getModel()
//	{
//		return new PathElementModel(this);
//	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(n);
		out.writeInt(type);
		out.writeObject(obj_id);
		out.writeObject(thread_id);
		out.writeObject(scheme_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		n = in.readInt();
		type = in.readInt();
		obj_id = (String )in.readObject();
		thread_id = (String )in.readObject();
		scheme_id = (String )in.readObject();
	}

	public SchemeCableLink getSchemeCableLink()
	{
		return scheme_cable_link;
	}

	public void setSchemeCableLink(SchemeCableLink scheme_cable_link)
	{
		this.scheme_cable_link = scheme_cable_link;
	}

	public SchemeElement getSchemeElement()
	{
		return scheme_element;
	}

	public void setSchemeElement(SchemeElement scheme_element)
	{
		this.scheme_element = scheme_element;
	}

	public SchemeLink getSchemeLink()
	{
		return scheme_link;
	}

	public void setSchemeLink(SchemeLink scheme_link)
	{
		this.scheme_link = scheme_link;
	}

	public ObjectResource getSourcePort()
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				return (SchemeCablePort)Pool.get(SchemeCablePort.typ, start_port_id);
			case PathElement.LINK:
				return (SchemePort)Pool.get(SchemePort.typ, start_port_id);
			case PathElement.SCHEME_ELEMENT:
				SchemePort port = (SchemePort)Pool.get(SchemePort.typ, start_port_id);
				return port == null ? (ObjectResource)Pool.get(SchemeCablePort.typ, start_port_id) : port;
			default:
				return null;
		}
	}

	public ObjectResource getTargetPort()
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				return (SchemeCablePort) Pool.get(SchemeCablePort.typ, end_port_id);
			case PathElement.LINK:
				return (SchemePort) Pool.get(SchemePort.typ, end_port_id);
			case PathElement.SCHEME_ELEMENT:
				SchemePort port = (SchemePort) Pool.get(SchemePort.typ, end_port_id);
				return port == null ? (ObjectResource) Pool.get(SchemeCablePort.typ, end_port_id) : port;
			default:
				return null;
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
				return pe.getName();
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

