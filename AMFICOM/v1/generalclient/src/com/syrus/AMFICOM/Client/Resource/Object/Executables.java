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
/* 1 */     setDefaultCommand(commandNames,Checker.addDomain, "Добавить домен", categ);
/* 2 */     setDefaultCommand(commandNames,Checker.addGroup, "Добавить группу", categ);
/* 3 */     setDefaultCommand(commandNames,Checker.addProfile, "Добавить профиль", categ);
/* 4 */     setDefaultCommand(commandNames,Checker.modifyDomain, "Изменить домен", categ);
/* 5 */     setDefaultCommand(commandNames,Checker.modifyExec, "Изменить команду", categ);
/* 6 */     setDefaultCommand(commandNames,Checker.modifyGroup, "Изменить группу", categ);
/* 7 */     setDefaultCommand(commandNames,Checker.modifyProfile, "Изменить профиль", categ);
/* 8 */     setDefaultCommand(commandNames,Checker.readDomainInfo, "Прочитать инф. о доменах", categ);
/* 9 */     setDefaultCommand(commandNames,Checker.readExecInfo, "Прочитать инф. о командах", categ);
/* 10*/     setDefaultCommand(commandNames,Checker.readGroupInfo, "Прочитать инф. о группах", categ);
/* 11*/     setDefaultCommand(commandNames,Checker.readProfileInfo, "Прочитать инф. о профилях", categ);
/* 12*/     setDefaultCommand(commandNames,Checker.removeDomain, "Удалить домен", categ);
/* 13*/     setDefaultCommand(commandNames,Checker.removeGroup, "Удалить группу", categ);
/* 14*/     setDefaultCommand(commandNames,Checker.removeProfile, "Удалить профиль", categ);
/* 15*/     setDefaultCommand(commandNames,Checker.modifyCategory, "Изменить категорию", categ);
/* 16*/     setDefaultCommand(commandNames,Checker.readCategoryInfo, "Прочитать инф. о категориях", categ);
/* 17*/     setDefaultCommand(commandNames,Checker.openAdminWindow, "Открыть окно администрирования", categ);
/* 18*/     setDefaultCommand(commandNames,Checker.readUserInfo, "Прочитать инф. о пользователях", categ);

/* 19*/     setDefaultCommand(commandNames,Checker.readAgentInfo, "Просмотреть информацию об агентах", categ);
/* 20*/     setDefaultCommand(commandNames,Checker.modifyAgentInfo, "Изменить информацию об агентах", categ);
/* 21*/     setDefaultCommand(commandNames,Checker.createAgent, "Создать агента", categ);
/* 22*/     setDefaultCommand(commandNames,Checker.deleteAgent, "Удалить агента", categ);
/* 23*/     setDefaultCommand(commandNames,Checker.readServerInfo, "Просмотреть информацию о серверах", categ);
/* 24*/     setDefaultCommand(commandNames,Checker.modifyServerInfo, "Изменить информацию о серверах", categ);
/* 25*/     setDefaultCommand(commandNames,Checker.createServer, "Создать сервер", categ);
/* 26*/     setDefaultCommand(commandNames,Checker.deleteServer, "Удалить сервер", categ);
/* 27*/     setDefaultCommand(commandNames,Checker.readClientInfo, "Просмотреть информацию о клиентах", categ);
/* 28*/     setDefaultCommand(commandNames,Checker.modifyClientInfo, "Изменить информацию о клиентах", categ);
/* 29*/     setDefaultCommand(commandNames,Checker.createClient, "Создать клиента", categ);
/* 30*/     setDefaultCommand(commandNames,Checker.deleteClient, "Удалить клиента", categ);


// SETTING OF THE TEST PLANNER COMMANDS (EXECUTABLES) //
 categ = new Vector();
 tmp = new String []{OperatorCategory.analyst, OperatorCategory.operator, OperatorCategory.spec, OperatorCategory.subscriber};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */  setDefaultCommand(commandNames,Checker.setOneTimeTest, "Установить однократное тестирование", categ);
