/*-
 * $Id: SchemeOptimizeInfoSwitch.java,v 1.8 2005/07/12 08:40:55 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoSwitch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/07/12 08:40:55 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoSwitch extends AbstractCloneableStorableObject {
	private static final long serialVersionUID = 2583191675321445786L;

	@SuppressWarnings("unused")
	public SchemeOptimizeInfoSwitch(final IdlSchemeOptimizeInfoSwitch transferable) throws CreateObjectException {
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
	public IdlSchemeOptimizeInfoSwitch getTransferable(final ORB orb) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SchemeOptimizeInfoSwitch clone() {
		final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch = (SchemeOptimizeInfoSwitch) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeOptimizeInfoSwitch;
	}
}
