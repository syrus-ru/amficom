/*
 * $Id: CriteriaSet.java,v 1.1 2004/08/18 13:13:22 bob Exp $
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
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class CriteriaSet implements ObjectResource, Serializable {

	private static final long				serialVersionUID	= 01L;
	public static final String				TYPE				= "criteriaset";
	/**
	 * @deprecated use TYPE
	 */
	public static final String				typ					= TYPE;
	private String							analysisTypeId		= "";
	private long							created				= 0;
	private String							createdBy			= "";
	private List							criteriaList		= new ArrayList();
	private String							id					= "";
	private String							name				= "";

	private boolean							changed				= false;

	private ClientCriteriaSet_Transferable	transferable;

	public CriteriaSet() {
		this.transferable = new ClientCriteriaSet_Transferable();
	}

	public CriteriaSet(ClientCriteriaSet_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the analysisTypeId.
	 */
	public String getAnalysisTypeId() {
		return this.analysisTypeId;
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

	/**
	 * @return Returns the criteriaList.
	 */
	public List getCriteriaList() {
		return this.criteriaList;
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

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @param analysisTypeId
	 *            The analysisTypeId to set.
	 */
	public void setAnalysisTypeId(String analysisTypeId) {
		this.changed = true;
		this.analysisTypeId = analysisTypeId;
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
	 * @param criteriaList
	 *            The criteriaList to set.
	 */
	public void setCriteriaList(List criteriaList) {
		this.changed = true;
		this.criteriaList = criteriaList;
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
		this.analysisTypeId = this.transferable.analysis_type_id;

		this.criteriaList.clear();
		for (int i = 0; i < this.transferable.criterias.length; i++) {
			Parameter parameter = new Parameter(this.transferable.criterias[i]);
			this.criteriaList.add(parameter);
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

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.created = this.created;
		this.transferable.created_by = this.createdBy;
		this.transferable.analysis_type_id = this.analysisTypeId;
		this.transferable.criterias = new ClientParameter_Transferable[this.criteriaList.size()];
		int i = 0;
		for (Iterator it = this.criteriaList.iterator(); it.hasNext(); i++) {
			Parameter criteria = (Parameter) it.next();
			criteria.setTransferableFromLocal();
			this.transferable.criterias[i] = (ClientParameter_Transferable) criteria.getTransferable();
		}
		this.changed = false;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.createdBy = (String) in.readObject();
		this.analysisTypeId = (String) in.readObject();
		this.criteriaList = (List) in.readObject();
		this.transferable = new ClientCriteriaSet_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.createdBy);
		out.writeObject(this.analysisTypeId);
		out.writeObject(this.criteriaList);
		this.changed = false;
	}

	public boolean isChanged() {
		return this.changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public long getModified() {
		throw new UnsupportedOperationException();
	}

	public ObjectResourceModel getModel() {
		throw new UnsupportedOperationException();
	}

	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}
}

