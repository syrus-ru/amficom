/*-
 * $Id: CORBAResourceObjectLoader.java,v 1.3 2005/05/27 16:24:44 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;

public final class CORBAResourceObjectLoader extends CORBAObjectLoader implements ResourceObjectLoader {
	public CORBAResourceObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}

	public Set loadImageResources(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadImageResourcesButIds(StorableObjectCondition condition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveImageResources(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
