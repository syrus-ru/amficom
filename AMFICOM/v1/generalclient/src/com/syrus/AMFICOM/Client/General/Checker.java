/*
 * $Id: Checker.java,v 1.10 2004/08/29 13:38:14 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2004/08/29 13:38:14 $
 * @module generalclient_v1
 */
public final class Checker {
	/////////////////////////////////////////////////////////////
	////        The set of the permissions attributes       /////
	/////////////////////////////////////////////////////////////
	public static final String read    = "r";
	public static final String write   = "w";
	public static final String execute = "x";

	public static final String model = "MOD";
	public static final String admin = "ADMIN";
	public static final String optim = "OPTIM";
	public static final String plan  = "PLAN";
	public static final String ana   = "ANALYS";
	public static final String config   = "CONFIG";
	public static final String observ   = "OBSERV";
	public static final String predict  = "PREDICT";

	///////////////////////////////////////////////////
	////          The set of the commands         /////
	///////////////////////////////////////////////////

	//--------------- ADMINISTRATING ----------------//
	/* 1 */
	public static final String openAdminWindow = admin + "openAdminWindow";

	/* 2 */
	public static final String modifyGroup = admin + "modifyGroup";
	/* 3 */ 
	public static final String addGroup = admin + "addGroup";
	/* 4 */
	public static final String addProfile = admin + "addProfile";
	/* 5 */  
	public static final String modifyProfile = admin + "modifyProfile";
	/* 6 */  
	public static final String modifyExec = admin + "modifyExec";
	/* 7 */  
	public static final String readGroupInfo = admin + "readGroupInfo";
	/* 8 */  
	public static final String readProfileInfo = admin + "readProfileInfo";
	/* 9 */  
	public static final String readExecInfo = admin + "readExecInfo";
	/* 10*/  
	public static final String addDomain = admin + "addDomain";
	/* 11*/  
	public static final String modifyDomain = admin + "modifyDomain";
	/* 12*/  
	public static final String readDomainInfo = admin + "readDomainInfo";
	/* 13*/  
	public static final String removeDomain = admin + "removeDomain";
	/* 14*/  
	public static final String removeProfile = admin + "removeProfile";
	/* 15*/  
	public static final String removeGroup = admin + "removeGroup";
	/* 16*/  
	public static final String modifyCategory = admin + "modifyCategory";
	/* 17*/  
	public static final String readCategoryInfo = admin + "readCategoryInfo";
	/* 18*/  
	public static final String readUserInfo = admin + "readUserInfo";

	/* 19*/  
	public static final String readAgentInfo = admin + "readAgentInfo";
	/* 20*/  
	public static final String modifyAgentInfo = admin + "modifyAgentInfo";
	/* 21*/  
	public static final String createAgent = admin + "createAgent";
	/* 22*/  
	public static final String deleteAgent = admin + "deleteAgent";

	/* 23*/  
	public static final String readServerInfo = admin + "readServerInfo";
	/* 24*/  
	public static final String modifyServerInfo = admin + "modifyServerInfo";
	/* 25*/  
	public static final String createServer = admin + "createServer";
	/* 26*/  
	public static final String deleteServer = admin + "deleteServer";

	/* 27*/  
	public static final String readClientInfo = admin + "readClientInfo";
	/* 28*/  
	public static final String modifyClientInfo = admin + "modifyClientInfo";
	/* 29*/  
	public static final String createClient = admin + "createClient";
	/* 30*/  
	public static final String deleteClient = admin + "deleteClient";

	//-------- PLANNER OF THE REFLECTO-TESTS --------//
	/* 1 */ 
	public static final String setOneTimeTest = plan + "setOneTimeTest";
	/* 2 */  
	public static final String setTestTimeTable = plan + "setTestTimeTable";
	/* 3 */  
	public static final String setPeriodicTest = plan + "setPeriodicTest";
	/* 4 */  
	public static final String refreshTest = plan + "refreshTest";
	/* 5 */  
	public static final String addTests = plan + "addTests";
	/* 6 */  
	public static final String saveTests = plan + "saveTests";
	/* 7 */  
	public static final String setOneKIStest = plan + "setOneKIStest";
	/* 8 */  
	public static final String setAllKIStest = plan + "setAllKIStest";
	/* 9 */  
	public static final String setUsualTest = plan + "setUsualTest";
	/* 10*/  
	public static final String setExtendedTest = plan + "setExtendedTest";
	/* 11*/  
	public static final String setTestFiltration = plan + "setTestFiltration";
	/* 12*/  
	public static final String openTestPlannerWindow = plan + "openTestPlannerWindow";

