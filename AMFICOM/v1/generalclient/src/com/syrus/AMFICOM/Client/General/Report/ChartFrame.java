/* ======================================
 * JFreeChart : a free Java chart library
 * ======================================
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * --------------------
 * Pie3DChartDemo1.java
 * --------------------
 * (C) Copyright 2002, 2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: ChartFrame.java,v 1.6 2004/08/18 08:05:19 peskovsky Exp $
 *
 * Changes
 * -------
 * 19-Jun-2002 : Version 1 (DG);
 * 31-Jul-2002 : Updated with changes to Pie3DPlot class (DG);
 * 11-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 *
 * 02-Feb-2004 : Modernized for some specific needs
 */

package com.syrus.AMFICOM.Client.General.Report;

import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Pie3DPlot;
import org.jfree.data.DefaultPieDataset;
import org.jfree.data.PieDataset;
import org.jfree.util.Rotation;

import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.BarRenderer;

import org.jfree.chart.Spacer;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.StandardXYItemRenderer;
import org.jfree.chart.renderer.XYItemRenderer;
import org.jfree.data.XYDataset;

import org.jfree.data.time.Hour;
import org.jfree.data.time.Day;
import org.jfree.data.time.Week;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Year;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.JInternalFrame;
import javax.swing.Timer;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.text.SimpleDateFormat;

import java.util.Date;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import org.jfree.chart.plot.PiePlot;

import com.syrus.AMFICOM.Client.General.Report.Statistics;
import com.syrus.AMFICOM.Client.General.Report.ObjectResourceReportModel;

public class ChartFrame extends JInternalFrame
{
	public ReportChartPanel chartPanel = null;

	private RenderingObject renderingObject = null;

	private Statistics stat = null;

	private Statistics[] stats = null;

	private int[] objectsNumberAtInterval = null;

	private List periodsBounds = null;

	private Class interval_size = null;

	private JFreeChart chart = null;

