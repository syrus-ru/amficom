/**
 * $Id: MapNodeElementState.java,v 1.5 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.HashMap;

/**
 * состояние элемента - узла 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapNodeElementState extends MapElementState
{
	String name;
	String description;
	String imageId;
	DoublePoint location;
	String optimizerAttribute;
	java.util.Map attributes = new HashMap();
	
	public MapNodeElementState(MapNodeElement mne)
	{
		super();
		name = mne.getName();
		description = mne.getDescription();
		imageId = mne.getImageId();
		location = (DoublePoint )mne.getLocation().clone();
		optimizerAttribute = mne.optimizerAttribute;

		attributes.putAll(mne.attributes);
	}

	public boolean equals(Object obj)
	{
		MapNodeElementState mnes = (MapNodeElementState )obj;
		return (this.name.equals(mnes.name)
			&& this.description.equals(mnes.description)
			&& this.imageId.equals(mnes.imageId)
			&& this.location.equals(mnes.location)
			&& this.optimizerAttribute.equals(mnes.optimizerAttribute));
	}
}
