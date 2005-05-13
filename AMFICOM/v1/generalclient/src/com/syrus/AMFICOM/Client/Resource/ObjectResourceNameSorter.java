package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.general.StorableObject;

public class ObjectResourceNameSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"name", "string"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}
	
	public String getString(StorableObject or, String column)
	{
		return or.getName();
//		ObjectResourceModel model = or.getModel();
//		return model.getColumnValue("name");
	}

	public long getLong(StorableObject or, String column)
	{
		return 0;
	}

}
