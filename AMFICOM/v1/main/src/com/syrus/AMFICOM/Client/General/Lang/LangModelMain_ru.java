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

public class LangModelMain_ru extends LangModelMain
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
		{ "menuExitText", "Выход" },

		{ "menuViewText", "Вид" },
		{ "menuViewPanelText", "Панель инструментов" },

		{ "menuToolsText", "Инструментарий" },
		{ "menuToolsAdminText", "Администрирование" },
		{ "menuToolsConfigText", "Конфигурирование" },
		{ "menuToolsComponentsText", "Редактор компонентов" },
		{ "menuToolsSchemeText", "Редактор физических схем" },
		{ "menuToolsMapText", "Редактор топологических схем" },
		{ "menuToolsTraceText", "Проектирование" },
		{ "menuToolsScheduleText", "Планирование" },
		{ "menuToolsSurveyText", "Наблюдение" },
		{ "menuToolsModelText", "Моделирование" },
		{ "menuToolsMonitorText", "Анализ" },
		{ "menuToolsAnalyseText", "Исследование" },
		{ "menuToolsNormsText", "Оценка" },
		{ "menuToolsMaintainText", "Сопровождение" },
		{ "menuToolsPrognosisText", "Прогнозирование" },
		{ "menuToolsReportBuilderText", "Редактор отчетов" },

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
		{ "menuExitToolTip", "Выход" },

		{ "menuViewToolTip", "Вид" },
		{ "menuViewPanelToolTip", "Панель инструментов" },

		{ "menuToolsToolTip", "Инструментарий" },
		{ "menuToolsAdminToolTip", "Администрирование" },
		{ "menuToolsConfigToolTip", "Конфигурирование" },
		{ "menuToolsComponentsToolTip", "Редактор компонентов" },
		{ "menuToolsSchemeToolTip", "Редактор физических схем" },
		{ "menuToolsMapToolTip", "Редактор топологических схем" },
		{ "menuToolsTraceToolTip", "Проектирование" },
		{ "menuToolsScheduleToolTip", "Планирование" },
		{ "menuToolsSurveyToolTip", "Наблюдение" },
		{ "menuToolsModelToolTip", "Моделирование" },
		{ "menuToolsMonitorToolTip", "Анализ" },
		{ "menuToolsAnalyseToolTip", "Исследование" },
		{ "menuToolsNormsToolTip", "Оценка" },
		{ "menuToolsMaintainToolTip", "Сопровождение" },
		{ "menuToolsPrognosisToolTip", "Прогнозирование" },
		{ "menuToolsReportBuilderToolTip", "Редактор отчетов" },

		{ "menuHelpToolTip", "Помощь" },
		{ "menuHelpContentsToolTip", "Содержание" },
		{ "menuHelpFindToolTip", "Поиск темы" },
		{ "menuHelpTipsToolTip", "Контекстные подсказки" },
		{ "menuHelpStartToolTip", "Знакомство с системой" },
		{ "menuHelpCourseToolTip", "Курс подготовки" },
		{ "menuHelpHelpToolTip", "Использование помощи" },
		{ "menuHelpSupportToolTip", "Служба поддержки" },
		{ "menuHelpLicenseToolTip", "Лицензия" },
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

		{ "statusRunningAdmin", "Запуск Admin" },
		{ "statusRunningConfig", "Запуск Config" },
		{ "statusRunningTrace", "Запуск Trace" },
		{ "statusRunningSurvey", "Запуск Survey" },
		{ "statusRunningAnalyse", "Запуск Analyse" },
		{ "statusRunningNorms", "Запуск Norms" },

		{ "statusNoConnection", "Нет соединения" },
		{ "statusConnectionError", "Ошибка соединения!" },

		{ "statusNoSession", "Нет сессии" },

		{ "statusNoUser", " " },
		{ "statusNoSession", "Нет сессии" },

// Windows titles
		{ "AppTitle", "Оператор АМФИКОМ" },
		{ "ChangePasswordTitle", "Изменение пароля" },
		{ "SessionInfoTitle", "Параметры сессии" },
		{ "ConnectionTitle", "Параметры соединения с сервером" },
		{ "SessionOpenTitle", "Открыть сессию" },

// Button strings
		{ "buttonHelp", "Помощь" },
		{ "buttonAccept", "Принять" },
		{ "buttonCancel", "Отменить" },
		{ "buttonChange", "Изменить" },
		{ "buttonClose", "Закрыть" },
		{ "buttonEnter", "Войти в систему" },
		{ "buttonStandard", "Стандартные" },

// Label strings
		{ "labelName", "Имя" },
		{ "labelPassword", "Пароль" },
		{ "labelOldPassword", "Старый пароль" },
		{ "labelNewPassword", "Новый пароль" },
		{ "labelNewPassword2", "Новый пароль *" },
		{ "labelCategory", "Категория" },

		{ "labelServerObject", "Серверный объект" },
		{ "labelServerIP", "IP-адрес сервера" },
		{ "labelObjectPassword", "Пароль доступа к объекту" },
		{ "labelObjectName", "Имя доступа к объекту" },
		{ "labelServerTCP", "TCP-порт" },
		{ "labelSID", "SID" },

		{ "labelServer", "Сервер" },
		{ "labelUser", "Пользователь" },
		{ "labelCategory", "Категория" },
		{ "labelSessionStart", "Начало сессии" },
		{ "labelSessionTotal", "Общее время сессии" },
		{ "labelServerConnectPeriod", "Период соединения с сервером" },
		{ "labelServerConnectLast", "Время последнего соединения с сервером" },

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

	public LangModelMain_ru()
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