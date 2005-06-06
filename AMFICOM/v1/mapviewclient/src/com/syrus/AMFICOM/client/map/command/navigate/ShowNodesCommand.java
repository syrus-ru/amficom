/**
 * $Id: ShowNodesCommand.java,v 1.10 2005/06/06 12:20:31 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;

/**
 * Команда включения/выключения режима отображения топологических узлов на карте 
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class ShowNodesCommand extends MapNavigateCommand {
	AbstractButton button;

	public ShowNodesCommand(LogicalNetLayer logicalNetLayer) {
		super(logicalNetLayer);
	}

	public void setParameter(String field, Object value) {
		super.setParameter(field, value);
		if(field.equals("button"))
			this.button = (AbstractButton )value;
	}

	Icon visibleIcon = new ImageIcon("images/nodes_visible.gif");

	Icon invisibleIcon = new ImageIcon("images/nodes_invisible.gif");

	public void execute() {
		if(MapPropertiesManager.isShowPhysicalNodes()) {
			this.button.setIcon(this.invisibleIcon);
			MapPropertiesManager.setShowPhysicalNodes(false);
		}
		else
			if(!this.aModel.isSelected(MapApplicationModel.MODE_NODES)) {
				this.button.setIcon(this.visibleIcon);
				MapPropertiesManager.setShowPhysicalNodes(true);
			}

		this.aModel.setSelected(
				MapApplicationModel.MODE_NODES,
				MapPropertiesManager.isShowPhysicalNodes());
		this.aModel.fireModelChanged();
		try {
			this.logicalNetLayer.repaint(false);
		} catch(MapConnectionException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(MapDataException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
