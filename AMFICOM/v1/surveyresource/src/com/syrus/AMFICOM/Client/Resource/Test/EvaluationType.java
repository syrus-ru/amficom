package com.syrus.AMFICOM.Client.Resource.Test;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.CORBA.Survey.*;

public class EvaluationType extends StubResource implements Serializable {

	private static final long			serialVersionUID	= 01L;
	public static final String			TYPE					= "evaluationtype";
	/**
	 * @deprecated use TYPE
	 */
	public static final String			typ					= TYPE;
	private List						argumentList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector						arguments			= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						description			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						etalon_type_id		= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						etalon_type_name	= "";

	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						id					= "";
	long								modified			= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						name				= "";
	private List						parameterList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for parameterList to access this field
	 */
	public Vector						parameters			= new Vector();
	/**
	 * @deprecated use getter to access this field
	 */
	public Hashtable					sorted_arguments	= new Hashtable();
	/**
	 * @deprecated use getter to access this field
	 */
	public Hashtable					sorted_parameters	= new Hashtable();
	/**
	 * @deprecated use getter to access this field
	 */
	public Hashtable					sorted_thresholds	= new Hashtable();
	private List						thresholdList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for thresholdList to access this field
	 */
	public Vector						thresholds			= new Vector();
	private EvaluationType_Transferable	transferable;

