package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class Evaluation extends ObjectResource implements Serializable {

	private static final long						serialVersionUID		= 01L;
	public transient static final String			typ						= "evaluation";
	private List									argumentList			= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector									arguments				= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long										deleted					= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									description				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									etalon_id				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									id						= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long										modified				= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									monitored_element_id	= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									name					= "";
	private List									parameterList			= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for parameterList to access this field
	 */
	public Vector									parameters				= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String[]									result_ids				= new String[0];
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									threshold_set_id		= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public transient ClientEvaluation_Transferable	transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									type_id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									user_id					= "";

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
		this.arguments.add(argument);
		this.argumentList.add(argument);
	}

	public void addParameter(Parameter parameter) {
		this.parameters.add(parameter);
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
		return this.etalon_id;
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
		return this.monitored_element_id;
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
		return this.result_ids;
	}

	/**
	 * @return Returns the thresholdSetId.
	 */
	public String getThresholdSetId() {
		return this.threshold_set_id;
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
		return this.type_id;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return this.user_id;
	}

	/**
	 * @param argumentList
	 *            The argumentList to set.
	 */
	public void setArgumentList(List argumentList) {
		this.argumentList = argumentList;
	}

	/**
	 * @param deleted
	 *            The deleted to set.
	 */
	public void setDeleted(long deleted) {
		this.deleted = deleted;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param ethalonId
	 *            The ethalonId to set.
	 */
	public void setEthalonId(String ethalonId) {
		this.etalon_id = ethalonId;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setLocalFromTransferable() {
		this.modified = this.transferable.modified;
		this.deleted = this.transferable.deleted;
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.type_id = this.transferable.type_id;
		this.user_id = this.transferable.user_id;
		this.etalon_id = this.transferable.etalon_id;
		this.monitored_element_id = this.transferable.monitored_element_id;
		this.threshold_set_id = this.transferable.threshold_set_id;
		this.description = this.transferable.description;

		this.result_ids = this.transferable.result_ids;

		this.arguments.clear();
		this.argumentList.clear();

		EvaluationType et = (EvaluationType) Pool.get(EvaluationType.typ,
				this.type_id);

		for (int i = 0; i < this.transferable.arguments.length; i++) {
			Parameter param = new Parameter(this.transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.setApt((ActionParameterType) et.sorted_arguments.get(param
					.getCodename()));
			//arguments.add(param);
			this.addArgument(param);
		}
	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(long modified) {
		this.modified = modified;
	}

	/**
	 * @param monitoredElementId
	 *            The monitoredElementId to set.
	 */
	public void setMonitoredElementId(String monitoredElementId) {
		this.monitored_element_id = monitoredElementId;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parameterList
	 *            The parameterList to set.
	 */
	public void setParameterList(List parameterList) {
		this.parameterList = parameterList;
	}

	/**
	 * @param resultIds
	 *            The resultIds to set.
	 */
	public void setResultIds(String[] resultIds) {
		this.result_ids = resultIds;
	}

	/**
	 * @param thresholdSetId
	 *            The thresholdSetId to set.
	 */
	public void setThresholdSetId(String thresholdSetId) {
		this.threshold_set_id = thresholdSetId;
	}

	/**
	 * @param transferable
	 *            The transferable to set.
	 */
	public void setTransferable(ClientEvaluation_Transferable transferable) {
		this.transferable = transferable;
	}

	public void setTransferableFromLocal() {
		this.transferable.description = this.description;
		this.transferable.modified = this.modified;
		this.transferable.deleted = this.deleted;
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.type_id = this.type_id;
		this.transferable.user_id = this.user_id;
		this.transferable.etalon_id = this.etalon_id;
		this.transferable.monitored_element_id = this.monitored_element_id;
		this.transferable.threshold_set_id = this.threshold_set_id;

		this.transferable.result_ids = new String[0];

		this.transferable.arguments = new ClientParameter_Transferable[this.arguments
				.size()];

		//		for (int i = 0; i < transferable.arguments.length; i++) {
		//			Parameter argument = (Parameter) arguments.get(i);
		//			argument.setTransferableFromLocal();
		//			transferable.arguments[i] = (ClientParameter_Transferable) argument
		//					.getTransferable();
		//		}
		if (this.argumentList.size() == 0) {
			this.transferable.arguments = new ClientParameter_Transferable[this.arguments
					.size()];
			for (int i = 0; i < this.transferable.arguments.length; i++) {
				Parameter argument = (Parameter) this.arguments.get(i);
				argument.setTransferableFromLocal();
				this.transferable.arguments[i] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.arguments.size(); i++) {
				Object obj = this.arguments.get(i);
				map.put(obj, obj);
			}
			for (Iterator it=this.argumentList.iterator();it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.arguments = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.arguments[i++] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		}
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(String typeId) {
		this.type_id = typeId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.user_id = userId;
	}

	public void updateLocalFromTransferable() {
		// nothing to do
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.modified = in.readLong();
		this.user_id = (String) in.readObject();
		this.deleted = in.readLong();
		this.description = (String) in.readObject();
		this.etalon_id = (String) in.readObject();
		this.threshold_set_id = (String) in.readObject();
		this.monitored_element_id = (String) in.readObject();
		this.type_id = (String) in.readObject();
		this.result_ids = (String[]) in.readObject();
		this.parameters = (Vector) in.readObject();
		this.parameterList = (List) in.readObject();
		this.arguments = (Vector) in.readObject();
		this.argumentList = (List) in.readObject();

		this.transferable = new ClientEvaluation_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.modified);
		out.writeObject(this.user_id);
		out.writeLong(this.deleted);
		out.writeObject(this.description);
		out.writeObject(this.etalon_id);
		out.writeObject(this.threshold_set_id);
		out.writeObject(this.monitored_element_id);
		out.writeObject(this.type_id);
		out.writeObject(this.result_ids);
		out.writeObject(this.parameters);
		out.writeObject(this.parameterList);
		out.writeObject(this.arguments);
		out.writeObject(this.argumentList);
	}
}