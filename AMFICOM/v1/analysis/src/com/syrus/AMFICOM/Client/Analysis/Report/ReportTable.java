package com.syrus.AMFICOM.Client.Analysis.Report;

import javax.swing.table.TableModel;

public class ReportTable
{
	String title = "";
	TableModel model;
	//String[] columns;
	//String[] keys;
	//Object[] values;

	ReportTable(String title, TableModel model)
	{
		this.title = title;
		this.model = model;
	}
/*
	ReportTable(String title, String[] columns, String[] keys, Object[] values)
	{
		this.title = title;
		this.columns = columns;
		this.keys = keys;
		this.values = values;
	}*/
}
