/*-
 * $Id: Crutch235.java,v 1.1 2005/10/23 18:23:39 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.bugs;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @version $Revision: 1.1 $, $Date: 2005/10/23 18:23:39 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module bugs
 * @see <a href = "http://bass.science.syrus.ru/bugzilla/show_bug.cgi?id=235">This bug&apos;s homepage</a>
 */
@Retention(value = SOURCE)
@Target(value = {TYPE, FIELD, METHOD})
public @interface Crutch235 {
	String notes();
}
