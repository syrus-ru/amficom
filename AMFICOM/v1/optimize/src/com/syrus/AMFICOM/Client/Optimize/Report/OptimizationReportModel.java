package com.syrus.AMFICOM.Client.Optimize.Report;

import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.APOReportModel;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;

import com.syrus.AMFICOM.Client.General.Report.*;

import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Optimize.SolutionCompact;

import com.syrus.AMFICOM.Client.Schematics.Report.SchemeReportModel;
import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

import javax.swing.JComponent;

import java.util.Vector;

public class OptimizationReportModel extends APOReportModel
{
	public String getName() {return "optimizationreportmodel";}

  //Отчёты по данным с сервера
	public static String sourceData = "label_sourceData";
	public static String equipFeatures = "label_equipFeatures";
	public static String optimizeResults = "label_optimizeResults";
	public static String cost = "label_cost";
  
  //Отчёты по данным из окон
  
  // окно графика хода оптимизации  
  public static String iterationsHistory = "frameIterationsHistoryTitle";
  // окно задания параметров оптимизации  
  public static String optimizationParams = "frameOptimizationParamsTitle";
  // окно подробной нитки маршрута одного из решений  
  public static String solution = "frameSolutionTitle";
  // окно задания режимов узлов ( fixed , active )  
  public static String nodesOptimizeProperties = "frameNodesOptimizePropertiesTitle";

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repOptimizationResults");
	}

	public String getReportsName(ObjectsReport rp)
	{
		if (rp.field.equals(SchemeReportModel.scheme))
			 return (new SchemeReportModel().getReportsName(rp));
		 else if (rp.field.equals(MapReportModel.rep_topology))
			 return (new MapReportModel().getReportsName(rp));

		String return_value = this.getObjectsName() + ":" + getLangForField(rp.field);
		if (rp.reserveName != null)
			return_value += rp.reserveName;

		return return_value;
	}

	public String getLangForField(String field)
	{
		if (field.equals(SchemeReportModel.scheme))
			return LangModelSchematics.getString(field);
		else if (field.equals(MapReportModel.rep_topology))
			return LangModelMap.getString(field);

		return LangModelOptimize.getString(field);
	}

	public String getReportsReserveName(ObjectsReport rp)
			throws CreateReportException
	{
		String reserve_str = (String)rp.getReserve();
		int separatPosit = reserve_str.indexOf(':');
		if (separatPosit == -1)
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ,reserve_str);
			return ":" + scheme.name;
		}
		else
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ,reserve_str.substring(0,separatPosit));
			String secondPart = "";
			if (rp.field.equals(MapReportModel.rep_topology))
				secondPart = ((MapContext)Pool.get(MapContext.typ,reserve_str.substring(separatPosit+1))).name;
			else
				secondPart = ((SolutionCompact)Pool.get(SolutionCompact.typ,reserve_str.substring(separatPosit+1))).name;
			return ":" + scheme.name + ":" + secondPart;
		}
	}

	public OptimizationReportModel()
	{
	}

	public Vector getAvailableReports()
	{
		Vector result = new Vector();

		result.add(OptimizationReportModel.sourceData);
		result.add(SchemeReportModel.scheme);
		result.add(MapReportModel.rep_topology);
		result.add(OptimizationReportModel.equipFeatures);
		result.add(OptimizationReportModel.optimizeResults);
		result.add(OptimizationReportModel.cost);

		result.add(OptimizationReportModel.iterationsHistory);
		result.add(OptimizationReportModel.optimizationParams);
		result.add(OptimizationReportModel.solution);
		result.add(OptimizationReportModel.nodesOptimizeProperties);

		return result;
	}

	public void loadRequiredObjects(
				DataSourceInterface dsi,
				ObjectsReport rp,
				ReportTemplate rt)
	{
		if (rp.field.equals(OptimizationReportModel.sourceData))
		{
			String curValue = (String) rt.resourcesLoaded.get("schemeOptInfoLoaded");
			if (curValue.equals("false"))
			{
				dsi.LoadSchemeOptimizeInfo();
				rt.resourcesLoaded.put("schemeOptInfoLoaded","true");
			}

			curValue = (String) rt.resourcesLoaded.get("schemeMonSolutLoaded");
			if (curValue.equals("false"))
			{
				dsi.LoadSchemeMonitoringSolutions();
				rt.resourcesLoaded.put("schemeMonSolutLoaded","true");
			}
		}
		else if (rp.field.equals(SchemeReportModel.scheme))
		{
			new SchemeReportModel().loadRequiredObjects(dsi,rp,rt);
		}
		else if (rp.field.equals(MapReportModel.rep_topology))
		{
			new MapReportModel().loadRequiredObjects(dsi,rp,rt);
		}
		else if (rp.field.equals(OptimizationReportModel.equipFeatures))
		{
			String curValue = (String) rt.resourcesLoaded.get("schemeOptInfoLoaded");
			if (curValue.equals("false"))
			{
				dsi.LoadSchemeOptimizeInfo();
				rt.resourcesLoaded.put("schemeOptInfoLoaded","true");
			}

			curValue = (String) rt.resourcesLoaded.get("netDirectoryLoaded");
			if (curValue.equals("false"))
			{
				new SchemeDataSourceImage(dsi).LoadNetDirectory();
				rt.resourcesLoaded.put("netDirectoryLoaded","true");
			}

			curValue = (String) rt.resourcesLoaded.get("schemeMonSolutLoaded");
			if (curValue.equals("false"))
			{
				dsi.LoadSchemeMonitoringSolutions();
				rt.resourcesLoaded.put("schemeMonSolutLoaded","true");
			}
		}
		else if (rp.field.equals(OptimizationReportModel.optimizeResults))
		{
			String curValue = (String) rt.resourcesLoaded.get("schemeMonSolutLoaded");
			if (curValue.equals("false"))
			{
				dsi.LoadSchemeMonitoringSolutions();
				rt.resourcesLoaded.put("schemeMonSolutLoaded","true");
			}

			curValue = (String) rt.resourcesLoaded.get("schemesLoaded");
			if (curValue.equals("false"))
			{
				new SchemeDataSourceImage(dsi).LoadNetDirectory();
				rt.resourcesLoaded.put("schemesLoaded","true");
			}

			curValue = (String) rt.resourcesLoaded.get("netDirectoryLoaded");
			if (curValue.equals("false"))
			{
				new SchemeDataSourceImage(dsi).LoadNetDirectory();
				rt.resourcesLoaded.put("netDirectoryLoaded","true");
			}
		}
		else if (rp.field.equals(OptimizationReportModel.cost))
		{
			String curValue = (String) rt.resourcesLoaded.get("schemeOptInfoLoaded");
			if (curValue.equals("false"))
			{
				dsi.LoadSchemeOptimizeInfo();
				rt.resourcesLoaded.put("schemeOptInfoLoaded","true");
			}

			curValue = (String) rt.resourcesLoaded.get("schemeMonSolutLoaded");
			if (curValue.equals("false"))
			{
				dsi.LoadSchemeMonitoringSolutions();
				rt.resourcesLoaded.put("schemeMonSolutLoaded","true");
			}

			curValue = (String) rt.resourcesLoaded.get("netDirectoryLoaded");
			if (curValue.equals("false"))
			{
				new SchemeDataSourceImage(dsi).LoadNetDirectory();
				rt.resourcesLoaded.put("netDirectoryLoaded","true");
			}
		}
	}

	public int getReportKind(ObjectsReport rp)
	{
		if (rp.field.equals(SchemeReportModel.scheme))
			return -1;
		else if (   rp.field.equals(MapReportModel.rep_topology)
              ||rp.field.equals(OptimizationReportModel.iterationsHistory))
			return 0;

		return 1;
	}

	public JComponent createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule)

			throws CreateReportException
	{
		JComponent returnValue = null;

		if (rp.field.equals(OptimizationReportModel.sourceData))
		{
			OptimizationSourceData osTable =
					new OptimizationSourceData(rp, divisionsNumber);
			returnValue = new ReportResultsTablePanel(
					osTable.columnModel,
					osTable.tableModel,
					rt.findROforReport(rp));
		}

		else if (rp.field.equals(OptimizationReportModel.cost))
		{
			EquipmentCost ecTable = new EquipmentCost(rp, divisionsNumber);
			returnValue = new ReportResultsTablePanel(
					ecTable.columnModel,
					ecTable.tableModel,
					rt.findROforReport(rp));
		}

		else if (rp.field.equals(OptimizationReportModel.optimizeResults))
		{
			OptimizationResults ecTable = new OptimizationResults(
					rp,
					divisionsNumber);
			returnValue = new ReportResultsTablePanel(
					ecTable.columnModel,
					ecTable.tableModel,
					rt.findROforReport(rp));
		}

		else if (rp.field.equals(SchemeReportModel.scheme))
		{
			returnValue = new SchemeReportModel().createReport(
				rp,
				divisionsNumber,
				rt,
				aContext,
				fromAnotherModule);
		}

		else if (rp.field.equals(MapReportModel.rep_topology))
		{
			returnValue =  new MapReportModel().createReport(
				rp,
				divisionsNumber,
				rt,
				aContext,
				fromAnotherModule);
		}

		else if (rp.field.equals(OptimizationReportModel.equipFeatures))
		{
			OptEquipmentFeatures osTable =
					new OptEquipmentFeatures(rp, divisionsNumber);
			returnValue = new ReportResultsTablePanel(
					osTable.columnModel,
					osTable.tableModel,
					rt.findROforReport(rp));
		}

		else if (rp.field.equals(OptimizationReportModel.optimizationParams)
           ||rp.field.equals(OptimizationReportModel.solution))
		{
      TableModelDivider tmd = new TableModelDivider(rp,divisionsNumber);
			returnValue = new ReportResultsTablePanel(
					tmd.columnModel,
					tmd.tableModel,
					rt.findROforReport(rp));
		}

		else if (rp.field.equals(OptimizationReportModel.nodesOptimizeProperties))
		{
      ObjectResourceDivList ordl = new ObjectResourceDivList(rp,divisionsNumber);
			returnValue = new ReportResultsTablePanel(
					ordl.columnModel,
					ordl.tableModel,
					rt.findROforReport(rp));
		}

		else if (rp.field.equals(OptimizationReportModel.iterationsHistory))
		{
      returnValue = new IterHistoryPanelRenderer(rt.findROforReport(rp));
		}

		return returnValue;
	}

	public void setData(ReportTemplate rt,AMTReport data)
	{
    super.setData(rt,data);
	};
}