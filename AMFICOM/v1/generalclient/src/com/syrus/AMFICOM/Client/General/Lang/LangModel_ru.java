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
// * ��������: ������� ������ ��������� �������� �� ������� �� �������    * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 1.0                                                          * //
// * ��: 1 jul 2002                                                       * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModel_ru.java                         * //
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

public class LangModel_ru extends LangModel
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
		{ "menuSessionDomainText", "����� ������" },
		{ "menuExitText", "�����" },

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
		{ "menuSessionDomainToolTip", "����� ������" },
		{ "menuExitToolTip", "�����" },

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

		{ "statusRunningAdmin", "������ Admin" },
		{ "statusRunningConfig", "������ Config" },
		{ "statusRunningTrace", "������ Trace" },
		{ "statusRunningSurvey", "������ Survey" },
		{ "statusRunningAnalyse", "������ Analyse" },
		{ "statusRunningNorms", "������ Norms" },

		{ "statusNoConnection", "��� ����������" },
		{ "statusConnectionError", "������ ����������!" },

		{ "statusNoSession", "��� ������" },

		{ "statusNoDomain", "����� �� ������" },

		{ "statusNoUser", " " },
		{ "statusNoSession", "��� ������" },

// Windows titles
		{ "AppTitle", "�������� �������" },
		{ "ChangePasswordTitle", "��������� ������" },
		{ "SessionInfoTitle", "��������� ������" },
		{ "ConnectionTitle", "��������� ���������� � ��������" },
		{ "SessionOpenTitle", "������� ������" },
		{ "SessionDomainTitle", "����� ������" },

		{ "CalendarTitle", "���������" },
		{ "YearPostfix", "�." },
		{ "Today", "�������" },


// Button strings
		{ "buttonHelp", "������" },
		{ "buttonAccept", "�������" },
		{ "buttonCancel", "��������" },
		{ "buttonChange", "��������" },
		{ "buttonClose", "�������" },
		{ "buttonEnter", "����� � �������" },
		{ "buttonStandard", "�����������" },
		{ "buttonCheck", "����" },
		{ "buttonSelect", "�������" },

		{ "Help", "������" },
		{ "Ok", "OK" },
		{ "Remove", "�������" },
		{ "Cancel", "��������" },

// Label strings
		{ "labelName", "���" },
		{ "labelPassword", "������" },
		{ "labelOldPassword", "������ ������" },
		{ "labelNewPassword", "����� ������" },
		{ "labelNewPassword2", "����� ������ *" },
		{ "labelCategory", "���������" },
		{ "labelDomain", "�����" },

		{ "labelServerIP", "IP-����� �������:" },
		{ "labelServerTCP", "TCP-����:" },
		{ "labelSID", "SID:" },
		{ "labelCtx", "������������� ������:" },
		{ "labelServerObject", "��������� ������:" },
		{ "labelServiceURL", "URL �������:"},
		{ "labelFullURL", "������ URL:"},
		{ "labelObjectName", "��� ������� � �������:" },
		{ "labelObjectPassword", "������ ������� � �������:" },

		{ "labelServer", "������" },
		{ "labelUser", "������������" },
		{ "labelCategory", "���������" },
		{ "labelSessionStart", "������ ������" },
		{ "labelSessionTotal", "����� ����� ������" },
		{ "labelActiveDomain", "�������� �����" },
		{ "labelServerConnectLast", "����� ���������� ����������" },
		{ "labelServerConnectLast2", " � ��������" },

// Label strings for filters
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

// Label strings for tree
		{ "labelRoot", "������" },
		{ "ORMones", "������� �����" },
		{ "ORTestTypes", "���� �����" },

// Label strings
		{ "labelName", "���" },
		{ "labelTabbedFilter", "������" },
		{ "labelTabbedList", "������" },
		{ "labelTabbedProperties", "��������" },

// Error messages
		{ "errorTitleChangePassword", "������ ��������� ������" },
		{ "errorTitleOpenSession", "������ �����" },

		{ "errorWrongName", "������� ������� ��� ������������" },
		{ "errorWrongPassword", "������� ������ ������" },
		{ "errorPasswordTooShort", "����� ������ ������� ��������" },
		{ "errorWrongPassword2", "����� ������ ������ ��� ������ ��������" },
		{ "errorWrongLogin", "��������� ��� ������������ � ������" },

// Info messages
		{ "messageTitle", "information title" },

		{ "message", "information text" },

		{ "connectionSuccess", "���� ����������!" },
		{ "connectionFail", "��� ����������" },

