/*-
* $Id: DimensionResourceHandler.java,v 1.2 2005/11/11 12:46:16 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Dimension;
import com.syrus.AMFICOM.extensions.resources.Size;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/11 12:46:16 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class DimensionResourceHandler implements ConcreateResourceHandler<Dimension>{
	public void load(final Dimension dimension) {
		final Size size = dimension.getSize();
		final java.awt.Dimension awtDimension = new java.awt.Dimension(size.getWidth(), size.getHeight());
		final String id = dimension.getId();
		System.out.println("dimension for id:" + id + " is " + awtDimension);		
		UIManager.put(id, awtDimension);
	}
}
