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
// * Название: базовый модуль строковых констант на русском ПО АМФИКОМ    * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModel_ru.java                         * //
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

public class LangModel_ru extends LangModel
{
	public Object[][] contents = {

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
		{ "menuSessionDomainText", "Выбор домена" },
		{ "menuExitText", "Выход" },

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
		{ "menuSessionDomainToolTip", "Выбор домена" },
		{ "menuExitToolTip", "Выход" },

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

		{ "statusRunningAdmin", "Запуск Admin" },
		{ "statusRunningConfig", "Запуск Config" },
		{ "statusRunningTrace", "Запуск Trace" },
		{ "statusRunningSurvey", "Запуск Survey" },
		{ "statusRunningAnalyse", "Запуск Analyse" },
		{ "statusRunningNorms", "Запуск Norms" },

		{ "statusNoConnection", "Нет соединения" },
		{ "statusConnectionError", "Ошибка соединения!" },

		{ "statusNoSession", "Нет сессии" },

		{ "statusNoDomain", "Домен не выбран" },

		{ "statusNoUser", " " },
		{ "statusNoSession", "Нет сессии" },

// Windows titles
		{ "AppTitle", "Оператор АМФИКОМ" },
		{ "ChangePasswordTitle", "Изменение пароля" },
		{ "SessionInfoTitle", "Параметры сессии" },
		{ "ConnectionTitle", "Параметры соединения с сервером" },
		{ "SessionOpenTitle", "Открыть сессию" },
		{ "SessionDomainTitle", "Выбор домена" },

		{ "CalendarTitle", "Календарь" },
		{ "YearPostfix", "г." },
		{ "Today", "Сегодня" },


// Button strings
		{ "buttonHelp", "Помощь" },
		{ "buttonAccept", "Принять" },
		{ "buttonCancel", "Отменить" },
		{ "buttonChange", "Изменить" },
		{ "buttonClose", "Закрыть" },
		{ "buttonEnter", "Войти в систему" },
		{ "buttonStandard", "Стандартные" },
		{ "buttonCheck", "Тест" },
		{ "buttonSelect", "Выбрать" },

		{ "Help", "Помощь" },
		{ "Ok", "OK" },
		{ "Remove", "Удалить" },
		{ "Cancel", "Отменить" },

// Label strings
		{ "labelName", "Имя" },
		{ "labelPassword", "Пароль" },
		{ "labelOldPassword", "Старый пароль" },
		{ "labelNewPassword", "Новый пароль" },
		{ "labelNewPassword2", "Новый пароль *" },
		{ "labelCategory", "Категория" },
		{ "labelDomain", "Домен" },

		{ "labelServerIP", "IP-адрес сервера:" },
		{ "labelServerTCP", "TCP-порт:" },
		{ "labelSID", "SID:" },
		{ "labelCtx", "Идентификатор сессии:" },
		{ "labelServerObject", "Серверный объект:" },
		{ "labelServiceURL", "URL сервиса:"},
		{ "labelFullURL", "Полный URL:"},
		{ "labelObjectName", "Имя доступа к объекту:" },
		{ "labelObjectPassword", "Пароль доступа к объекту:" },

		{ "labelServer", "Сервер" },
		{ "labelUser", "Пользователь" },
		{ "labelCategory", "Категория" },
		{ "labelSessionStart", "Начало сессии" },
		{ "labelSessionTotal", "Общее время сессии" },
		{ "labelActiveDomain", "Активный домен" },
		{ "labelServerConnectLast", "Время последнего соединения" },
		{ "labelServerConnectLast2", " с сервером" },

// Label strings for filters
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

// Label strings for tree
		{ "labelRoot", "Корень" },
		{ "ORMones", "Объекты теста" },
		{ "ORTestTypes", "Типы теста" },

// Label strings
		{ "labelName", "Имя" },
		{ "labelTabbedFilter", "Фильтр" },
		{ "labelTabbedList", "Список" },
		{ "labelTabbedProperties", "Свойства" },

// Error messages
		{ "errorTitleChangePassword", "Ошибка изменения пароля" },
		{ "errorTitleOpenSession", "Ошибка входа" },

		{ "errorWrongName", "Неверно введено имя пользователя" },
		{ "errorWrongPassword", "Неверно введен пароль" },
		{ "errorPasswordTooShort", "Новый пароль слишком короткий" },
		{ "errorWrongPassword2", "Новый пароль второй раз введен ошибочно" },
		{ "errorWrongLogin", "Проверьте имя пользователя и пароль" },

// Info messages
		{ "messageTitle", "information title" },

		{ "message", "information text" },

		{ "connectionSuccess", "Есть соединение!" },
		{ "connectionFail", "Нет соединения" },

// Node names
		{ "nodeoperator", "Операторы" },
		{ "nodeoperatorcategory", "Категории" },
		{ "nodeoperatorprofile", "Профили" },
		{ "nodeoperatorrole", "Роли" },
		{ "nodeoperatorprivilege", "Привилегии" },
		{ "nodeoperatorgroup", "Группы" },
		{ "nodesubscriber", "Абоненты" },
		{ "nodesubscribercategory", "Категории" },
		{ "nodesubscriberprofile", "Профили" },
		{ "nodesubscribergroup", "Группы" },
		{ "nodeorganization", "Внешние объекты" },
		{ "nodeorganizationcategory", "Категории" },
		{ "nodeorganizationgroup", "Группы" },
		{ "nodeorganizationrole", "Роли" },
		{ "nodeorganizationprofile", "Профили" },
		{ "nodeorganizationorder", "Порядок взаимодействия" },
		{ "nodeorganizationauto", "Автоматизация взаимодействи" },
		{ "nodeoperational", "Служба эксплуатации" },
		{ "nodeoperationalgroup", "Группы" },
		{ "nodeoperationalrole", "Роли" },
		{ "nodeoperationalprivilege", "Привилегии" },
		{ "nodeoperationalstaff", "Персонал" },
		{ "nodeobject", "Объекты" },
//		{ "nodemapconnectionpointelement", "Точка соединения" },

		{ "nodeaccessport", "Измерительный порт" },
		{ "nodeaccessporttype", "Тип измерительного порта" },
		{ "nodeactionargument", "Входные параметры" },
		{ "nodeactionparameter", "Выходные параметры" },
		{ "nodeagent", "Агент КИС" },
		{ "nodealarm", "Сигнал тревоги" },
		{ "nodealarmrule", "Правило генерации" },
		{ "nodealerting", "Оповещение" },
		{ "nodeanalysis", "Анализ" },
		{ "nodeanalysiscriteria", "Критерии анализа" },
		{ "nodeanalysistype", "Типы анализа" },
		{ "nodeanalysistypeargument", "Входные параметры типа анализа" },
		{ "nodeanalysistypecriteria", "Критерии типа анализа" },
		{ "nodeanalysistypeparameter", "Выходные параметры типа анализа" },
		{ "nodeattribute", "Атрибуты" },
		{ "nodeattributerule", "Правила атрибутов" },
		{ "nodecablelink", "Кабельная линия связи" },
		{ "nodecablelinkthread", "Волокно кабельной линии связи" },
		{ "nodecablelinktype", "Тип кабельной линии связи" },
		{ "nodecablethread", "Волокно в кабеле" },
		{ "nodecableport", "Кабельный порт" },
		{ "nodecableporttype", "Тип кабельного порта" },
		{ "nodecharacteristic", "Характеристики" },
		{ "nodecharacteristictype", "Тип характеристики" },
		{ "nodeclient", "Клиент" },
		{ "nodecomm_perm_attrib", "Доступ к функциям модуля" },
		{ "nodecriteria", "Параметр критерия" },
		{ "nodecriteriaset", "Набор критериев анализа" },
		{ "nodecriteriasetmelink", "Связь набора критериев анализа с исследуемым объектом" },
		{ "nodecriteriatype", "Тип критериев анализа" },
		{ "nodedeviceport", "Соединительный узел" },
		{ "nodedomain", "Домены" },
		{ "nodeelementaryresult", "Результат" },
		{ "nodeelementattribute", "Атрибуты" },
		{ "nodeelementattributetype", "Тип атрибута" },
		{ "nodeelementcharacteristic", "Характеристика" },
		{ "nodeequipment", "Оборудование" },
		{ "nodeequipmenttype", "Тип оборудования" },
		{ "nodeetalon", "Эталон" },
		{ "nodeetalontypeparameter", "Параметры типа эталона" },
		{ "nodeetalonparameter", "Параметры эталона" },
		{ "nodeevaluation", "Оценка" },
		{ "nodeevaluationargument", "Входные параметры оценки" },
		{ "nodeevaluationtype", "Тип оценки" },
		{ "nodeevaluationtypeargument", "Входные параметры типа оценки" },
		{ "nodeevaluationtypeparameter", "Выходные параметры типа оценки" },
		{ "nodeevaluationtypethreshold", "Пороги типа оценки" },
		{ "nodeevent", "Событие" },
		{ "nodeeventsource", "Источник событий" },
		{ "nodeglobalparametertype", "Тип параметра" },
		{ "nodeimageresource", "Изображение" },
		{ "nodeismmapcontext", "Структура ИСМ" },
		{ "nodekis", "КИС" },
		{ "nodekistype", "Тип КИС" },
		{ "nodelink", "Линия связи" },
		{ "nodelinktype", "Тип линии связи" },
		{ "nodeloggeduser", "Текущий пользователь системы" },
		{ "nodemapcontext", "Структура сети" },
		{ "nodemapelementlink", "Связь карты с элементом карты" },
		{ "nodemapprotogroup", "Группа элементов" },
		{ "nodemapequipmentelement", "Сетевой узел" },
		{ "nodemapkiselement", "Узел с КИС" },
		{ "nodemaplinkelement", "Линия связи" },
		{ "nodemaplinkproto", "Шаблон линии связи" },
		{ "nodemapmarkelement", "Метка" },
		{ "nodemapmarker", "Маркер" },
		{ "nodemapalarmmarker", "Сигнал тревоги" },
		{ "nodemapeventmarker", "Событие" },
		{ "nodemapnodeelement", "Топологический узел" },
		{ "nodemapnodelinkelement", "Фрагмент линии связи" },
		{ "nodemappathelement", "Путь тестирования" },
		{ "nodemappathlink", "Связь карты с элементом карты" },
		{ "nodemappathproto", "Шаблон пути тестирования" },
		{ "nodemapprotoelement", "Шаблон оборудования" },
		{ "nodemodeling", "Моделирование" },
		{ "nodemodelingtype", "Тип моделирования" },
		{ "nodemonitoredelement", "Исследуемый объект" },
		{ "nodemonitoredelementattachment", "Связь исследуемого объекта с КИС" },
		{ "nodeoperatorcategorylink", "Связь оператора с категорией" },
		{ "nodeoperatorgrouplink", "Связь оператора с группой" },
		{ "nodeparameter", "Параметр" },
		{ "nodepath", "Путь тестирования" },
		{ "nodepathlink", "Фрагмент пути тестирования" },
		{ "nodepathtype", "Тип пути тестирования" },
		{ "nodeport", "Порт" },
		{ "nodeporttype", "Тип порта" },
		{ "nodeproto", "Шаблон элемента" },
		{ "noderesourcequery", "Запрос на результаты исследования" },
		{ "noderesult", "Результат" },
		{ "noderesultparameter", "Параметр результата" },
		{ "noderesultset", "Диапазон архива измерений" },
		{ "nodescheme", "Схема" },
		{ "nodeschemecablelink", "Соединительная кабельная линия" },
		{ "nodeschemecableport", "Соединительный кабельный узел" },
		{ "nodeschemecablethread", "Волокно соединительной кабельной линии" },
		{ "nodeschemedevice", "Устройство" },
		{ "nodeschemeelement", "Элемент схемы" },
		{ "nodeschemeelementlink", "" },
		{ "nodeschemelink", "Соединительная линия" },
		{ "nodeschemepath", "Схемный путь тестирования" },
		{ "nodeschemepathlink", "" },
		{ "nodeschemeport", "Соединительный узел" },
		{ "nodeserver", "Сервер" },
		{ "nodesystemevent", "Событие" },
		{ "nodesystemeventsource", "Источник событий" },
		{ "nodesourceeventtyperule", "Правило генерации событий" },
		{ "nodetest", "Тест" },
		{ "nodetestargument", "Входной параметр теста" },
		{ "nodetestargumentset", "Набор входных параметров теста" },
		{ "nodetestport", "Порт тестирования" },
		{ "nodetestporttype", "Тип порта тестирования" },
		{ "nodetestrequest", "Запрос на тестирование" },
		{ "nodetestsetup", "Шаблон исследования" },
		{ "nodetesttimestamp", "Временная метка теста" },
		{ "nodetesttype", "Тип теста" },
		{ "nodethreshold", "Параметр порога" },
		{ "nodethresholdset", "Набор параметров порога" },
		{ "nodeuser", "Пользователь системы" },

		{ "node", "" },
		{ "node", "" },
		{ "node", "" },

		{ "actiontestrequest", "Запрос на тестирование" },
		{ "actiontest", "Тест" },
		{ "actionanalysis", "Анализ" },
		{ "actionmodeling", "Моделирование" },
		{ "actionevaluation", "Оценка" },
		{ "action", "Действие" },

		{ "property_name", "Свойство" },
		{ "property_value", "Значение" },
//		{ "property_name", "Property" },
//		{ "property_value", "Value" },

		{ "AppText", "" },

//for filter and LogicScheme
		{ "label_and", "И"},
		{ "label_or", "ИЛИ"},
		{ "label_condition", "Условие"},
		{ "label_operand", "Операнд"},
		{ "label_result", "Результат"},

		{ "label_deleteCriteria", "Удалить критерий"},
		{ "label_filterCriteria", "Критерий"},
		{ "label_equality", "Равенство"},
		{ "label_diapason", "Диапазон"},
		{ "label_time", "Время"},
		{ "label_substring", "Подстрока"},
		{ "label_list", "Список"},
		{ "label_editFilterTree", "Редактировать дерево фильтрации"},
		{ "label_summaryExpression", "Суммарное выражение"},

		{ "label_emptyScheme", "Схема пуста!"},
		{ "label_cantDeleteComp", "Нельзя удалять компоненты \"Результат\" и \"Условие\"!"},
		{ "label_error", "Ошибка"},

		{ "label_change", "Изменить"},
		{ "label_add", "Добавить"},
		{ "label_apply", "Применить"},
		{ "label_close", "Закрыть"},

		{ "label_filter", "Фильтр"},

		{ "label_lswTitle", "Редактор логической схемы"},
		{ "label_delete", "Удалить"},
		{ "label_createStandartScheme", "При добавлении новых условий создавать стандартную схему"},
		{ "label_schemeNotCompleted", "Схема не закончена. Используется стандартная."},
		{ "label_time", "Время"},
		{ "label_substring", "Подстрока"},
		{ "label_list", "Список"},
		{ "label_editFilterTree", "Редактировать дерево фильтрации"},
		{ "label_summaryExpression", "Суммарное выражение"},

		{ "Error", "Ошибка"},
		{ "Aborted", "Операция отменена"},
		{ "Finished", "Операция завершена"},
		{ "Initiating", "Инициализация данных..."},
		{ "DataLoaded", "Данные загружены"},
		{ "NotAllFieldsValid", "Невозможно создать объект - не все поля заданы"},

//for filter and LogicScheme

		};

	public LangModel_ru()
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
