/*
 * $Id: AlarmImpl.java,v 1.2 2004/12/22 15:10:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.event.corba.AlarmStatus;
import java.sql.Timestamp;

/**
 * @version $Revision: 1.2 $, $Date: 2004/12/22 15:10:55 $
 * @author $Author: bass $
 * @module corbaportable_v1
 */
final class AlarmImpl {
	/**
	 * Value: {@value}.
	 */
	private static final int STATUS_GENERATED = AlarmStatus._ALARM_STATUS_GENERATED;

	/**
	 * Value: {@value}.
	 */
	private static final int STATUS_ASSIGNED = AlarmStatus._ALARM_STATUS_ASSIGNED;

	/**
	 * Value: {@value}.
	 */
	private static final int STATUS_FIXED = AlarmStatus._ALARM_STATUS_FIXED;

	/**
	 * Value: {@value}.
	 */
	private static final int STATUS_DELETED = AlarmStatus._ALARM_STATUS_DELETED;

	/**
	 * <code>VARCHAR2(64)</code>, primary key.
	 *
	 * Is generated from a sequence and has the form
	 * &quot;<code>alrm&lt;sequence&nbsp;value&gt;</code>&quot;.
	 */
	private String id;

	/**
	 * <code>NUMBER(22)</code>, scale 0. Can be one of:
	 * <ul>
	 *     <li>{@link #STATUS_GENERATED STATUS_GENERATED};</li>
	 *     <li>{@link #STATUS_ASSIGNED STATUS_ASSIGNED};</li>
	 *     <li>{@link #STATUS_FIXED STATUS_FIXED};</li>
	 *     <li>{@link #STATUS_DELETED STATUS_DELETED}.</li>
	 * </ul>
	 */
	private int status;

	/**
	 * <code>VARCHAR2(256)</code>, can be <code>null</code>.
	 *
	 * Long description.
	 */
	private String comments;

	/**
	 * <code>DATE(7)</code>, can be <code>null</code>.
	 *
	 * @see #assignedTo
	 */
	private Timestamp assigned;

	/**
	 * <code>DATE(7)</code>, can be <code>null</code>.
	 */
	private Timestamp deleted;

	/**
	 * <code>DATE(7)</code>, can be <code>null</code>.
	 *
	 * @see #fixedBy
	 */
	private Timestamp fixed;

	/**
	 * <code>DATE(7)</code>, can be <code>null</code>.
	 */
	private Timestamp generated;

	/**
	 * <code>DATE(7)</code>, can be <code>null</code>.
	 */
	private Timestamp modified;

	/**************************************************************************
	 * External references.                                                   *
	 **************************************************************************/

	/**
	 * <code>VARCHAR2(64)</code>, foreign key, can be <code>null</code>.
	 *
	 * @deprecated No need in this field as we can use
	 *             <code>alarms.event_id.domain_id</code>.
	 * @see #eventId
	 * @see EventImpl#domainId
	 */
	private DomainImpl domainId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventImpl#id
	 */
	private EventImpl eventId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @deprecated No need in this field as we can use
	 *             <code>alarms.event_id.source_id</code>.
	 * @see #eventId
	 * @see EventImpl#sourceId
	 */
	private EventSourceImpl sourceId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see AlarmTypeImpl#id
	 */
	private AlarmTypeImpl typeId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key, can be <code>null</code>.
	 *
	 * @see UserImpl#id
	 * @see #assigned
	 */
	private UserImpl assignedTo;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key, can be <code>null</code>.
	 *
	 * @see UserImpl#id
	 * @see #fixed
	 */
	private UserImpl fixedBy;

	private AlarmImpl() {
	}
}
