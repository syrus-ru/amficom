/*-
 * $Id: PathOwner.java,v 1.5 2005/07/21 18:34:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.SortedSet;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/07/21 18:34:00 $
 * @module scheme
 */
public interface PathOwner<U> {
	void addPathMember(final U child, final boolean processSubsequentSiblings);

	void removePathMember(final U child, final boolean processSubsequentSiblings);

	SortedSet<U> getPathMembers(); 
}
