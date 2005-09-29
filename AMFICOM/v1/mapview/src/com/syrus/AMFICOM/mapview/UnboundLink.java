/*-
 * $Id: UnboundLink.java,v 1.29 2005/09/29 11:34:11 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
 * ������� ������������� �����. ������������ ��� ������������ ����� 
 * {@link CablePath} � ������, ����� ������ �� �������� �� �����-���� ������� 
 * ����� ������.
 * 
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.29 $, $Date: 2005/09/29 11:34:11 $
 * @module mapview
 */
public final class UnboundLink extends PhysicalLink {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3762820380682828088L;
	/**
	 * ��������� ����, � ������� ������ ������������� �����.
	 */
	private CablePath cablePath;
	
	/**
	 * �����������.
	 * @param id �������������
	 * @param creatorId ������������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 * @param type ��� (������ ���� {@link PhysicalLinkType#DEFAULT_UNBOUND})
	 */
	private UnboundLink(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final PhysicalLinkType type) {
		super(id, creatorId, version, id.toString(), "", type, stNode.getId(), eNode.getId(), "", "", "", 0, 0, true, true);
	}

	/**
	 * ������� ������������� �����.
	 * @param creatorId ������������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 * @param type ��� (������ ���� {@link PhysicalLinkType#DEFAULT_UNBOUND})
	 * @return ����� �����
	 * @throws com.syrus.AMFICOM.general.CreateObjectException ����
	 * ������ ������� ������
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
	 * ���������� ��������� ����.
	 * 
	 * @param cablePath
	 *        ��������� ����
	 */
	public void setCablePath(final CablePath cablePath) {
		this.cablePath = cablePath;
	}

	/**
	 * �������� ��������� ����.
	 * 
	 * @return ��������� ����
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
}
