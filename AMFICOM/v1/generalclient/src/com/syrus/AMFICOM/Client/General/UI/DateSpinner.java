/*-
 * $Id: DateSpinner.java,v 1.3 2005/04/19 09:47:57 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/19 09:47:57 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class DateSpinner extends JSpinner {

	private static final String	PATTERN	= "dd  MMMMMMMMM  yyyy";

	public DateSpinner() {
		super(new SpinnerDateModel());
		this.setEditor(new JSpinner.DateEditor(this, PATTERN));
	}
	
	public static String getPattern() {
		return PATTERN;
	}

}
