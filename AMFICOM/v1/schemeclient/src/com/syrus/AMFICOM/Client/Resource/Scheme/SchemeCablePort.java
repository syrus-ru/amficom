package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SchemeCablePort extends StubResource implements Serializable
{
	public static final  String typ = "schemecableport";
	private static final long serialVersionUID = 01L;
	public SchemeCablePort_Transferable transferable;

	public String id = "";
	public String name = "";
	public String cable_port_id = "";
	public String cable_port_type_id = "";
	public String device_id = "";
	public String cable_link_id = "";
	public boolean is_access_port = false;
	public String access_port_id = "";
	public String access_port_type_id = "";
	public String direction_type = "";

	public Map attributes = new HashMap();

	public SchemeCablePort(SchemeCablePort_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeCablePort(String id)
	{
		this.id = id;
		transferable = new SchemeCablePort_Transferable();
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

	public Object getTransferable()
	{
		return transferable;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		cable_port_id = transferable.cable_port_id;
		cable_port_type_id = transferable.cable_port_type_id;
		device_id = transferable.device_id;
		cable_link_id = transferable.cable_link_id;
		is_access_port = transferable.is_access_port;
		access_port_id = transferable.access_port_id;
		access_port_type_id = transferable.access_port_type_id;
		direction_type = transferable.direction_type;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.cable_port_id = cable_port_id;
		transferable.cable_port_type_id = cable_port_type_id;
		transferable.device_id = device_id;
		transferable.cable_link_id = cable_link_id;
		transferable.is_access_port = is_access_port;
		transferable.access_port_id = access_port_id;
		transferable.access_port_type_id = access_port_type_id;
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
			SchemeCablePort cloned = (SchemeCablePort)Pool.get(SchemeCablePort.typ, cloned_id);
			if (cloned == null)
				System.err.println("SchemeCablePort.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		SchemeCablePort port = new SchemeCablePort(dataSource.GetUId(SchemeCablePort.typ));

		port.name = name;
		port.cable_port_id = cable_port_id;
		port.cable_port_type_id = cable_port_type_id;
		port.device_id = device_id;
		port.cable_link_id = cable_link_id;
		port.is_access_port = is_access_port;
		port.access_port_id = access_port_id;
		port.access_port_type_id = access_port_type_id;
		port.direction_type = direction_type;

		port.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeCablePort.typ, port.getId(), port);
		Pool.put("clonedids", id, port.id);
		return port;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(cable_port_id);
		out.writeObject(cable_port_type_id);
		out.writeObject(device_id);
		out.writeObject(cable_link_id);
		out.writeBoolean(is_access_port);
		out.writeObject(access_port_id);
		out.writeObject(access_port_type_id);
		out.writeObject(direction_type);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		cable_port_id = (String )in.readObject();
		cable_port_type_id = (String )in.readObject();
		device_id = (String )in.readObject();
		cable_link_id = (String )in.readObject();
		is_access_port = in.readBoolean();
		access_port_id = (String )in.readObject();
		access_port_type_id = (String )in.readObject();
		direction_type = (String )in.readObject();
		attributes = (Hashtable )in.readObject();

		transferable = new SchemeCablePort_Transferable();
	}
}
