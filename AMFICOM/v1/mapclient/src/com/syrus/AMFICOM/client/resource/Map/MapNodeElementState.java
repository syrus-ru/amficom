/**
 * $Id: MapNodeElementState.java,v 1.1 2004/09/13 12:02:01 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:02:01 $
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
	java.util.HashMap attributes = new HashMap();
	
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
}
