/*
 * $Id: AttachedTextComponent.java,v 1.2 2005/08/31 10:32:55 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.StorableElement;

public class AttachedTextComponent extends JTextPane implements RenderingComponent{
	private static final long serialVersionUID = 8382110834808763027L;

	protected final AttachedTextStorableElement textRenderingElement;
	
	private static final int MINIMUM_COMPONENT_WIDTH = 60;
	/**
	 * Точка клика мыши на надписи
	 */
	private Point mousePressedLocation = new Point();
	
	private ATComponentMouseMotionListener mouseMotionListener = null;
	
	public AttachedTextComponent(AttachedTextStorableElement atre)
	{
		this.textRenderingElement = atre;
		this.setListeners();
	}

	public StorableElement getElement() {
		return this.textRenderingElement;
	}
	
	private void setListeners()
	{
		if (this.mouseMotionListener != null)
			this.removeMouseMotionListener(this.mouseMotionListener);
		
		this.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				AttachedTextComponent.this.textPaneKeyPressed(e);
			}
		});

		this.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				AttachedTextComponent.this.textPaneMousePressed(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				((AttachedTextComponent) e.getSource()).setCursor(new Cursor(Cursor.
					DEFAULT_CURSOR));
				AttachedTextComponent.this.textPaneMouseReleased(e);
			}
		});

//		this.addMouseMotionListener(new ATComponentMouseMotionListener());
		
		this.addPropertyChangeListener(new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				//Если передвигается компонент отображения данных, к которому
				//привязан данный компонент отображения текста - передигаем последний
				//на соответсвующее расстояние
				DataRenderingComponent component =
					(DataRenderingComponent)evt.getSource();
				
				DataStorableElement drElement =
					(DataStorableElement)component.getElement();
				
				AttachedTextStorableElement textElement = AttachedTextComponent.this.textRenderingElement;
				if (	drElement.equals(textElement.getHorizontalAttacher())
					||	drElement.equals(textElement.getVerticalAttacher()))
				{
					textElement.suiteAttachingDistances();
				}
			}});
	}

	void textPaneKeyPressed(KeyEvent e)
	{
		//Проверка на минимальную ширину компонента отображения текста
		this.checkComponentWidth();
		this.getElement().setModified(System.currentTimeMillis());
		this.repaint();
	}

	void textPaneMouseReleased(MouseEvent e)
	{
		if (SwingUtilities.isRightMouseButton(e))
		{
			//Вызов контекстного меню
			TextComponentMenu menu = new TextComponentMenu(this);
			menu.setLocation(e.getX(),e.getY());
			menu.setVisible(true);
		}
	}

	void textPaneMousePressed(MouseEvent e)
	{
		this.mousePressedLocation.setLocation(e.getPoint());

		this.checkComponentWidth();
		
		AttachedTextComponent textPane = (AttachedTextComponent) e.getSource();
		textPane.getCaret().setBlinkRate(500);
		textPane.getCaret().setVisible(true);
		textPane.setEditable(true);

		if (SwingUtilities.isLeftMouseButton(e))
			textPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		this.repaint();
	}

	void textPaneMouseDragged(MouseEvent e)
	{
	}
	
	/**
	 * Чтобы надпись всегда можно было разглядеть на экране
	 * ей выставляется минимальная величина.
	 */
	private void checkComponentWidth()
	{
		if (this.getWidth() < AttachedTextComponent.MINIMUM_COMPONENT_WIDTH)
			this.setSize(
					AttachedTextComponent.MINIMUM_COMPONENT_WIDTH,
					this.getHeight());
		
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
