/*
 * $Id: Result.java,v 1.1 2004/08/18 13:13:22 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems. 
 * Научно-технический центр. 
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Resource.Result;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.Survey.Result.UI.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class Result implements ObjectResource, Serializable {

	private static final long					serialVersionUID	= 01L;

	public transient static final String		TYPE				= "result";
	/**
	 * @deprecated use TYPE
	 */
	public transient static final String		typ					= TYPE;

	private String								actionId			= "";
	private String								analysis_id			= "";
	private long								deleted				= 0;
	private long								elementaryStartTime	= 0;
	private String								evaluation_id		= "";
	private String								id					= "";
	private ObjectResourceModel					model;
	private String								modeling_id			= "";
	private String								name				= "";
	private List								parameterList		= new ArrayList();
	private String								result_set_id		= "";
	private String								result_type			= "";
	private String								test_id				= "";
	private String								test_request_id		= "";

	private transient ClientResult_Transferable	transferable;
	public String								user_id				= "";

	private boolean								changed				= false;

	public Result(ClientResult_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();

		this.name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(this.elementaryStartTime));
	}

	//	public Result()
	//	{
	//		transferable = new ClientResult_Transferable();
	//		parameters = new Vector();
	//
	//		name = sdf.format(new Date(elementaryStartTime));
	//	}

	public Result(String action_id, String type, String result_set_id, String user_id, String res_id) {
		this.id = res_id;
		this.result_type = type;
		this.user_id = user_id;
		this.result_set_id = result_set_id;
		this.actionId = action_id;
		//
		//		if (type.equals("analysis"))
		//			analysis_id = actionId;
		//		if (type.equals("testrequest"))
		//			test_request_id = actionId;
		//		if (type.equals("modeling"))
		//			modeling_id = actionId;
		//		if (type.equals("evaluation"))
		//			evalustion_id = actionId;
		//		if (type.equals("test"))
		//			test_id = actionId;
		//
		this.transferable = new ClientResult_Transferable();
		//parameters = new Vector();

		this.name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(this.elementaryStartTime));
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new ResultDisplayModel();
	}

	public static ObjectResourceSorter getDefaultSorter() {
		return new ObjectResourceSorter() {

			String[][]	sorted_columns	= new String[][] { { ObjectResourceSorter.COLUMN_TYPE_TIME,
												ObjectResourceSorter.COLUMN_TYPE_LONG}};

			public long getLong(ObjectResource or, String column) {
				Result res = (Result) or;
				return res.getElementaryStartTime();
			}

			public String[][] getSortedColumns() {
				return this.sorted_columns;
			}

			public String getString(ObjectResource or, String column) {
				return "";
			}
		};
	}

	public void addParameter(Parameter parameter) {
		this.parameterList.add(parameter);
	}

	/**
	 * @return Returns the actionId.
	 */
	public String getActionId() {
		return this.actionId;
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
		if (this.model == null)
			this.model = new ResultModel(this);
		return this.model;
	}

	/**
	 * @return Returns the modelingId.
	 */
	public String getModelingId() {
		return this.modeling_id;
	}

	public long getModified() {
		return this.elementaryStartTime;
	}

	public String getName() {
		return this.name;
		//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		//		return sdf.format(new Date(elementaryStartTime));
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
		this.actionId = actionId;
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
		this.actionId = this.transferable.action_id;
		this.id = this.transferable.id;
		this.result_set_id = this.transferable.result_set_id;
		this.result_type = this.transferable.result_type;
		this.user_id = this.transferable.user_id;
		this.elementaryStartTime = this.transferable.elementary_start_time;

		this.parameterList.clear();
		//
		//		Hashtable ht = new Hashtable();
		//		try
		//		{
		//		if (result_type.equals("analysis"))
		//		{
		//			analysis_id = actionId;
		//			Analysis a = (Analysis)Pool.get("analysis", actionId);
		//			AnalysisType at = (AnalysisType )Pool.get(AnalysisType.typ,
		// a.type_id);
		//			ht = at.sorted_parameters;
		//		}
		//		else
		//		if (result_type.equals("modeling"))
		//		{
		//			modeling_id = actionId;
		//			Modeling m = (Modeling )Pool.get("modeling", actionId);
		//			ModelingType mt = (ModelingType )Pool.get(ModelingType.typ,
		// m.type_id);
		//			ht = mt.sorted_parameters;
		//		}
		//		else
		//		if (result_type.equals("evaluation"))
		//		{
		//			evaluation_id = actionId;
		//			Evaluation e = (Evaluation )Pool.get("evaluation", actionId);
		//			EvaluationType et = (EvaluationType )Pool.get(EvaluationType.typ,
		// e.type_id);
		//			ht = et.sorted_parameters;
		//		}
		//		else
		//		if (result_type.equals("test"))
		//		{
		//			test_id = actionId;
		//			Test t = (Test )Pool.get("test", actionId);
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
		this.elementaryStartTime = modified;
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
		this.transferable.action_id = this.actionId;
		this.transferable.id = this.id;
		this.transferable.result_set_id = this.result_set_id;
		this.transferable.result_type = this.result_type;
		this.transferable.user_id = this.user_id;
		this.transferable.elementary_start_time = this.elementaryStartTime;

		{
			int i = 0;
			this.transferable.parameters = new ClientParameter_Transferable[this.parameterList.size()];
			for (Iterator it = this.parameterList.iterator(); it.hasNext(); i++) {
				Parameter parameter = (Parameter) it.next();
				parameter.setTransferableFromLocal();
				this.transferable.parameters[i] = (ClientParameter_Transferable) parameter.getTransferable();
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
			if (this.result_type.equals(Analysis.TYPE)) {
				this.analysis_id = this.actionId;
				Analysis a = (Analysis) Pool.get(Analysis.TYPE, this.actionId);
				AnalysisType at = (AnalysisType) Pool.get(AnalysisType.typ, a.getTypeId());
				ht = at.getSortedParameters();
			} else if (this.result_type.equals(Modeling.TYPE)) {
				this.modeling_id = this.actionId;
				Modeling m = (Modeling) Pool.get(Modeling.TYPE, this.actionId);
				ModelingType mt = (ModelingType) Pool.get(ModelingType.typ, m.getTypeId());
				ht = mt.getSortedParameters();
			} else if (this.result_type.equals(Evaluation.TYPE)) {
				this.evaluation_id = this.actionId;
				Evaluation e = (Evaluation) Pool.get(Evaluation.TYPE, this.actionId);
				EvaluationType et = (EvaluationType) Pool.get(EvaluationType.TYPE, e.getTypeId());
				ht = et.getSortedParameters();
			} else if (this.result_type.equals(Test.TYPE)) {
				this.test_id = this.actionId;
				Test t = (Test) Pool.get(Test.TYPE, this.actionId);
				TestType tt = (TestType) Pool.get(TestType.typ, t.getTestTypeId());
				ht = tt.getSortedParameters();
			}
		} catch (Exception ex) {
			// nothing to do
		}

		this.transferable.parameters = new ClientParameter_Transferable[this.parameterList.size()];
		for (Iterator it = this.parameterList.iterator(); it.hasNext();) {
			Parameter param = (Parameter) it.next();
			param.updateLocalFromTransferable();
			param.setApt((ActionParameterType) ht.get(param.getCodename()));
		}

		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
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
		this.actionId = (String) in.readObject();
		this.name = (String) in.readObject();
		this.elementaryStartTime = in.readLong();
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
		out.writeObject(this.actionId);
		out.writeObject(this.name);
		out.writeLong(this.elementaryStartTime);
		out.writeObject(this.parameterList);
		this.changed = false;
	}

	/**
	 * @return Returns the elementaryStartTime.
	 */
	public long getElementaryStartTime() {
		return this.elementaryStartTime;
	}

	/**
	 * @param elementaryStartTime
	 *            The elementaryStartTime to set.
	 */
	public void setElementaryStartTime(long elementaryStartTime) {
		this.elementaryStartTime = elementaryStartTime;
	}

	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}

	public boolean isChanged() {
		return this.changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}