// Node names
		{ "nodeoperator", "���������" },
		{ "nodeoperatorcategory", "���������" },
		{ "nodeoperatorprofile", "�������" },
		{ "nodeoperatorrole", "����" },
		{ "nodeoperatorprivilege", "����������" },
		{ "nodeoperatorgroup", "������" },
		{ "nodesubscriber", "��������" },
		{ "nodesubscribercategory", "���������" },
		{ "nodesubscriberprofile", "�������" },
		{ "nodesubscribergroup", "������" },
		{ "nodeorganization", "������� �������" },
		{ "nodeorganizationcategory", "���������" },
		{ "nodeorganizationgroup", "������" },
		{ "nodeorganizationrole", "����" },
		{ "nodeorganizationprofile", "�������" },
		{ "nodeorganizationorder", "������� ��������������" },
		{ "nodeorganizationauto", "������������� �������������" },
		{ "nodeoperational", "������ ������������" },
		{ "nodeoperationalgroup", "������" },
		{ "nodeoperationalrole", "����" },
		{ "nodeoperationalprivilege", "����������" },
		{ "nodeoperationalstaff", "��������" },
		{ "nodeobject", "�������" },
//		{ "nodemapconnectionpointelement", "����� ����������" },

		{ "nodeaccessport", "������������� ����" },
		{ "nodeaccessporttype", "��� �������������� �����" },
		{ "nodeactionargument", "������� ���������" },
		{ "nodeactionparameter", "�������� ���������" },
		{ "nodeagent", "����� ���" },
		{ "nodealarm", "������ �������" },
		{ "nodealarmrule", "������� ���������" },
		{ "nodealerting", "����������" },
		{ "nodeanalysis", "������" },
		{ "nodeanalysiscriteria", "�������� �������" },
		{ "nodeanalysistype", "���� �������" },
		{ "nodeanalysistypeargument", "������� ��������� ���� �������" },
		{ "nodeanalysistypecriteria", "�������� ���� �������" },
		{ "nodeanalysistypeparameter", "�������� ��������� ���� �������" },
		{ "nodeattribute", "��������" },
		{ "nodeattributerule", "������� ���������" },
		{ "nodecablelink", "��������� ����� �����" },
		{ "nodecablelinkthread", "������� ��������� ����� �����" },
		{ "nodecablelinktype", "��� ��������� ����� �����" },
		{ "nodecablethread", "������� � ������" },
		{ "nodecableport", "��������� ����" },
		{ "nodecableporttype", "��� ���������� �����" },
		{ "nodecharacteristic", "��������������" },
		{ "nodecharacteristictype", "��� ��������������" },
		{ "nodeclient", "������" },
		{ "nodecomm_perm_attrib", "������ � �������� ������" },
		{ "nodecriteria", "�������� ��������" },
		{ "nodecriteriaset", "����� ��������� �������" },
		{ "nodecriteriasetmelink", "����� ������ ��������� ������� � ����������� ��������" },
		{ "nodecriteriatype", "��� ��������� �������" },
		{ "nodedeviceport", "�������������� ����" },
		{ "nodedomain", "������" },
		{ "nodeelementaryresult", "���������" },
		{ "nodeelementattribute", "��������" },
		{ "nodeelementattributetype", "��� ��������" },
		{ "nodeelementcharacteristic", "��������������" },
		{ "nodeequipment", "������������" },
		{ "nodeequipmenttype", "��� ������������" },
		{ "nodeetalon", "������" },
		{ "nodeetalontypeparameter", "��������� ���� �������" },
		{ "nodeetalonparameter", "��������� �������" },
		{ "nodeevaluation", "������" },
		{ "nodeevaluationargument", "������� ��������� ������" },
		{ "nodeevaluationtype", "��� ������" },
		{ "nodeevaluationtypeargument", "������� ��������� ���� ������" },
		{ "nodeevaluationtypeparameter", "�������� ��������� ���� ������" },
		{ "nodeevaluationtypethreshold", "������ ���� ������" },
		{ "nodeevent", "�������" },
		{ "nodeeventsource", "�������� �������" },
		{ "nodeglobalparametertype", "��� ���������" },
		{ "nodeimageresource", "�����������" },
		{ "nodeismmapcontext", "��������� ���" },
		{ "nodekis", "���" },
		{ "nodekistype", "��� ���" },
		{ "nodelink", "����� �����" },
		{ "nodelinktype", "��� ����� �����" },
		{ "nodeloggeduser", "������� ������������ �������" },
		{ "nodemapcontext", "��������� ����" },
		{ "nodemapelementlink", "����� ����� � ��������� �����" },
		{ "nodemapprotogroup", "������ ���������" },
		{ "nodemapequipmentelement", "������� ����" },
		{ "nodemapkiselement", "���� � ���" },
		{ "nodemaplinkelement", "����� �����" },
		{ "nodemaplinkproto", "������ ����� �����" },
		{ "nodemapmarkelement", "�����" },
		{ "nodemapmarker", "������" },
		{ "nodemapalarmmarker", "������ �������" },
		{ "nodemapeventmarker", "�������" },
		{ "nodemapnodeelement", "�������������� ����" },
		{ "nodemapnodelinkelement", "�������� ����� �����" },
		{ "nodemappathelement", "���� ������������" },
		{ "nodemappathlink", "����� ����� � ��������� �����" },
		{ "nodemappathproto", "������ ���� ������������" },
		{ "nodemapprotoelement", "������ ������������" },
		{ "nodemodeling", "�������������" },
		{ "nodemodelingtype", "��� �������������" },
		{ "nodemonitoredelement", "����������� ������" },
		{ "nodemonitoredelementattachment", "����� ������������ ������� � ���" },
		{ "nodeoperatorcategorylink", "����� ��������� � ����������" },
		{ "nodeoperatorgrouplink", "����� ��������� � �������" },
		{ "nodeparameter", "��������" },
		{ "nodepath", "���� ������������" },
		{ "nodepathlink", "�������� ���� ������������" },
		{ "nodepathtype", "��� ���� ������������" },
		{ "nodeport", "����" },
		{ "nodeporttype", "��� �����" },
		{ "nodeproto", "������ ��������" },
		{ "noderesourcequery", "������ �� ���������� ������������" },
		{ "noderesult", "���������" },
		{ "noderesultparameter", "�������� ����������" },
		{ "noderesultset", "�������� ������ ���������" },
		{ "nodescheme", "�����" },
		{ "nodeschemecablelink", "�������������� ��������� �����" },
		{ "nodeschemecableport", "�������������� ��������� ����" },
		{ "nodeschemecablethread", "������� �������������� ��������� �����" },
		{ "nodeschemedevice", "����������" },
		{ "nodeschemeelement", "������� �����" },
		{ "nodeschemeelementlink", "" },
		{ "nodeschemelink", "�������������� �����" },
		{ "nodeschemepath", "������� ���� ������������" },
		{ "nodeschemepathlink", "" },
		{ "nodeschemeport", "�������������� ����" },
		{ "nodeserver", "������" },
		{ "nodesystemevent", "�������" },
		{ "nodesystemeventsource", "�������� �������" },
		{ "nodesourceeventtyperule", "������� ��������� �������" },
		{ "nodetest", "����" },
		{ "nodetestargument", "������� �������� �����" },
		{ "nodetestargumentset", "����� ������� ���������� �����" },
		{ "nodetestport", "���� ������������" },
		{ "nodetestporttype", "��� ����� ������������" },
		{ "nodetestrequest", "������ �� ������������" },
		{ "nodetestsetup", "������ ������������" },
		{ "nodetesttimestamp", "��������� ����� �����" },
		{ "nodetesttype", "��� �����" },
		{ "nodethreshold", "�������� ������" },
		{ "nodethresholdset", "����� ���������� ������" },
		{ "nodeuser", "������������ �������" },

		{ "node", "" },
		{ "node", "" },
		{ "node", "" },

		{ "actiontestrequest", "������ �� ������������" },
		{ "actiontest", "����" },
		{ "actionanalysis", "������" },
		{ "actionmodeling", "�������������" },
		{ "actionevaluation", "������" },
		{ "action", "��������" },

		{ "property_name", "��������" },
		{ "property_value", "��������" },
