/**
 * $Id: VoidStrategy.java,v 1.3 2005/02/01 16:16:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.event.MouseEvent;
/**
 * Пустая стратегия. Ничего не делает.
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/01 16:16:13 $
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
	{
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
	public void doContextChanges(MouseEvent me)
	{
	}

	/**
	 * {@inheritDoc}
	 * <br>Empty.
	 */
	public void setMapElement(MapElement me)
	{
	}
}

