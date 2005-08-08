/*-
 * $Id: TimeSpinner.java,v 1.3 2005/08/08 11:41:00 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:41:00 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class TimeSpinner extends JSpinner {
	private static final long serialVersionUID = 3690758401483223600L;

	private static final String	PATTERN	= "HH:mm";

	public TimeSpinner() {
		super(new SpinnerDateModel(new Date(), null, null, Calendar.HOUR));
		this.setEditor(new JSpinner.DateEditor(this, PATTERN));
	}

	public static String getPattern() {
		return PATTERN;
	}

}
