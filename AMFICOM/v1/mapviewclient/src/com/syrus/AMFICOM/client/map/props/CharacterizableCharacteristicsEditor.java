/*-
 * $Id: CharacterizableCharacteristicsEditor.java,v 1.1 2006/06/08 10:26:34 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.util.Log;

public class CharacterizableCharacteristicsEditor extends CharacteristicsPanel<Characterizable> {
	protected Characterizable characterizable;

	protected CharacterizableCharacteristicsEditor() {
		super();
	}

	protected CharacterizableCharacteristicsEditor(Characterizable characterizable) {
		this();
		setObject(characterizable);
	}

	public Characterizable getObject() {
		return this.characterizable;
	}

	public void setObject(Characterizable object) {
		this.characterizable = object;
		super.clear();
			
		if(this.characterizable != null) {
				super.setTypeSortMapping(
						IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
						this.characterizable,
						this.characterizable.getId(), 
						isEditable());
			try {
				super.addCharacteristics(this.characterizable.getCharacteristics(true), this.characterizable.getId());
			} catch(ApplicationException e) {
				Log.debugMessage(e, Level.WARNING);
			}
		}
		else
			super.showNoSelection();
	}

}
