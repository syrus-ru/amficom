/*
 * $Id: MapElementCharacteristicsEditor.java,v 1.8 2005/05/26 15:31:14 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.CharacteristicsPanel;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/05/26 15:31:14 $
 * @module schemeclient_v1
 */

public class MapElementCharacteristicsEditor extends CharacteristicsPanel {
	protected MapElement mapElement;

	protected MapElementCharacteristicsEditor() {
		super();
	}

	protected MapElementCharacteristicsEditor(MapElement mapElement) {
		this();
		setObject(mapElement);
	}

	public Object getObject() {
		return this.mapElement;
	}
	
	public void setObject(Object or) {
		this.mapElement = (MapElement) or;
		super.clear();
		
		if (this.mapElement != null
				&& !(this.mapElement instanceof Selection)) {
				super.setTypeSortMapping(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.mapElement,
						this.mapElement.getId(), 
						true);
			super.addCharacteristics(this.mapElement.getCharacteristics(), this.mapElement.getId());
		}
		else
			super.showNoSelection();
	}

//	void setCharacteristicValue(Collection characteristics, String name,
//			String value) {
//		for (Iterator it = characteristics.iterator(); it.hasNext();) {
//			Characteristic ch = (Characteristic) it.next();
//			if (ch.getName().equals(name)) {
//				if(ch.getCharacterizableId().equals(this.mapElement.getId()))
//					ch.setValue(value);
//				else {
//					PhysicalLinkController controller = (PhysicalLinkController )PhysicalLinkController.getInstance();
//					if(ch.getType().getCodename().equals(controller.ATTRIBUTE_COLOR))
//						controller.setColor(this.mapElement, null);
//				}
//				break;
//			}
//		}
//	}

}
