/**
 * $Id: ZoomOutCommand.java,v 1.6 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;

/**
 * Команда "Отдалить вид со стандартным коэффициентом" 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public class ZoomOutCommand extends MapNavigateCommand {
	public ZoomOutCommand(LogicalNetLayer logicalNetLayer) {
		super(logicalNetLayer);
	}

	public void execute() {
		try {
			this.logicalNetLayer.zoomOut();
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
