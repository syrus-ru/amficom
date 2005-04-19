/*-
 * $Id: TimeSpinner.java,v 1.5 2005/04/19 09:47:57 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/19 09:47:57 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class TimeSpinner extends JSpinner {

	private static final String	PATTERN	= "HH:mm";

	public TimeSpinner() {
		super(new SpinnerDateModel());
		this.setEditor(new JSpinner.DateEditor(this, PATTERN));
	}

	public static String getPattern() {
		return PATTERN;
	}

}
