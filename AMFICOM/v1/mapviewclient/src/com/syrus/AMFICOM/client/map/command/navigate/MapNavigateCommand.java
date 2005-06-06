/**
 * $Id: MapNavigateCommand.java,v 1.3 2005/06/06 12:20:31 krupenn Exp $ 
 * Syrus Systems 
 * Научно-технический центр 
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный 
 * Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class MapNavigateCommand extends AbstractCommand {
	LogicalNetLayer logicalNetLayer;

	ApplicationModel aModel;

	protected Throwable exception = null;

	public MapNavigateCommand(LogicalNetLayer logicalNetLayer) {
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value) {
		if(field.equals("logicalNetLayer"))
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			this.aModel = (ApplicationModel )value;
	}

	protected LogicalNetLayer getLogicalNetLayer() {
		return this.logicalNetLayer;
	}

	public Throwable getException() {
		return this.exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}
