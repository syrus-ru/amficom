/**
 * $Id: ZoomInCommand.java,v 1.4 2005/02/08 15:11:10 krupenn Exp $
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
 * Команда "Приблизить вид со стандартным коэффициентом" 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class ZoomInCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public ZoomInCommand(LogicalNetLayer logicalNetLayer)
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

	public void execute()
	{
	    this.logicalNetLayer.zoomIn();
		this.logicalNetLayer.repaint(true);
	}
}
