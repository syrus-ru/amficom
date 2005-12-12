/*-
* $Id: ExtensionHandler.java,v 1.2 2005/12/12 13:40:13 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.extensions;



/**
 * @version $Revision: 1.2 $, $Date: 2005/12/12 13:40:13 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public interface ExtensionHandler<T extends ExtensionPoint> {

	public void addHandlerData(final T resources);
	
}

