/**
 * $Id: ZoomOutCommand.java,v 1.3 2005/01/12 15:45:53 krupenn Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/01/12 15:45:53 $
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

	public void execute()
	{
		logicalNetLayer.zoomOut();
		logicalNetLayer.repaint(true);
	}
}
