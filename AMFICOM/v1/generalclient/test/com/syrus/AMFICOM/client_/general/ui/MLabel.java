/*
 * MLabel.java
 * Created on 23.08.2004 11:02:51
 * 
 */

package com.syrus.AMFICOM.client_.general.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JLabel;

import com.syrus.AMFICOM.CORBA.General.TestStatus;

/**
 * @author Vladimir Dolzhenko
 */
public class MLabel extends JLabel implements Comparable {

	private int	status;

	public MLabel(String text) {
		super(text);
		setOpaque(true);
	}

	public MLabel(String text, int status) {
		this(text);
		this.status = status;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color color = null;
		switch (this.status) {
			case TestStatus._TEST_STATUS_ABORTED:
				color = Color.RED;
				break;
			case TestStatus._TEST_STATUS_COMPLETED:
				color = Color.GREEN;
				break;
			case TestStatus._TEST_STATUS_PROCESSING:
				color = Color.CYAN;
				break;
			case TestStatus._TEST_STATUS_SCHEDULED:
				color = Color.GRAY;
				break;
			default:
				break;
		}
		if (color != null) {
			g.setColor(color);
			Font font = this.getFont();
			FontMetrics fm = this.getFontMetrics(font);
			int width = fm.stringWidth(this.getText());
			g.fillRect(width, getHeight() / 2 - 1, getWidth() - width, 3);
		}
		
	}	
	
	
	public int compareTo(Object o) {
		int result = 0;
		if (o instanceof MLabel){
			MLabel mlabel = (MLabel)o;
			result = this.getText().compareTo(mlabel.getText());
		}
		return result;
	}
}