/*
 * $Id: Parameter.java,v 1.1 2004/08/18 13:13:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;

import java.io.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class Parameter implements ObjectResource, Serializable {

	private static final long						serialVersionUID	= 01L;
	public static final String						TYPE				= "parameter";
	public static final String						typ					= TYPE;
	
	private transient ActionParameterType			apt					= null;
	private String									codename			= "";
	private transient GlobalParameterType			gpt					= null;
	private String									id					= "";
	private String									parameterTypeId	= "";
	private transient ClientParameter_Transferable	transferable;	
	private String									typeId				= "";
	private byte[]									value				= new byte[0];
	
	private boolean changed = false;

	public Parameter() {
		this.transferable = new ClientParameter_Transferable();
	}

	public Parameter(ClientParameter_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Parameter(String id, String type_id, byte[] value, String codename,
			String parameter_type_id) {
		this.id = id;
		this.typeId = type_id;
		this.value = value;
		this.codename = codename;
		this.parameterTypeId = parameter_type_id;

		this.transferable = new ClientParameter_Transferable();
	}

	/**
	 * @return Returns the apt.
	 */
	public ActionParameterType getApt() {
		return this.apt;
	}

	/**
	 * @return Returns the codename.
	 */
	public String getCodename() {
		return this.codename;
	}

	public String getDomainId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return Returns the gpt.
	 */
	public GlobalParameterType getGpt() {
		return this.gpt;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return (this.apt!=null)? this.apt.getName():this.gpt.getName();
	}

	/**
	 * @return Returns the parameterTypeId.
	 */
	public String getParameterTypeId() {
		return this.parameterTypeId;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the typeId.
	 */
	public String getTypeId() {
		return this.typeId;
	}

	/**
	 * @return Returns the value.
	 */
	public byte[] getValue() {
		return this.value;
	}

	/**
	 * @param apt
	 *            The apt to set.
	 */
	public void setApt(ActionParameterType apt) {
		this.changed = true;
		this.apt = apt;
	}

	/**
	 * @param codename
	 *            The codename to set.
	 */
	public void setCodename(String codename) {
		this.changed = true;
		this.codename = codename;
	}

	/**
	 * @param gpt
	 *            The gpt to set.
	 */
	public void setGpt(GlobalParameterType gpt) {
		this.changed = true;
		this.gpt = gpt;
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
		this.typeId = this.transferable.type_id;
		this.value = this.transferable.value;
		this.codename = this.transferable.codename;
		this.parameterTypeId = this.transferable.parameter_type_id;
		this.changed = false;
	}

	/**
	 * @param parameterTypeId
	 *            The parameterTypeId to set.
	 */
	public void setParameterTypeId(String parameterTypeId) {
		this.changed = true;
		this.parameterTypeId = parameterTypeId;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.type_id = this.typeId;
		this.transferable.value = this.value;
		this.transferable.codename = this.codename;
		this.transferable.parameter_type_id = this.parameterTypeId;
		this.changed = false;
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(String typeId) {
		this.changed = true;
		this.typeId = typeId;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(byte[] value) {
		this.changed = true;
		this.value = value;
	}

	public void updateLocalFromTransferable() {
		this.gpt = (GlobalParameterType) Pool.get(GlobalParameterType.TYPE,
				this.parameterTypeId);
		this.apt = (ActionParameterType) Pool.get(ActionParameterType.TYPE,
				this.typeId);
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.typeId = (String) in.readObject();
		this.codename = (String) in.readObject();
		this.parameterTypeId = (String) in.readObject();

		Object obj = in.readObject();
		this.value = (byte[]) obj;

		this.transferable = new ClientParameter_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.typeId);
		out.writeObject(this.codename);
		out.writeObject(this.parameterTypeId);
		Object obj = this.value;
		out.writeObject(obj);
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

