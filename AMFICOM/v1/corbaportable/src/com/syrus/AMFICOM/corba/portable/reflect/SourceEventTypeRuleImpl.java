package com.syrus.AMFICOM.corba.portable.reflect;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
final class SourceEventTypeRuleImpl {
	/**
	 * <code>VARCHAR2(64)</code>, primary key, can be <code>null</code>.
	 * Currently <code>null</code>.
	 */
	private String id;

	/**
	 * <code>VARCHAR2(240)</code>, can be <code>null</code>. Currently either
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
	 * @see SourceTypeEventTypeLinkImpl#defaultReactionSubstring
	 */
	private String logicText;

	/**
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>.
	 *
	 * Short description.
	 */
	private String name;

	/**************************************************************************
	 * External references.                                                   *
	 **************************************************************************/

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
	private EventTypeImpl eventTypeId;

	private SourceEventTypeRuleImpl() {
	}
}
