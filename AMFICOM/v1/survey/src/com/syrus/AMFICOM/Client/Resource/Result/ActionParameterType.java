package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class ActionParameterType extends ObjectResource implements Serializable {

	private static final long					serialVersionUID	= 01L;
	public static final String					typ					= "actionparametertype";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								codename			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								holder_type_id		= "";
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
	public String								parameter_type_id	= "";

	private ActionParameterType_Transferable	transferable;

	public ActionParameterType() {
		this.transferable = new ActionParameterType_Transferable();
	}

	public ActionParameterType(ActionParameterType_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the codename.
	 */
	public String getCodename() {
		return this.codename;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the holderTypeId.
	 */
	public String getHolderTypeId() {
		return this.holder_type_id;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the parameterTypeId.
	 */
	public String getParameterTypeId() {
		return this.parameter_type_id;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return typ;
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
	 * @param holderTypeId
	 *            The holderTypeId to set.
	 */
	public void setHolderTypeId(String holderTypeId) {
		this.changed = true;
		this.holder_type_id = holderTypeId;
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
		this.codename = this.transferable.codename;
		this.parameter_type_id = this.transferable.parameter_type_id;
		this.holder_type_id = this.transferable.holder_type_id;
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
	 * @param parameterTypeId
	 *            The parameterTypeId to set.
	 */
	public void setParameterTypeId(String parameterTypeId) {
		this.changed = true;
		this.parameter_type_id = parameterTypeId;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.codename = this.codename;
		this.transferable.parameter_type_id = this.parameter_type_id;
		this.transferable.holder_type_id = this.holder_type_id;
		this.changed = false;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.codename = (String) in.readObject();
		this.parameter_type_id = (String) in.readObject();
		this.holder_type_id = (String) in.readObject();
		this.transferable = new ActionParameterType_Transferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeObject(this.codename);
		out.writeObject(this.parameter_type_id);
		out.writeObject(this.holder_type_id);
		this.changed = false;
	}
}