/*
 * $Id: MeasurementTypeCharacteristicsPanel.java,v 1.8 2005/09/07 03:02:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/09/07 03:02:53 $
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
