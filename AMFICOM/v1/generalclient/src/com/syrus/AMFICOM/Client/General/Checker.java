package com.syrus.AMFICOM.Client.General;

import java.util.*;


import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import javax.swing.*;


public class Checker
{
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
/* 1 */  public static final String openAdminWindow = admin + "openAdminWindow";

/* 2 */  public static final String modifyGroup = admin + "modifyGroup";
/* 3 */  public static final String addGroup = admin + "addGroup";
/* 4 */  public static final String addProfile = admin + "addProfile";
/* 5 */  public static final String modifyProfile = admin + "modifyProfile";
/* 6 */  public static final String modifyExec = admin + "modifyExec";
/* 7 */  public static final String readGroupInfo = admin + "readGroupInfo";
/* 8 */  public static final String readProfileInfo = admin + "readProfileInfo";
/* 9 */  public static final String readExecInfo = admin + "readExecInfo";
/* 10*/  public static final String addDomain = admin + "addDomain";
/* 11*/  public static final String modifyDomain = admin + "modifyDomain";
/* 12*/  public static final String readDomainInfo = admin + "readDomainInfo";
/* 13*/  public static final String removeDomain = admin + "removeDomain";
/* 14*/  public static final String removeProfile = admin + "removeProfile";
/* 15*/  public static final String removeGroup = admin + "removeGroup";
/* 16*/  public static final String modifyCategory = admin + "modifyCategory";
/* 17*/  public static final String readCategoryInfo = admin + "readCategoryInfo";
/* 18*/  public static final String readUserInfo = admin + "readUserInfo";

/* 19*/  public static final String readAgentInfo = admin + "readAgentInfo";
/* 20*/  public static final String modifyAgentInfo = admin + "modifyAgentInfo";
/* 21*/  public static final String createAgent = admin + "createAgent";
/* 22*/  public static final String deleteAgent = admin + "deleteAgent";

/* 23*/  public static final String readServerInfo = admin + "readServerInfo";
/* 24*/  public static final String modifyServerInfo = admin + "modifyServerInfo";
/* 25*/  public static final String createServer = admin + "createServer";
/* 26*/  public static final String deleteServer = admin + "deleteServer";

/* 27*/  public static final String readClientInfo = admin + "readClientInfo";
/* 28*/  public static final String modifyClientInfo = admin + "modifyClientInfo";
/* 29*/  public static final String createClient = admin + "createClient";
/* 30*/  public static final String deleteClient = admin + "deleteClient";

//-------- PLANNER OF THE REFLECTO-TESTS --------//
/* 1 */  public static final String setOneTimeTest = plan + "setOneTimeTest";
/* 2 */  public static final String setTestTimeTable = plan + "setTestTimeTable";
/* 3 */  public static final String setPeriodicTest = plan + "setPeriodicTest";
/* 4 */  public static final String refreshTest = plan + "refreshTest";
/* 5 */  public static final String addTests = plan + "addTests";
/* 6 */  public static final String saveTests = plan + "saveTests";
/* 7 */  public static final String setOneKIStest = plan + "setOneKIStest";
/* 8 */  public static final String setAllKIStest = plan + "setAllKIStest";
/* 9 */  public static final String setUsualTest = plan + "setUsualTest";
/* 10*/  public static final String setExtendedTest = plan + "setExtendedTest";
/* 11*/  public static final String setTestFiltration = plan + "setTestFiltration";
/* 12*/  public static final String openTestPlannerWindow = plan + "openTestPlannerWindow";



//-- ANALYSIS, EXTENDED ANALYSIS AND THRESHOLDS --//
/* 1 */ public static final String enterAnalysisModul = ana + "enterAnalysisModul";
/* 2 */ public static final String enterExtendedAnalysisModul = ana + "enterExtendedAnalysisModul";
/* 3 */ public static final String enterThresholdModul = ana + "enterThresholdModul";

/* 4 */  public static final String performDeterminedAnalysis  = ana + "performDeterminedAnalysis";
/* 5 */  public static final String performWaveletAnalysis  = ana + "performWaveletAnalysis";
/* 6 */  public static final String performMINUITanalysis = ana + "performMINUITanalysis";

/* 7 */  public static final String openReflectogrammFile  = ana + "openReflectogrammFile";
/* 8 */  public static final String saveReflectogrammFile = ana + "saveReflectogrammFile";
/* 9 */  public static final String loadReflectogrammFromDB = ana + "loadReflectogrammFromDB";
/* 10*/  public static final String loadReferenceTrace = ana + "loadReferenceTrace";



//--MODELING (CALCULATING OF THE REFLECTOGRAMM)--//
/* 1 */  public static final String enterReflectoModelingWindow = model + "enterReflectoModelingWindow";
/* 2 */  public static final String openSchemeForModeling = model + "openSchemeForModeling";
/* 3 */  public static final String performReflectoModeling = model + "performReflectoModeling";
/* 4 */  public static final String saveReflectoModeling = model + "saveReflectoModeling";
/* 5 */  public static final String setParamsReflectoModeling = model + "setParamsReflectoModeling";
/* 6 */  public static final String openMapForModeling = model + "openMapForModeling";


//------- OPTIMIZATOR OF THE OPTICAL NET --------//
/* 1 */  public static final String startOptimization = optim + "startOptimization";
/* 2 */  public static final String stopOptimization = optim + "stopOptimization";
/* 3 */  public static final String saveResultOfOptimization = optim + "saveResultOfOptimization";
/* 4 */  public static final String setParamsForOptimization = optim + "setParamsForOptimization";
/* 5 */  public static final String saveParamsForOptimization = optim + "saveParamsOfOptimization";
/* 6 */  public static final String enterOptimizationModul = optim + "enterOptimizationModul";
/* 7 */  public static final String openMapToBeOptimized = optim + "openMaptoBeOptimized";
/* 8 */  public static final String openSchemeToBeOptimized = optim + "openSchemeToBeOptimized";



//------- CONFIGURING OF THE SYSTEM ---------//
/* 0 */ public static final String enterConfiguringModul = config + "enterConfiguringModul";
/* 1 */ public static final String componentEditing = config + "componentEditing";
/* 2 */ public static final String schemeEditing = config + "schemeEditing";
/* 3 */ public static final String topologyEditing = config + "topologyEditing";
/* 4 */ public static final String schemeViewing = config + "schemeViewing";
/* 5 */ public static final String topologyViewing = config + "topologyViewing";
/* 6 */ public static final String catalogTCviewing = config + "catalogTCviewing";
/* 7 */ public static final String catalogTCediting = config + "catalogTCediting";
/* 8 */ public static final String catalogCMviewing = config + "catalogCMviewing";
/* 9 */ public static final String catalogCMediting = config + "catalogCMediting";

//------- OBSERVING MODUL ----------//
/* 0 */ public static final String enterObservingModul = observ + "enterObservingModul";
/* 1 */ public static final String viewingOperativeInformation = observ + "viewingOperativeInformation";
/* 2 */ public static final String viewingChangeArchive = observ + "viewingChangeArchive";
/* 3 */ public static final String fastTaskSetting = observ + "fastTaskSetting";
/* 4 */ public static final String reflectogrammObserving = observ + "reflectogrammObserving";
/* 5 */ public static final String alarmsObserving = observ + "alarmsObserving";
/* 6 */ public static final String alarmSignalAssurement = observ + "alarmSignalAssurement";
/* 7 */ public static final String alarmSignalDeleting = observ + "alarmSignalDeleting";
/* 8 */ public static final String alarmSignalStopping = observ + "alarmSignalStopping";


//------- PREDICTION MODUL ---------//
/* 0 */ public static final String enterPredictionModul = predict + "enterPredictionModul";
/* 1 */ public static final String calculatePredictedReflectogramm = predict + "calculatePredictedReflectogramm";
/* 2 */ public static final String loadDataForPrediction = predict + "loadDataForPrediction";
/* 3 */ public static final String savePredictedReflectogramm = predict + "savePredictedReflectogramm";

