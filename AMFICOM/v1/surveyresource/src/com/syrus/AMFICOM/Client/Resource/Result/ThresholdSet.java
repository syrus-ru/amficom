/*
 * $Id: ThresholdSet.java,v 1.1 2004/08/18 13:13:22 bob Exp $
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
public class ThresholdSet implements ObjectResource, Serializable {

	private static final long				serialVersionUID	= 01L;

	public static final String				TYPE				= "thresholdset";
	/**
	 * @deprecated use TYPE
	 */
	public static final String				typ					= TYPE;

	private long							created				= 0;
	private String							createdBy			= "";
	private String							evaluationTypeId	= "";
	private String							id					= "";
	private String							name				= "";
	private List							thresholdList;

	private ClientThresholdSet_Transferable	transferable;
	private boolean							changed				= false;

	public ThresholdSet() {
		this.transferable = new ClientThresholdSet_Transferable();
		this.thresholdList = new ArrayList();
	}

	public ThresholdSet(ClientThresholdSet_Transferable transferable) {
		this.transferable = transferable;
		this.thresholdList = new ArrayList();
		setLocalFromTransferable();
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

	/**
	 * @return Returns the evaluationTypeId.
	 */
	public String getEvaluationTypeId() {
		return this.evaluationTypeId;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the thresholdList.
	 */
	public List getThresholdList() {
		return this.thresholdList;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return TYPE;
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
	 * @param evaluationTypeId
	 *            The evaluationTypeId to set.
	 */
	public void setEvaluationTypeId(String evaluationTypeId) {
		this.changed = true;
		this.evaluationTypeId = evaluationTypeId;
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
		this.evaluationTypeId = this.transferable.evaluation_type_id;

		this.thresholdList.clear();
		for (int i = 0; i < this.transferable.thresholds.length; i++) {
			Parameter param = new Parameter(this.transferable.thresholds[i]);
			this.thresholdList.add(param);
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
	 * @param thresholdList
	 *            The thresholdList to set.
	 */
	public void setThresholdList(List thresholdsList) {
		this.changed = true;
		this.thresholdList = thresholdsList;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.created = this.created;
		this.transferable.created_by = this.createdBy;
		this.transferable.evaluation_type_id = this.evaluationTypeId;

		{
			this.transferable.thresholds = new ClientParameter_Transferable[this.thresholdList.size()];
			int i = 0;
			for (Iterator it = this.thresholdList.iterator(); it.hasNext(); i++) {
				Parameter criteria = (Parameter) it.next();
				criteria.setTransferableFromLocal();
				this.transferable.thresholds[i] = (ClientParameter_Transferable) criteria.getTransferable();
			}
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
		this.evaluationTypeId = (String) in.readObject();
		this.thresholdList = (List) in.readObject();

		this.transferable = new ClientThresholdSet_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.createdBy);
		out.writeObject(this.evaluationTypeId);
		out.writeObject(this.thresholdList);
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

