/*
 * $Id: PortTypeCharacteristicsPanel.java,v 1.12 2005/09/07 03:02:53 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/09/07 03:02:53 $
 * @module schemeclient
 */

public class PortTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected PortType type;

	protected PortTypeCharacteristicsPanel() {
		super();
	}

	protected PortTypeCharacteristicsPanel(final PortType portType) {
		this();
		this.setObject(portType);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(final Object or) {
		this.type = (PortType) or;
		super.clear();

		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++) {
					super.setTypeSortMapping(sorts[i], this.type, this.type.getId(), true);
				}
				super.addCharacteristics(this.type.getCharacteristics(true), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				super.showNoSelection();
			}
		} else {
			super.showNoSelection();
		}
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.type != null) {
			super.save();
		}
	}
}
