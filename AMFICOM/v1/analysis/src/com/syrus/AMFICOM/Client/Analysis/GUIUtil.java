/*-
 * $Id: GUIUtil.java,v 1.17 2005/10/30 15:20:48 bass Exp $
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
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.17 $, $Date: 2005/10/30 15:20:48 $
 * @module
 */
public class GUIUtil {
	private static Map<String, Color> idColorMap = new HashMap<String, Color>();

	private static final String MSG_CREATE_OBJECT_PROBLEM = "createObjectProblem";
	private static final String MSG_ERROR_DATA_FORMAT = "errorDataReceivedUnrecognized";
	public static final String MSG_ERROR_DATABASE_FLUSH_ERROR = "errorDataBaseFlush";

	private static final String MSG_ERROR_COMMUNICATION_EXCEPTION = "errorCommunicationException";
	private static final String MSG_ERROR_DATABASE_EXCEPTION = "errorDatabaseException";

	public static final String MSG_ERROR_MALFORMED_ETALON = "errorMalformedEtalon";
	public static final String MSG_ERROR_EMPTY_NAME_ENTERED = "errorEmptyNameEntered";

	public static final String MSG_ERROR_NO_ONE_RESULT_HAS_TRACE = "errorResultHasNoTrace";
	public static final String MSG_WARNING_SOME_RESULTS_HAVE_NO_TRACE = "warningSomeResultsHaveNoTrace";

	public static final String MSG_ERROR_TRACE_ALREADY_LOADED = "errorTraceAlreadyLoaded";
	public static final String MSG_WARNING_SOME_TRACES_ARE_ALREADY_LOADED = "warningSomeTracesAlreadyLoaded";

	public static final String MSG_ERROR_INVALID_AP = "errorInvalidAP";

	private GUIUtil() {
		// non-instantiable
	}

	public static void showCreateObjectProblemError() {
		showErrorMessage(MSG_CREATE_OBJECT_PROBLEM);
	}

	public static void showDataFormatError() {
		showErrorMessage(MSG_ERROR_DATA_FORMAT);
	}

	/**
	 * "Отрабатывает" ApplicationException: выводит сообщение на экран и/или
	 * делает запись в Log
	 * 
	 * @param e
	 */
	public static void processApplicationException(ApplicationException e) {
		if (e instanceof CommunicationException) {
			e.printStackTrace();
			showErrorMessage(MSG_ERROR_COMMUNICATION_EXCEPTION);
		} else if (e instanceof DatabaseException) {
			e.printStackTrace();
			showErrorMessage(MSG_ERROR_DATABASE_EXCEPTION);
		} else {
			e.printStackTrace();
			assert Log.debugMessage(e, Log.DEBUGLEVEL03);
		}
	}

	public static void showErrorMessage(String codestring) {
		JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModelAnalyse.getString(codestring),
				LangModelAnalyse.getString("error"),
				JOptionPane.OK_OPTION);
	}

	public static void showWarningMessage(String codestring) {
		JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModelAnalyse.getString(codestring),
				LangModelAnalyse.getString("warning"),
				JOptionPane.OK_OPTION);
	}

	public static Color getColor(final String id) {
		Color color = null;
		// System.out.println("id is '" + id + "'");
		color = idColorMap.get(id);
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
				color = idColorMap.get(id1);
				if (color != null) {
					id1 = null;
				}
			}
			// System.out.println("by id:" + id1);
			color = UIManager.getColor(id1);
			if (color == null) {
				final Random random = new Random();
				// System.out.println("by random");
				color = new Color(Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256);
			}
			idColorMap.put(id1, color);
			if (!id1.equals(id)) {
				idColorMap.put(id, color);
			}
		}
		// System.out.println(color);
		// System.out.println();
		return color;
	}
}
