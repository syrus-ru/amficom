/*
 * $Id: MeasurementPortTypeCharacteristicsPanel.java,v 1.18 2006/06/06 12:42:06 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.18 $, $Date: 2006/06/06 12:42:06 $
 * @module schemeclient
 */

public class MeasurementPortTypeCharacteristicsPanel extends CharacteristicsPanel<MeasurementPortType> {
	protected MeasurementPortType type;

	protected MeasurementPortTypeCharacteristicsPanel() {
		super();
	}

	protected MeasurementPortTypeCharacteristicsPanel(final MeasurementPortType measurementPortType) {
		this();
		this.setObject(measurementPortType);
	}

	public MeasurementPortType getObject() {
		return this.type;
	}

	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT_TYPE);
	}
	
	public void setObject(final MeasurementPortType or) {
		this.type = or;
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
