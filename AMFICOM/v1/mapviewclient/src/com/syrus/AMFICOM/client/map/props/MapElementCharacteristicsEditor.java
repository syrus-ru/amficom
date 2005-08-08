/*
 * $Id: MapElementCharacteristicsEditor.java,v 1.15 2005/08/08 10:31:40 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.util.Log;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/08/08 10:31:40 $
 * @module mapviewclient_v1
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

		if(object instanceof MapElement) {
			MapElement mapElement = (MapElement) object;
			
			if (mapElement != null
					&& !(mapElement instanceof Selection)) {
					super.setTypeSortMapping(
							CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
							mapElement,
							mapElement.getId(), 
							true);
				try {
					super.addCharacteristics(mapElement.getCharacteristics(), mapElement.getId());
				} catch(ApplicationException e) {
					Log.debugException(e, Level.WARNING);
				}
			}
			else
				super.showNoSelection();
		}
		else if(object instanceof SiteNodeType) {
			SiteNodeType siteNodeType = (SiteNodeType)object;
			super.setTypeSortMapping(
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					siteNodeType,
					siteNodeType.getId(), 
					true);
			try {
				super.addCharacteristics(siteNodeType.getCharacteristics(), siteNodeType.getId());
			} catch(ApplicationException e) {
				Log.debugException(e, Level.WARNING);
			}
		}
		else if(object instanceof PhysicalLinkType) {
			PhysicalLinkType physicalLinkType = (PhysicalLinkType)object;
			super.setTypeSortMapping(
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					physicalLinkType,
					physicalLinkType.getId(), 
					true);
			try {
				super.addCharacteristics(physicalLinkType.getCharacteristics(), physicalLinkType.getId());
			} catch(ApplicationException e) {
				Log.debugException(e, Level.WARNING);
			}
		}
	}

}
