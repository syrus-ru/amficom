package com.syrus.AMFICOM.Client.Resource.Test;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.CORBA.Survey.*;

public class ModelingType extends ObjectResource implements Serializable
{ 
	private static final long serialVersionUID = 01L;
	static final public String typ = "modelingtype";
	public ModelingType_Transferable transferable;
	
	public String id = "";
	public String name = "";
	public String description = "";
	long modified = 0;

	public Vector parameters = new Vector();
	public Vector arguments = new Vector();

	public Hashtable sorted_parameters = new Hashtable();
	public Hashtable sorted_arguments = new Hashtable();

	public ModelingType(ModelingType_Transferable transferable)
	{
	    this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void updateLocalFromTransferable()
	{
		sorted_parameters = new Hashtable();
		sorted_arguments = new Hashtable();

		for (int i=0; i<parameters.size(); i++)
		{
			ActionParameterType parameter = (ActionParameterType )parameters.get(i);
			sorted_parameters.put(parameter.codename, parameter);
		}
		for (int i=0; i<arguments.size(); i++)
		{
			ActionParameterType argument = (ActionParameterType )arguments.get(i);
			sorted_arguments.put(argument.codename, argument);
		}
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.modified = this.modified;

		transferable.parameters = new ActionParameterType_Transferable[parameters.size()];
		transferable.arguments = new ActionParameterType_Transferable[arguments.size()];

		for (int i=0; i<transferable.parameters.length; i++)
		{
			ActionParameterType parameter = (ActionParameterType )parameters.get(i);
			parameter.setTransferableFromLocal();
			transferable.parameters[i] = (ActionParameterType_Transferable)parameter.getTransferable();
		}

		for (int i=0; i<transferable.arguments.length; i++)
		{
			ActionParameterType argument = (ActionParameterType )arguments.get(i);
			argument.setTransferableFromLocal();
			transferable.arguments[i] = (ActionParameterType_Transferable)argument.getTransferable();
		}

	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		modified = transferable.modified;

		parameters = new Vector();
		arguments = new Vector();

		for (int i=0; i<transferable.parameters.length; i++)
		{
			ActionParameterType parameter = new ActionParameterType(transferable.parameters[i]);
			parameters.add(parameter);
			sorted_parameters.put(parameter.codename, parameter);
		}
		for (int i=0; i<transferable.arguments.length; i++)
		{
			ActionParameterType argument = new ActionParameterType(transferable.arguments[i]);
			arguments.add(argument);
			sorted_arguments.put(argument.codename, argument);
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

	public String getTyp ()
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
		out.writeObject(description);
		out.writeObject(parameters);
		out.writeObject(arguments);

		out.writeLong(modified);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		parameters = (Vector )in.readObject();
		arguments = (Vector )in.readObject();

		modified = in.readLong();

		transferable = new ModelingType_Transferable();
		updateLocalFromTransferable();
	}
}