	//-- ANALYSIS, EXTENDED ANALYSIS AND THRESHOLDS --//
	/* 1 */
	public static final String enterAnalysisModul = ana + "enterAnalysisModul";
	/* 2 */
	public static final String enterExtendedAnalysisModul = ana + "enterExtendedAnalysisModul";
	/* 3 */
	public static final String enterThresholdModul = ana + "enterThresholdModul";

	/* 4 */
	public static final String performDeterminedAnalysis  = ana + "performDeterminedAnalysis";
	/* 5 */
	public static final String performWaveletAnalysis  = ana + "performWaveletAnalysis";
	/* 6 */
	public static final String performMINUITanalysis = ana + "performMINUITanalysis";

	/* 7 */
	public static final String openReflectogrammFile  = ana + "openReflectogrammFile";
	/* 8 */
	public static final String saveReflectogrammFile = ana + "saveReflectogrammFile";
	/* 9 */
	public static final String loadReflectogrammFromDB = ana + "loadReflectogrammFromDB";
	/* 10*/
	public static final String loadReferenceTrace = ana + "loadReferenceTrace";

	//--MODELING (CALCULATING OF THE REFLECTOGRAMM)--//
	/* 1 */ 
	public static final String enterReflectoModelingWindow = model + "enterReflectoModelingWindow";
	/* 2 */ 
	public static final String openSchemeForModeling = model + "openSchemeForModeling";
	/* 3 */ 
	public static final String performReflectoModeling = model + "performReflectoModeling";
	/* 4 */ 
	public static final String saveReflectoModeling = model + "saveReflectoModeling";
	/* 5 */ 
	public static final String setParamsReflectoModeling = model + "setParamsReflectoModeling";
	/* 6 */ 
	public static final String openMapForModeling = model + "openMapForModeling";


	//------- OPTIMIZATOR OF THE OPTICAL NET --------//
	/* 1 */
	public static final String startOptimization = optim + "startOptimization";
	/* 2 */
	public static final String stopOptimization = optim + "stopOptimization";
	/* 3 */
	public static final String saveResultOfOptimization = optim + "saveResultOfOptimization";
	/* 4 */
	public static final String setParamsForOptimization = optim + "setParamsForOptimization";
	/* 5 */
	public static final String saveParamsForOptimization = optim + "saveParamsOfOptimization";
	/* 6 */ 
	public static final String enterOptimizationModul = optim + "enterOptimizationModul";
	/* 7 */
	public static final String openMapToBeOptimized = optim + "openMaptoBeOptimized";
	/* 8 */
	public static final String openSchemeToBeOptimized = optim + "openSchemeToBeOptimized";

	//------- CONFIGURING OF THE SYSTEM ---------//
	/* 0 */
	public static final String enterConfiguringModul = config + "enterConfiguringModul";
	/* 1 */
	public static final String componentEditing = config + "componentEditing";
	/* 2 */
	public static final String schemeEditing = config + "schemeEditing";
	/* 3 */
	public static final String topologyEditing = config + "topologyEditing";
	/* 4 */
	public static final String schemeViewing = config + "schemeViewing";
	/* 5 */
	public static final String topologyViewing = config + "topologyViewing";
	/* 6 */
	public static final String catalogTCviewing = config + "catalogTCviewing";
	/* 7 */
	public static final String catalogTCediting = config + "catalogTCediting";
	/* 8 */
	public static final String catalogCMviewing = config + "catalogCMviewing";
	/* 9 */
	public static final String catalogCMediting = config + "catalogCMediting";

	//------- OBSERVING MODUL ----------//
	/* 0 */
	public static final String enterObservingModul = observ + "enterObservingModul";
	/* 1 */
	public static final String viewingOperativeInformation = observ + "viewingOperativeInformation";
	/* 2 */
	public static final String viewingChangeArchive = observ + "viewingChangeArchive";
	/* 3 */
	public static final String fastTaskSetting = observ + "fastTaskSetting";
	/* 4 */
	public static final String reflectogrammObserving = observ + "reflectogrammObserving";
	/* 5 */
	public static final String alarmsObserving = observ + "alarmsObserving";
	/* 6 */
	public static final String alarmSignalAssurement = observ + "alarmSignalAssurement";
	/* 7 */
	public static final String alarmSignalDeleting = observ + "alarmSignalDeleting";
	/* 8 */
	public static final String alarmSignalStopping = observ + "alarmSignalStopping";

	//------- PREDICTION MODUL ---------//
	/* 0 */
	public static final String enterPredictionModul = predict + "enterPredictionModul";
	/* 1 */
	public static final String calculatePredictedReflectogramm = predict + "calculatePredictedReflectogramm";
	/* 2 */
	public static final String loadDataForPrediction = predict + "loadDataForPrediction";
	/* 3 */
	public static final String savePredictedReflectogramm = predict + "savePredictedReflectogramm";

