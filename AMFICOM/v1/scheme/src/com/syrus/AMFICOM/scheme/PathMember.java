/*-
 * $Id: PathMember.java,v 1.1 2005/07/20 14:49:49 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/20 14:49:49 $
 * @module scheme_v1
 */
public interface PathMember<T extends PathOwner<U>, U> {
	void insertSelfBefore(final U sibling);

	void insertSelfAfter(final U sibling);

	int getSequentialNumber();

	T getParentPathOwner();

	void setParentPathOwner(final T parentPathOwner, final boolean processSubsequentSiblings);
}
