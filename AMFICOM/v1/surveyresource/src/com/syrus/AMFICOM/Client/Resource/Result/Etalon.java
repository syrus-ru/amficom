///*
// * $Id: Etalon.java,v 1.1 2004/08/18 13:13:22 bob Exp $
// *
// * Copyright © 2004 Syrus Systems.
// * Научно-технический центр.
// * Проект: АМФИКОМ.
// */
package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class Etalon implements ObjectResource, Serializable {

	private static final long			serialVersionUID		= 01L;
	public static final String			TYPE						= "etalon";
	/**
	 * @deprecated use TYPE
	 */
	public static final String			typ						= TYPE;

	private long						created					= 0;
	private String						description				= "";
	private List						etalonParameterList	= new ArrayList();
	private String						id						= "";
	private long						modified				= 0;
	private String						name					= "";
	private ClientEtalon_Transferable	transferable;
	private String						typeId					= "";

	private boolean						changed					= false;

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
		throw new UnsupportedOperationException();
	}

	/**
	 * @return Returns the etalonParameterList.
	 */
	public List getEthalonParameterList() {
		return this.etalonParameterList;
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
		return this.typeId;
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
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.changed = true;
		this.description = description;
	}

	/**
	 * @param etalonParameterList
	 *            The etalonParameterList to set.
	 */
	public void setEthalonParameterList(List ethalonParameterList) {
		this.changed = true;
		this.etalonParameterList = ethalonParameterList;
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
		this.description = this.transferable.description;
		this.typeId = this.transferable.type_id;
		this.modified = this.transferable.modified;

		this.etalonParameterList.clear();
		for (int i = 0; i < this.transferable.etalon_parameters.length; i++) {
			Parameter parameter = new Parameter(this.transferable.etalon_parameters[i]);
			this.etalonParameterList.add(parameter);

		}
		this.changed = false;
	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(long modified) {
		this.changed = true;
		this.modified = modified;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.created = this.created;
		this.transferable.description = this.description;
		this.transferable.type_id = this.typeId;
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
		 {
			this.transferable.etalon_parameters = new ClientParameter_Transferable[this.etalonParameterList.size()];
			int i = 0;
			for (Iterator it=this.etalonParameterList.iterator();it.hasNext(); i++) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.etalon_parameters[i] = (ClientParameter_Transferable) argument.getTransferable();
			}
		} 
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

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.description = (String) in.readObject();
		this.typeId = (String) in.readObject();
		this.etalonParameterList = (List) in.readObject();
		this.modified = in.readLong();

		this.transferable = new ClientEtalon_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.description);
		out.writeObject(this.typeId);
		out.writeObject(this.etalonParameterList);
		out.writeLong(this.modified);
	}

	public boolean isChanged() {
		return this.changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}


	public ObjectResourceModel getModel() {
		throw new UnsupportedOperationException();
	}
	
	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}
}

