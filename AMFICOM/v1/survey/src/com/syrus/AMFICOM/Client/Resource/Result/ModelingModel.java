package com.syrus.AMFICOM.Client.Resource.Result;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class ModelingModel extends ObjectResourceModel {

	private Modeling	m;

	public ModelingModel(Modeling modeling) {
		this.m = modeling;
	}

	public String getColumnValue(String col_id) {
		String s = null;
		try {
			if (col_id.equals(ConstStorage.COLUMN_NAME_ID)) s = this.m.getId();
			if (col_id.equals(ConstStorage.COLUMN_NAME_NAME))
					s = this.m.getName();
			if (col_id.equals(ConstStorage.COLUMN_NAME_MODIFIED))
					s = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(this.m
							.getModified()));
			if (col_id.equals(ConstStorage.COLUMN_NAME_USER_ID))
					s = this.m.getUserId();
		} catch (Exception e) {
			System.out.println("error gettin field value - Modeling");
			//s = "";
		}
		return s;
	}
}