/**
 * $Id: ShowNodesCommand.java,v 1.7 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;

/**
 * Команда включения/выключения режима отображения топологических узлов на карте 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
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
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("button"))
			this.button = (AbstractButton )value;
		if(field.equals("applicationModel"))
			this.aModel = (ApplicationModel )value;
	}

	Icon visibleIcon = new ImageIcon("images/nodes_visible.gif");
	Icon invisibleIcon = new ImageIcon("images/nodes_invisible.gif");

	public void execute()
	{
		if(MapPropertiesManager.isShowPhysicalNodes())
		{
			this.button.setIcon(this.invisibleIcon);
			MapPropertiesManager.setShowPhysicalNodes(false);
		}
		else
		if(!this.aModel.isSelected(MapApplicationModel.MODE_NODES))
		{
			this.button.setIcon(this.visibleIcon);
			MapPropertiesManager.setShowPhysicalNodes(true);
		}

		this.aModel.setSelected(MapApplicationModel.MODE_NODES, 
				MapPropertiesManager.isShowPhysicalNodes());
		this.aModel.fireModelChanged();
		this.logicalNetLayer.repaint(false);
	}
}
