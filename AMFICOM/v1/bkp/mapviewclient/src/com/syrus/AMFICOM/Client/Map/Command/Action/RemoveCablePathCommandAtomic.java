/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление кабельного пути из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

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

