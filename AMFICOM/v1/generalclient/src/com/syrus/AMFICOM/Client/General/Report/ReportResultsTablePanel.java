package com.syrus.AMFICOM.Client.General.Report;

import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.table.JTableHeader;

import oracle.jdeveloper.layout.XYLayout;
import oracle.jdeveloper.layout.XYConstraints;

import com.syrus.AMFICOM.Client.General.UI.ATable;

/**
 * <p>Title: </p>
 * <p>Description: Испольуется для реализации всех таблиц -
 * элементов шаблона</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportResultsTablePanel extends JScrollPane
{
	private DividableTableColumnModel columnModel = null;
	private DividableTableModel tableModel = null;

	private RenderingObject ro = null;

	private ATable statTable = null;
	private XYLayout xYLayout1 = new XYLayout();

	public ReportResultsTablePanel(
			DividableTableColumnModel columnModel,
			DividableTableModel tableModel,
			RenderingObject ro)
			throws CreateReportException
	{
		try
		{
			this.columnModel = columnModel;
			this.tableModel = tableModel;
			this.ro = ro;

			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CreateReportException("",CreateReportException.generalError);
		}
	}
	private void jbInit() throws Exception
	{
		if (columnModel != null)
			statTable = new ATable(tableModel,columnModel);
		else
			statTable = new ATable(tableModel);

		statTable.updateHeaderSize();

		this.setPreferredSize(getTableSize());

		this.setVisible(true);
		this.getViewport().add(statTable, null);
	}

	public Dimension getTableSize()
	{
		int height = this.statTable.getRowHeight() * (this.statTable.getRowCount() + 1);
		if (this.statTable.getRowCount() < 4)
			height -= 5;

		return new Dimension (
			ro.width,
			height);
	}

}
