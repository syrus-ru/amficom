package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.SchemeCablePort_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;

public class SchemeCablePort extends StubResource implements Serializable
{
	public static final  String typ = "schemecableport";
	private static final long serialVersionUID = 01L;
	public SchemeCablePort_Transferable transferable;

	public String id = "";
	public String name = "";
	public String deviceId = "";
	public String cableLinkId = "";
	public String cablePortId = "";
	public CablePort cablePort;

	public String cablePortTypeId = "";
	public String measurementPortId = "";
	public String measurementPortTypeId = "";
	public String directionType = "";

	public Map attributes;

	public SchemeCablePort(SchemeCablePort_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeCablePort(String id)
	{
		this.id = id;
		transferable = new SchemeCablePort_Transferable();
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

	public Object getTransferable()
	{
		return transferable;
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		cablePortId = transferable.cablePortId;
		cablePortTypeId = transferable.cablePortTypeId;
		deviceId = transferable.deviceId;
		cableLinkId = transferable.cableLinkId;
		measurementPortId = transferable.measurementPortId;
		measurementPortTypeId = transferable.measurementPortTypeId;
		directionType = transferable.directionType;

		attributes = new HashMap(transferable.attributes.length);
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.cablePortId = cablePort == null ? cablePortId : cablePort.getId();
		transferable.cablePortTypeId = cablePortTypeId;
		transferable.deviceId = deviceId;
		transferable.cableLinkId = cableLinkId;
		transferable.measurementPortId = measurementPortId;
		transferable.measurementPortTypeId = measurementPortTypeId;
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
		if (cablePortId.length() != 0)
			cablePort = (CablePort)Pool.get(CablePort.typ, cablePortId);
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String clonedId = (String)Pool.get("clonedids", id);
		if (clonedId != null)
		{
			SchemeCablePort cloned = (SchemeCablePort)Pool.get(SchemeCablePort.typ, clonedId);
			if (cloned == null)
				System.err.println("SchemeCablePort.clone() id not found: " + clonedId);
			else
				return cloned;
		}

		SchemeCablePort port = new SchemeCablePort(dataSource.GetUId(SchemeCablePort.typ));

		port.name = name;
		port.cablePortId = cablePortId;
		port.cablePort = cablePort;
		port.cablePortTypeId = cablePortTypeId;
		port.deviceId = deviceId;
		port.cableLinkId = cableLinkId;
		port.measurementPortId = measurementPortId;
		port.measurementPortTypeId = measurementPortTypeId;
		port.directionType = directionType;

		port.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeCablePort.typ, port.getId(), port);
		Pool.put("clonedids", id, port.id);
		return port;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(cablePortId);
		CablePort[] p;
		if (cablePort == null)
			p = new CablePort[0];
		else
		{
			p = new CablePort[1];
			p[0] = cablePort;
		}

		out.writeObject(cablePortTypeId);
		out.writeObject(deviceId);
		out.writeObject(cableLinkId);
		out.writeObject(measurementPortId);
		out.writeObject(measurementPortTypeId);
		out.writeObject(directionType);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		cablePortId = (String )in.readObject();
		CablePort[] p = (CablePort[])in.readObject();
		if (p.length == 1)
			cablePort = p[0];

		cablePortTypeId = (String )in.readObject();
		deviceId = (String )in.readObject();
		cableLinkId = (String )in.readObject();
		measurementPortId = (String )in.readObject();
		measurementPortTypeId = (String )in.readObject();
		directionType = (String )in.readObject();
		attributes = (Map )in.readObject();

		transferable = new SchemeCablePort_Transferable();
	}
}
