/*-
 * $Id: SchemeOptimizeInfoRtu.java,v 1.14 2005/08/01 16:18:09 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoRtu;

/**
 * #07 in hierarchy.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/08/01 16:18:09 $
 * @module scheme
 */
public final class SchemeOptimizeInfoRtu extends StorableObject
		implements ReverseDependencyContainer {
	private static final long serialVersionUID = 6687067380421014690L;

	Identifier parentSchemeOptimizeInfoId;

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
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies()
	 */
	public Set<Identifiable> getReverseDependencies() {
		return Collections.<Identifiable>singleton(super.id);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeOptimizeInfoRtu getTransferable(final ORB orb) {
		throw new UnsupportedOperationException();
	}
}
