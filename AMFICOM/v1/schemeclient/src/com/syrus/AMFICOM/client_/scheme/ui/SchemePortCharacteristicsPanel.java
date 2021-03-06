/*-
 * $Id: SchemePortCharacteristicsPanel.java,v 1.18 2006/06/06 12:41:55 stas Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.18 $, $Date: 2006/06/06 12:41:55 $
 * @module schemeclient
 */

public class SchemePortCharacteristicsPanel extends CharacteristicsPanel<SchemePort> {
	protected SchemePort schemePort;

	protected SchemePortCharacteristicsPanel() {
		super();
	}

	protected SchemePortCharacteristicsPanel(SchemePort p) {
		this();
		setObject(p);
	}

	public SchemePort getObject() {
		return this.schemePort;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT);
	}

	public void setObject(SchemePort or) {
		this.schemePort = or;

		super.clear();
		if (this.schemePort != null) {
			try {
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemePort,
						this.schemePort.getId(), isEditable());
				super.addCharacteristics(this.schemePort.getCharacteristics(true), this.schemePort.getId());
				Port port = this.schemePort.getPort();
				if (port != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								port,
								port.getId(), true);
					super.addCharacteristics(port.getCharacteristics(true), port.getId());
				} else {
					PortType portType = this.schemePort.getPortType();
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
