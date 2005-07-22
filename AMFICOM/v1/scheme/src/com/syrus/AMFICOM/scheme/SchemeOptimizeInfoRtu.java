/*-
 * $Id: SchemeOptimizeInfoRtu.java,v 1.10 2005/07/22 15:09:40 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoRtu;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/07/22 15:09:40 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoRtu extends StorableObject implements Cloneable {
	private static final long serialVersionUID = 6687067380421014690L;

	@SuppressWarnings("unused")
	public SchemeOptimizeInfoRtu(final IdlSchemeOptimizeInfoRtu transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}

	Identifier getParentSchemeOptimizeInfoId() {
		throw new UnsupportedOperationException();
	}

	public SchemeOptimizeInfo getParentSchemeOptimizeInfo() {
		throw new UnsupportedOperationException();
	}

	public void setParentSchemeOptimizeInfo(@SuppressWarnings("unused") final SchemeOptimizeInfo schemeOptimizeInfo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeOptimizeInfoRtu getTransferable(final ORB orb) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SchemeOptimizeInfoRtu clone() throws CloneNotSupportedException {
		final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu = (SchemeOptimizeInfoRtu) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeOptimizeInfoRtu;
	}
}
