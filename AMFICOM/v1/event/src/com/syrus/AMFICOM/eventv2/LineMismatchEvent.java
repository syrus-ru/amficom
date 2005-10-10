/*-
 * $Id: LineMismatchEvent.java,v 1.5 2005/10/10 11:12:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * Generation of this one may be triggered upon receipt of a
 * {@link ReflectogramMismatchEvent}.
 * 
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/10/10 11:12:46 $
 * @module event
 */
public interface LineMismatchEvent extends Event<IdlLineMismatchEvent> {
	/**
	 * @return mismatch type; see {@link AlarmType},
	 *         {@link com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAlarmType()}.
	 * @see AlarmType
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAlarmType()
	 */
	AlarmType getAlarmType();
	
	/**
	 * @return problem severity; see {@link Severity},
	 *         {@link com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getSeverity()}.
	 * @see Severity
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getSeverity()
	 */
	Severity getSeverity();

	/**
	 * @return {@code true} if <em>warning threshold</em> crossing degree is
	 *         defined; see
	 *         {@link com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#hasMismatch()}.
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#hasMismatch()
	 */
	boolean hasMismatch();

	/**
	 * @return lower bound of <em>warning threshold</em> crossing degree if
	 *         one is defined, i. e. {@link #hasMismatch()}<code>&nbsp;==&nbsp;true</code>.
	 *         In this case, it&apos;s guaranteed that
	 *         <code>0.0&nbsp;&lt;=&nbsp;</code>{@link #getMinMismatch()
	 *         minMismatch}<code>&nbsp;&lt;=&nbsp;</code>{@link
	 *         #getMaxMismatch() maxMismatch}<code>&nbsp;&lt;=&nbsp;1.0</code>.
	 * @throws IllegalStateException if threshold crossing degree is
	 *         undefined, i. e. {@link #hasMismatch()}<code>&nbsp;==&nbsp;false</code>.
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getMinMismatch()
	 */
	double getMinMismatch();

	/**
	 * @return upper bound of <em>warning threshold</em> crossing degree if
	 *         one is defined, i. e. {@link #hasMismatch()}<code>&nbsp;==&nbsp;true</code>.
	 *         In this case, it&apos;s guaranteed that
	 *         <code>0.0&nbsp;&lt;=&nbsp;</code>{@link #getMinMismatch()
	 *         minMismatch}<code>&nbsp;&lt;=&nbsp;</code>{@link
	 *         #getMaxMismatch() maxMismatch}<code>&nbsp;&lt;=&nbsp;1.0</code>.
	 * @throws IllegalStateException if threshold crossing degree is
	 *         undefined, i. e. {@link #hasMismatch()}<code>&nbsp;==&nbsp;false</code>.
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getMaxMismatch()
	 */
	double getMaxMismatch();

	/**
	 * @return identifier of the {@code PathElement} affected.
	 */
	Identifier getAffectedPathElementId();

	/**
	 * @return {@code true} if the {@code PathElement} pointed to by
	 *         {@link #getAffectedPathElementId() affectedPathElementId}
	 *         has a {@code length} (i. e. {@code physicalLength},
	 *         {@code opticalLength}) attribute. Currently, this is true for
	 *         {@code PathElement}s that encloses either a {@code SchemeLink}
	 *         or a {@code SchemeCableLink}.
	 */
	boolean isAffectedPathElementSpacious();

	/**
	 * @return physical distance in meters from starting point of the
	 *         {@code PathElement} affected, in case it is spatious, i. e.
	 *         {@link #isAffectedPathElementSpacious()
	 *         affectedPathElementSpacious}<code>&nbsp;==&nbsp;true</code>.
	 * @throws IllegalStateException if {@code PathElement} encloses a
	 *         {@code SchemeElement} and thus is <em>not</em> spatious, i. e.
	 *         {@link #isAffectedPathElementSpacious()
	 *         affectedPathElementSpacious}<code>&nbsp;==&nbsp;false</code>.
	 */
	double getPhysicalDistanceFromStart();

	/**
	 * @return physical distance in meters from ending point of the
	 *         {@code PathElement} affected, in case it is spatious, i. e.
	 *         {@link #isAffectedPathElementSpacious()
	 *         affectedPathElementSpacious}<code>&nbsp;==&nbsp;true</code>.
	 * @throws IllegalStateException if {@code PathElement} encloses a
	 *         {@code SchemeElement} and thus is <em>not</em> spatious, i. e.
	 *         {@link #isAffectedPathElementSpacious()
	 *         affectedPathElementSpacious}<code>&nbsp;==&nbsp;false</code>.
	 */
	double getPhysicalDistanceFromEnd();
}
