/*
 * $Id: LinkTypeCharacteristicsPanel.java,v 1.15 2005/10/31 12:30:25 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/10/31 12:30:25 $
 * @module schemeclient
 */

public class LinkTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected LinkType type;

	protected LinkTypeCharacteristicsPanel() {
		super();
	}

	protected LinkTypeCharacteristicsPanel(final LinkType linkType) {
		this();
		this.setObject(linkType);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(final Object or) {
		this.type = (LinkType) or;
		super.clear();

		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++) {
					super.setTypeSortMapping(sorts[i], this.type, this.type.getId(), true);
				}
				super.addCharacteristics(this.type.getCharacteristics(true), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorMessage(e);
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
