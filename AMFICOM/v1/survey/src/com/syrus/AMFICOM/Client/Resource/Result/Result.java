package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.Survey.Result.UI.*;

public class Result extends ObjectResource implements Serializable {

	private static final long					serialVersionUID		= 01L;

	public transient static final String		typ						= "result";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								action_id				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								analysis_id				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long									deleted					= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long									elementary_start_time	= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								evaluation_id			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								id						= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								modeling_id				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								name					= "";
	private List								parameterList			= new ArrayList();
	/**
	 * @deprecated use setter/getter pair for parameterList to access this field
	 */
	public Vector								parameters				= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								result_set_id			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								result_type				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								test_id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								test_request_id			= "";

	private transient ClientResult_Transferable	transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String								user_id					= "";

	public Result(ClientResult_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();

		name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
				elementary_start_time));
	}

	//	public Result()
	//	{
	//		transferable = new ClientResult_Transferable();
	//		parameters = new Vector();
	//
	//		name = sdf.format(new Date(elementary_start_time));
	//	}

	public Result(String action_id, String type, String result_set_id,
			String user_id, String res_id) {
		this.id = res_id;
		this.result_type = type;
		this.user_id = user_id;
		this.result_set_id = result_set_id;
		this.action_id = action_id;
		//
		//		if (type.equals("analysis"))
		//			analysis_id = action_id;
		//		if (type.equals("testrequest"))
		//			test_request_id = action_id;
		//		if (type.equals("modeling"))
		//			modeling_id = action_id;
		//		if (type.equals("evaluation"))
		//			evalustion_id = action_id;
		//		if (type.equals("test"))
		//			test_id = action_id;
		//
		transferable = new ClientResult_Transferable();
		//parameters = new Vector();

		name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
				elementary_start_time));
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new ResultDisplayModel();
	}

	public static ObjectResourceSorter getDefaultSorter() {
		return new ResultTimeSorter();
	}

	public void addParameter(Parameter parameter) {
		parameters.add(parameter);
		parameterList.add(parameter);
	}

	/**
	 * @return Returns the actionId.
	 */
	public String getActionId() {
		return action_id;
	}

	/**
	 * @return Returns the analysisId.
	 */
	public String getAnalysisId() {
		return analysis_id;
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return deleted;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the evaluationId.
	 */
	public String getEvaluationId() {
		return evaluation_id;
	}

	public String getId() {
		return id;
	}

	public ObjectResourceModel getModel() {
		return new ResultModel(this);
	}

	/**
	 * @return Returns the modelingId.
	 */
	public String getModelingId() {
		return modeling_id;
	}

	public long getModified() {
		return elementary_start_time;
	}

	public String getName() {
		return name;
		//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		//		return sdf.format(new Date(elementary_start_time));
	}

	/**
	 * @return Returns the parameterList.
	 */
	public List getParameterList() {
		return parameterList;
	}

	/**
	 * @return Returns the resultSetId.
	 */
	public String getResultSetId() {
		return result_set_id;
	}

	/**
	 * @return Returns the resultType.
	 */
	public String getResultType() {
		return result_type;
	}

	/**
	 * @return Returns the testId.
	 */
	public String getTestId() {
		return test_id;
	}

	/**
	 * @return Returns the testRequestId.
	 */
	public String getTestRequestId() {
		return test_request_id;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return user_id;
	}

	/**
	 * @param actionId
	 *            The actionId to set.
	 */
	public void setActionId(String actionId) {
		this.action_id = actionId;
	}

	/**
	 * @param analysisId
	 *            The analysisId to set.
	 */
	public void setAnalysis_id(String analysisId) {
		this.analysis_id = analysisId;
	}

	/**
	 * @param deleted
	 *            The deleted to set.
	 */
	public void setDeleted(long deleted) {
		this.deleted = deleted;
	}

	/**
	 * @param evaluationId
	 *            The evaluationId to set.
	 */
	public void setEvaluationId(String evaluationId) {
		this.evaluation_id = evaluationId;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setLocalFromTransferable() {
		action_id = transferable.action_id;
		id = transferable.id;
		result_set_id = transferable.result_set_id;
		result_type = transferable.result_type;
		user_id = transferable.user_id;
		elementary_start_time = transferable.elementary_start_time;

		parameters.clear();
		parameterList.clear();
		//
		//		Hashtable ht = new Hashtable();
		//		try
		//		{
		//		if (result_type.equals("analysis"))
		//		{
		//			analysis_id = action_id;
		//			Analysis a = (Analysis)Pool.get("analysis", action_id);
		//			AnalysisType at = (AnalysisType )Pool.get(AnalysisType.typ,
		// a.type_id);
		//			ht = at.sorted_parameters;
		//		}
		//		else
		//		if (result_type.equals("modeling"))
		//		{
		//			modeling_id = action_id;
		//			Modeling m = (Modeling )Pool.get("modeling", action_id);
		//			ModelingType mt = (ModelingType )Pool.get(ModelingType.typ,
		// m.type_id);
		//			ht = mt.sorted_parameters;
		//		}
		//		else
		//		if (result_type.equals("evaluation"))
		//		{
		//			evaluation_id = action_id;
		//			Evaluation e = (Evaluation )Pool.get("evaluation", action_id);
		//			EvaluationType et = (EvaluationType )Pool.get(EvaluationType.typ,
		// e.type_id);
		//			ht = et.sorted_parameters;
		//		}
		//		else
		//		if (result_type.equals("test"))
		//		{
		//			test_id = action_id;
		//			Test t = (Test )Pool.get("test", action_id);
		//			TestType tt = (TestType )Pool.get(TestType.typ, t.test_type_id);
		//			ht = tt.sorted_parameters;
		//		}
		//		}
		//		catch(Exception ex)
		//		{
		//		}

		for (int i = 0; i < transferable.parameters.length; i++) {
			Parameter param = new Parameter(transferable.parameters[i]);
			param.updateLocalFromTransferable();
			this.addParameter(param);
			//			parameters.add(param);
			//			parameterList.add(param);
		}
	}

	/**
	 * @param modelingId
	 *            The modelingId to set.
	 */
	public void setModelingId(String modelingId) {
		this.modeling_id = modelingId;
	}

	public void setModified(long modified) {
		this.elementary_start_time = modified;
	}

	public void setName(String n) {
		name = n;
	}

	/**
	 * @param parameterList
	 *            The parameterList to set.
	 */
	public void setParameterList(List parameterList) {
		this.parameterList = parameterList;
	}

	/**
	 * @param resultSetId
	 *            The resultSetId to set.
	 */
	public void setResultSetId(String resultSetId) {
		this.result_set_id = resultSetId;
	}

	/**
	 * @param resultType
	 *            The resultType to set.
	 */
	public void setResultType(String resultType) {
		this.result_type = resultType;
	}

	/**
	 * @param testId
	 *            The testId to set.
	 */
	public void setTestId(String testId) {
		this.test_id = testId;
	}

	/**
	 * @param testRequestId
	 *            The testRequestId to set.
	 */
	public void setTestRequestId(String testRequestId) {
		this.test_request_id = testRequestId;
	}

	public void setTransferableFromLocal() {
		transferable.action_id = action_id;
		transferable.id = id;
		transferable.result_set_id = result_set_id;
		transferable.result_type = result_type;
		transferable.user_id = user_id;
		transferable.elementary_start_time = elementary_start_time;

		if (parameterList.isEmpty()) {
			transferable.parameters = new ClientParameter_Transferable[parameters
					.size()];
			for (int i = 0; i < transferable.parameters.length; i++) {
				Parameter parameter = (Parameter) parameters.get(i);
				parameter.setTransferableFromLocal();
				//				transferable.parameters[i] =
				// (ClientParameter_Transferable)((Parameter)parameters.get(i)).getTransferable();
				transferable.parameters[i] = (ClientParameter_Transferable) parameter
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < parameters.size(); i++) {
				Object obj = parameters.get(i);
				map.put(obj, obj);
			}
			for (Iterator it = this.parameterList.iterator(); it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			transferable.parameters = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter parameter = (Parameter) it.next();
				parameter.setTransferableFromLocal();
				transferable.parameters[i++] = (ClientParameter_Transferable) parameter
						.getTransferable();
			}
		}
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.user_id = userId;
	}

	public void updateLocalFromTransferable() {
		Hashtable ht = new Hashtable();
		try {
			if (result_type.equals(Analysis.typ)) {
				analysis_id = action_id;
				Analysis a = (Analysis) Pool.get(Analysis.typ, action_id);
				AnalysisType at = (AnalysisType) Pool.get(AnalysisType.typ, a
						.getTypeId());
				ht = at.getSortedParameters();
			} else if (result_type.equals(Modeling.typ)) {
				modeling_id = action_id;
				Modeling m = (Modeling) Pool.get(Modeling.typ, action_id);
				ModelingType mt = (ModelingType) Pool.get(ModelingType.typ, m
						.getTypeId());
				ht = mt.getSortedParameters();
			} else if (result_type.equals(Evaluation.typ)) {
				evaluation_id = action_id;
				Evaluation e = (Evaluation) Pool.get(Evaluation.typ, action_id);
				EvaluationType et = (EvaluationType) Pool.get(
						EvaluationType.typ, e.getTypeId());
				ht = et.getSortedParameters();
			} else if (result_type.equals(Test.typ)) {
				test_id = action_id;
				Test t = (Test) Pool.get(Test.typ, action_id);
				TestType tt = (TestType) Pool.get(TestType.typ, t.test_type_id);
				ht = tt.getSortedParameters();
			}
		} catch (Exception ex) {
			// nothing to do
		}

		//		for (int i = 0; i < parameters.size(); i++)
		//		{
		//			Parameter param = (Parameter )parameters.get(i);
		//			param.updateLocalFromTransferable();
		//			param.apt = (ActionParameterType )ht.get(param.codename);
		//		}
		/**
		 * @todo only for backward parameters Vector implementation
		 */
		if (parameters.isEmpty()) {
			transferable.parameters = new ClientParameter_Transferable[parameterList
					.size()];
			for (Iterator it = this.parameterList.iterator(); it.hasNext();) {
				Parameter param = (Parameter) it.next();
				param.updateLocalFromTransferable();
				param.setApt((ActionParameterType) ht.get(param.getCodename()));
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < parameters.size(); i++) {
				Object obj = parameters.get(i);
				map.put(obj, obj);
			}
			for (Iterator it = this.parameterList.iterator(); it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			transferable.parameters = new ClientParameter_Transferable[keySet
					.size()];
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter param = (Parameter) it.next();
				param.updateLocalFromTransferable();
				param.setApt((ActionParameterType) ht.get(param.getCodename()));
			}
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		test_request_id = (String) in.readObject();
		deleted = in.readLong();
		modeling_id = (String) in.readObject();
		analysis_id = (String) in.readObject();
		result_type = (String) in.readObject();
		id = (String) in.readObject();
		user_id = (String) in.readObject();
		result_set_id = (String) in.readObject();
		evaluation_id = (String) in.readObject();
		test_id = (String) in.readObject();
		action_id = (String) in.readObject();
		name = (String) in.readObject();
		elementary_start_time = in.readLong();
		parameters = (Vector) in.readObject();
		parameterList = (List) in.readObject();

		transferable = new ClientResult_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(test_request_id);
		out.writeLong(deleted);
		out.writeObject(modeling_id);
		out.writeObject(analysis_id);
		out.writeObject(result_type);
		out.writeObject(id);
		out.writeObject(user_id);
		out.writeObject(result_set_id);
		out.writeObject(evaluation_id);
		out.writeObject(test_id);
		out.writeObject(action_id);
		out.writeObject(name);
		out.writeLong(elementary_start_time);
		out.writeObject(parameters);
		out.writeObject(parameterList);
	}

}

class ResultTimeSorter extends ObjectResourceSorter {

	String[][]	sorted_columns	= new String[][] { { "time", "long"}};

	public long getLong(ObjectResource or, String column) {
		Result res = (Result) or;
		return res.elementary_start_time;
	}

	public String[][] getSortedColumns() {
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column) {
		return "";
	}
}