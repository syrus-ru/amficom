package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ClientModeling_Transferable;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Test.ModelingType;
import com.syrus.AMFICOM.Client.Survey.Result.UI.ModelingDisplayModel;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Vector;

public class Modeling extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "modeling";
	public ClientModeling_Transferable transferable;

	public String id = "";
	public String name = "";
	public long modified = 0;
	public String user_id = "";
	public long deleted = 0;
	public String type_id = "";
	public String scheme_path_id = "";
	public String domain_id = "";

	public Vector arguments = new Vector();

	public Modeling(ClientModeling_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Modeling(String id)
	{
		arguments = new Vector();
		this.id = id;
		transferable = new ClientModeling_Transferable();
	}

	public void addArgument(Parameter argument)
	{
		arguments.add(argument);
	}

	public Enumeration getChildTypes()
	{
		Vector vec = new Vector();
		vec.add("arguments");
		return vec.elements();
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals("arguments"))
		{
			return arguments.elements();
		}
	    return new Vector().elements();
	}

	public void updateLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
		transferable.modified = modified;
		transferable.deleted = deleted;
		transferable.id = id;
		transferable.name = name;
		transferable.type_id = type_id;
		transferable.user_id = user_id;
		transferable.scheme_path_id = scheme_path_id;
		transferable.domain_id = domain_id;

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
		scheme_path_id = transferable.scheme_path_id;
		domain_id = transferable.domain_id;

		arguments = new Vector();

		ModelingType mt = (ModelingType )Pool.get(ModelingType.typ, type_id);

		for (int i=0; i<transferable.arguments.length; i++)
		{
			Parameter param = new Parameter(transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.apt = (ActionParameterType )mt.sorted_arguments.get(param.codename);
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

	public String getTyp ()
	{
		return typ;
	}

	public long getModified()
	{
		return modified;
	}

  public static PropertiesPanel getPropertyPane()
  {
    return new GeneralPanel();
  }

  public ObjectResourceModel getModel()
  {
    return new ModelingModel(this);
  }

  public static ObjectResourceDisplayModel getDefaultDisplayModel ()
  {
    return new ModelingDisplayModel();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(modified);
		out.writeObject(user_id);
		out.writeLong(deleted);
		out.writeObject(type_id);
		out.writeObject(scheme_path_id);
		out.writeObject(domain_id);
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
		type_id = (String )in.readObject();
		scheme_path_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		arguments = (Vector )in.readObject();

		transferable = new ClientModeling_Transferable();
		updateLocalFromTransferable();
	}
}