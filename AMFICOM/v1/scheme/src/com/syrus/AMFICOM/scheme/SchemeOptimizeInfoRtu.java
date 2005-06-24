/*-
 * $Id: SchemeOptimizeInfoRtu.java,v 1.5 2005/06/24 14:13:38 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoRtu;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/24 14:13:38 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoRtu extends AbstractCloneableStorableObject {
	private static final long serialVersionUID = 6687067380421014690L;

	@SuppressWarnings("unusedThrown")
	SchemeOptimizeInfoRtu(@SuppressWarnings("unusedArgument") final IdlSchemeOptimizeInfoRtu transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}

	public SchemeOptimizeInfo getParentSchemeOptimizeInfo() {
		throw new UnsupportedOperationException();
	}

	public void setParentSchemeOptimizeInfo(@SuppressWarnings("unusedArgument") final SchemeOptimizeInfo schemeOptimizeInfo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		throw new UnsupportedOperationException();
	}

	public IdlSchemeOptimizeInfoRtu getTransferable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SchemeOptimizeInfoRtu clone() {
		final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu = (SchemeOptimizeInfoRtu) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeOptimizeInfoRtu;
	}
}
