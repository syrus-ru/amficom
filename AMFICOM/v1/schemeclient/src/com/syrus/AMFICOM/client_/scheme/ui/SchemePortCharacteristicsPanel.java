/*-
 * $Id: SchemePortCharacteristicsPanel.java,v 1.1 2005/04/18 10:45:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client_.general.ui_.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemePort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:18 $
 * @module schemeclient_v1
 */

public class SchemePortCharacteristicsPanel extends CharacteristicsPanel {
	protected SchemePort schemePort;

	protected SchemePortCharacteristicsPanel() {
		super();
	}

	protected SchemePortCharacteristicsPanel(SchemePort p) {
		this();
		setObject(p);
	}

	public Object getObject() {
		return schemePort;
	}

	public void setObject(Object or) {
		this.schemePort = (SchemePort)or;
		super.clear();

		if (schemePort != null) {
			super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPORT, schemePort,
					schemePort.getId(), true);
			super.addCharacteristics(schemePort.getCharacteristics(), schemePort.getId());
			
			Port port = schemePort.getPort();
			if (port != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							CharacteristicSort.CHARACTERISTIC_SORT_PORT, port,
							port.getId(), true);
				super.addCharacteristics(port.getCharacteristics(), port.getId());
			}
			else {
				PortType portType = schemePort.getPortType();
				if (portType != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								CharacteristicSort.CHARACTERISTIC_SORT_PORTTYPE, portType,
								portType.getId(), false);
					super.addCharacteristics(portType.getCharacteristics(), portType.getId());
				}
			}
		} 
		else
			showNoSelection();
	}
}
