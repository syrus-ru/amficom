/*
 * $Id: MeasurementPortTypeCharacteristicsPanel.java,v 1.17.2.1 2006/05/18 17:50:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.17.2.1 $, $Date: 2006/05/18 17:50:00 $
 * @module schemeclient
 */

public class MeasurementPortTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected MeasurementPortType type;

	protected MeasurementPortTypeCharacteristicsPanel() {
		super();
	}

	protected MeasurementPortTypeCharacteristicsPanel(final MeasurementPortType measurementPortType) {
		this();
		this.setObject(measurementPortType);
	}

	public Object getObject() {
		return this.type;
	}

	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isTypeEditionAllowed();
	}
	
	public void setObject(final Object or) {
		this.type = (MeasurementPortType) or;
		super.clear();

		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++) {
					super.setTypeSortMapping(sorts[i], this.type, this.type.getId(), isEditable());
				}
				super.addCharacteristics(this.type.getCharacteristics(), this.type.getId());
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
