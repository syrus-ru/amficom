package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class Etalon extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "etalon";

	private ClientEtalon_Transferable transferable;

	public String id = "";
	public String name = "";
	public long created = 0;
	public String description = "";
	public String type_id = "";
	public long modified = 0;

	public Vector etalon_parameters;

	public Etalon()
	{
		transferable = new ClientEtalon_Transferable();
		etalon_parameters = new Vector();
	}

	public Etalon(ClientEtalon_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getTyp()
	{
		return typ;
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
		id = transferable.id;
		name = transferable.name;
		created = transferable.created;
		description = transferable.description;
		type_id = transferable.type_id;
		modified = transferable.modified;

		etalon_parameters = new Vector();
		for (int i=0; i<transferable.etalon_parameters.length; i++)
			etalon_parameters.add(new Parameter(transferable.etalon_parameters[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.created = created;
		transferable.description = description;
		transferable.type_id = type_id;
		transferable.modified = modified;

		transferable.etalon_parameters = new ClientParameter_Transferable[etalon_parameters.size()];
		for (int i=0; i<transferable.etalon_parameters.length; i++)
		{
			Parameter etalon_parameter = (Parameter)etalon_parameters.get(i);
			etalon_parameter.setTransferableFromLocal();
			transferable.etalon_parameters[i] = (ClientParameter_Transferable)etalon_parameter.getTransferable();
		}
	}

	public void updateLocalFromTransferable()
	{
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(created);
		out.writeObject(description);
		out.writeObject(type_id);
		out.writeObject(etalon_parameters);
		out.writeLong(modified);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		created = in.readLong();
		description = (String )in.readObject();
		type_id = (String )in.readObject();
		etalon_parameters = (Vector )in.readObject();
		modified = in.readLong();

		transferable = new ClientEtalon_Transferable();
		updateLocalFromTransferable();
	}
}


