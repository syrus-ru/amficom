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

public class LangModelOptimize_ru extends LangModelOptimize
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

		{ "menuSchemeText", "�����" },
		{ "menuMapOpenText", "������� �������������� ����� ����" },
		{ "menuSchemeOpenText", "������� ���������� ����� ����" },
		{ "menuSchemeSaveText", "��������� �� � �����" },
    { "menuSchemeSaveAsText", "��������� �� ��������"},
    { "menuLoadSmText", "��������� ��"}, // ������� ����� ���������� ( Solution )
    { "menuClearSchemeText", "�������� ����� �� ��"},

		{ "menuSchemeCloseText", "�������" },

		{ "menuViewText", "���" },
		{ "menuViewMapText", "�������������� ����� ����" },
		{ "menuViewSchemeText", "���������� ����� ����" },
		{ "menuViewMapElPropertiesText", "�������� �������� �������������� �����" },
    { "menuViewSchElPropertiesText", "�������� �������� ���������� �����" },
		{ "menuViewGraphText", "������ ���� �����������" },
		{ "menuViewSolutionText", "����������� �������" },
		{ "menuViewKISText", "��������� ���" },
		{ "menuViewModeText", "����� ����� � �������" },
		{ "menuViewParamsText", "��������� �����������" },
		{ "menuViewShowallText", "���������� ����" },

		{ "menuOptimizeText", "�����������" },
    { "menuOptimizeModeText", "�����" },
    { "menuOptimizeModeUnidirectText", "�������������" },
    { "menuOptimizeModeBidirectText", "���������" },
    { "menuOptimizeCriteriaPriceText", "���������" },
    { "menuOptimizeCriteriaPriceLoadText", "�������" },
    { "menuOptimizeCriteriaPriceSaveText", "���������" },
    { "menuOptimizeCriteriaPriceSaveasText", "��������� ���..." },
    { "menuOptimizeCriteriaPriceCloseText", "�������" },
    { "menuOptimizeCriteriaText", "��������" },
    { "menuOptimizeStartText", "�����" },
		{ "menuOptimizeStopText", "����" },

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
		{ "menuSessionSaveToolTip", "��������� ���������" },
		{ "menuSessionUndoToolTip", "�������� ���������" },
		{ "menuExitToolTip", "�����" },

		{ "menuSchemeToolTip", "�����" },
		{ "menuSchemeOpenToolTip", "������� ���������� ����� ����" },
    { "menuMapOpenToolTip", "������� �������������� ����� ����" },
		{ "menuSchemeSaveToolTip", "��������� �����" },
    { "menuSchemeSaveAsToolTip", "��������� �������" },
    { "menuLoadSmToolTip", "��������� �������" },
    { "menuClearSchemeToolTip", "�������� ����� �� ��" },
		{ "menuSchemeCloseToolTip", "�������" },

		{ "menuViewToolTip", "���" },
		{ "menuViewMapToolTip", "�������������� ����� ����" },
		{ "menuViewSchemeToolTip", "���������� ����� ����" },
		{ "menuViewMapElPropertiesToolTip", "�������� �������� �������������� �����" },
    { "menuViewSchElPropertiesToolTip", "�������� �������� ���������� �����" },
		{ "menuViewSolutionToolTip", "����������� �������" },
		{ "menuViewKISToolTip", "��������� ���" },
		{ "menuViewModeToolTip", "����� ����� � �������" },
		{ "menuViewParamsToolTip", "��������� �����������" },
		{ "menuViewShowallToolTip", "���������� ����" },

		{ "menuOptimizeToolTip", "�����������" },
    { "menuOptimizeCriteriaToolTip", "�������� �����������" },
    { "menuOptimizeModeToolTip", "����� �����������" },
    { "menuOptimizeModeUnidirectToolTip", "����� �������������� ������������" },
    { "menuOptimizeModeBidirectToolTip", "����� ���������� ������������" },
    { "menuOptimizeCriteriaPriceToolTip", "���������" },
    { "menuOptimizeCriteriaPriceLoadToolTip", "������� ���� ���" },
    { "menuOptimizeCriteriaPriceSaveToolTip", "��������� ���� ���" },
    { "menuOptimizeCriteriaPriceSaveasToolTip", "��������� ���� ��� ���..." },
    { "menuOptimizeCriteriaPriceCloseToolTip", "������� ���� ���" },
		{ "menuOptimizeStartToolTip", "�����" },
    { "menuOptimizeStopToolTip", "����" },

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
		{ "AppTitle", "�������������� �������" },

// Button strings
		{ "buttonHelp", "������" },

// Label strings
		{ "labelName", "���" },

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

	public LangModelOptimize_ru()
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