/*
 * UIStorage.java Created on 26.04.2004 11:47:57
 *  
 */

package com.syrus.AMFICOM.Client.Scheduler.General;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;


/**
 * Storage for frequency used gui routines
 * 
 * @author Vladimir Dolzhenko
 */
public final class UIStorage {

	public static final String				ICON_SCHEDULER_MINI		= "com.syrus.AMFICOM.icon.scheduler.mainmini";
	public static final String				ICON_DELETE				= "com.syrus.AMFICOM.icon.scheduler.delete";
	public static final String				ICON_RESUME				= "com.syrus.AMFICOM.icon.scheduler.resume";
	public static final String				ICON_PAUSE				= "com.syrus.AMFICOM.icon.scheduler.pause";

	public static final Icon				PLUS_ICON				= UIStorage
																			.getStringIcon("+");					//$NON-NLS-1$

	public static final Icon				MINUS_ICON				= UIStorage
																			.getStringIcon("-");					//$NON-NLS-1$
	public static final Icon				FILTER_ICON				= UIStorage
																			.getStringIcon("F");					//$NON-NLS-1$

	public static final SimpleDateFormat	HOUR_MINUTE_DATE_FORMAT	= new SimpleDateFormat(
																							"HH:mm");
	
	public static final String EDGE_COLOR =  "scheduler.color.edge";

	public static final String	COLOR_STOPPED						= "scheduler.color.stopped";

	public static final String	COLOR_STOPPED_SELECTED				= "scheduler.color.stoppedSelected";
	
	public static final String	COLOR_ABORTED						= "scheduler.color.aborted";

	public static final String	COLOR_ABORTED_SELECTED				= "scheduler.color.abortedSelected";

	public static final String	COLOR_ALARM							= "scheduler.color.alarm";

	public static final String	COLOR_ALARM_SELECTED				= "scheduler.color.alarmSelected";

	public static final String	COLOR_COMPLETED						= "scheduler.color.complete";

	public static final String	COLOR_COMPLETED_SELECTED			= "scheduler.color.completeSelected";

	public static final String	COLOR_PROCCESSING					= "scheduler.color.processing";

	public static final String	COLOR_PROCCESSING_SELECTED			= "scheduler.color.processingSelected";

	public static final String	COLOR_SCHEDULED						= "scheduler.color.scheduled";

	public static final String	COLOR_SCHEDULED_SELECTED			= "scheduler.color.scheduledSelected";

	public static final String	COLOR_UNRECOGNIZED					= "scheduler.color.unrecognized";

	public static final String	COLOR_WARNING						= "scheduler.color.warning";

	public static final String	COLOR_WARNING_SELECTED				= "scheduler.color.warningSelected";

	

	private UIStorage() {
		// nothing
	}

	public static Icon getStringIcon(String s) {
		return getStringIcon(s, 0);
	}

	/**
	 * create Icon with size 16x16 , and draw String on it
	 * 
	 * @param s
	 *            text which will draw on Icon
	 * @param angle
	 *            rotation angle in degree
	 * @return Icon
	 */
	public static Icon getStringIcon(	final String s,
	                                 	final int angle) {
		final int w = 16;
		final int h = 16;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g2d = (Graphics2D) img.getGraphics();
		final FontMetrics fm = g2d.getFontMetrics();
		final Font font = UIManager.getFont("Button.font");
		g2d.setFont(font);
		g2d.setColor(UIManager.getColor("Button.foreground"));
		g2d.drawString(s, w / 4, (h / 2 + fm.getHeight()) / 2);
		
		final AffineTransform tx = new AffineTransform();
		tx.rotate(angle * Math.PI / 180.0, img.getWidth() / 2, img.getHeight() / 2);
		
		final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		
		return new ImageIcon(img);
	}

}
