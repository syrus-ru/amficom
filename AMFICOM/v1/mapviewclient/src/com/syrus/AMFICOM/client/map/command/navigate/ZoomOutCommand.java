/**
 * $Id: ZoomOutCommand.java,v 1.11 2005/08/11 12:43:30 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;

/**
 * Команда "Отдалить вид со стандартным коэффициентом" 
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class ZoomOutCommand extends MapNavigateCommand {
	public ZoomOutCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	public void execute() {
		try {
			this.netMapViewer.zoomOut();
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
