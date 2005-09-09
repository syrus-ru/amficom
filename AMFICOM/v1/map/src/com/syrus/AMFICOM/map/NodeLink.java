/*-
 * $Id: NodeLink.java,v 1.88 2005/09/09 18:52:51 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.corba.IdlNodeLink;
import com.syrus.AMFICOM.map.corba.IdlNodeLinkHelper;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.xml.XmlNodeLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * Фрагмент линии на топологической схеме. Фрагмент представляет собой линейный
 * отрезок, соединяющий два концевых узла ({@link AbstractNode}). Фрагменты
 * не живут сами по себе, а входят в состав одной и только одной линии
 * ({@link PhysicalLink}).
 * @author $Author: bass $
 * @version $Revision: 1.88 $, $Date: 2005/09/09 18:52:51 $
 * @module map
 */
public final class NodeLink extends StorableObject implements MapElement, XmlBeansTransferable<XmlNodeLink> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257290240262617393L;

	private String name;
	private Identifier physicalLinkId;
	private Identifier startNodeId;
	private Identifier endNodeId;
	private double length;

	private transient CharacterizableDelegate characterizableDelegate;

	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;
	protected transient AbstractNode startNode = null;
	protected transient AbstractNode endNode = null;
	protected transient PhysicalLink physicalLink = null;

	public NodeLink(final IdlNodeLink nlt) throws CreateObjectException {
		try {
			this.fromTransferable(nlt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	NodeLink(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String name,
			final Identifier physicalLinkId,
			final Identifier startNodeId,
			final Identifier endNodeId,
			final double length) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.name = name;
		this.physicalLinkId = physicalLinkId;
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
		this.length = length;

		this.selected = false;
	}

	public static NodeLink createInstance(final Identifier creatorId,
			final PhysicalLink physicalLink,
			final AbstractNode stNode,
			final AbstractNode eNode) throws CreateObjectException {
		return NodeLink.createInstance(creatorId, "", physicalLink, stNode, eNode, 0.0D);
	}

	public static NodeLink createInstance(final Identifier creatorId,
			final String name,
			final PhysicalLink physicalLink,
			final AbstractNode starNode,
			final AbstractNode endNode,
			final double length) throws CreateObjectException {
		if (name == null || physicalLink == null || starNode == null || endNode == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final NodeLink nodeLink = new NodeLink(IdentifierPool.getGeneratedIdentifier(NODELINK_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					physicalLink.getId(),
					starNode.getId(),
					endNode.getId(),
					length);

			assert nodeLink.isValid() : OBJECT_BADLY_INITIALIZED;

			nodeLink.markAsChanged();

			return nodeLink;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlNodeLink nlt = (IdlNodeLink) transferable;
		super.fromTransferable(nlt);

		this.length = nlt.length;

		this.physicalLinkId = new Identifier(nlt.physicalLinkId);
		this.startNodeId = new Identifier(nlt.startNodeId);
		this.endNodeId = new Identifier(nlt.endNodeId);
		this.getPhysicalLink().addNodeLink(this);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlNodeLink getTransferable(final ORB orb) {
		return IdlNodeLinkHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.physicalLinkId.getTransferable(),
				this.startNodeId.getTransferable(),
				this.endNodeId.getTransferable(),
				this.length);
	}

	Identifier getEndNodeId() {
		return this.endNodeId;
	}

	void setEndNodeId(Identifier endNodeId) {
		this.endNodeId = endNodeId;
		try {
			this.endNode = StorableObjectPool.<AbstractNode>getStorableObject(this.endNodeId, true);
		} catch(ApplicationException e) {
			Log.errorException(e);
		}
		super.markAsChanged();
	}

	public AbstractNode getEndNode() {
		if(this.endNode == null) {
			try {
				this.endNode = StorableObjectPool.<AbstractNode>getStorableObject(this.endNodeId, true);
			} catch(ApplicationException e) {
				Log.errorException(e);
			}
		}
		return this.endNode;
	}

	public void setEndNode(final AbstractNode endNode) {
		assert endNode != null : NON_NULL_EXPECTED;
		this.endNodeId = endNode.getId();
		this.endNode = endNode;
		super.markAsChanged();
	}

	/**
	 * Если один из концевых узлов линии - кабельный ввод, и другой - 
	 * телефонный узел или здание, то это - проводка по дому. В этом
	 * случае длина считается при подсчете длины проводки по дому,
	 * а данный метод возвращает длину 0.
	 * @return топологическая длина фрагмента, и 0, если проводка по зданию
	 */
	public double getLength() {
		if(getPhysicalLink().getType().getSort().value() == PhysicalLinkTypeSort._INDOOR) {
			return 0.0D;
		}
		return this.length;
	}

	public void setLength(final double length) {
		this.length = length;
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	Identifier getPhysicalLinkId() {
		return this.physicalLinkId;
	}

	public PhysicalLink getPhysicalLink() {
		if(this.physicalLink == null) {
			try {
				this.physicalLink = StorableObjectPool.<PhysicalLink>getStorableObject(this.physicalLinkId, true);
			} catch(ApplicationException e) {
				Log.errorException(e);
				return null;
			}
		}
		return this.physicalLink;
	}

	void setPhysicalLinkId(Identifier physicalLinkId) {
		this.physicalLinkId = physicalLinkId;
		try {
			this.physicalLink = StorableObjectPool.<PhysicalLink>getStorableObject(this.physicalLinkId, true);
		} catch(ApplicationException e) {
			Log.errorException(e);
		}
	}

	public void setPhysicalLink(final PhysicalLink physicalLink) {
		this.physicalLinkId = physicalLink.getId();
		this.physicalLink = physicalLink;
		Log.debugMessage("For node link " + this.id.toString() + " set physicalLinkId = " + this.physicalLinkId.toString(), FINEST);
		super.markAsChanged();
	}

	Identifier getStartNodeId() {
		return this.startNodeId;
	}

	void setStartNodeId(Identifier startNodeId) {
		this.startNodeId = startNodeId;
		try {
			this.startNode = StorableObjectPool.<AbstractNode>getStorableObject(this.startNodeId, true);
		} catch(ApplicationException e) {
			Log.errorException(e);
		}
		markAsChanged();
	}

	public AbstractNode getStartNode() {
		if(this.startNode == null) {
			try {
				this.startNode = StorableObjectPool.<AbstractNode>getStorableObject(this.startNodeId, true);
			} catch(ApplicationException e) {
				Log.errorException(e);
			}
		}
		return this.startNode;
	}

	public void setStartNode(final AbstractNode startNode) {
		this.startNodeId = startNode.getId();
		this.startNode = startNode;
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier physicalLinkId,
			final Identifier startNodeId,
			final Identifier endNodeId,
			final double length) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.physicalLinkId = physicalLinkId;
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
		this.length = length;
	}

	/**
	 * Получить другой концевой узел фрагмента.
	 *
	 * @param node
	 *          концевой узел
	 * @return другой концевой узел. В случае, если node не является концевым для
	 *         данного фрагмента, возвращается <code>null</code>.
	 */
	public AbstractNode getOtherNode(final AbstractNode node) {
		if (this.getEndNode().equals(node)) {
			return getStartNode();
		}
		if (this.getStartNode().equals(node)) {
			return getEndNode();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(final boolean alarmState) {
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState() {
		return getPhysicalLink().getAlarmState();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		return new DoublePoint((getStartNode().getLocation().getX() + getEndNode().getLocation().getX()) / 2,
				(getStartNode().getLocation().getY() + getEndNode().getLocation().getY()) / 2);
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		return new NodeLinkState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(final MapElementState state) {
		final NodeLinkState mnles = (NodeLinkState) state;

		this.setName(mnles.name);
		this.setStartNode(mnles.startNode);
		this.setEndNode(mnles.endNode);

		try {
			final PhysicalLink physicalLink1 = StorableObjectPool.getStorableObject(mnles.physicalLinkId, false);
			setPhysicalLink(physicalLink1);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(final boolean removed) {
		this.removed = removed;
	}

	/**
	 * Получить топологическую длинну фрагмента.
	 *
	 * @return топологическая длина
	 */
	public double getLengthLt() {
		return getLength();
	}

	/**
	 * Установить топологическую длинну фрагмента. Высчитывается в месте, в
	 * котором осуществляется управление рисованием фрагментов линий.
	 *
	 * @param length
	 *          топологическая длина
	 */
	public void setLengthLt(final double length) {
		this.setLength(length);
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.physicalLinkId);
		dependencies.add(this.startNodeId);
		dependencies.add(this.endNodeId);
		return dependencies;
	}

	public XmlNodeLink getXmlTransferable(final String importType) {
		final XmlNodeLink xmlNodeLink = XmlNodeLink.Factory.newInstance();
		xmlNodeLink.setId(this.id.getXmlTransferable(importType));
		xmlNodeLink.setLength(this.length);
		xmlNodeLink.setPhysicalLinkId(this.physicalLinkId.getXmlTransferable(importType));
		xmlNodeLink.setStartNodeId(this.startNodeId.getXmlTransferable(importType));
		xmlNodeLink.setEndNodeId(this.endNodeId.getXmlTransferable(importType));
		
		return xmlNodeLink;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 */
	private NodeLink(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id,
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
		this.selected = false;
	}

	public void fromXmlTransferable(final XmlNodeLink xmlNodeLink, final String importType) throws ApplicationException {
		this.length = xmlNodeLink.getLength();

		final Identifier startNodeId1 = Identifier.fromXmlTransferable(xmlNodeLink.getStartNodeId(), importType);
		final Identifier endNodeId1 = Identifier.fromXmlTransferable(xmlNodeLink.getEndNodeId(), importType);

		this.physicalLinkId = Identifier.fromXmlTransferable(xmlNodeLink.getPhysicalLinkId(), importType);
		this.startNodeId = startNodeId1;
		this.endNodeId = endNodeId1;
		
		this.getPhysicalLink().addNodeLink(this);
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlNodeLink
	 * @throws CreateObjectException
	 */
	public static NodeLink createInstance(
			final Identifier creatorId, 
			final String importType, 
			final XmlNodeLink xmlNodeLink)
	throws CreateObjectException {
		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlNodeLink.getId(), NODELINK_CODE, importType);
			NodeLink nodeLink = StorableObjectPool.getStorableObject(id, true);
			if (nodeLink == null) {
				nodeLink = new NodeLink(id, new Date(), creatorId);
			}
			nodeLink.fromXmlTransferable(xmlNodeLink, importType);
			assert nodeLink.isValid() : OBJECT_BADLY_INITIALIZED;
			nodeLink.markAsChanged();
			return nodeLink;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}
}
