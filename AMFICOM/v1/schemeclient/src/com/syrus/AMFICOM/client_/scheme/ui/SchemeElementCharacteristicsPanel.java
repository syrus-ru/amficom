/*-
 * $Id: SchemeElementCharacteristicsPanel.java,v 1.19 2006/06/06 12:41:55 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.19 $, $Date: 2006/06/06 12:41:55 $
 * @module schemeclient
 */

public class SchemeElementCharacteristicsPanel extends CharacteristicsPanel<SchemeElement> {
	protected SchemeElement schemeElement;

	protected SchemeElementCharacteristicsPanel() {
		super();
	}

	protected SchemeElementCharacteristicsPanel(SchemeElement element) {
		this();
		setObject(element);
	}

	public SchemeElement getObject() {
		return this.schemeElement;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT);
	}

	public void setObject(SchemeElement or) {
		this.schemeElement = or;
		super.clear();

		if (this.schemeElement != null) {
			try {
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeElement,
						this.schemeElement.getId(), isEditable());
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
								eq.getId(), isEditable());
					super.addCharacteristics(eq.getCharacteristics(true), eq.getId());
				} else {
					ProtoEquipment protoEq = null;
					try {
						protoEq = this.schemeElement.getProtoEquipment();
					} catch (IllegalStateException e) {
						Log.debugMessage("No EqT set for SE '" + this.schemeElement.getId() + "'", Level.FINE);
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
				Log.errorMessage(e);
				showNoSelection();
			}
		} 
		else
			showNoSelection();
	}
}
