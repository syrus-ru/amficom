/*
 * $Id: ObjectResourceDivList.java,v 1.3 2004/11/15 14:30:21 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import javax.swing.table.*;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.3 $, $Date: 2004/11/15 14:30:21 $
 * @module generalclient_v1
 */
public class ObjectResourceDivList extends ReportData
{
	public ObjectResourceDivList(ObjectsReport report,
				int divisionsNumber) throws CreateReportException
	{
		tableModel = new ORDTableModel(divisionsNumber, report);
		columnModel = new ORDTableColumnModel(divisionsNumber,
			tableModel.getColumnCount(),report);
	}

	private class ORDTableColumnModel extends DividableTableColumnModel
	{
		private ORDTableColumnModel(
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

	private final class ORDTableModel extends DividableTableModel
	{
		private int width = 0;
	
		private int length = 0;
	
		private ObjectResourceTableModel model = null;
	
		private boolean viewColumnNames = false;
	
		private ORDTableModel(int divisionsNumber,
			ObjectsReport report) throws CreateReportException
		{
			super(divisionsNumber,
				((TableModel) report.getReserve()).getColumnCount());
	
			model = (ObjectResourceTableModel) report.getReserve();
	
			width = model.getColumnCount();
			length = model.getRowCount();
	
			for (int i = 0; i < width; i++)
				if (!model.getColumnName(i).equals(""))
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
				return model.getColumnName(col % width);
	
			int index = (this.getRowCount() - 1) * (int) (col / width) + row - 1;
	
			if (index >= this.length)
				return "";
	
			if (!viewColumnNames)
				index++;
	
			String text = model.getValueAt(index, col).toString();

			return text;
		}
	}
}
