/*-
 * $Id: SchemePortCharacteristicsPanel.java,v 1.16 2005/12/06 11:40:35 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.Set;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/12/06 11:40:35 $
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
		if (or instanceof Set) {
			this.schemePort = null;
		} else {
			this.schemePort = (SchemePort)or;
		}
		super.clear();
		if (this.schemePort != null) {
			try {
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemePort,
						this.schemePort.getId(), true);
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
