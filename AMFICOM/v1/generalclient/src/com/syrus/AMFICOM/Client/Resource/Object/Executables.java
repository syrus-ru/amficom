package com.syrus.AMFICOM.Client.Resource.Object;

import java.util.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class Executables
{
	DataSourceInterface dsi;
	User user;

  public Executables(DataSourceInterface dsi)
  {
    this.dsi = dsi;
    user = (User)Pool.get(User.typ, dsi.getSession().getUserId());
    setCommands();
  }

public void setCommands()
{
  Hashtable h = Pool.getHash(CommandPermissionAttributes.typ);
  if(h == null)
    h = new Hashtable();

  CommandPermissionAttributes cpa;
  String[] commandNames = new String[h.size()];

  int i=0;
  for(Enumeration e = h.elements(); e.hasMoreElements();)
  {
    cpa = (CommandPermissionAttributes)(e.nextElement());
    commandNames[i] = cpa.codename;
    if(commandNames[i] == null)
      commandNames[i] = "";
    i++;
  }
// commandNames contains the names of the commands,
// which have already been defined.

//     System.out.println("TRY TO SET THE EXECS");


// SETTING OF THE ADMINISTRATING COMMANDS //
  Vector categ = new Vector();
  String []tmp = new String []{OperatorCategory.sysadmin, OperatorCategory.admin};
  for(i=0; i<tmp.length; i++)
  {
    categ.add(tmp[i]);
  }
/* 1 */     setDefaultCommand(commandNames,Checker.addDomain, "�������� �����", categ);
/* 2 */     setDefaultCommand(commandNames,Checker.addGroup, "�������� ������", categ);
/* 3 */     setDefaultCommand(commandNames,Checker.addProfile, "�������� �������", categ);
/* 4 */     setDefaultCommand(commandNames,Checker.modifyDomain, "�������� �����", categ);
/* 5 */     setDefaultCommand(commandNames,Checker.modifyExec, "�������� �������", categ);
/* 6 */     setDefaultCommand(commandNames,Checker.modifyGroup, "�������� ������", categ);
/* 7 */     setDefaultCommand(commandNames,Checker.modifyProfile, "�������� �������", categ);
/* 8 */     setDefaultCommand(commandNames,Checker.readDomainInfo, "��������� ���. � �������", categ);
/* 9 */     setDefaultCommand(commandNames,Checker.readExecInfo, "��������� ���. � ��������", categ);
/* 10*/     setDefaultCommand(commandNames,Checker.readGroupInfo, "��������� ���. � �������", categ);
/* 11*/     setDefaultCommand(commandNames,Checker.readProfileInfo, "��������� ���. � ��������", categ);
/* 12*/     setDefaultCommand(commandNames,Checker.removeDomain, "������� �����", categ);
/* 13*/     setDefaultCommand(commandNames,Checker.removeGroup, "������� ������", categ);
/* 14*/     setDefaultCommand(commandNames,Checker.removeProfile, "������� �������", categ);
/* 15*/     setDefaultCommand(commandNames,Checker.modifyCategory, "�������� ���������", categ);
/* 16*/     setDefaultCommand(commandNames,Checker.readCategoryInfo, "��������� ���. � ����������", categ);
/* 17*/     setDefaultCommand(commandNames,Checker.openAdminWindow, "������� ���� �����������������", categ);
/* 18*/     setDefaultCommand(commandNames,Checker.readUserInfo, "��������� ���. � �������������", categ);

/* 19*/     setDefaultCommand(commandNames,Checker.readAgentInfo, "����������� ���������� �� �������", categ);
/* 20*/     setDefaultCommand(commandNames,Checker.modifyAgentInfo, "�������� ���������� �� �������", categ);
/* 21*/     setDefaultCommand(commandNames,Checker.createAgent, "������� ������", categ);
/* 22*/     setDefaultCommand(commandNames,Checker.deleteAgent, "������� ������", categ);
/* 23*/     setDefaultCommand(commandNames,Checker.readServerInfo, "����������� ���������� � ��������", categ);
/* 24*/     setDefaultCommand(commandNames,Checker.modifyServerInfo, "�������� ���������� � ��������", categ);
/* 25*/     setDefaultCommand(commandNames,Checker.createServer, "������� ������", categ);
/* 26*/     setDefaultCommand(commandNames,Checker.deleteServer, "������� ������", categ);
/* 27*/     setDefaultCommand(commandNames,Checker.readClientInfo, "����������� ���������� � ��������", categ);
/* 28*/     setDefaultCommand(commandNames,Checker.modifyClientInfo, "�������� ���������� � ��������", categ);
/* 29*/     setDefaultCommand(commandNames,Checker.createClient, "������� �������", categ);
/* 30*/     setDefaultCommand(commandNames,Checker.deleteClient, "������� �������", categ);


// SETTING OF THE TEST PLANNER COMMANDS (EXECUTABLES) //
 categ = new Vector();
 tmp = new String []{OperatorCategory.analyst, OperatorCategory.operator, OperatorCategory.spec, OperatorCategory.subscriber};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */  setDefaultCommand(commandNames,Checker.setOneTimeTest, "���������� ����������� ������������", categ);
/* 2 */  setDefaultCommand(commandNames,Checker.setTestTimeTable, "���������� ���������� ������", categ);
/* 3 */  setDefaultCommand(commandNames,Checker.setPeriodicTest, "���������� ������������� ������������", categ);
/* 4 */  setDefaultCommand(commandNames,Checker.refreshTest, "�������� �����", categ);
/* 5 */  setDefaultCommand(commandNames,Checker.addTests, "�������� ����", categ);
/* 6 */  setDefaultCommand(commandNames,Checker.saveTests, "��������� ����", categ);
/* 7 */  setDefaultCommand(commandNames,Checker.setOneKIStest, "���������� ������������ ������ ����", categ);
/* 8 */  setDefaultCommand(commandNames,Checker.setAllKIStest, "���������� ������������ ���� �����", categ);
/* 9 */  setDefaultCommand(commandNames,Checker.setUsualTest, "���������� ������� ����", categ);
/* 10*/  setDefaultCommand(commandNames,Checker.setExtendedTest, "���������� ����������� ����", categ);
/* 11*/  setDefaultCommand(commandNames,Checker.setTestFiltration, "���������� ������ ������", categ);
/* 12*/  setDefaultCommand(commandNames,Checker.openTestPlannerWindow, "������� ���� ������������", categ);

// SETTING OF THE MODELING COMMANDS (EXECUTABLES) //
 categ = new Vector();
 tmp = new String[]{OperatorCategory.spec, OperatorCategory.analyst};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */  setDefaultCommand(commandNames,Checker.enterReflectoModelingWindow, "����� � ������ �������������", categ);
/* 2 */  setDefaultCommand(commandNames,Checker.performReflectoModeling, "��������� �������������", categ);
/* 3 */  setDefaultCommand(commandNames,Checker.saveReflectoModeling, "��������� ������ ��������������", categ);
/* 4 */  setDefaultCommand(commandNames,Checker.setParamsReflectoModeling, "���������� ��������� �������������", categ);
/* 5 */  setDefaultCommand(commandNames,Checker.openSchemeForModeling, "������� ����� ������", categ);
/* 6 */  setDefaultCommand(commandNames,Checker.openMapForModeling, "������� ����� ������", categ);

// SETTING OF THE COMMANDS FOR ANALYSIS, EXTENDED ANALYSIS and THRESHOLD SETTING //
 categ = new Vector();
 tmp = new String []{OperatorCategory.analyst, OperatorCategory.subscriber, OperatorCategory.spec};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */  setDefaultCommand(commandNames,Checker.enterAnalysisModul, "����� � ������ �����������", categ);
/* 2 */  setDefaultCommand(commandNames,Checker.enterExtendedAnalysisModul, "����� � ������ ������������ �����������", categ);
/* 3 */  setDefaultCommand(commandNames,Checker.enterThresholdModul, "����� � ������ ������ � ��������", categ);

/* 4 */  setDefaultCommand(commandNames,Checker.performDeterminedAnalysis, "��������� ����������������� ������", categ);
/* 5 */  setDefaultCommand(commandNames,Checker.performWaveletAnalysis, "��������� �������������� ������", categ);
/* 6 */  setDefaultCommand(commandNames,Checker.performMINUITanalysis, "��������� �������������� ������", categ);

/* 7 */  setDefaultCommand(commandNames,Checker.openReflectogrammFile, "������� ���� � ���������������", categ);
/* 8 */  setDefaultCommand(commandNames,Checker.saveReflectogrammFile, "��������� �������������� � ����", categ);
/* 9 */  setDefaultCommand(commandNames,Checker.loadReflectogrammFromDB, "��������� �������������� �� ��", categ);
/* 10*/  setDefaultCommand(commandNames,Checker.loadReferenceTrace , "��������� ������� ��������������", categ);


//  --------- OPTIMIZATOR OF THE OPTICAL NET ----------  //
 categ = new Vector();
 tmp = new String []{OperatorCategory.spec, OperatorCategory.analyst};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */   setDefaultCommand(commandNames, Checker.startOptimization, "������ �����������", categ);
/* 2 */   setDefaultCommand(commandNames, Checker.stopOptimization, "�������� �����������", categ);
/* 3 */   setDefaultCommand(commandNames, Checker.saveResultOfOptimization, "��������� ��������� �����������", categ);
/* 4 */   setDefaultCommand(commandNames, Checker.setParamsForOptimization, "���������� ��������� �����������", categ);
/* 5 */   setDefaultCommand(commandNames, Checker.saveParamsForOptimization, "��������� ��������� �����������", categ);
/* 6 */   setDefaultCommand(commandNames, Checker.enterOptimizationModul, "����� � ������ �����������", categ);
/* 7 */   setDefaultCommand(commandNames, Checker.openMapToBeOptimized, "������� ����� ��� �����������", categ);
/* 8 */   setDefaultCommand(commandNames, Checker.openSchemeToBeOptimized, "������� ����� ��� �����������", categ);

//---------- CONFIGURING OF THE SYSTEM ----------//
 categ = new Vector();
 tmp = new String []{OperatorCategory.designer, OperatorCategory.admin};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */ setDefaultCommand(commandNames, Checker.enterConfiguringModul, "����� � ������ ����������������", categ);
/* 2 */ setDefaultCommand(commandNames, Checker.componentEditing, "�������������� �����������", categ);
/* 3 */ setDefaultCommand(commandNames, Checker.schemeEditing, "�������������� ����", categ);
/* 4 */ setDefaultCommand(commandNames, Checker.topologyEditing, "�������������� ���������", categ);
/* 5 */ setDefaultCommand(commandNames, Checker.schemeViewing, "�������� ����", categ);
/* 6 */ setDefaultCommand(commandNames, Checker.topologyViewing, "�������� ���������", categ);
/* 7 */ setDefaultCommand(commandNames, Checker.catalogTCviewing, "�������� �������� ��", categ);
/* 8 */ setDefaultCommand(commandNames, Checker.catalogTCediting, "�������������� �������� ��", categ);
/* 9 */ setDefaultCommand(commandNames, Checker.catalogCMviewing, "�������� �������� ��", categ);
/* 10*/ setDefaultCommand(commandNames, Checker.catalogCMediting, "�������������� �������� ��", categ);

//------- OBSERVING MODUL ----------//
 categ = new Vector();
 tmp = new String []{OperatorCategory.subscriber, OperatorCategory.spec};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }


/* 1 */ setDefaultCommand(commandNames, Checker.enterObservingModul, "����� � ������ ����������", categ);
/* 2 */ setDefaultCommand(commandNames, Checker.viewingOperativeInformation, "�������� ����������� ����������", categ);
/* 3 */ setDefaultCommand(commandNames, Checker.viewingChangeArchive, "�������� ������� ���������", categ);
/* 4 */ setDefaultCommand(commandNames, Checker.fastTaskSetting, "������� ���������� ������", categ);
/* 5 */ setDefaultCommand(commandNames, Checker.reflectogrammObserving, "�������� �������������", categ);
/* 6 */ setDefaultCommand(commandNames, Checker.alarmsObserving, "�������� �������� �������", categ);
/* 7 */ setDefaultCommand(commandNames, Checker.alarmSignalAssurement, "������������� �������� �������", categ);
/* 8 */ setDefaultCommand(commandNames, Checker.alarmSignalDeleting, "�������� �������� �������", categ);
/* 9 */ setDefaultCommand(commandNames, Checker.alarmSignalStopping, "������ �������� �������", categ);



//------- PREDICTION MODUL ---------//
 categ = new Vector();
 tmp = new String []{OperatorCategory.analyst, OperatorCategory.spec};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }


