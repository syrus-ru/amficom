//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Configure\Application\TextIcon.java                    * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

public class TextIcon extends Object implements Icon
{
	String text;
	Component parent;
	int height;
	int width;
	AffineTransform at = new AffineTransform(0, -1, 0, 1, 0, 0);
	boolean visible = true;

	public TextIcon(String s, Component c)
	{
		text = s;
		parent = c;

		FontMetrics fm = c.getFontMetrics(c.getFont());
		height = fm.stringWidth(s);
		width = fm.getHeight();
	}

	public TextIcon(String s, Component c, boolean visible)
	{
		this(s, c);
		this.visible = visible;
	}

	public int getIconHeight()
	{
		return height + 20;
	}

	public int getIconWidth()
	{
		return width - 10;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D )g;

		AffineTransform t = g2.getTransform();

		FontMetrics fm = g2.getFontMetrics(g2.getFont());
		g2.translate(0, height + 10);
		g2.rotate (Math.toRadians(270), x - 5, y);

		if(visible)
			g2.setColor(Color.black);
		else
			g2.setColor(Color.gray);

		g2.drawString(text, x - 5, y + fm.getAscent());

		g2.setTransform(t);
	}
}
