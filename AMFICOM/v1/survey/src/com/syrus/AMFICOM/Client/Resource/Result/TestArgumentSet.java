package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class TestArgumentSet extends ObjectResource implements Serializable {

	private static final long					serialVersionUID	= 01L;

	public static final String					typ					= "testargumentset";
	private List								argumentList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector								arguments			= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long									created				= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								created_by			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								name				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								test_type_id		= "";

	private ClientTestArgumentSet_Transferable	transferable;

	public TestArgumentSet() {
		transferable = new ClientTestArgumentSet_Transferable();
		//arguments = new Vector();
	}

	public TestArgumentSet(ClientTestArgumentSet_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void addArgument(Parameter argument) {
		arguments.add(argument);
		argumentList.add(argument);
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return argumentList;
	}

	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return created_by;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Returns the testTypeId.
	 */
	public String getTestTypeId() {
		return test_type_id;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @param argumentList
	 *            The argumentList to set.
	 */
	public void setArgumentList(List argumentsList) {
		this.changed = true;
		this.argumentList = argumentsList;
	}

	/**
	 * @param created
	 *            The created to set.
	 */
	public void setCreated(long created) {
		this.changed = true;
		this.created = created;
	}

	/**
	 * @param createdBy
	 *            The createdBy to set.
	 */
	public void setCreatedBy(String createdBy) {
		this.changed = true;
		this.created_by = createdBy;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.changed = true;
		this.id = id;
	}

	public void setLocalFromTransferable() {
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.created = this.transferable.created;
		this.created_by = this.transferable.created_by;
		this.test_type_id = this.transferable.test_type_id;

		//arguments = new Vector();
		this.arguments.clear();
		this.argumentList.clear();
		for (int i = 0; i < this.transferable.arguments.length; i++) {
			Parameter param = new Parameter(this.transferable.arguments[i]);
			param.updateLocalFromTransferable();
			this.argumentList.add(param);
			this.arguments.add(param);
		}
		this.changed = false;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	/**
	 * @param testTypeId
	 *            The testTypeId to set.
	 */
	public void setTestTypeId(String testTypeId) {
		this.changed = true;
		this.test_type_id = testTypeId;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.created = this.created;
		this.transferable.created_by = this.created_by;
		this.transferable.test_type_id = this.test_type_id;

		//		transferable.arguments = new
		// ClientParameter_Transferable[arguments.size()];
		//		for (int i=0; i<transferable.arguments.length; i++)
		//		{
		//			Parameter argument = (Parameter)arguments.get(i);
		//			argument.setTransferableFromLocal();
		//			transferable.arguments[i] =
		// (ClientParameter_Transferable)argument.getTransferable();
		//		}
		/**
		 * @todo only for backward arguments Vector implementation
		 */
		if (this.arguments.isEmpty()) {
			this.transferable.arguments = new ClientParameter_Transferable[this.argumentList
					.size()];
			int i = 0;
			for (Iterator it = this.argumentList.iterator(); it.hasNext();) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.arguments[i++] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.arguments.size(); i++) {
				Object obj = this.arguments.get(i);
				map.put(obj, obj);
			}
			for (Iterator it = this.argumentList.iterator(); it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.arguments = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.arguments[i++] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		}
		this.changed = false;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	public void updateTestArgumentSet(String test_type_id) {
		TestType tt = (TestType) Pool.get(TestType.typ, test_type_id);

		//		for (int i = 0; i < this.argumentList.size(); i++) {
		//			Parameter param = (Parameter) argumentList.get(i);
		//			param.setApt((ActionParameterType) tt.sorted_arguments.get(param
		//					.getCodename()));
		//		}
		HashMap map = new HashMap();
		for (int i = 0; i < this.arguments.size(); i++) {
			Object obj = this.arguments.get(i);
			map.put(obj, obj);
		}
		for (Iterator it = this.argumentList.iterator(); it.hasNext();) {
			Object obj = it.next();
			map.put(obj, obj);
		}

		Set keySet = map.keySet();
		this.transferable.arguments = new ClientParameter_Transferable[keySet
				.size()];
		for (Iterator it = keySet.iterator(); it.hasNext();) {
			Parameter param = (Parameter) it.next();
			param.setApt((ActionParameterType) tt.getSortedArguments().get(
					param.getCodename()));
		}
		this.changed = true;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.created_by = (String) in.readObject();
		this.test_type_id = (String) in.readObject();
		this.arguments = (Vector) in.readObject();

		this.transferable = new ClientTestArgumentSet_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.created_by);
		out.writeObject(this.test_type_id);
		out.writeObject(this.arguments);
		this.changed = false;
	}
}

