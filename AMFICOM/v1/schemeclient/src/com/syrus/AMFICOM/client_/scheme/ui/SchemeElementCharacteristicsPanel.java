/*-
 * $Id: SchemeElementCharacteristicsPanel.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client_.general.ui_.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
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
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEELEMENT, schemeElement,
					schemeElement.getId(), true);
			super.addCharacteristics(schemeElement.getCharacteristics(), schemeElement.getId());
			
			Equipment eq = schemeElement.getEquipment();
			if (eq != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT, eq,
							eq.getId(), true);
				super.addCharacteristics(eq.getCharacteristics(), eq.getId());
			}
			else {
				EquipmentType eqt = schemeElement.getEquipmentType();
				if (eqt != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE, eqt,
								eqt.getId(), false);
					super.addCharacteristics(eqt.getCharacteristics(), eqt.getId());
				}
			}
		} 
		else
			showNoSelection();
	}
}
