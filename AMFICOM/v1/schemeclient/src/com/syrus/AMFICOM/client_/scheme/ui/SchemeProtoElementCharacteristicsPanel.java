/*-
 * $Id: SchemeProtoElementCharacteristicsPanel.java,v 1.6 2005/07/11 12:31:39 stas Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
 */

public class SchemeProtoElementCharacteristicsPanel extends CharacteristicsPanel {
	protected SchemeProtoElement schemeProtoElement;

	protected SchemeProtoElementCharacteristicsPanel() {
		super();
	}

	protected SchemeProtoElementCharacteristicsPanel(SchemeProtoElement element) {
		this();
		setObject(element);
	}

	public Object getObject() {
		return schemeProtoElement;
	}

	public void setObject(Object or) {
		this.schemeProtoElement = (SchemeProtoElement)or;
		super.clear();

		if (schemeProtoElement != null) {
			super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					schemeProtoElement,
					schemeProtoElement.getId(), true);
			super.addCharacteristics(schemeProtoElement.getCharacteristics(), schemeProtoElement.getId());

			EquipmentType eqt = schemeProtoElement.getEquipmentType();
			if (eqt != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							eqt, eqt.getId(), false);
				super.addCharacteristics(eqt.getCharacteristics(), eqt.getId());
			}
		} 
		else
			showNoSelection();
	}
}
