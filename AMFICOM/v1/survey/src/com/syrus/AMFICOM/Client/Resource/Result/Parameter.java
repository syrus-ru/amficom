package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.io.*;
import java.util.*;

public class Parameter extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	transient static final public String typ = "parameter";

	public String id = "";
	public String type_id = "";
	public byte[] value = new byte[0];
	public String codename = "";
	public String parameter_type_id = "";

	transient public GlobalParameterType gpt = null;
	transient public ActionParameterType apt = null;

	transient ClientParameter_Transferable transferable;

	public Parameter()
	{
		transferable = new ClientParameter_Transferable();
	}

	public Parameter(
			String id,
			String type_id,
			byte[] value,
			String codename,
			String parameter_type_id)
	{
		this.id = id;
		this.type_id = type_id;
		this.value = value;
		this.codename = codename;
		this.parameter_type_id = parameter_type_id;
		
		transferable = new ClientParameter_Transferable();
	}

	public Parameter(ClientParameter_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void updateLocalFromTransferable()
	{
		gpt = (GlobalParameterType )Pool.get(GlobalParameterType.typ, parameter_type_id);
		apt = (ActionParameterType )Pool.get(ActionParameterType.typ, type_id);
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.type_id = type_id;
		transferable.value = value;
		transferable.codename = codename;
		transferable.parameter_type_id = parameter_type_id;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		type_id = transferable.type_id;
		value = transferable.value;
		codename = transferable.codename;
		parameter_type_id = transferable.parameter_type_id;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public String getName()
	{
		if(apt != null)
			return apt.getName();
		return gpt.getName();
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getTyp ()
	{
		return typ;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(type_id);
		out.writeObject(codename);
		out.writeObject(parameter_type_id);
		Object obj = value;
		out.writeObject(obj);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		type_id = (String )in.readObject();
		codename = (String )in.readObject();
		parameter_type_id = (String )in.readObject();

		Object obj = in.readObject();
		value = (byte[] )obj;

		transferable = new ClientParameter_Transferable();
		updateLocalFromTransferable();
	}
}