/**
 * $Id: ZoomInCommand.java,v 1.7 2005/06/06 12:20:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;

/**
 * Команда "Приблизить вид со стандартным коэффициентом" 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/06/06 12:20:32 $
 * @module mapviewclient_v1
 */
public class ZoomInCommand extends MapNavigateCommand {
	public ZoomInCommand(LogicalNetLayer logicalNetLayer) {
		super(logicalNetLayer);
	}

	public void execute() {
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
