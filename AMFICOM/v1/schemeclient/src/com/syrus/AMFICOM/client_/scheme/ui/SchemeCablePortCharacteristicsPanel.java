/*-
 * $Id: SchemeCablePortCharacteristicsPanel.java,v 1.14 2005/10/31 12:30:28 bass Exp $
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
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/10/31 12:30:28 $
 * @module schemeclient
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
		return this.schemeCablePort;
	}

	public void setObject(Object or) {
		this.schemeCablePort = (SchemeCablePort)or;
		super.clear();

		if (this.schemeCablePort != null) {
			try {
				super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeCablePort,
						this.schemeCablePort.getId(), true);
				super.addCharacteristics(this.schemeCablePort.getCharacteristics(true), this.schemeCablePort.getId());
				Port port = this.schemeCablePort.getPort();
				if (port != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								port,
								port.getId(), true);
					super.addCharacteristics(port.getCharacteristics(true), port.getId());
				} else {
					PortType portType = this.schemeCablePort.getPortType();
					if (portType != null) {
						for (int i = 0; i < sorts.length; i++)
							super.setTypeSortMapping(sorts[i],
									portType,
									portType.getId(), false);
						super.addCharacteristics(portType.getCharacteristics(true), portType.getId());
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

