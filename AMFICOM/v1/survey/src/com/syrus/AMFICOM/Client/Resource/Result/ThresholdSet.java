package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class ThresholdSet extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	/**
	 * @deprecated
	 */	
	public static final String typ = "thresholdset";

	private ClientThresholdSet_Transferable transferable;
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
	/**
	 * @deprecated
	 */	
	public String created_by = "";
	/**
	 * @deprecated
	 */	
	public String evaluation_type_id = "";
	/**
	 * @deprecated
	 */	
	public Vector thresholds;
	private ArrayList thresholdsList;

	public ThresholdSet()
	{
		transferable = new ClientThresholdSet_Transferable();
		thresholds = new Vector();
		thresholdsList = new ArrayList();
	}

	public ThresholdSet(ClientThresholdSet_Transferable transferable)
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

	public Object getTransferable()
	{
		return transferable;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		created = transferable.created;
		created_by = transferable.created_by;
		evaluation_type_id = transferable.evaluation_type_id;

		thresholds.clear();
		thresholdsList.clear();
		for (int i=0; i<transferable.thresholds.length; i++){
			Parameter param = new Parameter(transferable.thresholds[i]);
			thresholds.add(param);
			thresholdsList.add(param);
		}
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.created = created;
		transferable.created_by = created_by;
		transferable.evaluation_type_id = evaluation_type_id;
		/**
		 * @todo recast with thresholdsList usage
		 */
		transferable.thresholds = new ClientParameter_Transferable[thresholds.size()];
		for (int i=0; i<transferable.thresholds.length; i++)
		{
			Parameter criteria = (Parameter)thresholds.get(i);
			criteria.setTransferableFromLocal();
			transferable.thresholds[i] = (ClientParameter_Transferable)criteria.getTransferable();
		}
	}

	public void updateLocalFromTransferable()
	{
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeLong(created);
		out.writeObject(created_by);
		out.writeObject(evaluation_type_id);
		out.writeObject(thresholds);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		created = in.readLong();
		created_by = (String )in.readObject();
		evaluation_type_id = (String )in.readObject();
		thresholds = (Vector )in.readObject();

		transferable = new ClientThresholdSet_Transferable();
		updateLocalFromTransferable();
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
	 * @deprecated use getThresholdsList
	 * @return Returns the thresholds.
	 */
	public Vector getThresholds() {
		return thresholds;
	}
	/**
	 * @deprecated use setThresholdsList
	 * @param thresholds The thresholds to set.
	 */
	public void setThresholds(Vector thresholds) {
		this.thresholds = thresholds;
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
	/**
	 * @return Returns the thresholdsList.
	 */
	public ArrayList getThresholdsList() {
		return thresholdsList;
	}
	/**
	 * @param thresholdsList The thresholdsList to set.
	 */
	public void setThresholdsList(ArrayList thresholdsList) {
		this.thresholdsList = thresholdsList;
	}
}


