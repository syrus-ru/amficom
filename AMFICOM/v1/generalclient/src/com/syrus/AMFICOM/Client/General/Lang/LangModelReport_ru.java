//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ������ ��������� �������� �� ������� ����� ��� ���������   * //
// *         ���� ���������� ����� �� �������                             * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ���������� �.�.                                              * //
// *                                                                      * //
// * ������: 1.0                                                          * //
// * ��: 1 jul 2002                                                       * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelMain_ru.java                     * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Lang;



public class LangModelReport_ru extends LangModelReport
{
	static final Object[][] contents = {

//���� ��������
		{ "rtt_Evaluation","������� �� ������"},
		{ "rtt_Prediction","������� �� ���������������"},
		{ "rtt_Analysis","������� �� �������"},
		{ "rtt_Survey","������� �� ������������"},
		{ "rtt_Modeling","������� �� �������������"},
		{ "rtt_Optimization","������� �� �����������"},
		{ "rtt_AllTemplates","��� �������"},

//���� ������
		{ "rep_stat_po_polju","���������� �� ����"},
		{ "rep_graphic","������ - �������� ������"},
		{ "rep_gistogram","�����������"},
		{ "rep_diagr_pirog","��������� - ����� 3D"},
		{ "rep_diagr_pirog2D","��������� - �����"},
		{ "rep_stolb_diagr","���������� ��������� 3D"},
		{ "rep_stolb_diagr2D","���������� ���������"},
		{ "rep_spis_obj","����� \"������ ��������\""},

		{ "rep_equipChars","������������:��������������"},
		{ "rep_linkChars","����� �����:��������������"},
		{ "label_byFields","�� ����� "},
		{ "label_more","...(��� "},

//���� �������� and some additional
		{ "popup_priv_vert","�������� �� ���������"},
		{ "popup_priv_horiz","�������� �� �����������"},
		{ "popup_cancel_priv","�������� ��������"},

		{ "priv_templ_left","�������� � ����� ����� ���� �������"},
		{ "priv_templ_top","�������� � ������� ����� ���� �������"},
		{ "priv_obj_top","�������� � ������� ����� ������� ������"},
		{ "priv_obj_bottom","�������� � ������ ����� ������� ������"},
		{ "priv_obj_left","�������� � ����� ����� ������� ������"},
		{ "priv_obj_right","�������� � ������ ����� ������� ������"},

		{ "vybor_priv1","�������� ����� ������� ��� ��������"},
		{ "vybor_priv2","����� ������� ��������"},

//Report module labels and messages
//Common
		{ "label_confirm","�������������"},
		{ "label_error","������"},
		{ "label_cancel","������"},
		{ "label_choose","�������"},
		{ "label_change","��������"},
		{ "label_open","�������"},
		{ "label_close","�������"},
		{ "label_save","���������"},
		{ "label_delete","�������"},
		{ "label_print","�����������"},
		{ "label_add","��������"},
		{ "label_apply","���������"},
		{ "label_input","������"},
		{ "label_exit","�����"},

//for RenderingObject FirmedTextPane,ReportTemplatePanel,ReportTemplateImplementationPanel
		{ "label_lb","�������"},
		{ "label_ro","������� �������"},
		{ "label_im","��������"},

		{ "popup_font","�����"},
		{ "label_chooseReport","����� ������"},
		{ "popup_vertRazb","������ ���������� ��������� �� ���������"},

		{ "label_noSchemeSet","�� ������� �����!"},
		{ "label_noselectedTopology","��������� �� �������!"},
		{ "label_chooseDialog","�����"},
		{ "label_vertRazb","������� ���������� ������������ ��������� �������"},
		{ "label_selectedReports","��������� ������"},
		{ "label_availableReports","��������� ���������"},
		{ "label_emptyReport","����� ����!"},
		{ "label_allFiles","��� �����"},
		{ "label_htmlFiles","����� HTML"},
		{ "label_GIFFiles","����� GIF"},
		{ "label_JPGFiles","����� JPG"},

		{ "label_fileExists","���� � ����� ������ ����������. ��������?"},
		{ "label_noSelectedSolution","������� �� ����������� �� �������!"},

		{ "label_rtbWindowTitle","�������� ������� ������"},
		{ "label_templateScheme","����� �������"},
		{ "label_reportForTemplate","����� �� �������"},

		{ "label_newTemplate","������� ����� ������"},
		{ "label_saveTemplate","���������� �������"},
		{ "label_openTemplate","�������� �������"},
		{ "label_importTemplate","������ ��������"},
		{ "label_exportTemplate","������� ��������"},
		{ "label_viewTemplatesScheme","����������� ����� �������"},
		{ "label_viewReport","����������� �����"},

		{ "label_addLabel","�������� �������"},
		{ "label_addImage","�������� ��������"},
		{ "label_deleteObject","������� ������"},
		{ "label_saveReport","��������� �����"},
		{ "label_saveChanges","��������� ��������� � ������� �������?"},
		{ "label_confImportTemplates","�� ������������� ������ ����������� ������ ��������?"},
		{ "label_crossesExist","������� � ������� �� ����� ������� �� ������ ������������."},

		{ "label_report","����� "},
		{ "label_cantImpl"," �� ����� ���� ����������."},
		{ "label_templatePiece","�������� ������� ������������� ��� ���������� �� ������ �������"},
		{ "label_poolObjNotExists","�� ���������� ������� ��� ���������� ������ "},
		{ "label_generalCRError","������������� ������ ��� �������� ���������� ���������� ��� ������."},

		{ "label_chooseTemplateSize","�������� ������ �������:"},

//for ChartFrame
		{ "label_number","����������"},
		{ "label_period","������"},
		{ "label_numberForPeriod","����������, �� ������"},
		{ "label_field","��������"},
		{ "label_value","��������"},
		{ "label_Intervalvalue","������� �������� ��������� ��� ����������"},

		{ "label_hour","���"},
		{ "label_day","����"},
		{ "label_week","������"},
		{ "label_month","�����"},
		{ "label_quarter","�������"},
		{ "label_year","���"},


//for SelectReportsPanel
		{ "label_srw_title","����� �������� ������"},

		{ "label_diagrProp","�������� ���������"},
		{ "label_templateExists","������ � ����� ������ ����������. ��������?"},

		{ "label_repSboiSistemy","����� �� ����� ������� �����������"},
		{ "label_repStat","�������������� �����"},
		{ "label_repAnalysisResults","���������� �������"},
		{ "label_repSurveyResults","���������� ������������"},
		{ "label_repEvaluationResults","���������� ������"},
		{ "label_repPredictionResults","���������� ���������������"},
		{ "label_repModelingResults","���������� �������������"},
		{ "label_repOptimizationResults","���������� �����������"},
		{ "label_repPhysicalScheme","���������� �����"},
		{ "label_repTopologicalScheme","�������������� �����"},
		{ "label_repAbonentsPassports","��������� ���������"},
		{ "label_repMetrologicalPoverka","����� � ��������������� �������"},

		{ "label_availableReports","��������� ������"},
		{ "label_availableTemplElems","��������� �������� �������"},


//for FontChooserDialog
		{ "label_chooseFont","����� ������"},
		{ "label_fontName","��������"},
		{ "label_fontSize","������"},
		{ "label_fontExample","������"},
		{ "label_fontBold","������"},
		{ "label_fontItalic","������"},

//all errors
		{ "error_noDataToDisplay","��� ������ ��� �����������!"},
		{ "error_numb_req","������� �������� ��������!"},
		{ "error_fileNotAvailable","���� ���������� ��� ������!"},
		{ "error_emptyObjectReport","����� �� ������ �������� ������ ��������� ���� �� ���� �������� �������!"},

		{ "error_noTemplate","������ � ����� ������ �� ����������!"},

//for opimization model and tables
		{ "label_sourceData","�������� ������ �� �����������"},
		{ "label_scheme","�����"},
		{ "label_topology","���������"},
		{ "label_solution","������� �� �����������"},
		{ "label_equipFeatures","�������� �������������� ������������"},
		{ "label_optimizeResults","���������� �����������"},
		{ "label_cost","������ ���������"},

		{ "label_chooseParams","����� ����������"},

		{ "label_path","����"},
		{ "label_fallValue","��������� ���������, ��"},
		{ "error_noSourceInfo","��� �������� ���������� �� ����������� ��� ����� ID = "},

		{ "label_cost","����"},
		{ "label_reflectName","��� �������������"},
		{ "label_dinamicDiapazon","������������ ��������"},
		{ "label_switchName","��� ������"},
		{ "label_portsNumber","���������� ������"},
		{ "label_totalNodeCost","��������� ��������� ����"},
		{ "label_totalSumm","�����:"},

		{ "label_date","����"},
		{ "label_koefZapasa","����������� ������"},
		{ "label_procentRandom","������� ������������"},
		{ "label_stepenRandom","������� ������������"},
		{ "label_verUdalRTU","����������� �������� RTU"},
		{ "label_verSozdRTU","����������� �������� RTU"},
		{ "label_verObjedVolokon","����������� ����������� �������"},
		{ "label_verRazjedVolokon","����������� ������������ �������"},
		{ "label_konserv","����������������"},

		{ "label_reflectChars","�������������� ��������������"},
		{ "label_switchChars","�������������� �������"},

//for opimization model and tables

/*//for evaluation model and tables
		{ "label_testParams","��������� �����"},
		{ "label_commonInfo","����� ����������"},
		{ "label_markersData","������ �� ��������"},
		{ "label_analysisParams","��������� �������"},
		{ "label_reflectogram","��������������"},
		{ "label_mask_view","��� �����"},
		{ "label_commonChars","�������� ��������������"},
		{ "label_addChars","�������������� ��������������"},
		{ "label_mask_type","��� � ��������� �����"},
//for evaluation model and tables*/

		{ "label_equipName","�������� ������������"},
		{ "label_equipClass","����� ������������"},
		{ "label_equipMark","�����"},
		{ "label_equipChars","�������������� ������������"},
	};


	public LangModelReport_ru()
	{
		symbols.setEras(new String [] {"�.�.","�� �.�."});
		symbols.setMonths(new String [] {
			"������","�������", "����",
			"������", "���", "����",
			"����", "������", "��������",
				"�������", "������", "�������"});
		symbols.setShortMonths(new String [] {
			"���","���", "���",
			"���","���", "���",
			"���", "���", "���",
			"���", "���", "���"});
//		symbols.setWeekDays(new String [] {"�����������", "�������",
//				"�����", "�������", "�������", "�������", "�����������"} );
//		symbols.setShortWeekDays(new String [] {"��", "��", "��", "��",
//				"��", "��", "��"} );
	}

	public Object[][] getContents()
	{
		return contents;
	}
}
