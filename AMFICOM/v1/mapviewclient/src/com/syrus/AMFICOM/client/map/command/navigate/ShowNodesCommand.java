/**
 * $Id: ShowNodesCommand.java,v 1.13 2005/08/17 14:14:19 arseniy Exp $
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

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;

/**
 * Команда включения/выключения режима отображения топологических узлов на карте 
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2005/08/17 14:14:19 $
 * @module mapviewclient
 */
public class ShowNodesCommand extends MapNavigateCommand {
	AbstractButton button;

	public ShowNodesCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
	public void setParameter(String field, Object value) {
		super.setParameter(field, value);
		if(field.equals("button"))
			this.button = (AbstractButton )value;
	}

	Icon visibleIcon = new ImageIcon("images/nodes_visible.gif");

	Icon invisibleIcon = new ImageIcon("images/nodes_invisible.gif");

	@Override
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
			this.netMapViewer.repaint(false);
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