/* 2 */  setDefaultCommand(commandNames,Checker.setTestTimeTable, "Установить расписание тестов", categ);
/* 3 */  setDefaultCommand(commandNames,Checker.setPeriodicTest, "Установить периодическое тестирование", categ);
/* 4 */  setDefaultCommand(commandNames,Checker.refreshTest, "Обновить тесты", categ);
/* 5 */  setDefaultCommand(commandNames,Checker.addTests, "Добавить тест", categ);
/* 6 */  setDefaultCommand(commandNames,Checker.saveTests, "Сохранить тест", categ);
/* 7 */  setDefaultCommand(commandNames,Checker.setOneKIStest, "Установить тестирование одного КИСа", categ);
/* 8 */  setDefaultCommand(commandNames,Checker.setAllKIStest, "Установить тестирование всех КИСов", categ);
/* 9 */  setDefaultCommand(commandNames,Checker.setUsualTest, "Установить простой тест", categ);
/* 10*/  setDefaultCommand(commandNames,Checker.setExtendedTest, "Установить расширенный тест", categ);
/* 11*/  setDefaultCommand(commandNames,Checker.setTestFiltration, "Установить фильтр тестов", categ);
/* 12*/  setDefaultCommand(commandNames,Checker.openTestPlannerWindow, "Открыть окно тестирования", categ);

// SETTING OF THE MODELING COMMANDS (EXECUTABLES) //
 categ = new Vector();
 tmp = new String[]{OperatorCategory.spec, OperatorCategory.analyst};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */  setDefaultCommand(commandNames,Checker.enterReflectoModelingWindow, "Войти в модуль моделирования", categ);
/* 2 */  setDefaultCommand(commandNames,Checker.performReflectoModeling, "Выполнить моделирование", categ);
/* 3 */  setDefaultCommand(commandNames,Checker.saveReflectoModeling, "Сохранить модель рефлектограммы", categ);
/* 4 */  setDefaultCommand(commandNames,Checker.setParamsReflectoModeling, "Установить параметры моделирования", categ);
/* 5 */  setDefaultCommand(commandNames,Checker.openSchemeForModeling, "Открыть схему модели", categ);
/* 6 */  setDefaultCommand(commandNames,Checker.openMapForModeling, "Открыть карту модели", categ);

// SETTING OF THE COMMANDS FOR ANALYSIS, EXTENDED ANALYSIS and THRESHOLD SETTING //
 categ = new Vector();
 tmp = new String []{OperatorCategory.analyst, OperatorCategory.subscriber, OperatorCategory.spec};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */  setDefaultCommand(commandNames,Checker.enterAnalysisModul, "Войти в модуль анализатора", categ);
/* 2 */  setDefaultCommand(commandNames,Checker.enterExtendedAnalysisModul, "Войти в модуль расширенного анализатора", categ);
/* 3 */  setDefaultCommand(commandNames,Checker.enterThresholdModul, "Войти в модуль работы с порогами", categ);

/* 4 */  setDefaultCommand(commandNames,Checker.performDeterminedAnalysis, "Выполнить детерменированный анализ", categ);
/* 5 */  setDefaultCommand(commandNames,Checker.performWaveletAnalysis, "Выполнить статистический анализ", categ);
/* 6 */  setDefaultCommand(commandNames,Checker.performMINUITanalysis, "Выполнить корреляционный анализ", categ);

/* 7 */  setDefaultCommand(commandNames,Checker.openReflectogrammFile, "Открыть файл с рефлектограммой", categ);
/* 8 */  setDefaultCommand(commandNames,Checker.saveReflectogrammFile, "Сохранить рефлектограмму в файл", categ);
/* 9 */  setDefaultCommand(commandNames,Checker.loadReflectogrammFromDB, "Загрузить рефлектограмму из БД", categ);
/* 10*/  setDefaultCommand(commandNames,Checker.loadReferenceTrace , "Загрузить опорную рефлектограмму", categ);


//  --------- OPTIMIZATOR OF THE OPTICAL NET ----------  //
 categ = new Vector();
 tmp = new String []{OperatorCategory.spec, OperatorCategory.analyst};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */   setDefaultCommand(commandNames, Checker.startOptimization, "Начать оптимизацию", categ);