	User user;

	public Checker(DataSourceInterface dsi) {
		if (dsi != null)
			this.user = (User) (Pool.get(User.typ, dsi.getSession().getUserId()));
		else
			this.user = null;
	}

	public Checker(SessionInterface si) {
		if (si != null)
			this.user = (User) (Pool.get(User.typ, si.getUserId()));
		else
			this.user = null;
	}

	public Checker(User user) {
		this.user = user;
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//  Defenition of the object checkers.

	public boolean checkObject(String objectTyp, String objectId, String operationType) {
		return checkObject(objectTyp, objectId, operationType, true);
	}
	
	public boolean checkObject(String objectTyp, String objectId, String operationType, boolean showMessage) {
		return checkObject(objectTyp, objectId, operationType, this.user, showMessage);
	}

	public static boolean checkObject(String userId, String objectTyp, String objectId, String operationType, boolean showMessage) {
		return checkObject(objectTyp, objectId, operationType, (User) (Pool.get(User.typ, userId)), showMessage);
	}

	public static boolean checkObject(String userId, String objectTyp, String objectId, String operationType) {
		return checkObject(userId, objectTyp, objectId, operationType, true);
	}

	private static boolean checkObject(final String typ, final String id, final String operationType, final User user, final boolean showMessage) {
		if (user == null) {
			if (showMessage)
				JOptionPane.showMessageDialog(null, "Права пользователя не установлены.", "Ошибка", JOptionPane.OK_OPTION);
			return false;
		}
		if (user.login.equals("sys"))
			return true;

		ObjectPermissionAttributes opa = null;
		if (typ.equals(Domain.typ)) {
			Domain dom = (Domain)Pool.get(typ, id);
			if (dom == null)
				return true;
			opa = dom.opa;
		}
		if (opa == null)
			return true;

		if (opa.owner_id.equals(user.id)) {
			if (opa.userR && operationType.equals(read))
				return true;
			if (opa.userW && operationType.equals(write))
				return true;
			if (opa.userX && operationType.equals(execute))
				return true;
		} else if (hasEqualElements(opa.group_ids, user.group_ids)) {
			if (opa.groupR && operationType.equals(read))
				return true;
			if (opa.groupW && operationType.equals(write))
				return true;
			if (opa.groupX && operationType.equals(execute))
				return true;
		} else {
			if(opa.otherR && operationType.equals(read))
				return true;
			if(opa.otherW && operationType.equals(write))
				return true;
			if(opa.otherX && operationType.equals(execute))
				return true;
		}

		if (showMessage)
			JOptionPane.showMessageDialog(null, opa.whyRejected, "Ошибка", JOptionPane.OK_OPTION);

		return false;
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//  Defenition of the command checkers.
	public boolean checkCommand(String commandName) {
		return checkCommand(commandName, this.user);
	}

	public static boolean checkCommand(User user, String commandName) {
		return checkCommand(commandName, user);
	}

	public static boolean checkCommandByUserId(String userID, String commandName) {
		return checkCommand(commandName, (User) (Pool.get(User.typ, userID)));
	}

	private static boolean checkCommand(String commandName, User user) {
		if (user == null) {
			JOptionPane.showMessageDialog(null, "Права пользователя не установлены", "Ошибка", JOptionPane.OK_OPTION);
			return false;
		}

		if (user.login.equals("sys"))
			return true;

		CommandPermissionAttributes cpa = getCommand(commandName);
		if ((cpa == null) || cpa.owner_id.equals(user.id) || hasEqualElements(cpa.category_ids, user.category_ids))
			return true;

		JOptionPane.showMessageDialog(null, cpa.whyRejected, "Ошибка", JOptionPane.OK_OPTION);
		return false;
	}

	// Definition of the helping functions.
	private static CommandPermissionAttributes getCommand(final String commandName) {
		Map map = Pool.getMap(CommandPermissionAttributes.typ);
		if (map == null)
			return null;

		for (Iterator iterator = map.values().iterator(); iterator.hasNext();) {
			CommandPermissionAttributes tmp = (CommandPermissionAttributes) (iterator.next());
			if (tmp.codename.equals(commandName))
				return tmp;
		}
		return null;
	}

 	private static boolean hasEqualElements(final Collection c1, final Collection c2) {
		for (Iterator iterator = c1.iterator(); iterator.hasNext();)
			if (c2.contains(iterator.next()))
				return true;
		return false;
	}
}
