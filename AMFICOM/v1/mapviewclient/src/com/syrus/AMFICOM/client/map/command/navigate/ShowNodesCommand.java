/**
 * $Id: ShowNodesCommand.java,v 1.4 2004/10/26 13:32:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Команда включения/выключения режима отображения топологических узлов на карте 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/26 13:32:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ShowNodesCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	AbstractButton button;
	ApplicationModel aModel;

	public ShowNodesCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("button"))
			button = (AbstractButton )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public Object clone()
	{
		Command com = new ShowNodesCommand(logicalNetLayer);
		com.setParameter("button", button);
		return com;
	}

	Icon visibleIcon = new ImageIcon("images/nodes_visible.gif");
	Icon invisibleIcon = new ImageIcon("images/nodes_invisible.gif");

	public void execute()
	{
		if(MapPropertiesManager.isShowPhysicalNodes())
		{
			button.setIcon(invisibleIcon);
			MapPropertiesManager.setShowPhysicalNodes(false);
		}
		else
		if(!aModel.isSelected(MapApplicationModel.MODE_NODES))
		{
			button.setIcon(visibleIcon);
			MapPropertiesManager.setShowPhysicalNodes(true);
		}

		aModel.setSelected(MapApplicationModel.MODE_NODES, 
				MapPropertiesManager.isShowPhysicalNodes());
		aModel.fireModelChanged();
		logicalNetLayer.repaint();
	}
}
