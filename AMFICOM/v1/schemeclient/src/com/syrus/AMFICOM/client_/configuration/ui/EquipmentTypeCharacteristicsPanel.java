/*-
 * $Id: EquipmentTypeCharacteristicsPanel.java,v 1.10 2005/09/06 12:45:57 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2005/09/06 12:45:57 $
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
				super.addCharacteristics(this.type.getCharacteristics(true), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} else
			showNoSelection();
	}
	
	public void commitChanges() {
		super.commitChanges();
		if (this.type != null) {
			save();
		}
	}
}
