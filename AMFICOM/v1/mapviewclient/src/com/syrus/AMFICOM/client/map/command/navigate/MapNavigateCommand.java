/**
 * $Id: MapNavigateCommand.java,v 1.5 2005/08/11 12:43:30 arseniy Exp $ 
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
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
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
