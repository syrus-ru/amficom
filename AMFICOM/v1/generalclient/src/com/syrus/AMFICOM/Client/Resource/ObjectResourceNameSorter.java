package com.syrus.AMFICOM.Client.Resource;

public class ObjectResourceNameSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"name", "string"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}
	
	public String getString(ObjectResource or, String column)
	{
		return or.getName();
//		ObjectResourceModel model = or.getModel();
//		return model.getColumnValue("name");
	}

	public long getLong(ObjectResource or, String column)
	{
		return 0;
	}

}
