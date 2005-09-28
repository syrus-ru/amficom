/*-
 * $Id: EquipmentTypeCharacteristicsPanel.java,v 1.12 2005/09/28 11:37:50 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.12 $, $Date: 2005/09/28 11:37:50 $
 * @module schemeclient
 */

public class EquipmentTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected ProtoEquipment type;

	protected EquipmentTypeCharacteristicsPanel() {
		super();
	}

	protected EquipmentTypeCharacteristicsPanel(final ProtoEquipment protoEq) {
		this();
		this.setObject(protoEq);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(final Object or) {
		this.type = (ProtoEquipment) or;
		super.clear();

		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++) {
					super.setTypeSortMapping(sorts[i], this.type, this.type.getId(), true);
				}
				super.addCharacteristics(this.type.getCharacteristics(true), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
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
