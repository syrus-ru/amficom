/**
 * $Id: UnboundNode.java,v 1.2 2005/01/30 15:38:18 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.mapview;

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
 * @version $Revision: 1.2 $, $Date: 2005/01/30 15:38:18 $
 * @module mapviewclient_v1
 */
public class UnboundNode extends SiteNode
{
	/**
	 * ������� �����.
	 */
	protected SchemeElement schemeElement;
	
	/**
	 * ���� ����, ��� ������������� ������� ����� ���� �������� � ��������
	 * �����. ������������ ��� ����������� �������������� �������� ������.
	 * ��� ����������� ��� ����� ������� �������������� ����� ���� ��������
	 * �������� <code>true</code>, ��� ��������, ��� ��� ���������� ����
	 * ������� ������� {@link schemeElement} ����� �������� � ��������.
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
	 * @param pe ��� �������� (������ ���� {@link SiteNodeType#UNBOUND})
	 */
	public UnboundNode(
		SchemeElement schemeElement,
		Identifier id,
		DoublePoint location,
		Map map,
		SiteNodeType pe)
	{
		super(
				id, 
				map.getCreatorId(), 
				pe.getImageId(), 
				pe.getName(), 
				"", 
				pe, 
				location.getX(),
				location.getY(), 
				"", 
				"", 
				"");

		setSchemeElement(schemeElement);
	}

	/**
	 * ������� ����� ������������� �������.
	 * ���������� ��� ����� �������� �������� �� ���� �����
	 * (��. {@link com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener#schemeElementDropped(SchemeElement, Point)}).
	 * @param schemeElement ������� �����
	 * @param location �������������� ���������� �������������� ��������
	 * @param map �������������� �����
	 * @param pe ��� �������� (������ ���� {@link SiteNodeType#UNBOUND})
	 * @return ����� ������������� �������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * 	���� ���������� �������� ����� �������������
	 */
	public static UnboundNode createInstance(
			SchemeElement schemeElement,
			DoublePoint location,
			Map map,
			SiteNodeType pe)
		throws CreateObjectException 
	{
		if (schemeElement == null || map == null || location == null || pe == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new UnboundNode(
				schemeElement,
				ide,
				location,
				map,
				pe);
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

	public void setCanBind(boolean canBind)
	{
		this.canBind = canBind;
	}
	
	public boolean getCanBind()
	{
		return this.canBind;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}


	public void setSchemeElement(SchemeElement schemeElement)
	{
		this.schemeElement = schemeElement;
		setName(schemeElement.name());
	}


	public SchemeElement getSchemeElement()
	{
		return schemeElement;
	}

////////////////////////////////////////////////////////////////////////////////

	public void insert() throws CreateObjectException
	{
		throw new UnsupportedOperationException();
	}

	public List getDependencies()
	{
		throw new UnsupportedOperationException();
	}

	public StorableObject_Transferable getHeaderTransferable()
	{
		throw new UnsupportedOperationException();
	}

	public Object getTransferable()
	{
		throw new UnsupportedOperationException();
	}

}
