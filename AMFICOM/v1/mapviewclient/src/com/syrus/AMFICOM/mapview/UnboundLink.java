/**
 * $Id: UnboundLink.java,v 1.1 2005/01/31 13:11:21 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

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
 * Элемент непривязанной линии. Использыется как составляющая честь 
 * {@link CablePath} в случае, когда кабель не привязан на каком-либо участке 
 * между узлами.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/01/31 13:11:21 $
 * @module mapviewclient_v1
 */
public class UnboundLink extends PhysicalLink
{
	protected CablePath cablePath;
	
	protected UnboundLink(
			Identifier id,
			AbstractNode stNode, 
			AbstractNode eNode, 
			Map map,
			PhysicalLinkType proto)
	{
		super(id, map.getCreatorId(), id.toString(), "", proto, stNode, eNode, "", "", "", 0, 0, true, true);
	}

	public static UnboundLink createInstance(
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
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE);
			return new UnboundLink(
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

	public void setCablePath(CablePath cablePath)
	{
		this.cablePath = cablePath;
	}


	public CablePath getCablePath()
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
