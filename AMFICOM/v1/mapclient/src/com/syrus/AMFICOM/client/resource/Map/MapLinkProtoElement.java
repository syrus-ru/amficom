/**
 * $Id: MapLinkProtoElement.java,v 1.14 2004/12/22 16:17:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.PhysicalLinkType;

import java.io.Serializable;

/**
 * тип физической линии 
 * 
 * 
 * 
 * @version $Revision: 1.14 $, $Date: 2004/12/22 16:17:38 $
 * @module
 * @author $Author: krupenn $
 * @see
 */	
public final class MapLinkProtoElement extends PhysicalLinkType
		implements Serializable
{
	private static final long serialVersionUID = 02L;

	public static final String TUNNEL = "tunnel";
	public static final String COLLECTIOR = "collector";
	public static final String UNBOUND = "cable";

	/** имя класса панели свойств объекта */
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapLinkProtoElement(
			String name,
			String description,
			IntDimension bindingDimension)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("physicallinktype"));
		this.setName(name);
		this.setDescription(description);
		this.setBindingDimension(bindingDimension);
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
}

