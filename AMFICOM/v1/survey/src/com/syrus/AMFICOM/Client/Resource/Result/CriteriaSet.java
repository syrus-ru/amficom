package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class CriteriaSet extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "criteriaset";

	private ClientCriteriaSet_Transferable transferable;

	public String id = "";
	public String name = "";
	public long created = 0;
	public String created_by = "";
	public String analysis_type_id = "";

	public Vector criterias;

	public CriteriaSet()
	{
		transferable = new ClientCriteriaSet_Transferable();
		criterias = new Vector();
	}

	public CriteriaSet(ClientCriteriaSet_Transferable transferable)
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
		analysis_type_id = transferable.analysis_type_id;

		criterias = new Vector();
		for (int i=0; i<transferable.criterias.length; i++)
			criterias.add(new Parameter(transferable.criterias[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.created = created;
		transferable.created_by = created_by;
		transferable.analysis_type_id = analysis_type_id;

		transferable.criterias = new ClientParameter_Transferable[criterias.size()];
		for (int i=0; i<transferable.criterias.length; i++)
		{
			Parameter criteria = (Parameter)criterias.get(i);
			criteria.setTransferableFromLocal();
			transferable.criterias[i] = (ClientParameter_Transferable)criteria.getTransferable();
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
		out.writeObject(analysis_type_id);
		out.writeObject(criterias);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		created = in.readLong();
		created_by = (String )in.readObject();
		analysis_type_id = (String )in.readObject();
		criterias = (Vector )in.readObject();

		transferable = new ClientCriteriaSet_Transferable();
		updateLocalFromTransferable();
	}
}

