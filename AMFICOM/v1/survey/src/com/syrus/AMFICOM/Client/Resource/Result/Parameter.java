package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.io.*;

public class Parameter extends ObjectResource implements Serializable {

	private static final long						serialVersionUID	= 01L;
	public static final String						typ					= "parameter";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public transient ActionParameterType			apt					= null;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									codename			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public transient GlobalParameterType			gpt					= null;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									parameter_type_id	= "";

	private transient ClientParameter_Transferable	transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									type_id				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public byte[]									value				= new byte[0];

	public Parameter() {
		transferable = new ClientParameter_Transferable();
	}

	public Parameter(ClientParameter_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Parameter(String id, String type_id, byte[] value, String codename,
			String parameter_type_id) {
		this.id = id;
		this.type_id = type_id;
		this.value = value;
		this.codename = codename;
		this.parameter_type_id = parameter_type_id;

		transferable = new ClientParameter_Transferable();
	}

	/**
	 * @return Returns the apt.
	 */
	public ActionParameterType getApt() {
		return apt;
	}

	/**
	 * @return Returns the codename.
	 */
	public String getCodename() {
		return codename;
	}

	public String getDomainId() {
		return "sysdomain";
	}

	/**
	 * @return Returns the gpt.
	 */
	public GlobalParameterType getGpt() {
		return gpt;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		if (apt != null) return apt.getName();
		return gpt.getName();
	}

	/**
	 * @return Returns the parameterTypeId.
	 */
	public String getParameterTypeId() {
		return parameter_type_id;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the typeId.
	 */
	public String getTypeId() {
		return type_id;
	}

	/**
	 * @return Returns the value.
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * @param apt
	 *            The apt to set.
	 */
	public void setApt(ActionParameterType apt) {
		this.apt = apt;
	}

	/**
	 * @param codename
	 *            The codename to set.
	 */
	public void setCodename(String codename) {
		this.codename = codename;
	}

	/**
	 * @param gpt
	 *            The gpt to set.
	 */
	public void setGpt(GlobalParameterType gpt) {
		this.gpt = gpt;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setLocalFromTransferable() {
		id = transferable.id;
		type_id = transferable.type_id;
		value = transferable.value;
		codename = transferable.codename;
		parameter_type_id = transferable.parameter_type_id;
	}

	/**
	 * @param parameterTypeId
	 *            The parameterTypeId to set.
	 */
	public void setParameterTypeId(String parameterTypeId) {
		this.parameter_type_id = parameterTypeId;
	}

	public void setTransferableFromLocal() {
		transferable.id = id;
		transferable.type_id = type_id;
		transferable.value = value;
		transferable.codename = codename;
		transferable.parameter_type_id = parameter_type_id;
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(String typeId) {
		this.type_id = typeId;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}

	public void updateLocalFromTransferable() {
		gpt = (GlobalParameterType) Pool.get(GlobalParameterType.typ,
				parameter_type_id);
		apt = (ActionParameterType) Pool.get(ActionParameterType.typ, type_id);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		type_id = (String) in.readObject();
		codename = (String) in.readObject();
		parameter_type_id = (String) in.readObject();

		Object obj = in.readObject();
		value = (byte[]) obj;

		transferable = new ClientParameter_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(id);
		out.writeObject(type_id);
		out.writeObject(codename);
		out.writeObject(parameter_type_id);
		Object obj = value;
		out.writeObject(obj);
	}
}