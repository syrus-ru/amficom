/*
 * $Id: MeasurementTypeCharacteristicsPanel.java,v 1.4 2005/08/05 12:39:58 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/08/05 12:39:58 $
 * @module schemeclient_v1
 */

public class MeasurementTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected MeasurementType type;

	protected MeasurementTypeCharacteristicsPanel() {
		super();
	}

	protected MeasurementTypeCharacteristicsPanel(MeasurementType l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(Object or) {
		this.type = (MeasurementType) or;
		showNoSelection();
	}
}
