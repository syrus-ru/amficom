package com.syrus.AMFICOM.Client.General.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public final class DEF 
{
	public static final Color backgroundNodeLinkSizeTable = Color.white;

	public static final String metric = "ì.";
	public static final boolean show_length = true;

	public static final int thickness = 1;
	public static final String style = "Solid line";
	public static final BasicStroke stroke = new BasicStroke(2);
	public static final Color color = Color.blue;

	public static final boolean alarmState = false;
	public static final boolean showAlarmState = true;

	public static final int alarmed_thickness = 3;
	public static final String alarmed_style = "Solid line";
	public static final BasicStroke alarmed_stroke = new BasicStroke(3);
	public static final Color alarmed_color = Color.red;
	public static final String alarmed_animation = "blink";

	public static final String font_id = "Arial_1_12";
	public static final Font font = new Font ("Arial", 1, 12);
	
	public static final int selected_thickness = 1;
	public static final String selected_style = "Solid line";
	public static final BasicStroke selected_stroke = new BasicStroke( 
			1,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL,
			(float)0.0,
			new float[] {5, 5},
			(float)0.0);
	public static final Color selected_color = Color.blue;
}
