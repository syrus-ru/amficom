/*
 * $Id: TestArgumentSet.java,v 1.1 2004/08/18 13:13:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class TestArgumentSet implements ObjectResource, Serializable {

	private static final long					serialVersionUID	= 01L;

	public static final String					TYPE				= "testargumentset";
	/**
	 * @deprecated use TYPE
	 */
	public static final String					typ					= TYPE;

	private List								argumentList		= new ArrayList();
	private long								created				= 0;
	private String								createdBy			= "";
	private String								id					= "";
	private String								name				= "";
	private String								testTypeId			= "";

	private boolean								changed				= false;

	private ClientTestArgumentSet_Transferable	transferable;

	public TestArgumentSet() {
		this.transferable = new ClientTestArgumentSet_Transferable();
	}

	public TestArgumentSet(ClientTestArgumentSet_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void addArgument(Parameter argument) {
		this.argumentList.add(argument);
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return this.argumentList;
	}

	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return this.created;
	}

	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return this.createdBy;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the testTypeId.
	 */
	public String getTestTypeId() {
		return this.testTypeId;
	}

	public Object getTransferable() {
		return this.transferable;
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
		this.createdBy = createdBy;
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
		this.createdBy = this.transferable.created_by;
		this.testTypeId = this.transferable.test_type_id;

		this.argumentList.clear();
		for (int i = 0; i < this.transferable.arguments.length; i++) {
			Parameter param = new Parameter(this.transferable.arguments[i]);
			param.updateLocalFromTransferable();
			this.argumentList.add(param);
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
		this.testTypeId = testTypeId;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.created = this.created;
		this.transferable.created_by = this.createdBy;
		this.transferable.test_type_id = this.testTypeId;

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
		{
			this.transferable.arguments = new ClientParameter_Transferable[this.argumentList.size()];
			int i = 0;
			for (Iterator it = this.argumentList.iterator(); it.hasNext(); i++) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.arguments[i] = (ClientParameter_Transferable) argument.getTransferable();
			}
		}
		this.changed = false;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	public void updateTestArgumentSet(String test_type_id) {
		TestType tt = (TestType) Pool.get(TestType.typ, test_type_id);

		int i = 0;
		for (Iterator it = this.argumentList.iterator(); it.hasNext(); i++) {
			Parameter param = (Parameter) it.next();
			param.setApt((ActionParameterType) tt.getSortedArguments().get(param.getCodename()));
		}
		this.changed = true;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.createdBy = (String) in.readObject();
		this.testTypeId = (String) in.readObject();
		this.argumentList = (List) in.readObject();

		this.transferable = new ClientTestArgumentSet_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.createdBy);
		out.writeObject(this.testTypeId);
		out.writeObject(this.argumentList);
		this.changed = false;
	}

	public ObjectResourceModel getModel() {
		throw new UnsupportedOperationException();
	}

	public long getModified() {
		throw new UnsupportedOperationException();
	}

	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}

	public boolean isChanged() {
		return this.changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}

