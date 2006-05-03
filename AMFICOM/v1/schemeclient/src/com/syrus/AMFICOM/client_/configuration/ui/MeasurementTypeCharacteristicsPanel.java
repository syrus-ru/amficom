/*
 * $Id: MeasurementTypeCharacteristicsPanel.java,v 1.9 2006/05/03 04:49:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2006/05/03 04:49:00 $
 * @module schemeclient
 */

public class MeasurementTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected MeasurementType type;

	protected MeasurementTypeCharacteristicsPanel() {
		super();
	}

	protected MeasurementTypeCharacteristicsPanel(final MeasurementType measurementType) {
		this();
		this.setObject(measurementType);
	}

	public Object getObject() {
		return this.type;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isTypeEditionAllowed();
	}

	public void setObject(final Object or) {
		this.type = (MeasurementType) or;
		super.showNoSelection();
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.type != null) {
			super.save();
		}
	}
}
