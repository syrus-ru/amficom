/*
 * $Id: CableLinkTypeCharacteristicsPanel.java,v 1.1 2005/03/10 08:09:08 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:09:08 $
 * @module schemeclient_v1
 */

public class CableLinkTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected CableLinkType type;
	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	protected CableLinkTypeCharacteristicsPanel() {
		super();
	}

	protected CableLinkTypeCharacteristicsPanel(CableLinkType l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (CableLinkType) or;
		super.clear();
		
		if (type != null) {
			for (int i = 0; i < sorts.length; i++)
				super.setTypeSortMapping(sorts[i],
						CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE, type,
						type.getId(), true);
			super.addCharacteristics(type.getCharacteristics(), type.getId());
		}
		else
			showNoSelection();
	}
}
