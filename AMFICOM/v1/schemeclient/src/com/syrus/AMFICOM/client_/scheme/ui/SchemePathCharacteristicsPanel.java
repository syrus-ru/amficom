/*-
 * $Id: SchemePathCharacteristicsPanel.java,v 1.5 2005/12/06 11:40:35 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class SchemePathCharacteristicsPanel extends CharacteristicsPanel {
	protected SchemePath schemePath;

	protected SchemePathCharacteristicsPanel() {
		super();
	}

	protected SchemePathCharacteristicsPanel(SchemePath schemePath) {
		this();
		setObject(schemePath);
	}

	public Object getObject() {
		return this.schemePath;
	}

	public void setObject(Object or) {
		this.schemePath = (SchemePath)or;
		super.clear();

		if (this.schemePath != null) {
			try {
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemePath,
						this.schemePath.getId(), true);
				super.addCharacteristics(this.schemePath.getCharacteristics(true), this.schemePath.getId());
				TransmissionPath tp = null;
				try {
					tp = this.schemePath.getTransmissionPath();
				} catch (IllegalStateException e) {
					// ignore as it means no TransmissionPath created
				}
				if (tp != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								tp,
								tp.getId(), true);
					super.addCharacteristics(tp.getCharacteristics(true), tp.getId());
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				showNoSelection();
			}
		} 
		else
			showNoSelection();
	}
}
