/*-
 * $Id: EquipmentTypeCharacteristicsPanel.java,v 1.11 2005/09/07 03:02:53 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/09/07 03:02:53 $
 * @module schemeclient
 */

public class EquipmentTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected EquipmentType type;

	protected EquipmentTypeCharacteristicsPanel() {
		super();
	}

	protected EquipmentTypeCharacteristicsPanel(final EquipmentType eqt) {
		this();
		this.setObject(eqt);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(final Object or) {
		this.type = (EquipmentType) or;
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
