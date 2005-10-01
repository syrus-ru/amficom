/*-
 * $Id: ErrorMessages.java,v 1.26 2005/10/01 09:27:27 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.26 $, $Date: 2005/10/01 09:27:27 $
 * @module general
 */
public interface ErrorMessages {
	String NOT_LOGGED_IN = "NOT_LOGGED_IN";
	String OBJECT_NOT_INITIALIZED = "object not initialized yet";
	String NON_NULL_EXPECTED = "non-null value expected";
	String NON_ZERO_EXPECTED = "non-zero value expected";
	String NON_EMPTY_EXPECTED = "both non-null and non-empty string/array value expected";
	String NON_VOID_EXPECTED = "both non-null and non-void identifier expected";
	String CIRCULAR_DEPS_PROHIBITED = "circular dependencies not allowed";
	String EXACTLY_ONE_PARENT_REQUIRED = "exactly one parent required";
	String REMOVAL_OF_AN_ABSENT_PROHIBITED = "this collection is missing the element to be removed; removal of an absent member not allowed";
	String OUT_OF_LIBRARY_HIERARCHY = "object doesn't belong to library hierarchy";
	String UNSUPPORTED_CHILD_TYPE = "object doesn't realize any of interfaces supported within this library hierarchy";
	String CHILDREN_PROHIBITED = "library hierarchy entry sterile";
	String OBJECT_WILL_DELETE_ITSELF_FROM_POOL = "object will delete itself from pool";
	String ACTION_WILL_RESULT_IN_NOTHING = "action will result in nothing";
	String OBJECT_STATE_ILLEGAL = "object state illegal";
	String OBJECT_BADLY_INITIALIZED = "object badly initialized";
	String NO_COMMON_PARENT = "objects do not have a common parent";
	String NATURE_INVALID = "nature invalid";
	String CHILDREN_ALIEN = "object(s) alien with respect to this parent";
	String TIMEOUT_TOO_SHORT = "Timeout too short, should be at least 10 min";
	String METHOD_NOT_NEEDED = "Method not needed";
	String OBJECTS_NOT_OF_THE_SAME_ENTITY = "Objects not of the same entity";
	String OBJECTS_NOT_OF_THE_SAME_GROUP = "Objects not of the same group";
	String ILLEGAL_ENTITY_CODE = "Illegal entity code";
	String ILLEGAL_GROUP_CODE = "Illegal group code";
	String GROUP_POOL_NOT_REGISTERED = "Group pool not registered";
	String ENTITY_POOL_NOT_REGISTERED = "Entity pool not registered";
	String NOT_IMPLEMENTED = "Not implemented";
	String OPERATION_IS_OPTIONAL = "The operation is optional, and not implemented here";
	String XML_BEAN_NOT_COMPLETE = "The xml bean is incomplete and thus not usable when updating a storable object";
	String PERSISTENCE_COUNTER_NEGATIVE = "Persistence counter is negative: ";
	String \u0425\u0423\u041b\u0418_\u041f\u0423\u0421\u0422\u041e\u0419 = "\u0425\u0443\u043b\u0438 \u043f\u0443\u0441\u0442\u043e\u0439-\u0442\u043e, \u0430?";
}
