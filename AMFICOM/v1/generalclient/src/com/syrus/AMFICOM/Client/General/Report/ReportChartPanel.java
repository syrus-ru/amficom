package com.syrus.AMFICOM.Client.General.Report;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.IOException;

/**
 * <p>Title: </p>
 * <p>Description: Панель для отображения графиков
 * с добавленной функцией сохранения в файл</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportChartPanel extends ChartPanel
{
	JFreeChart itsChart = null;
	public ReportChartPanel(JFreeChart chart)
	{
		super(chart);
		this.itsChart = chart;
	}

	public void doSave(String filename) throws IOException
	{
		ChartUtilities.saveChartAsPNG(new File(filename), this.itsChart, getWidth(), getHeight());
	}
}
