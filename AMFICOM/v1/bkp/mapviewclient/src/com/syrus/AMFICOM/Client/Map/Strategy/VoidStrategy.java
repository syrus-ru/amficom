/**
 * $Id: VoidStrategy.java,v 1.6 2005/02/07 16:09:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.map.MapElement;
/**
 * Пустая стратегия. Ничего не делает.
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/02/07 16:09:27 $
 * @module mapviewclient_v1
 */
public final class VoidStrategy extends MapStrategy 
{
	/**
	 * Instance.
	 */
	private static VoidStrategy instance = new VoidStrategy();
	
	/**
	 * Private constructor.
	 */
	private VoidStrategy()
	{//empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static VoidStrategy getInstance()
	{
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * <br>Empty.
	 */
	public void setMapElement(MapElement me)
	{//empty
	}
}

