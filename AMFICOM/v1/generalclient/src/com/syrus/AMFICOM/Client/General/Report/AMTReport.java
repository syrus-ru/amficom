package com.syrus.AMFICOM.Client.General.Report;

import java.util.Hashtable;
import javax.swing.table.TableModel;
import javax.swing.JPanel;

public class AMTReport
{
	public Hashtable data = new Hashtable();

	public AMTReport()
	{
	}

	public void addRecord(String teTitle, Object teData)
	{
		data.put(teTitle,teData);
	}
}