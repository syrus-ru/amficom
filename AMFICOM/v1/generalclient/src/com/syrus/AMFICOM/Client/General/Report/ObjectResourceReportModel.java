package com.syrus.AMFICOM.Client.General.Report;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.util.GregorianCalendar;

import java.text.SimpleDateFormat;
import java.text.ParsePosition;

import org.jfree.data.time.Hour;
import org.jfree.data.time.Day;
import org.jfree.data.time.Week;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Year;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
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

	abstract public Hashtable getAvailableViews();

	private List allColumnNames = null;

	private List allColumnIDs = null;

	private List allColumnSizes = null;

	private Hashtable availableViews = null;

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
		List result = new LinkedList();

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
		for (int i = 0; i < allColumnNames.size(); i++)
			if (((String) allColumnNames.get(i)).equals(name))
				return (String) allColumnIDs.get(i);

		return null;
	}

	public final List getAvailableViewTypesforField(String ID)
	{
		if (ID == null)
			return new LinkedList();

		List result = (List) availableViews.get(ID);
		if (result == null)
			return new LinkedList();

		return result;
	}

	private static final List getReports(List reports)
	{
		List result = new LinkedList();
		for (int i = 0; i < reports.size(); i++)
			result.add((ObjectsReport) reports.get(i));
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

    List reportObjects = new LinkedList();
		if (or.model instanceof ObjectResourceReportModel)
		{
			ObjectResourceReportModel orrm = (ObjectResourceReportModel) or.model;
			Hashtable hash = Pool.getHash(orrm.getObjectsType());
			if (hash == null)
				return new LinkedList();

			ObjectResourceFilter filter = this.findORFilterforModel(rt,dsi);

			Enumeration allObjects = hash.elements();
			while (allObjects.hasMoreElements())
			{
				ObjectResource curObject = (ObjectResource) allObjects.nextElement();
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

		List reportObjects = getReportObjects(rt, rp,aContext.getDataSourceInterface());

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

			List periodsBounds = new LinkedList();

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

		for (int i = 1; i < objects.size(); i++)
		{
			ObjectResource curObject = (ObjectResource) objects.get(i);

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

		for (int k = 0; k < repObjects.size(); k++)
		{
			ObjectResource or = (ObjectResource) repObjects.get(k);
			String curFieldValue = or.getModel().getColumnValue(rp.field);

			SimpleDateFormat formatter
				= new SimpleDateFormat("dd.MM.yy hh:mm:ss");
			ParsePosition pos = new ParsePosition(0);

			Date tempDate = formatter.parse(curFieldValue, pos);
			long time = tempDate.getTime();

			for (int m = 0; m < allBounds.size(); m++)
			{
				Long[] curBounds = (Long[]) allBounds.get(m);
				if ((curBounds[0].longValue() <= time) &&
					(time <= curBounds[1].longValue()))
				{
					objectsNumberAtInterval[m]++;
					break;
				}
			}
		}

		return objectsNumberAtInterval;
	}
}
