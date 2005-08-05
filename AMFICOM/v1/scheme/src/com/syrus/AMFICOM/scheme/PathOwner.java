/*-
 * $Id: PathOwner.java,v 1.6 2005/08/05 10:38:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.SortedSet;

import com.syrus.AMFICOM.general.ApplicationException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/08/05 10:38:06 $
 * @module scheme
 */
public interface PathOwner<U> {
	void addPathMember(final U child, final boolean processSubsequentSiblings);

	void removePathMember(final U child, final boolean processSubsequentSiblings);

	SortedSet<U> getPathMembers();

	U getPathMember(final int sequentialNumber) throws ApplicationException;
}
