/**
 * $Id: MapUnboundNodeElement.java,v 1.7 2004/12/08 16:20:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.io.Serializable;

/**
 * уэел 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/08 16:20:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapUnboundNodeElement extends MapSiteNodeElement implements Serializable
{
	protected SchemeElement schemeElement;
	
	protected boolean canBind = false;

	public MapUnboundNodeElement(
		SchemeElement schemeElement,
		String id,
		DoublePoint location,
		Map map,
		MapNodeProtoElement pe)
	{
		super(id, location, map, pe);

		setSchemeElement(schemeElement);
	}

	public void setCanBind(boolean canBind)
	{
		this.canBind = canBind;
	}
	
	public boolean getCanBind()
	{
		return this.canBind;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}


	public void setSchemeElement(SchemeElement schemeElement)
	{
		this.schemeElement = schemeElement;
		setName(schemeElement.getName());
	}


	public SchemeElement getSchemeElement()
	{
		return schemeElement;
	}

}
