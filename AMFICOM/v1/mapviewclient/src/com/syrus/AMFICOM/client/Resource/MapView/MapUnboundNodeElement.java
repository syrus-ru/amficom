/**
 * $Id: MapUnboundNodeElement.java,v 1.8 2004/12/22 16:38:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * уэел 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/12/22 16:38:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapUnboundNodeElement extends SiteNode
{
	protected SchemeElement schemeElement;
	
	protected boolean canBind = false;

	public MapUnboundNodeElement(
		SchemeElement schemeElement,
		Identifier id,
		DoublePoint location,
		Map map,
		SiteNodeType pe)
	{
		super(
				id, 
				map.getCreatorId(), 
				pe.getImageId(), 
				pe.getName(), 
				"", 
				pe, 
				location.x,
				location.y, 
				"", 
				"", 
				"");

		setSchemeElement(schemeElement);
	}

	public static MapUnboundNodeElement createInstance(
			SchemeElement schemeElement,
			DoublePoint location,
			Map map,
			SiteNodeType pe)
		throws CreateObjectException 
	{
		if (schemeElement == null || map == null || location == null || pe == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new MapUnboundNodeElement(
				schemeElement,
				ide,
				location,
				map,
				pe);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapUnboundNodeElement.createInstance | cannot generate identifier ", e);
		}
		catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("MapUnboundNodeElement.createInstance | cannot generate identifier ", e);
		}
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
		setName(schemeElement.name());
	}


	public SchemeElement getSchemeElement()
	{
		return schemeElement;
	}

////////////////////////////////////////////////////////////////////////////////

	public void insert() throws CreateObjectException
	{
		throw new UnsupportedOperationException();
	}

	public List getDependencies()
	{
		throw new UnsupportedOperationException();
	}

	public StorableObject_Transferable getHeaderTransferable()
	{
		throw new UnsupportedOperationException();
	}

	public Object getTransferable()
	{
		throw new UnsupportedOperationException();
	}

}
