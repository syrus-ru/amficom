package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.awt.*;
import java.text.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Survey.Result.UI.*;

public class Result extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	transient static final public String typ = "result";

	transient public ClientResult_Transferable transferable;

	public String test_request_id = "";
	public long deleted = 0;
	public String modeling_id = "";
	public String analysis_id = "";
	public String result_type = "";
	public String id = "";
	public String user_id = "";
	public String result_set_id = "";
	public String evaluation_id = "";
	public String test_id = "";
	public String action_id = "";

	public String name = "";

	public long elementary_start_time = 0;

	public Vector parameters = new Vector();

	transient static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

	public Result(ClientResult_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();

		name = sdf.format(new Date(elementary_start_time));
	}
/*
	public Result()
	{
		transferable = new ClientResult_Transferable();
		parameters = new Vector();

		name = sdf.format(new Date(elementary_start_time));
	}
*/
	public Result(String action_id, String type, String result_set_id, String user_id, String res_id)
	{
		this.id = res_id;
		this.result_type = type;
		this.user_id = user_id;
		this.result_set_id = result_set_id;
		this.action_id = action_id;
/*
		if (type.equals("analysis"))
			analysis_id = action_id;
		if (type.equals("testrequest"))
			test_request_id = action_id;
		if (type.equals("modeling"))
			modeling_id = action_id;
		if (type.equals("evaluation"))
			evalustion_id = action_id;
		if (type.equals("test"))
			test_id = action_id;
*/
		transferable = new ClientResult_Transferable();
		parameters = new Vector();

		name = sdf.format(new Date(elementary_start_time));
	}

	public void addParameter(Parameter parameter)
	{
		parameters.add(parameter);
	}

	public void updateLocalFromTransferable()
	{
		Hashtable ht = new Hashtable();
		try
		{
		if (result_type.equals("analysis"))
		{
			analysis_id = action_id;
			Analysis a = (Analysis)Pool.get("analysis", action_id);
			AnalysisType at = (AnalysisType )Pool.get(AnalysisType.typ, a.type_id);
			ht = at.sorted_parameters;
		}
		else
		if (result_type.equals("modeling"))
		{
			modeling_id = action_id;
			Modeling m = (Modeling )Pool.get("modeling", action_id);
			ModelingType mt = (ModelingType )Pool.get(ModelingType.typ, m.type_id);
			ht = mt.sorted_parameters;
		}
		else
		if (result_type.equals("evaluation"))
		{
			evaluation_id = action_id;
			Evaluation e = (Evaluation )Pool.get("evaluation", action_id);
			EvaluationType et = (EvaluationType )Pool.get(EvaluationType.typ, e.type_id);
			ht = et.sorted_parameters;
		}
		else
		if (result_type.equals("test"))
		{
			test_id = action_id;
			Test t = (Test )Pool.get("test", action_id);
			TestType tt = (TestType )Pool.get(TestType.typ, t.test_type_id);
			ht = tt.sorted_parameters;
		}
		}
		catch(Exception ex)
		{
		}

		for (int i = 0; i < parameters.size(); i++)
		{
			Parameter param = (Parameter )parameters.get(i);
			param.updateLocalFromTransferable();
			param.apt = (ActionParameterType )ht.get(param.codename);
		}
	}

	public void setTransferableFromLocal()
	{
		transferable.parameters = new ClientParameter_Transferable[parameters.size()];
		transferable.action_id = action_id;
		transferable.id = id;
		transferable.result_set_id = result_set_id;
		transferable.result_type = result_type;
		transferable.user_id = user_id;
		transferable.elementary_start_time = elementary_start_time;

		for (int i = 0; i < transferable.parameters.length; i++)
		{
			Parameter parameter = (Parameter)parameters.get(i);
			parameter.setTransferableFromLocal();
//			transferable.parameters[i] = (ClientParameter_Transferable)((Parameter)parameters.get(i)).getTransferable();
			transferable.parameters[i] = (ClientParameter_Transferable)parameter.getTransferable();
		}
	}

	public void setLocalFromTransferable()
	{
		action_id = transferable.action_id;
		id = transferable.id;
		result_set_id = transferable.result_set_id;
		result_type = transferable.result_type;
		user_id = transferable.user_id;
		elementary_start_time = transferable.elementary_start_time;

		parameters = new Vector();
/*
		Hashtable ht = new Hashtable();
		try
		{
		if (result_type.equals("analysis"))
		{
			analysis_id = action_id;
			Analysis a = (Analysis)Pool.get("analysis", action_id);
			AnalysisType at = (AnalysisType )Pool.get(AnalysisType.typ, a.type_id);
			ht = at.sorted_parameters;
		}
		else
		if (result_type.equals("modeling"))
		{
			modeling_id = action_id;
			Modeling m = (Modeling )Pool.get("modeling", action_id);
			ModelingType mt = (ModelingType )Pool.get(ModelingType.typ, m.type_id);
			ht = mt.sorted_parameters;
		}
		else
		if (result_type.equals("evaluation"))
		{
			evaluation_id = action_id;
			Evaluation e = (Evaluation )Pool.get("evaluation", action_id);
			EvaluationType et = (EvaluationType )Pool.get(EvaluationType.typ, e.type_id);
			ht = et.sorted_parameters;
		}
		else
		if (result_type.equals("test"))
		{
			test_id = action_id;
			Test t = (Test )Pool.get("test", action_id);
			TestType tt = (TestType )Pool.get(TestType.typ, t.test_type_id);
			ht = tt.sorted_parameters;
		}
		}
		catch(Exception ex)
		{
		}
*/
		for (int i = 0; i < transferable.parameters.length; i++)
		{
			Parameter param = new Parameter(transferable.parameters[i]);
			param.updateLocalFromTransferable();
//			param.apt = (ActionParameterType )ht.get(param.codename);
			parameters.add(param);
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
//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
//		return sdf.format(new Date(elementary_start_time));
	}

	public void setName(String n)
	{
		name = n;
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
		return elementary_start_time;
	}

	public ObjectResourceModel getModel()
	{
		return new ResultModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new ResultDisplayModel();
	}

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new ResultTimeSorter();
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(test_request_id);
		out.writeLong(deleted);
		out.writeObject(modeling_id);
		out.writeObject(analysis_id);
		out.writeObject(result_type);
		out.writeObject(id);
		out.writeObject(user_id);
		out.writeObject(result_set_id);
		out.writeObject(evaluation_id);
		out.writeObject(test_id);
		out.writeObject(action_id);
		out.writeObject(name);
		out.writeLong(elementary_start_time);
		out.writeObject(parameters);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		test_request_id = (String )in.readObject();
		deleted = in.readLong();
		modeling_id = (String )in.readObject();
		analysis_id = (String )in.readObject();
		result_type = (String )in.readObject();
		id = (String )in.readObject();
		user_id = (String )in.readObject();
		result_set_id = (String )in.readObject();
		evaluation_id = (String )in.readObject();
		test_id = (String )in.readObject();
		action_id = (String )in.readObject();
		name = (String )in.readObject();
		elementary_start_time = in.readLong();
		parameters = (Vector )in.readObject();

		transferable = new ClientResult_Transferable();
		updateLocalFromTransferable();
	}
}

class ResultTimeSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"time", "long"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}
	
	public String getString(ObjectResource or, String column)
	{
		return "";
	}

	public long getLong(ObjectResource or, String column)
	{
		Result res = (Result )or;
		return res.elementary_start_time;
	}
}
