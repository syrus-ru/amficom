package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.util.Iterator;

import javax.swing.table.TableColumn;

public class CableLayoutReport extends ReportData
{
	public CableLayoutReport(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new CableLayoutReportTableModel(divisionsNumber,report);
		columnModel = new CableLayoutReportColumnModel(divisionsNumber);
	}
}

class CableLayoutReportColumnModel extends DividableTableColumnModel
{
	public CableLayoutReportColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 4,100));//site name
			this.addColumn(new TableColumn(j * 4 + 1,250));//cable reserve at entrance
			this.addColumn(new TableColumn(j * 4 + 2,100));//cable reserve at exit
			this.addColumn(new TableColumn(j * 4 + 3,100));//tunnel info
		}
	}
}

class CableLayoutReportTableModel extends DividableTableModel
{
	int length = 0;
  String[][] tableData = null;

	public CableLayoutReportTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,4);

		String schemeCableLink_id = (String)report.getReserve();
		if (schemeCableLink_id == null)
			throw new CreateReportException(report.getName(),CreateReportException.cantImplement);

		SchemeCableLink scLink =
      (SchemeCableLink)Pool.get(SchemeCableLink.typ,schemeCableLink_id);
      
		if (scLink == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

    Scheme scheme = (Scheme) Pool.get(Scheme.typ,scLink.getSchemeId());
		if (scheme == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

    Map map = scheme.getMap();
		if (map == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

    scLink.channelingItems.setMap(map);
    Iterator cciIterator = scLink.channelingItems.iterator();
    
    length = scLink.channelingItems.size() + 1;
    
    tableData = new String[this.getBaseColumnCount()][];
    for (int i = 0; i < this.getBaseColumnCount(); i++)
      tableData[i] = new String[length];
    
    int curCCI = 0;
    for (;cciIterator.hasNext();)
    {
      CableChannelingItem chanellingItem = (CableChannelingItem) cciIterator.next();
      
      String fullName = this.getSiteFullName(map,chanellingItem.startSiteId);
      if (fullName == null)
        throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);
        
      // Имя колодца/узла      
      tableData[0][curCCI] = fullName;

      //Запас на входе
      tableData[1][curCCI] = Double.toString(chanellingItem.startSpare);

      //Запас на выходе
      if (curCCI != 0)
        tableData[3][curCCI] = Double.toString(chanellingItem.endSpare);
      else
        tableData[3][curCCI] = "--";

      // Информация о тоннеле - строка типа Тоннель тон.1, место N, L = xxx
      
      if (curCCI != (length - 1))
      {
        String tunnelInfo = "";
        
        MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement) Pool.get(
          MapPhysicalLinkElement.typ,
          chanellingItem.physicalLinkId);
          
        if (physicalLink == null)
          throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);
        
        //Тип и имя тоннеля
        MapPipePathElement pipePath =  map.getCollector(physicalLink);
        if (pipePath != null)
          tunnelInfo += LangModelMap.getString("Collector") + pipePath.getName();
        else
          tunnelInfo += LangModelMap.getString("Tunnel") + chanellingItem.getName();
        
        int place = physicalLink.getBinding().getSequenceNumber(
          chanellingItem.row_x,
          chanellingItem.place_y);
        
        //Место в тоннеле
        tunnelInfo += "," + LangModelMap.getString("maptunnelposit") +
          ": " + Integer.toString(place);
        
        //Длина тоннеля
        tunnelInfo += ", L = " + physicalLink.getLengthLt() + "м";
        
        tableData[2][curCCI] = tunnelInfo;
      }
      
      curCCI++;
      
      //Информация о замыкающем узле - имя + запас на входе
      if (!cciIterator.hasNext())
      {
        fullName = this.getSiteFullName(map,chanellingItem.endSiteId);
        if (fullName == null)
          throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);
          
        // Имя колодца/узла      
        tableData[0][curCCI] = fullName;
      }
    }
		
	}

  private String getSiteFullName(Map map,String id)
  {
      MapSiteNodeElement siteNode = map.getMapSiteNodeElement(id);
      if (siteNode == null)
        return null;
        
      MapNodeProtoElement nodeProto = (MapNodeProtoElement) Pool.get(
        MapNodeProtoElement.typ,
        siteNode.getMapProtoId());
        
      if (nodeProto == null)
        return null;
 
      return nodeProto.getName() + " " + siteNode.getName();
  }

	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if (length % this.getDivisionsNumber() == 0)
			return  (int)(length / this.getDivisionsNumber()) + 1; //+заголовок
		//а если нельзя, то добавляем ещё ряд
		return (int)(length / this.getDivisionsNumber()) + 2;
	}

	public Object getValueAt(int row, int col)
	{
		if (row == 0)
			switch (col % this.getBaseColumnCount())
			{
				case 0: return LangModelMap.getString("StartNode");
				case 1: return LangModelMap.getString("EndSpare");
				case 2: return LangModelMap.getString("StartLink");
				case 3: return LangModelMap.getString("StartSpare");
			}

		int index = (this.getRowCount() - 1) *
      (int) (col / this.getBaseColumnCount()) + row - 1;
      
		if (index >= this.length)
			return "";

		return this.tableData[col % this.getBaseColumnCount()][index];
	}
}
