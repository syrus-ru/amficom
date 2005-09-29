/*-
 * $Id: Crutch136.java,v 1.1 2005/09/29 12:34:19 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.bugs;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @version $Revision: 1.1 $, $Date: 2005/09/29 12:34:19 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module bugs
 * @see <a href = "http://bass.science.syrus.ru/bugzilla/show_bug.cgi?id=136">This bug&apos;s homepage</a>
 */
@Retention(value = SOURCE)
@Target(value = {FIELD})
public @interface Crutch136 {
	String notes();
}
