/*
 * UIStorage.java Created on 26.04.2004 11:47:57
 *  
 */
package com.syrus.AMFICOM.Client.Scheduler.General;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.text.SimpleDateFormat;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

/**
 * Storage for frequency used gui routines
 * 
 * @author Vladimir Dolzhenko
 */
final public class UIStorage {

	public static final Insets				INSET_NULL			= new Insets(0,
																		0, 0, 0);

	public static final Insets				INSETS1010			= new Insets(1,
																		0, 1, 0);

	public static final Icon				GENERAL_ICON		= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/general.gif"));	//$NON-NLS-1$

	public static final Icon				PLUS_ICON			= UIStorage
																		.getStringIcon("+");						//$NON-NLS-1$

	public static final Icon				MINUS_ICON			= UIStorage
																		.getStringIcon("-");						//$NON-NLS-1$

	public static final Icon				CALENDAR_ICON		= UIStorage
																		.getStringIcon("^");						//$NON-NLS-1$

	public static final Icon				TIME_ICON			= UIStorage
																		.getStringIcon("t");						//$NON-NLS-1$

	public static final Icon				OPEN_FILE_ICON		= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/openfile.gif"));	//$NON-NLS-1$

	public static final Icon				DELETE_ICON			= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/delete.gif"));		//$NON-NLS-1$

	public static final Icon				FOLDER_ICON			= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/folder.gif"));		//$NON-NLS-1$

	public static final Icon				TESTING_ICON		= new ImageIcon(
																		(Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/testing.gif") //$NON-NLS-1$
																				.getScaledInstance(
																						16,
																						16,
																						Image.SCALE_SMOOTH)));

	public static final Icon				PORT_ICON			= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/port.gif"));		//$NON-NLS-1$

	public static final Icon				PATHMODE_ICON		= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/pathmode.gif"));	//$NON-NLS-1$
	public static final Icon				REFRESH_ICON		= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/refresh.gif"));	//$NON-NLS-1$
	public static final Icon				SAVE_ICON			= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/save.gif"));		//$NON-NLS-1$

	public static final Icon				ZOOMIN_ICON			= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/zoom_in.gif"));	//$NON-NLS-1$
	public static final Icon				ZOOMOUT_ICON		= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/zoom_out.gif"));	//$NON-NLS-1$
	public static final Icon				NOZOOM_ICON			= new ImageIcon(
																		Toolkit
																				.getDefaultToolkit()
																				.getImage(
																						"images/zoom_actual.gif")); //$NON-NLS-1$

	public static final Cursor				WAIT_CURSOR			= new Cursor(
																		Cursor.WAIT_CURSOR);

	public static final Cursor				DEFAULT_CURSOR		= new Cursor(
																		Cursor.DEFAULT_CURSOR);

	public static final Cursor				CROSS_HAIR_CURSOR	= new Cursor(
																		Cursor.CROSSHAIR_CURSOR);

	public static final Font				ARIAL_12_FONT		= new Font(
																		"Arial", //$NON-NLS-1$
																		Font.PLAIN,
																		12);
	public static final Font				MONOSPACED_14_FONT	= new Font(
																		"Monospaced", //$NON-NLS-1$
																		Font.BOLD,
																		14);
	public static final SimpleDateFormat	sdf					= ConstStorage.SIMPLE_DATE_FORMAT;

	/**
	 * create Icon with size 16x16 , and draw String on it
	 * 
	 * @param s
	 *            text which will draw on Icon
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
		Font font = MONOSPACED_14_FONT;
		g2d.setFont(font);
		g2d.setColor(Color.black);
		g2d.drawString(s, w / 4, (h / 2 + fm.getHeight()) / 2);
		Icon icon = new ImageIcon(img);
		return icon;
	}

	/**
	 * set rigid dimenstion (min,max,preferred) size for component
	 * 
	 * @param component
	 * @param dimension
	 */
	public static void setRigidSize(JComponent component, Dimension dimension) {
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
		component.setMinimumSize(dimension);
	}

	/**
	 * create JRadioButton with title and Action
	 * 
	 * @param title
	 *            title for button
	 * @param action
	 * @return (min,max,preferred)
	 */
	public static JRadioButton createRadioButton(final String title,
			final Action action) {
		JRadioButton button = new JRadioButton(title);
		button.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					action
							.actionPerformed(new ActionEvent(e, e.getID(),
									title));
				}
			}
		});
		return button;
	}

}