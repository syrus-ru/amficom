/*-
 * $Id: PathMember.java,v 1.5 2005/08/04 18:55:05 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.ApplicationException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/08/04 18:55:05 $
 * @module scheme
 */
public interface PathMember<T, U extends PathMember<T, U>> {
	void insertSelfBefore(final U sibling) throws ApplicationException;

	void insertSelfAfter(final U sibling) throws ApplicationException;

	int getSequentialNumber();

	T getParentPathOwner();

	void setParentPathOwner(final T parentPathOwner, final boolean processSubsequentSiblings);
}
