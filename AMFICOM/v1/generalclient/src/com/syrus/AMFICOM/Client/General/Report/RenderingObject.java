package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.JScrollPane;
import java.awt.font.FontRenderContext;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.util.Vector;
import java.util.Enumeration;

import com.syrus.AMFICOM.CORBA.Report.RenderingObject_Transferable;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
/**
 * <p>Title: </p>
 * <p>Description: ������� �������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public class RenderingObject
{
	public String id = "";
	/**
	 * ���������� x �� �����
	 */
	public int x = 0;
	/**
	 * ���������� y �� �����
	 */
	public int y = 0;
	/**
	 * ������ �� �����
	 */
	public int width = 200;
	/**
	 * ������ �� �����
	 */
	public int height = 100;
	/**
	 * ����������� ���������� �������� �������
	 */
	public TitledPanel rendererPanel = null;
	/**
	 * ������������ �����
	 */
	private ObjectsReport reportToRender = null;
	/**
	 * ������ � �������� �������� (��� �������)
	 */
	private Vector columnWidths = null;
	/**
	 * ���������� ������������ ��������� (��� �������)
	 */
	private int tableDivisionsNumber = 1;

	/**
	 * ������ ������� ������� �� �������� ������� � �������,
	 * �������������� �� ���������. ����� ������������� ���������
	 * ����������.
	 * @param x ���������� �� x ������ �������� ���� ��������
	 * @param y ���������� �� y ������ �������� ���� ��������
	 */
	public RenderingObject(int x, int y, Dimension size)
	{
		this.x = x;
		this.y = y;
		this.width = size.width;
		this.height = size.height;
	}
	/**
	 * ��������������� ������� ������� �� ������ � �������.
	 * @param ro_trans ������ �� ��������, ���������� �� �������
	 */
	public RenderingObject(RenderingObject_Transferable ro_trans)
	{
		this.id = ro_trans.roID;

		this.x = ro_trans.objectX;
		this.y = ro_trans.objectY;
		this.width = ro_trans.width;
		this.height = ro_trans.height;

		this.tableDivisionsNumber = ro_trans.tableDivisionsNumber;
		this.setColumnWidths(ro_trans.column_widths);

		ObjectsReport loadedReport = null;
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(ro_trans.incapsulated_report);
			ObjectInputStream ois = new ObjectInputStream(bais);
			loadedReport = (ObjectsReport) ois.readObject();
			bais.close();
		}
		catch(Exception ex)
		{
			System.out.println("Error reading report for rendering object " + this.id);
		}

		this.setReportToRender(loadedReport);
	}

	/**
	 * @return ���������� ������� �������� ������� �� �����
	 */
	public Rectangle getBounds()
	{
		return new Rectangle(this.x,this.y,this.width,this.height);
	}

	/**
	 * ������������ ������� ������� �� �����. �������� ������,
	 * ������������� � ����� ��������, ������� �� ��� �����.
	 * � ������, ���� ������ �������� ��� ������ ���, ��� ���� ��
	 * ������ �������� ������ �� ����������, ������ ���������� ��
	 * ���������� ����������.
	 * @param g ����������� ��������
	 */
	public void paint(Graphics g)
	{
		if (g == null)
			return;

		if (this.height < 50)
			this.height = 50;

		if (reportToRender != null)
		{
			int divPosit = reportToRender.getName().indexOf(
					':',
					reportToRender.getName().indexOf(':') + 1);
			if (divPosit == -1)
				divPosit = reportToRender.getName().indexOf(':');

			String stringToPrint1 = reportToRender.getName().substring(0,divPosit);
			String stringToPrint2 = reportToRender.getName().substring(
					divPosit + 1,
					reportToRender.getName().length());

			Rectangle labelBounds1 = g.getFontMetrics().getStringBounds(
				stringToPrint1,g).getBounds();
			Rectangle labelBounds2 = g.getFontMetrics().getStringBounds(
				stringToPrint2,g).getBounds();

			int maxWidth = (int)(labelBounds1.getWidth() > labelBounds2.getWidth() ?
													 labelBounds1.getWidth() : labelBounds2.getWidth());

			if (maxWidth > this.width)
				this.width = maxWidth;

			g.setColor(Color.black);
			g.drawRect(this.x,this.y,this.width,this.height);

			g.drawString(stringToPrint1,
									 (int) (this.x + this.width / 2 - labelBounds1.getWidth() / 2) + 3,
									 (int) (this.y + this.height / 3 + labelBounds1.getHeight() / 2));

			g.drawString(stringToPrint2,
									 (int) (this.x + this.width / 2 - labelBounds2.getWidth() / 2) + 3,
									 (int) (this.y + this.height * 2/3 + labelBounds2.getHeight() / 2));
		}
		else
		{
			String stringToPrint = LangModelReport.String("label_ro");

			Rectangle labelBounds = g.getFont().getStringBounds(stringToPrint,
					new FontRenderContext(null,true,true)).getBounds();

			if (labelBounds.getWidth() > this.width)
				this.width = (int) labelBounds.getWidth();

			g.setColor(Color.black);
			g.drawRect(this.x,this.y,this.width,this.height);

			g.drawString(stringToPrint,
									 (int) (this.x + this.width / 2 - labelBounds.getWidth() / 2),
									 (int) (this.y + this.height / 2 + labelBounds.getHeight() / 2));
		}
	}

	/**
	 * ���������� ���������� ������� ����� �������� ������� � ������ columnWidths.
	 * ���������� ��� �������� �� ����������� ���������� ������� � ��� �����.
	 * ��� ����������� ������ ����������� ���������� ������ ������������� ��
	 * ��������� �� ���������� �������.
	 */
	public void saveColumnWidths()
	{
		this.columnWidths = null;

		if ((this.rendererPanel == null) ||
				!(this.rendererPanel.getComponent(0) instanceof ReportResultsTablePanel))
			return;

		JScrollPane sp = (JScrollPane) this.rendererPanel.getComponent(0);

		this.columnWidths = new Vector();
		JTable itsTable = (JTable)sp.getViewport().getView();
		int colNumber = itsTable.getColumnModel().getColumnCount();
		for (int i = 0; i < colNumber / this.getTableDivisionsNumber(); i ++)
		{
			float startTableWidth = itsTable.getColumnModel().getTotalColumnWidth();
			float currTableWidth = sp.getWidth();
			int columnWidth = (int)(itsTable.getColumnModel().getColumn(i).getWidth() *
										currTableWidth / startTableWidth);

			this.columnWidths.add(new Integer(columnWidth));
		}
	}

	/**
	 * ���������� �������������� ����� �������� ������� �� ���������
	 * �� ������� columnWidths.
	 */
	public void setColumnWidths()
	{
		if (this.columnWidths == null)
			return;

		if (this.rendererPanel.getComponentCount() == 0)
			return;

		JScrollPane sp = (JScrollPane) this.rendererPanel.getComponent(0);
		if (sp == null)
			return;

		JTable itsTable = (JTable)sp.getViewport().getView();

		int colNumber = itsTable.getColumnModel().getColumnCount();
		for (int i = 0; i < colNumber; i++)
		{
			int savedIndex = i % this.columnWidths.size();
			TableColumn tc = itsTable.getColumnModel().getColumn(i);
			tc.setWidth(((Integer)this.columnWidths.get(savedIndex)).intValue());
			tc.setPreferredWidth(((Integer)this.columnWidths.get(savedIndex)).intValue());
		}
	}

	/**
	 * ����� �������� ����� ��������. ������������ ��� ��������������
	 * ������� �� ������ � �������.
	 * @param widths ����� �������� ����� �������� �������
	 */
	public void setColumnWidths(int[] widths)
	{
		if (widths[0] == -1)
		{
			this.columnWidths = null;
			return;
		}
		this.columnWidths = new Vector();
		for (int i = 0; i < widths.length; i++)
			this.columnWidths.add(new Integer(widths[i]));
	}

	/**
	 * ������������ ��� ���������� ������� �� ������.
	 * @return ���������� �������� ����� �������� �������.
	 */
	public int[] getColumnWidths()
	{
		if (this.columnWidths == null)
		{
			int [] result = new int[1];
			result[0] = -1;
			return result;
		}
		int[] result = new int[this.columnWidths.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = ((Integer) this.columnWidths.get(i)).intValue();
		return result;
	}

	/**
	 * ����������� �������� ����� � ��������.
	 * @param rp �����
	 */
	public void setReportToRender(ObjectsReport rp)
	{
		this.reportToRender = rp;
		columnWidths = null;
		tableDivisionsNumber = 1;
	}

	/**
	 * @return ���������� �����, ������������ � ��������.
	 */
	public ObjectsReport getReportToRender()
	{
		return reportToRender;
	}

	/**
	 * @return ���������� ������ �������
	 */
	public int getTotalTableWidth()
	{
		if (this.columnWidths != null)
		{
			int result = 0;
			for (int i = 0; i < this.columnWidths.size(); i++)
				result += ((Integer) this.columnWidths.get(i)).intValue();
			return result;
		}

		if (this.rendererPanel != null)
			return this.rendererPanel.getWidth();

		return this.width;
	}

	/**
	 * @param labels ������, ���������� ��� ������� �������.
	 * @return ���������� ������� ��������������, � �������
	 * ����������� ����������� ����������� �������� ������� �
	 * ����������� � ���� �������.
	 */
	public Rectangle getObjectWithLabelsBounds(Vector labels)
	{
		int x1 = this.x;
		int y1 = this.y;
		int x2 = this.x + this.width;
		int y2 = this.y + this.height;

		if (labels != null)
			for (int i = 0; i < labels.size(); i++)
			{
				FirmedTextPane curLabel = (FirmedTextPane) labels.get(i);
				if (!((curLabel.vertFirmer != null) &&
							curLabel.vertFirmer.equals(this) ||
							(curLabel.horizFirmer != null) &&
							curLabel.horizFirmer.equals(this)))
					continue;

//        curLabel.refreshCoords();
				if (curLabel.getX() < x1)
					x1 = curLabel.getX();

				if (curLabel.getX() + curLabel.getWidth() > x2)
					x2 = curLabel.getX() + curLabel.getWidth();

				if (curLabel.getY() < y1)
					y1 = curLabel.getY();

				if (curLabel.getY() + curLabel.getHeight() > y2)
					y2 = curLabel.getY() + curLabel.getHeight();
			}

		return new Rectangle(x1,y1,x2 - x1,y2 - y1);
	}

	/**
	 * ���������: ����������� �� �������� (�������� ������� � ������������ ���������)
	 * ������ �����.
	 * @param x ���������� ����� �� x
	 * @param y ���������� ����� �� y
	 * @param labels ������, ���������� ��� ������� �������.
	 * @return true ���� �����, ����������� ��������
	 */
	public boolean hasPoint (int x, int y, Vector labels)
	{
		Rectangle r = null;
		if (labels != null)
			r = this.getObjectWithLabelsBounds(labels);
		else
		{
			if (rendererPanel != null)
				r = new Rectangle (this.rendererPanel.getX(),
													 this.rendererPanel.getY(),
													 (int)this.rendererPanel.getPreferredSize().getWidth(),
													 (int)this.rendererPanel.getPreferredSize().getHeight());
			else
				r = new Rectangle (this.x,this.y,this.width,this.height);
		}

		if ((r.x < x) && (x < r.x + r.width) &&
				(r.y < y) && (y < r.y + r.height))
			return true;
		return false;
	}

	/**
	 * @return ���������� ���������� ������������ ��������� �������
	 */
	public int getTableDivisionsNumber()
	{
		return tableDivisionsNumber;
	}

	/**
	 * ������������� ���������� ������������ ��������� (��� �������)
	 * @param newValue ����� �������� ���������� ���������
	 */
	public void setTableDivisionsNumber(int newValue)
	{
		tableDivisionsNumber = newValue;
	}
}