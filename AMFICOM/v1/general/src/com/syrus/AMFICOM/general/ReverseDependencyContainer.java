/*-
 * $Id: ReverseDependencyContainer.java,v 1.1 2005/08/01 16:18:10 bass Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/08/01 16:18:10 $
 * @module general
 */
public interface ReverseDependencyContainer {
	Set<Identifiable> getReverseDependencies() throws ApplicationException;
}
