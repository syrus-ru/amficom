package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class ThresholdSet extends ObjectResource implements Serializable {

	private static final long				serialVersionUID	= 01L;

	public static final String				typ					= "thresholdset";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long								created				= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							created_by			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							evaluation_type_id	= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String							name				= "";
	private List							thresholdList;
	/**
	 * @deprecated use setter/getter pair for thresholdList to access this field
	 */
	public Vector							thresholds;

	private ClientThresholdSet_Transferable	transferable;

	public ThresholdSet() {
		this.transferable = new ClientThresholdSet_Transferable();
		this.thresholds = new Vector();
		this.thresholdList = new ArrayList();
	}

	public ThresholdSet(ClientThresholdSet_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return created_by;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the evaluationTypeId.
	 */
	public String getEvaluationTypeId() {
		return evaluation_type_id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Returns the thresholdList.
	 */
	public List getThresholdList() {
		return thresholdList;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
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
		this.created_by = createdBy;
	}

	/**
	 * @param evaluationTypeId
	 *            The evaluationTypeId to set.
	 */
	public void setEvaluationTypeId(String evaluationTypeId) {
		this.changed = true;
		this.evaluation_type_id = evaluationTypeId;
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
		this.created_by = this.transferable.created_by;
		this.evaluation_type_id = this.transferable.evaluation_type_id;

		this.thresholds.clear();
		this.thresholdList.clear();
		for (int i = 0; i < this.transferable.thresholds.length; i++) {
			Parameter param = new Parameter(this.transferable.thresholds[i]);
			this.thresholds.add(param);
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
		this.transferable.created_by = this.created_by;
		this.transferable.evaluation_type_id = this.evaluation_type_id;

		/**
		 * @todo only for backward thresholds Vector implementation
		 */
		if (this.thresholds.isEmpty()) {
			this.transferable.thresholds = new ClientParameter_Transferable[this.thresholdList
					.size()];
			int i = 0;
			for (Iterator it = this.thresholdList.iterator(); it.hasNext();) {
				Parameter criteria = (Parameter) it.next();
				criteria.setTransferableFromLocal();
				this.transferable.thresholds[i++] = (ClientParameter_Transferable) criteria
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.thresholds.size(); i++) {
				Object obj = this.thresholds.get(i);
				map.put(obj, obj);
			}
			for (Iterator it = this.thresholdList.iterator(); it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.thresholds = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter criteria = (Parameter) it.next();
				criteria.setTransferableFromLocal();
				this.transferable.thresholds[i++] = (ClientParameter_Transferable) criteria
						.getTransferable();
			}
		}
		this.changed = false;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.created = in.readLong();
		this.created_by = (String) in.readObject();
		this.evaluation_type_id = (String) in.readObject();
		this.thresholds = (Vector) in.readObject();
		this.thresholdList = (List) in.readObject();

		this.transferable = new ClientThresholdSet_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.created);
		out.writeObject(this.created_by);
		out.writeObject(this.evaluation_type_id);
		out.writeObject(this.thresholds);
		out.writeObject(this.thresholdList);
		this.changed = false;
	}
}

