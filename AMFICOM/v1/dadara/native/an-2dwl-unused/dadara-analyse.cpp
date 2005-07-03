#include <assert.h>
#include <jni.h>
#include <windows.h> // for APIENTRY #define
#include <math.h>

#include "../Common/com_syrus_AMFICOM_analysis_AnalysisManager.h"
#include "../Common/com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent.h"
#include "../Common/EventP.h"
#include "../Common/Java-bridge.h"
#include "Analyse.h"

#include "../Common/prf.h"

#include "group.h"
#include "findLength.h"

//#define InEvent Event

// #define DEBUG_DADARA_ANALYSE

/*
#ifdef DEBUG_DADARA_ANALYSE
	#ifndef _WIN32
		#include <sys/time.h>
		#include <time.h>
	#endif
	FILE* dbg_stream;
#endif
*/

JNIEXPORT jobjectArray JNICALL 
Java_com_syrus_AMFICOM_analysis_AnalysisManager_analyse2(
	JNIEnv* env,
	jclass obj,
	jint waveletType,             //type of the WaveLet transformation applied.
	jdoubleArray y,               //the refl. itself
	jdouble delta_x,              //dx
	jdouble connFallParams,       // Param. to descr. the behav. of conn. at fall
	jdouble min_level,            // ?
	jdouble max_level_noise,      // ?
	jdouble min_level_to_find_end,// ? 
	jdouble min_weld,             // ?
	jdouble min_connector,
	jdoubleArray ret_meanAttenuation,
	jint reflectiveSize, 
	jint nonReflectiveSize)
{

/*
#ifdef DEBUG_DADARA_ANALYSE

	#ifdef _WIN32
		dbg_stream = fopen("c:\\dadara-analyse.log", "a");
		setvbuf(dbg_stream, 0, 0, _IONBF); // no buffering
		fprintf (dbg_stream, "# logfile opened in AnalysisManager_analyse\n");
	#else
		timeval tv;
		gettimeofday(&tv, NULL);
		tm* t = localtime(&tv.tv_sec);
		const int size = 64; // was: 9 + 6 + 1 + 14 + 1 + 3 + 1 ...
		char* filename = new char[size];
		sprintf(filename, ".//logs//%04d%02d%02d%02d%02d%02d-dadara-analyse.log", 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
		filename[size - 1] = 0;
		dbg_stream = fopen(filename, "a");
		delete[] filename;
	#endif

	fprintf (dbg_stream, "\t""waveletType %d\n", (int)waveletType);
	fprintf (dbg_stream, "\t""delta_x %g\n", (double)delta_x);
	fprintf (dbg_stream, "\t""connFallParams %g\n", (double)connFallParams);
	fprintf (dbg_stream, "\t""min_level %g\n", (double)min_level);
	fprintf (dbg_stream, "\t""max_level_noise %g\n", (double)max_level_noise);
	fprintf (dbg_stream, "\t""min_level_to_find_end %g\n", (double)min_level_to_find_end);
	fprintf (dbg_stream, "\t""min_weld %g\n", (double)min_weld);
	fprintf (dbg_stream, "\t""min_connector %g\n", (double)min_connector);
	//fprintf (dbg_stream, "\t""strategy %d\n", (int)strategy);
#endif
*/
	prf_b("AnalysisManager_analyse2: enter");
	// берем в JNI массив y-значений (его надо будет потом освободить)
	double* data    = (double*)env->GetDoubleArrayElements(y, NULL);
	jsize sz = env->GetArrayLength(y);
	int data_l = sz;
/*
#ifdef DEBUG_DADARA_ANALYSE
	fprintf (dbg_stream, "\tsz %d\n", (int)sz);
	if (0)
	{
		int i;
		for (i=0; i<sz; i++) fprintf (dbg_stream, "%g ", (double)data[i]);
		fprintf (dbg_stream, "\n");
	}
#endif
*/
	// корректируем параметры
	if (min_level <= 0.01)
		min_level =  0.01;		// typ. 0.04
	if (max_level_noise < 0.05)
		max_level_noise = 0.05;
	if (max_level_noise > 5.)
		max_level_noise = 5.;	// typ. 2.0  (input was=5)
	if (connFallParams > 0.49)
		connFallParams = 0.49;	// typ. 0.3
	if (connFallParams < 0.001)
		connFallParams = 0.001;
	if (min_weld < 0.01)
		min_weld = 0.01;		// typ. 0.4
	if (min_connector < 0.05)
		min_connector = 0.05;	// typ. 0.2
	if (min_level_to_find_end < 1.)
		min_level_to_find_end = 1.; //  typ. 6.0

	// определяем длину рефлектограммы
	int rg_len = find_reflectogram_length(data, data_l);

	// Первичный анализ - определение "скачков"

	ArrList al0;

	prf_b("AnalysisManager_analyse2: analyse_fill_events()");

	analyse_fill_events(data, rg_len, al0);
	// после заполнения al0, для освобождения памяти надо будет сделать al0.disposeAll();

	// Исправляем перекрывающиеся события, дополняем  промежутки линейными участками

	// Переносим объекты из al0 в al1, исправляя их и оставляя на их месте в al0 NULL-ссылки;
	// Добавляем новые объекты в al1.
	// после этого освобождение памяти можно сделать как al0.disposeAll(); al1.disposeAll();

	int i;

	int nEvents;
	EventP *re;

	//*

	prf_b("AnalysisManager_analyse2: my-process events");

	{
		ArrList ali;
		int len = al0.getLength();
		
		int ofs = 1;
		InEvent *ie = new InEvent[len + ofs];
		assert(ie);

		// вставляем событие начала р/г
		ie[0].initAsRgStart();

		// типа, значит, копируем структуры
		for (i = 0; i < len; i++)
		{
			ie[i + ofs] = *(InEvent *)al0[i];
		}

		// группируем события
		group_events(ie, len, ali);

		// окончательно сортируем по координате
		ali.qsort(OutEvent::fcmp_begin);

		delete ie;

		// находим последнее отражение - это и будет конец волокна

		len = ali.getLength();
		if(0) //FIXIT
			while (len > 0)
		{
			OutEvent &ev = *(OutEvent*)ali[len - 1];
			if (ev.oet == OET_CONNECTOR_OR_EOF)
				break;
			else
				len--;
		}

		// распознали события. вставляем линейные участки
		ArrList al2;
		int last_end = 0;
		for (i = 0; i < len; i++)
		{
			OutEvent &ev = *(OutEvent*)ali[i];
			fprintf(stderr, "OE[%d] (%d:%d-%d)",
				i, ev.oet, ev.begin, ev.end);
			//assert (ev.begin >= last_end); // ?? возможно, это не страшно
			if (ev.begin < last_end)
			{
				fprintf(stderr, "- overlapped with ...%d)", last_end);
			}
			fprintf(stderr, "\n");
			if (ev.begin > last_end)
			{
				int begin = last_end;
				int end = ev.begin;
				OutEvent *le = new OutEvent;
				le->oet = OET_LINEAR;
				le->begin = begin;
				le->end = end;
				le->front = begin;
				le->tail = end;
				al2.add(le);
			}
			last_end = ev.end;
			al2.add(&ev); // само событие
			ali.set(i, 0);
		}

		fprintf(stderr, "recogined %d nonlinear events, totally %d events\n",
			ali.getLength(), al2.getLength());

		ali.disposeAll();

		//

		nEvents = al2.getLength();

		// create and fill EventP (begin/end may overlap after that) // NB: FIXIT: возможно нулевое кол-во элементов - исправить; это допускается тогда и только тогда когда на входе пустая р/г

		re = new EventP[nEvents];
		assert(re);

		for (i = 0; i < nEvents; i++)
		{
			int j = i;
			OutEvent &e1 = *(OutEvent*) al2[i];
			switch(e1.oet)
			{
			case OET_LINEAR:
				re[j].begin = e1.begin;
				re[j].end = e1.end;
				re[j].delta_x = delta_x;
				re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_LINEAR;
				re[j].mf.entry = MF_ID2entry(MF_ID_LIN);
				re[j].mf.pars[0] = 30; // XXX
				re[j].mf.pars[1] = 0; // XXX
				break;
			case OET_SPLICE:
				re[j].begin = e1.begin;
				re[j].end = e1.end;
				re[j].delta_x = delta_x;
				re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_WELD;
				re[j].mf.entry = MF_ID2entry(MF_ID_SPL1);
				re[j].mf.pars[0] = 0; // лин. параметр
				re[j].mf.pars[1] = 0; // лин. параметр
				re[j].mf.pars[2] = 1; // XXX
				re[j].mf.pars[3] = (e1.front + e1.tail) / 2;
				re[j].mf.pars[4] = (e1.tail - e1.front) / 2;
				break;
			case OET_CONNECTOR_OR_EOF: // fall through
			case OET_CONNECTOR_NOT_EOF:
				re[j].begin = e1.begin;
				re[j].end = e1.end;
				re[j].delta_x = delta_x;
				re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_CONNECTOR;
				re[j].mf.entry = MF_ID2entry(MF_ID_CON1c);
				re[j].mf.pars[0] = 0; // лин. параметр
				re[j].mf.pars[1] = 0; // лин. параметр
				re[j].mf.pars[2] = 0; // лин. параметр
				re[j].mf.pars[3] = e1.front;
				re[j].mf.pars[4] = e1.tail;
				re[j].mf.pars[5] = 1; // XXX
				re[j].mf.pars[6] = 1; // XXX
				re[j].mf.pars[7] = 100; // XXX
				re[j].mf.pars[8] = 1; // XXX
				//re[j].mf.pars[7] = 0; // лин. параметр
				break;
			case OET_UNKNOWN:
				re[j].begin = e1.begin;
				re[j].end = e1.end;
				re[j].delta_x = delta_x;
				//re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_LINEAR;
				re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_SINGULARITY;
				//re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_WELD;
				re[j].mf.entry = MF_ID2entry(MF_ID_LIN);
				re[j].mf.pars[0] = 0; // лин. параметр
				re[j].mf.pars[1] = 0; // лин. параметр
				break;
			default:
				assert(0); // XXX
			}
		}
		
		al2.disposeAll();
	}

	/*/

	typedef InEvent Event;

	prf_b("AnalysisManager_analyse2: process events");

	ArrList al1;

	for (i = 0; i < al0.getLength(); i++)
	{
		int prev_end = al1.getLength() > 0 // конец последнего элемента al1 либо 0
			? ((Event *)al1[al1.getLength() - 1])->end
			: 0;
		Event &al0cur = *(Event *)al0[i];
		fprintf(stderr, "process event[%d] val %.3f (%d-%d) prev_end=%d%s\n",
			i, al0cur.value, al0cur.begin, al0cur.end, prev_end,
			al0cur.dup ? " dup" : "");
		if (al0cur.begin < 0)
			al0cur.begin = 0;
		if (al0cur.end < 0)
			al0cur.end = 0;
		if (al0cur.end == al0cur.begin)
			continue;
		assert(al0cur.end > al0cur.begin);
		if (al0cur.begin > prev_end)
		{	// вставляем линейный участок; в т.ч. перед началом р/г
			int begin = prev_end;
			int end = al0cur.begin;
			Event *ev = new Event;
			ev->type = Event_Type_LINEAR;
			ev->x0 = begin / 2; // середину устанавливаем на начало
			ev->scale = 0; // unapplicable for LINEAR
			ev->value = 0; // -//-
			ev->begin = begin;
			ev->end = end;
			al1.add(ev);
		}
		else if (al0cur.begin < prev_end)
		{	// исправляем перекрытие (известно что центр нового события правее чем у старого)
			if (al1.getLength()) // should be always true; проверка оставлена на случай ошибки
			{
				int &prev_center = ((Event *)al1[al1.getLength() - 1])->x0;
				int pos = (prev_center + al0cur.begin) / 2;
				prev_center = pos;
				al0cur.begin = prev_center;
			}
			else
			{
				// XXX: сгенерировать warning для разработчика
			}
		}
		// переносим объект из al0 в al1
		al1.add(&al0cur);
		al0.set(i, 0);
	}

	// освобождаем неиспользованные элементы al0
	al0.disposeAll();

	// calc number of resulting events

	nEvents = al1.getLength();

	// create and fill EventP (begin/end may overlap after that) // NB: FIXIT: возможно нулевое кол-во элементов - исправить; это допускается тогда и только тогда когда на входе пустая р/г

	re = new EventP[nEvents];
	assert(re);

	for (i = 0; i < nEvents; i++)
	{
		int j = i;
		Event &e1 = *(Event*) al1[i];
		switch(e1.type)
		{
		case Event_Type_SOMETHING: // raw event
			re[j].begin = e1.begin;
			re[j].end = e1.end;
			re[j].delta_x = delta_x;
			re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_WELD;
			re[j].mf.entry = MF_ID2entry(MF_ID_SPL1);
			re[j].mf.pars[0] = 30; // XXX
			re[j].mf.pars[1] = 0; // XXX
			re[j].mf.pars[2] = 1; // XXX
			re[j].mf.pars[3] = e1.x0;
			re[j].mf.pars[4] = e1.scale / 3;
			break;
		case Event_Type_LINEAR: // linear
			re[j].begin = e1.begin;
			re[j].end = e1.end;
			re[j].delta_x = delta_x;
			re[j].gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_LINEAR;
			re[j].mf.entry = MF_ID2entry(MF_ID_LIN);
			re[j].mf.pars[0] = 30; // XXX
			re[j].mf.pars[1] = 0; // XXX
			break;
		default:
			assert(0); // XXX
		}
	}

	fprintf( stderr, "dadara-analyse: al.getLength %d nEvents %d\n", al1.getLength(), nEvents);
	fflush(stderr);

	prf_b("AnalysisManager_analyse2: post-process");

	// освобождаем al1
	al1.disposeAll();

	//*/

/*
#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "ia->getEventsCount = %d\n", nEvents);
#endif
*/

	for (i = 0; i < nEvents; i++)
	{
		//EPold2EPnew(ep[i], re[i], delta_x);

		FILE *dbg_stream = stderr;

		fprintf (dbg_stream, "event [%d]: begin %d end %d gentype %d mf.entry %d\n", i, re[i].begin, re[i].end, re[i].gentype, re[i].mf.entry);

		//int k;
		//for (k = 0; k < MF_entry2npars(re[i].mf.entry); k++)
		//	fprintf(dbg_stream, "\t""pars[%d]: %g\n", k, re[i].mf.pars[k]);

		//fprintf (dbg_stream, "\t""Y0 %g\n", MF_calc_fun(re[i].mf.entry, re[i].mf.pars, re[i].begin));
		//fprintf (dbg_stream, "\t""Y1 %g\n", MF_calc_fun(re[i].mf.entry, re[i].mf.pars, re[i].end));
	}

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# converted to Cnew\n");
#endif

	fprintf(stderr, "invoking EventP_C2J_arr with re %p nEvents %d\n", re, nEvents); //XXX

	jobjectArray ret_obj = EventP_C2J_arr(env, re, nEvents, stderr);

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# converted to Java, ret_obj = %p\n", ret_obj);
#endif

	fprintf(stderr, "dadara-analyse: freeing re\n");
	fflush(stderr);

	delete[] re;

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# re freed\n");
#endif
	// send to JNI
	double meanAtt = 0; //ia->getMeanAttenuation(); // FIXIT

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "returning to java %d events, meanAtt=%g \n", nEvents, meanAtt);
#endif

	jsize len = (env)->GetArrayLength(ret_meanAttenuation);
	if (len)
	{
		jdouble *body = (env)->GetDoubleArrayElements(ret_meanAttenuation, 0);
		body[0] = meanAtt;
		(env)->ReleaseDoubleArrayElements(ret_meanAttenuation, body, 0); // copy-back
	}

//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# logfile closed\n");
	fclose( dbg_stream );
#endif

	fprintf(stderr, "dadara-analyse: done\n");
	fflush(stderr);

	prf_b("AnalysisManager_analyse2: done");

	prf_print(stderr);
	prf_e();

	return ret_obj;
}

