/*
 * $Id: ErrorMessages.java,v 1.3 2005/03/21 16:43:46 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/21 16:43:46 $
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
}
