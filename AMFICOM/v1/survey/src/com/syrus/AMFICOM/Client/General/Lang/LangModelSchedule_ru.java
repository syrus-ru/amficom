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

public class LangModelSchedule_ru extends LangModelSchedule
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

		{ "menuViewText", "Вид" },
		{ "menuViewPlanText", "План-график тестов" },
		{ "menuViewTableText", "Таблица" },
		{ "menuViewTreeText", "Объекты тестирования" },
		{ "menuViewTimeText", "Временные параметры" },
		{ "menuViewParamText", "Параметры измерения" },
		{ "menuViewSaveText", "Задание на тестирование" },
		{ "menuViewSchemeText", "Физическая схема сети" },
		{ "menuViewMapText", "Топологическая схема сети" },
		{ "menuViewAllText", "Разместить окна" },

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

		{ "menuOmeTimeTest", "Одноразовое тестирование"},
		{ "menuTimeTableTest", "Тестирование по расписанию"},
		{ "menuPeriodicalTest", "Периодическое тестирование"},
		{ "menuAddTest", "Добавить тест"},
		{ "menuRefresh", "Обновить тесты"},
		{ "menuSaveTest", "Сохранить новые тесты"},
		{ "menuKISChoise1", "Один КИС"},
		{ "menuKISChoise2", "Все КИСы"},
		{ "menuOriginalTest", "Обычное тестирование"},
		{ "menuExtendedTest", "Расширенное тестирование"},
		{ "menuFilterTest", "Фильтрация выводимых тестов"},

		{ "menuParametersTesting", "Параметры тестирования"},
		{ "menuOperationsTesting", "Операции с тестами"},
		{ "menuTypeTesting", "Вид тестирования"},

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

		{ "menuViewAllToolTip", "Разместить окна" },

		{ "menuViewToolTip", "Вид" },
		{ "menuViewNavigatorToolTip", "Навигатор объектов" },
		{ "menuViewMessagesToolTip", "Окно диагностических сообщений" },
		{ "menuViewToolbarToolTip", "Панель инструментов" },
		{ "menuViewRefreshToolTip", "Обновить информацию" },
		{ "menuViewCatalogueToolTip", "Каталог операций" },

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
		{ "statusBD", "Подгрузка БД" },
		{ "statusBDFinish", "Подгрузка БД завершена" },
		{ "statusTestRefresh", "Обновление тестов из БД" },
		{ "statusTestRefreshFinish", "Обновление тестов из БД завершено" },
		{ "statusTestSave", "Сохранение новых тестов в БД" },
		{ "statusTestSaveFinish", "Сохранение новых тестов в БД завершено" },
		{ "statusDomain", "Выбор домена" },
		{ "statusDomainFinish", "Выбор домена завершен" },
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
		{ "AppTitle", "Планирование АМФИКОМ" },
		{ "MyPlanTitle", "План-график"},
		{ "MyTableTitle", "Состояние и характеристики тестов"},
		{ "MyTreeTitle", "Объекты тестирования"},
		{ "MyTimeTitle", "Временные параметры теста"},
		{ "MySaveTitle", "Задание теста"},
		{ "MySchemeTitle", "Физическая схема сети"},
		{ "MyMapTitle", "Топологическая схема сети"},
		{ "MyParamTitle", "Измерительные параметры теста"},
		{ "MyExtendedTitle", "Расширенное тестирование"},
		{ "MyFiltrationTitle", "Фильтрация тестов"},

// Button ToolTip strings
		{ "buttonHelpToolTip", "Помощь" },
		{ "OmeTimeTestToolTip", "Одноразовое тестирование"},
		{ "TimeTableTestToolTip", "Тестирование по расписанию"},
		{ "PeriodicalTestToolTip", "Периодическое тестирование"},
		{ "AddTestToolTip", "Добавить тест"},
		{ "RefreshToolTip", "Обновить тесты"},
		{ "DeleteTestToolTip", "Удалить выбранные тесты"},
		{ "SaveTestToolTip", "Сохранить новые тесты"},
		{ "KISChoise1ToolTip", "Один КИС"},
		{ "KISChoise2ToolTip", "Все КИСы"},
		{ "OriginalTestToolTip", "Обычное тестирование"},
		{ "AutomatTestToolTip", "Расширенное тестирование"},
		{ "FilterTestToolTip", "Фильтрация выводимых тестов"},
		{ "LeftMoveToolTip", "Сдвиг влево"},
		{ "RightMoveToolTip", "Сдвиг вправо"},

// Button strings
		{ "Enter", "Ввести"},