/* 1 */ setDefaultCommand(commandNames, Checker.enterPredictionModul, "����� � ������ ���������������", categ);
/* 2 */ setDefaultCommand(commandNames, Checker.calculatePredictedReflectogramm, "���������� �������������� ��������������", categ);
/* 3 */ setDefaultCommand(commandNames, Checker.loadDataForPrediction, "��������� ������ ��� ���������������", categ);
/* 4 */ setDefaultCommand(commandNames, Checker.savePredictedReflectogramm, "��������� �������������� ��������������", categ);
  return;
}

  private void setDefaultCommand(String []commandNames, String commandName, String rusCommandName)
 {
//    System.out.println("setDefaulCommand routine");
   for(int i=0; i<commandNames.length; i++)
   {
     if(commandNames[i].equals(commandName))
     {
//       System.out.println("Exec exist");
       return;
     }
   }
   System.out.println("Setting of new exec...");
   if(user == null)
	    {
//               System.out.println("User = null");
               return;
	    }
   String ID = this.dsi.
	       GetUId(CommandPermissionAttributes.typ);
   if(ID == null || ID.equals(""))
    {
//     System.out.println("Exec ID = null");
     return;
    }

// Construction of the new command, and setting it to the pool.
   Date d = new Date();
   CommandPermissionAttributes cpa = new CommandPermissionAttributes();
   cpa.id = ID;
   cpa.codename = commandName;
   cpa.name     = rusCommandName;

   cpa.category_ids = new Vector();

   cpa.group_ids = new Vector();

   cpa.created_by = user.id;
   cpa.modified_by = user.id;
   cpa.owner_id = user.id;
   cpa.modified = d.getTime();
   cpa.created = d.getTime();
   cpa.whyRejected = "��������  " + rusCommandName + "\n  ���������.";

   cpa.SetUserR(false);
   cpa.SetUserW(false);
   cpa.SetUserX(true);

   cpa.SetGroupR(false);
   cpa.SetGroupW(false);
   cpa.SetGroupX(true);

   cpa.SetOtherR(false);
   cpa.SetOtherW(false);
   cpa.SetOtherX(false);

//   System.out.println("New exec is set");
   Pool.put(CommandPermissionAttributes.typ, cpa.id, cpa);
   this.dsi.SaveExec(cpa.id);
 }


 private void setDefaultCommand(String []commandNames, String commandName, String rusCommandName, String []categories)
{
  for(int i=0; i<commandNames.length; i++)
  {
    if(commandNames[i].equals(commandName))
    {
      return;
    }
  }
   System.out.println("Setting of new exec...");

   if(user == null)
   {
     return;
   }
   String ID = this.dsi.
               GetUId(CommandPermissionAttributes.typ);
   if(ID == null || ID.equals(""))
   {
     return;
   }

// Construction of the new command, and setting it to the pool.
   Date d = new Date();
   CommandPermissionAttributes cpa = new CommandPermissionAttributes();
   cpa.id = ID;
   cpa.codename = commandName;
   cpa.name     = rusCommandName;

   Vector tmp = new Vector();

   if(categories != null)
   {
     for(int i=0; i<categories.length; i++)
     {
       tmp.add(categories[i]);
     }
   }

   cpa.category_ids = tmp;

   cpa.group_ids = new Vector();

   cpa.created_by = user.id;
   cpa.modified_by = user.id;
   cpa.owner_id = user.id;
   cpa.modified = d.getTime();
   cpa.created = d.getTime();
   cpa.whyRejected = "��������  " + rusCommandName + "\n  ���������.";

   cpa.SetUserR(false);
   cpa.SetUserW(false);
   cpa.SetUserX(true);

   cpa.SetGroupR(false);
   cpa.SetGroupW(false);
   cpa.SetGroupX(true);

   cpa.SetOtherR(false);
   cpa.SetOtherW(false);
   cpa.SetOtherX(false);

// System.out.println("New exec is set");
   Pool.put(CommandPermissionAttributes.typ, cpa.id, cpa);
   this.dsi.SaveExec(cpa.id);
 }



 private void setDefaultCommand(String []commandNames, String commandName, String rusCommandName, Vector categories)
{
  for(int i=0; i<commandNames.length; i++)
  {
    if(commandNames[i].equals(commandName))
    {
      return;
    }
  }
   System.out.println("Setting of new exec...");

   if(user == null)
   {
     return;
   }
   String ID = this.dsi.
               GetUId(CommandPermissionAttributes.typ);
   if(ID == null || ID.equals(""))
   {
     return;
   }

// Construction of the new command, and setting it to the pool.
   Date d = new Date();
   CommandPermissionAttributes cpa = new CommandPermissionAttributes();
   cpa.id = ID;
   cpa.codename = commandName;
   cpa.name     = rusCommandName;


   if(categories != null)
     cpa.category_ids = categories;
   else
     cpa.category_ids = new Vector();

   cpa.group_ids = new Vector();

   cpa.created_by = user.id;
   cpa.modified_by = user.id;
   cpa.owner_id = user.id;
   cpa.modified = d.getTime();
   cpa.created = d.getTime();
   cpa.whyRejected = "��������  " + rusCommandName + "\n  ���������.";

   cpa.SetUserR(false);
   cpa.SetUserW(false);
   cpa.SetUserX(true);

   cpa.SetGroupR(false);
   cpa.SetGroupW(false);
   cpa.SetGroupX(true);

   cpa.SetOtherR(false);
   cpa.SetOtherW(false);
   cpa.SetOtherX(false);

// System.out.println("New exec is set");
   Pool.put(CommandPermissionAttributes.typ, cpa.id, cpa);
   this.dsi.SaveExec(cpa.id);
 }

}
