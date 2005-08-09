/**
 * $Id: UnboundLink.java,v 1.25 2005/08/09 16:28:40 arseniy Exp $
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

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLink;

/**
 * Элемент непривязанной линии. Использыется как составляющая честь 
 * {@link CablePath} в случае, когда кабель не привязан на каком-либо участке 
 * между узлами.
 * @author $Author: arseniy $
 * @version $Revision: 1.25 $, $Date: 2005/08/09 16:28:40 $
 * @module mapview
 */
public final class UnboundLink extends PhysicalLink {
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
	 * @param type тип (должен быть {@link PhysicalLinkType#DEFAULT_UNBOUND})
	 */
	protected UnboundLink(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final PhysicalLinkType type) {
		super(id, creatorId, version, id.toString(), "", type, stNode.getId(), eNode.getId(), "", "", "", 0, 0, true, true);
	}

	/**
	 * Создать непривязанную линию.
	 * @param creatorId пользователь
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param type тип (должен быть {@link PhysicalLinkType#DEFAULT_UNBOUND})
	 * @return новая линия
	 * @throws com.syrus.AMFICOM.general.CreateObjectException если
	 * нельзя создать объект
	 */
	public static PhysicalLink createInstance(final Identifier creatorId,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final PhysicalLinkType type) throws CreateObjectException {
		if (stNode == null || eNode == null || type == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final UnboundLink unboundLink = new UnboundLink(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICALLINK_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					stNode,
					eNode,
					type);
			unboundLink.markAsChanged();
			return unboundLink;
		} catch (IdentifierGenerationException e) {
			throw new CreateObjectException("UnboundLink.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * Установить кабельный путь.
	 * 
	 * @param cablePath
	 *        кабельный путь
	 */
	public void setCablePath(final CablePath cablePath) {
		this.cablePath = cablePath;
	}

	/**
	 * Получить кабельный путь.
	 * 
	 * @return кабельный путь
	 */
	public CablePath getCablePath() {
		return this.cablePath;
	}

// //////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	@Override
	public IdlPhysicalLink getTransferable(final ORB orb) {
		throw new UnsupportedOperationException();
	}
}
