/*-
 * $Id: EquipmentTypeCharacteristicsPanel.java,v 1.4 2005/06/17 11:36:21 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.EquipmentType;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/06/17 11:36:21 $
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
						type,
						type.getId(), true);
			super.addCharacteristics(type.getCharacteristics(), type.getId());
		} else
			showNoSelection();
	}
}
