/*
 * $Id: ObjectResourceReportModel.java,v 1.11 2004/09/27 09:14:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.*;
import java.text.*;
import java.util.*;
import javax.swing.JComponent;
import org.jfree.data.time.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2004/09/27 09:14:49 $
 * @module generalclient_v1
 */
abstract public class ObjectResourceReportModel extends ReportModel
{
  public static List reportObjects = null;

	public static final String rt_statistics = "rep_stat_po_polju";

	public static final String rt_timefunction = "rep_graphic";

	public static final String rt_gistogram = "rep_gistogram";

	public static final String rt_pieChart = "rep_diagr_pirog";

	public static final String rt_pie2DChart = "rep_diagr_pirog2D";

	public static final String rt_barChart = "rep_stolb_diagr";

	public static final String rt_bar2DChart = "rep_stolb_diagr2D";

	public static final String rt_objectsReport = "rep_spis_obj";

	public static final String rt_objProperies = "rep_obj_properties";

	public String getName()
	{
		return "objectresourcereportmodel";
	}

	abstract public String getObjectsType();

	abstract public String getObjectsName();

	abstract public ObjectResourceFilter findORFilterforModel(
		  ReportTemplate rt,
		  DataSourceInterface dsi);

	abstract public List getAllObjectColumnIDs();

	abstract public List getAllObjectColumnNames();

	abstract public List getAllObjectColumnSizes();

	abstract public Map getAvailableViews();

	private List allColumnNames = null;

	private List allColumnIDs = null;

	private List allColumnSizes = null;

	private Map availableViews = null;

	public ObjectResourceReportModel()
	{
		allColumnIDs = getAllObjectColumnIDs();
		allColumnNames = getAllObjectColumnNames();
		allColumnSizes = getAllObjectColumnSizes();

		availableViews = getAvailableViews();
	}

	public final List getAllColumnIDs()
	{
		return allColumnIDs;
	}

	public final List getColumnNamesbyIDs(List IDs)
	{
		List result = new ArrayList();

		for (int j = 0; j < IDs.size(); j++)
			result.add(this.getColumnNamebyID((String) IDs.get(j)));

		return result;
	}

	public final String getColumnNamebyID(String ID)
	{
		for (int i = 0; i < allColumnIDs.size(); i++)
			if (allColumnIDs.get(i).equals(ID))
				return (String) allColumnNames.get(i);

		return null;
	}

	public final int getColumnSizebyID(String ID)
	{
		for (int i = 0; i < allColumnIDs.size(); i++)
			if (allColumnIDs.get(i).equals(ID))
				return ((Integer) allColumnSizes.get(i)).intValue();

		return 0;
	}

	public final String getColumnIDbyName(String name)
	{
    int index = 0;
		for (ListIterator lIt = allColumnNames.listIterator(); lIt.hasNext(); index++)
			if (((String) lIt.next()).equals(name))
				return (String) allColumnIDs.get(index);

		return null;
	}

	public final List getAvailableViewTypesforField(String ID)
	{
		if (ID == null)
			return new ArrayList();

		List result = (List) availableViews.get(ID);
		if (result == null)
			return new ArrayList();

		return result;
	}

	private static final List getReports(List reports)
	{
		List result = new ArrayList();
		for (ListIterator lIt = reports.listIterator(); lIt.hasNext();)
			result.add(lIt.next());
      
		return result;
	}

	public final String getReportsReserveName(ObjectsReport rp) throws
		CreateReportException
	{
		String reserve_str = (String) rp.getReserve();
		int separatPosit = reserve_str.indexOf(':');
		String type = reserve_str.substring(0, separatPosit);
		String id = reserve_str.substring(separatPosit + 1);

		if (type.equals("equipment"))
			type = "kisequipment";

		ObjectResource obj = (ObjectResource) Pool.get(type, id);
		if (obj != null)
			return ":" + obj.getName();
    else
			throw new CreateReportException("",
				CreateReportException.poolObjNotExists);
	}

