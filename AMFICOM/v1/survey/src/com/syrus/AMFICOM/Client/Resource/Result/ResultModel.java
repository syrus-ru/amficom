package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.awt.*;
import java.text.*;
import java.util.*;

public class ResultModel extends ObjectResourceModel {

	public static final String	COLUMN_NAME_ID			= "id";
	public static final String	COLUMN_NAME_RESULT_TYPE	= "result_type";
	public static final String	COLUMN_NAME_CREATED		= "created";
	public static final String	COLUMN_NAME_USER_ID		= "user_id";
	public static final String	COLUMN_NAME_ACTION_ID	= "action_id";

	/**
	 * @deprecated use setter/getter pair to access this field
	 */

	public Result				r;

	public ResultModel(Result r) {
		this.r = r;
	}

	public String getColumnValue(String colId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try {
			if (colId.equals(COLUMN_NAME_ID))
				s = r.getId();
			else if (colId.equals(COLUMN_NAME_RESULT_TYPE)) {
				/**
				 * @todo need to i18n !!!
				 * XXX:  need to i18n !!!
				 */
				Hashtable actions = new Hashtable();
				actions.put(Analysis.typ, "Анализ");
				actions.put(Modeling.typ, "Моделирование");
				actions.put(Evaluation.typ, "Оценка");
				actions.put(Test.typ, "Тестирование");
				actions.put(TestRequest.typ, "Запрос на тестирование");
				s = (String) actions.get(r.getResultType());
				if (s == null) s = "";
			} else if (colId.equals(COLUMN_NAME_CREATED))
				s = sdf.format(new Date(r.getModified()));
			else if (colId.equals(COLUMN_NAME_USER_ID))
				s = r.getUserId();
			else if (colId.equals(COLUMN_NAME_ACTION_ID)) {
				s = r.getActionId();
			}
		} catch (Exception e) {
			//			System.out.println("error gettin field value - Result");
			s = "";
		}
		return s;
	}

	public Component getColumnRenderer(String colId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		if (colId.equals(COLUMN_NAME_ID))
			return new TextFieldEditor(r.getId());
		else if (colId.equals(COLUMN_NAME_RESULT_TYPE))
			return new TextFieldEditor(r.getResultType());
		else if (colId.equals(COLUMN_NAME_CREATED))
			return new TextFieldEditor(sdf.format(new Date(r.getModified())));
		else if (colId.equals(COLUMN_NAME_USER_ID))
			return new TextFieldEditor(r.getUserId());
		else if (colId.equals(COLUMN_NAME_ACTION_ID))
				return new TextFieldEditor(r.getActionId());
		return null;
	}

	public Component getColumnEditor(String colId) {
		return getColumnRenderer(colId);
	}

	/**
	 * @return Returns the result.
	 */
	public Result getResult() {
		return r;
	}

	/**
	 * @param result
	 *            The result to set.
	 */
	public void setResult(Result result) {
		this.r = result;
	}
}