/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.7 2005/01/30 15:38:17 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление кабельного пути из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/30 15:38:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveCablePathCommandAtomic extends MapActionCommand
{
	CablePath cp;
	
	public RemoveCablePathCommandAtomic(CablePath cp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.cp = cp;
	}
	
	public CablePath getPath()
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

		logicalNetLayer.getMapViewController().removeCablePath(cp);
	}

	public void redo()
	{
		logicalNetLayer.getMapViewController().removeCablePath(cp);
	}

	public void undo()
	{
		logicalNetLayer.getMapViewController().addCablePath(cp);
	}
}

