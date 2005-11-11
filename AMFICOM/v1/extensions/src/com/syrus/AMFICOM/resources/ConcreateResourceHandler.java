/*-
* $Id: ConcreateResourceHandler.java,v 1.2 2005/11/11 11:23:59 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;



/**
 * @version $Revision: 1.2 $, $Date: 2005/11/11 11:23:59 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public interface ConcreateResourceHandler <T> {
	void load(final T t);
}
