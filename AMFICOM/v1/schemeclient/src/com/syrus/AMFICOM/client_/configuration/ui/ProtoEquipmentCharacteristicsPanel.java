/*-
 * $Id: ProtoEquipmentCharacteristicsPanel.java,v 1.6 2006/06/06 12:42:06 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2006/06/06 12:42:06 $
 * @module schemeclient
 */

public class ProtoEquipmentCharacteristicsPanel extends CharacteristicsPanel<ProtoEquipment> {
	protected ProtoEquipment type;

	protected ProtoEquipmentCharacteristicsPanel() {
		super();
	}

	protected ProtoEquipmentCharacteristicsPanel(final ProtoEquipment protoEq) {
		this();
		this.setObject(protoEq);
	}

	public ProtoEquipment getObject() {
		return this.type;
	}

	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT);
	}
	
	public void setObject(final ProtoEquipment or) {
		this.type = or;
		super.clear();

		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++) {
					super.setTypeSortMapping(sorts[i], this.type, this.type.getId(), isEditable());
				}
				super.addCharacteristics(this.type.getCharacteristics(true), this.type.getId());
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
