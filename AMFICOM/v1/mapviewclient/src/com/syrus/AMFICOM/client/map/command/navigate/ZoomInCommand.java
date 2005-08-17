/**
 * $Id: ZoomInCommand.java,v 1.12 2005/08/17 14:14:19 arseniy Exp $
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
 * Команда "Приблизить вид со стандартным коэффициентом" 
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/08/17 14:14:19 $
 * @module mapviewclient
 */
public class ZoomInCommand extends MapNavigateCommand {
	public ZoomInCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
	public void execute() {
		try {
			this.netMapViewer.zoomIn();
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
