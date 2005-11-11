/*-
* $Id: ColorResourceHandler.java,v 1.1 2005/11/11 11:14:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import javax.swing.UIManager;

import com.syrus.amficom.extensions.resources.Color;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 11:14:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class ColorResourceHandler implements ConcreateResourceHandler<Color>{
	public void load(final Color color) {
		final int red = color.getRed();
		final int green = color.getGreen();
		final int blue = color.getBlue();
		final boolean alphaEnable = color.isSetAlpha();
		
		final java.awt.Color awtColor;
		if (alphaEnable) {
			final int alpha = color.getAlpha();
			awtColor = new java.awt.Color(red, green, blue, alpha);
		} else {
			awtColor = new java.awt.Color(red, green, blue);
		}
		final String id = color.getId();
		System.out.println("color for id:" + id + " is " + awtColor);		
		UIManager.put(id, awtColor);
	}
}
