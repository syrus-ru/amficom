package com.syrus.AMFICOM.Client.General.Lang;

public class LangModelPrediction_ru extends LangModelPrediction
{
	static final Object[][] contents = {

		{ "language", "�������" },
		{ "country", "������" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

// Menu text
		{ "menuSessionText", "������" },
		{ "menuSessionOpenText", "����� ������" },
		{ "menuSessionOpenToolTip", "����� ������" },
		{ "menuSessionCloseText", "������� ������" },
		{ "menuSessionOptionsText", "��������� ������" },
		{ "menuSessionConnectionText", "��������� ����������" },
		{ "menuSessionChangePasswordText", "�������� ������" },
		{ "menuSessionSaveText", "��������� ���������" },
		{ "menuSessionUndoText", "�������� ���������" },
		{ "menuExitText", "�����" },
		{ "menuSessionDomainText", "����� ������"},
		{ "menuSessionDomainToolTip", "����� ������"},

		{ "menuViewText", "����������" },
		{ "menuViewDataLoadText", "��������� ������"},
		{ "menuViewDataLoadToolTip", "��������� ������"},
		{ "menuViewCountPredictionText", "������� �������"},
		{ "menuViewCountPredictionToolTip", "������� �������������� ��������������"},
		{ "menuViewSavePredictionText", "��������� �������"},
		{ "menuViewSavePredictionToolTip", "��������� �������"},

		{ "menuReportText", "�����"},
		{ "menuReportCreateText", "������� �����"},

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
		{ "menuSessionOpenToolTip", "����� ������" },
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
		{ "AppTitle", "��������������� �������" },
		{ "TimedTableTitle", "�������������� ������" },
		{ "TimedGraphTitle", "��������� �������������" },
		{ "StatHistogramTitle", "�������������� �������������" },


// Parameters Table
		{ "attenuation", "������ ���������" },
		{ "loss", "������ ������" },
		{ "reflectance", "������ ���������" },
		{ "amplitude", "������ ���������" },
		{ "power_level", "������ ������ ��������� ���������" },
		{ "show_points", "���������� �����" },
		{ "show_lines", "���������� �����" },
		{ "show_approximation", "���������� ���������������� ������" },

// Button strings
		{ "buttonHelp", "������" },

		{ "buttonCompute", "�����������" },
// Label strings
		{ "labelName", "���" },
		{ "labelTabbedFilter", "������" },
		{ "labelTabbedList", "������" },
		{ "labelTabbedProperties", "��������" },

// Error messages
		{ "errorTitleChangePassword", "������ ���������" },
		{ "errorTitleOpenSession", "������ �����" },

		{ "errorWrongName", "������� ������� ��� ������������" },
		{ "errorWrongPassword", "������� ������ ������" },
		{ "errorWrongPassword2", "����� ������ ������ ��� ������ ��������" },
		{ "errorWrongLogin", "��������� ��� ������������ � ������" },

// Info messages
		{ "messageTitle", "information title" },

		{ "message", "information text" },

		{ "AppText", "" }
		};

	public LangModelPrediction_ru()
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