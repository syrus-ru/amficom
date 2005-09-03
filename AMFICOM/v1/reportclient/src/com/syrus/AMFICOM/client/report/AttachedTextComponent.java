/*
 * $Id: AttachedTextComponent.java,v 1.3 2005/09/03 12:42:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.syrus.AMFICOM.client.UI.MultiRowString;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.resource.IntDimension;

public class AttachedTextComponent extends JTextPane implements RenderingComponent{
	private static final long serialVersionUID = 8382110834808763027L;
	
	public static Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.BLACK,1);
	public static Font DEFAULT_FONT = new Font("Times New Roman",Font.BOLD,20);	
	protected final AttachedTextStorableElement textRenderingElement;
	
	/**
	 * Это чтобы во время редактирования не приходилось набирать у самой рамки
	 */
	private static final String SYMBOLS_FOR_ADDITONAL_WIDTH = "XX";
	/**
	 * А это "довесок" - потому что он хреново определяет ширину таких символов, как ";"
	 */
	private static final String SYMBOLS_FOR_ADDITONAL_WIDTH1 = "x";	
	/**
	 * Точка клика мыши на надписи
	 */
	private Point mousePressedLocation = new Point();

	public AttachedTextComponent(AttachedTextStorableElement atre) {
		this.textRenderingElement = atre;
		//Выставляется шрифт по умолчанию
		this.setFont(DEFAULT_FONT);
	}

	@Override
	public void setFont(Font newFont){
		super.setFont(newFont);
		if (this.textRenderingElement == null)
			return;
		
		this.textRenderingElement.setFont(newFont);
		this.textRenderingElement.setModified(System.currentTimeMillis());		
	}
	
	public StorableElement getElement() {
		return this.textRenderingElement;
	}

	/**
	 * Чтобы надпись всегда можно было разглядеть на экране
	 * ей выставляется минимальная величина.
	 */
	public void checkComponentWidth()
	{
		IntDimension minimumSize = AttachedTextStorableElement.MINIMUM_COMPONENT_SIZE;
		this.setSize(
			this.getWidth() > minimumSize.getWidth() ? this.getWidth() : minimumSize.getWidth(),
			this.getHeight() > minimumSize.getHeight() ? this.getHeight() : minimumSize.getHeight());
	}

	/**
	 * Расчитывает габариты текста введённого в компонент для текущего шрифта
	 * @return Dimension с габаритами
	 */
	public Dimension getTextSize() {
		String text = this.textRenderingElement.getText() + SYMBOLS_FOR_ADDITONAL_WIDTH1;
		FontMetrics fontMetrics = this.getGraphics().getFontMetrics();

		MultiRowString multiRowString = new MultiRowString(text);
		int maxsize = SwingUtilities.computeStringWidth(fontMetrics, multiRowString.get(0));
		for(int i = 1; i < multiRowString.getRowCount(); i++)
			maxsize = Math.max(
					maxsize, 
					SwingUtilities.computeStringWidth(fontMetrics, multiRowString.get(i)));
		
		Dimension result = new Dimension(
				maxsize,
				fontMetrics.getHeight() * multiRowString.getRowCount());
		
		return result;
	}

	/**
	 * Расчитывает значение дополнительной ширины для компонента, которое
	 * добавляется справа при вводе текста - для текущего шрифта.
	 * @return int значение ширины
	 */
	public Dimension getAdditionalSize() {
		//TODO По хорошему, это значение следует фиксировать для данного шрифта 
		Graphics componentGraphics = this.getGraphics();
		Rectangle2D textBounds = componentGraphics.getFontMetrics().getStringBounds(
				SYMBOLS_FOR_ADDITONAL_WIDTH,
				componentGraphics);
		
		return new Dimension((int)textBounds.getWidth(),(int)textBounds.getHeight());
	}
	
	public Point getMousePressedLocation() {
		return this.mousePressedLocation;
	}

	public void setMousePressedLocation(Point mousePressedLocation) {
		this.mousePressedLocation = mousePressedLocation;
	}

	public void setX(int x) {
		this.setLocation(x,this.getY());
	}

	public void setY(int y) {
		this.setLocation(this.getX(),y);
	}

	public void setWidth(int width) {
		this.setSize(width,this.getHeight());
	}

	public void setHeight(int height) {
		this.setSize(this.getWidth(),height);
	}
}
