/**
 * $Id: UnboundNode.java,v 1.14 2005/05/16 11:30:41 krupenn Exp $
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

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * ������������� �������. ������������ ��������� �����, �� ������������ 
 * �� � ������ �������� �������������� �����.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/05/16 11:30:41 $
 * @module mapviewclient_v1
 */
public class UnboundNode extends SiteNode
{
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
	protected UnboundNode(
		Identifier id,
		Identifier creatorId,
		final long version,
		SchemeElement schemeElement,
		DoublePoint location,
		SiteNodeType nodeType)
	{
		super(
				id, 
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
	 * ������� ����� ������������� �������.
	 * ���������� ��� ����� �������� �������� �� ���� �����
	 * (��. com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener.schemeElementDropped(SchemeElement, Point)
	 * � ������ mapviewclient_v1).
	 * @param schemeElement ������� �����
	 * @param location �������������� ���������� �������������� ��������
	 * @param nodeType ��� �������� (������ ���� {@link SiteNodeType#DEFAULT_UNBOUND})
	 * @return ����� ������������� �������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * 	���� ���������� �������� ����� �������������
	 */
	public static UnboundNode createInstance(
			Identifier creatorId,
			SchemeElement schemeElement,
			DoublePoint location,
			SiteNodeType nodeType)
		throws CreateObjectException 
	{
		if (schemeElement == null || location == null || nodeType == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			UnboundNode unboundNode = new UnboundNode(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE),
				creatorId,
				0L,
				schemeElement,
				location,
				nodeType);
			unboundNode.changed = true;
			return unboundNode;
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("UnboundNode.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * ���������� ���� ����������� �������� �������������� ���� � ��������
	 * ����.
	 * @param canBind �������� �����
	 */
	public void setCanBind(boolean canBind)
	{
		this.canBind = canBind;
	}
	
	/**
	 * �������� ���� ����������� �������� �������������� ���� � ��������
	 * ����.
	 * @return �������� �����
	 */
	public boolean getCanBind()
	{
		return this.canBind;
	}

	/**
	 * ���������� ������� �����.
	 * @param schemeElement ������� �����
	 */
	public void setSchemeElement(SchemeElement schemeElement)
	{
		this.schemeElement = schemeElement;
		setName(schemeElement.getName());
	}


	/**
	 * �������� ������� �����.
	 * @return ������� �����
	 */
	public SchemeElement getSchemeElement()
	{
		return this.schemeElement;
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

	public String getName() {
		return this.schemeElement.getName();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException("Use SchemeElement.setName(String)");
	}

}
