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



public class LangModelReport_en extends LangModelReport
{
	static final Object[][] contents = {

//Типы шаблонов
		{ "rtt_Evaluation","Evaluation report templates"},
		{ "rtt_Prediction","Prediction report templates"},
		{ "rtt_Analysis","Analysis report templates"},
		{ "rtt_Survey","Survey report templates"},
		{ "rtt_Modeling","Modeling report templates"},
		{ "rtt_Optimization","Optimization report templates"},
		{ "rtt_AllTemplates","All report templates"},

//Типы отчёта
		{ "rep_stat_po_polju","Statistics for field"},
		{ "rep_graphic","График - ломанная кривая"},
		{ "rep_gistogram","Histogram"},
		{ "rep_diagr_pirog","3D pie chart"},
		{ "rep_diagr_pirog2D","Pie chart"},
		{ "rep_stolb_diagr","Vertical 3D bar chart"},
		{ "rep_stolb_diagr2D","Vertical bar chart"},
		{ "rep_spis_obj","Report \"Objects' list\""},

		{ "rep_equipChars","Equipment:features"},
		{ "rep_linkChars","Link:features"},

		{ "label_byFields","for fields "},
		{ "label_more","...(more "},

//Типы привязок and some additional
		{ "popup_priv_vert","Vertical tie"},
		{ "popup_priv_horiz","Horizontal tie"},
		{ "popup_cancel_priv","Untie"},

		{ "priv_templ_left","Tie to the left edge of the template field"},
		{ "priv_templ_top","Tie to the top of the template field"},
		{ "priv_obj_top","Tie to the top of the template element"},
		{ "priv_obj_bottom","Tie to the bottom of the template element"},
		{ "priv_obj_left","Tie to the left edge of the template element"},
		{ "priv_obj_right","Tie to the right edge of the template element"},

		{ "vybor_priv1","Choose the object's edge to tie to"},
		{ "vybor_priv2","Choosing kind of tie"},

//Report module labels and messages
//Common
		{ "label_confirm","Confirmation"},
		{ "label_error","Error"},
		{ "label_cancel","Cancel"},
		{ "label_choose","Choose"},
		{ "label_change","Change"},
		{ "label_open","Open"},
		{ "label_close","Close"},
		{ "label_save","Save"},
		{ "label_delete","Delete"},
		{ "label_print","Print"},
		{ "label_add","Add"},
		{ "label_apply","Apply"},
		{ "label_input","Input"},
		{ "label_exit","Exit"},

//for RenderingObject FirmedTextPane,ReportTemplatePanel,ReportTemplateImplementationPanel
		{ "label_lb","Label"},
		{ "label_ro","Template's element"},
		{ "label_im","Picture"},

		{ "popup_font","Font"},
		{ "label_chooseReport","Font choosing"},
		{ "popup_vertRazb","Set number of vertical divisions"},

		{ "label_noSchemeSet","No scheme choosed"},
		{ "label_noselectedTopology","No map choosed"},
		{ "label_noSelectedSolution","No optimization solution choosed"},
		{ "label_chooseDialog","Choosing"},
		{ "label_vertRazb","Set the number of vertical divisions"},
		{ "label_availableReports","Available documents"},
		{ "label_emptyReport","The report is empty!"},
		{ "label_allFiles","All files"},
		{ "label_htmlFiles","HTML files"},
		{ "label_GIFFiles","GIF files"},
		{ "label_JPGFiles","JPG files"},

		{ "label_fileExists","The file already exists. Replace?"},

		{ "label_rtbWindowTitle","Report templates editor"},
		{ "label_templateScheme","Template's scheme"},
		{ "label_reportForTemplate","Report for the template"},

		{ "label_newTemplate","Create new template"},
		{ "label_saveTemplate","Save template"},
		{ "label_openTemplate","Load template"},
		{ "label_importTemplate","Import templates"},
		{ "label_exportTemplate","Export templates"},
		{ "label_viewTemplatesScheme","View template's scheme"},
		{ "label_viewReport","View report for the template"},

		{ "label_addLabel","Add label"},
		{ "label_addImage","Add image"},
		{ "label_deleteObject","Remove object"},
		{ "label_saveReport","Save report"},
		{ "label_saveChanges","Save changes in current template?"},
		{ "label_confImportTemplates","Do you really want to import templates?"},
		{ "label_crossesExist","Template elements and labels shouldn't intersect with each other."},

		{ "label_report","Report "},
		{ "label_cantImpl"," couldn't be implemented."},
		{ "label_templatePiece","This template elements are to be implemented from other modules"},
		{ "label_poolObjNotExists","No data for implementing template's element "},
		{ "label_generalCRError","General error with creating visual implementation for template's element."},

		{ "label_chooseTemplateSize","Choose template's size:"},

//for ChartFrame
		{ "label_number","Number"},
		{ "label_period","Period"},
		{ "label_numberForPeriod","Number, for period"},
		{ "label_field","Field"},
		{ "label_value","Value"},
		{ "label_Intervalvalue","Input the quantum interval value"},

		{ "label_hour","Hour"},
		{ "label_day","Day"},
		{ "label_week","Week"},
		{ "label_month","Month"},
		{ "label_quarter","Quarter"},
		{ "label_year","Year"},


//for SelectReportsPanel
		{ "label_srw_title","Available documents"},

		{ "label_diagrProp","Chart's properties"},
		{ "label_templateExists","The template already exists. Replace?"},

		{ "label_repSboiSistemy","Отчёт по сбоям системы мониторинга"},///////////////////
		{ "label_repStat","Статистический отчёт"},///////////////////////////
		{ "label_repAnalysisResults","Analysis results"},
		{ "label_repSurveyResults","Survey results"},
		{ "label_repEvaluationResults","Evaluation results"},
		{ "label_repPredictionResults","Prediction results"},
		{ "label_repModelingResults","Modeling results"},
		{ "label_repOptimizationResults","Optimization results"},
		{ "label_repPhysicalScheme","Physical scheme"},
		{ "label_repTopologicalScheme","Topological scheme"},
		{ "label_repAbonentsPassports","Пасспорта абонентов"},//////////////////////
		{ "label_repMetrologicalPoverka","Отчёт о метрологической поверке"},/////////////////

		{ "label_availableReports","Available reports"},
		{ "label_availableTemplElems","Available template elements"},


//for FontChooserDialog
		{ "label_chooseFont","Font choosing"},
		{ "label_fontName","Name"},
		{ "label_fontSize","Size"},
		{ "label_fontExample","Example"},
		{ "label_fontBold","Bold"},
		{ "label_fontItalic","Italic"},

//all errors
		{ "error_noDataToDisplay","No data to display!"},
		{ "error_numb_req","The number value required!"},
		{ "error_fileNotAvailable","Couldn't write to the file!"},
		{ "error_emptyObjectReport","Objects' list report should contain at least one active column!"},

		{ "error_noTemplate","No template with such name"},

//for opimization model and tables
		{ "label_sourceData","Optimization source data"},
		{ "label_scheme","Scheme"},
		{ "label_topology","Topology"},
		{ "label_solution","Optimization solution"},
		{ "label_equipFeatures","Equipment's general features"},
		{ "label_optimizeResults","Optimization results"},
		{ "label_cost","Total cost"},

		{ "label_chooseParams","Choosing parameters"},

		{ "label_path","Path"},
		{ "label_fallValue","Оценочное attanuation, дБ"},////////////////////////////////////
		{ "error_noSourceInfo","No source data for scheme ID = "},

		{ "label_cost","Cost"},
		{ "label_reflectName","Reflectometer's name"},
		{ "label_dinamicDiapazon","Dynamic diapason"},
		{ "label_switchName","Switch's name"},
		{ "label_portsNumber","Ports' number"},
		{ "label_totalNodeCost","Total node's cost"},
		{ "label_totalSumm","TOTAL:"},

		{ "label_date","Date"},
		{ "label_koefZapasa","Коэффициент запаса"},////////////////
		{ "label_procentRandom","Процент рандомизации"},
		{ "label_stepenRandom","Степень рандомизации"},
		{ "label_verUdalRTU","Вероятность удаления RTU"},
		{ "label_verSozdRTU","Вероятность создания RTU"},
		{ "label_verObjedVolokon","Вероятность объединения волокон"},
		{ "label_verRazjedVolokon","Вероятность разъединения волокон"},
		{ "label_konserv","Консервативность"},///////////////////////

		{ "label_reflectChars","Reflectometers' features"},
		{ "label_switchChars","Switches' features"},

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

		{ "label_equipName","Equipment's name"},
		{ "label_equipClass","Equipment's class"},
		{ "label_equipMark","Марка"},///////
		{ "label_equipChars","Equipment's features"},
	};


	public LangModelReport_en()
	{
		symbols.setEras(new String [] {"н.э.","до н.э."});
		symbols.setMonths(new String [] {
			"january","february", "march",
			"april", "may", "june",
			"july", "august", "september",
				"october", "november", "december"});
		symbols.setShortMonths(new String [] {
			"jan","feb", "mar",
			"apr","may", "jun",
			"jul", "aug", "sep",
			"oct", "nov", "dec"});
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
