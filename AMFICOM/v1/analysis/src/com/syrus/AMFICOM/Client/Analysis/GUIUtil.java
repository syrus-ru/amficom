/*-
 * $Id: GUIUtil.java,v 1.2 2005/04/18 16:33:37 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/04/18 16:33:37 $
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

