package com.syrus.AMFICOM.Client.Resource.Test;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.CORBA.Survey.*;

public class TestType extends StubResource implements Serializable {

	private static final long		serialVersionUID	= 01L;
	public static final String		typ					= "testtype";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					analysis_type_ids[];
	private List					argumentList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector					arguments			= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					description			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					evaluation_type_ids[];
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					id					= "";
	long							modified			= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					name				= "";
	private List					parameterList		= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for parameterList to access this field
	 */
	public Vector					parameters			= new Vector();
	/**
	 * @deprecated use getter to access this field
	 */
	public Hashtable				sorted_arguments	= new Hashtable();
	/**
	 * @deprecated use getter to access this field
	 */
	public Hashtable				sorted_parameters	= new Hashtable();
	private TestType_Transferable	transferable;

	public TestType(TestType_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @return Returns the analysisTypeIds.
	 */
	public String[] getAnalysisTypeIds() {
		return analysis_type_ids;
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return argumentList;
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
	 * @return Returns the evaluationTypeIds.
	 */
	public String[] getEvaluationTypeIds() {
		return evaluation_type_ids;
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
	 * @return Returns the sorted_arguments.
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

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @param analysisTypeIds
	 *            The analysisTypeIds to set.
	 */
	public void setAnalysisTypeIds(String[] analysisTypeIds) {
		this.analysis_type_ids = analysisTypeIds;
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
	 * @param evaluationTypeIds
	 *            The evaluationTypeIds to set.
	 */
	public void setEvaluationTypeIds(String[] evaluationTypeIds) {
		this.evaluation_type_ids = evaluationTypeIds;
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

		sorted_arguments.clear();
		sorted_parameters.clear();
		parameters.clear();
		parameterList.clear();
		arguments.clear();
		argumentList.clear();

		analysis_type_ids = new String[transferable.analysis_type_ids.length];
		for (int i = 0; i < transferable.analysis_type_ids.length; i++)
			analysis_type_ids[i] = transferable.analysis_type_ids[i];

		evaluation_type_ids = new String[transferable.evaluation_type_ids.length];
		for (int i = 0; i < transferable.evaluation_type_ids.length; i++)
			evaluation_type_ids[i] = transferable.evaluation_type_ids[i];

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

		transferable.analysis_type_ids = new String[analysis_type_ids.length];
		for (int i = 0; i < analysis_type_ids.length; i++)
			transferable.analysis_type_ids[i] = analysis_type_ids[i];

		transferable.evaluation_type_ids = new String[evaluation_type_ids.length];
		for (int i = 0; i < evaluation_type_ids.length; i++)
			transferable.evaluation_type_ids[i] = evaluation_type_ids[i];
		//		transferable.parameters = new
		// ActionParameterType_Transferable[parameters
		//																		.size()];
		//																
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
		//																		.size()];
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
	}

	public void updateLocalFromTransferable() {
		sorted_parameters.clear();
		sorted_arguments.clear();

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
		Object o = in.readObject();
		analysis_type_ids = (String[]) o;
		Object o2 = in.readObject();
		evaluation_type_ids = (String[]) o2;

		modified = in.readLong();

		transferable = new TestType_Transferable();
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

		Object o = analysis_type_ids;
		out.writeObject(o);
		Object o2 = evaluation_type_ids;
		out.writeObject(o2);

		out.writeLong(modified);
	}
}