 User user;

 public Checker(DataSourceInterface dsi)
 {
   if(dsi != null)
     this.user = (User)Pool.get(User.typ, dsi.getSession().getUserId());
   else
   {
     this.user = null;
   }
 }

 public Checker(SessionInterface si)
 {
   if(si != null)
     this.user = (User)Pool.get(User.typ, si.getUserId());
   else
   {
     this.user = null;
   }
 }


 public Checker(User user)
 {
   this.user = user;
 }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//  Defenition of the object checkers.


/**/
	public boolean checkObject(
			String objectTyp, 
			String objectId, 
			String operationType)
	{
		return checkObject(
				objectTyp, 
				objectId, 
				operationType, 
				true);
	}
	
	public boolean checkObject(
			String objectTyp, 
			String objectId, 
			String operationType,
			boolean showMessage)
	{
		return checkObject(
				objectTyp, 
				objectId, 
				operationType, 
				this.user,
				showMessage);
	}

	public static boolean checkObject(
			String userId, 
			String objectTyp,
			String objectId, 
			String operationType,
			boolean showMessage)
	{
		User user = (User )Pool.get(User.typ, userId);
		return checkObject(
				objectTyp, 
				objectId, 
				operationType, 
				user, 
				showMessage);
	}

	/**/
	public static boolean checkObject(
			String userId, 
			String objectTyp,
			String objectId, 
			String operationType)
	{
		return checkObject(
				userId, 
				objectTyp,
				objectId, 
				operationType,
				true);
	}

