package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import javax.swing.JInternalFrame;
import javax.swing.table.TableModel;

public abstract class ATableFrame extends JInternalFrame
{
	public abstract String getReportTitle();
	public abstract TableModel getTableModel();
}