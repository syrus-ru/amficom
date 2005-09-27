/*-
 * $Id: NodeLink.java,v 1.101 2005/09/27 07:44:21 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.bugs.Crutch134;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlNodeLink;
import com.syrus.AMFICOM.map.corba.IdlNodeLinkHelper;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.xml.XmlNodeLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * �������� ����� �� �������������� �����. �������� ������������ ����� ��������
 * �������, ����������� ��� �������� ���� ({@link AbstractNode}). ���������
 * �� ����� ���� �� ����, � ������ � ������ ����� � ������ ����� �����
 * ({@link PhysicalLink}).
 * @author $Author: krupenn $
 * @version $Revision: 1.101 $, $Date: 2005/09/27 07:44:21 $
 * @module map
 */
public final class NodeLink extends StorableObject
		implements Characterizable, MapElement, XmlBeansTransferable<XmlNodeLink> {
	private static final long serialVersionUID = 3257290240262617393L;

	private String name;
	private Identifier physicalLinkId;
	private Identifier startNodeId;
	private Identifier endNodeId;
	private double length;

	private AbstractNode startNode = null;
	private AbstractNode endNode = null;
	private PhysicalLink physicalLink = null;

	private transient boolean selected = false;
	private transient boolean removed = false;

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
//		this.getPhysicalLink().addNodeLink(this);
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

	public Identifier getEndNodeId() {
		return this.endNodeId;
	}

	public void setEndNodeId(Identifier endNodeId) {
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

	public double getLength0() {
		return this.length;
	}
	/**
	 * ���� ���� �� �������� ����� ����� - ��������� ����, � ������ - 
	 * ���������� ���� ��� ������, �� ��� - �������� �� ����. � ����
	 * ������ ����� ��������� ��� �������� ����� �������� �� ����,
	 * � ������ ����� ���������� ����� 0.
	 * @return �������������� ����� ���������, � 0, ���� �������� �� ������
	 */
	public double getLength() {
		if(getPhysicalLink().getType().getSort().value() == PhysicalLinkTypeSort._INDOOR) {
			return 0.0D;
		}
		return this.getLength0();
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

	public Identifier getPhysicalLinkId() {
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

	public void setPhysicalLinkId(Identifier physicalLinkId) {
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

	public Identifier getStartNodeId() {
		return this.startNodeId;
	}

	public void setStartNodeId(Identifier startNodeId) {
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
	 * �������� ������ �������� ���� ���������.
	 *
	 * @param node
	 *          �������� ����
	 * @return ������ �������� ����. � ������, ���� node �� �������� �������� ���
	 *         ������� ���������, ������������ <code>null</code>.
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
		throw new UnsupportedOperationException("Cannot set alarm state for a node link!");
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
	 * �������� �������������� ������ ���������.
	 *
	 * @return �������������� �����
	 */
	public double getLengthLt() {
		return getLength();
	}

	/**
	 * ���������� �������������� ������ ���������. ������������� � �����, �
	 * ������� �������������� ���������� ���������� ���������� �����.
	 *
	 * @param length
	 *          �������������� �����
	 */
	public void setLengthLt(final double length) {
		this.setLength(length);
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

	/**
	 * @param nodeLink
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void getXmlTransferable(final XmlNodeLink nodeLink,
			final String importType)
	throws ApplicationException {
		this.id.getXmlTransferable(nodeLink.addNewId(), importType);
		nodeLink.setLength(this.length);
		this.physicalLinkId.getXmlTransferable(nodeLink.addNewPhysicalLinkId(), importType);
		this.startNodeId.getXmlTransferable(nodeLink.addNewStartNodeId(), importType);
		this.endNodeId.getXmlTransferable(nodeLink.addNewEndNodeId(), importType);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private NodeLink(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(Identifier.fromXmlTransferable(id, importType, NODELINK_CODE),
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
		/**
		 * @todo Should go to #fromTransferable(...) or
		 *       the corresponding complementor.
		 */
		this.selected = false;
	}

	public void fromXmlTransferable(final XmlNodeLink xmlNodeLink, final String importType) throws ApplicationException {
		this.length = xmlNodeLink.getLength();

		final Identifier startNodeId1 = Identifier.fromXmlTransferable(xmlNodeLink.getStartNodeId(), importType, MODE_THROW_IF_ABSENT);
		final Identifier endNodeId1 = Identifier.fromXmlTransferable(xmlNodeLink.getEndNodeId(), importType, MODE_THROW_IF_ABSENT);

		this.physicalLinkId = Identifier.fromXmlTransferable(xmlNodeLink.getPhysicalLinkId(), importType, MODE_THROW_IF_ABSENT);
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
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlNodeLink.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			NodeLink nodeLink;
			if (id.isVoid()) {
				nodeLink = new NodeLink(xmlId,
						importType,
						created,
						creatorId);
			} else {
				nodeLink = StorableObjectPool.getStorableObject(id, true);
				if (nodeLink == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					nodeLink = new NodeLink(xmlId,
							importType,
							created,
							creatorId);
				}
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

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicContainerWrappee()
	 */
	@Crutch134(notes = "Remove subclassing here.")
	public final StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		if (this.characteristicContainerWrappee == null) {
			this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE) {
				private static final long serialVersionUID = -2741783821486426615L;

				@Override
				protected void ensureCacheBuilt(final boolean usePool)
				throws ApplicationException {
					if (!this.cacheBuilt || usePool) {
						if (this.containees == null) {
							this.containees = new HashSet<Characteristic>();
						} else {
							this.containees.clear();
						}
						this.containees.addAll(StorableObjectPool.<Characteristic>getStorableObjectsByCondition(this.condition, false));
						this.cacheBuilt = true;
					}
				}
			};
		}
		return this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}

	public Characterizable getCharacterizable() {
		return null;
	}
}
