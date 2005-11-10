/*-
* $Id: Validator.java,v 1.3 2005/11/10 13:59:01 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/10 13:59:01 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface Validator<T extends AbstractBean> {

	boolean isValid(final String source, final String target);
}

