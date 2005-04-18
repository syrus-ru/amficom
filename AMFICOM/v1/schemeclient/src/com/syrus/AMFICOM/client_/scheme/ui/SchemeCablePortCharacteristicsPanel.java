/*-
 * $Id: SchemeCablePortCharacteristicsPanel.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client_.general.ui_.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemeCablePort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class SchemeCablePortCharacteristicsPanel extends CharacteristicsPanel {
	protected SchemeCablePort schemeCablePort;

	protected SchemeCablePortCharacteristicsPanel() {
		super();
	}

	protected SchemeCablePortCharacteristicsPanel(SchemeCablePort p) {
		this();
		setObject(p);
	}

	public Object getObject() {
		return schemeCablePort;
	}

	public void setObject(Object or) {
		this.schemeCablePort = (SchemeCablePort)or;
		super.clear();

		if (schemeCablePort != null) {
			super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLEPORT, schemeCablePort,
					schemeCablePort.getId(), true);
			super.addCharacteristics(schemeCablePort.getCharacteristics(), schemeCablePort.getId());
			
			Port port = schemeCablePort.getPort();
			if (port != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							CharacteristicSort.CHARACTERISTIC_SORT_CABLEPORT, port,
							port.getId(), true);
				super.addCharacteristics(port.getCharacteristics(), port.getId());
			}
			else {
				PortType portType = schemeCablePort.getPortType();
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

