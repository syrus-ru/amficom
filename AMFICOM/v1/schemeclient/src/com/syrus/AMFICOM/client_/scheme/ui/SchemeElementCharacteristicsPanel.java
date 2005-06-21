/*-
 * $Id: SchemeElementCharacteristicsPanel.java,v 1.6 2005/06/21 12:52:14 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/06/21 12:52:14 $
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
