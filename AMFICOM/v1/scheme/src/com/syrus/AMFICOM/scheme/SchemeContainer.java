/*-
 * $Id: SchemeContainer.java,v 1.1 2005/09/12 02:52:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/09/12 02:52:17 $
 * @module scheme
 */
interface SchemeContainer extends Identifiable {
	Set<Scheme> getSchemes(final boolean usePool) throws ApplicationException;
}
