/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.2 2004/09/21 14:59:20 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление физической линии из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveCablePathCommandAtomic extends MapActionCommand
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
		Pool.remove(MapCablePathElement.typ, cp.getId());
	}

	public void redo()
	{
		logicalNetLayer.getMapView().removeCablePath(cp);
		Pool.remove(MapCablePathElement.typ, cp.getId());
	}

	public void undo()
	{
		logicalNetLayer.getMapView().addCablePath(cp);
		Pool.put(MapCablePathElement.typ, cp.getId(), cp);
	}
}

