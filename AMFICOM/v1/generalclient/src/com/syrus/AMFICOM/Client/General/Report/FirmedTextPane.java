package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.JTextPane;

import java.util.Vector;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.font.FontRenderContext;
import java.awt.Font;

import com.syrus.AMFICOM.CORBA.Report.FirmedTextPane_Transferable;

/**
 * <p>Title: </p>
 * <p>Description: ������� � ������������ ��������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public class FirmedTextPane extends JTextPane
{
	/**
	 * �������� �� ��������� � �������� ���� ���� �������
	 */
	public static String toFieldsTop = "priv_templ_top";
	/**
	 * �������� �� ��������� � �������� ���� �������� �������
	 */
	public static String toTop = "priv_obj_top";
	/**
	 * �������� �� ��������� � ������� ���� �������� �������
	 */
	public static String toBottom = "priv_obj_bottom";
	/**
	 * �������� �� ����������� � ������ ���� ���� �������
	 */
	public static String toFieldsLeft = "priv_templ_left";
	/**
	 * �������� �� ����������� � ������ ���� �������� �������
	 */
	public static String toLeft = "priv_obj_left";
	/**
	 * �������� �� ����������� � ������� ���� �������� �������
	 */
	public static String toRight = "priv_obj_right";
	/**
	 * ������� �������, � �������� ������������ �������� �� ���������
	 */
	public RenderingObject vertFirmer = null;
	/**
	 * ������� �������, � �������� ������������ �������� �� �����������
	 */
	public RenderingObject horizFirmer = null;
	/**
	 * ����� �������
	 */
	public Font labelsFont = null;
	/**
	 * ��� �������� �� ���������
	 */
	public String verticalFirmTo = this.toFieldsTop;
	/**
	 * ��� �������� �� �����������
	 */
	public String horizontalFirmTo = this.toFieldsLeft;
	/**
	 * ������� ����� x ������� � x �������, � �������� ��� ���������.
	 */
	public int distanceX = 0;
	/**
	 * ������� ����� y ������� � y �������, � �������� ��� ���������.
	 */
	public int distanceY = 0;

	public FirmedTextPane()
	{
		super();
	}

	/**
	 * ��������������� ������� �� ������ � �������
	 * @param ftp_trans ������ � �������, ���������� �� �������
	 * @param ros ������ ���� ���������, ������������� �������
	 */
	public FirmedTextPane(FirmedTextPane_Transferable ftp_trans,
		Vector ros)
	{
		this();
		this.setLocation(ftp_trans.labelX, ftp_trans.labelY);
		this.setText(ftp_trans.strings_contained);

		int first_Symb = ftp_trans.labels_font.indexOf("name=") + 5;
		int last_Symb = ftp_trans.labels_font.indexOf(',', first_Symb);
		String fontName = ftp_trans.labels_font.substring(first_Symb, last_Symb);

		int fontStyle = 0;
		first_Symb = ftp_trans.labels_font.indexOf("plain");
		if (first_Symb > 0)
			first_Symb = Font.PLAIN;
		else
		{
			first_Symb = ftp_trans.labels_font.indexOf("bold");
			if (first_Symb > 0)
				fontStyle += Font.BOLD;

			first_Symb = ftp_trans.labels_font.indexOf("italic");
			if (first_Symb > 0)
				fontStyle += Font.ITALIC;
		}

		first_Symb = ftp_trans.labels_font.indexOf("size=") + 5;
		last_Symb = ftp_trans.labels_font.indexOf(']', first_Symb);
		int fontSize = Integer.parseInt(
			ftp_trans.labels_font.substring(first_Symb, last_Symb));

		try
		{
			this.setFont(new Font(fontName, fontStyle, fontSize));
		}
		catch (Exception ex)
		{}

		Dimension itsSize = this.getContentBounds();
		this.setSize(itsSize.width, itsSize.height);

		this.distanceX = ftp_trans.distanceX;
		this.distanceY = ftp_trans.distanceY;
		this.verticalFirmTo = ftp_trans.verticalFirmTo;
		this.horizontalFirmTo = ftp_trans.horizontalFirmTo;

		if ((!ftp_trans.horizFirmer.equals("")) ||
			(!ftp_trans.vertFirmer.equals("")))
			for (int i = 0; i < ros.size(); i++)
			{
				RenderingObject curRO = (RenderingObject) ros.get(i);
				if (ftp_trans.horizFirmer.equals(curRO.id))
					this.horizFirmer = curRO;
				if (ftp_trans.vertFirmer.equals(curRO.id))
					this.vertFirmer = curRO;
			}
	}

	/**
	 * ����� �������� �� �����������
	 * @param hRenderer ������, � �������� �������������� ��������
	 * @param horizFirmTo ��� ��������
	 */
	public void setHorizFirmedTo(RenderingObject hRenderer,
		String horizFirmTo)
	{
		this.horizFirmer = hRenderer;
		this.horizontalFirmTo = horizFirmTo;
	}

	/**
	 * ����� �������� �� ���������
	 * @param vRenderer ������, � �������� �������������� ��������
	 * @param vertFirmTo ��� ��������
	 */
	public void setVertFirmedTo(RenderingObject vRenderer,
		String vertFirmTo)
	{
		this.vertFirmer = vRenderer;
		this.verticalFirmTo = vertFirmTo;
	}

	/**
	 * ����� ����� �������� distanceX,distanceY.
	 * ������������ ��� ����������� ����������� ������� ������������
	 * �������, � �������� ��� ���������.
	 */
	public void setFirmingDistance()
	{
		if (horizFirmer == null)
			distanceX = this.getX();
		if (vertFirmer == null)
			distanceY = this.getY();

		int width = 0;
		int height = 0;

		if (vertFirmer != null)
		{
			if (vertFirmer.rendererPanel == null)
				height = vertFirmer.height;
			else
				height = vertFirmer.rendererPanel.getHeight();

			if (verticalFirmTo.equals(this.toFieldsTop))
				distanceY = this.getY();
			if (verticalFirmTo.equals(this.toTop))
				distanceY = this.getY() - vertFirmer.y;
			if (verticalFirmTo.equals(this.toBottom))
				distanceY = this.getY() - vertFirmer.y - height;
		}

		if (horizFirmer != null)
		{
			if (horizFirmer.rendererPanel == null)
				width = horizFirmer.width;
			else
				width = horizFirmer.rendererPanel.getWidth();

			if (horizontalFirmTo.equals(this.toFieldsLeft))
				distanceX = this.getX();
			if (horizontalFirmTo.equals(this.toLeft))
				distanceX = this.getX() - horizFirmer.x;
			if (horizontalFirmTo.equals(this.toRight))
				distanceX = this.getX() - horizFirmer.x - width;
		}
	}

	/**
	 * ������������ ��� ����������� �������, � �������� ���������
	 * �������. �������� ���������� ������� ���, ����� ����������
	 * ����� �������� � �������� �������� ����������.
	 * @param imagableRect ����
	 */
	public void refreshCoords(Rectangle imagableRect)
	{
		int newX = 0;
		int newY = 0;

		if (horizFirmer == null)
			newX = this.getX();
		if (vertFirmer == null)
			newY = this.getY();

		if (vertFirmer != null)
		{
			int height = 0;
			int vertFirmerY = 0;
			if (vertFirmer.rendererPanel == null)
			{
				height = vertFirmer.height;
				vertFirmerY = vertFirmer.y;
			}
			else
			{
				height = vertFirmer.rendererPanel.getHeight();
				vertFirmerY = vertFirmer.rendererPanel.getY();
			}

			if (verticalFirmTo.equals(this.toTop))
				newY = vertFirmerY + distanceY;

			if (verticalFirmTo.equals(this.toBottom))
				newY = vertFirmerY + height + distanceY;

			if (imagableRect != null)
			{
				if (newY < imagableRect.y)
					newY = imagableRect.y;

				int tpheight = (int) this.getPreferredSize().getWidth();
				if (newY + tpheight > imagableRect.y + imagableRect.height)
					newY = imagableRect.y + imagableRect.height - tpheight;
			}
		}

		if (horizFirmer != null)
		{
			int width = 0;
			int horizFirmerX = 0;

			if (horizFirmer.rendererPanel == null)
			{
				width = horizFirmer.width;
				horizFirmerX = horizFirmer.x;
			}
			else
			{
				width = horizFirmer.rendererPanel.getWidth();
				horizFirmerX = horizFirmer.rendererPanel.getX();
			}

			if (horizontalFirmTo.equals(this.toLeft))
				newX = horizFirmerX + distanceX;

			if (horizontalFirmTo.equals(this.toRight))
				newX = horizFirmerX + width + distanceX;

			if (imagableRect != null)
			{
				if (newX < imagableRect.x)
					newX = imagableRect.x;

				int tpwidth = (int) this.getPreferredSize().getWidth();

				if (newX + tpwidth > imagableRect.x + imagableRect.width)
					newX = imagableRect.x + imagableRect.width - tpwidth;
			}
		}

		this.setLocation(newX, newY);
	}

	/**
	 * ���������: ����������� �� ������ ����� �������.
	 * @param x ���������� ����� �� x
	 * @param y ���������� ����� �� y
	 * @return true, ���� ����� ����������� �������
	 */
	public boolean hasPoint(int x, int y)
	{
		if ((this.getX() <= x) && (x <= this.getX() + this.getWidth()) &&
			(this.getY() <= y) && (y <= this.getY() + this.getHeight()))
			return true;
		return false;
	}

	/**
	 * @return ���������� �������� ������, ������������ ��������
	 */
	public Dimension getContentBounds()
	{
		String strToConsider = this.getText();
		if (strToConsider.equals(""))
			strToConsider = " ";

		int stringCount = 0;

		int maxStringLength = 0;
		int mSLstartIndex = 0;

		int stringStart = 0;
		for (int i = 0; i < strToConsider.length(); i++)
			if ((strToConsider.charAt(i) == '\n') ||
				(i == strToConsider.length() - 1))
			{
				int curStringLength = i - stringStart;
				if (i == strToConsider.length() - 1)
					curStringLength++;

				if (curStringLength > maxStringLength)
				{
					maxStringLength = curStringLength;
					mSLstartIndex = stringStart;
				}
				stringCount++;
				stringStart = i + 1;
			}

		Dimension result = new Dimension();

		result.height =
			(this.getFont().getStringBounds(
			strToConsider,
			new FontRenderContext(null, true, true))).
			getBounds().height * stringCount + 3;

		result.width =
			(this.getFont().getStringBounds(
			strToConsider.substring(mSLstartIndex,
			mSLstartIndex + maxStringLength),
			new FontRenderContext(null, true, true))).
			getBounds().width + 3;

		return result;
	}

	/**
	 * ������������� ��� ������� �������� �����
	 * @param newFont �����
	 */
	public void setFont(Font newFont)
	{
		super.setFont(newFont);
		this.labelsFont = newFont;
	}
}
