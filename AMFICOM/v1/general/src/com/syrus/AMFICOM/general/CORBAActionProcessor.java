/*-
* $Id: CORBAActionProcessor.java,v 1.2 2005/10/11 14:15:09 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;



/**
 * @version $Revision: 1.2 $, $Date: 2005/10/11 14:15:09 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module csbridge
 */
public interface CORBAActionProcessor {

	void performAction(final CORBAAction action) throws ApplicationException;

}
