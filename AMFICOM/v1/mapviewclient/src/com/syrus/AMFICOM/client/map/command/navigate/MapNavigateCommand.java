/**
 * $Id: MapNavigateCommand.java,v 1.2 2005/05/27 15:14:56 krupenn Exp $ 
 * Syrus Systems 
 * Научно-технический центр 
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный 
 * Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/05/27 15:14:56 $
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
