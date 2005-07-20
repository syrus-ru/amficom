/*-
 * $Id: PathMember.java,v 1.2 2005/07/20 15:10:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/20 15:10:50 $
 * @module scheme_v1
 */
public interface PathMember<T extends PathOwner<U>, U extends PathMember> {
	void insertSelfBefore(final U sibling);

	void insertSelfAfter(final U sibling);

	int getSequentialNumber();

	T getParentPathOwner();

	void setParentPathOwner(final T parentPathOwner, final boolean processSubsequentSiblings);
}
