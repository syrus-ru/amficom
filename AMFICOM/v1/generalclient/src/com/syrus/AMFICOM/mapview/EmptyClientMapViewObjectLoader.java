package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

public class EmptyClientMapViewObjectLoader implements MapViewObjectLoader {
	public void delete(final Set ids) {
		// nothing to do
	}

	public Set loadMapViews(final Set ids) {
		return Collections.EMPTY_SET;
	}

	public Set loadMapViewsButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public Set refresh(final Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public void saveMapViews(final Set set,
			final boolean force) throws ApplicationException {
		// empty
	}
}
