/*-
* $Id: ConcreateResourceHandler.java,v 1.1 2005/11/11 11:14:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import com.syrus.amficom.extensions.resources.Resource;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 11:14:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public interface ConcreateResourceHandler <T extends Resource> {
	void load(final T t);
}
