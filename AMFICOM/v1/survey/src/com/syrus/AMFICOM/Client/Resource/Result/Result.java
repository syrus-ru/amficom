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
	private ObjectResourceModel					model;
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

		this.name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
				this.elementary_start_time));
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
		this.transferable = new ClientResult_Transferable();
		//parameters = new Vector();

		this.name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
				this.elementary_start_time));
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new ResultDisplayModel();
	}

	public static ObjectResourceSorter getDefaultSorter() {
		return new ResultTimeSorter();
	}

	public void addParameter(Parameter parameter) {
		this.parameters.add(parameter);
		this.parameterList.add(parameter);
	}

	/**
	 * @return Returns the actionId.
	 */
	public String getActionId() {
		return this.action_id;
	}

	/**
	 * @return Returns the analysisId.
	 */
	public String getAnalysisId() {
		return this.analysis_id;
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return this.deleted;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the evaluationId.
	 */
	public String getEvaluationId() {
		return this.evaluation_id;
	}

	public String getId() {
		return this.id;
	}

	public ObjectResourceModel getModel() {
		if (this.model == null) this.model = new ResultModel(this);
		return this.model;
	}

	/**
	 * @return Returns the modelingId.
	 */
	public String getModelingId() {
		return this.modeling_id;
	}

	public long getModified() {
		return this.elementary_start_time;
	}

	public String getName() {
		return this.name;
		//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		//		return sdf.format(new Date(elementary_start_time));
	}

	/**
	 * @return Returns the parameterList.
	 */
	public List getParameterList() {
		return this.parameterList;
	}

	/**
	 * @return Returns the resultSetId.
	 */
	public String getResultSetId() {
		return this.result_set_id;
	}

	/**
	 * @return Returns the resultType.
	 */
	public String getResultType() {
		return this.result_type;
	}

	/**
	 * @return Returns the testId.
	 */
	public String getTestId() {
		return this.test_id;
	}

	/**
	 * @return Returns the testRequestId.
	 */
	public String getTestRequestId() {
		return this.test_request_id;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return this.user_id;
	}

	/**
	 * @param actionId
	 *            The actionId to set.
	 */
	public void setActionId(String actionId) {
		this.changed = true;
		this.action_id = actionId;
	}

	/**
	 * @param analysisId
	 *            The analysisId to set.
	 */
	public void setAnalysisId(String analysisId) {
		this.changed = true;
		this.analysis_id = analysisId;
	}

	/**
	 * @param deleted
	 *            The deleted to set.
	 */
	public void setDeleted(long deleted) {
		this.changed = true;
		this.deleted = deleted;
	}

	/**
	 * @param evaluationId
	 *            The evaluationId to set.
	 */
	public void setEvaluationId(String evaluationId) {
		this.changed = true;
		this.evaluation_id = evaluationId;
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
		this.action_id = this.transferable.action_id;
		this.id = this.transferable.id;
		this.result_set_id = this.transferable.result_set_id;
		this.result_type = this.transferable.result_type;
		this.user_id = this.transferable.user_id;
		this.elementary_start_time = this.transferable.elementary_start_time;

		this.parameters.clear();
		this.parameterList.clear();
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

		for (int i = 0; i < this.transferable.parameters.length; i++) {
			Parameter param = new Parameter(this.transferable.parameters[i]);
			param.updateLocalFromTransferable();
			this.addParameter(param);
			//			parameters.add(param);
			//			parameterList.add(param);
		}
		this.changed = false;
	}

	/**
	 * @param modelingId
	 *            The modelingId to set.
	 */
	public void setModelingId(String modelingId) {
		this.changed = true;
		this.modeling_id = modelingId;
	}

	public void setModified(long modified) {
		this.changed = true;
		this.elementary_start_time = modified;
	}

	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	/**
	 * @param parameterList
	 *            The parameterList to set.
	 */
	public void setParameterList(List parameterList) {
		this.changed = true;
		this.parameterList = parameterList;
	}

	/**
	 * @param resultSetId
	 *            The resultSetId to set.
	 */
	public void setResultSetId(String resultSetId) {
		this.changed = true;
		this.result_set_id = resultSetId;
	}

	/**
	 * @param resultType
	 *            The resultType to set.
	 */
	public void setResultType(String resultType) {
		this.changed = true;
		this.result_type = resultType;
	}

	/**
	 * @param testId
	 *            The testId to set.
	 */
	public void setTestId(String testId) {
		this.changed = true;
		this.test_id = testId;
	}

	/**
	 * @param testRequestId
	 *            The testRequestId to set.
	 */
	public void setTestRequestId(String testRequestId) {
		this.changed = true;
		this.test_request_id = testRequestId;
	}

	public void setTransferableFromLocal() {
		this.transferable.action_id = this.action_id;
		this.transferable.id = this.id;
		this.transferable.result_set_id = this.result_set_id;
		this.transferable.result_type = this.result_type;
		this.transferable.user_id = this.user_id;
		this.transferable.elementary_start_time = this.elementary_start_time;

		if (this.parameterList.isEmpty()) {
			this.transferable.parameters = new ClientParameter_Transferable[this.parameters
					.size()];
			for (int i = 0; i < this.transferable.parameters.length; i++) {
				Parameter parameter = (Parameter) this.parameters.get(i);
				parameter.setTransferableFromLocal();
				//				transferable.parameters[i] =
				// (ClientParameter_Transferable)((Parameter)parameters.get(i)).getTransferable();
				this.transferable.parameters[i] = (ClientParameter_Transferable) parameter
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.parameters.size(); i++) {
				Object obj = this.parameters.get(i);
				map.put(obj, obj);
			}
			for (Iterator it = this.parameterList.iterator(); it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.parameters = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter parameter = (Parameter) it.next();
				parameter.setTransferableFromLocal();
				this.transferable.parameters[i++] = (ClientParameter_Transferable) parameter
						.getTransferable();
			}
		}
		this.changed = false;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.changed = true;
		this.user_id = userId;
	}

	public void updateLocalFromTransferable() {
		Hashtable ht = new Hashtable();
		try {
			if (this.result_type.equals(Analysis.typ)) {
				this.analysis_id = this.action_id;
				Analysis a = (Analysis) Pool.get(Analysis.typ, this.action_id);
				AnalysisType at = (AnalysisType) Pool.get(AnalysisType.typ, a
						.getTypeId());
				ht = at.getSortedParameters();
			} else if (this.result_type.equals(Modeling.typ)) {
				this.modeling_id = this.action_id;
				Modeling m = (Modeling) Pool.get(Modeling.typ, this.action_id);
				ModelingType mt = (ModelingType) Pool.get(ModelingType.typ, m
						.getTypeId());
				ht = mt.getSortedParameters();
			} else if (this.result_type.equals(Evaluation.typ)) {
				this.evaluation_id = this.action_id;
				Evaluation e = (Evaluation) Pool.get(Evaluation.typ,
						this.action_id);
				EvaluationType et = (EvaluationType) Pool.get(
						EvaluationType.typ, e.getTypeId());
				ht = et.getSortedParameters();
			} else if (this.result_type.equals(Test.typ)) {
				this.test_id = this.action_id;
				Test t = (Test) Pool.get(Test.typ, this.action_id);
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
		if (this.parameters.isEmpty()) {
			this.transferable.parameters = new ClientParameter_Transferable[this.parameterList
					.size()];
			for (Iterator it = this.parameterList.iterator(); it.hasNext();) {
				Parameter param = (Parameter) it.next();
				param.updateLocalFromTransferable();
				param.setApt((ActionParameterType) ht.get(param.getCodename()));
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.parameters.size(); i++) {
				Object obj = this.parameters.get(i);
				map.put(obj, obj);
			}
			for (Iterator it = this.parameterList.iterator(); it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.parameters = new ClientParameter_Transferable[keySet
					.size()];
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter param = (Parameter) it.next();
				param.updateLocalFromTransferable();
				param.setApt((ActionParameterType) ht.get(param.getCodename()));
			}
		}
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.test_request_id = (String) in.readObject();
		this.deleted = in.readLong();
		this.modeling_id = (String) in.readObject();
		this.analysis_id = (String) in.readObject();
		this.result_type = (String) in.readObject();
		this.id = (String) in.readObject();
		this.user_id = (String) in.readObject();
		this.result_set_id = (String) in.readObject();
		this.evaluation_id = (String) in.readObject();
		this.test_id = (String) in.readObject();
		this.action_id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.elementary_start_time = in.readLong();
		this.parameters = (Vector) in.readObject();
		this.parameterList = (List) in.readObject();

		this.transferable = new ClientResult_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.test_request_id);
		out.writeLong(this.deleted);
		out.writeObject(this.modeling_id);
		out.writeObject(this.analysis_id);
		out.writeObject(this.result_type);
		out.writeObject(this.id);
		out.writeObject(this.user_id);
		out.writeObject(this.result_set_id);
		out.writeObject(this.evaluation_id);
		out.writeObject(this.test_id);
		out.writeObject(this.action_id);
		out.writeObject(this.name);
		out.writeLong(this.elementary_start_time);
		out.writeObject(this.parameters);
		out.writeObject(this.parameterList);
		this.changed = false;
	}

}

class ResultTimeSorter extends ObjectResourceSorter {

	String[][]	sorted_columns	= new String[][] { { "time", "long"}};

	public long getLong(ObjectResource or, String column) {
		Result res = (Result) or;
		return res.elementary_start_time;
	}

	public String[][] getSortedColumns() {
		return this.sorted_columns;
	}

	public String getString(ObjectResource or, String column) {
		return "";
	}
}

