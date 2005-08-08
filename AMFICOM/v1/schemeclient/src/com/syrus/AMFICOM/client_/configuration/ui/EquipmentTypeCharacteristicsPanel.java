/*-
 * $Id: EquipmentTypeCharacteristicsPanel.java,v 1.7 2005/08/08 11:58:06 arseniy Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
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
		return this.type;
	}

	public void setObject(Object or) {
		this.type = (EquipmentType) or;
		super.clear();
		
		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							this.type,
							this.type.getId(), true);
				super.addCharacteristics(this.type.getCharacteristics(), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} else
			showNoSelection();
	}
}