 /**/

/*
 private static boolean checkObject(String typ, String id,
                                    String operationType, User user)
 {
   if(user == null)
   {
     String error = "Права пользователя не установлены.";
     JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
     return false;
   }
   if(user.login.equals("sys"))
     return true;

   ObjectPermissionAttributes opa = null;
   if(typ.equals(Domain.typ))
   {
     opa = ((Domain)Pool.get(typ, id)).opa;
   }
   if(opa == null)
   {
     String error = "Права доступа к обьекту не установлены.";
     JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
     return false;
   }


   if(opa.owner_id.equals(user.id))
   {
//     System.out.println("You are owner");
     if(hasEqualElements(opa.category_ids, user.category_ids))
     {
//       System.out.println("You are owner, and category is Ok");
       if(opa.userR && operationType.equals(read))
         return true;
       if(opa.userW && operationType.equals(write))
         return true;
       if(opa.userX && operationType.equals(execute))
         return true;
     }
   }
   else
     if(hasEqualElements(opa.group_ids, user.group_ids))
     {
//       System.out.println("You are from group");
       if(hasEqualElements(opa.category_ids, user.category_ids))
       {
//         System.out.println("You are from group, and category is Ok");
         if(opa.groupR && operationType.equals(read))
           return true;
         if(opa.groupW && operationType.equals(write))
           return true;
         if(opa.groupX && operationType.equals(execute))
           return true;
       }
     }
     else
     {
//       System.out.println("You are OTHER...");
       if(hasEqualElements(opa.category_ids, user.category_ids))
       {
//         System.out.println("You are OTHER..., But it's Ok, anyway.");
         if(opa.otherR && operationType.equals(read))
           return true;
         if(opa.otherW && operationType.equals(write))
           return true;
         if(opa.otherX && operationType.equals(execute))
           return true;
       }
     }

     String error = opa.whyRejected;
     JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);

     return false;
 }
*/

	private static boolean checkObject(
			String typ, 
			String id,
            String operationType, 
			User user)
	{
		return checkObject(
			typ, 
			id,
            operationType, 
			user,
			true);
	}


	private static boolean checkObject(
			String typ, 
			String id,
            String operationType, 
			User user,
			boolean showMessage)
	{
		if(user == null)
		{
			if(showMessage)
			{
				String error = "Права пользователя не установлены.";
				JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
			}
			return false;
		}
		if(user.login.equals("sys"))
			return true;

		ObjectPermissionAttributes opa = null;
		if(typ.equals(Domain.typ))
		{
			Domain dom = (Domain)Pool.get(typ, id);
			if(dom == null)
			{
				return true;
			}
			opa = dom.opa;
		}
		if(opa == null)
		{
			return true;
		}

		if(opa.owner_id.equals(user.id))
		{
			if(opa.userR && operationType.equals(read))
				return true;
			if(opa.userW && operationType.equals(write))
				return true;
			if(opa.userX && operationType.equals(execute))
				return true;
		}
		else
		if(hasEqualElements(opa.group_ids, user.group_ids))
		{
			if(opa.groupR && operationType.equals(read))
				return true;
			if(opa.groupW && operationType.equals(write))
				return true;
			if(opa.groupX && operationType.equals(execute))
				return true;
		}
		else
		{
			if(opa.otherR && operationType.equals(read))
				return true;
			if(opa.otherW && operationType.equals(write))
				return true;
			if(opa.otherX && operationType.equals(execute))
				return true;
		}

		if(showMessage)
		{
			String error = opa.whyRejected;
			JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
		}

		return false;
	}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//  Defenition of the command checkers.
/*
 public boolean checkCommand(String commandName, String operationType)//depricated
 {
   return checkCommand(commandName, operationType, this.user);
 }
*/
 public boolean checkCommand(String commandName)
 {
   return checkCommand(commandName, this.user);
 }

 /*
 public static boolean checkCommand(User user, String commandName, String operationType)//depricated
 {
   return checkCommand(commandName, operationType, user);
 }
*/
 public static boolean checkCommand(User user, String commandName)
 {
   return checkCommand(commandName, user);
 }

