package com.syrus.AMFICOM.Client.General.Lang;

public class LangModelSchematics_ru extends LangModelSchematics
{

	public Object[][] contents = {

		{ "language", "�������" },
		{ "country", "������" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

		{ "SchemeEditorTitle", "�������� ���� �������"},
		{ "ElementsEditorTitle", "�������� ����������� �������"},
		{ "characteristicsTitle", "��������������"},
		{ "elementsListTitle", "��������"},
		{ "elementsMainTitle", "���������"},
		{ "elementsUGOTitle", "���"},
		{ "treeFrameTitle", "������ �����������"},
		{ "schemeMainTitle", "�����"},
		{ "elementsUgoTitle", "�������� �����������"},

// Menu text
		{ "new", "�����"},
		{ "delete", "�������"},

		{ "menuSchemeText", "�����"},
		{ "menuSchemeNewText", "�����"},
		{ "menuSchemeLoadText", "�������"},
		{ "menuSchemeSaveText", "���������"},
		{ "menuSchemeSaveAsText", "��������� ���..."},
		{ "menuInsertToCatalogText", "������� � ���������"},
		{ "menuSchemeImportText", "������ �� �����"},
		{ "menuSchemeExportText", "������� � ����"},

		{ "menuComponentText", "���������"},
		{ "menuComponentNewText", "�����"},
		{ "menuComponentSaveText", "���������"},

		{ "menuPathText", "�������"},
		{ "menuPathNewText", "����� �������"},
		{ "menuPathCancelText", "��������"},
		{ "menuPathSaveText", "��������� �������"},
		{ "menuPathDeleteText", "������� �������"},
		{ "menuPathEditText", "������������� �������"},
		{ "menuPathAddStartText", "������ ������"},
		{ "menuPathAddEndText", "������ �����"},
		{ "menuPathAddLinkText", "�������� �����"},
		{ "menuPathRemoveLinkText", "������� �����"},
		{ "menuPathUpdateLinkText", "�������� �����"},

		{ "menuWindowText", "����"},
		{ "menuWindowArrangeText", "����������� ����"},
		{ "menuWindowTreeText", "������ �����������"},
		{ "menuWindowSchemeText", "�����"},
		{ "menuWindowCatalogText", "�������"},
		{ "menuWindowElementsText", "���������"},
		{ "menuWindowUgoText", "���"},
		{ "menuWindowPropsText", "��������������"},
		{ "menuWindowListText", "��������"},

// Menu tooltips
		{ "menuSchemeNewToolTip", "����� �����"},
		{ "menuSchemeLoadToolTip", "������� �����"},
		{ "menuSchemeSaveToolTip", "��������� �����"},
		{ "menuInsertToCatalogToolTip", "������� � ���������"},

		{ "menuComponentNewToolTip", "����� ���������"},
		{ "menuComponentSaveToolTip", "��������� ���������"},

		{ "scheme", "�����" },
		{ "network", "����� ����" },
		{ "cablesubnetwork", "����� ������" },
		{ "building", "����� ������" },
		{ "floor", "����� �����" },
		{ "room", "����� �������" },
		{ "rack", "����� ������" },
		{ "bay", "����� �����" },
		{ "cardcage", "����� �����" },

// messages

// status bar constants
		{ "statusReady", "��������" },
		{ "statusNoUser", " " },
		};

	public LangModelSchematics_ru()
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
