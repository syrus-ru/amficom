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



public class LangModelAdmin_ru extends LangModelAdmin
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
    { "menuViewNavigatorText", "Навигатор"},
    { "menuViewOpenAllText", "Упорядочить окна"},
    { "menuViewWhoAmIText", "Текущий пользователь"},
    { "menuViewOpenObjectsWindowText", "Окно редактирования"},

    { "menuArchitectureText", "Архитектура" },
    { "menuArchitectureServerText", "Сервер"},
    { "menuArchitectureAgentText", "Агент"},
    { "menuArchitectureClientText", "Клиент"},

    { "menuUserText", "Пользователь" },
    { "menuUserCategoryText", "Категории"},
    { "menuUserGroupText", "Группы"},
    { "menuUserProfileText", "Профили"},

    { "menuAccessText", "Доступ" },
    { "menuAccessDomainText", "Домены"},
    { "menuAccessModulText", "Функции модулей"},
	{ "menuAccessMaintainText", "Сопровождение" },



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
    { "menuViewNavigatorToolTip", "Навигатор"},
    { "menuViewOpenAllToolTip", "Открыть все окна"},
    { "menuViewWhoAmIToolTip", "Текущий пользователь"},
    { "menuViewOpenObjectsWindowToolTip", "Окно редактирования"},

    { "menuArchitectureToolTip", "Архитектура" },
    { "menuArchitectureServerToolTip", "Сервер"},
    { "menuArchitectureAgentToolTip", "Агент"},
    { "menuArchitectureClientToolTip", "Клиент"},

    { "menuUserToolTip", "Пользователь" },
    { "menuUserCategoryToolTip", "Категории"},
    { "menuUserGroupToolTip", "Группы"},
    { "menuUserProfileToolTip", "Профили"},

    { "menuAccessToolTip", "Доступ" },
    { "menuAccessDomainToolTip", "Домены"},
    { "menuAccessModulToolTip", "Функции модулей"},
	{ "menuAccessMaintainToolTip", "Сопровождение" },


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
    { "AppTitle", "Администрирование АМФИКОМ" },
    { "titleObjectsCatalog", "Административные объекты"},

// Button strings
    { "buttonHelp", "Помощь" },
    {"buttonAttribSet","Установить"},
    {"buttonAttribCancel","Отменить"},

// Label strings
    { "labelName", "Имя" },
    { "labelTabbedFilter", "Фильтр" },
    { "labelTabbedList", "Список" },
    { "labelTabbedProperties", "Свойства" },
    {"AttribUser","Пользователь"},
    {"AttribGroup","Группа"},
    {"AttribOther","Прочие"},
    {"AttribR","Чтение"},
    {"AttribW","Запись"},
    {"AttribX","Выполнение"},
    {"AttribID","ID объекта"},
    {"AttribOwner","Владелец"},
    {"AttribGRP","Группа"},
    {"AttribOperator","Оператор"},
    {"AttribAdministrator","Администаратор"},
    {"AttribSysAdmin","Сист. Админ."},
    {"AttribConfigurator","Конфигуратор"},


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

    { "AppText", "" },



    { "group", "Группа"},
    { "category","Категория"},
    { "domain","Домен"},
    { "domains","Домены"},
    { "user","Пользователь"},
    { "users","Пользователи"},
    { "read","Чтение"},
    { "write","Запись"},
    { "execute","Исполнение"},
    { "created","Создан"},
    { "modified","Изменен"},
    { "codename","Кодовое имя"},
    { "name","Имя"},
    { "login","Логин"},

// Text for panels and buttons
    {"attachText", "Прикрепить"},
    {"detachText", "Открепить "},
    {"okText",     "Принять "},
    {"cancelText", "Отменить"},

    {"nameText", "Название"},
    {"idText", "Идентификатор"},
    {"codenameText", "Кодовое имя"},
    {"loginText", "Логин"},
    {"createdText", "Создан"},
    {"timeCreationText", "Дата создания"},
    {"modifiedText", "Изменен"},
    {"timeModificationText", "Дата изменения"},
    {"domainText", "Домен"},
    {"includedInDomainText", "В составе домена"},
    {"denyMessageText", "Текст запрета"},
    {"readText", "Чтение"},
    {"writeText", "Запись"},
    {"executeText", "Исполнение"},
    {"ownerText", "Владелец"},
    {"Text", ""},
    {"Text", ""},
    {"Text", ""},



  };

  public LangModelAdmin_ru()
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