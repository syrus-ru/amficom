package com.syrus.AMFICOM.Client.Resource.Test;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.CORBA.Survey.*;

public class AnalysisType extends ObjectResource implements Serializable {

	private static final long			serialVersionUID	= 01L;
	public static final String			typ					= "analysistype";
	private List						argumentList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector						arguments			= new Vector();
	private List						criteriaList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for criteriaList to access this field
	 */
	public Vector						criterias			= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						description			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
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
	 * @deprecated use setter/getter pair to access this field
	 */
	public Hashtable					sorted_arguments	= new Hashtable();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public Hashtable					sorted_criterias	= new Hashtable();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public Hashtable					sorted_parameters	= new Hashtable();

	private AnalysisType_Transferable	transferable;

	public AnalysisType(AnalysisType_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return argumentList;
	}

	/**
	 * @return Returns the criteriaList.
	 */
	public List getCriteriaList() {
		return criteriaList;
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
	 * @return Returns the sortedCriterias.
	 */
	public Hashtable getSortedCriterias() {
		return sorted_criterias;
	}

	/**
	 * @return Returns the sortedParameters.
	 */
	public Hashtable getSortedParameters() {
		return sorted_parameters;
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
	 * @param criteriaList
	 *            The criteriaList to set.
	 */
	public void setCriteriaList(List criteriaList) {
		this.criteriaList = criteriaList;
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
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		modified = transferable.modified;

		parameters.clear();
		parameterList.clear();
		arguments.clear();
		argumentList.clear();
		criterias.clear();
		criteriaList.clear();
		sorted_parameters.clear();
		sorted_arguments.clear();
		sorted_criterias.clear();


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
		for (int i = 0; i < transferable.criterias.length; i++) {
			ActionParameterType criteria = new ActionParameterType(
					transferable.criterias[i]);
			criterias.add(criteria);
			criteriaList.add(criteria);
			sorted_criterias.put(criteria.getCodename(), criteria);
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

	public void setTransferableFromLocal() {
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
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
				transferable.parameters[i] = (ActionParameterType_Transferable) argument
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

		//		transferable.criterias = new
		// ActionParameterType_Transferable[criterias
		//				.size()];
		//		for (int i = 0; i < transferable.criterias.length; i++) {
		//			ActionParameterType parameter = (ActionParameterType) criterias
		//					.get(i);
		//			parameter.setTransferableFromLocal();
		//			transferable.criterias[i] = (ActionParameterType_Transferable)
		// parameter
		//					.getTransferable();
		//		}

		if (criteriaList.size() == 0) {
			transferable.criterias = new ActionParameterType_Transferable[criterias
					.size()];
			for (int i = 0; i < transferable.criterias.length; i++) {
				ActionParameterType parameter = (ActionParameterType) criterias
						.get(i);
				parameter.setTransferableFromLocal();
				transferable.criterias[i] = (ActionParameterType_Transferable) parameter
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < criterias.size(); i++) {
				Object obj = criterias.get(i);
				map.put(obj, obj);
			}
			for (int i = 0; i < criteriaList.size(); i++) {
				Object obj = criteriaList.get(i);
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			transferable.criterias = new ActionParameterType_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				ActionParameterType parameter = (ActionParameterType) it.next();
				parameter.setTransferableFromLocal();
				transferable.criterias[i++] = (ActionParameterType_Transferable) parameter
						.getTransferable();
			}
		}

	}

	public void updateLocalFromTransferable() {
		sorted_parameters.clear();
		sorted_arguments.clear();
		sorted_criterias.clear();

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
		for (int i = 0; i < criterias.size(); i++) {
			ActionParameterType criteria = (ActionParameterType) criterias
					.get(i);
			sorted_criterias.put(criteria.getCodename(), criteria);
		}
		for (int i = 0; i < criteriaList.size(); i++) {
			ActionParameterType criteria = (ActionParameterType) criteriaList
					.get(i);
			sorted_criterias.put(criteria.getCodename(), criteria);
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		name = (String) in.readObject();
		description = (String) in.readObject();
		parameters = (Vector) in.readObject();
		parameterList = (List) in.readObject();
		arguments = (Vector) in.readObject();
		argumentList = (List) in.readObject();
		criterias = (Vector) in.readObject();
		criteriaList = (List) in.readObject();
		modified = in.readLong();

		transferable = new AnalysisType_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(parameters);
		out.writeObject(parameterList);
		out.writeObject(arguments);
		out.writeObject(argumentList);
		out.writeObject(criterias);
		out.writeObject(criteriaList);
		out.writeLong(modified);
	}
}

