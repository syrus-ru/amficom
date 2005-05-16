/**
 * $Id: UnboundNode.java,v 1.14 2005/05/16 11:30:41 krupenn Exp $
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
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * Непривязанный элемент. Сооветствует элеименту схемы, не привязанному 
 * ни к какому элементу топологической схемы.
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
	 * элемент схемы.
	 */
	protected SchemeElement schemeElement;
	
	/**
	 * Флаг того, что непривязанный элемент может быть привязан к элементу
	 * карты. Используется при перемещении непривязанного элемента мышкой.
	 * При перемещении его через элемент топологической карты флаг получает
	 * значение <code>true</code>, что означает, что при отпускании мыши
	 * схемный элемент {@link #schemeElement} будет привязан к элементу.
	 * При перемещении мыши за пределы элемента топологической карты 
	 * флаг опять принимает значение <code>false</code>.
	 */
	protected boolean canBind = false;

	/**
	 * Конструктор.
	 * @param schemeElement элемент схемы
	 * @param id идентификатор непривязанного элемента
	 * @param location географические координаты непривязанного элемента
	 * @param nodeType тип элемента (должен быть {@link SiteNodeType#DEFAULT_UNBOUND})
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
	 * Создать новый непривязанный элемент.
	 * Вызывается при дропе схемного элемента на окно карты
	 * (см. com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener.schemeElementDropped(SchemeElement, Point)
	 * в модуле mapviewclient_v1).
	 * @param schemeElement элемент схемы
	 * @param location географические координаты непривязанного элемента
	 * @param nodeType тип элемента (должен быть {@link SiteNodeType#DEFAULT_UNBOUND})
	 * @return новый непривязанный элемент
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * 	если невозможно получить новый идентификатор
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
	 * Установить флаг возможности привязки непривязанного узла к сетевому
	 * узлу.
	 * @param canBind значение флага
	 */
	public void setCanBind(boolean canBind)
	{
		this.canBind = canBind;
	}
	
	/**
	 * получить флаг возможности привязки непривязанного узла к сетевому
	 * узлу.
	 * @return значение флага
	 */
	public boolean getCanBind()
	{
		return this.canBind;
	}

	/**
	 * Установить элемент схемы.
	 * @param schemeElement элемент схемы
	 */
	public void setSchemeElement(SchemeElement schemeElement)
	{
		this.schemeElement = schemeElement;
		setName(schemeElement.getName());
	}


	/**
	 * Получить элемент схемы.
	 * @return элемент схемы
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
