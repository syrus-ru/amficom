/**
 * $Id: UnboundNode.java,v 1.5 2005/02/10 13:00:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

import java.util.List;

/**
 * ������������� �������. ������������ ��������� �����, �� ������������ 
 * �� � ������ �������� �������������� �����.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/02/10 13:00:42 $
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
	 * @param map �������������� �����
	 * @param nodeType ��� �������� (������ ���� {@link SiteNodeType#UNBOUND})
	 */
	protected UnboundNode(
		Identifier id,
		Identifier creatorId,
		SchemeElement schemeElement,
		DoublePoint location,
		Map map,
		SiteNodeType nodeType)
	{
		super(
				id, 
				creatorId, 
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
		this.name = schemeElement.name();
	}

	/**
	 * ������� ����� ������������� �������.
	 * ���������� ��� ����� �������� �������� �� ���� �����
	 * (��. com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener.schemeElementDropped(SchemeElement, Point)
	 * � ������ mapviewclient_v1).
	 * @param schemeElement ������� �����
	 * @param location �������������� ���������� �������������� ��������
	 * @param map �������������� �����
	 * @param nodeType ��� �������� (������ ���� {@link SiteNodeType#UNBOUND})
	 * @return ����� ������������� �������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * 	���� ���������� �������� ����� �������������
	 */
	public static UnboundNode createInstance(
			Identifier creatorId,
			SchemeElement schemeElement,
			DoublePoint location,
			Map map,
			SiteNodeType nodeType)
		throws CreateObjectException 
	{
		if (schemeElement == null || map == null || location == null || nodeType == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
//				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new UnboundNode(
				ide,
				creatorId,
				schemeElement,
				location,
				map,
				nodeType);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapUnboundNodeElement.createInstance | cannot generate identifier ", e);
		}
		catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("MapUnboundNodeElement.createInstance | cannot generate identifier ", e);
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
		setName(schemeElement.name());
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
