package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.CORBA.General.EventStatus;
import java.sql.Timestamp;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
public final class EventImpl {
	/**
	 * Value: {@value}.
	 */
	private static final int STATUS_GENERATED = EventStatus._EVENT_STATUS_GENERATED;  
	
	/**
	 * Value: {@value}.
	 */
	private static final int STATUS_PROCESSED = EventStatus._EVENT_STATUS_PROCESSED;

	/**
	 * <code>VARCHAR2(64)</code>, primary key.
	 *
	 * Is generated from a sequence and has the form
	 * &quot;<code>sysev&lt;sequence&nbsp;value&gt;</code>&quot;.
	 */
	private String id;

	/**
	 * <code>NUMBER(22)</code>, scale 0. Can be one of:
	 * <ul>
	 *     <li>{@link #STATUS_GENERATED STATUS_GENERATED};</li>
	 *     <li>{@link #STATUS_PROCESSED STATUS_PROCESSED}.</li>
	 * </ul>
	 */
	private int status;
	
	/**
	 * <code>DATE(7)</code>, can be <code>null</code>.
	 */
	private Timestamp created;

	/**
	 * <code>VARCHAR2(256)</code>, can be <code>null</code>.
	 *
	 * Long description.
	 */
	private String description;

	/*
	 * External references.
	 **************************************************************************/

	/**
	 * <code>VARCHAR2(64)</code>, foreign key, can be <code>null</code>.
	 *
	 * @see DomainImpl#id
	 */
	private DomainImpl domainId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventSourceImpl#id
	 */
	private EventSourceImpl sourceId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventTypeImpl#id
	 */
	private EventTypeImpl typeId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key, can be <code>null</code>.
	 *
	 * @see ResultImpl#id
	 */
	private ResultImpl descriptor;

	EventImpl() {
	}

	/**
	 * Getter for property id.
	 *
	 * @return Value of property id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter for property status.
	 *
	 * @return Value of property status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Getter for property created.
	 *
	 * @return Value of property created.
	 */
	public Timestamp getCreated() {
		return created;
	}

	/**
	 * Getter for property description.
	 *
	 * @return Value of property description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter for property domainId.
	 *
	 * @return Value of property domainId.
	 */
	public DomainImpl getDomainId() {
		return domainId;
	}

	/**
	 * Getter for property sourceId.
	 *
	 * @return Value of property sourceId.
	 */
	public EventSourceImpl getSourceId() {
		return sourceId;
	}

	/**
	 * Getter for property typeId.
	 *
	 * @return Value of property typeId.
	 */
	public EventTypeImpl getTypeId() {
		return typeId;
	}

	/**
	 * Getter for property descriptor.
	 *
	 * @return Value of property descriptor.
	 */
	public ResultImpl getDescriptor() {
		return descriptor;
	}
}
