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
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelConfig_ru.java                   * //
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

public class LangModelConfig_ru extends LangModelConfig
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
		{ "menuExitText", "Выход" },

		{ "menuViewText", "Вид" },
		{ "menuViewNavigatorText", "Навигатор объектов" },
		{ "menuViewMessagesText", "Окно диагностических сообщений" },
		{ "menuViewToolbarText", "Панель инструментов" },
		{ "menuViewAttributesText", "Окно свойств" },
		{ "menuViewElementsText", "Окно элементов" },
		{ "menuViewSetupText", "Операции на карте" },
		{ "menuViewMapText", "Окно карты" },
		{ "menuViewMapSchemeText", "Окно топологической привязки" },

		{ "menuViewRefreshText", "Обновить информацию" },
		{ "menuViewCatalogueText", "Каталог операций" },
		{ "menuViewElementsNavigatorText", "Панель объектов" },
		{ "menuViewAllText", "Открыть всё" },

		{ "menuMapText", "Карта" },
    { "menuMapNewText", "Новая"},
		{ "menuMapOpenText", "Открыть" },
		{ "menuMapCloseText", "Закрыть" },
		{ "menuMapSaveText", "Сохранить" },
		{ "menuMapOptionsText", "Настройки" },
		{ "menuMapCatalogueText", "Управление картами" },

		{ "menuSchemeText", "Схема" },
		{ "menuSchemeMapBitmapsText", "Растровые изображения" },
		{ "menuSchemeMapIconsText", "Пиктограммы" },
		{ "menuSchemeMapStyleText", "Стиль изображения" },
		{ "menuSchemeMapText", "Карта" },
		{ "menuSchemeMapGISText", "ГИС" },
		{ "menuSchemeMapCoordText", "Система координат" },
		{ "menuSchemeNetText", "Схема ТС" },
		{ "menuSchemeNetSchemeText", "Редактор физических схем" },
		{ "menuSchemeNetAttributeText", "Атрибуты" },
		{ "menuSchemeNetElTypeText", "Редактор компонентов" },
		{ "menuSchemeNetElementText", "Редактор компонентов" },
		{ "menuSchemeNetViewText", "Редактор топологических схем" },
		{ "menuSchemeNetCatalogueText", "Каталог схем на карте" },
		{ "menuSchemeNetOpenText", "Просмотр топологической схемы сети" },
		{ "menuSchemeNetOpenSchemeText", "Просмотр физической схемы сети" },
		{ "menuSchemeJText", "Схема СМ" },
		{ "menuSchemeJSchemeText", "Схема" },
		{ "menuSchemeJAttributeText", "Атрибуты" },
		{ "menuSchemeJElTypeText", "Типы элементов" },
		{ "menuSchemeJElementText", "Элементы" },
		{ "menuSchemeJLayoutText", "Вид на карте" },
		{ "menuSchemeJCatalogueText", "Каталог схем" },
		{ "menuSchemeJOpenText", "Открыть" },

		{ "menuObjectText", "Объекты сети" },
		{ "menuObjectNavigatorText", "Навигатор объектов" },
		{ "menuObjectDomainText", "Домены" },
		{ "menuNetText", "Объекты ТС" },
		{ "menuNetDirText", "Справочник ТС" },
		{ "menuNetDirAddressText", "Типы адресов" },
		{ "menuNetDirResourceText", "Типы сетевых ресурсов" },
		{ "menuNetDirEquipmentText", "Типы оборудования" },
		{ "menuNetDirProtocolText", "Протоколы взаимодействия" },
		{ "menuNetDirLinkText", "Типы линий связи" },
		{ "menuNetDirCableText", "Типы кабельных линий связи" },
    { "menuNetDirPortText", "Типы портов" },
    { "menuNetDirCablePortText", "Типы кабельных портов" },
		{ "menuNetDirTechnologyText", "Технология взаимодействия" },
		{ "menuNetDirInterfaceText", "Типы интерфейсов" },
		{ "menuNetDirPortText", "Типы сетевых портов" },
		{ "menuNetDirStackText", "Стек протоколов" },
		{ "menuNetCatText", "Каталог ТС" },
		{ "menuNetCatEquipmentText", "Оборудование" },
		{ "menuNetCatLinkText", "Линии связи" },
		{ "menuNetCatCableText", "Кабельные линии связи" },
    { "menuNetCatPortText", "Порты" },
    { "menuNetCatCablePortText", "Кабельные порты" },
		{ "menuNetCatResourceText", "Сетевые ресурсы" },
		{ "menuNetCatTPGroupText", "Группы точек наблюдения" },
		{ "menuNetCatTestPointText", "Точки наблюдения" },
		{ "menuNetLocationText", "Размещение" },

		{ "menuJText", "Объекты СМ" },
		{ "menuJDirText", "Справочник СМ" },
		{ "menuJDirKISText", "Типы контрольно-измерительного оборудования" },
		{ "menuJDirAccessPointText", "Типы портов тестирования" },
		{ "menuJDirPathText", "Типы путей тестирования" },
		{ "menuJDirLinkText", "Типы соединения" },
		{ "menuJCatText", "Каталог СМ" },
		{ "menuJCatKISText", "Контрольно-измерительное оборудование" },
		{ "menuJCatAccessPointText", "Порты тестирования" },
		{ "menuJCatPathText", "Пути тестирования" },
		{ "menuJCatResourceText", "Сетевые ресурсы" },
		{ "menuJInstallText", "Инсталляция КИС" },

		{ "menuMaintainText", "Сопровождение" },
		{ "menuMaintainTypeText", "Виды нештатных ситуаций" },
		{ "menuMaintainEventText", "События" },
		{ "menuMaintainAlarmRuleText", "Правила генерирования сигналов тревоги" },
		{ "menuMaintainMessageRuleText", "Правила генерирования сообщений" },
		{ "menuMaintainAlertRuleText", "Правила оповещения" },
		{ "menuMaintainReactRuleText", "Правила реагирования" },
		{ "menuMaintainRuleText", "Правила сопровождения" },
		{ "menuMaintainCorrectRuleText", "Правила исправления" },

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
		{ "menuWindowTileHorizontalText", "Упорядочить горизонтально" },
		{ "menuWindowTileVerticalText", "Упорядочить вертикально" },
		{ "menuWindowCascadeText", "Каскадировать" },
		{ "menuWindowArrangeText", "Упорядочить" },
		{ "menuWindowArrangeIconsText", "Упорядочить иконки" },
		{ "menuWindowMinimizeAllText", "Минимизировать все" },
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
		{ "menuViewAttributesToolTip", "Окно свойств" },
		{ "menuViewElementsToolTip", "Окно элементов" },
		{ "menuViewSetupToolTip", "Операции на карте" },
		{ "menuViewMapToolTip", "Окно карты" },
		{ "menuViewMapSchemeToolTip", "Окно топологической привязки" },
		{ "menuViewRefreshToolTip", "Обновить информацию" },
		{ "menuViewCatalogueToolTip", "Каталог операций" },
		{ "menuViewElementsNavigatorToolTip", "Панель объектов" },
		{ "menuViewAllText", "Открыть всё" },

		{ "menuMapText", "Карта" },
		{ "menuMapNewText", "Новая"},
		{ "menuMapOpenText", "Открыть" },
		{ "menuMapCloseText", "Закрыть" },
		{ "menuMapSaveText", "Сохранить" },
		{ "menuMapOptionsText", "Настройки" },
		{ "menuMapCatalogueText", "Управление картами" },

		{ "menuObjectToolTip", "Объекты сети" },
		{ "menuObjectNavigatorToolTip", "Навигатор объектов" },
		{ "menuObjectDomainToolTip", "Домены" },
		{ "menuNetToolTip", "Объекты сети" },
		{ "menuNetDirToolTip", "Справочник сети" },
		{ "menuNetDirAddressToolTip", "Типы адресов" },
		{ "menuNetDirResourceToolTip", "Типы сетевых ресурсов" },
		{ "menuNetDirEquipmentToolTip", "Типы оборудования" },
		{ "menuNetDirProtocolToolTip", "Протоколы взаимодействия" },
		{ "menuNetDirLinkToolTip", "Типы линий связи" },
		{ "menuNetDirTechnologyToolTip", "Технология взаимодействия" },
		{ "menuNetDirInterfaceToolTip", "Типы интерфейсов" },
		{ "menuNetDirPortToolTip", "Типы сетевых портов" },
		{ "menuNetDirStackToolTip", "Стек протоколов" },
		{ "menuNetCatToolTip", "Каталог сети" },
		{ "menuNetCatEquipmentToolTip", "Оборудование" },
		{ "menuNetCatLinkToolTip", "Линии связи" },
		{ "menuNetCatCableToolTip", "Кабельные линии связи" },
		{ "menuNetCatResourceToolTip", "Сетевые ресурсы" },
		{ "menuNetCatTPGroupToolTip", "Группы точек наблюдения" },
		{ "menuNetCatTestPointToolTip", "Точки наблюдения" },
		{ "menuNetLocationToolTip", "Размещение" },

		{ "menuJToolTip", "Объекты ИСМ" },
		{ "menuJDirToolTip", "Справочник ИСМ" },
		{ "menuJDirKISToolTip", "Типы контрольно-измерительных средств" },
		{ "menuJDirAccessPointToolTip", "Типы портов доступа" },
		{ "menuJDirLinkToolTip", "Типы соединения" },
		{ "menuJCatToolTip", "Каталог ИСМ" },
		{ "menuJCatKISToolTip", "Контрольно-измерительные средства" },
		{ "menuJCatAccessPointToolTip", "Порт доступа" },
		{ "menuJCatResourceToolTip", "Сетевые ресурсы" },

		{ "menuSchemeToolTip", "Схема" },
		{ "menuSchemeMapBitmapsToolTip", "Растровые изображения" },
		{ "menuSchemeMapIconsToolTip", "Пиктограммы" },
		{ "menuSchemeMapStyleToolTip", "Стиль изображения" },
		{ "menuSchemeMapToolTip", "Карта" },
		{ "menuSchemeMapGISToolTip", "ГИС" },
		{ "menuSchemeMapCoordToolTip", "Система координат" },
		{ "menuSchemeNetToolTip", "Схема сети" },
		{ "menuSchemeNetSchemeToolTip", "Редактор физических схем" },
		{ "menuSchemeNetAttributeToolTip", "Атрибуты" },
		{ "menuSchemeNetElTypeToolTip", "Редактор компонентов" },
		{ "menuSchemeNetElementToolTip", "Редактор компонентов" },
		{ "menuSchemeNetCatalogueToolTip", "Каталог схем на карте" },
		{ "menuSchemeNetViewToolTip", "Редактор топологических схем" },
		{ "menuSchemeNetOpenToolTip", "Просмотр топологической схемы сети" },
		{ "menuSchemeNetOpenSchemeToolTip", "Просмотр физической схемы сети" },
		{ "menuSchemeJToolTip", "Схема ИСМ" },
		{ "menuSchemeJSchemeToolTip", "Схема" },
		{ "menuSchemeJAttributeToolTip", "Атрибуты" },
		{ "menuSchemeJElTypeToolTip", "Типы элементов" },
		{ "menuSchemeJElementToolTip", "Элементы" },
		{ "menuSchemeJLayoutToolTip", "Вид на карте" },
		{ "menuSchemeJCatalogueToolTip", "Каталог схем" },

		{ "menuMaintainToolTip", "Сопровождение" },
		{ "menuMaintainTypeToolTip", "Виды нештатных ситуаций" },
		{ "menuMaintainEventToolTip", "События" },
		{ "menuMaintainAlarmRuleToolTip", "Правила генерирования сигналов тревоги" },
		{ "menuMaintainMessageRuleToolTip", "Правила генерирования сообщений" },
		{ "menuMaintainAlertRuleToolTip", "Правила оповещения" },
		{ "menuMaintainReactRuleToolTip", "Правила реагирования" },
		{ "menuMaintainRuleToolTip", "Правила сопровождения" },
		{ "menuMaintainCorrectRuleToolTip", "Правила исправления" },

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
		{ "menuToolsStyleTextToolTip", "Текст" },
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
		{ "menuWindowTileHorizontalToolTip", "Упорядочить горизонтально" },
		{ "menuWindowTileVerticalToolTip", "Упорядочить вертикально" },
		{ "menuWindowCascadeToolTip", "Каскадировать" },
		{ "menuWindowArrangeToolTip", "Упорядочить" },
		{ "menuWindowArrangeIconsToolTip", "Упорядочить иконки" },
		{ "menuWindowMinimizeAllToolTip", "Минимизировать все" },
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
		{ "menuHelpAboutToolTip", "О программе" },
		{ "menuJInstallToolTip", "Инсталляция КИС" },

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
		{ "MapTitle", "Редактор топологии АМФИКОМ" },
		{ "AppTitle", "Конфигурирование АМФИКОМ" },
		{ "ChangePasswordTitle", "Изменение пароля" },
		{ "SessionInfoTitle", "Параметры сессии" },
		{ "ConnectionTitle", "Параметры соединения с сервером" },
		{ "SessionOpenTitle", "Открыть сессию" },
		{ "titleMapsCatalog", "Каталог схем сети" },

