/**
 * $Id: MapNodeElementState.java,v 1.3 2004/10/26 13:25:36 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import java.util.HashMap;

/**
 * состояние элемента - узла 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/26 13:25:36 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapNodeElementState extends MapElementState
{
	Rectangle bounds;
	double scaleCoefficient;
	String name;
	String description;
	Image icon;
	String imageId;
	Point2D.Double anchor;
	String optimizerAttribute;
	java.util.Map attributes = new HashMap();
	
	public MapNodeElementState(MapNodeElement mne)
	{
		super();
		bounds = (Rectangle )mne.getBounds().clone();
		scaleCoefficient = mne.scaleCoefficient;
		name = mne.getName();
		description = mne.getDescription();
		icon = mne.getImage();
		imageId = mne.getImageId();
		anchor = (Point2D.Double )mne.getAnchor().clone();
		optimizerAttribute = mne.optimizerAttribute;

		attributes.putAll(mne.attributes);
	}

	public boolean equals(Object obj)
	{
		MapNodeElementState mnes = (MapNodeElementState )obj;
		return (this.name.equals(mnes.name)
			&& this.description.equals(mnes.description)
			&& this.bounds.equals(mnes.bounds)
			&& this.scaleCoefficient == mnes.scaleCoefficient
			&& this.imageId.equals(mnes.imageId)
			&& this.anchor.equals(mnes.anchor)
			&& this.optimizerAttribute.equals(mnes.optimizerAttribute));
	}
}
