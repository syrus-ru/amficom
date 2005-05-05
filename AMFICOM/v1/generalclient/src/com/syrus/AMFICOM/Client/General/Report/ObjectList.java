package com.syrus.AMFICOM.Client.General.Report;

import java.util.List;

import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;

/**
 * <p>Title: </p>
 * <p>Description: �����, ����������� �����-������ �������� </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public final class ObjectList
		extends ReportData
{
//	public Vector objects = null;

	public DividableTableColumnModel columnModel = null;
	public DividableTableModel tableModel = null;

	public ObjectList(int divisionsNumber,
										List objects,
										ObjectsReport or) throws CreateReportException
	{
//		this.objects = objects;

		if (! (or.model instanceof ObjectResourceReportModel))
			throw new CreateReportException(or.getName(),
																			CreateReportException.cantImplement);

		ObjectResourceReportModel orrm = (ObjectResourceReportModel) or.model;

		columnModel = new ObjectListColumnModel(
				divisionsNumber,
				(List) or.getReserve(),
				orrm);

		tableModel = new objectListModel(divisionsNumber,
																		 objects,
																		 (List) or.getReserve(),
																		 orrm.getColumnNamesbyIDs((List) or.
																			 getReserve()));
	}
}

final class ObjectListColumnModel
		extends DividableTableColumnModel
{
	public ObjectListColumnModel(
			int divisionsNumber,
			List columnIDs,
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
	private List objects = null;
	private List columnIDs = null;
	private List columnNames = null;
	private int length = 0;
	private int width = 0;

	public objectListModel(
			int divisionsNumber,
			List objects,
			List columnIDs,
			List columnNames)
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
		// ���� ������ ����� ��������� ������� �� ����� ���������� ��������
		if (length % this.getDivisionsNumber() == 0)
			return (int) (length / this.getDivisionsNumber()) + 1; //+���������
		//� ���� ������, �� ��������� ��� ���
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
