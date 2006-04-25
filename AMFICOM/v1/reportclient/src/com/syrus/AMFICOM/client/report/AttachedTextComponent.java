/*
 * $Id: AttachedTextComponent.java,v 1.10 2006/04/25 11:01:43 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.StorableElement;

public class AttachedTextComponent extends JTextPane implements RenderingComponent{
	private static final long serialVersionUID = 8382110834808763027L;
		
	protected final AttachedTextStorableElement textRenderingElement;
	
	/**
	 * Это чтобы во время редактирования не приходилось набирать у самой рамки
	 */
	private static final String SYMBOLS_FOR_ADDITONAL_WIDTH = "XX";
	/**
	 * А это "довесок" - потому что он хреново определяет ширину таких символов, как ";"
	 */
	private static final String SYMBOLS_FOR_ADDITONAL_WIDTH1 = "X";

	public static final Dimension MINIMUM_COMPONENT_SIZE = new Dimension(90, 30);	
	/**
	 * Точка клика мыши на надписи
	 */
	private Point mousePressedLocation = new Point();
	
	private PropertyChangeListener atPropertyChangeListener = null;

	public AttachedTextComponent(AttachedTextStorableElement atre) {
		this.textRenderingElement = atre;
		//Выставляется шрифт по умолчанию
		this.setFont(atre.getFont());
		this.getCaret().setBlinkRate(500);
	}
	
	public StorableElement getElement() {
		return this.textRenderingElement;
	}

	/**
	 * Чтобы надпись всегда можно было разглядеть на экране
	 * ей выставляется минимальная величина.
	 */
	public void checkComponentWidth() {
		this.setSize(
			this.getWidth() > MINIMUM_COMPONENT_SIZE.width
				? this.getWidth() : MINIMUM_COMPONENT_SIZE.width,
			this.getHeight() > MINIMUM_COMPONENT_SIZE.height
				? this.getHeight() : MINIMUM_COMPONENT_SIZE.height);
	}

	/**
	 * Расчитывает габариты текста введённого в компонент для текущего шрифта
	 * @return Dimension с габаритами
	 */
	public Dimension getTextSize() {
		//Написан свой вариант расчёта размера текста, поскольку
		//стандартный StringTokenizer глючит при работе с пустыми строками.
		String text = this.textRenderingElement.getText();
		FontMetrics fontMetrics = this.getGraphics().getFontMetrics();
		
		int lineEndsFound = 0;
		int maxLineSize = 0;
		
		int currentPosition = -1;
		int lastPosition = -1;		

		while (currentPosition < text.length()) {
			currentPosition = text.indexOf('\n',lastPosition + 1);
			
			String subString = text.substring(
					lastPosition > 0 ? lastPosition : 0,
					currentPosition > -1 ? currentPosition : text.length());
			subString += SYMBOLS_FOR_ADDITONAL_WIDTH1;
			
			int subStringSize = SwingUtilities.computeStringWidth(
					fontMetrics,
					subString);

			if (subStringSize > maxLineSize)
				maxLineSize = subStringSize;

			if (currentPosition >= 0) {
				lineEndsFound++;
				lastPosition = currentPosition;
			} else {
				break;				
			}
		}
	
		Dimension result = new Dimension(
				maxLineSize,
				fontMetrics.getHeight() * (lineEndsFound + 1));
		
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
	
	public void setATPropertyChangeListener(PropertyChangeListener listener) {
		this.atPropertyChangeListener = listener;
	}
	
	public PropertyChangeListener getATPropertyChangeListener() {
		return this.atPropertyChangeListener;
	}
}
