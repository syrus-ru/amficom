/*-
* $Id: ColorResourceHandler.java,v 1.3 2005/12/13 09:02:01 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/12/13 09:02:01 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class ColorResourceHandler implements ConcreateResourceHandler<Color>{
	
	private static Map<String, java.awt.Color> COLOR_MAP; 
	
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
			final String name = colorEnum.toString();
			awtColor = getColorMap().get(name);
			if (awtColor == null) {
				Log.errorMessage("Color alias for " + name + " does not registered.");
			}
		}
		final String id = color.getId();
		assert Log.debugMessage("color for id:" + id + " is " + awtColor, Log.DEBUGLEVEL08);
		UIManager.put(id, awtColor);
	}
	
	public static synchronized Map<String, java.awt.Color> getColorMap() {
		if (COLOR_MAP == null) {
			COLOR_MAP = new HashMap<String, java.awt.Color>(13);
			COLOR_MAP.put("white", java.awt.Color.white);
			COLOR_MAP.put("lightGray", java.awt.Color.lightGray);
			COLOR_MAP.put("gray", java.awt.Color.gray);
			COLOR_MAP.put("darkGray", java.awt.Color.darkGray);
			COLOR_MAP.put("black", java.awt.Color.black);
			COLOR_MAP.put("red", java.awt.Color.red);
			COLOR_MAP.put("pink", java.awt.Color.pink);
			COLOR_MAP.put("orange", java.awt.Color.orange);
			COLOR_MAP.put("yellow", java.awt.Color.yellow);
			COLOR_MAP.put("green", java.awt.Color.green);
			COLOR_MAP.put("magenta", java.awt.Color.magenta);
			COLOR_MAP.put("cyan", java.awt.Color.cyan);
			COLOR_MAP.put("blue", java.awt.Color.blue);
		}

		return COLOR_MAP;
	}
}
