package com.syrus.AMFICOM.corba.portable.reflect;

/**
 * This class represents a table containing default (hardcoded) rules, which
 * cannot be modified, but just enabled or disabled.
 *
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
final class SourceTypeEventTypeLinkImpl {
	/**
	 * <code>NUMBER(22)</code>, scale 0. The enabled/disabled flag of this rule;
	 * can be toggled by an operator.
	 */
	private boolean isGenerated;

	/**
	 * <code>VARCHAR2(256)</code>, can be <code>null</code>. Currently either
	 * <code>null</code> or
	 * &quot;<code>GENERATE_ALARM(&lt;string&gt;)</code>&quot;, where
	 * <code>&lt;string&gt;</code> can be one of:
	 * <ul>
	 *     <li>{@link AlarmTypeImpl#ID_RTU_HW_ALARM AlarmTypeImpl.ID_RTU_HW_ALARM};</li>
	 *     <li>{@link AlarmTypeImpl#ID_RTU_SW_ALARM AlarmTypeImpl.ID_RTU_SW_ALARM};</li>
	 *     <li>{@link AlarmTypeImpl#ID_RTU_TEST_ALARM AlarmTypeImpl.ID_RTU_TEST_ALARM};</li>
	 *     <li>{@link AlarmTypeImpl#ID_RTU_TEST_WARNING AlarmTypeImpl.ID_RTU_TEST_WARNING}.</li>
	 * </ul>
	 *
	 * @see SourceEventTypeRuleImpl#logicText
	 */
	private String defaultReactionSubstring;

	/**************************************************************************
	 * External references.                                                   *
	 **************************************************************************/

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventSourceTypeImpl#id
	 */
	private EventSourceTypeImpl sourceTypeId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventTypeImpl#id
	 */
	private EventTypeImpl eventTypeId;

	private SourceTypeEventTypeLinkImpl() {
	}
}
