/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

/**
 * удаление физической линии из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class RemoveCablePathCommandAtomic extends MapActionCommand
{
	MapCablePathElement cp;
	
	public RemoveCablePathCommandAtomic(MapCablePathElement cp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.cp = cp;
	}
	
	public MapCablePathElement getPath()
	{
		return cp;
	}
	
	public void execute()
	{
		logicalNetLayer.getMapView().removeCablePath(cp);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().removeCablePath(cp);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().addCablePath(cp);
	}
}

