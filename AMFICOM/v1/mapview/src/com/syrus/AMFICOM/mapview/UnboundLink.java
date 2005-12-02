/*-
 * $Id: UnboundLink.java,v 1.33 2005/12/02 11:24:20 bass Exp $
 *
 * Copyright њ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.Characterizable;
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
 * Ёлемент неприв€занной линии. »спользыетс€ как составл€юща€ честь 
 * {@link CablePath} в случае, когда кабель не прив€зан на каком-либо участке 
 * между узлами.
 * 
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.33 $, $Date: 2005/12/02 11:24:20 $
 * @module mapview
 */
public final class UnboundLink extends PhysicalLink {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3762820380682828088L;
	/**
	 *  абельный путь, в который входит неприв€занна€ лини€.
	 */
	private CablePath cablePath;
	
	/**
	 *  онструктор.
	 * @param id идентификатор
	 * @param creatorId пользователь
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param type тип (должен быть {@link PhysicalLinkType#DEFAULT_UNBOUND})
	 */
	private UnboundLink(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final PhysicalLinkType type) {
		super(id, creatorId, version, id.toString(), "", type, stNode.getId(), eNode.getId(), "", "", "");
	}

	/**
	 * —оздать неприв€занную линию.
	 * @param creatorId пользователь
	 * @param stNode начальный узел
	 * @param eNode конечный узел
	 * @param type тип (должен быть {@link PhysicalLinkType#DEFAULT_UNBOUND})
	 * @return нова€ лини€
	 * @throws com.syrus.AMFICOM.general.CreateObjectException если
	 * нельз€ создать объект
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
					StorableObjectVersion.INITIAL_VERSION,
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
	 * ”становить кабельный путь.
	 * 
	 * @param cablePath
	 *        кабельный путь
	 */
	public void setCablePath(final CablePath cablePath) {
		this.cablePath = cablePath;
	}

	/**
	 * ѕолучить кабельный путь.
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

	@Override
	public Characterizable getCharacterizable() {
		return null;
	}

	@Override
	public String getName() {
		return (this.cablePath == null) ? "new" : this.cablePath.getName();
	}
}