	private final List getReportObjects(
		ReportTemplate rt,
		ObjectsReport or,
		DataSourceInterface dsi)
	{
    if (ObjectResourceReportModel.reportObjects != null)
      return ObjectResourceReportModel.reportObjects;

    List reportObjects = new ArrayList();
		if (or.model instanceof ObjectResourceReportModel)
		{
			ObjectResourceReportModel orrm = (ObjectResourceReportModel) or.model;
			Map hash = Pool.getMap(orrm.getObjectsType());
			if (hash == null)
				return new ArrayList();

			ObjectResourceFilter filter = this.findORFilterforModel(rt,dsi);

			for (Iterator it = hash.values().iterator(); it.hasNext();)
			{
				ObjectResource curObject = (ObjectResource) it.next();
				if (filter == null)
					reportObjects.add(curObject);
				else if (filter.logicScheme.passesAllConstraints(curObject))
					reportObjects.add(curObject);
			}
		}

		return reportObjects;
	}

	public int getReportKind(ObjectsReport rp)
	{
		if (  rp.view_type.equals(ObjectResourceReportModel.rt_statistics)
			|| rp.view_type.equals(ObjectResourceReportModel.rt_objProperies)
			|| rp.view_type.equals(ObjectResourceReportModel.rt_objectsReport))
			return 1;

		return 0;
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

		List reportObjects = getReportObjects(rt, rp,aContext.getDataSource());

		ObjectResourceReportModel model = (ObjectResourceReportModel) rp.model;
		if (rp.view_type.equals(ObjectResourceReportModel.rt_statistics))
		{
			Statistics stat = new Statistics(divisionsNumber,
				reportObjects,
				rp.field,
				model);
			returnValue = new ReportResultsTablePanel(stat.columnModel,
				stat.tableModel,
				rt.findROforReport(rp));
		}

		else if (rp.view_type.equals(ObjectResourceReportModel.rt_pieChart))
		{
			Statistics stat = new Statistics(divisionsNumber,
				reportObjects,
				rp.field,
				model);

			ChartFrame chartFrame = new ChartFrame(stat,rt.findROforReport(rp));
			chartFrame.setChart(rp.view_type);

			returnValue = chartFrame.chartPanel;
		}

		else if (rp.view_type.equals(ObjectResourceReportModel.rt_barChart) ||
			rp.view_type.equals(ObjectResourceReportModel.rt_bar2DChart) ||
			rp.view_type.equals(ObjectResourceReportModel.rt_pie2DChart))
		{
			Statistics stat = new Statistics(divisionsNumber,
				reportObjects,
				rp.field,
				model);
			Statistics[] stats = new Statistics[1];
			stats[0] = stat;

			ChartFrame chartFrame = new ChartFrame(stats,rt.findROforReport(rp));
			chartFrame.setChart(rp.view_type);

			returnValue = chartFrame.chartPanel;
		}

		else if (rp.view_type.equals(ObjectResourceReportModel.rt_objectsReport))
		{
			ObjectList list = new ObjectList(divisionsNumber, reportObjects, rp);
			returnValue = new ReportResultsTablePanel(
				list.columnModel,
				list.tableModel,
				rt.findROforReport(rp));
		}

		else if (rp.view_type.equals(ObjectResourceReportModel.rt_timefunction) ||
			rp.view_type.equals(ObjectResourceReportModel.rt_gistogram))
		{
			Long[] minDate = new Long[1];
			Long[] maxDate = new Long[1];

			List periodsBounds = new ArrayList();

			getMinMaxDates(minDate, maxDate, reportObjects, rp, model);

			int periodsNumber = getPeriods(
				minDate[0],
				maxDate[0],
				(Class) rp.getReserve(),
				periodsBounds);

			if (periodsNumber == 0)
				return null;

			int[] objectsNumberAtIntervals = getObjectsNumberAtIntervals(
				reportObjects,
				rp,
				periodsBounds,
				model);

			ChartFrame chartFrame = new ChartFrame(
				objectsNumberAtIntervals,
				periodsBounds,
				(Class) rp.getReserve(),
				rt.findROforReport(rp));
			chartFrame.setChart(rp.view_type);

			returnValue = chartFrame.chartPanel;
		}

		return returnValue;
	}

