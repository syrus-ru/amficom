/*-
 * $Id: SchemeOptimizeInfoRtu.java,v 1.4 2005/06/21 15:10:05 bass Exp $
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
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoRtu_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/06/21 15:10:05 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoRtu extends AbstractCloneableStorableObject {
	private static final long serialVersionUID = 6687067380421014690L;

	@SuppressWarnings("unusedThrown")
	SchemeOptimizeInfoRtu(@SuppressWarnings("unusedArgument") final SchemeOptimizeInfoRtu_Transferable transferable) throws CreateObjectException {
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

	public SchemeOptimizeInfoRtu_Transferable getTransferable() {
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
