/*
 * $Id: MeasurementPortTypeCharacteristicsPanel.java,v 1.11 2005/08/20 19:58:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/08/20 19:58:10 $
 * @module schemeclient
 */

public class MeasurementPortTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected MeasurementPortType type;

	protected MeasurementPortTypeCharacteristicsPanel() {
		super();
	}

	protected MeasurementPortTypeCharacteristicsPanel(MeasurementPortType l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(Object or) {
		this.type = (MeasurementPortType) or;
		super.clear();
		
		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							this.type,
							this.type.getId(), true);
				super.addCharacteristics(this.type.getCharacteristics(true), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} else
			showNoSelection();
	}
	
	public void commitChanges() {
		super.commitChanges();
		if (this.type != null) {
			save();
		}
	}
}
