package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

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
		return ConstStorage.SYS_DOMAIN;
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
		this.type_id = this.transferable.type_id;
		this.value = this.transferable.value;
		this.codename = this.transferable.codename;
		this.parameter_type_id = this.transferable.parameter_type_id;
		this.changed = false;
	}

	/**
	 * @param parameterTypeId
	 *            The parameterTypeId to set.
	 */
	public void setParameterTypeId(String parameterTypeId) {
		this.changed = true;
		this.parameter_type_id = parameterTypeId;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.type_id = this.type_id;
		this.transferable.value = this.value;
		this.transferable.codename = this.codename;
		this.transferable.parameter_type_id = this.parameter_type_id;
		this.changed = false;
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(String typeId) {
		this.changed = true;
		this.type_id = typeId;
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
		this.gpt = (GlobalParameterType) Pool.get(GlobalParameterType.typ,
				this.parameter_type_id);
		this.apt = (ActionParameterType) Pool.get(ActionParameterType.typ,
				this.type_id);
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.type_id = (String) in.readObject();
		this.codename = (String) in.readObject();
		this.parameter_type_id = (String) in.readObject();

		Object obj = in.readObject();
		this.value = (byte[]) obj;

		this.transferable = new ClientParameter_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.type_id);
		out.writeObject(this.codename);
		out.writeObject(this.parameter_type_id);
		Object obj = this.value;
		out.writeObject(obj);
		this.changed = false;
	}
}

