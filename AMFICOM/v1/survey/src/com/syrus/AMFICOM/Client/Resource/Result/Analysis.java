package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ClientAnalysis_Transferable;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.Client.Survey.Result.UI.AnalysisDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Vector;

public class Analysis extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	transient static final public String typ = "analysis";
	transient public ClientAnalysis_Transferable transferable;

	public String id = "";
	public String name = "";
	public long modified = 0;
	public String user_id = "";
	public long deleted = 0;
	public String description = "";
	public String result_id = "";

	public String criteria_set_id = "";
	public String monitored_element_id = "";
	public String type_id = "";

	public String[] result_ids = new String[0];

	public Vector parameters = new Vector();
	public Vector arguments = new Vector();

	public Analysis(ClientAnalysis_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Analysis(String id)
	{
		parameters = new Vector();
		arguments = new Vector();
		this.id = id;
		transferable = new ClientAnalysis_Transferable();
	}

	public void addParameter(Parameter parameter)
	{
		parameters.add(parameter);
	}

	public void addArgument(Parameter argument)
	{
		arguments.add(argument);
	}

	public Enumeration getChildTypes()
	{
		Vector vec = new Vector();
		vec.add("arguments");
		vec.add("parameters");
		return vec.elements();
	}

	public Class getChildClass(String key)
	{
		if(key.equals("arguments"))
		{
			return Parameter.class;
		}
		else if(key.equals("parameters"))
		{
			return Parameter.class;
		}
		return ObjectResource.class;
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals("arguments"))
		{
			return arguments.elements();
		}
		else if(key.equals("parameters"))
		{
			return parameters.elements();
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
		transferable.description = description;
		transferable.id = id;
		transferable.name = name;
		transferable.user_id = user_id;
		transferable.monitored_element_id = monitored_element_id;
		transferable.type_id = type_id;
		transferable.result_ids = new String[0];
		transferable.criteria_set_id = criteria_set_id;

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
		description = transferable.description;
		id = transferable.id;
		name = transferable.name;
		user_id = transferable.user_id;
		monitored_element_id = transferable.monitored_element_id;
		type_id = transferable.type_id;
		criteria_set_id = transferable.criteria_set_id;

		result_ids = transferable.result_ids;

		arguments = new Vector();

		AnalysisType at = (AnalysisType )Pool.get(AnalysisType.typ, type_id);

		for (int i=0; i<transferable.arguments.length; i++)
			{
			Parameter param = new Parameter(transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.apt = (ActionParameterType )at.sorted_arguments.get(param.codename);
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

	public static PropertiesPanel getPropertyPane()
	{
		return new GeneralPanel();
	}

	public ObjectResourceModel getModel()
	{
		return new AnalysisModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel ()
	{
		return new AnalysisDisplayModel();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(modified);
		out.writeObject(user_id);
		out.writeLong(deleted);
		out.writeObject(description);
		out.writeObject(result_id);
		out.writeObject(criteria_set_id);
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
		result_id = (String )in.readObject();
		criteria_set_id = (String )in.readObject();
		monitored_element_id = (String )in.readObject();
		type_id = (String )in.readObject();
		Object obj = in.readObject();
		result_ids = (String[] )obj;
		parameters = (Vector )in.readObject();
		arguments = (Vector )in.readObject();

		transferable = new ClientAnalysis_Transferable();
		updateLocalFromTransferable();
	}
}