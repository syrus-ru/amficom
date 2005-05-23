/*-
 * $Id: SchemeOptimizeInfoRtu.java,v 1.2 2005/05/23 16:18:43 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoRtu_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/05/23 16:18:43 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoRtu extends AbstractCloneableStorableObject {
	private static final long serialVersionUID = 6687067380421014690L;

	SchemeOptimizeInfoRtu(final SchemeOptimizeInfoRtu_Transferable transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}

	public SchemeOptimizeInfo getParentSchemeOptimizeInfo() {
		throw new UnsupportedOperationException();
	}

	public void setParentSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		throw new UnsupportedOperationException();
	}

	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}
}
