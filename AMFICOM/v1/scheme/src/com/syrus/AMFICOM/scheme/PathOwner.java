/*-
 * $Id: PathOwner.java,v 1.7 2005/10/17 12:09:36 bass Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/10/17 12:09:36 $
 * @module scheme
 */
public interface PathOwner<U> {
	void addPathMember(final U child, final boolean processSubsequentSiblings)
	throws ApplicationException;

	void removePathMember(final U child, final boolean processSubsequentSiblings)
	throws ApplicationException;

	SortedSet<U> getPathMembers() throws ApplicationException;

	U getPathMember(final int sequentialNumber) throws ApplicationException;
}
