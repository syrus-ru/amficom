/**
 * $Id: ZoomOutCommand.java,v 1.2 2004/10/19 10:41:03 krupenn Exp $
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
 * Команда "Отдалить вид со стандартным коэффициентом" 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/19 10:41:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ZoomOutCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public ZoomOutCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public Object clone()
	{
		return new ZoomOutCommand(logicalNetLayer);
	}

	public void execute()
	{
		logicalNetLayer.zoomOut();
	}
}