	public ChartFrame(Statistics stat, RenderingObject ro)
	{
		this.stat = stat;
		renderingObject = ro;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
		}
	}

	public ChartFrame(Statistics[] stats, RenderingObject ro)
	{
		this.stats = stats;
		renderingObject = ro;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
		}
	}

	public ChartFrame(int[] onai, List periodsBounds, Class interval_size, RenderingObject ro)
	{
		renderingObject = ro;
		this.objectsNumberAtInterval = onai;
		this.periodsBounds = periodsBounds;
		this.interval_size = interval_size;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
		}
	}

	private PieDataset createPieDataset()
	{
		if (stat == null)
			return null;

		DefaultPieDataset result = new DefaultPieDataset();

		for (int i = 0; i < stat.fieldValues.size(); i++)
		{
			String fieldValue = (String) stat.fieldValues.get(i);
			Integer intTimesFound = (Integer) stat.timesFound.get(i);
			Double timesFound = Double.valueOf(intTimesFound.toString());

			result.setValue(fieldValue, timesFound);
		}

		return result;
	}

	/**
	 * Creates a sample chart.
	 *
	 * @param dataset  the dataset.
	 *
	 * @return A chart.
*/
	private JFreeChart createPieChart(PieDataset dataset)
	{
		if (dataset == null)
			return null;

		JFreeChart chart = ChartFactory.createPieChart3D(
			Statistics.field, // chart title
			dataset, // data
			true, // include legend
			true,
			false
			);

		// set the background color for the chart...
		chart.setBackgroundPaint(new Color(220, 220, 220));
		Pie3DPlot plot = (Pie3DPlot) chart.getPlot();
		plot.setStartAngle(270);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage(LangModelReport.getString("error_noDataToDisplay"));
//      plot.setExplodePercent();
		Rotator rotator = new Rotator(plot, this);
		rotator.start();

		return chart;
	}

	private JFreeChart createPie2DChart(CategoryDataset dataset)
	{
		if (dataset == null)
			return null;

		// create the chart...

		JFreeChart chart = ChartFactory.createPieChart(
			Statistics.field, // chart title
			dataset, // data
			PiePlot.PER_COLUMN,
			true, // include legend
			true, // tooltips?
			false // URLs?
			);

		chart.setBackgroundPaint(new Color(220, 220, 220));
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionLabelType(PiePlot.VALUE_AND_PERCENT_LABELS);

		return chart;
	}

	private CategoryDataset createBarDataset()
	{
		if (stats == null)
			return null;

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < stats.length; i++)
		{
			for (int j = 0; j < stats[i].fieldValues.size(); j++)
				dataset.addValue(
					((Integer) stats[i].timesFound.get(j)).doubleValue(),
					(String) stats[i].fieldValues.get(j),
					Statistics.field);
		}

		return dataset;
	}

	private JFreeChart createBarChart(CategoryDataset dataset)
	{
		if (dataset == null)
			return null;

		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart3D(
			Statistics.field, // chart title
			"", //LangModelReport.String("label_field"), // domain axis label
			LangModelReport.getString("label_value"), // range axis label
			dataset, // data
			PlotOrientation.VERTICAL,
			true, // include legend
			false, // tooltips?
			false // URLs?
			);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(new Color(220, 220, 220));
		// get a reference to the plot for further customisation...
		CategoryPlot plot = chart.getCategoryPlot();

		// skip some labels if they overlap...
		CategoryAxis domainAxis = plot.getDomainAxis();
//		domainAxis.setSkipCategoryLabelsToFit(true);

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	private JFreeChart createBar2DChart(CategoryDataset dataset)
	{
		if (dataset == null)
			return null;

		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart(
			Statistics.field, // chart title
			"", //LangModelReport.String("label_field"), // domain axis label
			LangModelReport.getString("label_number"), // range axis label
			dataset, // data
			PlotOrientation.VERTICAL,
			true, // include legend
			false, // tooltips?
			false // URLs?
			);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(new Color(220, 220, 220));

		// get a reference to the plot for further customisation...
		CategoryPlot plot = chart.getCategoryPlot();

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		int seriesCount = dataset.getRowKeys().size();
		for (int i = 0; i < seriesCount; i++)
		{
			Color col = (Color) renderer.getSeriesPaint(i);

			GradientPaint gp = new GradientPaint(
				0.0f, 0.0f, col,
				0.0f, 0.0f, Color.lightGray
				);

			renderer.setSeriesPaint(i, gp);
		}
		CategoryAxis domainAxis = plot.getDomainAxis();
		/*    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
			domainAxis.setMaxCategoryLabelWidthRatio(5.0f);*/
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	private TimeSeriesCollection createXYDataset()
	{
		if (this.objectsNumberAtInterval == null)
			return null;

		TimeSeries s1 = new TimeSeries(
			LangModelReport.getString("label_number"), this.interval_size);

		for (int i = 0; i < this.objectsNumberAtInterval.length; i++)
		{
			if (this.interval_size.equals(Hour.class))
			{
				Long[] periodBounds = (Long[])this.periodsBounds.get(i);

				Date d = new Date(periodBounds[0].longValue());
				Hour period = new Hour(d);
				s1.add(period, this.objectsNumberAtInterval[i]);
				continue;
			}

			if (this.interval_size.equals(Day.class))
			{
				Long[] periodBounds = (Long[])this.periodsBounds.get(i);
				Date d = new Date(periodBounds[0].longValue());
				Day period = new Day(d);
				s1.add(period, this.objectsNumberAtInterval[i]);
				continue;
			}

			if (this.interval_size.equals(Week.class))
			{
				Long[] periodBounds = (Long[])this.periodsBounds.get(i);
				Date d = new Date(periodBounds[0].longValue());
				Week period = new Week(d);
				s1.add(period, this.objectsNumberAtInterval[i]);
				continue;
			}

			if (this.interval_size.equals(Month.class))
			{
				Long[] periodBounds = (Long[])this.periodsBounds.get(i);
				Date d = new Date(periodBounds[0].longValue());
				Month period = new Month(d);
				s1.add(period, this.objectsNumberAtInterval[i]);
				continue;
			}

			if (this.interval_size.equals(Quarter.class))
			{
				Long[] periodBounds = (Long[])this.periodsBounds.get(i);
				Date d = new Date(periodBounds[0].longValue());
				Quarter period = new Quarter(d);
				s1.add(period, this.objectsNumberAtInterval[i]);
				continue;
			}

			if (this.interval_size.equals(Year.class))
			{
				Long[] periodBounds = (Long[])this.periodsBounds.get(i);
				Date d = new Date(periodBounds[0].longValue());
				Year period = new Year(d);
				s1.add(period, this.objectsNumberAtInterval[i]);
				continue;
			}

		}

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(s1);

		dataset.setDomainIsPointsInTime(true);

		return dataset;
	}

	private JFreeChart createTimeSeriesChart(XYDataset dataset)
	{

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
			LangModelReport.getString("label_numberForPeriod"),
			LangModelReport.getString("label_period"),
			LangModelReport.getString("label_number"),
			dataset,
			true,
			true,
			false
			);

		chart.setBackgroundPaint(new Color(220, 220, 220));

		StandardLegend sl = (StandardLegend) chart.getLegend();
		sl.setDisplaySeriesShapes(true);

		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));

		XYItemRenderer renderer = plot.getRenderer();
		if (renderer instanceof StandardXYItemRenderer)
		{
			StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
			rr.setPlotShapes(true);
			rr.setShapesFilled(true);
		}

		DateAxis axis = (DateAxis) plot.getDomainAxis();

		if (this.interval_size.equals(Hour.class))
			axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy hh '÷.'"));
		if (this.interval_size.equals(Day.class))
			axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy"));
		if (this.interval_size.equals(Week.class))
			axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy"));
		if (this.interval_size.equals(Month.class))
			axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		if (this.interval_size.equals(Quarter.class))
			axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		if (this.interval_size.equals(Year.class))
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
		return chart;

	}

	private JFreeChart createXYChart(TimeSeriesCollection dataset)
	{

		JFreeChart chart = ChartFactory.createXYBarChart(
			LangModelReport.getString("label_numberForPeriod"),
			LangModelReport.getString("label_period"),
			LangModelReport.getString("label_number"),
			dataset,
			PlotOrientation.VERTICAL,
			true,
			true,
			false
			);

		chart.setBackgroundPaint(new Color(220, 220, 220));

		StandardLegend sl = (StandardLegend) chart.getLegend();
		sl.setDisplaySeriesShapes(true);

		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));

		XYItemRenderer renderer = plot.getRenderer();
		if (renderer instanceof StandardXYItemRenderer)
		{
			StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
			rr.setPlotShapes(true);
			rr.setShapesFilled(true);
		}

		DateAxis axis = (DateAxis) plot.getDomainAxis();

		if (this.interval_size.equals(Hour.class))
			axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy hh '÷.'"));
		if (this.interval_size.equals(Day.class))
			axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy"));
		if (this.interval_size.equals(Week.class))
			axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy"));
		if (this.interval_size.equals(Month.class))
			axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		if (this.interval_size.equals(Quarter.class))
			axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		if (this.interval_size.equals(Year.class))
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
		return chart;
	}

	private void jbInit() throws Exception
	{
//		this.setPreferredSize(new Dimension(renderingObject.width, renderingObject.height));
		this.setClosable(true);
		this.setResizable(true);
		this.setVisible(true);
	}

	public void setChart(String type)
	{
		if (type.equals(ObjectResourceReportModel.rt_pieChart))
		{
			PieDataset dataset = createPieDataset();
			chart = createPieChart(dataset);
		}

		if (type.equals(ObjectResourceReportModel.rt_pie2DChart))
		{
			CategoryDataset dataset = createBarDataset();
			chart = createPie2DChart(dataset);
		}

		if (type.equals(ObjectResourceReportModel.rt_barChart))
		{
			CategoryDataset dataset = createBarDataset();
			chart = createBarChart(dataset);
		}

		if (type.equals(ObjectResourceReportModel.rt_bar2DChart))
		{
			CategoryDataset dataset = createBarDataset();
			chart = createBar2DChart(dataset);
		}

		if (type.equals(ObjectResourceReportModel.rt_timefunction))
		{
			TimeSeriesCollection dataset = createXYDataset();
			chart = createTimeSeriesChart(dataset);
		}

		if (type.equals(ObjectResourceReportModel.rt_gistogram))
		{
			TimeSeriesCollection dataset = createXYDataset();
			chart = createXYChart(dataset);
		}

		chartPanel = new ReportChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(renderingObject.width, renderingObject.height));
		this.getContentPane().add(chartPanel, BorderLayout.CENTER);
	}
}

class Rotator extends Timer implements ActionListener
{

	/** The plot. */
	private Pie3DPlot plot;

	private JInternalFrame ownerWindow = null;

	/** The angle. */
	private int angle = 270;

	Rotator(Pie3DPlot plot, JInternalFrame ownerWindow)
	{
		super(6000, null);
		this.plot = plot;
		this.ownerWindow = ownerWindow;
		addActionListener(this);
	}

	/**
	 * Modifies the starting angle.
	 *
	 * @param event  the action event.
*/
	public void actionPerformed(ActionEvent event)
	{
		this.plot.setStartAngle(angle);
		this.angle = this.angle + 30;
		if (this.angle == 360)
		{
			this.angle = 0;
		}
		ownerWindow.repaint();
	}

}
