/*
 * $Id: MapElementCharacteristicsEditor.java,v 1.2 2005/04/18 12:10:38 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.CharacteristicsPanel;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 12:10:38 $
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

	CharacteristicSort getCharacteristicSort(MapElement mapElement) {
		if(mapElement instanceof TopologicalNode)
			return CharacteristicSort.CHARACTERISTIC_SORT_TOPOLOGICAL_NODE; 
		if(mapElement instanceof SiteNode)
			return CharacteristicSort.CHARACTERISTIC_SORT_SITE_NODE; 
		if(mapElement instanceof NodeLink)
			return CharacteristicSort.CHARACTERISTIC_SORT_NODE_LINK; 
		if(mapElement instanceof PhysicalLink)
			return CharacteristicSort.CHARACTERISTIC_SORT_PHYSICAL_LINK; 
		if(mapElement instanceof Mark)
			return CharacteristicSort.CHARACTERISTIC_SORT_MARK; 
		if(mapElement instanceof Collector)
			return CharacteristicSort.CHARACTERISTIC_SORT_TOPOLOGICAL_NODE; 
//		if(mapElement instanceof CablePath)
//			return CharacteristicSort.CHARACTERISTIC_SORT_CABLELINK; 
//		if(mapElement instanceof MeasurementPath)
//			return CharacteristicSort.CHARACTERISTIC_SORT_TOPOLOGICAL_NODE; 
//		if(mapElement instanceof Marker)
//			return CharacteristicSort.CHARACTERISTIC_SORT_TOPOLOGICAL_NODE; 
//		if(mapElement instanceof UnboundLink)
//			return CharacteristicSort.CHARACTERISTIC_SORT_TOPOLOGICAL_NODE; 
//		if(mapElement instanceof Selection)
//			return CharacteristicSort.CHARACTERISTIC_SORT_TOPOLOGICAL_NODE;
		return null;
	}
	
	public void setObject(Object or) {
		this.mapElement = (MapElement) or;
		super.clear();
		
		if (this.mapElement != null) {
				super.setTypeSortMapping(
						CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						getCharacteristicSort(this.mapElement),
						this.mapElement,
						this.mapElement.getId(), 
						true);
			super.addCharacteristics(this.mapElement.getCharacteristics(), this.mapElement.getId());
		}
//		else
//			super.showNoSelection();
	}
}
