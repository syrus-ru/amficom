/*
 * $Id: ObjectResourceDivList.java,v 1.2 2004/09/27 09:20:34 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTableModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import javax.swing.table.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/27 09:20:34 $
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
	
			String column_id = model.getColumnByNumber(col % width);
			ObjectResource or = (ObjectResource) model.getValueAt(index, col);
			String text = or.getModel().getColumnValue(column_id);
			return text;
		}
	}
}
