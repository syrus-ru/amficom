/**
 * $Id: ZoomInCommand.java,v 1.5 2005/02/18 12:19:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;

/**
 * Команда "Приблизить вид со стандартным коэффициентом" 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/02/18 12:19:45 $
 * @module mapviewclient_v1
 */
public class ZoomInCommand extends MapNavigateCommand
{
	public ZoomInCommand(LogicalNetLayer logicalNetLayer)
	{
		super(logicalNetLayer);
	}

	public void execute()
	{
	    try {
			this.logicalNetLayer.zoomIn();
			this.logicalNetLayer.repaint(true);
		} catch(MapConnectionException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(MapDataException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