//		{ "property_name", "Property" },
//		{ "property_value", "Value" },

		{ "AppText", "" },

//for filter and LogicScheme
		{ "label_and", "�"},
		{ "label_or", "���"},
		{ "label_condition", "�������"},
		{ "label_operand", "�������"},
		{ "label_result", "���������"},

		{ "label_deleteCriteria", "������� ��������"},
		{ "label_filterCriteria", "��������"},
		{ "label_equality", "���������"},
		{ "label_diapason", "��������"},
		{ "label_time", "�����"},
		{ "label_substring", "���������"},
		{ "label_list", "������"},
		{ "label_editFilterTree", "������������� ������ ����������"},
		{ "label_summaryExpression", "��������� ���������"},

		{ "label_emptyScheme", "����� �����!"},
		{ "label_cantDeleteComp", "������ ������� ���������� \"���������\" � \"�������\"!"},
		{ "label_error", "������"},

		{ "label_change", "��������"},
		{ "label_add", "��������"},
		{ "label_apply", "���������"},
		{ "label_close", "�������"},

		{ "label_filter", "������"},

		{ "label_lswTitle", "�������� ���������� �����"},
		{ "label_delete", "�������"},
		{ "label_createStandartScheme", "��� ���������� ����� ������� ��������� ����������� �����"},
		{ "label_schemeNotCompleted", "����� �� ���������. ������������ �����������."},
		{ "label_time", "�����"},
		{ "label_substring", "���������"},
		{ "label_list", "������"},
		{ "label_editFilterTree", "������������� ������ ����������"},
		{ "label_summaryExpression", "��������� ���������"},

		{ "Error", "������"},
		{ "Aborted", "�������� ��������"},
		{ "Finished", "�������� ���������"},
		{ "Initiating", "������������� ������..."},
		{ "DataLoaded", "������ ���������"},
		{ "NotAllFieldsValid", "���������� ������� ������ - �� ��� ���� ������"},

//for filter and LogicScheme

		};

	public LangModel_ru()
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
