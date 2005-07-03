/*-
 * $Id: DateSpinner.java,v 1.2 2005/06/14 07:33:04 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/14 07:33:04 $
 * @author $Author: arseniy $
 * @module filterclient_v1
 */
public class DateSpinner extends JSpinner {
	private static final long serialVersionUID = 3907215931355967544L;

	private static final String	PATTERN	= "dd  MMMMMMMMM  yyyy";

	public DateSpinner() {
		super(new SpinnerDateModel());
		this.setEditor(new JSpinner.DateEditor(this, PATTERN));
	}
	
	public static String getPattern() {
		return PATTERN;
	}

}
