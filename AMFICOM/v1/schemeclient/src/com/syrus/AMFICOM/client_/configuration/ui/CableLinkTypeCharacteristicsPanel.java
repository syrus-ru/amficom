/*
 * $Id: CableLinkTypeCharacteristicsPanel.java,v 1.6 2005/07/11 12:31:37 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.CableLinkType;


/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/07/11 12:31:37 $
 * @module schemeclient_v1
 */

public class CableLinkTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected CableLinkType type;

	protected CableLinkTypeCharacteristicsPanel() {
		super();
	}

	protected CableLinkTypeCharacteristicsPanel(CableLinkType l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (CableLinkType) or;
		super.clear();
		
		if (type != null) {
			for (int i = 0; i < sorts.length; i++)
				super.setTypeSortMapping(sorts[i],
						type,
						type.getId(), true);
			super.addCharacteristics(type.getCharacteristics(), type.getId());
		} else
			showNoSelection();
	}
}
