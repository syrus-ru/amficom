package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Schematics.UI.*;

public class SchemeLink extends ObjectResource implements Serializable
{
	public static final String typ = "schemelink";
	private static final long serialVersionUID = 01L;
	public SchemeLink_Transferable transferable;

	public String id = "";
	public String name = "";
	public String source_port_id = "";
	public String target_port_id = "";
	public String link_id = "";
	public String link_type_id = "";

	public double optical_length = 0;
	public double physical_length = 0;

	public Hashtable attributes = new Hashtable();

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

	public static PropertiesPanel getPropertyPane()
	{
		return new SchemeLinkPane();
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
		source_port_id = transferable.source_port_id;
		target_port_id = transferable.target_port_id;
		link_type_id = transferable.link_type_id;
		link_id = transferable.link_id;

		try
		{
			optical_length = Double.parseDouble(transferable.optical_length);
		}
		catch (NumberFormatException ex)
		{
			optical_length = 0;
		}
		try
		{
			physical_length = Double.parseDouble(transferable.physical_length);
		}
		catch (NumberFormatException ex)
		{
			physical_length = 0;
		}

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.source_port_id = source_port_id;
		transferable.target_port_id = target_port_id;
		transferable.link_id = link_id;
		transferable.link_type_id = link_type_id;

		transferable.optical_length = String.valueOf(optical_length);
		transferable.physical_length = String.valueOf(physical_length);

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Enumeration e = attributes.elements(); e.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute)e.nextElement();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			SchemeLink cloned_link = (SchemeLink)Pool.get(SchemeLink.typ, cloned_id);
			if (cloned_link == null)
				System.err.println("SchemeLink.clone() id not found: " + cloned_id);
			else
				return cloned_link;
		}

		SchemeLink link = new SchemeLink(dataSource.GetUId(SchemeLink.typ));

		link.name = name;
		link.link_id = link_id;
		link.link_type_id = link_type_id;
		link.source_port_id = (String)Pool.get("clonedids", source_port_id);
		if (link.source_port_id == null)
			link.source_port_id = source_port_id;
		link.target_port_id = (String)Pool.get("clonedids", target_port_id);
		if (link.target_port_id == null)
			link.target_port_id = target_port_id;

		link.optical_length = optical_length;
		link.physical_length = physical_length;

		if (!link.source_port_id.equals(""))
		{
			SchemePort source_port = (SchemePort)Pool.get(SchemePort.typ, link.source_port_id);
			source_port.link_id = link.getId();
		}
		if (!link.target_port_id.equals(""))
		{
			SchemePort target_port = (SchemePort)Pool.get(SchemePort.typ, link.target_port_id);
			target_port.link_id = link.getId();
		}

		link.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeLink.typ, link.getId(), link);
		Pool.put("clonedids", id, link.id);
		return link;
	}

	public double getPhysicalLength()
	{
		if (!link_id.equals(""))
		{
			Link link = (Link)Pool.get(Link.typ, link_id);
			if (link != null)
				return link.physical_length;
		}
		return physical_length;
	}

	public void setPhysicalLength(double d)
	{
		physical_length = d;
		if (!link_id.equals(""))
		{
			Link link = (Link)Pool.get(Link.typ, link_id);
			if (link != null)
				link.physical_length = d;
		}
	}

	public double getOpticalLength()
	{
		if (!link_id.equals(""))
		{
			Link link = (Link)Pool.get(Link.typ, link_id);
			if (link != null)
				return link.optical_length;
		}
		return optical_length;
	}

	public void setOpticalLength(double d)
	{
		optical_length = d;
		if (!link_id.equals(""))
		{
			Link link = (Link)Pool.get(Link.typ, link_id);
			if (link != null)
				link.optical_length = d;
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(source_port_id);
		out.writeObject(target_port_id);
		out.writeObject(link_id);
		out.writeObject(link_type_id);
		out.writeDouble(optical_length);
		out.writeDouble(physical_length);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		source_port_id = (String )in.readObject();
		target_port_id = (String )in.readObject();
		link_id = (String )in.readObject();
		link_type_id = (String )in.readObject();
		optical_length = in.readDouble();
		physical_length = in.readDouble();
		attributes = (Hashtable )in.readObject();

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

	public String getColumnValue(String col_id)
	{
		String s = "";
		try
		{
			if(col_id.equals("optimizerRibAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
				s = ea.value;
			}
			if(col_id.equals("name"))
			{
				s = sl.getName();
			}
			if(col_id.equals("id"))
			{
				s = sl.getId();
			}
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Analysis");
			s = "";
		}
		System.out.println("get COL VAL for " + sl.getId() + " - retrieve " + col_id + " value " + s);
		return s;
	}

	public void setColumnValue(String col_id, Object obj)
	{
		try
		{
			if(col_id.equals("optimizerRibAttribute"))
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

