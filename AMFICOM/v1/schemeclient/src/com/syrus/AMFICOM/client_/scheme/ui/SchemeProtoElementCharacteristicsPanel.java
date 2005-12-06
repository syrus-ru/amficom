/*-
 * $Id: SchemeProtoElementCharacteristicsPanel.java,v 1.15 2005/12/06 11:40:35 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/12/06 11:40:35 $
 * @module schemeclient
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
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeProtoElement,
						this.schemeProtoElement.getId(), true);
				super.addCharacteristics(this.schemeProtoElement.getCharacteristics(true), this.schemeProtoElement.getId());
				ProtoEquipment protoEq = this.schemeProtoElement.getProtoEquipment();
				if (protoEq != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								protoEq, protoEq.getId(), false);
					super.addCharacteristics(protoEq.getCharacteristics(true), protoEq.getId());
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
