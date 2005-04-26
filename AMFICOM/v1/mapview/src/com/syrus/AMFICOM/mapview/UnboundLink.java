/**
 * $Id: UnboundLink.java,v 1.9 2005/04/26 16:12:21 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 * Элемент непривязанной линии. Использыется как составляющая честь 
 * {@link CablePath} в случае, когда кабель не привязан на каком-либо участке 
 * между узлами.
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/04/26 16:12:21 $
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
	 * @param type тип (должен быть {@link PhysicalLinkType#UNBOUND})
	 */
	protected UnboundLink(
			Identifier id,
			Identifier creatorId,
			final long version,
			AbstractNode stNode, 
			AbstractNode eNode, 
			PhysicalLinkType type)
	{
		super(id, creatorId, version, id.toString(), "", type, stNode, eNode, "", "", "", 0, 0, true, true);
	}

	/**
	 * Создать непривязанную линию.
	 * @param creatorId пользователь
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param type тип (должен быть {@link PhysicalLinkType#UNBOUND})
	 * @return новая линия
	 * @throws com.syrus.AMFICOM.general.CreateObjectException если
	 * нельзя создать объект
	 */
	public static PhysicalLink createInstance(
			Identifier creatorId,
			AbstractNode stNode, 
			AbstractNode eNode, 
			PhysicalLinkType type)
		throws CreateObjectException 
	{
		if (stNode == null || eNode == null || type == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			UnboundLink unboundLink = new UnboundLink(
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE),
				creatorId,
				0L,
				stNode, 
				eNode, 
				type);
			unboundLink.changed = true;
			return unboundLink;
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("UnboundLink.createInstance | cannot generate identifier ", e);
		}
		catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("UnboundLink.createInstance | cannot generate identifier ", e);
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
	public Set getDependencies()
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
	public IDLEntity getTransferable()
	{
		throw new UnsupportedOperationException();
	}

}
