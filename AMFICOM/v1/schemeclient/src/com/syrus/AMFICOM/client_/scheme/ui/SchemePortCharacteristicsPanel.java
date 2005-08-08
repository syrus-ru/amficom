/*-
 * $Id: SchemePortCharacteristicsPanel.java,v 1.10 2005/08/08 11:58:08 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/08/08 11:58:08 $
 * @module schemeclient
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
		return this.schemePort;
	}

	public void setObject(Object or) {
		this.schemePort = (SchemePort)or;
		super.clear();

		if (this.schemePort != null) {
			try {
				super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemePort,
						this.schemePort.getId(), true);
				super.addCharacteristics(this.schemePort.getCharacteristics(), this.schemePort.getId());
				Port port = this.schemePort.getPort();
				if (port != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								port,
								port.getId(), true);
					super.addCharacteristics(port.getCharacteristics(), port.getId());
				} else {
					PortType portType = this.schemePort.getPortType();
					if (portType != null) {
						for (int i = 0; i < sorts.length; i++)
							super.setTypeSortMapping(sorts[i],
									portType,
									portType.getId(), false);
						super.addCharacteristics(portType.getCharacteristics(), portType.getId());
					}
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
