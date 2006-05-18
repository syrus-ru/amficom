/*-
 * $Id: ProtoEquipmentCharacteristicsPanel.java,v 1.5.2.1 2006/05/18 17:50:00 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5.2.1 $, $Date: 2006/05/18 17:50:00 $
 * @module schemeclient
 */

public class ProtoEquipmentCharacteristicsPanel extends CharacteristicsPanel {
	protected ProtoEquipment type;

	protected ProtoEquipmentCharacteristicsPanel() {
		super();
	}

	protected ProtoEquipmentCharacteristicsPanel(final ProtoEquipment protoEq) {
		this();
		this.setObject(protoEq);
	}

	public Object getObject() {
		return this.type;
	}

	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isEditionAllowed();
	}
	
	public void setObject(final Object or) {
		this.type = (ProtoEquipment) or;
		super.clear();

		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++) {
					super.setTypeSortMapping(sorts[i], this.type, this.type.getId(), isEditable());
				}
				super.addCharacteristics(this.type.getCharacteristics(), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				super.showNoSelection();
			}
		} else {
			super.showNoSelection();
		}
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.type != null) {
			super.save();
		}
	}
}