// Button strings
		{ "buttonHelp", "Помощь" },
		{ "buttonAccept", "Принять" },
		{ "buttonAdd", "Добавить" },
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


		{ "appText", "" },

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

		{ "labelALARM_STATUS_GENERATED", "Новый" },
		{ "labelALARM_STATUS_ASSIGNED", "Подтвержден" },
		{ "labelALARM_STATUS_FIXED", "Исправлен" },

// Alarm

//Equipment
    { "label_equipment","Общие характеристики оборудования"},

    { "equip_domen", "Домен" },
    { "equip_longitude", "Долгота" },
    { "equip_latitude", "Широта" },
    { "equip_hSN", "Сер-й номер оборуд-я" },
    { "equip_sSN", "Сер-й номер ПО" },
    { "equip_hVer", "Текущая версия оборуд-я" },
    { "equip_sVer", "Текущая версия ПО" },
    { "equip_regN", "Регистрационный номер" },

    { "equip_agent", "Агент КИС" },

    { "equip_portsNumber", "Число портов" },
    { "equip_cablePortsNumber1", "Число" },
    { "equip_cablePortsNumber2", "кабельных портов" },

    { "equip_hVer1", "Текущая версия" },
    { "equip_SN1", "Серийный номер" },
    { "equip_2", "оборудования" },
    { "equip_soft2", "програмного обеспечения" },
