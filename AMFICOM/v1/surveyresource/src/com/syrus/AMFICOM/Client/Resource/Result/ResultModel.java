package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.*;

import java.awt.*;
import java.util.*;

public class ResultModel extends ObjectResourceModel {

	protected Result	result;

	public ResultModel(Result result) {
		this.result = result;
	}

	public Component getColumnEditor(String colId) {
		return getColumnRenderer(colId);
	}

	public Component getColumnRenderer(String colId) {
		if (colId.equals(ConstStorage.COLUMN_NAME_ID))
			return new TextFieldEditor(this.result.getId());
		else if (colId.equals(ConstStorage.COLUMN_NAME_RESULT_TYPE))
			return new TextFieldEditor(this.result.getResultType());
		else if (colId.equals(ConstStorage.COLUMN_NAME_CREATED))
			return new TextFieldEditor(ConstStorage.SIMPLE_DATE_FORMAT
					.format(new Date(this.result.getModified())));
		else if (colId.equals(ConstStorage.COLUMN_NAME_USER_ID))
			return new TextFieldEditor(this.result.getUserId());
		else if (colId.equals(ConstStorage.COLUMN_NAME_ACTION_ID))
				return new TextFieldEditor(this.result.getActionId());
		return null;
	}

	public String getColumnValue(String colId) {
		String s = null;
		try {
			if (colId.equals(ConstStorage.COLUMN_NAME_ID))
				s = this.result.getId();
			else if (colId.equals(ConstStorage.COLUMN_NAME_RESULT_TYPE)) {

				Hashtable actions = new Hashtable();
				actions.put(Analysis.typ, LangModelSurvey.getString("Analysis")); //$NON-NLS-1$
				actions.put(Modeling.typ, LangModelSurvey.getString("Modeling")); //$NON-NLS-1$
				actions.put(Evaluation.typ, LangModelSurvey.getString("Evaluation")); //$NON-NLS-1$
				actions.put(Test.typ, LangModelSurvey.getString("Testing")); //$NON-NLS-1$
				actions.put(TestRequest.typ, LangModelSurvey.getString("TestRequest")); //$NON-NLS-1$
				s = (String) actions.get(this.result.getResultType());
				if (s == null) s = ""; //$NON-NLS-1$
			} else if (colId.equals(ConstStorage.COLUMN_NAME_CREATED))
				s = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(this.result
						.getModified()));
			else if (colId.equals(ConstStorage.COLUMN_NAME_USER_ID))
				s = this.result.getUserId();
			else if (colId.equals(ConstStorage.COLUMN_NAME_ACTION_ID)) {
				s = this.result.getActionId();
			}
		} catch (Exception e) {
			//			System.out.println("error gettin field value - Result");
			//s = "";
		}
		return s;
	}

}

