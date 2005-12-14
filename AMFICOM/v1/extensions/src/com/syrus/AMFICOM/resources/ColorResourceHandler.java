/*-
* $Id: ColorResourceHandler.java,v 1.4 2005/12/14 08:14:23 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Color;
import com.syrus.AMFICOM.extensions.resources.Rgb;
import com.syrus.AMFICOM.extensions.resources.Color.Name.Enum;
import com.syrus.util.Log;


/**
 * Color xml resource handler 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/12/14 08:14:23 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class ColorResourceHandler implements ConcreateResourceHandler<Color>{
	
	private static Map<Enum, java.awt.Color> COLOR_MAP; 
	
	public void load(final Color color) {
		final java.awt.Color awtColor;
		if (color.isSetRgb()) {
			final Rgb rgb = color.getRgb();
			final int red = rgb.getRed();
			final int green = rgb.getGreen();
			final int blue = rgb.getBlue();
			final boolean alphaEnable = rgb.isSetAlpha();
			
			
			if (alphaEnable) {
				final int alpha = rgb.getAlpha();
				awtColor = new java.awt.Color(red, green, blue, alpha);
			} else {
				awtColor = new java.awt.Color(red, green, blue);
			}
		} else {
			final Enum colorEnum = color.getName();
			awtColor = getColorMap().get(colorEnum);
			if (awtColor == null) {
				Log.errorMessage("Color alias for " + colorEnum.toString() + " does not registered.");
			}
		}
		final String id = color.getId();
		assert Log.debugMessage("color for id:" + id + " is " + awtColor, Log.DEBUGLEVEL08);
		UIManager.put(id, awtColor);
	}
	
	/**
	 * @return Xml Color mapping to java.awt.Color
	 */
	public static synchronized Map<Enum, java.awt.Color> getColorMap() {
		if (COLOR_MAP == null) {
			COLOR_MAP = new HashMap<Enum, java.awt.Color>(13);
			COLOR_MAP.put(Color.Name.WHITE, java.awt.Color.WHITE);
			COLOR_MAP.put(Color.Name.LIGHT_GRAY, java.awt.Color.LIGHT_GRAY);
			COLOR_MAP.put(Color.Name.GRAY, java.awt.Color.GRAY);
			COLOR_MAP.put(Color.Name.DARK_GRAY, java.awt.Color.DARK_GRAY);
			COLOR_MAP.put(Color.Name.BLACK, java.awt.Color.BLACK);
			COLOR_MAP.put(Color.Name.RED, java.awt.Color.RED);
			COLOR_MAP.put(Color.Name.PINK, java.awt.Color.PINK);
			COLOR_MAP.put(Color.Name.ORANGE, java.awt.Color.ORANGE);
			COLOR_MAP.put(Color.Name.YELLOW, java.awt.Color.YELLOW);
			COLOR_MAP.put(Color.Name.GREEN, java.awt.Color.GREEN);
			COLOR_MAP.put(Color.Name.MAGENTA, java.awt.Color.MAGENTA);
			COLOR_MAP.put(Color.Name.CYAN, java.awt.Color.CYAN);
			COLOR_MAP.put(Color.Name.BLUE, java.awt.Color.BLUE);
		}

		return COLOR_MAP;
	}
}
