package com.syrus.AMFICOM.Client.Map.Report;

import java.util.Iterator;

import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

public class CableLayoutReport extends ReportData
{
	public CableLayoutReport(ObjectsReport report, int divisionsNumber)
		throws CreateReportException
	{
		super.tableModel = new CableLayoutReportTableModel(divisionsNumber, report);
		super.columnModel = new CableLayoutReportColumnModel(divisionsNumber);
	}
}

class CableLayoutReportColumnModel extends DividableTableColumnModel
{
	public CableLayoutReportColumnModel(int divisionsNumber)
	{
		super(divisionsNumber);

		for(int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 4, 100));// site name
			this.addColumn(new TableColumn(j * 4 + 1, 250));// cable reserve at
															// entrance
			this.addColumn(new TableColumn(j * 4 + 2, 100));// cable reserve at
															// exit
			this.addColumn(new TableColumn(j * 4 + 3, 100));// tunnel info
		}
	}
}

class CableLayoutReportTableModel extends DividableTableModel
{
	int length = 0;

	String[][] tableData = null;

	public CableLayoutReportTableModel(int divisionsNumber, ObjectsReport report)
		throws CreateReportException
	{
		super(divisionsNumber, 4);

		com.syrus.AMFICOM.general.Identifier schemeCableLinkId = (com.syrus.AMFICOM.general.Identifier )report
				.getReserve();
		if(schemeCableLinkId == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.cantImplement);

		SchemeCableLink scLink = null;
		try
		{
			scLink = (SchemeCableLink )SchemeStorableObjectPool
					.getStorableObject(schemeCableLinkId, false);
		}
		catch(DatabaseException dExc)
		{
			dExc.printStackTrace();
		}
		catch(CommunicationException cExc)
		{
			cExc.printStackTrace();
		}

		if(scLink == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.poolObjNotExists);

		Scheme scheme = scLink.getParentScheme();
		if(scheme == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.poolObjNotExists);

		Map map = scheme.getMap();
		if(map == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.poolObjNotExists);

		this.length = scLink.getCableChannelingItems().size() + 1;

		this.tableData = new String[this.getBaseColumnCount()][];
		for(int i = 0; i < this.getBaseColumnCount(); i++)
			this.tableData[i] = new String[this.length];

		int curCCI = 0;
		for(Iterator iter = scLink.getCableChannelingItems().iterator(); iter.hasNext();) {
			CableChannelingItem chanellingItem = (CableChannelingItem )iter.next();

			String fullName = this.getSiteFullName(map, chanellingItem
					.getStartSiteNode());
			if(fullName == null)
				throw new CreateReportException(
						report.getName(),
						CreateReportException.poolObjNotExists);

			// Имя колодца/узла
			this.tableData[0][curCCI] = fullName;

			// Запас на входе
			this.tableData[1][curCCI] = Double.toString(chanellingItem.getStartSpare());

			// Запас на выходе
			if(curCCI != 0)
				this.tableData[3][curCCI] = Double.toString(chanellingItem
						.getEndSpare());
			else
				this.tableData[3][curCCI] = "--";

			// Информация о тоннеле - строка типа Тоннель тон.1, место N, L =
			// xxx

			if(curCCI != (this.length - 1))
			{
				String tunnelInfo = "";

				PhysicalLink physicalLink = chanellingItem.getPhysicalLink();

				if(physicalLink == null)
					throw new CreateReportException(
							report.getName(),
							CreateReportException.poolObjNotExists);

				// Тип и имя тоннеля
				Collector pipePath = map.getCollector(physicalLink);
				if(pipePath != null)
					tunnelInfo += LangModelMap.getString("Collector")
							+ pipePath.getName();
				else
					tunnelInfo += LangModelMap.getString("Tunnel")
							+ chanellingItem.getName();

				int place = physicalLink.getBinding().getSequenceNumber(
						chanellingItem.getRowX(),
						chanellingItem.getPlaceY());

				// Место в тоннеле
				tunnelInfo += "," + LangModelMap.getString("maptunnelposit")
						+ ": " + Integer.toString(place);

				// Длина тоннеля
				tunnelInfo += ", L = " + physicalLink.getLengthLt() + "м";

				this.tableData[2][curCCI] = tunnelInfo;
			}

			// Информация о замыкающем узле - имя + запас на входе
			if(!iter.hasNext())
			{
				fullName = this.getSiteFullName(map, chanellingItem
						.getEndSiteNode());
				if(fullName == null)
					throw new CreateReportException(
							report.getName(),
							CreateReportException.poolObjNotExists);

				// Имя колодца/узла
				this.tableData[0][curCCI] = fullName;
			}

			curCCI++;
		}
	}

	private String getSiteFullName(Map map, SiteNode node)
	{
		SiteNodeType nodeProto = (SiteNodeType )node.getType();

		if(nodeProto == null)
			return null;

		return nodeProto.getName() + " " + node.getName();
	}

	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if(this.length % this.getDivisionsNumber() == 0)
			return (this.length / this.getDivisionsNumber()) + 1; // +заголовок
		// а если нельзя, то добавляем ещё ряд
		return (this.length / this.getDivisionsNumber()) + 2;
	}

	public Object getValueAt(int row, int col)
	{
		if(row == 0)
			switch(col % this.getBaseColumnCount())
			{
				case 0:
					return LangModelMap.getString("StartNode");
				case 1:
					return LangModelMap.getString("EndSpare");
				case 2:
					return LangModelMap.getString("StartLink");
				case 3:
					return LangModelMap.getString("StartSpare");
			}

		int index = (this.getRowCount() - 1)
				* (col / this.getBaseColumnCount()) + row - 1;

		if(index >= this.length)
			return "";

		return this.tableData[col % this.getBaseColumnCount()][index];
	}
}
