/**
 * $Id: UnboundLink.java,v 1.22 2005/06/25 17:50:49 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLink;

/**
 * ������� ������������� �����. ������������ ��� ������������ ����� 
 * {@link CablePath} � ������, ����� ������ �� �������� �� �����-���� ������� 
 * ����� ������.
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/06/25 17:50:49 $
 * @module mapviewclient_v1
 */
public final class UnboundLink extends PhysicalLink {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3762820380682828088L;
	/**
	 * ��������� ����, � ������� ������ ������������� �����.
	 */
	protected CablePath cablePath;
	
	/**
	 * �����������.
	 * @param id �������������
	 * @param creatorId ������������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 * @param type ��� (������ ���� {@link PhysicalLinkType#DEFAULT_UNBOUND})
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
	 * ������� ������������� �����.
	 * @param creatorId ������������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 * @param type ��� (������ ���� {@link PhysicalLinkType#DEFAULT_UNBOUND})
	 * @return ����� �����
	 * @throws com.syrus.AMFICOM.general.CreateObjectException ����
	 * ������ ������� ������
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
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICALLINK_CODE),
				creatorId,
				0L,
				stNode,
				eNode,
				type);
			unboundLink.markAsChanged();
			return unboundLink;
		} catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("UnboundLink.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * ���������� ��������� ����.
	 * @param cablePath ��������� ����
	 */
	public void setCablePath(CablePath cablePath)
	{
		this.cablePath = cablePath;
	}


	/**
	 * �������� ��������� ����.
	 * @return ��������� ����
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
	public IdlStorableObject getHeaderTransferable(final ORB orb) {
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
