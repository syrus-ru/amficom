/*-
 * $Id: PathOwner.java,v 1.1 2005/07/20 14:49:49 bass Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/07/20 14:49:49 $
 * @module scheme_v1
 */
public interface PathOwner<T> {
	void addPathMember(final T child, final boolean processSubsequentSiblings);

	void removePathMember(final T child, final boolean processSubsequentSiblings);

	SortedSet<T> getPathMembers(); 
}
