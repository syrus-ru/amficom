package com.syrus.AMFICOM.Client.General.Lang;

import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.text.DateFormatSymbols;

public class LangModelMap_ru extends LangModelMap
{
	public Object[][] contents = {

		{ "language", "Русский" },
		{ "country", "Россия" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

// Menu text
		{ "menuMapText", "Топологическая схема" },
		{ "menuMapNewText", "Новая"},
		{ "menuMapOpenText", "Управление схемами" },
		{ "menuMapCloseText", "Закрыть" },
		{ "menuMapSaveText", "Сохранить" },
		{ "menuMapSaveAsText", "Сохранить как..." },
		{ "menuMapOptionsText", "Настройки" },

		{ "menuEditText", "Правка" },
		{ "menuEditUndoText", "Отменить" },
		{ "menuEditRedoText", "Повторить" },
		{ "menuEditCutText", "Вырезать" },
		{ "menuEditCopyText", "Копировать" },
		{ "menuEditPasteText", "Вставить" },
		{ "menuEditDeleteText", "Удалить" },
		{ "menuEditSelectAllText", "Выделить все" },
		{ "menuEditSelectText", "Выделить..." },

		{ "menuViewText", "Вид" },
		{ "menuViewNavigatorText", "Навигатор объектов" },
		{ "menuViewToolbarText", "Панель инструментов" },
		{ "menuViewRefreshText", "Обновить" },
		{ "menuViewPointsText", "Узлы" },
		{ "menuViewMetricsText", "Метки" },
		{ "menuViewPanelText", "Панель оборудования..." },
		{ "menuViewAllText", "Разместить окна" },

		{ "menuNavigateText", "Навигация" },
		{ "menuNavigateLeftText", "Сдвинуть влево" },
		{ "menuNavigateRightText", "Сдвинуть вправо" },
		{ "menuNavigateUpText", "Сдвинуть вверх" },
		{ "menuNavigateDownText", "Сдвинуть вних" },
		{ "menuNavigateZoomInText", "Приблизить" },
		{ "menuNavigateZoomOutText", "Отдалить" },
		{ "menuNavigateZoomBoxText", "Приблизить область" },
		{ "menuNavigateZoomSelectionText", "Приблизить выбранное" },
		{ "menuNavigateZoomMapText", "Приблизить центр карты" },
		{ "menuNavigateCenterPointText", "Центрировать" },
		{ "menuNavigateCenterSelectionText", "Центрировать выбранное" },
		{ "menuNavigateCenterMapText", "Центрировать карту" },

		{ "menuElementText", "Элемент" },
		{ "menuElementCatalogueText", "Каталог элементов" },
		{ "menuElementGroupText", "Сгруппировать" },
		{ "menuElementUngroupText", "Разгруппировать" },
		{ "menuElementAlignText", "Выровнять" },
		{ "menuElementPropertiesText", "Свойства" },

		{ "menuHelpText", "Помощь" },
		{ "menuHelpContentsText", "Содержание" },
		{ "menuHelpFindText", "Поиск темы" },
		{ "menuHelpTipsText", "Контекстные подсказки" },
		{ "menuHelpCourseText", "Курс подготовки" },
		{ "menuHelpHelpText", "Использование помощи" },
		{ "menuHelpAboutText", "О программе" },

		{ "EquipmentElementPopupMenuDeleteText", "Удалить" },
		{ "EquipmentElementPopupMenuChooseText", "Выбрать" },
		{ "EquipmentElementPopupMenuCopyText", "Копировать" },
		{ "EquipmentElementPopupMenuElementText", "Открыть схему элемента" },
		{ "EquipmentElementPopupMenuSchemeText", "Открыть схему нижнего уровня" },
		{ "EquipmentElementPopupMenuPropertiesText", "Свойства" },

		{ "KISElementPopupMenuDeleteText", "Удалить" },
		{ "KISElementPopupMenuChooseText", "Выбрать" },
		{ "KISElementPopupMenuCopyText", "Копировать" },
		{ "KISElementPopupMenuPropertiesText", "Свойства" },

		{ "NodeLinkPopupMenuChooseText", "Выбрать" },
		{ "NodeLinkPopupMenuDeleteText", "Удалить" },
		{ "NodeLinkPopupMenuLookText", "Посмотреть на схеме" },
		{ "NodeLinkPopupMenuPropertiesText", "Свойства" },

		{ "TransmissionPathPopupMenuChooseText", "Выбрать" },
		{ "TransmissionPathPopupMenuDeleteText", "Удалить" },
		{ "TransmissionPathPopupMenuAddText", "Добавить маркер" },
		{ "TransmissionPathPopupMenuPropertiesText", "Свойства" },

		{ "PhysicalLinkPopupMenuAddMarkText", "Добавить метку" },
		{ "PhysicalLinkPopupMenuChooseText", "Выбрать" },
		{ "PhysicalLinkPopupMenuDeleteText", "Удалить" },
		{ "PhysicalLinkPopupMenuLookText", "Посмотреть на схеме" },
		{ "PhysicalLinkPopupMenuCreatetransmisionPathText", "Создать путь тестирования" },
		{ "PhysicalLinkPopupMenuPropertiesText", "Свойства" },
		{ "PhysicalLinkPopupMenuShowtransmisionPathText", "Показать путь тестирования" },

		{ "VoidPopupMenuDeleteText", "Удалить" },
		{ "VoidPopupMenuChooseText", "Выбрать" },
		{ "VoidPopupMenuCopyText", "Копировать" },
		{ "VoidPopupMenuPropertiesText", "Свойства" },

		{ "PhysicalNodeElementPopupMenuDeleteText", "Удалить" },
		{ "PhysicalNodeElementPopupMenuChooseText", "Выбрать" },
		{ "PhysicalNodeElementPopupMenuCopyText", "Копировать" },
		{ "PhysicalNodeElementPopupMenuPropertiesText", "Свойства" },

// Tooltips
		{ "menuMapToolTip", "Топологическая схема" },
		{ "menuMapNewToolTip", "Новая топологическая схема" },
		{ "menuMapOpenToolTip", "Управление топологическими схемами" },
		{ "menuMapCloseToolTip", "Закрыть топологическую схему" },
		{ "menuMapSaveToolTip", "Сохранить топологическую схему" },
		{ "menuMapSaveAsToolTip", "Сохранить схему с новым именем" },
		{ "menuExitToolTip", "Выйти" },

		{ "menuEditToolTip", "Правка" },
		{ "menuEditUndoToolTip", "Отменить" },
		{ "menuEditRedoToolTip", "Повторить" },
		{ "menuEditCutToolTip", "Вырезать" },
		{ "menuEditCopyToolTip", "Копировать" },
		{ "menuEditPasteToolTip", "Вставить" },
		{ "menuEditDeleteToolTip", "Удалить" },
		{ "menuEditSelectAllToolTip", "Выделить все" },
		{ "menuEditSelectToolTip", "Выделить..." },

		{ "menuViewToolTip", "Вид" },
		{ "menuViewNavigatorToolTip", "Навигатор объектов" },
		{ "menuViewToolbarToolTip", "Панель инструментов" },
		{ "menuViewRefreshToolTip", "Обновить" },
		{ "menuViewPointsToolTip", "Узлы" },
		{ "menuViewMetricsToolTip", "Метки" },
		{ "menuViewPanelToolTip", "Панель оборудования..." },
		{ "menuViewAllToolTip", "Разместить окна" },

		{ "menuNavigateToolTip", "Навигация" },
		{ "menuNavigateLeftToolTip", "Сдвинуть влево" },
		{ "menuNavigateRightToolTip", "Сдвинуть вправо" },
		{ "menuNavigateUpToolTip", "Сдвинуть вверх" },
		{ "menuNavigateDownToolTip", "Сдвинуть вних" },
		{ "menuNavigateZoomInToolTip", "Приблизить" },
		{ "menuNavigateZoomOutToolTip", "Отдалить" },
		{ "menuNavigateZoomBoxToolTip", "Приблизить область" },
		{ "menuNavigateZoomSelectionToolTip", "Приблизить выбранное" },
		{ "menuNavigateZoomMapToolTip", "Приблизить центр карты" },
		{ "menuNavigateCenterPointToolTip", "Центрировать" },
		{ "menuNavigateCenterSelectionToolTip", "Центрировать выбранное" },
		{ "menuNavigateCenterMapToolTip", "Центрировать карту" },

		{ "menuElementToolTip", "Элемент" },
		{ "menuElementCatalogueToolTip", "Каталог элементов" },
		{ "menuElementGroupToolTip", "Сгруппировать" },
		{ "menuElementUngroupToolTip", "Разгруппировать" },
		{ "menuElementAlignToolTip", "Выровнять" },
		{ "menuElementPropertiesToolTip", "Свойства" },

		{ "menuHelpToolTip", "Помощь" },
		{ "menuHelpContentsToolTip", "Содержание" },
		{ "menuHelpFindToolTip", "Поиск темы" },
		{ "menuHelpTipsToolTip", "Контекстные подсказки" },
		{ "menuHelpCourseToolTip", "Курс подготовки" },
		{ "menuHelpHelpToolTip", "Использование помощи" },
		{ "menuHelpAboutToolTip", "О программе" },

		{ "AppTitle", "Топологическая схема" },
		{ "propertiesTitle", "Свойства" },
		{ "elementsTitle", "Объекты" },
		{ "end", "" }
		};

	public LangModelMap_ru()
	{
		symbols.setEras(new String [] {"н.э.","до н.э."});
		symbols.setMonths(new String [] {"январь","февраль", "март",
				"апрель", "май", "июнь", "июль", "август", "сентябрь",
				"октябрь", "ноябрь", "декабрь"});
		symbols.setShortMonths(new String [] {"янв", "фев", "мар", "апр",
				"май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"});

	}

	public Object[][] getContents()
	{
		return contents;
	}
}

