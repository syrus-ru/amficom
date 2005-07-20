/*-
 * $Id: PathOwner.java,v 1.2 2005/07/20 15:10:50 bass Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/07/20 15:10:50 $
 * @module scheme_v1
 */
public interface PathOwner<U extends PathMember> {
	void addPathMember(final U child, final boolean processSubsequentSiblings);

	void removePathMember(final U child, final boolean processSubsequentSiblings);

	SortedSet<U> getPathMembers(); 
}
