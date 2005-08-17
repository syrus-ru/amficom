/**
 * $Id: ShowIndicationCommand.java,v 1.4 2005/08/17 14:14:19 arseniy Exp $
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

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModel;

/**
 * Команда включения/выключения режима отображения топологических узлов на карте 
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/17 14:14:19 $
 * @module mapviewclient
 */
public class ShowIndicationCommand extends MapNavigateCommand {
	AbstractButton button;

	public ShowIndicationCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
	public void setParameter(String field, Object value) {
		super.setParameter(field, value);
		if(field.equals("button"))
			this.button = (AbstractButton )value;
	}

	Icon visibleIcon = new ImageIcon("images/indication_visible.gif");

	Icon invisibleIcon = new ImageIcon("images/indication_invisible.gif");

	@Override
	public void execute() {
		if(MapPropertiesManager.isShowAlarmIndication()) {
			this.button.setIcon(this.invisibleIcon);
			MapPropertiesManager.setShowAlarmIndication(false);
		}
		else
			if(!this.aModel.isSelected(MapApplicationModel.MODE_INDICATION)) {
				this.button.setIcon(this.visibleIcon);
				MapPropertiesManager.setShowAlarmIndication(true);
			}

		this.aModel.setSelected(
				MapApplicationModel.MODE_INDICATION,
				MapPropertiesManager.isShowAlarmIndication());
		this.aModel.fireModelChanged();
	}
}
