/*
 * $Id: ErrorMessages.java,v 1.4 2005/03/23 14:59:34 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/23 14:59:34 $
 * @module general_v1
 */
public interface ErrorMessages {
	String OBJECT_NOT_INITIALIZED = "object hasn't been initialized yet"; //$NON-NLS-1$
	String NON_NULL_EXPECTED = "non-null value expected"; //$NON-NLS-1$
	String NON_EMPTY_EXPECTED = "both non-null and non-empty string value expected"; //$NON-NLS-1$
	String NON_VOID_EXPECTED = "both non-null and non-void identifier expected"; //$NON-NLS-1$
	String CIRCULAR_DEPS_PROHIBITED = "circular dependencies are not allowed"; //$NON-NLS-1$
	String MULTIPLE_PARENTS_PROHIBITED = "multiple parents are not allowed"; //$NON-NLS-1$
	String HEADLESS_CHILD_PROHIBITED = "at least one parent required"; //$NON-NLS-1$
	String COLLECTION_IS_A_SET = "logically, this collection must be a set; double-addition of elements is not allowed"; //$NON-NLS-1$
	String REMOVAL_OF_AN_ABSENT_PROHIBITED = "this collection is missing the element to be removed; removal of an absent member is not allowed"; //$NON-NLS-1$
}