// Label strings
		{ "labelName", "Имя" },
		{ "labelTabbedFilter", "Фильтр" },
		{ "labelTabbedList", "Список" },
		{ "labelTabbedProperties", "Свойства" },
		{ "labelBegin", "Начало" },
		{ "labelInterval", "Интервал" },
		{ "labelEnd", "Окончание" },
		{ "labelEdit", "Редактирование" },
		{ "labelFilter", "Фильтровать" },
		{ "labelEqual", "Равенство" },
		{ "labelTime", "Время" },
		{ "labelSubstring", "Подстрока" },
		{ "labelRange", "Диапазон" },
		{ "labelChange", "Изменить" },
		{ "labelAdd", "Добавить" },
		{ "labelList", "Список" },
		{ "labelCriteriaFilt", "Критерий фильтрации" },
		{ "labelCriteriaDel", "Удалить критерий" },
		{ "labelAllTestResults", "Все результаты тестирования" },
		{ "labelIdIzmer", "Только идентификаторы измерения" },
		{ "labelKnownEvents", "Только распознанные события" },
		{ "labelSaveResBD", "Параметры сохранения" },
		{ "labelWaveLength", "Длина волны, нм" },
		{ "labelAverCount", "Количество усреднений" },
		{ "labelImpuls", "Ширина импульса, нс" },
		{ "labelDetalM", "Разрешение, м" },
		{ "labelMaxDistance", "Максимальная дистанция, м" },
		{ "labelSaveBD", "Хранение данных в БД" },
		{ "labelReflect", "Показатель преломления" },
		{ "labelIdIzmer", "Идентификатор измерения" },
		{ "labelTestType", "Тип теста" },
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
		{ "labelUslovie", "Условие" },
		{ "labelZnachenie", "Значение" },
		{ "labelFiltration", "Фильтрация" },
		{ "labelPoZnach", "по значению" },
		{ "labelPoSpisku", "по списку" },
		{ "labelPoPodstroke", "по подстроке" },
		{ "labelPoDiapOt", "по диапазону от" },
		{ "labelPoDiapDo", "до" },
		{ "labelTimeOt", "по времени от" },
		{ "labelTimeDo", "до" },
		{ "labelFrom", "ОТ" },
		{ "labelTo", "ДО" },
		{ "labelStringForSearch", "Строка для поиска" },
		{ "labelAlarmTest", "Тесты с алармами" },
		{ "labelNoAlarmTest", "Тесты без алармов" },
		{ "labelAlarm", "Аларм" },
		{ "labelTtimeTest", "Время тестирования" },
		{ "labelTimeTestType", "Вид тестирования (временной)" },
		{ "labelPopupEdit", "Редактировать тест" },
		{ "labelPopupDel", "Удалить тест(ы)" },
		{ "labelPopupAdd", "Добавить тест" },
		{ "labelPopupDelKIS", "Удалить панель КИСа" },
		{ "labelDelTestRealQ", "Вы действительно хотите удалить тест(ы)?" },
		{ "labelDelTestReal", "Подтверждение удаления" },
		{ "labelMakeAnalysis", "Анализ" },
		{ "labelMakeEvalAnalysis", "Сравнительный анализ" },
		{ "labelDataProcParam", "Параметры обработки данных" },
		{ "labelTipycalTests", "Шаблоны" },
		{ "labelParamTestValue", "Параметры измерений" },
		{ "labelKISObjects", "Список КИСов" },

// Detalyze strings
		{ "DetMinute", "Минута" },
		{ "DetHour", "Час" },
		{ "DetDay", "Сутки" },
		{ "DetMonth", "Месяц" },
		{ "DetYear", "Год" },

// OR strings
		{ "ORKIS", "КИС" },
		{ "ORDomain", "Домен" },
		{ "ORPath", "Волокно" },
		{ "ORPath1", "Объект теста" },
		{ "ORPaths", "Объекты теста" },
		{ "ORMones", "Объекты теста" },
		{ "ORTestType1", "Тип теста" },
		{ "ORTestType", "Тип теста" },
		{ "ORTestTypes", "Типы теста" },
		{ "ORPort", "Порт" },

// Error messages
		{ "errorTitleChangePassword", "Ошибка изменения" },
		{ "errorTitleOpenSession", "Ошибка входа" },

		{ "errorWrongName", "Неверно введено имя пользователя" },
		{ "errorWrongPassword", "Неверно введен пароль" },
		{ "errorWrongPassword2", "Новый пароль второй раз введен ошибочно" },
		{ "errorWrongLogin", "Проверьте имя пользователя и пароль" },
		{ "errorWrongTest", "Ошибка задания теста" },
		{ "errorWrongTestObjects", "Неверно выбраны объекты тестирования!" },
		{ "errorWrongTestTime", "Время окончания меньше времени начала теста!" },

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

	public LangModelSchedule_ru()
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