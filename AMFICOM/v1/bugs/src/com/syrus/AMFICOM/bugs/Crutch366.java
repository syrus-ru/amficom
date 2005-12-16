/*-
 * $Id: Crutch366.java,v 1.1 2005/12/16 11:15:47 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.bugs;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @version $Revision: 1.1 $, $Date: 2005/12/16 11:15:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module bugs
 */
@Retention(value = SOURCE)
@Target(value = {TYPE, METHOD})
public @interface Crutch366 {
	String notes();
}
