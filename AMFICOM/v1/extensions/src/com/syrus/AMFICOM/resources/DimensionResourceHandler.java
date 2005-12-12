/*-
* $Id: DimensionResourceHandler.java,v 1.3 2005/12/12 13:40:13 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Dimension;
import com.syrus.AMFICOM.extensions.resources.Size;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/12/12 13:40:13 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class DimensionResourceHandler implements ConcreateResourceHandler<Dimension>{
	public void load(final Dimension dimension) {
		final Size size = dimension.getSize();
		final java.awt.Dimension awtDimension = 
			new java.awt.Dimension(size.getWidth(), size.getHeight());
		final String id = dimension.getId();
		assert Log.debugMessage("dimension for id:" 
				+ id 
				+ " is " 
				+ awtDimension, 
			Log.DEBUGLEVEL03);
		UIManager.put(id, awtDimension);
	}
}