	public EvaluationType(EvaluationType_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return this.argumentList;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the ethalonTypeId.
	 */
	public String getEthalonTypeId() {
		return etalon_type_id;
	}

	/**
	 * @return Returns the ethalonTypeName.
	 */
	public String getEthalonTypeName() {
		return etalon_type_name;
	}

	public String getId() {
		return id;
	}

	public long getModified() {
		return modified;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Returns the parameterList.
	 */
	public List getParameterList() {
		return parameterList;
	}

	/**
	 * @return Returns the sortedArguments.
	 */
	public Hashtable getSortedArguments() {
		return sorted_arguments;
	}

	/**
	 * @return Returns the sorted_parameters.
	 */
	public Hashtable getSortedParameters() {
		return sorted_parameters;
	}

	/**
	 * @return Returns the sorted_thresholds.
	 */
	public Hashtable getSortedThresholds() {
		return sorted_thresholds;
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
	 * @param argumentList
	 *            The argumentList to set.
	 */
	public void setArgumentList(List argumentList) {
		this.argumentList = argumentList;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param ethalonTypeId
	 *            The ethalonTypeId to set.
	 */
	public void setEthalonTypeId(String ethalonTypeId) {
		this.etalon_type_id = ethalonTypeId;
	}

	/**
	 * @param ethalonTypeName
	 *            The ethalonTypeName to set.
	 */
	public void setEthalonTypeName(String ethalonTypeName) {
		this.etalon_type_name = ethalonTypeName;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setLocalFromTransferable() {
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		etalon_type_id = transferable.etalon_type_id;
		etalon_type_name = transferable.etalon_type_name;
		modified = transferable.modified;

		parameters.clear();
		parameterList.clear();
		arguments.clear();
		argumentList.clear();
		thresholds.clear();
		thresholdList.clear();

		for (int i = 0; i < transferable.parameters.length; i++) {
			ActionParameterType parameter = new ActionParameterType(
					transferable.parameters[i]);
			parameters.add(parameter);
			parameterList.add(parameter);
			sorted_parameters.put(parameter.getCodename(), parameter);
		}
		for (int i = 0; i < transferable.arguments.length; i++) {
			ActionParameterType argument = new ActionParameterType(
					transferable.arguments[i]);
			arguments.add(argument);
			argumentList.add(argument);
			sorted_arguments.put(argument.getCodename(), argument);
		}
		for (int i = 0; i < transferable.thresholds.length; i++) {
			ActionParameterType threshold = new ActionParameterType(
					transferable.thresholds[i]);
			thresholds.add(threshold);
			thresholdList.add(threshold);
			sorted_thresholds.put(threshold.getCodename(), threshold);
		}
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
	 * @param thresholdList
	 *            The thresholdList to set.
	 */
	public void setThresholdList(List thresholdList) {
		this.thresholdList = thresholdList;
	}

	public void setTransferableFromLocal() {
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.etalon_type_id = etalon_type_id;
		transferable.etalon_type_name = etalon_type_name;
		transferable.modified = this.modified;

		//		transferable.parameters = new
		// ActionParameterType_Transferable[parameters
		//				.size()];
		//		for (int i = 0; i < transferable.parameters.length; i++) {
		//			ActionParameterType parameter = (ActionParameterType) parameters
		//					.get(i);
		//			parameter.setTransferableFromLocal();
		//			transferable.parameters[i] = (ActionParameterType_Transferable)
		// parameter
		//					.getTransferable();
		//		}
		if (parameterList.size() == 0) {
			transferable.parameters = new ActionParameterType_Transferable[parameters
					.size()];
			for (int i = 0; i < transferable.parameters.length; i++) {
				ActionParameterType parameter = (ActionParameterType) parameters
						.get(i);
				parameter.setTransferableFromLocal();
				transferable.parameters[i] = (ActionParameterType_Transferable) parameter
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < parameters.size(); i++) {
				Object obj = parameters.get(i);
				map.put(obj, obj);
			}
			for (int i = 0; i < parameterList.size(); i++) {
				Object obj = parameterList.get(i);
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			transferable.parameters = new ActionParameterType_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				ActionParameterType parameter = (ActionParameterType) it.next();
				parameter.setTransferableFromLocal();
				transferable.parameters[i++] = (ActionParameterType_Transferable) parameter
						.getTransferable();
			}
		}

		//		transferable.arguments = new
		// ActionParameterType_Transferable[arguments
		//				.size()];
		//		for (int i = 0; i < transferable.arguments.length; i++) {
		//			ActionParameterType argument = (ActionParameterType) arguments
		//					.get(i);
		//			argument.setTransferableFromLocal();
		//			transferable.arguments[i] = (ActionParameterType_Transferable)
		// argument
		//					.getTransferable();
		//		}
		if (argumentList.size() == 0) {
			transferable.arguments = new ActionParameterType_Transferable[arguments
					.size()];
			for (int i = 0; i < transferable.arguments.length; i++) {
				ActionParameterType argument = (ActionParameterType) arguments
						.get(i);
				argument.setTransferableFromLocal();
				transferable.arguments[i] = (ActionParameterType_Transferable) argument
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < arguments.size(); i++) {
				Object obj = arguments.get(i);
				map.put(obj, obj);
			}
			for (int i = 0; i < argumentList.size(); i++) {
				Object obj = argumentList.get(i);
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			transferable.arguments = new ActionParameterType_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				ActionParameterType argument = (ActionParameterType) it.next();
				argument.setTransferableFromLocal();
				transferable.arguments[i++] = (ActionParameterType_Transferable) argument
						.getTransferable();
			}
		}
		//		transferable.thresholds = new
		// ActionParameterType_Transferable[thresholds
		//				.size()];
		//
		//		for (int i = 0; i < transferable.thresholds.length; i++) {
		//			ActionParameterType parameter = (ActionParameterType) thresholds
		//					.get(i);
		//			parameter.setTransferableFromLocal();
		//			transferable.thresholds[i] = (ActionParameterType_Transferable)
		// parameter
		//					.getTransferable();
		//		}
		if (thresholdList.size() == 0) {
			transferable.thresholds = new ActionParameterType_Transferable[thresholds
					.size()];
			for (int i = 0; i < transferable.thresholds.length; i++) {
				ActionParameterType threshold = (ActionParameterType) thresholds
						.get(i);
				threshold.setTransferableFromLocal();
				transferable.thresholds[i] = (ActionParameterType_Transferable) threshold
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < thresholds.size(); i++) {
				Object obj = thresholds.get(i);
				map.put(obj, obj);
			}
			for (int i = 0; i < thresholdList.size(); i++) {
				Object obj = thresholdList.get(i);
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			transferable.thresholds = new ActionParameterType_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				ActionParameterType threshold = (ActionParameterType) it.next();
				threshold.setTransferableFromLocal();
				transferable.thresholds[i++] = (ActionParameterType_Transferable) threshold
						.getTransferable();
			}
		}
	}

	public void updateLocalFromTransferable() {
		sorted_parameters = new Hashtable();
		sorted_arguments = new Hashtable();
		sorted_thresholds = new Hashtable();

		for (int i = 0; i < parameters.size(); i++) {
			ActionParameterType parameter = (ActionParameterType) parameters
					.get(i);
			sorted_parameters.put(parameter.getCodename(), parameter);
		}
		for (int i = 0; i < parameterList.size(); i++) {
			ActionParameterType parameter = (ActionParameterType) parameterList
					.get(i);
			sorted_parameters.put(parameter.getCodename(), parameter);
		}
		for (int i = 0; i < arguments.size(); i++) {
			ActionParameterType argument = (ActionParameterType) arguments
					.get(i);
			sorted_arguments.put(argument.getCodename(), argument);
		}
		for (int i = 0; i < argumentList.size(); i++) {
			ActionParameterType argument = (ActionParameterType) argumentList
					.get(i);
			sorted_arguments.put(argument.getCodename(), argument);
		}
		for (int i = 0; i < thresholds.size(); i++) {
			ActionParameterType threshold = (ActionParameterType) thresholds
					.get(i);
			sorted_thresholds.put(threshold.getCodename(), threshold);
		}
		for (int i = 0; i < thresholdList.size(); i++) {
			ActionParameterType threshold = (ActionParameterType) thresholdList
					.get(i);
			sorted_thresholds.put(threshold.getCodename(), threshold);
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.description = (String) in.readObject();
		this.etalon_type_id = (String) in.readObject();
		this.etalon_type_name = (String) in.readObject();
		this.parameters = (Vector) in.readObject();
		this.parameterList = (List) in.readObject();
		this.arguments = (Vector) in.readObject();
		this.argumentList = (List) in.readObject();
		this.thresholds = (Vector) in.readObject();
		this.thresholdList = (List) in.readObject();
		this.modified = in.readLong();

		this.transferable = new EvaluationType_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeObject(this.description);
		out.writeObject(this.etalon_type_id);
		out.writeObject(this.etalon_type_name);
		out.writeObject(this.parameters);
		out.writeObject(this.parameterList);
		out.writeObject(this.arguments);
		out.writeObject(this.argumentList);
		out.writeObject(this.thresholds);
		out.writeObject(this.thresholdList);
		out.writeLong(this.modified);
	}
}

