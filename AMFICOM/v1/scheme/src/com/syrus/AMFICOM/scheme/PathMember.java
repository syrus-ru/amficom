/*-
 * $Id: PathMember.java,v 1.4 2005/07/21 18:34:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/21 18:34:00 $
 * @module scheme
 */
public interface PathMember<T, U extends PathMember<T, U>> {
	void insertSelfBefore(final U sibling);

	void insertSelfAfter(final U sibling);

	int getSequentialNumber();

	T getParentPathOwner();

	void setParentPathOwner(final T parentPathOwner, final boolean processSubsequentSiblings);
}
