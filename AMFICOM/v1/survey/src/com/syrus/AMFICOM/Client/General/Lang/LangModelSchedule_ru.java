//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ������ ��������� �������� �� ������� ����� ��� ���������   * //
// *         ���� ���������� ����� �� �������                             * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 1.0                                                          * //
// * ��: 1 jul 2002                                                       * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelMain_ru.java                     * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Lang;

import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.text.DateFormatSymbols;

public class LangModelSchedule_ru extends LangModelSchedule
{
	static final Object[][] contents = {

		{ "language", "�������" },
		{ "country", "������" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

// Menu text
		{ "menuSessionText", "������" },
		{ "menuSessionNewText", "����� ������" },
		{ "menuSessionCloseText", "������� ������" },
		{ "menuSessionOptionsText", "��������� ������" },
		{ "menuSessionConnectionText", "��������� ����������" },
		{ "menuSessionChangePasswordText", "�������� ������" },
		{ "menuSessionSaveText", "��������� ���������" },
		{ "menuSessionUndoText", "�������� ���������" },
		{ "menuExitText", "�����" },

		{ "menuViewText", "���" },
		{ "menuViewPlanText", "����-������ ������" },
		{ "menuViewTableText", "�������" },
		{ "menuViewTreeText", "������� ������������" },
		{ "menuViewTimeText", "��������� ���������" },
		{ "menuViewParamText", "��������� ���������" },
		{ "menuViewSaveText", "������� �� ������������" },
		{ "menuViewSchemeText", "���������� ����� ����" },
		{ "menuViewMapText", "�������������� ����� ����" },
		{ "menuViewAllText", "���������� ����" },

		{ "menuHelpText", "������" },
		{ "menuHelpContentsText", "����������" },
		{ "menuHelpFindText", "����� ����" },
		{ "menuHelpTipsText", "����������� ���������" },
		{ "menuHelpStartText", "���������� � ��������" },
		{ "menuHelpCourseText", "���� ����������" },
		{ "menuHelpHelpText", "������������� ������" },
		{ "menuHelpSupportText", "������ ���������" },
		{ "menuHelpLicenseText", "��������" },
		{ "menuHelpAboutText", "� ���������" },

		{ "menuOmeTimeTest", "����������� ������������"},
		{ "menuTimeTableTest", "������������ �� ����������"},
		{ "menuPeriodicalTest", "������������� ������������"},
		{ "menuAddTest", "�������� ����"},
		{ "menuRefresh", "�������� �����"},
		{ "menuSaveTest", "��������� ����� �����"},
		{ "menuKISChoise1", "���� ���"},
		{ "menuKISChoise2", "��� ����"},
		{ "menuOriginalTest", "������� ������������"},
		{ "menuExtendedTest", "����������� ������������"},
		{ "menuFilterTest", "���������� ��������� ������"},

		{ "menuParametersTesting", "��������� ������������"},
		{ "menuOperationsTesting", "�������� � �������"},
		{ "menuTypeTesting", "��� ������������"},

// Menu tooltips
		{ "menuSessionToolTip", "������" },
		{ "menuSessionNewToolTip", "����� ������" },
		{ "menuSessionCloseToolTip", "������� ������" },
		{ "menuSessionOptionsToolTip", "��������� ������" },
		{ "menuSessionConnectionToolTip", "��������� ����������" },
		{ "menuSessionChangePasswordToolTip", "�������� ������" },
		{ "menuSessionSaveToolTip", "��������� ���������" },
		{ "menuSessionUndoToolTip", "�������� ���������" },
		{ "menuExitToolTip", "�����" },

		{ "menuViewAllToolTip", "���������� ����" },

		{ "menuViewToolTip", "���" },
		{ "menuViewNavigatorToolTip", "��������� ��������" },
		{ "menuViewMessagesToolTip", "���� ��������������� ���������" },
		{ "menuViewToolbarToolTip", "������ ������������" },
		{ "menuViewRefreshToolTip", "�������� ����������" },
		{ "menuViewCatalogueToolTip", "������� ��������" },

		{ "menuHelpToolTip", "������" },
		{ "menuHelpContentsToolTip", "����������" },
		{ "menuHelpFindToolTip", "����� ����" },
		{ "menuHelpTipsToolTip", "����������� ���������" },
		{ "menuHelpStartToolTip", "���������� � ��������" },
		{ "menuHelpCourseToolTip", "���� ����������" },
		{ "menuHelpHelpToolTip", "������������� ������" },
		{ "menuHelpSupportToolTip", "������ ���������" },
		{ "menuHelpLicenseTooltip", "��������" },
		{ "menuHelpAboutToolTip", "� ���������" },

// Status Strings
		{ "statusBD", "��������� ��" },
		{ "statusBDFinish", "��������� �� ���������" },
		{ "statusTestRefresh", "���������� ������ �� ��" },
		{ "statusTestRefreshFinish", "���������� ������ �� �� ���������" },
		{ "statusTestSave", "���������� ����� ������ � ��" },
		{ "statusTestSaveFinish", "���������� ����� ������ � �� ���������" },
		{ "statusDomain", "����� ������" },
		{ "statusDomainFinish", "����� ������ ��������" },
		{ "statusReady", "��������" },
		{ "statusConnecting", "����������" },
		{ "statusError", "������" },
		{ "statusSettingSession", "������..." },
		{ "statusSettingConnection", "����������..." },
		{ "statusCancelled", "������" },
		{ "statusOpeningSession", "�������� ������" },
		{ "statusClosingSession", "�������� ������" },
		{ "statusChangingPassword", "������..." },
		{ "statusDisconnecting", "������������" },
		{ "statusDisconnected", "������������" },
		{ "statusExiting", "�����" },

		{ "statusNoConnection", "��� ����������" },
		{ "statusConnectionError", "������ ����������!" },

		{ "statusNoSession", "��� ������" },

		{ "statusNoUser", " " },
		{ "statusNoSession", "��� ������" },

// Windows titles
		{ "AppTitle", "������������ �������" },
		{ "MyPlanTitle", "����-������"},
		{ "MyTableTitle", "��������� � �������������� ������"},
		{ "MyTreeTitle", "������� ������������"},
		{ "MyTimeTitle", "��������� ��������� �����"},
		{ "MySaveTitle", "������� �����"},
		{ "MySchemeTitle", "���������� ����� ����"},
		{ "MyMapTitle", "�������������� ����� ����"},
		{ "MyParamTitle", "������������� ��������� �����"},
		{ "MyExtendedTitle", "����������� ������������"},
		{ "MyFiltrationTitle", "���������� ������"},

// Button ToolTip strings
		{ "buttonHelpToolTip", "������" },
		{ "OmeTimeTestToolTip", "����������� ������������"},
		{ "TimeTableTestToolTip", "������������ �� ����������"},
		{ "PeriodicalTestToolTip", "������������� ������������"},
		{ "AddTestToolTip", "�������� ����"},
		{ "RefreshToolTip", "�������� �����"},
		{ "DeleteTestToolTip", "������� ��������� �����"},
		{ "SaveTestToolTip", "��������� ����� �����"},
		{ "KISChoise1ToolTip", "���� ���"},
		{ "KISChoise2ToolTip", "��� ����"},
		{ "OriginalTestToolTip", "������� ������������"},
		{ "AutomatTestToolTip", "����������� ������������"},
		{ "FilterTestToolTip", "���������� ��������� ������"},
		{ "LeftMoveToolTip", "����� �����"},
		{ "RightMoveToolTip", "����� ������"},

// Button strings
		{ "Enter", "������"},

// Label strings
		{ "labelName", "���" },
		{ "labelTabbedFilter", "������" },
		{ "labelTabbedList", "������" },
		{ "labelTabbedProperties", "��������" },
		{ "labelBegin", "������" },
		{ "labelInterval", "��������" },
		{ "labelEnd", "���������" },
		{ "labelEdit", "��������������" },
		{ "labelFilter", "�����������" },
		{ "labelEqual", "���������" },
		{ "labelTime", "�����" },
		{ "labelSubstring", "���������" },
		{ "labelRange", "��������" },
		{ "labelChange", "��������" },
		{ "labelAdd", "��������" },
		{ "labelList", "������" },
		{ "labelCriteriaFilt", "�������� ����������" },
		{ "labelCriteriaDel", "������� ��������" },
		{ "labelAllTestResults", "��� ���������� ������������" },
		{ "labelIdIzmer", "������ �������������� ���������" },
		{ "labelKnownEvents", "������ ������������ �������" },
		{ "labelSaveResBD", "��������� ����������" },
		{ "labelWaveLength", "����� �����, ��" },
		{ "labelAverCount", "���������� ����������" },
		{ "labelImpuls", "������ ��������, ��" },
		{ "labelDetalM", "����������, �" },
		{ "labelMaxDistance", "������������ ���������, �" },
		{ "labelSaveBD", "�������� ������ � ��" },
		{ "labelReflect", "���������� �����������" },
		{ "labelIdIzmer", "������������� ���������" },
		{ "labelTestType", "��� �����" },
		{ "labelRoot", "������" },
		{ "labelDoing", "�����������" },
		{ "labelDone", "��������" },
		{ "labelReadyToDo", "����� � ����������" },
		{ "labelOnetime", "�����������" },
		{ "labelPeriod", "�������������" },
		{ "labelTimeTable", "�� ����������" },
		{ "labelStatus", "������" },
		{ "labelStartTime", "����� ������� �����" },
		{ "labelZapros", "������" },
		{ "labelTimeType", "��������� ���" },
		{ "labelIzmerType", "��� ���������" },
		{ "labelUslovie", "�������" },
		{ "labelZnachenie", "��������" },
		{ "labelFiltration", "����������" },
		{ "labelPoZnach", "�� ��������" },
		{ "labelPoSpisku", "�� ������" },
		{ "labelPoPodstroke", "�� ���������" },
		{ "labelPoDiapOt", "�� ��������� ��" },
		{ "labelPoDiapDo", "��" },
		{ "labelTimeOt", "�� ������� ��" },
		{ "labelTimeDo", "��" },
		{ "labelFrom", "��" },
		{ "labelTo", "��" },
		{ "labelStringForSearch", "������ ��� ������" },
		{ "labelAlarmTest", "����� � ��������" },
		{ "labelNoAlarmTest", "����� ��� �������" },
		{ "labelAlarm", "�����" },
		{ "labelTtimeTest", "����� ������������" },
		{ "labelTimeTestType", "��� ������������ (���������)" },
		{ "labelPopupEdit", "������������� ����" },
		{ "labelPopupDel", "������� ����(�)" },
		{ "labelPopupAdd", "�������� ����" },
		{ "labelPopupDelKIS", "������� ������ ����" },
		{ "labelDelTestRealQ", "�� ������������� ������ ������� ����(�)?" },
		{ "labelDelTestReal", "������������� ��������" },
		{ "labelMakeAnalysis", "������" },
		{ "labelMakeEvalAnalysis", "������������� ������" },
		{ "labelDataProcParam", "��������� ��������� ������" },
		{ "labelTipycalTests", "�������" },
		{ "labelParamTestValue", "��������� ���������" },
		{ "labelKISObjects", "������ �����" },

// Detalyze strings
		{ "DetMinute", "������" },
		{ "DetHour", "���" },
		{ "DetDay", "�����" },
		{ "DetMonth", "�����" },
		{ "DetYear", "���" },

// OR strings
		{ "ORKIS", "���" },
		{ "ORDomain", "�����" },
		{ "ORPath", "�������" },
		{ "ORPath1", "������ �����" },
		{ "ORPaths", "������� �����" },
		{ "ORMones", "������� �����" },
		{ "ORTestType1", "��� �����" },
		{ "ORTestType", "��� �����" },
		{ "ORTestTypes", "���� �����" },
		{ "ORPort", "����" },

// Error messages
		{ "errorTitleChangePassword", "������ ���������" },
		{ "errorTitleOpenSession", "������ �����" },

		{ "errorWrongName", "������� ������� ��� ������������" },
		{ "errorWrongPassword", "������� ������ ������" },
		{ "errorWrongPassword2", "����� ������ ������ ��� ������ ��������" },
		{ "errorWrongLogin", "��������� ��� ������������ � ������" },
		{ "errorWrongTest", "������ ������� �����" },
		{ "errorWrongTestObjects", "������� ������� ������� ������������!" },
		{ "errorWrongTestTime", "����� ��������� ������ ������� ������ �����!" },

// Info messages
		{ "messageTitle", "information title" },
		{ "messageTitle", "" },
		{ "messageTitle", "" },
		{ "messageTitle", "" },
		{ "messageTitle", "" },

		{ "message", "information text" },
		{ "message", "" },
		{ "message", "" },
		{ "message", "" },
		{ "message", "" },

		{ "AppText", "" }
		};

	public LangModelSchedule_ru()
	{
		symbols.setEras(new String [] {"�.�.","�� �.�."});
		symbols.setMonths(new String [] {"������","�������", "����",
				"������", "���", "����", "����", "������", "��������",
				"�������", "������", "�������"});
		symbols.setShortMonths(new String [] {"���", "���", "���", "���",
				"���", "���", "���", "���", "���", "���", "���", "���"});
//		symbols.setWeekDays(new String [] {"�����������", "�������",
//				"�����", "�������", "�������", "�������", "�����������"} );
//		symbols.setShortWeekDays(new String [] {"��", "��", "��", "��",
//				"��", "��", "��"} );
	}

	public Object[][] getContents()
	{
		return contents;
	}
}