package com.syrus.AMFICOM.client.map.mapinfo;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapImageLoader;

public class MapInfoLocalConnection extends MapInfoConnection
{
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapConnection#createImageLoader()
	 */
	public MapImageLoader createImageLoader() throws MapConnectionException
	{
		return new MapInfoLocalStubImageLoader(this);
	}
}
