package com.syrus.AMFICOM.Client.General.Report;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

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

public class Statistics
		extends ReportData
{
	public List fieldValues = new ArrayList();
	public List timesFound = new ArrayList();

	public static String field = "";

	public Statistics(
			int divisionsNumber,
			List objects,
			String id,
			ObjectResourceReportModel orrm)
	{
		field = orrm.getObjectsName() + " : " + orrm.getColumnNamebyID(id);

		for (int i = 0; i < objects.size(); i++)
		{
			ObjectResource or = (ObjectResource) objects.get(i);
			String curFieldValue = or.getModel().getColumnValue(id);

			int valueFoundPosit = -1;
			for (int j = 0; j < fieldValues.size(); j++)

				if (fieldValues.get(j).equals(curFieldValue))
				{
					valueFoundPosit = j;
					Integer timesFoundValue = (Integer) timesFound.get(j);
					Integer newTimesFoundValue = new Integer(timesFoundValue.intValue() +
							1);
					timesFound.set(j, newTimesFoundValue);
					break;
				}

			if (valueFoundPosit == -1)
			{
				fieldValues.add(curFieldValue);
				timesFound.add(new Integer(1));
			}
		}
		tableModel = new StatTableModel(divisionsNumber, this);
		columnModel = new StatTableColumnModel(divisionsNumber);
	}
}

class StatTableColumnModel
		extends DividableTableColumnModel
{
	public StatTableColumnModel(int divisionsNumber)
	{
		super(divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2, 170)); //name
			this.addColumn(new TableColumn(j * 2 + 1, 100)); //number
		}
	}
}

class StatTableModel
		extends DividableTableModel
{
	Statistics data = null;
	int length = 0;

	public StatTableModel(int divisionsNumber, Statistics stat)
	{
		super(divisionsNumber, 2);
		data = stat;
		length = stat.fieldValues.size();
	}

	public int getColumnCount()
	{
		return 2 * this.getDivisionsNumber();
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
			switch (col % 2)
			{
				case 0:
					return LangModelReport.getString("label_value");
				case 1:
					return LangModelReport.getString("label_number");
			}

		int index = (this.getRowCount() - 1) * (int) (col / 2) + row - 1;
		if (index >= data.fieldValues.size())
			return "";

		switch (col % 2)
		{
			case 0:
				return data.fieldValues.get(index);
			case 1:
				return data.timesFound.get(index);
		}

		return "###";
	}
}
