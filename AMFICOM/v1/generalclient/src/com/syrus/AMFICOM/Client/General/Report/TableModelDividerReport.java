/*
 * $Id: TableModelDividerReport.java,v 1.5 2004/09/27 07:52:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.table.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 07:52:58 $
 * @module generalclient_v1
 */
public class TableModelDividerReport extends ReportData
{
	public TableModelDividerReport(ObjectsReport report,
		int divisionsNumber) throws CreateReportException
	{
		tableModel = new TMDRTableModel(divisionsNumber, report);
		columnModel = new TMDRTableColumnModel(divisionsNumber,
			tableModel.getColumnCount(),report);
	}

	private final class TMDRTableColumnModel extends DividableTableColumnModel
	{
		private TMDRTableColumnModel(
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
		}
	}

	private final class TMDRTableModel extends DividableTableModel
	{
		private int width = 0;
	
		private int length = 0;
	
		private TableModel model = null;
	
		private boolean viewColumnNames = false;
	
		private TMDRTableModel(int divisionsNumber,
			ObjectsReport report) throws CreateReportException
		{
			super(divisionsNumber,
				((TableModel) report.getReserve()).getColumnCount());
	
			model = (TableModel) report.getReserve();
	
			width = model.getColumnCount();
			length = model.getRowCount();
	
	    for (int i = 0; i < width; i++)
	    {
	      String curColName = model.getColumnName(i);
	      if ((curColName != null) && (!curColName.equals("")))
	      {
		viewColumnNames = true;
		length++;
		break;
	      }
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
				return model.getColumnName(col % width);
	
			int index = (this.getRowCount() - 1) * (int) (col / width) + row - 1;
	
			if (index >= this.length)
				return "";
	
			if (!viewColumnNames)
				index++;
	
			return model.getValueAt(index, col);
		}
	}
}
