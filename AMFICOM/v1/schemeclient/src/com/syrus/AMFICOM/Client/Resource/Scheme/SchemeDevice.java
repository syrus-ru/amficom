package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SchemeDevice extends StubResource implements Serializable
{
	public static final String typ = "schemedevice";
	private static final long serialVersionUID = 01L;
	public SchemeDevice_Transferable transferable;

	public String id = "";
	public String name = "";

	public Vector ports = new Vector();
	public Vector cableports = new Vector();

	public Map attributes = new HashMap();

	public SchemeDevice(SchemeDevice_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeDevice(String id)
	{
		this.id = id;
		ports = new Vector();
		cableports = new Vector();
		transferable = new SchemeDevice_Transferable();
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

	public Enumeration getChildTypes()
	{
		Vector vec = new Vector();
		vec.add("ports");
		vec.add("cableports");
		return vec.elements();
	}

	public Class getChildClass(String key)
	{
		if(key.equals("ports"))
		{
			return SchemePort.class;
		}
		else if(key.equals("cableports"))
		{
			return SchemeCablePort.class;
		}
		return ObjectResource.class;
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals("ports"))
		{
			return ports.elements();
		}
		else if(key.equals("cableports"))
		{
			return cableports.elements();
		}
		return new Vector().elements();
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;

		ports = new Vector();
		cableports = new Vector();

		for (int i = 0; i < transferable.ports.length; i++)
			ports.add(new SchemePort(transferable.ports[i]));
		for (int i = 0; i < transferable.cableports.length; i++)
			cableports.add(new SchemeCablePort(transferable.cableports[i]));
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;

		transferable.ports = new SchemePort_Transferable[ports.size()];
		transferable.cableports = new SchemeCablePort_Transferable[cableports.size()];

		int counter = 0;
		for (Iterator it = ports.iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			port.setTransferableFromLocal();
			transferable.ports[counter++] = (SchemePort_Transferable)port.getTransferable();
		}
		counter = 0;
		for (Iterator it = cableports.iterator(); it.hasNext();)
		{
			SchemeCablePort cableport = (SchemeCablePort)it.next();
			cableport.setTransferableFromLocal();
			transferable.cableports[counter++] = (SchemeCablePort_Transferable)cableport.getTransferable();
		}

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
		for (Iterator it = ports.iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			Pool.put(SchemePort.typ, port.getId(), port);
			port.updateLocalFromTransferable();
		}
		for (Iterator it = cableports.iterator(); it.hasNext();)
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			Pool.put(SchemeCablePort.typ, port.getId(), port);
			port.updateLocalFromTransferable();
		}
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			SchemeDevice cloned = (SchemeDevice)Pool.get(SchemeDevice.typ, cloned_id);
			if (cloned == null)
				System.err.println("Scheme.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		SchemeDevice dev = new SchemeDevice(dataSource.GetUId(SchemeDevice.typ));
		dev.name = name;

		dev.ports = new Vector(ports.size());
		for (Iterator it = ports.iterator(); it.hasNext();)
		{
			SchemePort new_port = (SchemePort)((SchemePort)it.next()).clone(dataSource);
			new_port.device_id = dev.getId();
			dev.ports.add(new_port);
		}

		dev.cableports = new Vector(cableports.size());
		for (Iterator it = cableports.iterator(); it.hasNext();)
		{
			SchemeCablePort new_port = (SchemeCablePort)((SchemeCablePort)it.next()).clone(dataSource);
			new_port.device_id = dev.getId();
			dev.cableports.add(new_port);
		}

		dev.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeDevice.typ, dev.getId(), dev);
		Pool.put("clonedids", id, dev.id);
		return dev;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(ports);
		out.writeObject(cableports);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		ports = (Vector )in.readObject();
		cableports = (Vector )in.readObject();
		attributes = (Hashtable )in.readObject();

		transferable = new SchemeDevice_Transferable();
		updateLocalFromTransferable();
	}
}

