package com.syrus.AMFICOM.mapview;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import java.util.Collections;
import java.util.List;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import java.util.Set;
import com.syrus.AMFICOM.general.VersionCollisionException;

public class EmptyClientMapViewObjectLoader implements MapViewObjectLoader 
{
	public EmptyClientMapViewObjectLoader()
	{
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException
	{
	}

	public void delete(List ids) throws CommunicationException, DatabaseException
	{
	}

	public MapView loadMapView(Identifier id) throws DatabaseException, CommunicationException
	{
		return null;
	}

	public List loadMapViews(List ids) throws DatabaseException, CommunicationException
	{
		return Collections.EMPTY_LIST;
	}

	public List loadMapViewsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
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

	public void saveMapViews(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
	}
}