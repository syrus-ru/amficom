package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SchemePort extends StubResource implements Serializable
{
	public static final String typ = "schemeport";
	private static final long serialVersionUID = 01L;
	public SchemePort_Transferable transferable;

	public String id = "";
	public String name = "";
	public String port_id = "";
	public String port_type_id = "";
	public boolean is_access_port = false;
	public String access_port_id = "";
	public String access_port_type_id = "";
	public String device_id = "";
	public String link_id = "";
	public String direction_type = "";

	public Map attributes;

	public SchemePort(SchemePort_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemePort(String id)
	{
		this.id = id;
		transferable = new SchemePort_Transferable();
		attributes = new HashMap();
	}

	public String getTyp()
	{
		return typ;
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
		port_type_id = transferable.port_type_id;
		access_port_type_id = transferable.access_port_type_id;
		device_id = transferable.device_id;
		link_id = transferable.link_id;
		port_id = transferable.port_id;
		access_port_id = transferable.access_port_id;
		is_access_port = transferable.is_access_port;
		direction_type = transferable.direction_type;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.port_type_id = port_type_id;
		transferable.access_port_type_id = access_port_type_id;
		transferable.device_id = device_id;
		transferable.link_id = link_id;
		transferable.port_id = port_id;
		transferable.access_port_id = access_port_id;
		transferable.is_access_port = is_access_port;
		transferable.direction_type = direction_type;

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
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			SchemePort cloned = (SchemePort)Pool.get(SchemePort.typ, cloned_id);
			if (cloned == null)
				System.err.println("SchemePort.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		SchemePort port = new SchemePort(dataSource.GetUId(SchemePort.typ));

		port.name = name;
		port.port_type_id = port_type_id;
		port.access_port_type_id = access_port_type_id;
		port.device_id = device_id;
		port.link_id = link_id;
		port.port_id = port_id;
		port.access_port_id = access_port_id;
		port.is_access_port = is_access_port;
		port.direction_type = direction_type;

		port.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemePort.typ, port.getId(), port);
		Pool.put("clonedids", id, port.id);

		return port;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(port_type_id);
		out.writeObject(access_port_type_id);
		out.writeObject(device_id);
		out.writeObject(link_id);
		out.writeObject(port_id);
		out.writeObject(access_port_id);
		out.writeBoolean(is_access_port);
		out.writeObject(direction_type);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		port_type_id = (String )in.readObject();
		access_port_type_id = (String )in.readObject();
		device_id = (String )in.readObject();
		link_id = (String )in.readObject();
		port_id = (String )in.readObject();
		access_port_id = (String )in.readObject();
		is_access_port = in.readBoolean();
		direction_type = (String )in.readObject();
		attributes = (Map )in.readObject();

		transferable = new SchemePort_Transferable();
	}
}
