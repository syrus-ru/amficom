/**
 * $Id: MapUnboundLinkElement.java,v 1.11 2004/12/23 16:58:00 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

import java.util.List;

/**
 * элемент линии 
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2004/12/23 16:58:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */

//MapPhysicalPathElement
public class MapUnboundLinkElement extends PhysicalLink
{
	protected MapCablePathElement cablePath;
	
	public MapUnboundLinkElement(
			Identifier id,
			AbstractNode stNode, 
			AbstractNode eNode, 
			Map map,
			PhysicalLinkType proto)
	{
		super(id, map.getCreatorId(), id.toString(), "", proto, stNode, eNode, "", "", "", 0, 0, true, true);
	}

	public static MapUnboundLinkElement createInstance(
			AbstractNode stNode, 
			AbstractNode eNode, 
			Map map,
			PhysicalLinkType proto)
		throws CreateObjectException 
	{
		if (stNode == null || map == null || eNode == null || proto == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new MapUnboundLinkElement(
				ide,
				stNode, 
				eNode, 
				map,
				proto);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapUnboundLinkElement.createInstance | cannot generate identifier ", e);
		}
		catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("MapUnboundLinkElement.createInstance | cannot generate identifier ", e);
		}
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public void setCablePath(MapCablePathElement cablePath)
	{
		this.cablePath = cablePath;
	}


	public MapCablePathElement getCablePath()
	{
		return cablePath;
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
