/*-
* $Id: FontResourceHandler.java,v 1.2 2005/11/11 12:46:16 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Font;
import com.syrus.AMFICOM.extensions.resources.Font.Style;
import com.syrus.AMFICOM.extensions.resources.Font.Style.Enum;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/11 12:46:16 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class FontResourceHandler implements ConcreateResourceHandler<Font> {
	public void load(final Font font) {
		final String fontname = font.getFontname();
		final int size = font.getSize();
		final Enum style = font.getStyle();
		final int fontStyle;
		switch(style.intValue()) {
		case Style.INT_BOLD:
			fontStyle = java.awt.Font.BOLD;
			break;
		case Style.INT_ITALIC:
			fontStyle = java.awt.Font.ITALIC;
			break;
		case Style.INT_BOLD_ITALIC:
			fontStyle = java.awt.Font.BOLD | java.awt.Font.ITALIC;
			break;
		default :
			fontStyle = java.awt.Font.PLAIN;
			break;
		}
		final String id = font.getId();
		final java.awt.Font awtFont = new java.awt.Font(fontname, fontStyle, size);
		System.out.println("font for id:" + id + " is " + awtFont);		
		UIManager.put(id, awtFont);
	}
}
