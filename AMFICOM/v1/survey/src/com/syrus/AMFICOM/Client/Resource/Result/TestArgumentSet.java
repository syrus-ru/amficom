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
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String id = "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String name = "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long created = 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String created_by = "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String test_type_id = "";
	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector arguments = new Vector();
	private List argumentList = new ArrayList();
	public TestArgumentSet()
	{
		transferable = new ClientTestArgumentSet_Transferable();
		//arguments = new Vector();
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

		//arguments = new Vector();
		arguments.clear();
		argumentList.clear();
		for (int i=0; i<transferable.arguments.length; i++)
		{
			Parameter param = new Parameter(transferable.arguments[i]);
			param.updateLocalFromTransferable();
			argumentList.add(param);
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

//		transferable.arguments = new ClientParameter_Transferable[arguments.size()];
//		for (int i=0; i<transferable.arguments.length; i++)
//		{
//			Parameter argument = (Parameter)arguments.get(i);
//			argument.setTransferableFromLocal();
//			transferable.arguments[i] = (ClientParameter_Transferable)argument.getTransferable();
//		}
		/**
		 * @todo only for backward arguments Vector implementation
		 */
		if (arguments.size() == 0) {
			transferable.arguments = new ClientParameter_Transferable[argumentList
					.size()];
			for (int i = 0; i < argumentList.size(); i++) {
				Parameter argument = (Parameter) argumentList.get(i);
				argument.setTransferableFromLocal();
				transferable.arguments[i] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < arguments.size(); i++) {
				Object obj = arguments.get(i);
				map.put(obj, obj);
			}
			for (int i = 0; i < argumentList.size(); i++) {
				Object obj = argumentList.get(i);
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			transferable.arguments = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				transferable.arguments[i++] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		}
	}

	public void updateLocalFromTransferable()
	{
	}

	public void updateTestArgumentSet(String test_type_id)
	{
		TestType tt = (TestType )Pool.get(TestType.typ, test_type_id);

		for (int i = 0; i < argumentList.size(); i++)
			{
			Parameter param = (Parameter )argumentList.get(i);
			param.apt = (ActionParameterType )tt.sorted_arguments.get(param.codename);
		}
	}

	public void addArgument(Parameter argument)
	{
		arguments.add(argument);
		argumentList.add(argument);
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
	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(long created) {
		this.created = created;
	}
	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return created_by;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(String createdBy) {
		this.created_by = createdBy;
	}
	/**
	 * @return Returns the testTypeId.
	 */
	public String getTestTypeId() {
		return test_type_id;
	}
	/**
	 * @param testTypeId The testTypeId to set.
	 */
	public void setTestTypeId(String testTypeId) {
		this.test_type_id = testTypeId;
	}
	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return argumentList;
	}
	/**
	 * @param argumentList The argumentList to set.
	 */
	public void setArgumentList(List argumentsList) {
		this.argumentList = argumentsList;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}

