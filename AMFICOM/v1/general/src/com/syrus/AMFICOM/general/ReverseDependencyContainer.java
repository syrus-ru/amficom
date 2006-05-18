/*-
 * $Id: ReverseDependencyContainer.java,v 1.2 2005/09/30 16:19:23 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/09/30 16:19:23 $
 * @module general
 */
public interface ReverseDependencyContainer {
	Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException;
}
