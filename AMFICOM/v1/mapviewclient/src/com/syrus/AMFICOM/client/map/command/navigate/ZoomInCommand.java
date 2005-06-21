/**
 * $Id: ZoomInCommand.java,v 1.9 2005/06/21 12:43:11 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageRenderer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;

/**
 * Команда "Приблизить вид со стандартным коэффициентом" 
 * @author $Author: peskovsky $
 * @version $Revision: 1.9 $, $Date: 2005/06/21 12:43:11 $
 * @module mapviewclient_v1
 */
public class ZoomInCommand extends MapNavigateCommand {
	public ZoomInCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	public void execute() {
		try {
			MapContext mapContext = this.netMapViewer.getMapContext();
			MapImageRenderer mapRenderer = this.netMapViewer.getRenderer();			
			mapContext.zoomIn();
			mapRenderer.setScale(mapContext.getScale());
			this.netMapViewer.repaint(true);
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