//Equipment

//Ports
    { "label_ports","Порты"},
    { "label_port", "Порт" },

    { "port_equipment_id", "Оборудование" },
    { "port_interface_id", "Интерфейс" },
    { "port_address_id", "Адрес" },
    { "port_domain_id", "Домен" },
    { "port_class", "Класс" },

    { "label_cableports","Кабельные порты"},
    { "label_accessports","Порты доступа"},
    { "label_accessport","Порт доступа"},
    { "label_evalport","Измерительный порт"},
//Ports

//Links
		{ "label_links","Линии связи"},
		{ "label_link","Линия связи"},

		{ "link_domain_id", "Домен" },
		{ "link_class", "Класс соединения" },

		{ "link_start_equipment_id", "Начальное устройство"},
		{ "link_start_equipment_id1", "начального устройства"},
		{ "link_start_port_id", "Начальный порт"},
		{ "link_end_equipment_id", "Конечное устройство"},
		{ "link_end_equipment_id1", "конечного устройства"},
		{ "link_end_port_id", "Конечный порт"},

		{ "link_optical_length", "Оптическая длина" },
		{ "link_physical_length", "Строительная длина" },

		{ "link_inventory_nr", "Регистрационный номер" },

		{ "label_cablelinks","Кабельные линии связи"},
		{ "label_cablelink","Кабельная линия связи"},
