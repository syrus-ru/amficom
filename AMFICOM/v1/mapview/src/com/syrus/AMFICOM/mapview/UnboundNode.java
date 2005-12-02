/*-
 * $Id: UnboundNode.java,v 1.34 2005/12/02 11:24:20 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
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
 * Непривязанный элемент. Сооветствует элеименту схемы, не привязанному
 * ни к какому элементу топологической схемы.
 *
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.34 $, $Date: 2005/12/02 11:24:20 $
 * @module mapview
 */
public final class UnboundNode extends SiteNode {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4121131437689942576L;

	/**
	 * элемент схемы.
	 */
	private SchemeElement schemeElement;
	
	/**
	 * Флаг того, что непривязанный элемент может быть привязан к элементу
	 * карты. Используется при перемещении непривязанного элемента мышкой.
	 * При перемещении его через элемент топологической карты флаг получает
	 * значение <code>true</code>, что означает, что при отпускании мыши
	 * схемный элемент {@link #schemeElement} будет привязан к элементу.
	 * При перемещении мыши за пределы элемента топологической карты
	 * флаг опять принимает значение <code>false</code>.
	 */
	private boolean canBind = false;

	/**
	 * Конструктор.
	 * @param schemeElement элемент схемы
	 * @param id идентификатор непривязанного элемента
	 * @param location географические координаты непривязанного элемента
	 * @param nodeType тип элемента (должен быть {@link SiteNodeType#DEFAULT_UNBOUND})
	 */
	private UnboundNode(final Identifier id,
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
	 * Создать новый непривязанный элемент. Вызывается при дропе схемного элемента
	 * на окно карты (см.
	 * com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener.schemeElementDropped(SchemeElement,
	 * Point) в модуле mapviewclient_v1).
	 * 
	 * @param schemeElement
	 *        элемент схемы
	 * @param location
	 *        географические координаты непривязанного элемента
	 * @param nodeType
	 *        тип элемента (должен быть {@link SiteNodeType#DEFAULT_UNBOUND})
	 * @return новый непривязанный элемент
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *         если невозможно получить новый идентификатор
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
					StorableObjectVersion.INITIAL_VERSION,
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
	 * Установить флаг возможности привязки непривязанного узла к сетевому узлу.
	 * 
	 * @param canBind
	 *        значение флага
	 */
	public void setCanBind(final boolean canBind) {
		this.canBind = canBind;
	}

	/**
	 * получить флаг возможности привязки непривязанного узла к сетевому узлу.
	 * 
	 * @return значение флага
	 */
	public boolean getCanBind() {
		return this.canBind;
	}

	/**
	 * Установить элемент схемы.
	 * 
	 * @param schemeElement
	 *        элемент схемы
	 */
	public void setSchemeElement(final SchemeElement schemeElement) {
		this.schemeElement = schemeElement;
		setName(schemeElement.getName());
	}

	/**
	 * Получить элемент схемы.
	 * 
	 * @return элемент схемы
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
