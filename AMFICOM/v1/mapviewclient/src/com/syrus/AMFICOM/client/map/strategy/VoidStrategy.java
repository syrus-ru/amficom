/**
 * $Id: VoidStrategy.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;

import java.awt.event.MouseEvent;
/**
 * Пустая стратегия. Ничего не делает
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class VoidStrategy implements MapStrategy 
{
	private static VoidStrategy instance = new VoidStrategy();
	
	private VoidStrategy()
	{
	}
	
	public static VoidStrategy getInstance()
	{
		return instance;
	}

	public void doContextChanges(MouseEvent me)
	{
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
	}
	
	public void setMapElement(MapElement me)
	{
	}
}