	private static void getMinMaxDates(
		Long[] minDate,
		Long[] maxDate,
		List objects,
		ObjectsReport rp,
		ObjectResourceReportModel curReportModel)
	{
		if (objects == null)
			return;
		if (objects.size() == 0)
			return;

		minDate[0] = new Long(Long.MAX_VALUE);
		maxDate[0] = new Long(Long.MIN_VALUE);

//		for (int i = 1; i < objects.size(); i++)
    for (ListIterator lIt = objects.listIterator(); lIt.hasNext();)
		{
			ObjectResource curObject = (ObjectResource) lIt.next();

			String objectsDate = curObject.getModel().getColumnValue(rp.field);

			SimpleDateFormat formatter
				= new SimpleDateFormat("dd.MM.yy hh:mm:ss");
			ParsePosition pos = new ParsePosition(0);

			Date tempDate = formatter.parse(objectsDate, pos);
			Long curValue = new Long(tempDate.getTime());

			if (minDate[0].longValue() > curValue.longValue())
				minDate[0] = curValue;
			if (maxDate[0].longValue() < curValue.longValue())
				maxDate[0] = curValue;
		}
	}

	private static int getPeriods(Long rMinDate, Long rMaxDate,
		Class periodClass,
		List bounds)
	{
		Date minTime = new Date(rMinDate.longValue()); //Это надо взять из фильтров
		Date maxTime = new Date(rMaxDate.longValue());

		if (periodClass.equals(Hour.class))
			return iterate(bounds, minTime, maxTime, 0, 0, 0, 1);

		if (periodClass.equals(Day.class))
			return iterate(bounds, minTime, maxTime, 0, 0, 1, 0);

		if (periodClass.equals(Week.class))
			return iterate(bounds, minTime, maxTime, 0, 0, 7, 0);

		if (periodClass.equals(Month.class))
			return iterate(bounds, minTime, maxTime, 0, 1, 0, 0);

		if (periodClass.equals(Quarter.class))
			return iterate(bounds, minTime, maxTime, 0, 4, 0, 0);

		if (periodClass.equals(Year.class))
			return iterate(bounds, minTime, maxTime, 1, 0, 0, 0);

		return 0;
	}

	private static int iterate(List bounds, Date minTime, Date maxTime,
		int yIncr, int moIncr, int daIncr, int hIncr)
	{
		GregorianCalendar maxTimeCalendar = new GregorianCalendar();
		maxTimeCalendar.setTime(maxTime);

		GregorianCalendar curTimeCalendar = new GregorianCalendar();
		curTimeCalendar.setTime(minTime);

		int periodsNumber = 0;

		while (curTimeCalendar.before(maxTimeCalendar))
		{
			Long[] minmaxTimes = new Long[2];
			minmaxTimes[0] = new Long(curTimeCalendar.getTime().getTime());
			curTimeCalendar.add(GregorianCalendar.YEAR, yIncr);
			curTimeCalendar.add(GregorianCalendar.MONTH, moIncr);
			curTimeCalendar.add(GregorianCalendar.DATE, daIncr);
			curTimeCalendar.add(GregorianCalendar.HOUR, hIncr);

			minmaxTimes[1] = new Long(curTimeCalendar.getTime().getTime());
			bounds.add(minmaxTimes);
			periodsNumber++;
		}

		return periodsNumber;
	}

	private static int[] getObjectsNumberAtIntervals(
		List repObjects,
		ObjectsReport rp,
		List allBounds,
		ObjectResourceReportModel curReportModel)
	{
		int[] objectsNumberAtInterval = new int[allBounds.size()];
		//Инициализируем массив векторов для размещения отсортированных объектов
		for (int k = 0; k < objectsNumberAtInterval.length; k++)
			objectsNumberAtInterval[k] = 0;

		for (ListIterator lIt = repObjects.listIterator(); lIt.hasNext();)
		{
			ObjectResource or = (ObjectResource) lIt.next();
			String curFieldValue = or.getModel().getColumnValue(rp.field);

			SimpleDateFormat formatter
				= new SimpleDateFormat("dd.MM.yy hh:mm:ss");
			ParsePosition pos = new ParsePosition(0);

			Date tempDate = formatter.parse(curFieldValue, pos);
			long time = tempDate.getTime();

      int curIndex = 0;
      for (ListIterator bIt = allBounds.listIterator(); bIt.hasNext(); curIndex++)
			{
				Long[] curBounds = (Long[]) bIt.next();
				if ((curBounds[0].longValue() <= time) &&
					(time <= curBounds[1].longValue()))
				{
					objectsNumberAtInterval[curIndex]++;
					break;
				}
			}
		}

		return objectsNumberAtInterval;
	}
}
