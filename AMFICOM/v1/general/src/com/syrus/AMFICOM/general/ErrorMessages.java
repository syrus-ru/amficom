/*-
 * $Id: ErrorMessages.java,v 1.15 2005/05/20 08:23:02 bass Exp $
 *
 * Copyright ø 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/05/20 08:23:02 $
 * @module general_v1
 */
public interface ErrorMessages {
	String OBJECT_NOT_INITIALIZED = "object not initialized yet";
	String NON_NULL_EXPECTED = "non-null value expected";
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
	String ËıÏÈ_ıÛÙÔÍ = "Ë’Ã… –’”‘œ -‘œ, ¡?";
	String TIMEOUT_TOO_SHORT = "Timeout too short, should be at least 10 min";
}
