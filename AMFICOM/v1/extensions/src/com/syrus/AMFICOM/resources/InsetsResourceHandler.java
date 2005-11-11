/*-
* $Id: InsetsResourceHandler.java,v 1.1 2005/11/11 11:14:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import javax.swing.UIManager;

import com.syrus.amficom.extensions.resources.Insets;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 11:14:30 $
 * @author $Author: bob $
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
