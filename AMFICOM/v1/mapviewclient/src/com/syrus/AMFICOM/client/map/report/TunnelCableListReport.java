package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.Resource.Map.IntPoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.awt.Point;

import java.util.ListIterator;

import javax.swing.table.TableColumn;

public class TunnelCableListReport extends ReportData
{
	public TunnelCableListReport(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new TunnelCableListReportTableModel(divisionsNumber,report);
		columnModel = new TunnelCableListReportColumnModel(divisionsNumber);
	}
}

class TunnelCableListReportColumnModel extends DividableTableColumnModel
{
	public TunnelCableListReportColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2,200));//site name
			this.addColumn(new TableColumn(j * 2 + 1,100));//cable reserve at entrance
		}
	}
}

class TunnelCableListReportTableModel extends DividableTableModel
{
	int length = 0;
  String[][] tableData = null;

	public TunnelCableListReportTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,2);

		String physicalLink_id = (String)report.getReserve();
		if (physicalLink_id == null)
			throw new CreateReportException(report.getName(),CreateReportException.cantImplement);

		MapPhysicalLinkElement physicalLink =
      (MapPhysicalLinkElement)Pool.get(MapPhysicalLinkElement.typ,physicalLink_id);
      
		if (physicalLink == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

    //number of table rows
    length = physicalLink.getBinding().getBindObjects().size() + 6;
    int tdIterator = 0;
    for (int i = 0; i < 2; i++)
      tableData[i] = new String[length];
    
    Map map = physicalLink.getMap(); //Возможно лажа!!

    String fullLinkName = "";
    MapPipePathElement pipePath = map.getCollector(physicalLink);
    if (pipePath != null)
      fullLinkName += LangModelMap.getString("Collector") + pipePath.getName();
    else
      fullLinkName += LangModelMap.getString("Tunnel") + physicalLink.getName();

    tableData[0][tdIterator] = fullLinkName;
    tableData[1][tdIterator++] = "";
    
    tableData[0][tdIterator] = LangModelMap.getString("Start_node_id");
    tableData[1][tdIterator++] = physicalLink.getStartNode().getName();

    tableData[0][tdIterator] = LangModelMap.getString("End_node_id");
    tableData[1][tdIterator++] = physicalLink.getStartNode().getName();

    tableData[0][tdIterator] = "";
    tableData[1][tdIterator++] = "";

    tableData[0][tdIterator] = LangModelMap.getString("tunnelCableList");
    tableData[1][tdIterator++] = "";

    tableData[0][tdIterator] = LangModelMap.getString("Cable");
    if (pipePath != null)
      tableData[1][tdIterator++] = LangModelMap.getString("maptunnelposit");
    else
      tableData[1][tdIterator++] = LangModelMap.getString("mapcollectorposit");

    //Getting scheme cable link iterator
    ListIterator sclIterator =
      physicalLink.getBinding().getBindObjects().listIterator();
      
    for (;sclIterator.hasNext();)
    {
      SchemeCableLink cableLink = (SchemeCableLink) sclIterator.next();
     
      // Имя колодца/узла      
      tableData[0][tdIterator] = cableLink.getName();
      // Информация о тоннеле - строка типа Тоннель тон.1, место N, L = xxx
      IntPoint binding = physicalLink.getBinding().getBinding(cableLink);
      tableData[1][tdIterator++] =
        Integer.toString(binding.x) + ":" + Integer.toString(binding.y);
    }
	}

	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if (length % this.getDivisionsNumber() == 0)
			return  (int)(length / this.getDivisionsNumber());
		//а если нельзя, то добавляем ещё ряд
		return (int)(length / this.getDivisionsNumber()) + 1;
	}

	public Object getValueAt(int row, int col)
	{
		int index = this.getRowCount() * (int) (col / this.getBaseColumnCount()) + row - 1;
		if (index >= this.length)
			return "";

		return this.tableData[col % this.getBaseColumnCount()][index];
	}
}
