//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: модуль строковых констант на русском языке для основного   * //
// *         окна клиентской части ПО АМФИКОМ                             * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelMain_ru.java                     * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Lang;

import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.text.DateFormatSymbols;

public class LangModelOptimize_ru extends LangModelOptimize
{
	static final Object[][] contents = {

		{ "language", "Русский" },
		{ "country", "Россия" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

// Menu text
		{ "menuSessionText", "Сессия" },
		{ "menuSessionNewText", "Новая сессия" },
		{ "menuSessionCloseText", "Закрыть сессию" },
		{ "menuSessionOptionsText", "Параметры сессии" },
		{ "menuSessionConnectionText", "Параметры соединения" },
		{ "menuSessionChangePasswordText", "Изменить пароль" },
		{ "menuSessionSaveText", "Сохранить изменения" },
		{ "menuSessionUndoText", "Отменить изменения" },
		{ "menuExitText", "Выход" },

		{ "menuSchemeText", "Схема" },
		{ "menuMapOpenText", "Открыть топологическую схему сети" },
		{ "menuSchemeOpenText", "Открыть физическую схему сети" },
		{ "menuSchemeSaveText", "Сохранить СМ в схему" },
    { "menuSchemeSaveAsText", "Сохранить СМ отдельно"},
    { "menuLoadSmText", "Загрузить СМ"}, // открыть схему мониторнга ( Solution )
    { "menuClearSchemeText", "Очистить схему от СМ"},

		{ "menuSchemeCloseText", "Закрыть" },

		{ "menuViewText", "Вид" },
		{ "menuViewMapText", "Топологическая схема сети" },
		{ "menuViewSchemeText", "Физическая схема сети" },
		{ "menuViewMapElPropertiesText", "Свойства элемента топологической схемы" },
    { "menuViewSchElPropertiesText", "Свойства элемента физической схемы" },
		{ "menuViewGraphText", "График хода оптимизации" },
		{ "menuViewSolutionText", "Детализация решения" },
		{ "menuViewKISText", "Стоимость КИС" },
		{ "menuViewModeText", "Режим узлов и волокон" },
		{ "menuViewParamsText", "Параметры оптимизации" },
		{ "menuViewShowallText", "Разместить окна" },

		{ "menuOptimizeText", "Оптимизация" },
    { "menuOptimizeModeText", "Режим" },
    { "menuOptimizeModeUnidirectText", "Одностороннее" },
    { "menuOptimizeModeBidirectText", "Встречное" },
    { "menuOptimizeCriteriaPriceText", "Стоимость" },
    { "menuOptimizeCriteriaPriceLoadText", "Открыть" },
    { "menuOptimizeCriteriaPriceSaveText", "Сохранить" },
    { "menuOptimizeCriteriaPriceSaveasText", "Сохранить как..." },
    { "menuOptimizeCriteriaPriceCloseText", "Закрыть" },
    { "menuOptimizeCriteriaText", "Критерии" },
    { "menuOptimizeStartText", "Старт" },
		{ "menuOptimizeStopText", "Стоп" },

		{ "menuHelpText", "Помощь" },
		{ "menuHelpContentsText", "Содержание" },
		{ "menuHelpFindText", "Поиск темы" },
		{ "menuHelpTipsText", "Контекстные подсказки" },
		{ "menuHelpStartText", "Знакомство с системой" },
		{ "menuHelpCourseText", "Курс подготовки" },
		{ "menuHelpHelpText", "Использование помощи" },
		{ "menuHelpSupportText", "Служба поддержки" },
		{ "menuHelpLicenseText", "Лицензия" },
		{ "menuHelpAboutText", "О программе" },

// Menu tooltips
		{ "menuSessionToolTip", "Сессия" },
		{ "menuSessionNewToolTip", "Новая сессия" },
		{ "menuSessionCloseToolTip", "Закрыть сессию" },
		{ "menuSessionOptionsToolTip", "Параметры сессии" },
		{ "menuSessionConnectionToolTip", "Параметры соединения" },
		{ "menuSessionChangePasswordToolTip", "Изменить пароль" },
		{ "menuSessionSaveToolTip", "Сохранить изменения" },
		{ "menuSessionUndoToolTip", "Отменить изменения" },
		{ "menuExitToolTip", "Выход" },

		{ "menuSchemeToolTip", "Схема" },
		{ "menuSchemeOpenToolTip", "Открыть физическую схему сети" },
    { "menuMapOpenToolTip", "Открыть топологическую схему сети" },
		{ "menuSchemeSaveToolTip", "Сохранить схему" },
    { "menuSchemeSaveAsToolTip", "Сохранить решение" },
    { "menuLoadSmToolTip", "Загрузить решение" },
    { "menuClearSchemeToolTip", "Очистить схему от СМ" },
		{ "menuSchemeCloseToolTip", "Закрыть" },

		{ "menuViewToolTip", "Вид" },
		{ "menuViewMapToolTip", "Топологическая схема сети" },
		{ "menuViewSchemeToolTip", "Физическая схема сети" },
		{ "menuViewMapElPropertiesToolTip", "Свойства элемента топологической схемы" },
    { "menuViewSchElPropertiesToolTip", "Свойства элемента физической схемы" },
		{ "menuViewSolutionToolTip", "Детализация решения" },
		{ "menuViewKISToolTip", "Стоимость КИС" },
		{ "menuViewModeToolTip", "Режим узлов и волокон" },
		{ "menuViewParamsToolTip", "Параметры оптимизации" },
		{ "menuViewShowallToolTip", "Разместить окна" },

		{ "menuOptimizeToolTip", "Оптимизация" },
    { "menuOptimizeCriteriaToolTip", "Критерии оптимизации" },
    { "menuOptimizeModeToolTip", "Режим оптимизации" },
    { "menuOptimizeModeUnidirectToolTip", "Режим одностороннего тестирования" },
    { "menuOptimizeModeBidirectToolTip", "Режим встречного тестирования" },
    { "menuOptimizeCriteriaPriceToolTip", "Стоимость" },
    { "menuOptimizeCriteriaPriceLoadToolTip", "Открыть файл цен" },
    { "menuOptimizeCriteriaPriceSaveToolTip", "Сохранить файл цен" },
    { "menuOptimizeCriteriaPriceSaveasToolTip", "Сохранить файл цен как..." },
    { "menuOptimizeCriteriaPriceCloseToolTip", "Закрыть файл цен" },
		{ "menuOptimizeStartToolTip", "Старт" },
    { "menuOptimizeStopToolTip", "Стоп" },

		{ "menuHelpToolTip", "Помощь" },
		{ "menuHelpContentsToolTip", "Содержание" },
		{ "menuHelpFindToolTip", "Поиск темы" },
		{ "menuHelpTipsToolTip", "Контекстные подсказки" },
		{ "menuHelpStartToolTip", "Знакомство с системой" },
		{ "menuHelpCourseToolTip", "Курс подготовки" },
		{ "menuHelpHelpToolTip", "Использование помощи" },
		{ "menuHelpSupportToolTip", "Служба поддержки" },
		{ "menuHelpLicenseTooltip", "Лицензия" },
		{ "menuHelpAboutToolTip", "О программе" },

// Status Strings
		{ "statusReady", "Ожидание" },
		{ "statusConnecting", "Соединяюсь" },
		{ "statusError", "Ошибка" },
		{ "statusSettingSession", "Сессия..." },
		{ "statusSettingConnection", "Соединение..." },
		{ "statusCancelled", "Отмена" },
		{ "statusOpeningSession", "Открываю сессию" },
		{ "statusClosingSession", "Закрываю сессию" },
		{ "statusChangingPassword", "Пароль..." },
		{ "statusDisconnecting", "Отсоединяюсь" },
		{ "statusDisconnected", "Отсоединился" },
		{ "statusExiting", "Выход" },

		{ "statusNoConnection", "Нет соединения" },
		{ "statusConnectionError", "Ошибка соединения!" },

		{ "statusNoSession", "Нет сессии" },

		{ "statusNoUser", " " },
		{ "statusNoSession", "Нет сессии" },

// Windows titles
		{ "AppTitle", "Проектирование АМФИКОМ" },

// Button strings
		{ "buttonHelp", "Помощь" },

// Label strings
		{ "labelName", "Имя" },

// Error messages
		{ "errorTitleChangePassword", "Ошибка изменения" },
		{ "errorTitleOpenSession", "Ошибка входа" },

		{ "errorWrongName", "Неверно введено имя пользователя" },
		{ "errorWrongPassword", "Неверно введен пароль" },
		{ "errorWrongPassword2", "Новый пароль второй раз введен ошибочно" },
		{ "errorWrongLogin", "Проверьте имя пользователя и пароль" },

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