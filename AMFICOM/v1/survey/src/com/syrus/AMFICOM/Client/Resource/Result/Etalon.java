package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class Etalon extends ObjectResource implements Serializable {

	private static final long			serialVersionUID		= 01L;
	public static final String			typ						= "etalon";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long							created					= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						description				= "";
	/**
	 * @deprecated use setter/getter pair for ethalonParameterList to access
	 *             this field
	 */
	public Vector						etalon_parameters		= new Vector();
	private List						ethalonParameterList	= new ArrayList();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						id						= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long							modified				= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						name					= "";

	private ClientEtalon_Transferable	transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						type_id					= "";

	public Etalon() {
		this.transferable = new ClientEtalon_Transferable();
	}

	public Etalon(ClientEtalon_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return this.created;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the ethalonParameterList.
	 */
	public List getEthalonParameterList() {
		return this.ethalonParameterList;
	}

	public String getId() {
		return this.id;
	}

	/**
	 * @return Returns the modified.
	 */
	public long getModified() {
		return this.modified;
	}

	public String getName() {
		return this.name;
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
		return this.type_id;
	}

	/**
	 * @param created
	 *            The created to set.
	 */
	public void setCreated(long created) {
		this.created = created;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param ethalonParameterList
	 *            The ethalonParameterList to set.
	 */
	public void setEthalonParameterList(List ethalonParameterList) {
		this.ethalonParameterList = ethalonParameterList;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setLocalFromTransferable() {
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.created = this.transferable.created;
		this.description = this.transferable.description;
		this.type_id = this.transferable.type_id;
		this.modified = this.transferable.modified;

		this.etalon_parameters.clear();
		this.ethalonParameterList.clear();
		for (int i = 0; i < this.transferable.etalon_parameters.length; i++) {
			Parameter parameter = new Parameter(
					this.transferable.etalon_parameters[i]);
			this.etalon_parameters.add(parameter);
			this.ethalonParameterList.add(parameter);

		}
	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(long modified) {
		this.modified = modified;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.created = this.created;
		this.transferable.description = this.description;
		this.transferable.type_id = this.type_id;
		this.transferable.modified = this.modified;

		//		transferable.etalon_parameters = new
		// ClientParameter_Transferable[etalon_parameters
		//				.size()];
		//		for (int i = 0; i < transferable.etalon_parameters.length; i++) {
		//			Parameter etalon_parameter = (Parameter) etalon_parameters.get(i);
		//			etalon_parameter.setTransferableFromLocal();
		//			transferable.etalon_parameters[i] = (ClientParameter_Transferable)
		// etalon_parameter
		//					.getTransferable();
		//		}
		if (this.ethalonParameterList.isEmpty()) {
			this.transferable.etalon_parameters = new ClientParameter_Transferable[this.etalon_parameters
					.size()];
			for (int i = 0; i < this.transferable.etalon_parameters.length; i++) {
				Parameter argument = (Parameter) this.etalon_parameters.get(i);
				argument.setTransferableFromLocal();
				this.transferable.etalon_parameters[i] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.etalon_parameters.size(); i++) {
				Object obj = this.etalon_parameters.get(i);
				map.put(obj, obj);
			}
			for (Iterator it=ethalonParameterList.iterator();it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.etalon_parameters = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.etalon_parameters[i++] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		}
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(String typeId) {
		this.type_id = typeId;
	}

	public void updateLocalFromTransferable() {
		// nothing to do
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.description = (String) in.readObject();
		this.type_id = (String) in.readObject();
		this.etalon_parameters = (Vector) in.readObject();
		this.ethalonParameterList = (List) in.readObject();
		this.modified = in.readLong();

		this.transferable = new ClientEtalon_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.description);
		out.writeObject(this.type_id);
		out.writeObject(this.etalon_parameters);
		out.writeObject(this.ethalonParameterList);
		out.writeLong(this.modified);
	}
}

