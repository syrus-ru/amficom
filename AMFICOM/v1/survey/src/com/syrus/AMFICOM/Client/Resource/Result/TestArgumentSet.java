package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;

public class TestArgumentSet extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "testargumentset";

	private ClientTestArgumentSet_Transferable transferable;

	public String id = "";
	public String name = "";
	public long created = 0;
	public String created_by = "";
	public String test_type_id = "";

	public Vector arguments = new Vector();

	public TestArgumentSet()
	{
		transferable = new ClientTestArgumentSet_Transferable();
		arguments = new Vector();
	}

	public TestArgumentSet(ClientTestArgumentSet_Transferable transferable)
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
		created_by = transferable.created_by;
		test_type_id = transferable.test_type_id;

		arguments = new Vector();
		for (int i=0; i<transferable.arguments.length; i++)
		{
			Parameter param = new Parameter(transferable.arguments[i]);
			param.updateLocalFromTransferable();
			arguments.add(param);
		}
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.created = created;
		transferable.created_by = created_by;
		transferable.test_type_id = test_type_id;

		transferable.arguments = new ClientParameter_Transferable[arguments.size()];
		for (int i=0; i<transferable.arguments.length; i++)
		{
			Parameter argument = (Parameter)arguments.get(i);
			argument.setTransferableFromLocal();
			transferable.arguments[i] = (ClientParameter_Transferable)argument.getTransferable();
		}
	}

	public void updateLocalFromTransferable()
	{
	}

	public void updateTestArgumentSet(String test_type_id)
	{
		TestType tt = (TestType )Pool.get(TestType.typ, test_type_id);

		for (int i = 0; i < arguments.size(); i++)
			{
			Parameter param = (Parameter )arguments.get(i);
			param.apt = (ActionParameterType )tt.sorted_arguments.get(param.codename);
		}
	}

	public void addArgument(Parameter argument)
	{
		arguments.add(argument);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(created);
		out.writeObject(created_by);
		out.writeObject(test_type_id);
		out.writeObject(arguments);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		created = in.readLong();
		created_by = (String )in.readObject();
		test_type_id = (String )in.readObject();
		arguments = (Vector )in.readObject();

		transferable = new ClientTestArgumentSet_Transferable();
		updateLocalFromTransferable();
	}
}

