/*-
 * $Id: ReverseDependencyContainer.java,v 1.2.8.1 2006/05/18 17:46:35 bass Exp $
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
 * @version $Revision: 1.2.8.1 $, $Date: 2006/05/18 17:46:35 $
 * @module general
 */
public interface ReverseDependencyContainer {
	Set<Identifiable> getReverseDependencies() throws ApplicationException;
}
