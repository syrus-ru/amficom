/*
 * $Id: AttachedTextComponent.java,v 1.1 2005/08/11 11:17:34 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.general.report;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.report.AttachedTextRenderingElement;
import com.syrus.AMFICOM.report.RenderingElement;
import com.syrus.AMFICOM.report.TextAttachingType;

public class AttachedTextComponent extends JTextPane implements ReportComponent{
	private static final long serialVersionUID = 8382110834808763027L;

	private static final int MINIMUM_COMPONENT_WIDTH = 60;
	
	private final AttachedTextRenderingElement textRenderingElement;

	/**
	 * Точка клика мыши на надписи
	 */
	private Point mousePressedLocation = new Point();
	/**
	 * Местоположение элемента в момент клика надписи
	 */
	private Point elementLocation = new Point();
	
	public AttachedTextComponent(AttachedTextRenderingElement atre)
	{
		this.textRenderingElement = atre;
		this.setListeners();
	}

	public RenderingElement getElement() {
		return this.textRenderingElement;
	}
	
	private void setListeners()
	{
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

		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				AttachedTextComponent.this.textPaneMouseDragged(e);
			}
		});
	}

	void textPaneKeyPressed(KeyEvent e)
	{
		this.checkComponentWidth();
		this.getElement().setModified(System.currentTimeMillis());
		this.repaint();
  }

	void textPaneMouseReleased(MouseEvent e)
	{
		if (SwingUtilities.isRightMouseButton(e))
		{
			TextComponentMenu menu = new TextComponentMenu(this);
			menu.setLocation(e.getX(),e.getY());
			menu.setVisible(true);
		}
	}

	void textPaneMousePressed(MouseEvent e)
	{
		this.mousePressedLocation.setLocation(e.getPoint());
		this.elementLocation.setLocation(this.getLocation());		

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
		// В режиме просмотра нельзя двигать привязанные надписи
		if (	ReportProcessingMode.getMode().equals(ReportProcessingMode.ProcessingMode.REPORT_PREVIEW)
			&&	(	!this.textRenderingElement.getHorizontalAttachType().equals(TextAttachingType.toFieldsLeft)
				||	!this.textRenderingElement.getVerticalAttachType().equals(TextAttachingType.toFieldsTop)))
			return;

		int newX = this.elementLocation.x + e.getX() - this.mousePressedLocation.x;
		int newY = this.elementLocation.y + e.getX() - this.mousePressedLocation.y;
		
		this.elementLocation.setLocation(newX,newY);
		this.setLocation(newX,newY);

		this.textRenderingElement.setModified(System.currentTimeMillis());
		this.repaint();
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
}
