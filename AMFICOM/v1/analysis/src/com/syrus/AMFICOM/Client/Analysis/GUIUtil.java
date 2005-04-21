/*-
 * $Id: GUIUtil.java,v 1.3 2005/04/21 11:11:38 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/04/21 11:11:38 $
 * @module
 */
public class GUIUtil
{
	private GUIUtil () {
		// non-instantiable
	}
	public static void showErrorMessage(String codestring)
	{
		JOptionPane.showMessageDialog(
			Environment.getActiveWindow(),
			LangModelAnalyse.getString(codestring),
			LangModelAnalyse.getString("error"),
			JOptionPane.OK_OPTION);
	}
}
