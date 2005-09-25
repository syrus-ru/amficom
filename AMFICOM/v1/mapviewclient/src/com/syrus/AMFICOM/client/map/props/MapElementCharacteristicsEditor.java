/*
 * $Id: MapElementCharacteristicsEditor.java,v 1.20 2005/09/25 16:05:46 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.20 $, $Date: 2005/09/25 16:05:46 $
 * @module mapviewclient
 */

public class MapElementCharacteristicsEditor extends CharacteristicsPanel {
	protected Object object;

	protected MapElementCharacteristicsEditor() {
		super();
	}

	protected MapElementCharacteristicsEditor(MapElement mapElement) {
		this();
		setObject(mapElement);
	}

	public Object getObject() {
		return this.object;
	}

	public void setObject(Object object) {
		this.object = object;
		super.clear();

		Characterizable characterizable = null;

		if(object instanceof MapElement) {
			MapElement mapElement = (MapElement) object;
			characterizable = mapElement.getCharacterizable();
		}
		else if(object instanceof Characterizable) {
			characterizable = (Characterizable) object;
		}
			
		if(characterizable != null) {
				super.setTypeSortMapping(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
						characterizable,
						characterizable.getId(), 
						true);
			try {
				super.addCharacteristics(characterizable.getCharacteristics(true), characterizable.getId());
			} catch(ApplicationException e) {
				Log.debugException(e, Level.WARNING);
			}
		}
		else
			super.showNoSelection();
	}

}
