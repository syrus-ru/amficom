package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.SchemeLink_Transferable;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Network.Link;

public class SchemeLink extends StubResource implements Serializable
{
	public static final String typ = "schemelink";
	private static final long serialVersionUID = 02L;
	public SchemeLink_Transferable transferable;

	public String id = "";
	public String name = "";
	public String sourcePortId = "";
	public String targetPortId = "";
	public String linkId = "";
	public Link link;

	public String linkTypeId = "";
	private String schemeId = "";
	public String siteId = "";

	public double opticalLength = 0;
	public double physicalLength = 0;

	public Map attributes;

	public boolean alarmed = false;

	public SchemeLink(SchemeLink_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeLink(String id)
	{
		this.id = id;
		transferable = new SchemeLink_Transferable();
		attributes = new HashMap();
	}

	public String getTyp()
	{
		return typ;
	}

	public ObjectResourceModel getModel()
	{
		return new SchemeLinkModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] { "name" }, new String[] { LangModelSchematics.getString("name") });
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Schematics.UI.SchemeLinkPane";
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		sourcePortId = transferable.sourcePortId;
		targetPortId = transferable.targetPortId;
		linkTypeId = transferable.linkTypeId;
		linkId = transferable.linkId;
		siteId = transferable.siteId;
		schemeId = transferable.schemeId;

		try
		{
			opticalLength = Double.parseDouble(transferable.opticalLength);
		}
		catch (NumberFormatException ex)
		{
			opticalLength = 0;
		}
		try
		{
			physicalLength = Double.parseDouble(transferable.physicalLength);
		}
		catch (NumberFormatException ex)
		{
			physicalLength = 0;
		}
		attributes = new HashMap(transferable.attributes.length);
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.sourcePortId = sourcePortId;
		transferable.targetPortId = targetPortId;
		transferable.linkId = link == null ? linkId : link.getId();
		transferable.linkTypeId = linkTypeId;
		transferable.siteId = siteId;
		transferable.schemeId = schemeId;

		transferable.opticalLength = String.valueOf(opticalLength);
		transferable.physicalLength = String.valueOf(physicalLength);

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute)it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	public void updateLocalFromTransferable()
	{
		if (linkId.length() != 0)
			link = (Link)Pool.get(Link.typ, linkId);
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String clonedId = (String)Pool.get("clonedids", id);
		if (clonedId != null)
		{
			SchemeLink clonedLink = (SchemeLink)Pool.get(SchemeLink.typ, clonedId);
			if (clonedLink == null)
				System.err.println("SchemeLink.clone() id not found: " + clonedId);
			else
				return clonedLink;
		}

		SchemeLink link = new SchemeLink(dataSource.GetUId(SchemeLink.typ));

		link.name = name;
		link.linkId = linkId;
		link.link = this.link;
		link.linkTypeId = linkTypeId;
		link.schemeId = schemeId;
		link.siteId = siteId;
		link.sourcePortId = (String)Pool.get("clonedids", sourcePortId);
		if (link.sourcePortId == null)
			link.sourcePortId = sourcePortId;
		link.targetPortId = (String)Pool.get("clonedids", targetPortId);
		if (link.targetPortId == null)
			link.targetPortId = targetPortId;

		link.opticalLength = opticalLength;
		link.physicalLength = physicalLength;

		if (!link.sourcePortId.equals(""))
		{
			SchemePort sourcePort = (SchemePort)Pool.get(SchemePort.typ, link.sourcePortId);
			sourcePort.linkId = link.getId();
		}
		if (!link.targetPortId.equals(""))
		{
			SchemePort targetPort = (SchemePort)Pool.get(SchemePort.typ, link.targetPortId);
			targetPort.linkId = link.getId();
		}

		link.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeLink.typ, link.getId(), link);
		Pool.put("clonedids", id, link.id);
		return link;
	}

	public double getPhysicalLength()
	{
		return physicalLength;
	}

	public void setPhysicalLength(double d)
	{
		physicalLength = d;
	}

	public double getOpticalLength()
	{
		return opticalLength;
	}

	public void setOpticalLength(double d)
	{
		opticalLength = d;
	}

	public String getSchemeId()
	{
		return schemeId;
	}

	public void setSchemeId(String schemeId)
	{
		this.schemeId = schemeId;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(sourcePortId);
		out.writeObject(targetPortId);
		out.writeObject(linkId);
		Link[] l;
		if (link == null)
			l = new Link[0];
		else
		{
			l = new Link[1];
			l[0] = link;
		}

		out.writeObject(linkTypeId);
		out.writeObject(siteId);
		out.writeObject(schemeId);
		out.writeDouble(opticalLength);
		out.writeDouble(physicalLength);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		sourcePortId = (String )in.readObject();
		targetPortId = (String )in.readObject();
		linkId = (String )in.readObject();
		Link[] l = (Link[])in.readObject();
		if (l.length == 1)
			link = l[0];

		linkTypeId = (String )in.readObject();
		siteId = (String )in.readObject();
		schemeId = (String )in.readObject();
		opticalLength = in.readDouble();
		physicalLength = in.readDouble();
		attributes = (Map )in.readObject();

		transferable = new SchemeLink_Transferable();
	}
}

class SchemeLinkModel extends ObjectResourceModel
{
	SchemeLink sl;

	public SchemeLinkModel(SchemeLink schemeLink)
	{
		sl = schemeLink;
	}

	public String getColumnValue(String colId)
	{
		String s = "";
		try
		{
			if(colId.equals("optimizerRibAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
				s = ea.value;
			}
			if(colId.equals("name"))
			{
				s = sl.getName();
			}
			if(colId.equals("id"))
			{
				s = sl.getId();
			}
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Analysis");
			s = "";
		}
		System.out.println("get COL VAL for " + sl.getId() + " - retrieve " + colId + " value " + s);
		return s;
	}

	public void setColumnValue(String colId, Object obj)
	{
		try
		{
			if(colId.equals("optimizerRibAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
				ea.setValue(obj);
			}
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Analysis");
		}
	}
}

