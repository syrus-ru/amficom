/*
 * $Id: Evaluation.java,v 1.1 2004/08/18 13:13:22 bob Exp $
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
public class Evaluation implements ObjectResource, Serializable {

	private static final long						serialVersionUID	= 01L;
	public transient static final String			TYPE				= "evaluation";
	/**
	 * @deprecated use TYPE
	 */
	public transient static final String			typ					= TYPE;

	private List									argumentList		= new ArrayList();
	private long									deleted				= 0;
	private String									description			= "";
	private String									etalonId			= "";
	private String									id					= "";
	private long									modified			= 0;
	private String									monitoredElementId	= "";
	private String									name				= "";
	private List									parameterList		= new ArrayList();
	private String[]								resultIds			= new String[0];
	private String									thresholdSetId		= "";
	private transient ClientEvaluation_Transferable	transferable;
	private String									typeId				= "";
	private String									userId				= "";

	private boolean									changed				= false;

	public Evaluation(ClientEvaluation_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Evaluation(String id) {
		//parameters = new Vector();
		//arguments = new Vector();
		this.id = id;
		this.transferable = new ClientEvaluation_Transferable();
	}

	public void addArgument(Parameter argument) {
		this.argumentList.add(argument);
	}

	public void addParameter(Parameter parameter) {
		this.parameterList.add(parameter);
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return this.argumentList;
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return this.deleted;
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
	 * @return Returns the etalonId.
	 */
	public String getEtalonId() {
		return this.etalonId;
	}

	public String getId() {
		return this.id;
	}

	public long getModified() {
		return this.modified;
	}

	/**
	 * @return Returns the monitoredElementId.
	 */
	public String getMonitoredElementId() {
		return this.monitoredElementId;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the parameterList.
	 */
	public List getParameterList() {
		return this.parameterList;
	}

	/**
	 * @return Returns the resultIds.
	 */
	public String[] getResultIds() {
		return this.resultIds;
	}

	/**
	 * @return Returns the thresholdSetId.
	 */
	public String getThresholdSetId() {
		return this.thresholdSetId;
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
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * @param argumentList
	 *            The argumentList to set.
	 */
	public void setArgumentList(List argumentList) {
		this.changed = true;
		this.argumentList = argumentList;
	}

	/**
	 * @param deleted
	 *            The deleted to set.
	 */
	public void setDeleted(long deleted) {
		this.changed = true;
		this.deleted = deleted;
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
	 * @param ethalonId
	 *            The ethalonId to set.
	 */
	public void setEthalonId(String ethalonId) {
		this.changed = true;
		this.etalonId = ethalonId;
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
		this.modified = this.transferable.modified;
		this.deleted = this.transferable.deleted;
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.typeId = this.transferable.type_id;
		this.userId = this.transferable.user_id;
		this.etalonId = this.transferable.etalon_id;
		this.monitoredElementId = this.transferable.monitored_element_id;
		this.thresholdSetId = this.transferable.threshold_set_id;
		this.description = this.transferable.description;

		this.resultIds = this.transferable.result_ids;

		this.argumentList.clear();

		EvaluationType et = (EvaluationType) Pool.get(EvaluationType.TYPE, this.typeId);

		for (int i = 0; i < this.transferable.arguments.length; i++) {
			Parameter param = new Parameter(this.transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.setApt((ActionParameterType) et.getSortedArguments().get(param.getCodename()));
			//arguments.add(param);
			this.addArgument(param);
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
	 * @param monitoredElementId
	 *            The monitoredElementId to set.
	 */
	public void setMonitoredElementId(String monitoredElementId) {
		this.changed = true;
		this.monitoredElementId = monitoredElementId;
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
	 * @param parameterList
	 *            The parameterList to set.
	 */
	public void setParameterList(List parameterList) {
		this.changed = true;
		this.parameterList = parameterList;
	}

	/**
	 * @param resultIds
	 *            The resultIds to set.
	 */
	public void setResultIds(String[] resultIds) {
		this.changed = true;
		this.resultIds = resultIds;
	}

	/**
	 * @param thresholdSetId
	 *            The thresholdSetId to set.
	 */
	public void setThresholdSetId(String thresholdSetId) {
		this.changed = true;
		this.thresholdSetId = thresholdSetId;
	}

	//	/**
	//	 * @param transferable
	//	 * The transferable to set.
	//	 */
	//	public void setTransferable(ClientEvaluation_Transferable transferable) {
	//		this.transferable = transferable;
	//	}

	public void setTransferableFromLocal() {
		this.transferable.description = this.description;
		this.transferable.modified = this.modified;
		this.transferable.deleted = this.deleted;
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.type_id = this.typeId;
		this.transferable.user_id = this.userId;
		this.transferable.etalon_id = this.etalonId;
		this.transferable.monitored_element_id = this.monitoredElementId;
		this.transferable.threshold_set_id = this.thresholdSetId;

		this.transferable.result_ids = new String[0];

		this.transferable.arguments = new ClientParameter_Transferable[this.argumentList.size()];
		{
			int i = 0;
			for (Iterator it = this.argumentList.iterator(); it.hasNext(); i++) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.arguments[i] = (ClientParameter_Transferable) argument.getTransferable();
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

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.changed = true;
		this.userId = userId;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.modified = in.readLong();
		this.userId = (String) in.readObject();
		this.deleted = in.readLong();
		this.description = (String) in.readObject();
		this.etalonId = (String) in.readObject();
		this.thresholdSetId = (String) in.readObject();
		this.monitoredElementId = (String) in.readObject();
		this.typeId = (String) in.readObject();
		this.resultIds = (String[]) in.readObject();
		this.parameterList = (List) in.readObject();
		this.argumentList = (List) in.readObject();

		this.transferable = new ClientEvaluation_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.modified);
		out.writeObject(this.userId);
		out.writeLong(this.deleted);
		out.writeObject(this.description);
		out.writeObject(this.etalonId);
		out.writeObject(this.thresholdSetId);
		out.writeObject(this.monitoredElementId);
		out.writeObject(this.typeId);
		out.writeObject(this.resultIds);
		out.writeObject(this.parameterList);
		out.writeObject(this.argumentList);
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isChanged() {
		return this.changed;
	}

	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}

	public ObjectResourceModel getModel() {
		throw new UnsupportedOperationException();
	}
}

