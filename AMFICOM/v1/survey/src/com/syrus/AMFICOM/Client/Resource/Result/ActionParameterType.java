package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;

public class ActionParameterType extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "actionparametertype";

	public String id = "";
	public String name = "";
	public String codename = "";
	public String parameter_type_id = "";
	public String holder_type_id = "";

	ActionParameterType_Transferable transferable;

	public ActionParameterType()
	{
		transferable = new ActionParameterType_Transferable();
	}

	public ActionParameterType(ActionParameterType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void updateLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.codename = codename;
		transferable.parameter_type_id = parameter_type_id;
		transferable.holder_type_id = holder_type_id;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
		parameter_type_id = transferable.parameter_type_id;
		holder_type_id = transferable.holder_type_id;
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
		return name;
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
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(parameter_type_id);
		out.writeObject(holder_type_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		parameter_type_id = (String )in.readObject();
		holder_type_id = (String )in.readObject();

		transferable = new ActionParameterType_Transferable();
	}
}