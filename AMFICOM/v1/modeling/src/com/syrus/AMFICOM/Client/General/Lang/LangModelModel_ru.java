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

public class LangModelModel_ru extends LangModelModel
{
	static final Object[][] contents = {

		{ "language", "Русский" },
		{ "country", "Россия" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

// Menu text
		{ "menuSessionOpenText", "Новая сессия" },
		{ "menuViewText", "Вид" },
		{ "menuViewMapOpenText", "Топологическая схема сети" },
		{ "menuViewMapEditText", "Редактировать топологическую схему" },
		{ "menuViewMapCloseText", "Закрыть топологическую схему" },
		{ "menuViewSchemeOpenText", "Физическая схема сети" },
		{ "menuViewSchemeEditText", "Редактировать физическую схему" },
		{ "menuViewSchemeCloseText", "Закрыть физическую схему" },
		{ "menuViewPerformModelingText", "Выполнить моделирование"},
		{ "menuViewPerformModelingToolTip", "Выполнить моделирование"},
		{ "menuViewModelSaveText", "Сохранить модель" },
		{ "menuViewModelLoadText", "Загрузить" },

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
		{ "menuSessionOpenToolTip", "Новая сессия" },
		{ "menuSessionCloseToolTip", "Закрыть сессию" },
		{ "menuSessionOptionsToolTip", "Параметры сессии" },
		{ "menuSessionConnectionToolTip", "Параметры соединения" },
		{ "menuSessionChangePasswordToolTip", "Изменить пароль" },
		{ "menuSessionSaveToolTip", "Сохранить изменения" },
		{ "menuSessionUndoToolTip", "Отменить изменения" },
		{ "menuExitToolTip", "Выход" },

		{ "menuViewToolTip", "Вид" },
		{ "menuViewNavigatorToolTip", "Навигатор объектов" },
		{ "menuViewMessagesToolTip", "Окно диагностических сообщений" },
		{ "menuViewToolbarToolTip", "Панель инструментов" },
		{ "menuViewRefreshToolTip", "Обновить информацию" },
		{ "menuViewCatalogueToolTip", "Каталог операций" },

		{ "menuViewMapOpenToolTip", "Топология сети" },
		{ "menuViewSchemeOpenToolTip", "Схема сети" },
		{ "menuViewModelLoadToolTip", "Загрузить" },
		{ "menuViewModelSaveToolTip", "Сохранить модель" },

		{ "menuReportText", "Отчет"},
		{ "menuReportCreateText", "Создать отчет"},

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
		{ "AppTitle", "Моделирование АМФИКОМ" },
		{ "elementsMainTitle", "Схема сети" },


// Parameters Table
		{ "ParamsTitle", "Параметры моделирования" },
		{ "parameter", "" },
		{ "value", "" },
		{ "DZAmplitude", "Амплитуда мертвой зоны, дБ" },
		{ "InputLevel", "Уровень вводимого излучения, дБ" },
		{ "AddWeldNumber", "Число дополнительных сварок" },
		{ "WeldLossLevel", "Максимальный уровень потерь на сварке" },
		{ "", "" },
		{ "", "" },
		{ "", "" },
		{ "", "" },

// Button strings
		{ "buttonHelp", "Помощь" },

		{ "buttonCompute", "Пересчитать" },
// Label strings
		{ "labelName", "Имя" },
		{ "labelTabbedFilter", "Фильтр" },
		{ "labelTabbedList", "Список" },
		{ "labelTabbedProperties", "Свойства" },

// Error messages
		{ "errorTitleChangePassword", "Ошибка изменения" },
		{ "errorTitleOpenSession", "Ошибка входа" },

		{ "errorWrongName", "Неверно введено имя пользователя" },
		{ "errorWrongPassword", "Неверно введен пароль" },
		{ "errorWrongPassword2", "Новый пароль второй раз введен ошибочно" },
		{ "errorWrongLogin", "Проверьте имя пользователя и пароль" },

// Info messages
		{ "messageTitle", "information title" },

		{ "message", "information text" },

		{ "AppText", "" }
		};

	public LangModelModel_ru()
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