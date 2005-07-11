/*-
 * $Id: SchemeElementCharacteristicsPanel.java,v 1.7 2005/07/11 12:31:39 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
 */

public class SchemeElementCharacteristicsPanel extends CharacteristicsPanel {
	protected SchemeElement schemeElement;

	protected SchemeElementCharacteristicsPanel() {
		super();
	}

	protected SchemeElementCharacteristicsPanel(SchemeElement element) {
		this();
		setObject(element);
	}

	public Object getObject() {
		return schemeElement;
	}

	public void setObject(Object or) {
		this.schemeElement = (SchemeElement)or;
		super.clear();

		if (schemeElement != null) {
			super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					schemeElement,
					schemeElement.getId(), true);
			super.addCharacteristics(schemeElement.getCharacteristics(), schemeElement.getId());
			
			Equipment eq = schemeElement.getEquipment();
			if (eq != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							eq,
							eq.getId(), true);
				super.addCharacteristics(eq.getCharacteristics(), eq.getId());
			} else {
				EquipmentType eqt = schemeElement.getEquipmentType();
				if (eqt != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								eqt,
								eqt.getId(), false);
					super.addCharacteristics(eqt.getCharacteristics(), eqt.getId());
				}
			}
		} 
		else
			showNoSelection();
	}
}
