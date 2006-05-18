/*
 * $Id: CableLinkTypeCharacteristicsPanel.java,v 1.17 2006/05/03 04:49:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;


/**
 * @author $Author: stas $
 * @version $Revision: 1.17 $, $Date: 2006/05/03 04:49:00 $
 * @module schemeclient
 */

public class CableLinkTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected CableLinkType type;

	protected CableLinkTypeCharacteristicsPanel() {
		super();
	}

	protected CableLinkTypeCharacteristicsPanel(final CableLinkType l) {
		this();
		this.setObject(l);
	}

	public Object getObject() {
		return this.type;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isTypeEditionAllowed();
	}

	public void setObject(final Object or) {
		this.type = (CableLinkType) or;
		super.clear();

		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++) {
					super.setTypeSortMapping(sorts[i], this.type, this.type.getId(), isEditable());
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
