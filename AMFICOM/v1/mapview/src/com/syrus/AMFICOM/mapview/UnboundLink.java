/**
 * $Id: UnboundLink.java,v 1.4 2005/02/02 15:17:30 krupenn Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/02/02 15:17:30 $
 * @module mapviewclient_v1
 */
public class UnboundLink extends PhysicalLink
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3762820380682828088L;
	/**
	 * Кабельный путь, в который входит непривязанная линия.
	 */
	protected CablePath cablePath;
	
	/**
	 * Конструктор.
	 * @param id идентификатор
	 * @param creatorId пользователь
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param map топологическая схема
	 * @param proto тип (должен быть {@link PhysicalLinkType#UNBOUND})
	 */
	protected UnboundLink(
			Identifier id,
			Identifier creatorId,
			AbstractNode stNode, 
			AbstractNode eNode, 
			Map map,
			PhysicalLinkType proto)
	{
		super(id, creatorId, id.toString(), "", proto, stNode, eNode, "", "", "", 0, 0, true, true);
	}

	/**
	 * Создать непривязанную линию.
	 * @param creatorId пользователь
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param map топологическая схема
	 * @param proto тип (должен быть {@link PhysicalLinkType#UNBOUND})
	 * @return новая линия
	 * @throws com.syrus.AMFICOM.general.CreateObjectException если
	 * нельзя создать объект
	 */
	public static UnboundLink createInstance(
			Identifier creatorId,
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
				creatorId,
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

	/**
	 * Установить кабельный путь.
	 * @param cablePath кабельный путь
	 */
	public void setCablePath(CablePath cablePath)
	{
		this.cablePath = cablePath;
	}


	/**
	 * Получить кабельный путь.
	 * @return кабельный путь
	 */
	public CablePath getCablePath()
	{
		return this.cablePath;
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public void insert() throws CreateObjectException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public List getDependencies()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public StorableObject_Transferable getHeaderTransferable()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public Object getTransferable()
	{
		throw new UnsupportedOperationException();
	}

}
