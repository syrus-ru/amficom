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

public class LangModelSurvey_ru extends LangModelSurvey
{
	static final Object[][] contents = {

		{ "language", "Русский" },
		{ "country", "Россия" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

// Menu text
		{ "menuViewText", "Вид" },
		{ "menuViewNavigatorText", "Навигатор объектов" },
		{ "menuViewMessagesText", "Окно диагностических сообщений" },
		{ "menuViewToolbarText", "Панель инструментов" },
		{ "menuViewRefreshText", "Обновить информацию" },
		{ "menuViewCatalogueText", "Каталог операций" },
		{ "menuViewMapSetupText", "Операции на карте" },

		{ "menuEvaluateText", "Операция" },
		{ "menuEvaluateRequestText", "Постановка задачи" },
		{ "menuEvaluateSchedulerText", "Планирование" },
		{ "menuEvaluateArchiveText", "Архив измерений" },
		{ "menuEvaluateTrackText", "Контроль исполнения" },
		{ "menuEvaluateTrackRequestText", "Запросы" },
		{ "menuEvaluateTrackTaskText", "Задания" },
		{ "menuEvaluateResultText", "Обзор результатов" },
		{ "menuEvaluateAnalizeText", "Анализ" },
		{ "menuEvaluateAnalizeExtText", "Исследование" },
		{ "menuEvaluateNormsText", "Оценка" },
		{ "menuEvaluatePrognosisText", "Прогнозирование" },
		{ "menuEvaluateModelingText", "Моделирование" },
		{ "menuEvaluateViewAllText", "Открыть всё" },

		{ "menuVisualizeText", "Вид" },
		{ "menuVisualizeNetGISText", "Топологическая схема сети" },
		{ "menuVisualizeNetText", "Схема сети" },
		{ "menuVisualizeISMText", "Схема СМ" },
		{ "menuVisualizeISMGISText", "Топологическая схема СМ" },
		{ "menuVisualizeMapEditText", "Редактор топологии" },
		{ "menuVisualizeMapCloseText", "Закрыть топологическую схему" },
		{ "menuVisualizeSchemeEditText", "Редактор схем" },
		{ "menuVisualizeSchemeCloseText", "Закрыть схему" },
		{ "menuViewAllText", "Разместить окна" },

		{ "menuMaintainText", "Сопровождение" },
		{ "menuMaintainAlarmText", "Сигналы тревоги" },
		{ "menuMaintainAlertText", "Сообщения" },
		{ "menuMaintainCallText", "Заявки" },
		{ "menuMaintainEventText", "События" },

		{ "menuReportText", "Отчет" },
		{ "menuReportTableText", "Табличная форма" },
		{ "menuReportHistogrammText", "Гистограмма" },
		{ "menuReportGraphText", "График" },
		{ "menuReportComplexText", "Сложный отчет" },
		{ "menuReportReportText", "Отчеты" },

		{ "menuToolsText", "Инструменты" },
		{ "menuToolsSortText", "Сортировка" },
		{ "menuToolsSortNewText", "Новая сортировка" },
		{ "menuToolsSortSaveText", "Сохранить сортировку" },
		{ "menuToolsSortOpenText", "Открыть сортировку" },
		{ "menuToolsSortListText", "Сортировки" },
		{ "menuToolsFilterText", "Фильтр" },
		{ "menuToolsFilterNewText", "Новый фильтр" },
		{ "menuToolsFilterSaveText", "Сохранить фильтр" },
		{ "menuToolsFilterOpenText", "Загрузить фильтр" },
		{ "menuToolsFilterListText", "Фильтры" },
		{ "menuToolsFindText", "Поиск" },
		{ "menuToolsFindFastText", "Быстрый поиск" },
		{ "menuToolsFindWordText", "Поиск по слову" },
		{ "menuToolsFindFieldText", "Поиск по полям" },
		{ "menuToolsFindNextText", "Найти дальше" },
		{ "menuToolsFindQueryText", "Сложный поиск" },
		{ "menuToolsBookmarkText", "Закладки" },
		{ "menuToolsBookmarkSetText", "Поставить закладку" },
		{ "menuToolsBookmarkGotoText", "Перейти к закладке" },
		{ "menuToolsBookmarkListText", "Список закладок" },
		{ "menuToolsBookmarkRemoveText", "Удалить закладку" },
		{ "menuToolsBookmarkEditText", "Редактировать закладку" },
		{ "menuToolsDictionaryText", "Словарь" },
		{ "menuToolsLanguageText", "Язык" },
		{ "menuToolsLockText", "Запереть приложение" },
		{ "menuToolsStyleText", "Стиль" },
		{ "menuToolsStyleTextText", "Текст" },
		{ "menuToolsStyleGraphText", "Графика" },
		{ "menuToolsStyleLineText", "Линия" },
		{ "menuToolsStyleTableText", "Таблица" },
		{ "menuToolsStyleSchemeText", "Схема" },
		{ "menuToolsStyleMapText", "Карта" },
		{ "menuToolsStyleSoundText", "Звук" },
		{ "menuToolsStyleColorText", "Цвет" },
		{ "menuToolsStyleLinkText", "Связь" },
		{ "menuToolsOptionsText", "Настройки" },

		{ "menuWindowText", "Окно" },
		{ "menuWindowCloseText", "Закрыть" },
		{ "menuWindowCloseAllText", "Закрыть все" },
		{ "menuWindowTileHorizontalText", "Упорядочить по горизонтали" },
		{ "menuWindowTileVerticalText", "Упорядочить по вертикали" },
		{ "menuWindowCascadeText", "Каскадировать" },
		{ "menuWindowArrangeText", "Упорядочить" },
		{ "menuWindowArrangeIconsText", "Упорядочить иконки" },
		{ "menuWindowMinimizeAllText", "Свернуть все" },
		{ "menuWindowRestoreAllText", "Восстановить все" },
		{ "menuWindowListText", "Список" },

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

		{ "menuViewToolTip", "Вид" },
		{ "menuViewNavigatorToolTip", "Навигатор объектов" },
		{ "menuViewMessagesToolTip", "Окно диагностических сообщений" },
		{ "menuViewToolbarToolTip", "Панель инструментов" },
		{ "menuViewRefreshToolTip", "Обновить информацию" },
		{ "menuViewCatalogueToolTip", "Каталог операций" },
		{ "menuViewMapSetupToolTip", "Операции на карте" },

		{ "menuEvaluateToolTip", "Операция" },
		{ "menuEvaluateRequestToolTip", "Постановка задачи" },
		{ "menuEvaluateSchedulerToolTip", "Планировщик" },
		{ "menuEvaluateArchiveToolTip", "Архив измерений" },
		{ "menuEvaluateTrackToolTip", "Контроль исполнения" },
		{ "menuEvaluateTrackRequestToolTip", "Запросы" },
		{ "menuEvaluateTrackTaskToolTip", "Задания" },
		{ "menuEvaluateResultToolTip", "Обзор результатов" },
		{ "menuEvaluateAnalizeToolTip", "Анализ" },
		{ "menuEvaluateAnalizeExtToolTip", "Исследование" },
		{ "menuEvaluateNormsToolTip", "Оценка" },
		{ "menuEvaluatePrognosisToolTip", "Прогнозирование" },
		{ "menuEvaluateModelingToolTip", "Моделирование" },
		{ "menuEvaluateViewAllToolTip", "Открыть всё" },

		{ "menuVisualizeToolTip", "Визуализация" },
		{ "menuVisualizeNetGISToolTip", "Топология сети" },
		{ "menuVisualizeNetToolTip", "Схема сети" },
		{ "menuVisualizeISMToolTip", "Схема ИСМ" },
		{ "menuVisualizeISMGISToolTip", "Топология ИСМ" },
		{ "menuViewAllToolTip", "Разместить окна" },

		{ "menuMaintainToolTip", "Сопровождение" },
		{ "menuMaintainAlarmToolTip", "Сигналы тревоги" },
		{ "menuMaintainAlertToolTip", "Сообщения" },
		{ "menuMaintainCallToolTip", "Заявки" },

		{ "menuReportToolTip", "Отчет" },
		{ "menuReportTableToolTip", "Табличная форма" },
		{ "menuReportHistogrammToolTip", "Гистограмма" },
		{ "menuReportGraphToolTip", "График" },
		{ "menuReportComplexToolTip", "Сложный отчет" },
		{ "menuReportReportToolTip", "Отчеты" },

		{ "menuToolsToolTip", "Инструменты" },
		{ "menuToolsSortToolTip", "Сортировка" },
		{ "menuToolsSortNewToolTip", "Новая сортировка" },
		{ "menuToolsSortSaveToolTip", "Сохранить сортировку" },
		{ "menuToolsSortOpenToolTip", "Открыть сортировку" },
		{ "menuToolsSortListToolTip", "Сортировки" },
		{ "menuToolsFilterToolTip", "Фильтр" },
		{ "menuToolsFilterNewToolTip", "Новый фильтр" },
		{ "menuToolsFilterSaveToolTip", "Сохранить фильтр" },
		{ "menuToolsFilterOpenToolTip", "Загрузить фильтр" },
		{ "menuToolsFilterListToolTip", "Фильтры" },
		{ "menuToolsFindToolTip", "Поиск" },
		{ "menuToolsFindFastToolTip", "Быстрый поиск" },
		{ "menuToolsFindWordToolTip", "Поиск по слову" },
		{ "menuToolsFindFieldToolTip", "Поиск по полям" },
		{ "menuToolsFindNextToolTip", "Найти дальше" },
		{ "menuToolsFindQueryToolTip", "Сложный поиск" },
		{ "menuToolsBookmarkToolTip", "Закладки" },
		{ "menuToolsBookmarkSetToolTip", "Поставить закладку" },
		{ "menuToolsBookmarkGotoToolTip", "Перейти к закладке" },
		{ "menuToolsBookmarkListToolTip", "Список закладок" },
		{ "menuToolsBookmarkRemoveToolTip", "Удалить закладку" },
		{ "menuToolsBookmarkEditToolTip", "Редактировать закладку" },
		{ "menuToolsDictionaryToolTip", "Словарь" },
		{ "menuToolsLanguageToolTip", "Язык" },
		{ "menuToolsLockToolTip", "Запереть приложение" },
		{ "menuToolsStyleToolTip", "Стиль" },
		{ "menuToolsStyleToolTipToolTip", "Текст" },
		{ "menuToolsStyleGraphToolTip", "Графика" },
		{ "menuToolsStyleLineToolTip", "Линия" },
		{ "menuToolsStyleTableToolTip", "Таблица" },
		{ "menuToolsStyleSchemeToolTip", "Схема" },
		{ "menuToolsStyleMapToolTip", "Карта" },
		{ "menuToolsStyleSoundToolTip", "Звук" },
		{ "menuToolsStyleColorToolTip", "Цвет" },
		{ "menuToolsStyleLinkToolTip", "Связь" },
		{ "menuToolsOptionsToolTip", "Настройки" },

		{ "menuWindowToolTip", "Окно" },
		{ "menuWindowCloseToolTip", "Закрыть" },
		{ "menuWindowCloseAllToolTip", "Закрыть все" },
		{ "menuWindowTileHorizontalToolTip", "Упорядочить по горизонтали" },
		{ "menuWindowTileVerticalToolTip", "Упорядочить по вертикали" },
		{ "menuWindowCascadeToolTip", "Каскадировать" },
		{ "menuWindowArrangeToolTip", "Упорядочить" },
		{ "menuWindowArrangeIconsToolTip", "Упорядочить иконки" },
		{ "menuWindowMinimizeAllToolTip", "Свернуть все" },
		{ "menuWindowRestoreAllToolTip", "Восстановить все" },
		{ "menuWindowListToolTip", "Список" },

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
		{ "AppTitle", "Наблюдение АМФИКОМ" },
		{ "ResultFrameTitle", "Результаты" },
		{ "titleObjectsCatalog", "Операции пользователя" },
		{ "AlarmTitle", "Сигналы тревоги" },

// Button strings
		{ "buttonHelp", "Помощь" },

////////////////////////////////////////////////////////////////////////////////
// Alarm strings (now filter also uses them - instead of "label...")
    { "label_alarm","Отчёт по сигналам тревоги"},
		{ "alarm_Source", "Источник события" },
		{ "alarm_Type", "Тип события" },
		{ "alarm_Status", "Состояние" },
		{ "alarm_Time", "Время появления события" },
		{ "alarm_Monitoredelement", "Исследуемый объект" },

		{"alarm_Assigned", "Начало исполнения"},
    {"alarm_Assigned_to", "Ответственный исполнитель"},

    {"alarm_Fixed_when","Окончание работ"},
    {"alarm_Fixed_by","Исполнитель"},
    {"alarm_Comments","Комментарии"},
// Alarm

//Equipment
    { "label_equipment","Характеристики оборудования"},

    { "equip_name", "Название" },
    { "equip_type", "Тип" },
    { "equip_domen", "Домен" },
    { "equip_longitude", "Долгота" },
    { "equip_latitude", "Широта" },
    { "equip_hSN", "Сер-й номер оборуд-я" },
    { "equip_sSN", "Сер-й номер ПО" },
    { "equip_hVer", "Текущая версия оборуд-я" },
    { "equip_sVer", "Текущая версия ПО" },
    { "equip_regN", "Регистрационный номер" },
    { "equip_manufacter", "Производитель" },
    { "equip_manCode", "Код производителя" },
    { "equip_supplier", "Поставщик" },
    { "equip_supCode", "Код поставщика" },

//Equipment

//Port
    { "label_ports","Порты"},

    { "port_name", "Название" },
    { "port_description", "Описание" },
    { "port_type_id", "Тип" },

    { "port_equipment_id", "Оборудование" },
    { "port_interface_id", "Интерфейс" },
    { "port_address_id", "Адрес" },
    { "port_domain_id", "Домен" },

//    { "port_optical_chars", "Текущая версия ПО" },
//    { "port_electrical_chars", "Регистрационный номер" },
//    { "port_interface_chars", "Производитель" },
//    { "port_expluatation_chars", "Код производителя" },
//    public Hashtable characteristics;
//Port
    {"menuReportBuilderToolTip","Открыть редактор шаблонов"},
    {"menuAlarmAlertToolTip","Открыть маршрутизатор сигналов тревоги"},

///////////////////////////////////////////////////////////////////////////////////

// Filter label strings
		{ "labelsource", "Источник" },
		{ "labelAlarmType", "Тип" },
		{ "labelStatus", "Статус" },
		{ "labelalarmtime", "Время появления" },
		{ "labelmonitoredelement", "Исследуемый объект" },

// Label strings
		{ "ArgParameter", "Входной параметр" },
		{ "ArgValue", "Значение" },
		{ "ResParameter", "Выходной параметр" },
		{ "ResValue", "Значение" },
		{ "ResultTabTitle", "Параметры" },
		{ "ReflectogramTabTitle", "Рефлектограмма" },

		{ "labelRoot", "Корень" },
		{ "labelDoing", "Выполняется" },
		{ "labelDone", "Выполнен" },
		{ "labelReadyToDo", "Готов к выполнению" },
		{ "labelOnetime", "Одноразовый" },
		{ "labelPeriod", "Периодический" },
		{ "labelTimeTable", "По расписанию" },
		{ "labelStatus", "Статус" },
		{ "labelStartTime", "Время первого теста" },
		{ "labelZapros", "Запрос" },
		{ "labelTimeType", "Временной тип" },
		{ "labelIzmerType", "Тип измерений" },

		{ "labelALARM_STATUS_GENERATED", "Новый" },
		{ "labelALARM_STATUS_ASSIGNED", "Подтвержден" },
		{ "labelALARM_STATUS_FIXED", "Исправлен" },

		{ "labelDoing", "Выполняется" },
		{ "labelDone", "Выполнен" },
		{ "labelReadyToDo", "Готов к выполнению" },

		{ "labelOnetime", "Одноразовый" },
		{ "labelPeriod", "Периодический" },
		{ "labelTimeTable", "По расписанию" },

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

	public LangModelSurvey_ru()
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