package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.SchemePort_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Network.Port;

public class SchemePort extends StubResource implements Serializable
{
	public static final String typ = "schemeport";
	private static final long serialVersionUID = 02L;
	public SchemePort_Transferable transferable;

	public String id = "";
	public String name = "";
	private String portId = "";
	public Port port;

	public String portTypeId = "";
	public String measurementPortId = "";
	public String measurementPortTypeId = "";
	public String deviceId = "";
	public String linkId = "";
	public String directionType = "";

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
		portTypeId = transferable.portTypeId;
		measurementPortTypeId = transferable.measurementPortTypeId;
		deviceId = transferable.deviceId;
		linkId = transferable.linkId;
		portId = transferable.portId;
		measurementPortId = transferable.measurementPortId;
		directionType = transferable.directionType;

		attributes = new HashMap(transferable.attributes.length);
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.portTypeId = portTypeId;
		transferable.measurementPortTypeId = measurementPortTypeId;
		transferable.deviceId = deviceId;
		transferable.linkId = linkId;
		transferable.portId = port == null ? portId : port.getId();
		transferable.measurementPortId = measurementPortId;
		transferable.directionType = directionType;

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
		if (portId.length() != 0)
			port = (Port)Pool.get(Port.typ, portId);
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String clonedId = (String)Pool.get("clonedids", id);
		if (clonedId != null)
		{
			SchemePort cloned = (SchemePort)Pool.get(SchemePort.typ, clonedId);
			if (cloned == null)
				System.err.println("SchemePort.clone() id not found: " + clonedId);
			else
				return cloned;
		}

		SchemePort port = new SchemePort(dataSource.GetUId(SchemePort.typ));

		port.name = name;
		port.portTypeId = portTypeId;
		port.measurementPortTypeId = measurementPortTypeId;
		port.deviceId = deviceId;
		port.linkId = linkId;
		port.portId = portId;
		port.port = this.port;
		port.measurementPortId = measurementPortId;
		port.directionType = directionType;

		port.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemePort.typ, port.getId(), port);
		Pool.put("clonedids", id, port.id);

		return port;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(portTypeId);
		out.writeObject(measurementPortTypeId);
		out.writeObject(deviceId);
		out.writeObject(linkId);
		out.writeObject(portId);
		Port[] p;
		if (port == null)
			p = new Port[0];
		else
		{
			p = new Port[1];
			p[0] = port;
		}

		out.writeObject(measurementPortId);
		out.writeObject(directionType);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		portTypeId = (String )in.readObject();
		measurementPortTypeId = (String )in.readObject();
		deviceId = (String )in.readObject();
		linkId = (String )in.readObject();
		portId = (String )in.readObject();
		Port[] p = (Port[])in.readObject();
		if (p.length == 1)
			port = p[0];

		measurementPortId = (String )in.readObject();
		directionType = (String )in.readObject();
		attributes = (Map )in.readObject();

		transferable = new SchemePort_Transferable();
	}
}
