/*
 * $Id: MeasurementTypeCharacteristicsPanel.java,v 1.5 2005/08/08 11:58:06 arseniy Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
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
