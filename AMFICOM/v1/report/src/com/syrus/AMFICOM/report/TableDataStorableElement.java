/*
 * $Id: TableDataStorableElement.java,v 1.2 2005/09/01 14:21:02 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * ����� ��� ����������� ������ � ��������� ����
 * @author $Author: peskovsky $
 * @version $Revision: 1.2 $, $Date: 2005/09/01 14:21:02 $
 * @module report_v1
 */
public final class TableDataStorableElement extends DataStorableElement implements Serializable {
	private static final long serialVersionUID = -2699698026579054587L;

	private static int DEFAULT_COLUMN_WIDTH = 100;
	
	private int[] columnWidths = null;
	
	/**
	 * ����� ������������ ��������� ������� (����������� ��� ������� � �����
	 * ������)
	 */
	private int verticalDivisionsCount = 1;
	
	public TableDataStorableElement (
			String reportName,
			String modelClassName,
			IntPoint location,
			int[] columnWidths)
	{
		super(reportName,modelClassName);
		this.columnWidths = columnWidths;
		
		this.setLocation(location);
		this.setSize(this.getTotalColumnWidth(),-1);
	}

	public TableDataStorableElement (
			String reportName,
			String modelClassName,
			IntPoint location,
			int columnCount)
	{
		super(reportName,modelClassName);		
		this.columnWidths = new int[columnCount];
		Arrays.fill(this.columnWidths,TableDataStorableElement.DEFAULT_COLUMN_WIDTH);
		
		this.setLocation(location);
		this.setSize(this.getTotalColumnWidth(),-1);
	}
	
	public int getTotalColumnWidth()
	{
		int totalColumnWidth = 0;
		for (int i = 0; i < this.columnWidths.length; i++)
			totalColumnWidth += this.columnWidths[i];
		
		return totalColumnWidth;
	}
	
	public void writeObject(java.io.ObjectOutputStream out) throws IOException {
		//��� ������������ �������������� ������ �� �������������.
		//�� ��������� �������, � �� ������.
		out.writeObject(this.getSize());
		out.writeObject(this.getLocation());		

		out.writeObject(this.getId());		
		out.writeObject(this.getReportName());		
		out.writeObject(this.getModelClassName());

		//TODO ��������� ��������� �� ����� ���������������
		//������ �����.
		out.writeObject(this.columnWidths);
	}

	public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.setSize((IntDimension)in.readObject());
		this.setLocation((IntPoint)in.readObject());

		this.id = (Identifier)in.readObject();
		this.reportName = (String)in.readObject();
		this.modelClassName = (String)in.readObject();

		//TODO ��������� ��������� �� ����� ���������������
		//������ �����.
		
		this.columnWidths = (int[])in.readObject();		
	}

	public int[] getColumnWidths() {
		return this.columnWidths;
	}

	public void setColumnWidth(int columnIndex, int columnWidth) {
		this.columnWidths[columnIndex] = columnWidth;
	}

	public int getVerticalDivisionsCount() {
		return this.verticalDivisionsCount;
	}

	public void setVerticalDivisionsCount(int verticalDivisionsCount) {
		this.verticalDivisionsCount = verticalDivisionsCount;
	}
}
