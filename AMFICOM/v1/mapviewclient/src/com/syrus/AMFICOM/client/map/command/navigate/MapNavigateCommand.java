/**
 * $Id: MapNavigateCommand.java,v 1.4 2005/06/16 10:57:20 krupenn Exp $ 
 * Syrus Systems 
 * Научно-технический центр 
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный 
 * Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public abstract class MapNavigateCommand extends AbstractCommand {

	protected ApplicationModel aModel;

	protected Throwable exception = null;

	protected NetMapViewer netMapViewer;

	public MapNavigateCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.aModel = aModel;
	}

	public Throwable getException() {
		return this.exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}
