package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;

public class TestSetup extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	/**
	 * @deprecated
	 */
	public static final String typ = "testsetup";

	private TestSetup_Transferable transferable;
	/**
	 * @deprecated
	 */
	public String id = "";
	/**
	 * @deprecated
	 */
	public String name = "";
	/**
	 * @deprecated
	 */	
	public long created = 0;
	public long modified = 0;
	public String created_by = "";
	public String description = "";

	public String test_type_id = "";
	public String test_argument_set_id = "";

	public String analysis_type_id = "";
	public String criteria_set_id = "";

	public String evaluation_type_id = "";
	public String threshold_set_id = "";
	public String etalon_id = "";

	public String[] monitored_element_ids;

	public TestSetup()
	{
		monitored_element_ids = new String[0];
		transferable = new TestSetup_Transferable();
	}

	public TestSetup(TestSetup_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public static String getTyp()
	{
		return typ;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		created = transferable.created;
		modified = transferable.modified;
		created_by = transferable.created_by;
		description = transferable.description;

		test_type_id = transferable.test_type_id;
		test_argument_set_id = transferable.test_argument_set_id;

		analysis_type_id = transferable.analysis_type_id;
		criteria_set_id = transferable.criteria_set_id;

		evaluation_type_id = transferable.evaluation_type_id;
		threshold_set_id = transferable.threshold_set_id;
		etalon_id = transferable.etalon_id;

		monitored_element_ids = transferable.monitored_element_ids;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.created = created;
		transferable.modified = modified;
		transferable.created_by = created_by;
		transferable.description = description;

		transferable.test_type_id = test_type_id;
		transferable.test_argument_set_id = test_argument_set_id;

		transferable.analysis_type_id = analysis_type_id;
		transferable.criteria_set_id = criteria_set_id;

		transferable.evaluation_type_id = evaluation_type_id;
		transferable.threshold_set_id = threshold_set_id;
		transferable.etalon_id = etalon_id;

		transferable.monitored_element_ids = new String[0];
	}

	public void updateLocalFromTransferable()
	{
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(created);
		out.writeLong(modified);
		out.writeObject(created_by);
		out.writeObject(description);

		out.writeObject(test_type_id);
		out.writeObject(test_argument_set_id);

		out.writeObject(analysis_type_id);
		out.writeObject(criteria_set_id);

		out.writeObject(evaluation_type_id);
		out.writeObject(threshold_set_id);
		out.writeObject(etalon_id);
		Object o = monitored_element_ids;
		out.writeObject(o);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		created = in.readLong();
		modified = in.readLong();
		created_by = (String )in.readObject();
		description = (String )in.readObject();

		test_type_id = (String )in.readObject();
		test_argument_set_id = (String )in.readObject();

		analysis_type_id = (String )in.readObject();
		criteria_set_id = (String )in.readObject();

		evaluation_type_id = (String )in.readObject();
		threshold_set_id = (String )in.readObject();
		etalon_id = (String )in.readObject();

		Object o = in.readObject();
		monitored_element_ids = (String[] )o;

		transferable = new TestSetup_Transferable();
		updateLocalFromTransferable();
	}
	/**
	 * @return Returns the analysisTypeId.
	 */
	public String getAnalysisTypeId() {
		return analysis_type_id;
	}
	/**
	 * @param analysisTypeId The analysisTypeId to set.
	 */
	public void setAnalysis_type_id(String analysisTypeId) {
		this.analysis_type_id = analysisTypeId;
	}
	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(long created) {
		this.created = created;
	}
	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return created_by;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(String createdBy) {
		this.created_by = createdBy;
	}
	/**
	 * @return Returns the criteriaSetId.
	 */
	public String getCriteriaSetId() {
		return criteria_set_id;
	}
	/**
	 * @param criteriaSetId The criteriaSetId to set.
	 */
	public void setCriteriaSetId(String criteriaSetId) {
		this.criteria_set_id = criteriaSetId;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the ethalonId.
	 */
	public String getEthalonId() {
		return etalon_id;
	}
	/**
	 * @param ethalonId The ethalonId to set.
	 */
	public void setEthalonId(String ethalonId) {
		this.etalon_id = ethalonId;
	}
	/**
	 * @return Returns the evaluationTypeId.
	 */
	public String getEvaluationTypeId() {
		return evaluation_type_id;
	}
	/**
	 * @param evaluationTypeId The evaluationTypeId to set.
	 */
	public void setEvaluationTypeId(String evaluationTypeId) {
		this.evaluation_type_id = evaluationTypeId;
	}
	/**
	 * @return Returns the modified.
	 */
	public long getModified() {
		return modified;
	}
	/**
	 * @param modified The modified to set.
	 */
	public void setModified(long modified) {
		this.modified = modified;
	}
	/**
	 * @return Returns the monitoredElementIds.
	 */
	public String[] getMonitoredElementIds() {
		return monitored_element_ids;
	}
	/**
	 * @param monitoredElementIds The monitoredElementIds to set.
	 */
	public void setMonitoredElementIds(String[] monitoredElementIds) {
		this.monitored_element_ids = monitoredElementIds;
	}
	/**
	 * @return Returns the testArgumentSetId.
	 */
	public String getTestArgumentSetId() {
		return test_argument_set_id;
	}
	/**
	 * @param testArgumentSetId The testArgumentSetId to set.
	 */
	public void setTestArgumentSetId(String testArgumentSetId) {
		this.test_argument_set_id = testArgumentSetId;
	}
	/**
	 * @return Returns the testTypeId.
	 */
	public String getTestTypeId() {
		return test_type_id;
	}
	/**
	 * @param testTypeId The testTypeId to set.
	 */
	public void settestTypeId(String testTypeId) {
		this.test_type_id = testTypeId;
	}
	/**
	 * @return Returns the thresholdSetId.
	 */
	public String getThresholdSetId() {
		return threshold_set_id;
	}
	/**
	 * @param thresholdSetId The thresholdSetId to set.
	 */
	public void setThresholdSetId(String thresholdSetId) {
		this.threshold_set_id = thresholdSetId;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}

