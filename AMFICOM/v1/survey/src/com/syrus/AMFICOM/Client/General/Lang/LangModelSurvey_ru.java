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

public class LangModelSurvey_ru extends LangModelSurvey
{
	static final Object[][] contents = {

		{ "language", "�������" },
		{ "country", "������" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

// Menu text
		{ "menuViewText", "���" },
		{ "menuViewNavigatorText", "��������� ��������" },
		{ "menuViewMessagesText", "���� ��������������� ���������" },
		{ "menuViewToolbarText", "������ ������������" },
		{ "menuViewRefreshText", "�������� ����������" },
		{ "menuViewCatalogueText", "������� ��������" },
		{ "menuViewMapSetupText", "�������� �� �����" },

		{ "menuEvaluateText", "��������" },
		{ "menuEvaluateRequestText", "���������� ������" },
		{ "menuEvaluateSchedulerText", "������������" },
		{ "menuEvaluateArchiveText", "����� ���������" },
		{ "menuEvaluateTrackText", "�������� ����������" },
		{ "menuEvaluateTrackRequestText", "�������" },
		{ "menuEvaluateTrackTaskText", "�������" },
		{ "menuEvaluateResultText", "����� �����������" },
		{ "menuEvaluateAnalizeText", "������" },
		{ "menuEvaluateAnalizeExtText", "������������" },
		{ "menuEvaluateNormsText", "������" },
		{ "menuEvaluatePrognosisText", "���������������" },
		{ "menuEvaluateModelingText", "�������������" },
		{ "menuEvaluateViewAllText", "������� ��" },

		{ "menuVisualizeText", "���" },
		{ "menuVisualizeNetGISText", "�������������� ����� ����" },
		{ "menuVisualizeNetText", "����� ����" },
		{ "menuVisualizeISMText", "����� ��" },
		{ "menuVisualizeISMGISText", "�������������� ����� ��" },
		{ "menuVisualizeMapEditText", "�������� ���������" },
		{ "menuVisualizeMapCloseText", "������� �������������� �����" },
		{ "menuVisualizeSchemeEditText", "�������� ����" },
		{ "menuVisualizeSchemeCloseText", "������� �����" },
		{ "menuViewAllText", "���������� ����" },

		{ "menuMaintainText", "�������������" },
		{ "menuMaintainAlarmText", "������� �������" },
		{ "menuMaintainAlertText", "���������" },
		{ "menuMaintainCallText", "������" },
		{ "menuMaintainEventText", "�������" },

		{ "menuReportText", "�����" },
		{ "menuReportTableText", "��������� �����" },
		{ "menuReportHistogrammText", "�����������" },
		{ "menuReportGraphText", "������" },
		{ "menuReportComplexText", "������� �����" },
		{ "menuReportReportText", "������" },

		{ "menuToolsText", "�����������" },
		{ "menuToolsSortText", "����������" },
		{ "menuToolsSortNewText", "����� ����������" },
		{ "menuToolsSortSaveText", "��������� ����������" },
		{ "menuToolsSortOpenText", "������� ����������" },
		{ "menuToolsSortListText", "����������" },
		{ "menuToolsFilterText", "������" },
		{ "menuToolsFilterNewText", "����� ������" },
		{ "menuToolsFilterSaveText", "��������� ������" },
		{ "menuToolsFilterOpenText", "��������� ������" },
		{ "menuToolsFilterListText", "�������" },
		{ "menuToolsFindText", "�����" },
		{ "menuToolsFindFastText", "������� �����" },
		{ "menuToolsFindWordText", "����� �� �����" },
		{ "menuToolsFindFieldText", "����� �� �����" },
		{ "menuToolsFindNextText", "����� ������" },
		{ "menuToolsFindQueryText", "������� �����" },
		{ "menuToolsBookmarkText", "��������" },
		{ "menuToolsBookmarkSetText", "��������� ��������" },
		{ "menuToolsBookmarkGotoText", "������� � ��������" },
		{ "menuToolsBookmarkListText", "������ ��������" },
		{ "menuToolsBookmarkRemoveText", "������� ��������" },
		{ "menuToolsBookmarkEditText", "������������� ��������" },
		{ "menuToolsDictionaryText", "�������" },
		{ "menuToolsLanguageText", "����" },
		{ "menuToolsLockText", "�������� ����������" },
		{ "menuToolsStyleText", "�����" },
		{ "menuToolsStyleTextText", "�����" },
		{ "menuToolsStyleGraphText", "�������" },
		{ "menuToolsStyleLineText", "�����" },
		{ "menuToolsStyleTableText", "�������" },
		{ "menuToolsStyleSchemeText", "�����" },
		{ "menuToolsStyleMapText", "�����" },
		{ "menuToolsStyleSoundText", "����" },
		{ "menuToolsStyleColorText", "����" },
		{ "menuToolsStyleLinkText", "�����" },
		{ "menuToolsOptionsText", "���������" },

		{ "menuWindowText", "����" },
		{ "menuWindowCloseText", "�������" },
		{ "menuWindowCloseAllText", "������� ���" },
		{ "menuWindowTileHorizontalText", "����������� �� �����������" },
		{ "menuWindowTileVerticalText", "����������� �� ���������" },
		{ "menuWindowCascadeText", "�������������" },
		{ "menuWindowArrangeText", "�����������" },
		{ "menuWindowArrangeIconsText", "����������� ������" },
		{ "menuWindowMinimizeAllText", "�������� ���" },
		{ "menuWindowRestoreAllText", "������������ ���" },
		{ "menuWindowListText", "������" },

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

		{ "menuViewToolTip", "���" },
		{ "menuViewNavigatorToolTip", "��������� ��������" },
		{ "menuViewMessagesToolTip", "���� ��������������� ���������" },
		{ "menuViewToolbarToolTip", "������ ������������" },
		{ "menuViewRefreshToolTip", "�������� ����������" },
		{ "menuViewCatalogueToolTip", "������� ��������" },
		{ "menuViewMapSetupToolTip", "�������� �� �����" },

		{ "menuEvaluateToolTip", "��������" },
		{ "menuEvaluateRequestToolTip", "���������� ������" },
		{ "menuEvaluateSchedulerToolTip", "�����������" },
		{ "menuEvaluateArchiveToolTip", "����� ���������" },
		{ "menuEvaluateTrackToolTip", "�������� ����������" },
		{ "menuEvaluateTrackRequestToolTip", "�������" },
		{ "menuEvaluateTrackTaskToolTip", "�������" },
		{ "menuEvaluateResultToolTip", "����� �����������" },
		{ "menuEvaluateAnalizeToolTip", "������" },
		{ "menuEvaluateAnalizeExtToolTip", "������������" },
		{ "menuEvaluateNormsToolTip", "������" },
		{ "menuEvaluatePrognosisToolTip", "���������������" },
		{ "menuEvaluateModelingToolTip", "�������������" },
		{ "menuEvaluateViewAllToolTip", "������� ��" },

		{ "menuVisualizeToolTip", "������������" },
		{ "menuVisualizeNetGISToolTip", "��������� ����" },
		{ "menuVisualizeNetToolTip", "����� ����" },
		{ "menuVisualizeISMToolTip", "����� ���" },
		{ "menuVisualizeISMGISToolTip", "��������� ���" },
		{ "menuViewAllToolTip", "���������� ����" },

		{ "menuMaintainToolTip", "�������������" },
		{ "menuMaintainAlarmToolTip", "������� �������" },
		{ "menuMaintainAlertToolTip", "���������" },
		{ "menuMaintainCallToolTip", "������" },

		{ "menuReportToolTip", "�����" },
		{ "menuReportTableToolTip", "��������� �����" },
		{ "menuReportHistogrammToolTip", "�����������" },
		{ "menuReportGraphToolTip", "������" },
		{ "menuReportComplexToolTip", "������� �����" },
		{ "menuReportReportToolTip", "������" },

		{ "menuToolsToolTip", "�����������" },
		{ "menuToolsSortToolTip", "����������" },
		{ "menuToolsSortNewToolTip", "����� ����������" },
		{ "menuToolsSortSaveToolTip", "��������� ����������" },
		{ "menuToolsSortOpenToolTip", "������� ����������" },
		{ "menuToolsSortListToolTip", "����������" },
		{ "menuToolsFilterToolTip", "������" },
		{ "menuToolsFilterNewToolTip", "����� ������" },
		{ "menuToolsFilterSaveToolTip", "��������� ������" },
		{ "menuToolsFilterOpenToolTip", "��������� ������" },
		{ "menuToolsFilterListToolTip", "�������" },
		{ "menuToolsFindToolTip", "�����" },
		{ "menuToolsFindFastToolTip", "������� �����" },
		{ "menuToolsFindWordToolTip", "����� �� �����" },
		{ "menuToolsFindFieldToolTip", "����� �� �����" },
		{ "menuToolsFindNextToolTip", "����� ������" },
		{ "menuToolsFindQueryToolTip", "������� �����" },
		{ "menuToolsBookmarkToolTip", "��������" },
		{ "menuToolsBookmarkSetToolTip", "��������� ��������" },
		{ "menuToolsBookmarkGotoToolTip", "������� � ��������" },
		{ "menuToolsBookmarkListToolTip", "������ ��������" },
		{ "menuToolsBookmarkRemoveToolTip", "������� ��������" },
		{ "menuToolsBookmarkEditToolTip", "������������� ��������" },
		{ "menuToolsDictionaryToolTip", "�������" },
		{ "menuToolsLanguageToolTip", "����" },
		{ "menuToolsLockToolTip", "�������� ����������" },
		{ "menuToolsStyleToolTip", "�����" },
		{ "menuToolsStyleToolTipToolTip", "�����" },
		{ "menuToolsStyleGraphToolTip", "�������" },
		{ "menuToolsStyleLineToolTip", "�����" },
		{ "menuToolsStyleTableToolTip", "�������" },
		{ "menuToolsStyleSchemeToolTip", "�����" },
		{ "menuToolsStyleMapToolTip", "�����" },
		{ "menuToolsStyleSoundToolTip", "����" },
		{ "menuToolsStyleColorToolTip", "����" },
		{ "menuToolsStyleLinkToolTip", "�����" },
		{ "menuToolsOptionsToolTip", "���������" },

		{ "menuWindowToolTip", "����" },
		{ "menuWindowCloseToolTip", "�������" },
		{ "menuWindowCloseAllToolTip", "������� ���" },
		{ "menuWindowTileHorizontalToolTip", "����������� �� �����������" },
		{ "menuWindowTileVerticalToolTip", "����������� �� ���������" },
		{ "menuWindowCascadeToolTip", "�������������" },
		{ "menuWindowArrangeToolTip", "�����������" },
		{ "menuWindowArrangeIconsToolTip", "����������� ������" },
		{ "menuWindowMinimizeAllToolTip", "�������� ���" },
		{ "menuWindowRestoreAllToolTip", "������������ ���" },
		{ "menuWindowListToolTip", "������" },

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
		{ "AppTitle", "���������� �������" },
		{ "ResultFrameTitle", "����������" },
		{ "titleObjectsCatalog", "�������� ������������" },
		{ "AlarmTitle", "������� �������" },

// Button strings
		{ "buttonHelp", "������" },

////////////////////////////////////////////////////////////////////////////////
// Alarm strings (now filter also uses them - instead of "label...")
    { "label_alarm","����� �� �������� �������"},
		{ "alarm_Source", "�������� �������" },
		{ "alarm_Type", "��� �������" },
		{ "alarm_Status", "���������" },
		{ "alarm_Time", "����� ��������� �������" },
		{ "alarm_Monitoredelement", "����������� ������" },

		{"alarm_Assigned", "������ ����������"},
    {"alarm_Assigned_to", "������������� �����������"},

    {"alarm_Fixed_when","��������� �����"},
    {"alarm_Fixed_by","�����������"},
    {"alarm_Comments","�����������"},
// Alarm

//Equipment
    { "label_equipment","�������������� ������������"},

    { "equip_name", "��������" },
    { "equip_type", "���" },
    { "equip_domen", "�����" },
    { "equip_longitude", "�������" },
    { "equip_latitude", "������" },
    { "equip_hSN", "���-� ����� ������-�" },
    { "equip_sSN", "���-� ����� ��" },
    { "equip_hVer", "������� ������ ������-�" },
    { "equip_sVer", "������� ������ ��" },
    { "equip_regN", "��������������� �����" },
    { "equip_manufacter", "�������������" },
    { "equip_manCode", "��� �������������" },
    { "equip_supplier", "���������" },
    { "equip_supCode", "��� ����������" },

//Equipment

//Port
    { "label_ports","�����"},

    { "port_name", "��������" },
    { "port_description", "��������" },
    { "port_type_id", "���" },

    { "port_equipment_id", "������������" },
    { "port_interface_id", "���������" },
    { "port_address_id", "�����" },
    { "port_domain_id", "�����" },

//    { "port_optical_chars", "������� ������ ��" },
//    { "port_electrical_chars", "��������������� �����" },
//    { "port_interface_chars", "�������������" },
//    { "port_expluatation_chars", "��� �������������" },
//    public Hashtable characteristics;
//Port
    {"menuReportBuilderToolTip","������� �������� ��������"},
    {"menuAlarmAlertToolTip","������� ������������� �������� �������"},

///////////////////////////////////////////////////////////////////////////////////

// Filter label strings
		{ "labelsource", "��������" },
		{ "labelAlarmType", "���" },
		{ "labelStatus", "������" },
		{ "labelalarmtime", "����� ���������" },
		{ "labelmonitoredelement", "����������� ������" },

// Label strings
		{ "ArgParameter", "������� ��������" },
		{ "ArgValue", "��������" },
		{ "ResParameter", "�������� ��������" },
		{ "ResValue", "��������" },
		{ "ResultTabTitle", "���������" },
		{ "ReflectogramTabTitle", "��������������" },

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

		{ "labelALARM_STATUS_GENERATED", "�����" },
		{ "labelALARM_STATUS_ASSIGNED", "�����������" },
		{ "labelALARM_STATUS_FIXED", "���������" },

		{ "labelDoing", "�����������" },
		{ "labelDone", "��������" },
		{ "labelReadyToDo", "����� � ����������" },

		{ "labelOnetime", "�����������" },
		{ "labelPeriod", "�������������" },
		{ "labelTimeTable", "�� ����������" },

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

	public LangModelSurvey_ru()
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