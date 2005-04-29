/*-
 * $Id: GUIUtil.java,v 1.4 2005/04/29 06:46:08 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/04/29 06:46:08 $
 * @module
 */
public class GUIUtil
{
    private static Map idColorMap = new HashMap();

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

    public static Color getColor(String id) {
        Color color = null;
        // System.out.println("id is '" + id + "'");
        color = (Color) idColorMap.get(id);
        if (color == null) {
            color = UIManager.getColor(id);
            if (color != null) {
                idColorMap.put(id, color);
            }
        }
        if (color == null) {
            int i = 0;
            String id1 = null;
            while (id1 == null) {
                id1 = AnalysisResourceKeys.COLOR_TRACE_PREFIX + i++;
                // System.out.println("search by " + id1);
                color = (Color) idColorMap.get(id1);
                if (color != null)
                    id1 = null;
            }
            // System.out.println("by id:" + id1);
            color = UIManager.getColor(id1);
            if (color == null) {
                Random random = new Random();
                // System.out.println("by random");
                color = new Color(Math.abs(random.nextInt()) % 256,
                        Math.abs(random.nextInt()) % 256,
                        Math.abs(random.nextInt()) % 256);
            }
            idColorMap.put(id1, color);
            if (!id1.equals(id))
                idColorMap.put(id, color);
        }
        // System.out.println(color);
        // System.out.println();
        return color;
    }
}