 /*
 public static boolean checkCommand(String userID, String commandName, String operationType)//depricated
 {
   User user = (User)Pool.get(User.typ, userID);
   return checkCommand(commandName, operationType, user);
 }
*/
 public static boolean checkCommandByUserId(String userID, String commandName)
 {
   User user = (User)Pool.get(User.typ, userID);
   return checkCommand(commandName, user);
 }


 private static boolean checkCommand(String commandName, String operationType,
                                     User user)
 {
   if(user == null)
   {
     String error = "Права пользователя не установлены";
     JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
     return false;
   }

   if(user.login.equals("sys"))
     return true;

   CommandPermissionAttributes cpa = getCommand(commandName);
   if(cpa == null)
   {
     return true;
   }

   if(cpa.owner_id.equals(user.id))
     return true;

   if(hasEqualElements(cpa.category_ids, user.category_ids))
     return true;

   String error = cpa.whyRejected;
   JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
   return false;
 }

 private static boolean checkCommand(String commandName, User user)
 {
   if(user == null)
   {
     String error = "Права пользователя не установлены";
     JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
     return false;
   }

   if(user.login.equals("sys"))
     return true;

   CommandPermissionAttributes cpa = getCommand(commandName);
   if(cpa == null)
   {
     return true;
   }

   if(cpa.owner_id.equals(user.id))
     return true;

   if(hasEqualElements(cpa.category_ids, user.category_ids))
     return true;

   String error = cpa.whyRejected;
   JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
   return false;
 }


/*
 private static boolean checkCommand(String commandName, String operationType,
                                     User user)
 {
   if(user == null)
   {
     String error = "Права пользователя не установлены";
     JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
     return false;
   }

   if(user.login.equals("sys"))
     return true;

   CommandPermissionAttributes cpa = getCommand(commandName);
   if(cpa == null)
   {
//     System.out.println("The command has no attributes or does not exist"+
//                        "Anybody can do it");
     return true;
   }

   if(cpa.owner_id.equals(user.id))
   {
//     System.out.println("You are owner");
     if(hasEqualElements(cpa.category_ids, user.category_ids))
     {
//       System.out.println("You are owner, and category is Ok");
       if(cpa.userR && operationType.equals(read))
         return true;
       if(cpa.userW && operationType.equals(write))
         return true;
       if(cpa.userX && operationType.equals(execute))
         return true;
     }
   }
   else
     if(hasEqualElements(cpa.group_ids, user.group_ids))
     {
//       System.out.println("You are from group");
       if(hasEqualElements(cpa.category_ids, user.category_ids))
       {
//         System.out.println("You are from group, and category is Ok");
         if(cpa.groupR && operationType.equals(read))
           return true;
         if(cpa.groupW && operationType.equals(write))
           return true;
         if(cpa.groupX && operationType.equals(execute))
           return true;
       }
     }
     else
     {
//       System.out.println("You are OTHER...");
       if(hasEqualElements(cpa.category_ids, user.category_ids))
       {
//         System.out.println("You are OTHER..., But it's Ok, anyway.");
         if(cpa.otherR && operationType.equals(read))
           return true;
         if(cpa.otherW && operationType.equals(write))
           return true;
         if(cpa.otherX && operationType.equals(execute))
           return true;
       }
     }

     String error = cpa.whyRejected;
     JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
     return false;
 }

*/



// Definition of the helping functions.
/**/
 private static CommandPermissionAttributes getCommand(String commandName)
 {
   Hashtable h = Pool.getHash(CommandPermissionAttributes.typ);
   if(h == null)
     return null;
   for(Enumeration e = h.elements(); e.hasMoreElements();)
   {
     CommandPermissionAttributes tmp =
                       (CommandPermissionAttributes)e.nextElement();
     if(tmp.codename.equals(commandName))
     {
       return tmp;
     }
   }
   return null;
 }


 /**/
 private static boolean hasEqualElements(Vector a, Vector b)
 {
   for(int i=0; i<a.size(); i++)
   {
     for(int j=0; j<b.size(); j++)
     {
       String as = (String)a.get(i);
       String bs = (String)b.get(j);
       if(as.equals(bs))
         return true;
     }
   }
   return false;
 }

 /**/
 private static boolean hasEqualElements(Vector a, Vector b, String s)
 {
   for(int i=0; i<a.size(); i++)
   {
     for(int j=0; j<b.size(); j++)
     {
       String as = (String)a.get(i);
       String bs = (String)b.get(j);
       if(as.equals(s) && bs.equals(s))
         return true;
     }
   }
//     if(a.size() == 0 && b.size() ==0 )
//   return true;
   return false;
 }


}
