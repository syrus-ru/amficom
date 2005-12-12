/*-
* $Id: InsetsResourceHandler.java,v 1.3 2005/12/12 13:40:13 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Insets;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/12/12 13:40:13 $
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
		final java.awt.Insets awtInsets = 
			new java.awt.Insets(top, left, bottom, right);
		assert Log.debugMessage("instens for id:" 
				+ id 
				+ " is " 
				+ awtInsets, 
			Log.DEBUGLEVEL08);
		UIManager.put(id, awtInsets);
	}
}
