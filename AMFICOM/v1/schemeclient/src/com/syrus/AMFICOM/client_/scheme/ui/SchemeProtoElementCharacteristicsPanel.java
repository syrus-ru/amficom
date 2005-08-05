/*-
 * $Id: SchemeProtoElementCharacteristicsPanel.java,v 1.8 2005/08/05 12:40:00 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2005/08/05 12:40:00 $
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
		return this.schemeProtoElement;
	}

	public void setObject(Object or) {
		this.schemeProtoElement = (SchemeProtoElement)or;
		super.clear();

		if (this.schemeProtoElement != null) {
			try {
				super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeProtoElement,
						this.schemeProtoElement.getId(), true);
				super.addCharacteristics(this.schemeProtoElement.getCharacteristics(), this.schemeProtoElement.getId());
				EquipmentType eqt = this.schemeProtoElement.getEquipmentType();
				if (eqt != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								eqt, eqt.getId(), false);
					super.addCharacteristics(eqt.getCharacteristics(), eqt.getId());
				}
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} 
		else
			showNoSelection();
	}
}
