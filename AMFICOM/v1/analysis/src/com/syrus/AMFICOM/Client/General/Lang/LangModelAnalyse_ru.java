package com.syrus.AMFICOM.Client.General.Lang;

public class LangModelAnalyse_ru  extends LangModelAnalyse
{

	public Object[][] contents = {

		{ "language", "Русский" },
		{ "country", "Россия" },

		{ "eLanguage", "Russian" },
		{ "eCountry", "Russia" },

		{ "AnalyseTitle", "Анализ АМФИКОМ"},
		{ "AnalyseExtTitle", "Исследование АМФИКОМ"},
		{ "ThresholdsTitle", "Оценка АМФИКОМ"},

// Menu text
		{ "menuFileText", "Файл" },
		{ "menuFileOpenText", "Открыть" },
		{ "menuFileOpenAsText", "Открыть как" },
		{ "menuFileOpenAsBellcoreText", "Bellcore" },
		{ "menuFileOpenAsWavetekText", "Wavetek" },
		{ "menuFileSaveText", "Сохранить Bellcore" },
		{ "menuFileSaveAllText", "Сохранить все" },
		{ "menuFileSaveAsText", "Сохранить как" },
		{ "menuFileSaveAsTextText", "Текстовый файл" },
		{ "menuFileCloseText", "Закрыть все" },
		{ "menuFileAddCompareText", "Добавить в сравнение" },
		{ "menuFileRemoveCompareText", "Убрать из сравнения" },
		{ "menuExitText", "Выход" },

		{ "menuAnalyseUploadText", "Сохранить анализ" },
		{ "menuAnalyseSaveCriteriaText", "Сохранить критерии" },

		{ "menuTraceText", "Рефлектограмма" },
		{ "menuTraceDownloadText", "Загрузить" },
		{ "menuTraceDownloadEtalonText", "Сравнить с эталоном" },
		{ "menuTraceCloseEtalonText", "Убрать эталон" },
		{ "menuTraceAddCompareText", "Добавить в сравнение" },
		{ "menuTraceRemoveCompareText", "Убрать из сравнения" },

		{ "menuSaveThresholdsText", "Сохранить пороги" },
		{ "menuSaveEtalonText", "Сохранить эталон"},
		{ "menuCreateTestSetupText", "Новый "},

		{ "menuSaveTestSetupText", "Сохранить"},
		{ "menuSaveTestSetupAsText", "Сохранить как..."},
		{ "menuLoadTestSetupText", "Загрузить"},
		{ "menuTestSetupText", "Шаблон"},
		{ "menuNetStudyText" , "Обучение сети"},

		{ "menuOptionsText", "Настройка"},
		{ "menuOptionsColorText", "Цвета"},

		{ "menuReportText", "Отчет"},
		{ "menuReportCreateText", "Создать отчет"},

		{ "menuWindowText", "Окно"},
		{ "menuWindowArrangeText", "Упорядочить окна"},
		{ "menuWindowTraceSelectorText", "Рефлектограмма"},
		{ "menuWindowPrimaryParametersText", "Параметры теста"},
		{ "menuWindowOverallStatsText", "Общая информация"},
		{ "menuWindowNoiseFrameText", "Уровень шумов"},
		{ "menuWindowFilteredFrameText", "Фильтрация шума"},
		{ "menuWindowEventsText", "Основные характеристики"},
		{ "menuWindowDetailedEventsText", "Доп. характеристики"},
		{ "menuWindowAnalysisText", "Анализ рефлектограммы"},
		{ "menuWindowMarkersInfoText", "Данные по маркерам"},
		{ "menuWindowAnalysisSelectionText", "Параметры анализа"},
		{ "menuWindowDerivHistoFrameText", "Гистограмма"},
		{ "menuWindowThresholdsSelectionText", "Тип и параметры маски"},
		{ "menuWindowThresholdsText", "Вид маски"},

// Menu tooltips
		{ "menuTraceDownloadToolTip", "Открыть из БД" },
		{ "menuTraceAddCompareToolTip", "Добавить в сравнение из БД" },
		{ "menuTraceRemoveCompareToolTip", "Убрать из сравнения" },

		{ "menuFileOpenToolTip", "Открыть из файла" },
		{ "menuFileCloseToolTip", "Закрыть все" },
		{ "menuFileAddCompareToolTip", "Добавить в сравнение из файла" },
		{ "menuFileRemoveCompareToolTip", "Убрать из сравнения" },

// errors
		{ "error", "Ошибка" },
		{ "warning", "Предупреждение" },
		{ "unkError", "Неожиданная ошибка" },
		{ "noSessionError", "Сессия не установлена" },
		{ "noAccessToThresholds", "Недостаточно прав для работы с модулем оценки" },
		{ "noMonitoredElementError", "Маршрут тестирования не задан" },
		{ "noTestSetupError", "Шаблон не задан" },
		{ "noEtalonError", "Эталон не задан" },
		{ "noAnalysisError", "Не проведен анализ рефлектограммы" },
		{ "noTestArgumentsError", "Аргументы тестирования не заданы" },

// buttons
		{ "okButton", "OK" },
		{ "cancelButton", "Отмена" },
		{ "refreshButton", "Обновить" },

// strings
		{ "trace", "Рефлектограмма" },
		{ "testsetup", "Шаблон" },
		{ "analysis", "Анализ" },
		{ "newname", "Название:" },


//////////////////////////////////////////////////////////
		{ "encreasex", "растянуть по горизонтали" },
		{ "encreasey", "растянуть по вертикали" },
		{ "decreasex", "сжать по горизонтали" },
		{ "decreasey", "сжать по вертикали" },
		{ "fittoscreen", "выровнять по размеру окна" },

		{ "centerA", "на маркер A" },
		{ "centerB", "на маркер B" },

		{ "encreasetx", "увеличить допустимую протяженность" },
		{ "decreasetx", "уменьшить допустимую протяженность" },
		{ "encreasety", "увеличить допустимую амплитуду" },
		{ "decreasety", "уменьшить допустимую амплитуду" },
		{ "fittoevent", "выровнять по размеру события" },
		{ "reflectionanalyse", "анализ отражения" },
		{ "lossanalyse", "анализ потерь" },
		{ "noanalyse", "без анализа" },
		{ "fixmarkers", "фиксировать маркеры" },
		{ "showevents", "отображать события" },
		{ "showmodel", "отображать модель" },
		{ "addmarker", "установить маркер" },
		{ "removemarker", "удалить маркер" },
		{ "bindToMarker", "привязать к маркерам" },
		{ "allThresholds", "показать все пороги" },

// warning messages
		{ "messageFileChanged", "Файл был изменен. Сохранить?" },
		{ "messageError", "Ошибка" },
		{ "messageReadError", "Ошибка чтения файла." },
		{ "messageFileAlreadyLoaded", "Файл уже загружен. Перезагрузить с диска?" },

// analysis frame text constants
		{ "analysisTitle", "Анализ рефлектограммы" },

// parameters frame text constants
		{ "parametersTitle", "Параметры теста" },
		{ "parametersKey", "" },
		{ "parametersValue", "" },
// selection frame text constants
		{ "selectorTitle", "Рефлектограмма" },
		{ "selectorKey", "" },//Рефлектограмма
		{ "selectorValue", "" },//Цвет

		{ "module id", "Оптический модуль" },
		{ "wavelength", "Длина волны" },
		{ "pulsewidth", "Длительность импульса" },
		{ "groupindex", "Коэфф. преломления" },
		{ "averages", "Число усреднений" },
		{ "resolution", "Разрешение" },
		{ "range", "Длина" },
		{ "time", "Время получения" },
		{ "date", "Дата получения" },
		{ "backscatter", "Коэфф. обр. рассеяния" },
		{ "mt", "м" },
		{ "km", "км" },
		{ "ns", "нс" },
		{ "nm", "нм" },
		{ "dB", "дБ" },
		{ "dB/km", "дБ/км" },
		{ "dBkm", "дБ·км" },

// events frame text constants
		{ "eventTableTitle", "Основные характеристики" },
		{ "eventNum", "N" },
		{ "eventType", "Тип события" },
		{ "eventTypeUnk", "Неидинифицированный" },
		{ "eventType2", "Мертвая зона" },
		{ "eventType0", "Линейный уч-к" },
		{ "eventType1", "Неидентиф." },
		{ "eventType4", "Коннектор" },
		{ "eventType3", "Сварное соед." },
		{ "eventType5", "Конец волокна" },
		{ "eventType6", "Шум" },
		{ "eventType7", "Область нагрузки" },

		{ "etEventType", "Тип эталона" },
		{ "maxDeviation", "Макс. отклонение" },
		{ "meanDeviation", "Ср. отклонение" },
		{ "delta", "Разность" },
		{ "dWidth", "Изм. ширины" },
		{ "dLocation", "Изм. локализации" },

		{ "traceLength", "Длина рефлектограммы" },
		{ "etLength", "Длина эталона" },

		{ "histogrammTitle", "Гистограмма" },
		{ "eventStartLocationKM", "Дистанция (км)" },
		{ "eventReflectanceDB", "Отражение (дБ)" },
		{ "eventLossDB", "Потери (дБ)" },
		{ "eventLeadAttenuationDBKM", "Затухание (дБ/км)" },
		{ "eventLengthKM", "Протяж.(км)" },
		{ "eventDetailedTableTitle", "Доп. характеристики" },
		{ "eventLength", "Протяженность" },
		{ "eventDetailedParam", "" },
		{ "eventDetailedValue", "" },
		{ "eventStartLevel", "Уровень начала" },
		{ "eventEndLevel", "Уровень конца" },
		{ "eventFade", "Затухание" },
		{ "eventRMSDeviation", "Ср. кв. отклонение" },
		{ "eventMaxDeviation", "Макс. отклонение" },
		{ "eventCurveFactor", "Коэфф. кривизны" },
		{ "eventAmplitude", "Амплитуда" },
		{ "eventPoLevel", "Уровень Po" },
		{ "eventEDZ", "EDZ" },
		{ "eventADZ", "ADZ" },
		{ "eventMaxLevel", "Макс. уровень" },
		{ "eventMinLevel", "Мин. уровень" },
		{ "eventReflectionLevel", "Уровень отражения" },
		{ "eventLoss", "Потери" },
		{ "eventFormFactor", "Формфактор" },

		// overall stats text constants
		{ "overallTitle", "Общая информация" },
		{ "overallKey", ""},
		{ "overallValue", ""},

		{ "totalLength", "Общая длина"},
		{ "totalLoss", "Общие потери"},
		{ "totalAttenuation", "Общее затухание"},
		{ "totalReturnLoss", "Возвратные потери"},
		{ "totalNoiseLevel", "Уровень шумов"},
		{ "totalNoiseDD", "ДД по шуму"},
		{ "totalEvents", "Число событий"},

// thresholds frame
		{ "thresholdsTitle", "Вид маски"},
		{ "thresholdsTableTitle", "Тип и параметры маски"},
		{ "thresholdsKey", "Порог"},
		{ "thresholdsConnector", "Отражательное событие"},
		{ "thresholdsWeld", "Неотражательное событие"},
		{ "thresholdsLinear", "Линейный участок"},
		{ "thresholdsAmplitude", "Амплитуда"},
		{ "thresholdsWidth", "Ширина"},
		{ "thresholdsHeight", "Высота"},
		{ "thresholdsCenter", "Середина"},
		{ "thresholdsUpAlarm", "Верхний критический"},
		{ "thresholdsUpWarning", "Верхний предупредительный"},
		{ "thresholdsDownAlarm", "Нижний критический"},
		{ "thresholdsDownWarning", "Нижний предупредительный"},

		// analysis selction parameters
		{ "analysisSelectionTitle", "Параметры анализа"},
		{ "analysisSelectionKey", ""},
		{ "analysisSelectionValue", ""},
		{ "analysisMinConnector", "Мин. амплитуда отражательного события (дБ)"},
		{ "analysisMinWeld", "Мин. амплитуда неотражательного события (дБ)"},
		{ "analysisMinEnd", "Мин. амплитуда отражения в конце волокна (дБ)"},
		{ "analysisMinEvent", "Порог чувствительности распознавания (дБ)"},
		{ "analysisNSigma", "Максимальный уровень шума (дБ)"},
		{ "analysisFormFactor", "Формфактор коннектора"},
		{ "analysisStrategy", "Степень аппроксимации"},
		{ "analysisWavelet", "Метод идентификации"},
		{ "strategy-1", "отсутствует" },
		{ "strategy0", "грубая" },
		{ "strategy1", "приблизительная" },
		{ "strategy2", "средняя" },
		{ "strategy3", "хорошая" },
		{ "strategy4", "с мин. ошибкой" },

		{ "analysisStart", "Провести анализ"},
		{ "analysisInitial", "Восстановить исходные"},
		{ "analysisDefaults", "Значения по умолчанию"},

		{ "noiseTitle", "Уровень шумов" },

		{ "filteredTitle", "Фильтрация шума" },

		// markers info text constants
		{ "markerInfoTitle", "Данные по маркерам" },
		{ "markerInfoKey", ""},
		{ "markerInfoValue", ""},

		{ "markerAPos", "Маркер A"},
		{ "markerALoss", "Потери в A"},
		{ "markerAReflection", "Отражение в A"},
		{ "markerAAttenuation", "Затухание в A"},
		{ "markerACumloss", "Общие потери в A"},
		{ "markerBPos", "Маркер B"},
		{ "markerABdist", "Дистанция A-B"},
		{ "markerBAdist", "Дистанция B-A"},
		{ "markerABloss", "2-точ. потери"},
		{ "markerABatt", "2-точ. затухание"},
		{ "markerABorl", "2-точ. ORL"},
		{ "markerABlsaatt", "Аппрокс. затухание"},

		//histoanalysis
		{ "histolevel", "Уровень:"},
		{ "histowidth", " Ширина (дБ/соб.):"},


// status bar constants
		{ "statusReady", "Ожидание" },
		{ "statusNoUser", " " },
	};

	public LangModelAnalyse_ru()
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