/*
 * $Id: TestSetup.java,v 1.1 2004/08/18 13:13:22 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems. 
 * Научно-технический центр. 
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class TestSetup implements ObjectResource, Serializable {

	private static final long		serialVersionUID	= 01L;

	public static final String		TYPE				= "testsetup";
	/**
	 * @deprecated use TYPE
	 */
	public static final String		typ					= TYPE;

	private String					analysisTypeId		= "";
	private long					created				= 0;
	private String					createdBy			= "";
	private String					criteriaSetId		= "";
	private String					description			= "";
	private String					etalonId			= "";
	private String					evaluationTypeId	= "";
	private String					id					= "";
	private long					modified			= 0;
	private String[]				monitoredElementIds;
	private String					name				= "";
	private String					testArgumentSetId	= "";
	private String					testTypeId			= "";
	private String					thresholdSetId		= "";

	private boolean					changed				= false;

	private TestSetup_Transferable	transferable;

	public TestSetup() {
		this.monitoredElementIds = new String[0];
		this.transferable = new TestSetup_Transferable();
	}

	public TestSetup(TestSetup_Transferable transferable) {
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
	 * @return Returns the criteriaSetId.
	 */
	public String getCriteriaSetId() {
		return this.criteriaSetId;
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
	 * @return Returns the ethalonId.
	 */
	public String getEthalonId() {
		return this.etalonId;
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

	/**
	 * @return Returns the modified.
	 */
	public long getModified() {
		return this.modified;
	}

	/**
	 * @return Returns the monitoredElementIds.
	 */
	public String[] getMonitoredElementIds() {
		return this.monitoredElementIds;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the testArgumentSetId.
	 */
	public String getTestArgumentSetId() {
		return this.testArgumentSetId;
	}

	/**
	 * @return Returns the testTypeId.
	 */
	public String getTestTypeId() {
		return this.testTypeId;
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
	 * @param criteriaSetId
	 *            The criteriaSetId to set.
	 */
	public void setCriteriaSetId(String criteriaSetId) {
		this.changed = true;
		this.criteriaSetId = criteriaSetId;
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
		this.modified = this.transferable.modified;
		this.createdBy = this.transferable.created_by;
		this.description = this.transferable.description;

		this.testTypeId = this.transferable.test_type_id;
		this.testArgumentSetId = this.transferable.test_argument_set_id;

		this.analysisTypeId = this.transferable.analysis_type_id;
		this.criteriaSetId = this.transferable.criteria_set_id;

		this.evaluationTypeId = this.transferable.evaluation_type_id;
		this.thresholdSetId = this.transferable.threshold_set_id;
		this.etalonId = this.transferable.etalon_id;

		this.monitoredElementIds = this.transferable.monitored_element_ids;
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
	 * @param monitoredElementIds
	 *            The monitoredElementIds to set.
	 */
	public void setMonitoredElementIds(String[] monitoredElementIds) {
		this.changed = true;
		this.monitoredElementIds = monitoredElementIds;
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
	 * @param testArgumentSetId
	 *            The testArgumentSetId to set.
	 */
	public void setTestArgumentSetId(String testArgumentSetId) {
		this.changed = true;
		this.testArgumentSetId = testArgumentSetId;
	}

	/**
	 * @param testTypeId
	 *            The testTypeId to set.
	 */
	public void settestTypeId(String testTypeId) {
		this.changed = true;
		this.testTypeId = testTypeId;
	}

	/**
	 * @param thresholdSetId
	 *            The thresholdSetId to set.
	 */
	public void setThresholdSetId(String thresholdSetId) {
		this.changed = true;
		this.thresholdSetId = thresholdSetId;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.created = this.created;
		this.transferable.modified = this.modified;
		this.transferable.created_by = this.createdBy;
		this.transferable.description = this.description;

		this.transferable.test_type_id = this.testTypeId;
		this.transferable.test_argument_set_id = this.testArgumentSetId;

		this.transferable.analysis_type_id = this.analysisTypeId;
		this.transferable.criteria_set_id = this.criteriaSetId;

		this.transferable.evaluation_type_id = this.evaluationTypeId;
		this.transferable.threshold_set_id = this.thresholdSetId;
		this.transferable.etalon_id = this.etalonId;

		this.transferable.monitored_element_ids = new String[0];
		this.changed = false;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.modified = in.readLong();
		this.createdBy = (String) in.readObject();
		this.description = (String) in.readObject();

		this.testTypeId = (String) in.readObject();
		this.testArgumentSetId = (String) in.readObject();

		this.analysisTypeId = (String) in.readObject();
		this.criteriaSetId = (String) in.readObject();

		this.evaluationTypeId = (String) in.readObject();
		this.thresholdSetId = (String) in.readObject();
		this.etalonId = (String) in.readObject();

		Object o = in.readObject();
		this.monitoredElementIds = (String[]) o;

		this.transferable = new TestSetup_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeLong(this.modified);
		out.writeObject(this.createdBy);
		out.writeObject(this.description);

		out.writeObject(this.testTypeId);
		out.writeObject(this.testArgumentSetId);

		out.writeObject(this.analysisTypeId);
		out.writeObject(this.criteriaSetId);

		out.writeObject(this.evaluationTypeId);
		out.writeObject(this.thresholdSetId);
		out.writeObject(this.etalonId);
		Object o = this.monitoredElementIds;
		out.writeObject(o);
		this.changed = false;
	}
	
	
	public ObjectResourceModel getModel() {
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

