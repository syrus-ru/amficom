/**
 * $Id: MapConnection.java,v 1.4 2005/05/27 15:14:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ 
 */
package com.syrus.AMFICOM.Client.Map;

import java.util.List;

import com.syrus.AMFICOM.client.model.Environment;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/27 15:14:54 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public abstract class MapConnection
{
	public abstract boolean connect() throws MapConnectionException;

	public abstract boolean release() throws MapConnectionException;

	public abstract void setPath(String path);

	public abstract void setView(String name);

	public abstract void setURL(String url);

	public abstract String getURL();

	public abstract String getPath();

	public abstract String getView();

	/**
	 * Получить список названий доступных видов.
	 * 
	 * @return Список видов &lt;{@link String}&gt;
	 */
	public abstract List getAvailableViews() throws MapDataException;

	public static MapConnection create(String connectionClass)
			throws MapConnectionException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER,
				"method call MapConnection.create()");

		MapConnection connection = null;
		try
		{
			connection = (MapConnection )Class.forName(connectionClass)
					.newInstance();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
			throw new MapConnectionException(
					"MapConnection.create() throws ClassNotFoundException");
		}
		catch(InstantiationException ie)
		{
			ie.printStackTrace();
			throw new MapConnectionException(
					"MapConnection.create() throws InstantiationException");
		}
		catch(IllegalAccessException iae)
		{
			iae.printStackTrace();
			throw new MapConnectionException(
					"MapConnection.create() throws IllegalAccessException");
		}

		return connection;
	}
}
