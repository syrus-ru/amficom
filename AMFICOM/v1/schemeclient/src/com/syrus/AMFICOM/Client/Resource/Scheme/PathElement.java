package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;

import com.syrus.AMFICOM.CORBA.Scheme.PathElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;

public class PathElement extends StubResource implements Serializable
{
	private static final long serialVersionUID = 03L;

	public static final int SCHEME_ELEMENT = 1;
	public static final int CABLE_LINK = 2;
	public static final int LINK = 3;

	protected int type = 0;
	public int n;

	private SchemeLink scheme_link;
	private SchemeCableLink scheme_cable_link;
	private SchemeElement scheme_element;
	private String elementId = "";

	public String threadId = "";
	public String startPortId = "";
	public String endPortId = "";
	public String schemeId = "";

	PathElement_Transferable transferable;

	public PathElement()
	{
	}

	public PathElement(PathElement_Transferable transferable)
	{
		n = transferable.n;
		type = transferable.type;
		elementId = transferable.elementId;
		threadId = transferable.threadId;
		startPortId = transferable.startPortId;
		endPortId = transferable.endPortId;
		schemeId = transferable.schemeId;
	}

	public void updateLocalFromTransferable()
	{
		setObject(type, elementId);
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
		pe.linkId = linkId.length() == 0 ? "" : (String)Pool.get("clonedids", linkId);
		pe.schemeId = schemeId.length() == 0 ? "" : (String)Pool.get("clonedids", schemeId);
		pe.scheme_elementId = scheme_elementId.length() == 0 ? "" : (String)Pool.get("clonedids", scheme_elementId);
		pe.threadId = threadId.length() == 0 ? "" : (String)Pool.get("clonedids", threadId);
		pe.startPortId = startPortId.length() == 0 ? "" : (String)Pool.get("clonedids", startPortId);
		pe.endPortId = endPortId.length() == 0 ? "" : (String)Pool.get("clonedids", endPortId);
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
				return scheme_element.getName();
			case PathElement.CABLE_LINK:
				return scheme_cable_link.getName();
			case PathElement.LINK:
				return scheme_link.getName();
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

	public void setObject(int type, String objId)
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				scheme_cable_link = (SchemeCableLink) Pool.get(SchemeCableLink.typ, objId);
				break;
			case PathElement.LINK:
				scheme_link = (SchemeLink) Pool.get(SchemeLink.typ, objId);
				break;
			case PathElement.SCHEME_ELEMENT:
				scheme_element = (SchemeElement) Pool.get(SchemeElement.typ, objId);
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
		PathElement_Transferable transferable = new PathElement_Transferable();
		transferable.n = n;
		transferable.type = type;
		transferable.elementId = elementId;
		transferable.threadId = threadId;
		transferable.startPortId = startPortId;
		transferable.endPortId = endPortId;
		transferable.schemeId = schemeId;
		return transferable;
	}

//	public ObjectResourceModel getModel()
//	{
//		return new PathElementModel(this);
//	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(n);
		out.writeInt(type);
		out.writeObject(elementId);
		out.writeObject(threadId);
		out.writeObject(startPortId);
		out.writeObject(endPortId);
		out.writeObject(schemeId);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		n = in.readInt();
		type = in.readInt();
		elementId = (String )in.readObject();
		threadId = (String) in.readObject();
		startPortId = (String) in.readObject();
		endPortId = (String) in.readObject();
		schemeId = (String) in.readObject();
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
				return (SchemeCablePort)Pool.get(SchemeCablePort.typ, startPortId);
			case PathElement.LINK:
				return (SchemePort)Pool.get(SchemePort.typ, startPortId);
			case PathElement.SCHEME_ELEMENT:
				SchemePort port = (SchemePort)Pool.get(SchemePort.typ, startPortId);
				return port == null ? (ObjectResource)Pool.get(SchemeCablePort.typ, startPortId) : port;
			default:
				return null;
		}
	}

	public ObjectResource getTargetPort()
	{
		switch (type)
		{
			case PathElement.CABLE_LINK:
				return (SchemeCablePort) Pool.get(SchemeCablePort.typ, endPortId);
			case PathElement.LINK:
				return (SchemePort) Pool.get(SchemePort.typ, endPortId);
			case PathElement.SCHEME_ELEMENT:
				SchemePort port = (SchemePort) Pool.get(SchemePort.typ, endPortId);
				return port == null ? (ObjectResource) Pool.get(SchemeCablePort.typ, endPortId) : port;
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

