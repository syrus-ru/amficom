/*
 * UIUtil.java
 * Created on 26.04.2004 11:47:57
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

/**
 * Storage for frequency used gui routines
 * 
 * @author Vladimir Dolzhenko
 */
final public class UIUtil {
	public static Insets nullInsets = new Insets(0, 0, 0, 0);
	public static Insets inset1010 = new Insets(1, 0, 1, 0);
	public static Icon plusIcon = UIUtil.getStringIcon("+");
	public static Icon minusIcon = UIUtil.getStringIcon("-");
	public static Icon calendarIcon = UIUtil.getStringIcon("^");
	public static Icon timeIcon = UIUtil.getStringIcon("t");

	public static Icon openFileIcon =
		new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/openfile.gif"));
	public static Icon deleteIcon =
		new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/delete.gif"));
	/**
	 * create Icon with size 16x16 , and draw String on it   
	 * @param s text which will draw on Icon
	 * @return Icon
	 */
	public static Icon getStringIcon(String s) {
		int w = 16;
		int h = 16;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setBackground(Color.lightGray);
		g2d.clearRect(0, 0, w, h);
		Font font = new Font("Monospaced", Font.BOLD, 14);
		g2d.setFont(font);
		g2d.setColor(Color.black);
		g2d.drawString(s, w / 4, (h / 2 + fm.getHeight()) / 2);
		Icon icon = new ImageIcon((Image) img);
		return icon;
	}
	/**
	 * set rigid dimenstion (min,max,preferred) size  for component 
	 * @param component
	 * @param dimension
	 */
	public static void setRigidSize(
		JComponent component,
		Dimension dimension) {
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
		component.setMinimumSize(dimension);
	}
	/**
	 * create JRadioButton with title and Action
	 * @param title title for button
	 * @param action 
	 * @return (min,max,preferred)
	 */
	public static JRadioButton createRadioButton(
		final String title,
		final Action action) {
		JRadioButton button = new JRadioButton(title);
		button.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					action.actionPerformed(
						new ActionEvent(e, e.getID(), title));
				}
			}
		});
		return button;
	}

}
