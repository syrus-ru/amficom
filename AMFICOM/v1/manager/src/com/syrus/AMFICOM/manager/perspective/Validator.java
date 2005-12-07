/*-
* $Id: Validator.java,v 1.3 2005/12/07 15:43:50 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

/**
 * @version $Revision: 1.3 $, $Date: 2005/12/07 15:43:50 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Validator {

	boolean isValid(final String source, final String target);
}

