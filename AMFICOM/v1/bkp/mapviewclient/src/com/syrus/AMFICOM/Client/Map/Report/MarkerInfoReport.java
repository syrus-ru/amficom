package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;

import javax.swing.table.TableColumn;

public class MarkerInfoReport extends ReportData {
	public MarkerInfoReport(ObjectsReport report, int divisionsNumber)
			throws CreateReportException {
		super.tableModel = new MarkerInfoReportTableModel(
				divisionsNumber,
				report);
		super.columnModel = new MarkerInfoReportColumnModel(divisionsNumber);
	}
}

class MarkerInfoReportColumnModel extends DividableTableColumnModel {
	public MarkerInfoReportColumnModel(int divisionsNumber) {
		super(divisionsNumber);

		for(int j = 0; j < this.getDivisionsNumber(); j++) {
			this.addColumn(new TableColumn(j * 2, 150));// site name
			this.addColumn(new TableColumn(j * 2 + 1, 100));// cable reserve at
															// entrance
		}
	}
}

class MarkerInfoReportTableModel extends DividableTableModel {
	int length = 0;

	String[][] tableData = null;

	public MarkerInfoReportTableModel(int divisionsNumber, ObjectsReport report)
			throws CreateReportException {
		super(divisionsNumber, 4);

		Identifier markerId = (Identifier )report.getReserve();
		if(markerId == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.cantImplement);

		Marker marker = null;
		try {
			marker = (Marker )MapStorableObjectPool.getStorableObject(
					markerId,
					false);
		} catch(DatabaseException dExc) {
			dExc.printStackTrace();
		} catch(CommunicationException cExc) {
			cExc.printStackTrace();
		}

		MarkerController mc = (MarkerController )MarkerController.getInstance();

		if(marker == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.poolObjNotExists);

		CablePath cablePath = marker.getCablePath();
		NodeLink nodeLink = marker.getNodeLink();

		this.length = 12;

		this.tableData = new String[this.getBaseColumnCount()][];
		for(int i = 0; i < this.getBaseColumnCount(); i++)
			this.tableData[i] = new String[this.length + 1];
		// +1 - reserve for name of tunnel in collector

		int curCCI = 0;

		this.tableData[0][curCCI] = LangModelMap.getString("Marker");
		this.tableData[1][curCCI++] = marker.getName();

		this.tableData[0][curCCI] = "";
		this.tableData[1][curCCI++] = "";

		this.tableData[0][curCCI] = LangModelMap.getString("Cable");
		this.tableData[1][curCCI++] = cablePath.getName();// !!!!!!Тут фигня
															// возможно

		this.tableData[0][curCCI] = "";
		this.tableData[1][curCCI++] = "";

		this.tableData[0][curCCI] = LangModelMap.getString("mapnodedistances");
		this.tableData[1][curCCI++] = nodeLink.getName();// !!!!!!И тут тоже

		this.tableData[0][curCCI] = "";
		this.tableData[1][curCCI++] = "";

		this.tableData[0][curCCI] = "fornode" + " "
				+ marker.getLeft().getName();
		try {
			this.tableData[1][curCCI++] = Double.toString(
					mc.getPhysicalDistanceFromLeft(marker));
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.tableData[0][curCCI] = "fornode" + " "
				+ marker.getRight().getName();
		try {
			this.tableData[1][curCCI++] = Double.toString(
					mc.getPhysicalDistanceFromRight(marker));
		} catch(MapConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(MapDataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.tableData[0][curCCI] = "";
		this.tableData[1][curCCI++] = "";

		PhysicalLink physicalLink = null;
		try {
			physicalLink = (PhysicalLink )MapStorableObjectPool
					.getStorableObject(
							marker.getNodeLink().getPhysicalLink().getId(), 
							false);
		} catch(DatabaseException dExc) {
			dExc.printStackTrace();
		} catch(CommunicationException cExc) {
			cExc.printStackTrace();
		}

		if(physicalLink == null)
			throw new CreateReportException(
					report.getName(),
					CreateReportException.poolObjNotExists);

		Map map = physicalLink.getMap(); // Возможно лажа!!
		Collector pipePath = map.getCollector(physicalLink);
		if(pipePath != null) {
			this.tableData[0][curCCI] = LangModelMap.getString("Collector");
			this.tableData[1][curCCI++] = pipePath.getName();

			this.tableData[0][curCCI] = LangModelMap.getString("Tunnel");
			this.tableData[1][curCCI++] = physicalLink.getName();
			this.length++;

			this.tableData[0][curCCI] = LangModelMap
					.getString("maptunnelposit");
		} else {
			this.tableData[0][curCCI] = LangModelMap.getString("Tunnel");
			this.tableData[1][curCCI++] = physicalLink.getName();
			this.tableData[0][curCCI] = LangModelMap
					.getString("mapcollectorposit");
		}

		IntPoint binding = physicalLink.getBinding().getBinding(cablePath);// И
																			// здесь
		this.tableData[1][curCCI++] = Integer.toString(binding.x) + ":"
				+ Integer.toString(binding.y);

		this.tableData[0][curCCI] = LangModelMap.getString("geographicCoords");
		this.tableData[1][curCCI++] = Double.toString(
				marker.getLocation().getX())
				+ ":" + Double.toString(marker.getLocation().getY());
	}

	private String getSiteFullName(Map map, Identifier id) {
		SiteNode siteNode = map.getSiteNode(id);
		if(siteNode == null)
			return null;

		SiteNodeType nodeType = (SiteNodeType )siteNode.getType();
		return nodeType.getName() + " " + siteNode.getName();
	}

	public int getRowCount() {
		// Если данные можно разложить поровну на такое количество столбцов
		if(this.length % this.getDivisionsNumber() == 0)
			return (this.length / this.getDivisionsNumber()); // +заголовок
		// а если нельзя, то добавляем ещё ряд
		return (this.length / this.getDivisionsNumber()) + 1;
	}

	public Object getValueAt(int row, int col) {
		int index = (this.getRowCount()) * (col / this.getBaseColumnCount())
				+ row - 1;

		if(index >= this.length)
			return "";

		return this.tableData[col % this.getBaseColumnCount()][index];
	}
}
