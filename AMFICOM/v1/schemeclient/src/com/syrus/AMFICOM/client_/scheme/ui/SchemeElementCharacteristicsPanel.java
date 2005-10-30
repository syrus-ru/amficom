/*-
 * $Id: SchemeElementCharacteristicsPanel.java,v 1.15 2005/10/30 15:20:55 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/10/30 15:20:55 $
 * @module schemeclient
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
		return this.schemeElement;
	}

	public void setObject(Object or) {
		this.schemeElement = (SchemeElement)or;
		super.clear();

		if (this.schemeElement != null) {
			try {
				super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeElement,
						this.schemeElement.getId(), true);
				super.addCharacteristics(this.schemeElement.getCharacteristics(true), this.schemeElement.getId());
				Equipment eq = null;
				try {
					eq = this.schemeElement.getEquipment();
				} catch (IllegalStateException e) {
					// ignore as it means no Equipment created
				}
				if (eq != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								eq,
								eq.getId(), true);
					super.addCharacteristics(eq.getCharacteristics(true), eq.getId());
				} else {
					ProtoEquipment protoEq = null;
					try {
						protoEq = this.schemeElement.getProtoEquipment();
					} catch (IllegalStateException e) {
						assert Log.debugMessage("No EqT set for SE '" + this.schemeElement.getId() + "'", Level.FINE);
					}
					if (protoEq != null) {
						for (int i = 0; i < sorts.length; i++)
							super.setTypeSortMapping(sorts[i],
									protoEq,
									protoEq.getId(), false);
						super.addCharacteristics(protoEq.getCharacteristics(true), protoEq.getId());
					}
				}
			} catch (ApplicationException e) {
				assert Log.errorMessage(e);
				showNoSelection();
			}
		} 
		else
			showNoSelection();
	}
}
