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

public class LangModelMain_ru extends LangModelMain
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
		{ "menuExitText", "�����" },

		{ "menuViewText", "���" },
		{ "menuViewPanelText", "������ ������������" },

		{ "menuToolsText", "��������������" },
		{ "menuToolsAdminText", "�����������������" },
		{ "menuToolsConfigText", "����������������" },
		{ "menuToolsComponentsText", "�������� �����������" },
		{ "menuToolsSchemeText", "�������� ���������� ����" },
		{ "menuToolsMapText", "�������� �������������� ����" },
		{ "menuToolsTraceText", "��������������" },
		{ "menuToolsScheduleText", "������������" },
		{ "menuToolsSurveyText", "����������" },
		{ "menuToolsModelText", "�������������" },
		{ "menuToolsMonitorText", "������" },
		{ "menuToolsAnalyseText", "������������" },
		{ "menuToolsNormsText", "������" },
		{ "menuToolsMaintainText", "�������������" },
		{ "menuToolsPrognosisText", "���������������" },
		{ "menuToolsReportBuilderText", "�������� �������" },

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

// Menu tooltips
		{ "menuSessionToolTip", "������" },
		{ "menuSessionNewToolTip", "����� ������" },
		{ "menuSessionCloseToolTip", "������� ������" },
		{ "menuSessionOptionsToolTip", "��������� ������" },
		{ "menuSessionConnectionToolTip", "��������� ����������" },
		{ "menuSessionChangePasswordToolTip", "�������� ������" },
		{ "menuExitToolTip", "�����" },

		{ "menuViewToolTip", "���" },
		{ "menuViewPanelToolTip", "������ ������������" },

		{ "menuToolsToolTip", "��������������" },
		{ "menuToolsAdminToolTip", "�����������������" },
		{ "menuToolsConfigToolTip", "����������������" },
		{ "menuToolsComponentsToolTip", "�������� �����������" },
		{ "menuToolsSchemeToolTip", "�������� ���������� ����" },
		{ "menuToolsMapToolTip", "�������� �������������� ����" },
		{ "menuToolsTraceToolTip", "��������������" },
		{ "menuToolsScheduleToolTip", "������������" },
		{ "menuToolsSurveyToolTip", "����������" },
		{ "menuToolsModelToolTip", "�������������" },
		{ "menuToolsMonitorToolTip", "������" },
		{ "menuToolsAnalyseToolTip", "������������" },
		{ "menuToolsNormsToolTip", "������" },
		{ "menuToolsMaintainToolTip", "�������������" },
		{ "menuToolsPrognosisToolTip", "���������������" },
		{ "menuToolsReportBuilderToolTip", "�������� �������" },

		{ "menuHelpToolTip", "������" },
		{ "menuHelpContentsToolTip", "����������" },
		{ "menuHelpFindToolTip", "����� ����" },
		{ "menuHelpTipsToolTip", "����������� ���������" },
		{ "menuHelpStartToolTip", "���������� � ��������" },
		{ "menuHelpCourseToolTip", "���� ����������" },
		{ "menuHelpHelpToolTip", "������������� ������" },
		{ "menuHelpSupportToolTip", "������ ���������" },
		{ "menuHelpLicenseToolTip", "��������" },
		{ "menuHelpAboutToolTip", "� ���������" },

// Status Strings
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

		{ "statusRunningAdmin", "������ Admin" },
		{ "statusRunningConfig", "������ Config" },
		{ "statusRunningTrace", "������ Trace" },
		{ "statusRunningSurvey", "������ Survey" },
		{ "statusRunningAnalyse", "������ Analyse" },
		{ "statusRunningNorms", "������ Norms" },

		{ "statusNoConnection", "��� ����������" },
		{ "statusConnectionError", "������ ����������!" },

		{ "statusNoSession", "��� ������" },

		{ "statusNoUser", " " },
		{ "statusNoSession", "��� ������" },

// Windows titles
		{ "AppTitle", "�������� �������" },
		{ "ChangePasswordTitle", "��������� ������" },
		{ "SessionInfoTitle", "��������� ������" },
		{ "ConnectionTitle", "��������� ���������� � ��������" },
		{ "SessionOpenTitle", "������� ������" },

// Button strings
		{ "buttonHelp", "������" },
		{ "buttonAccept", "�������" },
		{ "buttonCancel", "��������" },
		{ "buttonChange", "��������" },
		{ "buttonClose", "�������" },
		{ "buttonEnter", "����� � �������" },
		{ "buttonStandard", "�����������" },

// Label strings
		{ "labelName", "���" },
		{ "labelPassword", "������" },
		{ "labelOldPassword", "������ ������" },
		{ "labelNewPassword", "����� ������" },
		{ "labelNewPassword2", "����� ������ *" },
		{ "labelCategory", "���������" },

		{ "labelServerObject", "��������� ������" },
		{ "labelServerIP", "IP-����� �������" },
		{ "labelObjectPassword", "������ ������� � �������" },
		{ "labelObjectName", "��� ������� � �������" },
		{ "labelServerTCP", "TCP-����" },
		{ "labelSID", "SID" },

		{ "labelServer", "������" },
		{ "labelUser", "������������" },
		{ "labelCategory", "���������" },
		{ "labelSessionStart", "������ ������" },
		{ "labelSessionTotal", "����� ����� ������" },
		{ "labelServerConnectPeriod", "������ ���������� � ��������" },
		{ "labelServerConnectLast", "����� ���������� ���������� � ��������" },

// Error messages
		{ "errorTitleChangePassword", "������ ���������" },
		{ "errorTitleOpenSession", "������ �����" },

		{ "errorWrongName", "������� ������� ��� ������������" },
		{ "errorWrongPassword", "������� ������ ������" },
		{ "errorWrongPassword2", "����� ������ ������ ��� ������ ��������" },
		{ "errorWrongLogin", "��������� ��� ������������ � ������" },

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

	public LangModelMain_ru()
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