/**
 * $Id: ShowNodesCommand.java,v 1.6 2005/01/12 15:45:53 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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
 * ������� ���������/���������� ������ ����������� �������������� ����� �� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2005/01/12 15:45:53 $
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
		logicalNetLayer.repaint(false);
	}
}
