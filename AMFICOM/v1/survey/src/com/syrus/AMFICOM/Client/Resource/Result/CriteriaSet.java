package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class CriteriaSet extends ObjectResource implements Serializable {

	private static final long				serialVersionUID	= 01L;
	public static final String				typ					= "criteriaset";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							analysis_type_id	= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long								created				= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							created_by			= "";
	private List							criteriaList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public Vector							criterias			= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							name				= "";

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
		return this.analysis_type_id;
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
		return this.created_by;
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
		this.analysis_type_id = analysisTypeId;
	}

	/**
	 * @param created
	 *            The created to set.
	 */
	public void setCreated(long created) {
		this.created = created;
	}

	/**
	 * @param createdBy
	 *            The createdBy to set.
	 */
	public void setCreatedBy(String createdBy) {
		this.created_by = createdBy;
	}

	/**
	 * @param criteriaList
	 *            The criteriaList to set.
	 */
	public void setCriteriaList(List criteriaList) {
		this.criteriaList = criteriaList;
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
		this.created_by = this.transferable.created_by;
		this.analysis_type_id = this.transferable.analysis_type_id;

		this.criterias.clear();
		this.criteriaList.clear();
		for (int i = 0; i < this.transferable.criterias.length; i++) {
			Parameter parameter = new Parameter(this.transferable.criterias[i]);
			this.criterias.add(parameter);
			this.criteriaList.add(parameter);
		}
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
		this.transferable.created_by = this.created_by;
		this.transferable.analysis_type_id = this.analysis_type_id;

		//		transferable.criterias = new ClientParameter_Transferable[criterias
		//				.size()];
		//		for (int i = 0; i < transferable.criterias.length; i++) {
		//			Parameter criteria = (Parameter) criterias.get(i);
		//			criteria.setTransferableFromLocal();
		//			transferable.criterias[i] = (ClientParameter_Transferable) criteria
		//					.getTransferable();
		//		}
		if (this.criteriaList.size() == 0) {
			this.transferable.criterias = new ClientParameter_Transferable[this.criterias
					.size()];
			for (int i = 0; i < this.transferable.criterias.length; i++) {
				Parameter criteria = (Parameter) this.criterias.get(i);
				criteria.setTransferableFromLocal();
				this.transferable.criterias[i] = (ClientParameter_Transferable) criteria
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.criterias.size(); i++) {
				Object obj = this.criterias.get(i);
				map.put(obj, obj);
			}
			for (int i = 0; i < this.criteriaList.size(); i++) {
				Object obj = this.criteriaList.get(i);
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.criterias = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter criteria = (Parameter) it.next();
				criteria.setTransferableFromLocal();
				this.transferable.criterias[i++] = (ClientParameter_Transferable) criteria
						.getTransferable();
			}
		}
	}

	public void updateLocalFromTransferable() {
		// nothing to do
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.created_by = (String) in.readObject();
		this.analysis_type_id = (String) in.readObject();
		this.criterias = (Vector) in.readObject();
		this.criteriaList = (List) in.readObject();
		this.transferable = new ClientCriteriaSet_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.created_by);
		out.writeObject(this.analysis_type_id);
		out.writeObject(this.criterias);
		out.writeObject(this.criteriaList);
	}
}

