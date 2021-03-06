/*-
 * $$Id: MapElementCharacteristicsEditor.java,v 1.28 2006/06/08 10:25:46 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.28 $, $Date: 2006/06/08 10:25:46 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */

public class MapElementCharacteristicsEditor extends CharacteristicsPanel<MapElement> {
	protected MapElement object;

	protected MapElementCharacteristicsEditor() {
		super();
	}

	protected MapElementCharacteristicsEditor(MapElement mapElement) {
		this();
		setObject(mapElement);
	}

	public MapElement getObject() {
		return this.object;
	}

	public void setObject(MapElement object) {
		this.object = object;
		super.clear();

		Characterizable characterizable = null;

		MapElement mapElement = object;
		characterizable = mapElement.getCharacterizable();
			
		if(characterizable != null) {
				super.setTypeSortMapping(
						IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
						characterizable,
						characterizable.getId(), 
						isEditable());
			try {
				super.addCharacteristics(characterizable.getCharacteristics(true), characterizable.getId());
			} catch(ApplicationException e) {
				Log.debugMessage(e, Level.WARNING);
			}
		}
		else
			super.showNoSelection();
	}

}
