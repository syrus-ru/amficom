package com.syrus.AMFICOM.Client.Map.Report;

import java.util.ListIterator;

import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

public class TunnelCableListReport extends ReportData
{
	public TunnelCableListReport(ObjectsReport report, int divisionsNumber)
		throws CreateReportException
	{
		super.tableModel = new TunnelCableListReportTableModel(
				divisionsNumber,
				report);
		super.columnModel = new TunnelCableListReportColumnModel(
				divisionsNumber);
	}
}

class TunnelCableListReportColumnModel extends DividableTableColumnModel
{
	public TunnelCableListReportColumnModel(int divisionsNumber)
	{
		super(divisionsNumber);

		for(int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2, 200));// site name
			this.addColumn(new TableColumn(j * 2 + 1, 100));// cable reserve at
															// entrance
		}
	}
}

class TunnelCableListReportTableModel extends DividableTableModel
{
	int length = 0;

	String[][] tableData = null;

	public TunnelCableListReportTableModel(
			int divisionsNumber,
			ObjectsReport report) throws CreateReportException
	{
		super(divisionsNumber, 2);

		Identifier physicalLinkId = (Identifier )report.getReserve();
		if(physicalLinkId == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.cantImplement);

		PhysicalLink physicalLink = null;
		try
		{
			physicalLink = (PhysicalLink )MapStorableObjectPool
					.getStorableObject(physicalLinkId, false);
		} catch(ApplicationException e) {
			e.printStackTrace();
		}

		if(physicalLink == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.poolObjNotExists);

		// number of table rows
		this.length = physicalLink.getBinding().getBindObjects().size() + 6;
		int tdIterator = 0;
		for(int i = 0; i < 2; i++)
			this.tableData[i] = new String[this.length];

		Map map = physicalLink.getMap(); // Возможно лажа!!

		String fullLinkName = "";
		Collector pipePath = map.getCollector(physicalLink);
		if(pipePath != null)
			fullLinkName += LangModelMap.getString("Collector")
					+ pipePath.getName();
		else
			fullLinkName += LangModelMap.getString("Tunnel")
					+ physicalLink.getName();

		this.tableData[0][tdIterator] = fullLinkName;
		this.tableData[1][tdIterator++] = "";

		this.tableData[0][tdIterator] = LangModelMap.getString("Start_node_id");
		this.tableData[1][tdIterator++] = physicalLink.getStartNode().getName();

		this.tableData[0][tdIterator] = LangModelMap.getString("End_node_id");
		this.tableData[1][tdIterator++] = physicalLink.getStartNode().getName();

		this.tableData[0][tdIterator] = "";
		this.tableData[1][tdIterator++] = "";

		this.tableData[0][tdIterator] = LangModelMap.getString("tunnelCableList");
		this.tableData[1][tdIterator++] = "";

		this.tableData[0][tdIterator] = LangModelMap.getString("Cable");
		if(pipePath != null)
			this.tableData[1][tdIterator++] = LangModelMap
					.getString("maptunnelposit");
		else
			this.tableData[1][tdIterator++] = LangModelMap
					.getString("mapcollectorposit");

		// Getting scheme cable link iterator
		ListIterator sclIterator = physicalLink.getBinding().getBindObjects()
				.listIterator();

		for(; sclIterator.hasNext();)
		{
			SchemeCableLink cableLink = (SchemeCableLink )sclIterator.next();

			// Имя колодца/узла
			this.tableData[0][tdIterator] = cableLink.getName();
			// Информация о тоннеле - строка типа Тоннель тон.1, место N, L =
			// xxx
			IntPoint binding = physicalLink.getBinding().getBinding(cableLink);
			this.tableData[1][tdIterator++] = Integer.toString(binding.x) + ":"
					+ Integer.toString(binding.y);
		}
	}

	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if(this.length % this.getDivisionsNumber() == 0)
			return (this.length / this.getDivisionsNumber());
		// а если нельзя, то добавляем ещё ряд
		return (this.length / this.getDivisionsNumber()) + 1;
	}

	public Object getValueAt(int row, int col)
	{
		int index = this.getRowCount()
				* (col / this.getBaseColumnCount()) + row - 1;
		if(index >= this.length)
			return "";

		return this.tableData[col % this.getBaseColumnCount()][index];
	}
}
