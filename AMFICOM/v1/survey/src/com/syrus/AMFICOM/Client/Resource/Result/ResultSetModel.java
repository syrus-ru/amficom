package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

import java.awt.*;
import java.util.*;

public class ResultSetModel extends ObjectResourceModel {

	private ResultSet	resultSet;

	public ResultSetModel(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String getColumnValue(String colId) {
		String s = null;
		try {
			if (colId.equals(ConstStorage.COLUMN_NAME_DOMAIN_ID))
					s = Pool.getName(Domain.typ, this.resultSet.getDomainId());
			if (colId.equals(ConstStorage.COLUMN_NAME_START_TIME))
					s = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
							this.resultSet.getStartTime()));
			if (colId.equals(ConstStorage.COLUMN_NAME_END_TIME))
					s = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
							this.resultSet.getEndTime()));
			if (colId.equals(ConstStorage.COLUMN_NAME_ACTIVE)) {
				if (this.resultSet.isActive())
					s = LangModelSurvey.getString("Yes"); //$NON-NLS-1$
				else
					s = ""; //$NON-NLS-1$
			}
		} catch (Exception e) {
			//			System.out.println("error gettin field value - Result");
			s = ""; //$NON-NLS-1$
		}
		return s;
	}

	public Component getColumnRenderer(String colId) {
		if (colId.equals(ConstStorage.COLUMN_NAME_DOMAIN_ID))
				return new ObjectResourceComboBox(Pool.getHash(Domain.typ),
						this.resultSet.getDomainId());
		if (colId.equals(ConstStorage.COLUMN_NAME_START_TIME))
				return new TextFieldEditor(ConstStorage.SIMPLE_DATE_FORMAT
						.format(new Date(this.resultSet.getStartTime())));
		if (colId.equals(ConstStorage.COLUMN_NAME_END_TIME))
				return new TextFieldEditor(ConstStorage.SIMPLE_DATE_FORMAT
						.format(new Date(this.resultSet.getEndTime())));
		if (colId.equals(ConstStorage.COLUMN_NAME_ACTIVE))
				return new TextFieldEditor(getColumnValue(colId));
		return null;
	}

	public Component getColumnEditor(String colId) {
		return getColumnRenderer(colId);
	}
}

