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
 * <p>Description: Надпись с возможностью привязки</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class FirmedTextPane extends JTextPane
{
	/**
	 * Привязка по вертикали к верхнему краю поля шаблона
	 */
	public static String toFieldsTop = "priv_templ_top";
	/**
	 * Привязка по вертикали к верхнему краю элемента шаблона
	 */
	public static String toTop = "priv_obj_top";
	/**
	 * Привязка по вертикали к нижнему краю элемента шаблона
	 */
	public static String toBottom = "priv_obj_bottom";
	/**
	 * Привязка по горизонтали к левому краю поля шаблона
	 */
	public static String toFieldsLeft = "priv_templ_left";
	/**
	 * Привязка по горизонтали к левому краю элемента шаблона
	 */
	public static String toLeft = "priv_obj_left";
	/**
	 * Привязка по горизонтали к правому краю элемента шаблона
	 */
	public static String toRight = "priv_obj_right";
	/**
	 * Элемент шаблона, к которому осуществлена привязка по вертикали
	 */
	public RenderingObject vertFirmer = null;
	/**
	 * Элемент шаблона, к которому осуществлена привязка по горизонтали
	 */
	public RenderingObject horizFirmer = null;
	/**
	 * Шрифт надписи
	 */
	public Font labelsFont = null;
	/**
	 * Тип привязки по вертикали
	 */
	public String verticalFirmTo = this.toFieldsTop;
	/**
	 * Тип привязки по горизонтали
	 */
	public String horizontalFirmTo = this.toFieldsLeft;
	/**
	 * Разница между x надписи и x объекта, к которому она привязана.
	 */
	public int distanceX = 0;
	/**
	 * Разница между y надписи и y объекта, к которому она привязана.
	 */
	public int distanceY = 0;

	public FirmedTextPane()
	{
		super();
	}

	/**
	 * Восстанавливает надпись по данным с сервера
	 * @param ftp_trans данные о надписи, сохранённые на сервере
	 * @param ros список всех элементов, принадлежащих шаблону
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
	 * Задаёт привязку по горизонтали
	 * @param hRenderer объект, к которому осуществляется привязка
	 * @param horizFirmTo тип привязки
	 */
	public void setHorizFirmedTo(RenderingObject hRenderer,
		String horizFirmTo)
	{
		this.horizFirmer = hRenderer;
		this.horizontalFirmTo = horizFirmTo;
	}

	/**
	 * Задаёт привязку по вертикали
	 * @param vRenderer объект, к которому осуществляется привязка
	 * @param vertFirmTo тип привязки
	 */
	public void setVertFirmedTo(RenderingObject vRenderer,
		String vertFirmTo)
	{
		this.vertFirmer = vRenderer;
		this.verticalFirmTo = vertFirmTo;
	}

	/**
	 * Задаёт новые значения distanceX,distanceY.
	 * Используется при перемещении привязанной надписи относительно
	 * объекта, к которому она привязана.
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
	 * Используется при перемещении объекта, к которому привязана
	 * надпись. Изменяет координаты надписи так, чтобы расстояние
	 * между надписью и объектом осталось неизменным.
	 * @param imagableRect поля
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
	 * Проверяет: принадлежит ли данная точка надписи.
	 * @param x координата точки по x
	 * @param y координата точки по y
	 * @return true, если точка принадлежит надписи
	 */
	public boolean hasPoint(int x, int y)
	{
		if ((this.getX() <= x) && (x <= this.getX() + this.getWidth()) &&
			(this.getY() <= y) && (y <= this.getY() + this.getHeight()))
			return true;
		return false;
	}

	/**
	 * @return Возвращает габариты строки, отображаемой надписью
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
	 * Устанавливает для надписи заданный шрифт
	 * @param newFont шрифт
	 */
	public void setFont(Font newFont)
	{
		super.setFont(newFont);
		this.labelsFont = newFont;
	}
}
