package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class ThresholdSet extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "thresholdset";

	private ClientThresholdSet_Transferable transferable;

	public String id = "";
	public String name = "";
	public long created = 0;
	public String created_by = "";
	public String evaluation_type_id = "";

	public Vector thresholds;

	public ThresholdSet()
	{
		transferable = new ClientThresholdSet_Transferable();
		thresholds = new Vector();
	}

	public ThresholdSet(ClientThresholdSet_Transferable transferable)
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
		id = transferable.id;
		name = transferable.name;
		created = transferable.created;
		created_by = transferable.created_by;
		evaluation_type_id = transferable.evaluation_type_id;

		thresholds = new Vector();
		for (int i=0; i<transferable.thresholds.length; i++)
			thresholds.add(new Parameter(transferable.thresholds[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.created = created;
		transferable.created_by = created_by;
		transferable.evaluation_type_id = evaluation_type_id;

		transferable.thresholds = new ClientParameter_Transferable[thresholds.size()];
		for (int i=0; i<transferable.thresholds.length; i++)
		{
			Parameter criteria = (Parameter)thresholds.get(i);
			criteria.setTransferableFromLocal();
			transferable.thresholds[i] = (ClientParameter_Transferable)criteria.getTransferable();
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
		out.writeObject(created_by);
		out.writeObject(evaluation_type_id);
		out.writeObject(thresholds);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		created = in.readLong();
		created_by = (String )in.readObject();
		evaluation_type_id = (String )in.readObject();
		thresholds = (Vector )in.readObject();

		transferable = new ClientThresholdSet_Transferable();
		updateLocalFromTransferable();
	}
}


