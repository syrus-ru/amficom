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
// * Автор: Песковский П.Ю.                                              * //
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



public class LangModelReport_ru extends LangModelReport
{
	static final Object[][] contents = {

//Типы шаблонов
		{ "rtt_Evaluation","Шаблоны по оценке"},
		{ "rtt_Prediction","Шаблоны по прогнозированию"},
		{ "rtt_Analysis","Шаблоны по анализу"},
		{ "rtt_Survey","Шаблоны по исследованию"},
		{ "rtt_Modeling","Шаблоны по моделированию"},
		{ "rtt_Optimization","Шаблоны по оптимизации"},
		{ "rtt_AllTemplates","Все шаблоны"},

//Типы отчёта
		{ "rep_stat_po_polju","Статистика по полю"},
		{ "rep_graphic","График - ломанная кривая"},
		{ "rep_gistogram","Гистограмма"},
		{ "rep_diagr_pirog","Диаграмма - пирог 3D"},
		{ "rep_diagr_pirog2D","Диаграмма - пирог"},
		{ "rep_stolb_diagr","Столбцовая диаграмма 3D"},
		{ "rep_stolb_diagr2D","Столбцовая диаграмма"},
		{ "rep_spis_obj","Отчёт \"Список объектов\""},

		{ "rep_equipChars","Оборудование:характеристики"},
		{ "rep_linkChars","Линия связи:характеристики"},
		{ "label_byFields","по полям "},
		{ "label_more","...(ещё "},

//Типы привязок and some additional
		{ "popup_priv_vert","Привязка по вертикали"},
		{ "popup_priv_horiz","Привязка по горизонтали"},
		{ "popup_cancel_priv","Отменить привязки"},

		{ "priv_templ_left","Привязка к левой грани поля шаблона"},
		{ "priv_templ_top","Привязка к верхней грани поля шаблона"},
		{ "priv_obj_top","Привязка к верхней грани объекта отчёта"},
		{ "priv_obj_bottom","Привязка к нижней грани объекта отчёта"},
		{ "priv_obj_left","Привязка к левой грани объекта отчёта"},
		{ "priv_obj_right","Привязка к правой грани объекта отчёта"},

		{ "vybor_priv1","Выберите грань объекта для привязки"},
		{ "vybor_priv2","Выбор способа привязки"},

//Report module labels and messages
//Common
		{ "label_confirm","Подтверждение"},
		{ "label_error","Ошибка"},
		{ "label_cancel","Отмена"},
		{ "label_choose","Выбрать"},
		{ "label_change","Изменить"},
		{ "label_open","Открыть"},
		{ "label_close","Закрыть"},
		{ "label_save","Сохранить"},
		{ "label_delete","Удалить"},
		{ "label_print","Распечатать"},
		{ "label_add","Добавить"},
		{ "label_apply","Применить"},
		{ "label_input","Задать"},
		{ "label_exit","Выход"},

//for RenderingObject FirmedTextPane,ReportTemplatePanel,ReportTemplateImplementationPanel
		{ "label_lb","Надпись"},
		{ "label_ro","Элемент шаблона"},
		{ "label_im","Картинка"},

		{ "popup_font","Шрифт"},
		{ "label_chooseReport","Выбор отчёта"},
		{ "popup_vertRazb","Задать количество разбиений по вертикали"},

		{ "label_noSchemeSet","Не выбрана схема!"},
		{ "label_noselectedTopology","Топология не выбрана!"},
		{ "label_chooseDialog","Выбор"},
		{ "label_vertRazb","Введите количество вертикальных разбиений таблицы"},
		{ "label_selectedReports","Выбранные отчёты"},
		{ "label_availableReports","Доступные документы"},
		{ "label_emptyReport","Отчёт пуст!"},
		{ "label_allFiles","Все файлы"},
		{ "label_htmlFiles","Файлы HTML"},
		{ "label_GIFFiles","Файлы GIF"},
		{ "label_JPGFiles","Файлы JPG"},

		{ "label_fileExists","Файл с таким именем существует. Заменить?"},
		{ "label_noSelectedSolution","Решение по оптимизации не выбрано!"},

		{ "label_rtbWindowTitle","Редактор шаблона отчёта"},
		{ "label_templateScheme","Схема шаблона"},
		{ "label_reportForTemplate","Отчёт по шаблону"},

		{ "label_newTemplate","Создать новый шаблон"},
		{ "label_saveTemplate","Сохранение шаблона"},
		{ "label_openTemplate","Загрузка шаблона"},
		{ "label_importTemplate","Импорт шаблонов"},
		{ "label_exportTemplate","Экспорт шаблонов"},
		{ "label_viewTemplatesScheme","Просмотреть схему шаблона"},
		{ "label_viewReport","Просмотреть отчёт"},

		{ "label_addLabel","Добавить надпись"},
		{ "label_addImage","Добавить картинку"},
		{ "label_deleteObject","Удалить объект"},
		{ "label_saveReport","Сохранить отчёт"},
		{ "label_saveChanges","Сохранить изменения в текущем шаблоне?"},
		{ "label_confImportTemplates","Вы действительно хотите осуществить импорт шаблонов?"},
		{ "label_crossesExist","Объекты и надписи на схеме шаблона не должны пересекаться."},

		{ "label_report","Отчёт "},
		{ "label_cantImpl"," не может быть реализован."},
		{ "label_templatePiece","Элементы шаблона предназначены для реализации из других модулей"},
		{ "label_poolObjNotExists","Не подгружены объекты для реализации отчёта "},
		{ "label_generalCRError","Неопределённая ошибка при создании визульного компонента для отчёта."},

		{ "label_chooseTemplateSize","Выберите размер шаблона:"},

//for ChartFrame
		{ "label_number","Количество"},
		{ "label_period","Период"},
		{ "label_numberForPeriod","Количество, за период"},
		{ "label_field","Параметр"},
		{ "label_value","Значение"},
		{ "label_Intervalvalue","Введите величину интервала для статистики"},

		{ "label_hour","Час"},
		{ "label_day","День"},
		{ "label_week","Неделя"},
		{ "label_month","Месяц"},
		{ "label_quarter","Квартал"},
		{ "label_year","Год"},


//for SelectReportsPanel
		{ "label_srw_title","Выбор объектов отчёта"},

		{ "label_diagrProp","Свойства диаграммы"},
		{ "label_templateExists","Шаблон с таким именем существует. Заменить?"},

		{ "label_repSboiSistemy","Отчёт по сбоям системы мониторинга"},
		{ "label_repStat","Статистический отчёт"},
		{ "label_repAnalysisResults","Результаты анализа"},
		{ "label_repSurveyResults","Результаты исследования"},
		{ "label_repEvaluationResults","Результаты оценки"},
		{ "label_repPredictionResults","Результаты прогнозирования"},
		{ "label_repModelingResults","Результаты моделирования"},
		{ "label_repOptimizationResults","Результаты оптимизации"},
		{ "label_repPhysicalScheme","Физическая схема"},
		{ "label_repTopologicalScheme","Топологическая схема"},
		{ "label_repAbonentsPassports","Пасспорта абонентов"},
		{ "label_repMetrologicalPoverka","Отчёт о метрологической поверке"},

		{ "label_availableReports","Доступные отчёты"},
		{ "label_availableTemplElems","Доступные элементы шаблона"},


//for FontChooserDialog
		{ "label_chooseFont","Выбор шрифта"},
		{ "label_fontName","Название"},
		{ "label_fontSize","Размер"},
		{ "label_fontExample","Пример"},
		{ "label_fontBold","Жирный"},
		{ "label_fontItalic","Курсив"},

//all errors
		{ "error_noDataToDisplay","Нет данных для отображения!"},
		{ "error_numb_req","Введите числовое значение!"},
		{ "error_fileNotAvailable","Файл недоступен для записи!"},
		{ "error_emptyObjectReport","Отчёт по списку объектов должен содержать хотя бы одну активную колонку!"},

		{ "error_noTemplate","Шаблон с таким именем не существует!"},

//for opimization model and tables
		{ "label_sourceData","Исходные данные по оптимизации"},
		{ "label_scheme","Схема"},
		{ "label_topology","Топология"},
		{ "label_solution","Решение по оптимизации"},
		{ "label_equipFeatures","Основные характериситки оборудования"},
		{ "label_optimizeResults","Результаты оптимизации"},
		{ "label_cost","Расчёт стоимости"},

		{ "label_chooseParams","Выбор параметров"},

		{ "label_path","Путь"},
		{ "label_fallValue","Оценочное затухание, дБ"},
		{ "error_noSourceInfo","Нет исходной информации по оптимизации для схемы ID = "},

		{ "label_cost","Цена"},
		{ "label_reflectName","Имя рефлектометра"},
		{ "label_dinamicDiapazon","Динамический диапазон"},
		{ "label_switchName","Имя свитча"},
		{ "label_portsNumber","Количество портов"},
		{ "label_totalNodeCost","Суммарная стоимость узла"},
		{ "label_totalSumm","ИТОГО:"},

		{ "label_date","Дата"},
		{ "label_koefZapasa","Коэффициент запаса"},
		{ "label_procentRandom","Процент рандомизации"},
		{ "label_stepenRandom","Степень рандомизации"},
		{ "label_verUdalRTU","Вероятность удаления RTU"},
		{ "label_verSozdRTU","Вероятность создания RTU"},
		{ "label_verObjedVolokon","Вероятность объединения волокон"},
		{ "label_verRazjedVolokon","Вероятность разъединения волокон"},
		{ "label_konserv","Консервативность"},

		{ "label_reflectChars","Характеристики рефлектометров"},
		{ "label_switchChars","Характеристики свитчей"},

//for opimization model and tables

/*//for evaluation model and tables
		{ "label_testParams","Параметры теста"},
		{ "label_commonInfo","Общая информация"},
		{ "label_markersData","Данные по маркерам"},
		{ "label_analysisParams","Параметры анализа"},
		{ "label_reflectogram","Рефлектограмма"},
		{ "label_mask_view","Вид маски"},
		{ "label_commonChars","Основные характеристики"},
		{ "label_addChars","Дополнительные характеристики"},
		{ "label_mask_type","Тип и параметры маски"},
//for evaluation model and tables*/

		{ "label_equipName","Название оборудования"},
		{ "label_equipClass","Класс оборудования"},
		{ "label_equipMark","Марка"},
		{ "label_equipChars","Характеристики оборудования"},
	};


	public LangModelReport_ru()
	{
		symbols.setEras(new String [] {"н.э.","до н.э."});
		symbols.setMonths(new String [] {
			"январь","февраль", "март",
			"апрель", "май", "июнь",
			"июль", "август", "сентябрь",
				"октябрь", "ноябрь", "декабрь"});
		symbols.setShortMonths(new String [] {
			"янв","фев", "мар",
			"апр","май", "июн",
			"июл", "авг", "сен",
			"окт", "ноя", "дек"});
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
