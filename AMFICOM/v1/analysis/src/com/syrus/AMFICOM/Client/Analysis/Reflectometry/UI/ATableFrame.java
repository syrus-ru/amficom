package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import javax.swing.table.TableModel;
import javax.swing.JInternalFrame;

public abstract class ATableFrame extends JInternalFrame
{
	public abstract String getReportTitle();
	public abstract TableModel getTableModel();
}