package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.awt.*;
import java.text.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;

public class Evaluation extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	transient static final public String typ = "evaluation";
	transient public ClientEvaluation_Transferable transferable;

	public String id = "";
	public String name = "";
	public long modified = 0;
	public String user_id = "";
	public long deleted = 0;
	public String type_id = "";

	public String description = "";

	public String etalon_id = "";
	public String monitored_element_id = "";
	public String threshold_set_id = "";

	public String[] result_ids = new String[0];

	public Vector parameters = new Vector();
	public Vector arguments = new Vector();

	public Evaluation(ClientEvaluation_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Evaluation(String id)
	{
		parameters = new Vector();
		arguments = new Vector();
		this.id = id;
		transferable = new ClientEvaluation_Transferable();
	}

	public void addParameter(Parameter parameter)
	{
		parameters.add(parameter);
	}

	public void addArgument(Parameter argument)
	{
		arguments.add(argument);
	}

	public void updateLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
		transferable.description = description;
		transferable.modified = modified;
		transferable.deleted = deleted;
		transferable.id = id;
		transferable.name = name;
		transferable.type_id = type_id;
		transferable.user_id = user_id;
		transferable.etalon_id = etalon_id;
		transferable.monitored_element_id = monitored_element_id;
		transferable.threshold_set_id = threshold_set_id;

		transferable.result_ids = new String[0];

		transferable.arguments = new ClientParameter_Transferable[arguments.size()];

		for (int i=0; i<transferable.arguments.length; i++)
		{
			Parameter argument = (Parameter)arguments.get(i);
			argument.setTransferableFromLocal();
			transferable.arguments[i] = (ClientParameter_Transferable)argument.getTransferable();
		}
	}

	public void setLocalFromTransferable()
	{
		modified = transferable.modified;
		deleted = transferable.deleted;
		id = transferable.id;
		name = transferable.name;
		type_id = transferable.type_id;
		user_id = transferable.user_id;
		etalon_id = transferable.etalon_id;
		monitored_element_id = transferable.monitored_element_id;
		threshold_set_id = transferable.threshold_set_id;
		description = transferable.description;

		result_ids = transferable.result_ids;

		arguments = new Vector();

		EvaluationType et = (EvaluationType )Pool.get(EvaluationType.typ, type_id);

		for (int i=0; i<transferable.arguments.length; i++)
		{
			Parameter param = new Parameter(transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.apt = (ActionParameterType )et.sorted_arguments.get(param.codename);
			arguments.add(param);
		}
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

	public String getTyp()
	{
		return typ;
	}

	public long getModified()
	{
		return modified;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(modified);
		out.writeObject(user_id);
		out.writeLong(deleted);
		out.writeObject(description);
		out.writeObject(etalon_id);
		out.writeObject(threshold_set_id);
		out.writeObject(monitored_element_id);
		out.writeObject(type_id);
		Object obj = result_ids;
		out.writeObject(obj);
		out.writeObject(parameters);
		out.writeObject(arguments);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		modified = in.readLong();
		user_id = (String )in.readObject();
		deleted = in.readLong();
		description = (String )in.readObject();
		etalon_id = (String )in.readObject();
		threshold_set_id = (String )in.readObject();
		monitored_element_id = (String )in.readObject();
		type_id = (String )in.readObject();
		Object obj = in.readObject();
		result_ids = (String[] )obj;
		parameters = (Vector )in.readObject();
		arguments = (Vector )in.readObject();

		transferable = new ClientEvaluation_Transferable();
		updateLocalFromTransferable();
	}
}