package com.syrus.AMFICOM.Client.Analysis.Report;

import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class EvaluationTableReport extends ReportData
{
	public EvaluationTableReport(ObjectsReport report,
		int divisionsNumber) throws CreateReportException
	{
		tableModel = new EvaluationTableReportTableModel(divisionsNumber, report);
		columnModel = new EvaluationTableReportTableColumnModel(divisionsNumber,
			tableModel.getColumnCount(),report);
	}
}

class EvaluationTableReportTableColumnModel extends DividableTableColumnModel
{
	public EvaluationTableReportTableColumnModel(
		int divisionsNumber,
		int columnCount,
		ObjectsReport report)
	{
		super(divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
			for (int i = 0; i < columnCount; i++)
				this.addColumn(new TableColumn(
					j * columnCount + i,
					100));
//					((ReportTable) report.getReserve()).model.)); //Value
	}
}

class EvaluationTableReportTableModel extends DividableTableModel
{
	int width = 0;

	int length = 0;

	ReportTable reportTable = null;

	boolean viewColumnNames = false;

	public EvaluationTableReportTableModel(int divisionsNumber,
		ObjectsReport report) throws CreateReportException
	{
		super(divisionsNumber,
			((ReportTable) report.getReserve()).model.getColumnCount());

		reportTable = (ReportTable) report.getReserve();

		width = reportTable.model.getColumnCount();
		length = reportTable.model.getRowCount();

		for (int i = 0; i < width; i++)
			if (!reportTable.model.getColumnName(i).equals(""))
			{
				viewColumnNames = true;
				length++;
				break;
			}
	}

	public int getColumnCount()
	{
		return width * this.getDivisionsNumber();
	}

	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if (length % this.getDivisionsNumber() == 0)
			return (int) (length / this.getDivisionsNumber());
		//а если нельзя, то добавляем ещё ряд
		return (int) (length / this.getDivisionsNumber()) + 1;
	}

	public Object getValueAt(int row, int col)
	{
		if ((row == 0) && viewColumnNames)
			return reportTable.model.getColumnName(col % width);

		int index = (this.getRowCount() - 1) * (int) (col / width) + row - 1;

		if (index >= this.length)
			return "";

		if (!viewColumnNames)
			index++;

		return reportTable.model.getValueAt(index, col);
	}
}