//Links

// for AvailableReportsTreeModel
		{ "label_equipChars", "Характеристики оборудования"},
		{ "mufta", "Муфты"},
		{ "switch", "Оптические переключатели"},
		{ "cross", "Кросс-панели"},
		{ "multiplexer", "Мультиплексеры"},
		{ "filter", "Фильтры"},
		{ "transmitter", "Передатчики"},
		{ "reciever", "Приёмники"},
		{ "tester", "РТУ"},
// for AvailableReportsTreeModel


//characteristics and several other
    { "label_configuration","Конфигурирование"},

    { "status_initing_data", "Инициализация данных..." },
    { "status_init_data_complete", "Данные загружены" },

    { "label_id", "Идентификатор" },
    { "label_name", "Название" },
    { "label_description", "Описание" },
    { "label_type", "Тип" },

    { "label_modified1", "Время" },
    { "label_modified2", "последнего изменения" },

    { "label_manufacter", "Производитель" },
    { "label_manCode", "Код производителя" },
    { "label_supplier", "Поставщик" },
    { "label_supCode", "Код поставщика" },

    { "label_local_addr1", "Строка" },
    { "label_local_addr2", "локальной адресации" },

    { "label_inventory_nr1", "Регистрационный " },
    { "label_inventory_nr2", "номер" },
    { "label_test_types", "Типы тестирования"},
    { "label_fibers", "Волокна"},
    { "label_mark", "Метка"},

    { "label_chars", "Характеристики"},
    { "label_char", "Характеристика"},
    { "label_new_char", "Новая характеристика"},
    { "label_add_char", "Добавить характеристику"},
    { "label_delete_char", "Удалить характеристику"},
    { "label_comp_chars", "Характеристики компонента"},
    { "label_opt_chars", "Оптические характеристики"},
    { "label_el_chars", "Электрические характеристики"},
    { "label_exp_chars", "Эксплуатационные характеристики"},
    { "label_interface_chars", "Интерфейсные характеристики"},
    { "label_general", "Общие"},
    { "label_additional", "Прочие"},

		{ "label_schemeStructure", "Структура сети" },
		{ "label_topologicalScheme", "Топологическая схема" },
		{ "label_nodes", "Узлы" },
		{ "label_topologicalNodes", "Топологические узлы" },
		{ "label_inputLinks", "Входяшие линии связи" },
		{ "label_marks", "Метки" },
		{ "label_KIS", "КИС" },

    { "err_incorrect_data_input", "Неправильно введены данные!"}

//characteristics and several other

		};

	public LangModelConfig_ru()
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

