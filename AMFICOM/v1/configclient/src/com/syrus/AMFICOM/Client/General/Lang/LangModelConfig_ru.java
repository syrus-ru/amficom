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
// * ��������: ���������� ��������� ����� ���������� ��������� ����       * //
// *           (�������� ���������� ������ pmServer � ������ pmRISDImpl)  * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelConfig_ru.java                   * //
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

public class LangModelConfig_ru extends LangModelConfig
{
	public Object[][] contents = {

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
		{ "menuViewNavigatorText", "��������� ��������" },
		{ "menuViewMessagesText", "���� ��������������� ���������" },
		{ "menuViewToolbarText", "������ ������������" },
		{ "menuViewAttributesText", "���� �������" },
		{ "menuViewElementsText", "���� ���������" },
		{ "menuViewSetupText", "�������� �� �����" },
		{ "menuViewMapText", "���� �����" },
		{ "menuViewMapSchemeText", "���� �������������� ��������" },

		{ "menuViewRefreshText", "�������� ����������" },
		{ "menuViewCatalogueText", "������� ��������" },
		{ "menuViewElementsNavigatorText", "������ ��������" },
		{ "menuViewAllText", "������� ��" },

		{ "menuMapText", "�����" },
    { "menuMapNewText", "�����"},
		{ "menuMapOpenText", "�������" },
		{ "menuMapCloseText", "�������" },
		{ "menuMapSaveText", "���������" },
		{ "menuMapOptionsText", "���������" },
		{ "menuMapCatalogueText", "���������� �������" },

		{ "menuSchemeText", "�����" },
		{ "menuSchemeMapBitmapsText", "��������� �����������" },
		{ "menuSchemeMapIconsText", "�����������" },
		{ "menuSchemeMapStyleText", "����� �����������" },
		{ "menuSchemeMapText", "�����" },
		{ "menuSchemeMapGISText", "���" },
		{ "menuSchemeMapCoordText", "������� ���������" },
		{ "menuSchemeNetText", "����� ��" },
		{ "menuSchemeNetSchemeText", "�������� ���������� ����" },
		{ "menuSchemeNetAttributeText", "��������" },
		{ "menuSchemeNetElTypeText", "�������� �����������" },
		{ "menuSchemeNetElementText", "�������� �����������" },
		{ "menuSchemeNetViewText", "�������� �������������� ����" },
		{ "menuSchemeNetCatalogueText", "������� ���� �� �����" },
		{ "menuSchemeNetOpenText", "�������� �������������� ����� ����" },
		{ "menuSchemeNetOpenSchemeText", "�������� ���������� ����� ����" },
		{ "menuSchemeJText", "����� ��" },
		{ "menuSchemeJSchemeText", "�����" },
		{ "menuSchemeJAttributeText", "��������" },
		{ "menuSchemeJElTypeText", "���� ���������" },
		{ "menuSchemeJElementText", "��������" },
		{ "menuSchemeJLayoutText", "��� �� �����" },
		{ "menuSchemeJCatalogueText", "������� ����" },
		{ "menuSchemeJOpenText", "�������" },

		{ "menuObjectText", "������� ����" },
		{ "menuObjectNavigatorText", "��������� ��������" },
		{ "menuObjectDomainText", "������" },
		{ "menuNetText", "������� ��" },
		{ "menuNetDirText", "���������� ��" },
		{ "menuNetDirAddressText", "���� �������" },
		{ "menuNetDirResourceText", "���� ������� ��������" },
		{ "menuNetDirEquipmentText", "���� ������������" },
		{ "menuNetDirProtocolText", "��������� ��������������" },
		{ "menuNetDirLinkText", "���� ����� �����" },
		{ "menuNetDirCableText", "���� ��������� ����� �����" },
    { "menuNetDirPortText", "���� ������" },
    { "menuNetDirCablePortText", "���� ��������� ������" },
		{ "menuNetDirTechnologyText", "���������� ��������������" },
		{ "menuNetDirInterfaceText", "���� �����������" },
		{ "menuNetDirPortText", "���� ������� ������" },
		{ "menuNetDirStackText", "���� ����������" },
		{ "menuNetCatText", "������� ��" },
		{ "menuNetCatEquipmentText", "������������" },
		{ "menuNetCatLinkText", "����� �����" },
		{ "menuNetCatCableText", "��������� ����� �����" },
    { "menuNetCatPortText", "�����" },
    { "menuNetCatCablePortText", "��������� �����" },
		{ "menuNetCatResourceText", "������� �������" },
		{ "menuNetCatTPGroupText", "������ ����� ����������" },
		{ "menuNetCatTestPointText", "����� ����������" },
		{ "menuNetLocationText", "����������" },

		{ "menuJText", "������� ��" },
		{ "menuJDirText", "���������� ��" },
		{ "menuJDirKISText", "���� ����������-�������������� ������������" },
		{ "menuJDirAccessPointText", "���� ������ ������������" },
		{ "menuJDirPathText", "���� ����� ������������" },
		{ "menuJDirLinkText", "���� ����������" },
		{ "menuJCatText", "������� ��" },
		{ "menuJCatKISText", "����������-������������� ������������" },
		{ "menuJCatAccessPointText", "����� ������������" },
		{ "menuJCatPathText", "���� ������������" },
		{ "menuJCatResourceText", "������� �������" },
		{ "menuJInstallText", "����������� ���" },

		{ "menuMaintainText", "�������������" },
		{ "menuMaintainTypeText", "���� ��������� ��������" },
		{ "menuMaintainEventText", "�������" },
		{ "menuMaintainAlarmRuleText", "������� ������������� �������� �������" },
		{ "menuMaintainMessageRuleText", "������� ������������� ���������" },
		{ "menuMaintainAlertRuleText", "������� ����������" },
		{ "menuMaintainReactRuleText", "������� ������������" },
		{ "menuMaintainRuleText", "������� �������������" },
		{ "menuMaintainCorrectRuleText", "������� �����������" },

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
		{ "menuWindowTileHorizontalText", "����������� �������������" },
		{ "menuWindowTileVerticalText", "����������� �����������" },
		{ "menuWindowCascadeText", "�������������" },
		{ "menuWindowArrangeText", "�����������" },
		{ "menuWindowArrangeIconsText", "����������� ������" },
		{ "menuWindowMinimizeAllText", "�������������� ���" },
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
		{ "menuViewAttributesToolTip", "���� �������" },
		{ "menuViewElementsToolTip", "���� ���������" },
		{ "menuViewSetupToolTip", "�������� �� �����" },
		{ "menuViewMapToolTip", "���� �����" },
		{ "menuViewMapSchemeToolTip", "���� �������������� ��������" },
		{ "menuViewRefreshToolTip", "�������� ����������" },
		{ "menuViewCatalogueToolTip", "������� ��������" },
		{ "menuViewElementsNavigatorToolTip", "������ ��������" },
		{ "menuViewAllText", "������� ��" },

		{ "menuMapText", "�����" },
		{ "menuMapNewText", "�����"},
		{ "menuMapOpenText", "�������" },
		{ "menuMapCloseText", "�������" },
		{ "menuMapSaveText", "���������" },
		{ "menuMapOptionsText", "���������" },
		{ "menuMapCatalogueText", "���������� �������" },

		{ "menuObjectToolTip", "������� ����" },
		{ "menuObjectNavigatorToolTip", "��������� ��������" },
		{ "menuObjectDomainToolTip", "������" },
		{ "menuNetToolTip", "������� ����" },
		{ "menuNetDirToolTip", "���������� ����" },
		{ "menuNetDirAddressToolTip", "���� �������" },
		{ "menuNetDirResourceToolTip", "���� ������� ��������" },
		{ "menuNetDirEquipmentToolTip", "���� ������������" },
		{ "menuNetDirProtocolToolTip", "��������� ��������������" },
		{ "menuNetDirLinkToolTip", "���� ����� �����" },
		{ "menuNetDirTechnologyToolTip", "���������� ��������������" },
		{ "menuNetDirInterfaceToolTip", "���� �����������" },
		{ "menuNetDirPortToolTip", "���� ������� ������" },
		{ "menuNetDirStackToolTip", "���� ����������" },
		{ "menuNetCatToolTip", "������� ����" },
		{ "menuNetCatEquipmentToolTip", "������������" },
		{ "menuNetCatLinkToolTip", "����� �����" },
		{ "menuNetCatCableToolTip", "��������� ����� �����" },
		{ "menuNetCatResourceToolTip", "������� �������" },
		{ "menuNetCatTPGroupToolTip", "������ ����� ����������" },
		{ "menuNetCatTestPointToolTip", "����� ����������" },
		{ "menuNetLocationToolTip", "����������" },

		{ "menuJToolTip", "������� ���" },
		{ "menuJDirToolTip", "���������� ���" },
		{ "menuJDirKISToolTip", "���� ����������-������������� �������" },
		{ "menuJDirAccessPointToolTip", "���� ������ �������" },
		{ "menuJDirLinkToolTip", "���� ����������" },
		{ "menuJCatToolTip", "������� ���" },
		{ "menuJCatKISToolTip", "����������-������������� ��������" },
		{ "menuJCatAccessPointToolTip", "���� �������" },
		{ "menuJCatResourceToolTip", "������� �������" },

		{ "menuSchemeToolTip", "�����" },
		{ "menuSchemeMapBitmapsToolTip", "��������� �����������" },
		{ "menuSchemeMapIconsToolTip", "�����������" },
		{ "menuSchemeMapStyleToolTip", "����� �����������" },
		{ "menuSchemeMapToolTip", "�����" },
		{ "menuSchemeMapGISToolTip", "���" },
		{ "menuSchemeMapCoordToolTip", "������� ���������" },
		{ "menuSchemeNetToolTip", "����� ����" },
		{ "menuSchemeNetSchemeToolTip", "�������� ���������� ����" },
		{ "menuSchemeNetAttributeToolTip", "��������" },
		{ "menuSchemeNetElTypeToolTip", "�������� �����������" },
		{ "menuSchemeNetElementToolTip", "�������� �����������" },
		{ "menuSchemeNetCatalogueToolTip", "������� ���� �� �����" },
		{ "menuSchemeNetViewToolTip", "�������� �������������� ����" },
		{ "menuSchemeNetOpenToolTip", "�������� �������������� ����� ����" },
		{ "menuSchemeNetOpenSchemeToolTip", "�������� ���������� ����� ����" },
		{ "menuSchemeJToolTip", "����� ���" },
		{ "menuSchemeJSchemeToolTip", "�����" },
		{ "menuSchemeJAttributeToolTip", "��������" },
		{ "menuSchemeJElTypeToolTip", "���� ���������" },
		{ "menuSchemeJElementToolTip", "��������" },
		{ "menuSchemeJLayoutToolTip", "��� �� �����" },
		{ "menuSchemeJCatalogueToolTip", "������� ����" },

		{ "menuMaintainToolTip", "�������������" },
		{ "menuMaintainTypeToolTip", "���� ��������� ��������" },
		{ "menuMaintainEventToolTip", "�������" },
		{ "menuMaintainAlarmRuleToolTip", "������� ������������� �������� �������" },
		{ "menuMaintainMessageRuleToolTip", "������� ������������� ���������" },
		{ "menuMaintainAlertRuleToolTip", "������� ����������" },
		{ "menuMaintainReactRuleToolTip", "������� ������������" },
		{ "menuMaintainRuleToolTip", "������� �������������" },
		{ "menuMaintainCorrectRuleToolTip", "������� �����������" },

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
		{ "menuToolsStyleTextToolTip", "�����" },
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
		{ "menuWindowTileHorizontalToolTip", "����������� �������������" },
		{ "menuWindowTileVerticalToolTip", "����������� �����������" },
		{ "menuWindowCascadeToolTip", "�������������" },
		{ "menuWindowArrangeToolTip", "�����������" },
		{ "menuWindowArrangeIconsToolTip", "����������� ������" },
		{ "menuWindowMinimizeAllToolTip", "�������������� ���" },
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
		{ "menuHelpAboutToolTip", "� ���������" },
		{ "menuJInstallToolTip", "����������� ���" },

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
		{ "MapTitle", "�������� ��������� �������" },
		{ "AppTitle", "���������������� �������" },
		{ "ChangePasswordTitle", "��������� ������" },
		{ "SessionInfoTitle", "��������� ������" },
		{ "ConnectionTitle", "��������� ���������� � ��������" },
		{ "SessionOpenTitle", "������� ������" },
		{ "titleMapsCatalog", "������� ���� ����" },

// Button strings
		{ "buttonHelp", "������" },
		{ "buttonAccept", "�������" },
		{ "buttonAdd", "��������" },
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


		{ "appText", "" },

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

		{ "labelALARM_STATUS_GENERATED", "�����" },
		{ "labelALARM_STATUS_ASSIGNED", "�����������" },
		{ "labelALARM_STATUS_FIXED", "���������" },

// Alarm

//Equipment
    { "label_equipment","����� �������������� ������������"},

    { "equip_domen", "�����" },
    { "equip_longitude", "�������" },
    { "equip_latitude", "������" },
    { "equip_hSN", "���-� ����� ������-�" },
    { "equip_sSN", "���-� ����� ��" },
    { "equip_hVer", "������� ������ ������-�" },
    { "equip_sVer", "������� ������ ��" },
    { "equip_regN", "��������������� �����" },

    { "equip_agent", "����� ���" },

    { "equip_portsNumber", "����� ������" },
    { "equip_cablePortsNumber1", "�����" },
    { "equip_cablePortsNumber2", "��������� ������" },

    { "equip_hVer1", "������� ������" },
    { "equip_SN1", "�������� �����" },
    { "equip_2", "������������" },
    { "equip_soft2", "����������� �����������" },
//Equipment

//Ports
    { "label_ports","�����"},
    { "label_port", "����" },

    { "port_equipment_id", "������������" },
    { "port_interface_id", "���������" },
    { "port_address_id", "�����" },
    { "port_domain_id", "�����" },
    { "port_class", "�����" },

    { "label_cableports","��������� �����"},
    { "label_accessports","����� �������"},
    { "label_accessport","���� �������"},
    { "label_evalport","������������� ����"},
//Ports

//Links
		{ "label_links","����� �����"},
		{ "label_link","����� �����"},

		{ "link_domain_id", "�����" },
		{ "link_class", "����� ����������" },

		{ "link_start_equipment_id", "��������� ����������"},
		{ "link_start_equipment_id1", "���������� ����������"},
		{ "link_start_port_id", "��������� ����"},
		{ "link_end_equipment_id", "�������� ����������"},
		{ "link_end_equipment_id1", "��������� ����������"},
		{ "link_end_port_id", "�������� ����"},

		{ "link_optical_length", "���������� �����" },
		{ "link_physical_length", "������������ �����" },

		{ "link_inventory_nr", "��������������� �����" },

		{ "label_cablelinks","��������� ����� �����"},
		{ "label_cablelink","��������� ����� �����"},
//Links

// for AvailableReportsTreeModel
		{ "label_equipChars", "�������������� ������������"},
		{ "mufta", "�����"},
		{ "switch", "���������� �������������"},
		{ "cross", "�����-������"},
		{ "multiplexer", "��������������"},
		{ "filter", "�������"},
		{ "transmitter", "�����������"},
		{ "reciever", "��������"},
		{ "tester", "���"},
// for AvailableReportsTreeModel


//characteristics and several other
    { "label_configuration","����������������"},

    { "status_initing_data", "������������� ������..." },
    { "status_init_data_complete", "������ ���������" },

    { "label_id", "�������������" },
    { "label_name", "��������" },
    { "label_description", "��������" },
    { "label_type", "���" },

    { "label_modified1", "�����" },
    { "label_modified2", "���������� ���������" },

    { "label_manufacter", "�������������" },
    { "label_manCode", "��� �������������" },
    { "label_supplier", "���������" },
    { "label_supCode", "��� ����������" },

    { "label_local_addr1", "������" },
    { "label_local_addr2", "��������� ���������" },

    { "label_inventory_nr1", "��������������� " },
    { "label_inventory_nr2", "�����" },
    { "label_test_types", "���� ������������"},
    { "label_fibers", "�������"},
    { "label_mark", "�����"},

    { "label_chars", "��������������"},
    { "label_char", "��������������"},
    { "label_new_char", "����� ��������������"},
    { "label_add_char", "�������� ��������������"},
    { "label_delete_char", "������� ��������������"},
    { "label_comp_chars", "�������������� ����������"},
    { "label_opt_chars", "���������� ��������������"},
    { "label_el_chars", "������������� ��������������"},
    { "label_exp_chars", "���������������� ��������������"},
    { "label_interface_chars", "������������ ��������������"},
    { "label_general", "�����"},
    { "label_additional", "������"},

		{ "label_schemeStructure", "��������� ����" },
		{ "label_topologicalScheme", "�������������� �����" },
		{ "label_nodes", "����" },
		{ "label_topologicalNodes", "�������������� ����" },
		{ "label_inputLinks", "�������� ����� �����" },
		{ "label_marks", "�����" },
		{ "label_KIS", "���" },

    { "err_incorrect_data_input", "����������� ������� ������!"}

//characteristics and several other

		};

	public LangModelConfig_ru()
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

