/*
 * $Id: PortTypeCharacteristicsPanel.java,v 1.7 2005/08/05 12:39:58 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/08/05 12:39:58 $
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
		return this.type;
	}

	public void setObject(Object or) {
		this.type = (PortType) or;
		super.clear();
		
		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							this.type,
							this.type.getId(), true);
				super.addCharacteristics(this.type.getCharacteristics(), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} else
			showNoSelection();
	}
}