/* 2 */   setDefaultCommand(commandNames, Checker.stopOptimization, "Прервать оптимизацию", categ);
/* 3 */   setDefaultCommand(commandNames, Checker.saveResultOfOptimization, "Сохранить результат оптимизации", categ);
/* 4 */   setDefaultCommand(commandNames, Checker.setParamsForOptimization, "Установить параметры оптимизации", categ);
/* 5 */   setDefaultCommand(commandNames, Checker.saveParamsForOptimization, "Сохранить параметры оптимизации", categ);
/* 6 */   setDefaultCommand(commandNames, Checker.enterOptimizationModul, "Войти в модуль оптимизации", categ);
/* 7 */   setDefaultCommand(commandNames, Checker.openMapToBeOptimized, "Открыть карту для оптимизации", categ);
/* 8 */   setDefaultCommand(commandNames, Checker.openSchemeToBeOptimized, "Открыть схему для оптимизации", categ);

//---------- CONFIGURING OF THE SYSTEM ----------//
 categ = new Vector();
 tmp = new String []{OperatorCategory.designer, OperatorCategory.admin};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }

/* 1 */ setDefaultCommand(commandNames, Checker.enterConfiguringModul, "Войти в модуль конфигурирования", categ);
/* 2 */ setDefaultCommand(commandNames, Checker.componentEditing, "Редактирование компонентов", categ);
/* 3 */ setDefaultCommand(commandNames, Checker.schemeEditing, "Редактирование схем", categ);
/* 4 */ setDefaultCommand(commandNames, Checker.topologyEditing, "Редактирование топологии", categ);
/* 5 */ setDefaultCommand(commandNames, Checker.schemeViewing, "Просмотр схем", categ);
/* 6 */ setDefaultCommand(commandNames, Checker.topologyViewing, "Просмотр топологии", categ);
/* 7 */ setDefaultCommand(commandNames, Checker.catalogTCviewing, "Просмотр каталога ТС", categ);
/* 8 */ setDefaultCommand(commandNames, Checker.catalogTCediting, "Редактирование каталога ТС", categ);
/* 9 */ setDefaultCommand(commandNames, Checker.catalogCMviewing, "Просмотр каталога СМ", categ);
/* 10*/ setDefaultCommand(commandNames, Checker.catalogCMediting, "Редактирование каталога СМ", categ);

//------- OBSERVING MODUL ----------//
 categ = new Vector();
 tmp = new String []{OperatorCategory.subscriber, OperatorCategory.spec};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }


/* 1 */ setDefaultCommand(commandNames, Checker.enterObservingModul, "Войти в модуль наблюдения", categ);
/* 2 */ setDefaultCommand(commandNames, Checker.viewingOperativeInformation, "Просмотр оперативной информации", categ);
/* 3 */ setDefaultCommand(commandNames, Checker.viewingChangeArchive, "Просмотр архивов изменений", categ);
/* 4 */ setDefaultCommand(commandNames, Checker.fastTaskSetting, "Быстрая постановка задачи", categ);
/* 5 */ setDefaultCommand(commandNames, Checker.reflectogrammObserving, "Просмотр рефлектограмм", categ);
/* 6 */ setDefaultCommand(commandNames, Checker.alarmsObserving, "Просмотр сигналов тревоги", categ);
/* 7 */ setDefaultCommand(commandNames, Checker.alarmSignalAssurement, "Подтверждение сигналов тревоги", categ);
/* 8 */ setDefaultCommand(commandNames, Checker.alarmSignalDeleting, "Удаление сигналов тревоги", categ);
/* 9 */ setDefaultCommand(commandNames, Checker.alarmSignalStopping, "Снятие сигналов тревоги", categ);



//------- PREDICTION MODUL ---------//
 categ = new Vector();
 tmp = new String []{OperatorCategory.analyst, OperatorCategory.spec};
 for(i=0; i<tmp.length; i++)
 {
   categ.add(tmp[i]);
 }


/* 1 */ setDefaultCommand(commandNames, Checker.enterPredictionModul, "Войти в модуль прогнозирования", categ);
/* 2 */ setDefaultCommand(commandNames, Checker.calculatePredictedReflectogramm, "Рассчитать прогнозируемую рефлектограмму", categ);
/* 3 */ setDefaultCommand(commandNames, Checker.loadDataForPrediction, "Загрузить данные для прогнозирования", categ);
/* 4 */ setDefaultCommand(commandNames, Checker.savePredictedReflectogramm, "Сохранить прогнозируемую рефлектограмму", categ);
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
   cpa.whyRejected = "Операция  " + rusCommandName + "\n  запрещена.";

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
   cpa.whyRejected = "Операция  " + rusCommandName + "\n  запрещена.";

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
   cpa.whyRejected = "Операция  " + rusCommandName + "\n  запрещена.";

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
