/*
 * $Id: ConnectionDialog.java,v 1.4 2004/09/25 19:40:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI.Session;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import java.awt.Frame;
import javax.swing.*;

/**
 * @version $Revision: 1.4 $, $Date: 2004/09/25 19:40:21 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public final class ConnectionDialog extends JDialog {
	/**
	 * @deprecated
	 */
	public int retCode = 0;

	/**
	 * @deprecated
	 */
	public static final int RET_OK = 1;

	/**
	 * @deprecated
	 */
	public static final int RET_CANCEL = 2;

	/**
	 * @deprecated
	 */
	public ConnectionDialog(Frame parent, String title, boolean modal) {
	}

	/**
	 * @deprecated
	 */
	public ConnectionDialog(ApplicationContext aContext) {
	}

	/**
	 * @deprecated
	 */
	public JButton buttonOk;

	/**
	 * @deprecated
	 */
	public JButton buttonCheck;

	/**
	 * @deprecated
	 */
	public JTextField fieldIP;

	/**
	 * @deprecated
	 */
	public JTextField fieldTCP;

	/**
	 * @deprecated
	 */
	public JTextField fieldSID;

	/**
	 * @deprecated
	 */
	public JTextField fieldObject;

	/**
	 * @deprecated
	 */
	public JTextField fieldUser;

	/**
	 * @deprecated
	 */
	public JTextField fieldPassword;
}
