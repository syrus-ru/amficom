package com.syrus.AMFICOM.mapview;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;

import java.util.Collection;
import java.util.Collections;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import java.util.Set;
import com.syrus.AMFICOM.general.VersionCollisionException;

public class EmptyClientMapViewObjectLoader implements MapViewObjectLoader 
{
	public EmptyClientMapViewObjectLoader()
	{
	}

	public void delete(Identifier id) throws IllegalDataException
	{
	}

	public void delete(Collection ids) 
	{
	}

	public MapView loadMapView(Identifier id) throws DatabaseException, CommunicationException
	{
		return null;
	}

	public Collection loadMapViews(Collection ids)
	{
		return Collections.EMPTY_LIST;
	}

	public Collection loadMapViewsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return Collections.EMPTY_LIST;
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException
	{
		return Collections.EMPTY_SET;
	}

	public void saveMapView(MapView map, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
	}

	public void saveMapViews(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
	}
}