package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;

public class TestSetup extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "testsetup";

	private TestSetup_Transferable transferable;

	public String id = "";
	public String name = "";
	public long created = 0;
	public long modified = 0;
	public String created_by = "";
	public String description = "";

	public String test_type_id = "";
	public String test_argument_set_id = "";

	public String analysis_type_id = "";
	public String criteria_set_id = "";

	public String evaluation_type_id = "";
	public String threshold_set_id = "";
	public String etalon_id = "";

	public String[] monitored_element_ids;

	public TestSetup()
	{
		monitored_element_ids = new String[0];
		transferable = new TestSetup_Transferable();
	}

	public TestSetup(TestSetup_Transferable transferable)
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
		modified = transferable.modified;
		created_by = transferable.created_by;
		description = transferable.description;

		test_type_id = transferable.test_type_id;
		test_argument_set_id = transferable.test_argument_set_id;

		analysis_type_id = transferable.analysis_type_id;
		criteria_set_id = transferable.criteria_set_id;

		evaluation_type_id = transferable.evaluation_type_id;
		threshold_set_id = transferable.threshold_set_id;
		etalon_id = transferable.etalon_id;

		monitored_element_ids = transferable.monitored_element_ids;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.created = created;
		transferable.modified = modified;
		transferable.created_by = created_by;
		transferable.description = description;

		transferable.test_type_id = test_type_id;
		transferable.test_argument_set_id = test_argument_set_id;

		transferable.analysis_type_id = analysis_type_id;
		transferable.criteria_set_id = criteria_set_id;

		transferable.evaluation_type_id = evaluation_type_id;
		transferable.threshold_set_id = threshold_set_id;
		transferable.etalon_id = etalon_id;

		transferable.monitored_element_ids = new String[0];
	}

	public void updateLocalFromTransferable()
	{
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(created);
		out.writeLong(modified);
		out.writeObject(created_by);
		out.writeObject(description);

		out.writeObject(test_type_id);
		out.writeObject(test_argument_set_id);

		out.writeObject(analysis_type_id);
		out.writeObject(criteria_set_id);

		out.writeObject(evaluation_type_id);
		out.writeObject(threshold_set_id);
		out.writeObject(etalon_id);
		Object o = monitored_element_ids;
		out.writeObject(o);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		created = in.readLong();
		modified = in.readLong();
		created_by = (String )in.readObject();
		description = (String )in.readObject();

		test_type_id = (String )in.readObject();
		test_argument_set_id = (String )in.readObject();

		analysis_type_id = (String )in.readObject();
		criteria_set_id = (String )in.readObject();

		evaluation_type_id = (String )in.readObject();
		threshold_set_id = (String )in.readObject();
		etalon_id = (String )in.readObject();

		Object o = in.readObject();
		monitored_element_ids = (String[] )o;

		transferable = new TestSetup_Transferable();
		updateLocalFromTransferable();
	}
}

