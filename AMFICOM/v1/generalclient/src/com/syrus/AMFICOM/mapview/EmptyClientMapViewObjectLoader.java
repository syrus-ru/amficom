
package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

public class EmptyClientMapViewObjectLoader implements MapViewObjectLoader {

	public EmptyClientMapViewObjectLoader() {
		// nothing to do
	}

	public void delete(Identifier id) {
		// nothing to do
	}

	public void delete(Set ids) {
		// nothing to do
	}

	public MapView loadMapView(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadMapViews(Set ids) {
		return Collections.EMPTY_SET;
	}

	public Set loadMapViewsButIds(	StorableObjectCondition condition,
									Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		return Collections.EMPTY_SET;
	}

	public void saveMapView(MapView map,
							boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
	}

	public void saveMapViews(	Set set,
								boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
	}
}
