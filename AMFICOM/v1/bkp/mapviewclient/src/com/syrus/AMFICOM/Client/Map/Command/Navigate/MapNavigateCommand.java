/**
 * $Id: MapNavigateCommand.java,v 1.1 2005/02/18 12:18:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

/**
 *  
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/02/18 12:18:55 $
 * @module mapviewclient_v1
 */
public class MapNavigateCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;

	protected Throwable exception = null;
	
	public MapNavigateCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			this.aModel = (ApplicationModel )value;
	}

	protected LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}

	public Throwable getException()
	{
		return this.exception;
	}
	
	public void setException(Throwable exception)
	{
		this.exception = exception;
	}
}
