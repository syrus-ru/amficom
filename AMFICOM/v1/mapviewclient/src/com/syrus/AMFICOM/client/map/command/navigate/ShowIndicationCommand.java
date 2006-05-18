/*-
 * $$Id: ShowIndicationCommand.java,v 1.6 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 *  
 * @version $Revision: 1.6 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
		if(field.equals("button")) //$NON-NLS-1$
			this.button = (AbstractButton )value;
	}

	Icon visibleIcon = new ImageIcon("images/indication_visible.gif"); //$NON-NLS-1$

	Icon invisibleIcon = new ImageIcon("images/indication_invisible.gif"); //$NON-NLS-1$

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
