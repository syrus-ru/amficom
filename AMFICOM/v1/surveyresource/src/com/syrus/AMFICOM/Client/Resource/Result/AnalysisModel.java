package com.syrus.AMFICOM.Client.Resource.Result;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class AnalysisModel extends ObjectResourceModel {

	private Analysis	analysis;

	public AnalysisModel(Analysis analysis) {
		this.analysis = analysis;
	}

	public String getColumnValue(String colId) {

		String s = null;
		try {
			if (colId.equals(ConstStorage.COLUMN_NAME_ID))
				s = this.analysis.getId();
			else if (colId.equals(ConstStorage.COLUMN_NAME_NAME))
				s = this.analysis.getName();
			else if (colId.equals(ConstStorage.COLUMN_NAME_MODIFIED))
				s = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
						this.analysis.getModified()));
			else if (colId.equals(ConstStorage.COLUMN_NAME_USER_ID))
				s = this.analysis.getUserId();
			else if (colId.equals(ConstStorage.COLUMN_NAME_DESCRIPTION))
					s = this.analysis.getDescription();
		} catch (Exception e) {
			//      System.out.println("error gettin field value - Analysis");
			// s = "";
		}
		return s;
	}
}

