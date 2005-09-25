/*-
 * $Id: UnboundNode.java,v 1.31 2005/09/25 15:50:54 krupenn Exp $
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
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * ������������� �������. ������������ ��������� �����, �� ������������
 * �� � ������ �������� �������������� �����.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.31 $, $Date: 2005/09/25 15:50:54 $
 * @module mapview
 */
public final class UnboundNode extends SiteNode {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4121131437689942576L;

	/**
	 * ������� �����.
	 */
	protected SchemeElement schemeElement;
	
	/**
	 * ���� ����, ��� ������������� ������� ����� ���� �������� � ��������
	 * �����. ������������ ��� ����������� �������������� �������� ������.
	 * ��� ����������� ��� ����� ������� �������������� ����� ���� ��������
	 * �������� <code>true</code>, ��� ��������, ��� ��� ���������� ����
	 * ������� ������� {@link #schemeElement} ����� �������� � ��������.
	 * ��� ����������� ���� �� ������� �������� �������������� �����
	 * ���� ����� ��������� �������� <code>false</code>.
	 */
	protected boolean canBind = false;

	/**
	 * �����������.
	 * @param schemeElement ������� �����
	 * @param id ������������� �������������� ��������
	 * @param location �������������� ���������� �������������� ��������
	 * @param nodeType ��� �������� (������ ���� {@link SiteNodeType#DEFAULT_UNBOUND})
	 */
	protected UnboundNode(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final SchemeElement schemeElement,
			final DoublePoint location,
			final SiteNodeType nodeType) {
		super(id,
				creatorId,
				version,
				nodeType.getImageId(),
				nodeType.getName(),
				"",
				nodeType,
				location.getX(),
				location.getY(),
				"",
				"",
				"");

		this.schemeElement = schemeElement;
	}

	/**
	 * ������� ����� ������������� �������. ���������� ��� ����� �������� ��������
	 * �� ���� ����� (��.
	 * com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener.schemeElementDropped(SchemeElement,
	 * Point) � ������ mapviewclient_v1).
	 * 
	 * @param schemeElement
	 *        ������� �����
	 * @param location
	 *        �������������� ���������� �������������� ��������
	 * @param nodeType
	 *        ��� �������� (������ ���� {@link SiteNodeType#DEFAULT_UNBOUND})
	 * @return ����� ������������� �������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *         ���� ���������� �������� ����� �������������
	 */
	public static UnboundNode createInstance(final Identifier creatorId,
			final SchemeElement schemeElement,
			final DoublePoint location,
			final SiteNodeType nodeType) throws CreateObjectException {
		if (schemeElement == null || location == null || nodeType == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final UnboundNode unboundNode = new UnboundNode(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					schemeElement,
					location,
					nodeType);
			unboundNode.markAsChanged();
			return unboundNode;
		} catch (IdentifierGenerationException e) {
			throw new CreateObjectException("UnboundNode.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * ���������� ���� ����������� �������� �������������� ���� � �������� ����.
	 * 
	 * @param canBind
	 *        �������� �����
	 */
	public void setCanBind(final boolean canBind) {
		this.canBind = canBind;
	}

	/**
	 * �������� ���� ����������� �������� �������������� ���� � �������� ����.
	 * 
	 * @return �������� �����
	 */
	public boolean getCanBind() {
		return this.canBind;
	}

	/**
	 * ���������� ������� �����.
	 * 
	 * @param schemeElement
	 *        ������� �����
	 */
	public void setSchemeElement(final SchemeElement schemeElement) {
		this.schemeElement = schemeElement;
		setName(schemeElement.getName());
	}

	/**
	 * �������� ������� �����.
	 * 
	 * @return ������� �����
	 */
	public SchemeElement getSchemeElement() {
		return this.schemeElement;
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
	public IdlSiteNode getTransferable(final ORB orb) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return this.schemeElement.getName();
	}

	@Override
	public void setName(final String name) {
		throw new UnsupportedOperationException("Use SchemeElement.setName(String)");
	}

	@Override
	public Characterizable getCharacterizable() {
		return null;
	}

}
