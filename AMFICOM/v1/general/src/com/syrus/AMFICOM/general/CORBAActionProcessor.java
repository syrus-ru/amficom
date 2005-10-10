/*-
* $Id: CORBAActionProcessor.java,v 1.1 2005/10/10 10:43:03 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;



/**
 * @version $Revision: 1.1 $, $Date: 2005/10/10 10:43:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module csbridge
 */
public interface CORBAActionProcessor {
	
	void performAction(final CORBAAction action) throws ApplicationException;
	
}

