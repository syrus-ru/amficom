package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

public abstract class ObjectResourceSorter
{
	static public final int SORT_ASCENDING = 0;
	static public final int SORT_DESCENDING = 1;

	DataSet dataSet;

	public ObjectResourceSorter()
	{
	}

	public ObjectResourceSorter(DataSet dataSet)
	{
		this.dataSet = dataSet;
	}

	public void setDataSet(DataSet dataSet)
	{
		this.dataSet = dataSet;
	}

	public abstract String[][] getSortedColumns();
	public abstract String getString(ObjectResource or, String column);
	public abstract long getLong(ObjectResource or, String column);

	public DataSet default_sort()
	{
		return sort(getSortedColumns()[0][0], SORT_ASCENDING);
	}
	
	public DataSet sort(String column, int sortdir)
	{
		DataSet sorted_dataSet = new DataSet();
		int size = dataSet.size();
		String sort_type = "";
		ObjectResource temp_or = null;
		long long_obj_sorter = 0;
		String string_obj_sorter = "";

		long curr_long = -9223372036854775808L;
		String curr_string = "";

		String[][] sorted_columns = getSortedColumns();

		for (int i = 0; i < sorted_columns.length; i++)
		{
			if (column.equals(sorted_columns[i][0])) {
				sort_type = sorted_columns[i][1];
				break;
			}
			if (i == sorted_columns.length - 1)
			{
				sorted_dataSet = dataSet;
			}
		}

		for (int i = 0; i < size; i++)
		{
			curr_long = -9223372036854775808L;
			curr_string = "";
			for(Enumeration e = dataSet.elements(); e.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource )e.nextElement();
				if (sort_type.equals("long"))
				{
					long_obj_sorter = getLong(or, column);
					if (sortdir == SORT_ASCENDING)
					{
						if (long_obj_sorter < curr_long || curr_long == -9223372036854775808L)
						{
							curr_long = long_obj_sorter;
							temp_or = or;
						}
					}
					if (sortdir == SORT_DESCENDING)
					{
						if (long_obj_sorter > curr_long)
						{
							curr_long = long_obj_sorter;
							temp_or = or;
						}
					}
				}
				else
				if (sort_type.equals("string"))
				{
					string_obj_sorter = getString(or, column);
					if (sortdir == SORT_ASCENDING)
					{
						if (string_obj_sorter.compareTo(curr_string) < 0 || curr_string.equals(""))
						{
							curr_string = string_obj_sorter;
							temp_or = or;
						}
					}
					if (sortdir == SORT_DESCENDING)
					{
						if (string_obj_sorter.compareTo(curr_string) > 0 || curr_string.equals(""))
						{
							curr_string = string_obj_sorter;
							temp_or = or;
						}
					}
				}

			}
			sorted_dataSet.add(temp_or);
			dataSet.remove(temp_or);
		}
		return sorted_dataSet;
	}

}