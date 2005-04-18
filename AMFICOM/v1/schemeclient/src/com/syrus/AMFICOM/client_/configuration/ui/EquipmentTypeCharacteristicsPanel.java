/*-
 * $Id: EquipmentTypeCharacteristicsPanel.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client_.general.ui_.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class EquipmentTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected EquipmentType type;

	protected EquipmentTypeCharacteristicsPanel() {
		super();
	}

	protected EquipmentTypeCharacteristicsPanel(EquipmentType eqt) {
		this();
		setObject(eqt);
	}

	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (EquipmentType) or;
		super.clear();
		
		if (type != null) {
			for (int i = 0; i < sorts.length; i++)
				super.setTypeSortMapping(sorts[i],
						CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE, type,
						type.getId(), true);
			super.addCharacteristics(type.getCharacteristics(), type.getId());
		}
		else
			showNoSelection();
	}
}
