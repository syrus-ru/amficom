/*
 * $Id: Analysis.java,v 1.1 2004/08/18 13:13:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ClientAnalysis_Transferable;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.Client.Survey.Result.UI.AnalysisDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;

import java.io.IOException;
import java.io.Serializable;

import java.util.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:21 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class Analysis implements ObjectResource, Serializable {

	private static final long						serialVersionUID	= 01L;
	public transient static final String			TYPE				= "analysis";
	/**
	 * @deprecated use TYPE
	 */
	public transient static final String			typ					= TYPE;

	private List									argumentList		= new ArrayList();
	private String									criteriaSetId		= "";
	private long									deleted				= 0;
	private String									description			= "";
	private String									id					= "";
	private long									modified			= 0;
	private String									monitoredElementId	= "";
	private String									name				= "";
	private List									parameterList		= new ArrayList();
	private String									result_id			= "";
	private String[]								resultIds			= new String[0];
	private transient ClientAnalysis_Transferable	transferable;
	private String									typeId				= "";
	private String									userId				= "";

	private boolean									changed				= false;

	public Analysis(ClientAnalysis_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Analysis(String id) {
		this.id = id;
		this.transferable = new ClientAnalysis_Transferable();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new AnalysisDisplayModel();
	}

	public static PropertiesPanel getPropertyPane() {
		return new GeneralPanel();
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
	 * @return Returns the criteriaSetId.
	 */
	public String getCriteriaSetId() {
		return this.criteriaSetId;
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
		throw new UnsupportedOperationException();
	}

	public String getId() {
		return this.id;
	}

	public ObjectResourceModel getModel() {
		return new AnalysisModel(this);
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
	 * @return Returns the resultId.
	 */
	public String getResultId() {
		return this.result_id;
	}

	/**
	 * @return Returns the resultIds.
	 */
	public String[] getResultIds() {
		return this.resultIds;
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
	 * @param criteriaSetId
	 *            The criteriaSetId to set.
	 */
	public void setCriteriaSetId(String criteriaSetId) {
		this.changed = true;
		this.criteriaSetId = criteriaSetId;
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
		this.description = this.transferable.description;
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.userId = this.transferable.user_id;
		this.monitoredElementId = this.transferable.monitored_element_id;
		this.typeId = this.transferable.type_id;
		this.criteriaSetId = this.transferable.criteria_set_id;

		this.resultIds = this.transferable.result_ids;

		this.argumentList.clear();
		AnalysisType at = (AnalysisType) Pool.get(AnalysisType.typ, this.typeId);

		for (int i = 0; i < this.transferable.arguments.length; i++) {
			Parameter param = new Parameter(this.transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.setApt((ActionParameterType) at.getSortedArguments().get(param.getCodename()));
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
	 * @param resultId
	 *            The resultId to set.
	 */
	public void setResultId(String resultId) {
		this.changed = true;
		this.result_id = resultId;
	}

	/**
	 * @param resultIds
	 *            The resultIds to set.
	 */
	public void setResultIds(String[] resultIds) {
		this.changed = true;
		this.resultIds = resultIds;
	}

	public void setTransferableFromLocal() {
		this.transferable.modified = this.modified;
		this.transferable.deleted = this.deleted;
		this.transferable.description = this.description;
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.user_id = this.userId;
		this.transferable.monitored_element_id = this.monitoredElementId;
		this.transferable.type_id = this.typeId;
		this.transferable.result_ids = new String[0];
		this.transferable.criteria_set_id = this.criteriaSetId;

		{
			int i = 0;
			this.transferable.arguments = new ClientParameter_Transferable[this.argumentList.size()];
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
		//nothing to do
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.modified = in.readLong();
		this.userId = (String) in.readObject();
		this.deleted = in.readLong();
		this.description = (String) in.readObject();
		this.result_id = (String) in.readObject();
		this.criteriaSetId = (String) in.readObject();
		this.monitoredElementId = (String) in.readObject();
		this.typeId = (String) in.readObject();
		this.resultIds = (String[]) in.readObject();
		this.parameterList = (List) in.readObject();
		this.argumentList = (List) in.readObject();

		this.transferable = new ClientAnalysis_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.modified);
		out.writeObject(this.userId);
		out.writeLong(this.deleted);
		out.writeObject(this.description);
		out.writeObject(this.result_id);
		out.writeObject(this.criteriaSetId);
		out.writeObject(this.monitoredElementId);
		out.writeObject(this.typeId);
		out.writeObject(this.resultIds);
		out.writeObject(this.parameterList);
		out.writeObject(this.argumentList);
		this.changed = false;
	}

	public boolean isChanged() {
		return this.changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}
}

