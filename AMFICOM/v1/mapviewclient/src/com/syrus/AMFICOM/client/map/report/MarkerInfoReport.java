package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.PathElement;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

import java.awt.Point;

import javax.swing.table.TableColumn;

public class MarkerInfoReport extends ReportData
{
	public MarkerInfoReport(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new MarkerInfoReportTableModel(divisionsNumber,report);
		columnModel = new MarkerInfoReportColumnModel(divisionsNumber);
	}
}

class MarkerInfoReportColumnModel extends DividableTableColumnModel
{
	public MarkerInfoReportColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2,150));//site name
			this.addColumn(new TableColumn(j * 2 + 1,100));//cable reserve at entrance
		}
	}
}

class MarkerInfoReportTableModel extends DividableTableModel
{
	int length = 0;
  String[][] tableData = null;

	public MarkerInfoReportTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,4);

		Identifier markerId = (Identifier)report.getReserve();
		if (markerId == null)
			throw new CreateReportException(report.getName(),CreateReportException.cantImplement);

		Marker marker =	null;
		try
		{
			marker = (Marker)MapStorableObjectPool.getStorableObject(markerId,false);
		}
		catch (DatabaseException dExc)
		{
			dExc.printStackTrace();
		}
		catch (CommunicationException cExc)
		{
			cExc.printStackTrace();
		}

		MarkerController mc = (MarkerController)MarkerController.getInstance();
      
		if (marker == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

    CablePath cablePath = marker.getCablePath();
		NodeLink nodeLink = marker.getNodeLink();

    length = 12;
    
    tableData = new String[this.getBaseColumnCount()][];
    for (int i = 0; i < this.getBaseColumnCount(); i++)
      tableData[i] = new String[length + 1];
    //+1 - reserve for name of tunnel in collector
    
    int curCCI = 0;
    
    tableData[0][curCCI] = LangModelMap.getString("Marker");
    tableData[1][curCCI++] = marker.getName();

    tableData[0][curCCI] = "";
    tableData[1][curCCI++] = "";
    
    tableData[0][curCCI] = LangModelMap.getString("Cable");
    tableData[1][curCCI++] = cablePath.getName();//!!!!!!Тут фигня возможно

    tableData[0][curCCI] = "";
    tableData[1][curCCI++] = "";

    tableData[0][curCCI] = LangModelMap.getString("mapnodedistances");
    tableData[1][curCCI++] = nodeLink.getName();//!!!!!!И тут тоже

    tableData[0][curCCI] = "";
    tableData[1][curCCI++] = "";

    tableData[0][curCCI] = "fornode" + " " + marker.getLeft().getName();
    tableData[1][curCCI++] = Double.toString(mc.getPhysicalDistanceFromLeft(marker));

    tableData[0][curCCI] = "fornode" + " " + marker.getRight().getName();
    tableData[1][curCCI++] = Double.toString(mc.getPhysicalDistanceFromRight(marker));
    
    tableData[0][curCCI] = "";
    tableData[1][curCCI++] = "";

    PhysicalLink physicalLink = null;
		try
		{
			physicalLink = (PhysicalLink)MapStorableObjectPool.getStorableObject(
				marker.getNodeLink().getPhysicalLink().getId(),false);
		}
		catch (DatabaseException dExc)
		{
			dExc.printStackTrace();
		}
		catch (CommunicationException cExc)
		{
			cExc.printStackTrace();
		}
		
    if (physicalLink == null)
			throw new CreateReportException(
				report.getName(),
				CreateReportException.poolObjNotExists);
      
    Map map = physicalLink.getMap(); //Возможно лажа!!
    Collector pipePath =  map.getCollector(physicalLink);
    if (pipePath != null)
    {
      tableData[0][curCCI] = LangModelMap.getString("Collector");
      tableData[1][curCCI++] = pipePath.getName();
      
      tableData[0][curCCI] = LangModelMap.getString("Tunnel");
      tableData[1][curCCI++] = physicalLink.getName();
      length++;
 
      tableData[0][curCCI] = LangModelMap.getString("maptunnelposit");
    }
    else
    {
      tableData[0][curCCI] = LangModelMap.getString("Tunnel");
      tableData[1][curCCI++] = physicalLink.getName();
      tableData[0][curCCI] = LangModelMap.getString("mapcollectorposit");
    }
    
    IntPoint binding = physicalLink.getBinding().getBinding(cablePath);//И здесь
    tableData[1][curCCI++] =
      Integer.toString(binding.x) + ":" + Integer.toString(binding.y);
  
    tableData[0][curCCI] = LangModelMap.getString("geographicCoords");
    tableData[1][curCCI++] =
      Double.toString(marker.getLocation().getX()) + ":" +
      Double.toString(marker.getLocation().getY());
	}

  private String getSiteFullName(Map map,Identifier id)
  {
    SiteNode siteNode = map.getSiteNode(id);
		if (siteNode == null)
			return null;

		SiteNodeType nodeType = (SiteNodeType) siteNode.getType();
		return nodeType.getName() + " " + siteNode.getName();
  }

	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if (length % this.getDivisionsNumber() == 0)
			return  (int)(length / this.getDivisionsNumber()); //+заголовок
		//а если нельзя, то добавляем ещё ряд
		return (int)(length / this.getDivisionsNumber()) + 1;
	}

	public Object getValueAt(int row, int col)
	{
		int index = (this.getRowCount()) *
      (int) (col / this.getBaseColumnCount()) + row - 1;
      
		if (index >= this.length)
			return "";

		return this.tableData[col % this.getBaseColumnCount()][index];
	}
}
