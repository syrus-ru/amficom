/*-
 * $Id: SchemePathCharacteristicsPanel.java,v 1.7 2006/06/06 12:41:55 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class SchemePathCharacteristicsPanel extends CharacteristicsPanel<SchemePath> {
	protected SchemePath schemePath;

	protected SchemePathCharacteristicsPanel() {
		super();
	}

	protected SchemePathCharacteristicsPanel(SchemePath schemePath) {
		this();
		setObject(schemePath);
	}

	public SchemePath getObject() {
		return this.schemePath;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT);
	}

	public void setObject(SchemePath or) {
		this.schemePath = or;
		super.clear();

		if (this.schemePath != null) {
			try {
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemePath,
						this.schemePath.getId(), isEditable());
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
