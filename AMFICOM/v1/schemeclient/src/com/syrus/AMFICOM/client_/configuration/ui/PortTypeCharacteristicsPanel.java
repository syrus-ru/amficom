/*
 * $Id: PortTypeCharacteristicsPanel.java,v 1.4 2005/05/26 15:31:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.PortType;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/26 15:31:13 $
 * @module schemeclient_v1
 */

public class PortTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected PortType type;

	protected PortTypeCharacteristicsPanel() {
		super();
	}

	protected PortTypeCharacteristicsPanel(PortType l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (PortType) or;
		super.clear();
		
		if (type != null) {
			for (int i = 0; i < sorts.length; i++)
				super.setTypeSortMapping(sorts[i],
						type,
						type.getId(), true);
			super.addCharacteristics(type.getCharacteristics(), type.getId());
		}
		else
			showNoSelection();
	}
}
