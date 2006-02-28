/*-
 * $Id: UnboundLink.java,v 1.36 2006/02/28 15:20:02 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
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
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.36 $, $Date: 2006/02/28 15:20:02 $
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
		super(id, creatorId, version, id.toString(), "", type, stNode.getId(), eNode.getId(), "", "", "");
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
			final UnboundLink unboundLink = new UnboundLink(IdentifierPool.getGeneratedIdentifier(PHYSICALLINK_CODE),
					creatorId,
					INITIAL_VERSION,
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
	protected Set<Identifiable> getDependenciesTmpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	@Override
	public IdlPhysicalLink getIdlTransferable(final ORB orb) {
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
