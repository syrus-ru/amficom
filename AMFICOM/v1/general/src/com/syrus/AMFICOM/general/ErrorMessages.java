/*-
 * $Id: ErrorMessages.java,v 1.28.2.1 2006/02/27 16:13:20 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.28.2.1 $, $Date: 2006/02/27 16:13:20 $
 * @module general
 */
public final class ErrorMessages {
	public static final String NOT_LOGGED_IN = "NOT_LOGGED_IN";
	public static final String OBJECT_NOT_INITIALIZED = "object not initialized yet";
	public static final String NON_NULL_EXPECTED = "non-null value expected";
	public static final String NON_ZERO_EXPECTED = "non-zero value expected";
	public static final String NON_EMPTY_EXPECTED = "both non-null and non-empty string/array value expected";
	public static final String NON_VOID_EXPECTED = "both non-null and non-void identifier expected";
	public static final String ONLY_ONE_EXPECTED = "Only one object expected";
	public static final String CIRCULAR_DEPS_PROHIBITED = "circular dependencies not allowed";
	public static final String EXACTLY_ONE_PARENT_REQUIRED = "exactly one parent required";
	public static final String REMOVAL_OF_AN_ABSENT_PROHIBITED = "this collection is missing the element to be removed; removal of an absent member not allowed";
	public static final String OUT_OF_LIBRARY_HIERARCHY = "object doesn't belong to library hierarchy";
	public static final String UNSUPPORTED_CHILD_TYPE = "object doesn't realize any of interfaces supported within this library hierarchy";
	public static final String CHILDREN_PROHIBITED = "library hierarchy entry sterile";
	public static final String OBJECT_WILL_DELETE_ITSELF_FROM_POOL = "object will delete itself from pool";
	public static final String ACTION_WILL_RESULT_IN_NOTHING = "action will result in nothing";
	public static final String OBJECT_STATE_ILLEGAL = "object state illegal";
	public static final String OBJECT_BADLY_INITIALIZED = "object badly initialized";
	public static final String NO_COMMON_PARENT = "objects do not have a common parent";
	public static final String NATURE_INVALID = "nature invalid";
	public static final String CHILDREN_ALIEN = "object(s) alien with respect to this parent";
	public static final String TIMEOUT_TOO_SHORT = "Timeout too short, should be at least 10 min";
	public static final String METHOD_NOT_NEEDED = "Method not needed";
	public static final String OBJECTS_NOT_OF_THE_SAME_ENTITY = "Objects not of the same entity";
	public static final String OBJECTS_NOT_OF_THE_SAME_GROUP = "Objects not of the same group";
	public static final String ILLEGAL_ENTITY_CODE = "Illegal entity code";
	public static final String ILLEGAL_GROUP_CODE = "Illegal group code";
	public static final String GROUP_POOL_NOT_REGISTERED = "Group pool not registered";
	public static final String ENTITY_POOL_NOT_REGISTERED = "Entity pool not registered";
	public static final String NOT_IMPLEMENTED = "Not implemented";
	public static final String OPERATION_IS_OPTIONAL = "The operation is optional, and not implemented here";
	public static final String XML_BEAN_NOT_COMPLETE = "The xml bean is incomplete and thus not usable when updating a storable object";
	public static final String \u0425\u0423\u041b\u0418_\u041f\u0423\u0421\u0422\u041e\u0419 = "\u0425\u0443\u043b\u0438 \u043f\u0443\u0441\u0442\u043e\u0439-\u0442\u043e, \u0430?";

	private ErrorMessages() {
		assert false;
	}
}
