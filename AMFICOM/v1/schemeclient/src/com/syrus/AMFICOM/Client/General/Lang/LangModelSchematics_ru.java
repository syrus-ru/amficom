package com.syrus.AMFICOM.Client.General.Lang;

public class LangModelSchematics_ru extends LangModelSchematics
{

	public Object[][] contents = {

		{ "language", "Русский" },
		{ "country", "Россия" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

		{ "SchemeEditorTitle", "Редактор схем АМФИКОМ"},
		{ "ElementsEditorTitle", "Редактор компонентов АМФИКОМ"},
		{ "characteristicsTitle", "Характеристики"},
		{ "elementsListTitle", "Свойства"},
		{ "elementsMainTitle", "Компонент"},
		{ "elementsUGOTitle", "УГО"},
		{ "treeFrameTitle", "Список компонентов"},
		{ "schemeMainTitle", "Схема"},
		{ "elementsUgoTitle", "Условное обозначение"},

// Menu text
		{ "new", "Новое"},
		{ "delete", "Удалить"},

		{ "menuSchemeText", "Схема"},
		{ "menuSchemeNewText", "Новая"},
		{ "menuSchemeLoadText", "Открыть"},
		{ "menuSchemeSaveText", "Сохранить"},
		{ "menuSchemeSaveAsText", "Сохранить как..."},
		{ "menuInsertToCatalogText", "Связать с каталогом"},
		{ "menuSchemeImportText", "Импорт из файла"},
		{ "menuSchemeExportText", "Экспорт в файл"},

		{ "menuComponentText", "Компонент"},
		{ "menuComponentNewText", "Новый"},
		{ "menuComponentSaveText", "Сохранить"},

		{ "menuPathText", "Маршрут"},
		{ "menuPathNewText", "Новый маршрут"},
		{ "menuPathCancelText", "Отменить"},
		{ "menuPathSaveText", "Сохранить маршрут"},
		{ "menuPathDeleteText", "Удалить маршрут"},
		{ "menuPathEditText", "Редактировать маршрут"},
		{ "menuPathAddStartText", "Задать начало"},
		{ "menuPathAddEndText", "Задать конец"},
		{ "menuPathAddLinkText", "Добавить связь"},
		{ "menuPathRemoveLinkText", "Удалить связь"},
		{ "menuPathUpdateLinkText", "Изменить связь"},

		{ "menuWindowText", "Окно"},
		{ "menuWindowArrangeText", "Упорядочить окна"},
		{ "menuWindowTreeText", "Список компонентов"},
		{ "menuWindowSchemeText", "Схема"},
		{ "menuWindowCatalogText", "Каталог"},
		{ "menuWindowElementsText", "Компонент"},
		{ "menuWindowUgoText", "УГО"},
		{ "menuWindowPropsText", "Характеристики"},
		{ "menuWindowListText", "Свойства"},

// Menu tooltips
		{ "menuSchemeNewToolTip", "Новая схема"},
		{ "menuSchemeLoadToolTip", "Открыть схему"},
		{ "menuSchemeSaveToolTip", "Сохранить схему"},
		{ "menuInsertToCatalogToolTip", "Связать с каталогом"},

		{ "menuComponentNewToolTip", "Новый компонент"},
		{ "menuComponentSaveToolTip", "Сохранить компонент"},

		{ "scheme", "Схема" },
		{ "network", "Схема сети" },
		{ "cablesubnetwork", "Схема кабеля" },
		{ "building", "Схема здания" },
		{ "floor", "Схема этажа" },
		{ "room", "Схема комнаты" },
		{ "rack", "Схема стойки" },
		{ "bay", "Схема полки" },
		{ "cardcage", "Схема шасси" },

// messages

// status bar constants
		{ "statusReady", "Ожидание" },
		{ "statusNoUser", " " },
		};

	public LangModelSchematics_ru()
	{
		symbols.setEras(new String [] {"н.э.","до н.э."});
		symbols.setMonths(new String [] {"январь","февраль", "март",
				"апрель", "май", "июнь", "июль", "август", "сентябрь",
				"октябрь", "ноябрь", "декабрь"});
		symbols.setShortMonths(new String [] {"янв", "фев", "мар", "апр",
				"май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"});
//		symbols.setWeekDays(new String [] {"понедельник", "вторник",
//				"среда", "четверг", "пятница", "суббота", "воскресенье"} );
//		symbols.setShortWeekDays(new String [] {"пн", "вт", "ср", "чт",
//				"пт", "сб", "вс"} );
	}

	public Object[][] getContents()
	{
		return contents;
	}

}
