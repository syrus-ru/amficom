/*-
* $Id: InsetsResourceHandler.java,v 1.2 2005/11/11 12:46:16 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Insets;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/11 12:46:16 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class InsetsResourceHandler implements ConcreateResourceHandler<Insets>{
	public void load(final Insets insets) {
		final int top = insets.getTop();
		final int left = insets.getLeft();
		final int bottom = insets.getBottom();
		final int right = insets.getRight();
		final String id = insets.getId();
		final java.awt.Insets awtInsets = new java.awt.Insets(top, left, bottom, right);
		System.out.println("instens for id:" + id + " is " + awtInsets);		
		UIManager.put(id, awtInsets);
	}
}
