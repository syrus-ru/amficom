package com.syrus.AMFICOM.Client.General.Report;

import java.util.Vector;
import javax.swing.table.TableColumn;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

/**
 * <p>Title: </p>
 * <p>Description: Класс, реализующий отчёт-список объектов </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public final class ObjectList
		extends ReportData
{
//	public Vector objects = null;

	public DividableTableColumnModel columnModel = null;
	public DividableTableModel tableModel = null;

	public ObjectList(int divisionsNumber,
										Vector objects,
										ObjectsReport or) throws CreateReportException
	{
//		this.objects = objects;

		if (! (or.model instanceof ObjectResourceReportModel))
			throw new CreateReportException(or.getName(),
																			CreateReportException.cantImplement);

		ObjectResourceReportModel orrm = (ObjectResourceReportModel) or.model;

		columnModel = new ObjectListColumnModel(
				divisionsNumber,
				(Vector) or.getReserve(),
				orrm);

		tableModel = new objectListModel(divisionsNumber,
																		 objects,
																		 (Vector) or.getReserve(),
																		 orrm.getColumnNamesbyIDs((Vector) or.
																			 getReserve()));
	}
}

final class ObjectListColumnModel
		extends DividableTableColumnModel
{
	public ObjectListColumnModel(
			int divisionsNumber,
			Vector columnIDs,
			ObjectResourceReportModel model)
	{
		super(divisionsNumber);
		for (int j = 0; j < this.getDivisionsNumber(); j++)
			for (int i = 0; i < columnIDs.size(); i++)
			{
				String id = (String) columnIDs.get(i);
				int size = model.getColumnSizebyID(id);
				this.addColumn(new TableColumn(j * columnIDs.size() + i, size));
			}
	}
}

final class objectListModel
		extends DividableTableModel
{
	private Vector objects = null;
	private Vector columnIDs = null;
	private Vector columnNames = null;
	private int length = 0;
	private int width = 0;

	public objectListModel(
			int divisionsNumber,
			Vector objects,
			Vector columnIDs,
			Vector columnNames)
	{
		super(divisionsNumber, columnIDs.size());
		this.objects = objects;
		this.columnIDs = columnIDs;
		this.columnNames = columnNames;

		length = objects.size();
		width = columnIDs.size();
	}

	public int getColumnCount()
	{
		return width * this.getDivisionsNumber();
	}

	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if (length % this.getDivisionsNumber() == 0)
			return (int) (length / this.getDivisionsNumber()) + 1; //+заголовок
		//а если нельзя, то добавляем ещё ряд
		return (int) (length / this.getDivisionsNumber()) + 2;
	}

	public Object getValueAt(int row, int col)
	{
		if (row == 0)
			return (String) columnNames.get(col % width);

		int orNumber = (this.getRowCount() - 1) * (int) (col / width) + row - 1;
		if (orNumber >= objects.size())
			return "";

		ObjectResource or = (ObjectResource) objects.get(orNumber);

		return or.getModel().getColumnValue( (String) columnIDs.get(col % width));
	}
}
