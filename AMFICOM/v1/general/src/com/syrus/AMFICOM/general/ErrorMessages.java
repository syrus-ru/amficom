/*-
 * $Id: ErrorMessages.java,v 1.8 2005/03/25 18:06:48 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/03/25 18:06:48 $
 * @module general_v1
 */
public interface ErrorMessages {
	String OBJECT_NOT_INITIALIZED = "object not initialized yet"; //$NON-NLS-1$
	String NON_NULL_EXPECTED = "non-null value expected"; //$NON-NLS-1$
	String NON_EMPTY_EXPECTED = "both non-null and non-empty string value expected"; //$NON-NLS-1$
	String NON_VOID_EXPECTED = "both non-null and non-void identifier expected"; //$NON-NLS-1$
	String CIRCULAR_DEPS_PROHIBITED = "circular dependencies not allowed"; //$NON-NLS-1$
	String MULTIPLE_PARENTS_PROHIBITED = "multiple parents not allowed"; //$NON-NLS-1$
	String PARENTLESS_CHILD_PROHIBITED = "exactly one parent required"; //$NON-NLS-1$
	String COLLECTION_IS_A_SET = "logically, this collection must be a set; double-addition of elements not allowed"; //$NON-NLS-1$
	String REMOVAL_OF_AN_ABSENT_PROHIBITED = "this collection is missing the element to be removed; removal of an absent member not allowed"; //$NON-NLS-1$
	String OUT_OF_LIBRARY_HIERARCHY = "object doesn't belong to library hierarchy"; //$NON-NLS-1$
	String UNSUPPORTED_CHILD_TYPE = "object doesn't realize any of interfaces supported within this library hierarchy"; //$NON-NLS-1$
	String CHILDREN_PROHIBITED = "library hierarchy entry sterile"; //$NON-NLS-1$
	String OBJECT_WILL_DELETE_ITSELF_FROM_POOL = "object will delete itself from pool"; //$NON-NLS-1$
	String ACTION_WILL_RESULT_IN_NOTHING = "action will result in nothing"; //$NON-NLS-1$
	String OBJECT_STATE_ILLEGAL = "object state illegal"; //$NON-NLS-1$
	String OBJECT_BADLY_INITIALIZED = "object badly initialized"; //$NON-NLS-1$
	String NO_COMMON_PARENT = "objects do not have a common parent"; //$NON-NLS-1$
}
