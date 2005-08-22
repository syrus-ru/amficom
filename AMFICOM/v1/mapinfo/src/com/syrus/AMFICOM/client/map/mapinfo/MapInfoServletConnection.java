package com.syrus.AMFICOM.client.map.mapinfo;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;

public class MapInfoServletConnection extends MapInfoConnection {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapConnection#createImageLoader()
	 */
	@Override
	public MapImageLoader createImageLoader() throws MapConnectionException {
		return new MapInfoServletImageLoader(this);
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapConnection#getAvailableViews()
	 */
	@Override
	public List<String> getAvailableViews() throws MapDataException {
		final List<String> listToReturn = new ArrayList<String>();
		listToReturn.add(this.getPath() + this.getView());

		return listToReturn;
	}
}
