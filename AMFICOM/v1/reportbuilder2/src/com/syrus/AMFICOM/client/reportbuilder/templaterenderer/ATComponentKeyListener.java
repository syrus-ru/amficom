/*
 * $Id: ATComponentKeyListener.java,v 1.2 2006/04/26 13:13:01 stas Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;

public class ATComponentKeyListener implements KeyListener{
	private final ApplicationContext applicationContext;
	
	private static ATComponentKeyListener instance = null;
	
	public static void createInstance(
			ApplicationContext aContext){
		instance = new ATComponentKeyListener(aContext);
	}

	public static ATComponentKeyListener getInstance(){
		if (instance == null)
			throw new AssertionError("No instance for ATComponentKeyListener. Use createInstance() first!");
		return instance;
	}
	
	private ATComponentKeyListener(
			ApplicationContext aContext){
		this.applicationContext = aContext;
	}

	public void keyTyped(KeyEvent e) {
		AttachedTextComponent component = (AttachedTextComponent)e.getSource();
		AttachedTextStorableElement element =
			(AttachedTextStorableElement)component.getElement();
		
		char keyChar = e.getKeyChar();
		if (keyChar != KeyEvent.CHAR_UNDEFINED) {
			String textToProcess = component.getText();
	
			element.setText(textToProcess);
			
			//?????????? ?????? ??????????
			Dimension textSize = component.getTextSize();
			Dimension additionalSize = component.getAdditionalSize();			
			component.setSize(
					textSize.width + additionalSize.width,
					textSize.height + additionalSize.height);
			element.setSize(textSize.width,textSize.height);
			//?????????, ???? ?? ??? ?? ?????? ??????????
			component.checkComponentWidth();
		}
		
		this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		//Empty
	}
}
