package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ClientAnalysis_Transferable;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.Survey.Result.UI.AnalysisDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;

import java.io.IOException;
import java.io.Serializable;

import java.util.*;

public class Analysis extends ObjectResource implements Serializable {

	private static final long						serialVersionUID		= 01L;
	public transient static final String			typ						= "analysis";
	private List									argumentList			= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector									arguments				= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									criteria_set_id			= "";
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
	public String									result_id				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String[]									result_ids				= new String[0];
	private transient ClientAnalysis_Transferable	transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									type_id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String									user_id					= "";

	public Analysis(ClientAnalysis_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Analysis(String id) {
		this.parameters = new Vector();
		this.arguments = new Vector();
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
	 * @deprecated use getter for argumentList/parameterList
	 */
	public Class getChildClass(String key) {
		if (key.equals("arguments")) {
			return Parameter.class;
		} else if (key.equals("parameters")) { return Parameter.class; }
		return ObjectResource.class;
	}

	/**
	 * @deprecated use getter for argumentList/parameterList
	 */
	public Enumeration getChildren(String key) {
		if (key.equals("arguments")) {
			return this.arguments.elements();
		} else if (key.equals("parameters")) { return this.parameters
				.elements(); }
		return new Vector().elements();
	}

	/**
	 * @deprecated use getter for argumentList/parameterList
	 */
	public Enumeration getChildTypes() {
		Vector vec = new Vector();
		vec.add("arguments");
		vec.add("parameters");
		return vec.elements();
	}

	/**
	 * @return Returns the criteriaSetId.
	 */
	public String getCriteriaSetId() {
		return this.criteria_set_id;
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
	 * @return Returns the resultId.
	 */
	public String getResultId() {
		return this.result_id;
	}

	/**
	 * @return Returns the resultIds.
	 */
	public String[] getResultIds() {
		return this.result_ids;
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
	 * @param criteriaSetId
	 *            The criteriaSetId to set.
	 */
	public void setCriteriaSetId(String criteriaSetId) {
		this.criteria_set_id = criteriaSetId;
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
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setLocalFromTransferable() {
		this.modified = this.transferable.modified;
		this.deleted = this.transferable.deleted;
		this.description = this.transferable.description;
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.user_id = this.transferable.user_id;
		this.monitored_element_id = this.transferable.monitored_element_id;
		this.type_id = this.transferable.type_id;
		this.criteria_set_id = this.transferable.criteria_set_id;

		this.result_ids = this.transferable.result_ids;

		this.arguments.clear();
		this.argumentList.clear();
		AnalysisType at = (AnalysisType) Pool.get(AnalysisType.typ,
				this.type_id);

		for (int i = 0; i < this.transferable.arguments.length; i++) {
			Parameter param = new Parameter(this.transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.setApt((ActionParameterType) at.getSortedArguments().get(
					param.getCodename()));
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
	 * @param resultId
	 *            The resultId to set.
	 */
	public void setResultId(String resultId) {
		this.result_id = resultId;
	}

	/**
	 * @param resultIds
	 *            The resultIds to set.
	 */
	public void setResultIds(String[] resultIds) {
		this.result_ids = resultIds;
	}

	public void setTransferableFromLocal() {
		this.transferable.modified = this.modified;
		this.transferable.deleted = this.deleted;
		this.transferable.description = this.description;
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.user_id = this.user_id;
		this.transferable.monitored_element_id = this.monitored_element_id;
		this.transferable.type_id = this.type_id;
		this.transferable.result_ids = new String[0];
		this.transferable.criteria_set_id = this.criteria_set_id;

		//		transferable.arguments = new
		// ClientParameter_Transferable[arguments.size()];
		//
		//		for (int i=0; i<transferable.arguments.length; i++)
		//		{
		//			Parameter argument = (Parameter)arguments.get(i);
		//			argument.setTransferableFromLocal();
		//			transferable.arguments[i] =
		// (ClientParameter_Transferable)argument.getTransferable();
		//		}
		if (this.argumentList.isEmpty()) {
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
			for (Iterator it = this.argumentList.iterator(); it.hasNext();) {
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
		//nothing to do
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.modified = in.readLong();
		this.user_id = (String) in.readObject();
		this.deleted = in.readLong();
		this.description = (String) in.readObject();
		this.result_id = (String) in.readObject();
		this.criteria_set_id = (String) in.readObject();
		this.monitored_element_id = (String) in.readObject();
		this.type_id = (String) in.readObject();
		this.result_ids = (String[]) in.readObject();
		this.parameters = (Vector) in.readObject();
		this.arguments = (Vector) in.readObject();

		this.transferable = new ClientAnalysis_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.modified);
		out.writeObject(this.user_id);
		out.writeLong(this.deleted);
		out.writeObject(this.description);
		out.writeObject(this.result_id);
		out.writeObject(this.criteria_set_id);
		out.writeObject(this.monitored_element_id);
		out.writeObject(this.type_id);
		out.writeObject(this.result_ids);
		out.writeObject(this.parameters);
		out.writeObject(this.arguments);
	}
}