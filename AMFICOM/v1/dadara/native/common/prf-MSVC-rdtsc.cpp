#include <time.h>
#include <stdio.h>
#include <assert.h>
#include "prf.h"

#define USE_clock 1
#define USE_rdtsc 1

static int clock_cur;
static __int64 clock_cur_64;
static int cur_id = -1;

const int debug_to_console = 0;
const int dump_regularly = 0;

#if USE_clock
const int clock_loop_print = CLK_TCK * 5;
static time_t clock_at_next_print = clock_loop_print;
#else
const __int64 clock_loop_print = (__int64) 2e9;
static __int64 clock_at_next_print = clock_loop_print;
#endif

#if USE_rdtsc
__int64 rdtsc()
{
__int64 v = 0;
__asm
{
rdtsc;
push edx;
push eax;
pop dword ptr v;
pop dword ptr v+4;
}
return v;
}
#endif

#define MAX_ID 50

struct PRF_DATA
{
	char *id; // value of zero means 'premature end of list'
#if USE_clock
	int total_time;
#endif
#if USE_rdtsc
	__int64 total_time_64;
#endif
	int total_count;
} pdata[MAX_ID] = {0};

void prf_reset()
{
	pdata[0].id = 0;
	cur_id = -1;
}

void prf_b(char *id)
{
	if (debug_to_console)
		printf("prf_b('%s')\n", id ? id : "<null>");

	// завершить предыдущий блок
	if(cur_id >= 0)
	{
#if USE_rdtsc
		__int64 cur_64 = rdtsc();
		__int64 dtime64 = cur_64 - clock_cur_64;
		if (dtime64 > 0)
			pdata[cur_id].total_time_64 += dtime64;
#endif
		// считывание времени,
		// учет только что законченного блока,
		// автоматический вывод статистики.	
#if USE_clock
		time_t cur_clock = clock();
		int dtime = cur_clock - clock_cur;
		if (dtime > 0)
			pdata[cur_id].total_time += dtime;
		if (dump_regularly && cur_clock > clock_at_next_print)
		{
			prf_print();
			clock_at_next_print = cur_clock + clock_loop_print;
		}
#else
		if (dump_regularly && cur_64 > clock_at_next_print)
		{
			prf_print();
			clock_at_next_print = cur_64 + clock_loop_print;
		}
#endif
		pdata[cur_id].total_count++;
	}
	// если не надо начинать другой блок, заканчиваем
	if (id == 0)
	{
		cur_id = -1;
		return;
	}
	// найти или создать блок
	int i;
	for (i = 0; i < MAX_ID; i++)
	{
		if (pdata[i].id == 0) // начинаем новый блок
		{
			PRF_DATA tmp  = { id }; // инициализируем остальное как 0
			pdata[i] = tmp;
			if (i != MAX_ID - 1)
				pdata[i + 1].id = 0;
		} // fall through to next if()
		if (pdata[i].id == id) // найден существующий блок
		{
			cur_id = i;
#if USE_clock
			clock_cur = clock();
#endif
#if USE_rdtsc
			clock_cur_64 = rdtsc();
#endif
			return; // NB: досрочный return
		}
	}
	// кончилось место!
	assert(0); // харакири
	cur_id = -1; // если и это не удалось, то ничего не делаем
}

void prf_print(FILE *f) // f == 0 is default (stdout)
{
	if (f == 0)
		f = stdout;

	int i;

#if USE_clock
	int total = 0;
	for (i = 0; i < MAX_ID && pdata[i].id; i++)
		total += pdata[i].total_time;
#endif
#if USE_rdtsc
	__int64 total64 = 0;
	for (i = 0; i < MAX_ID && pdata[i].id; i++)
		total64 += pdata[i].total_time_64;
#endif

	int records = i;

#if USE_clock && !USE_rdtsc
	char *head = "count   ticks   %time   name";
	char *line = "-----   -----   -----   ----";
	char *form = "%5d   %5d   %4.1f%%   %s\n";
#elif USE_clock && USE_rdtsc
	char *head = "count   ticks   %tick   %rdtsc  name";
	char *line = "-----   -----   -----   ------  ----";
	char *form = "%5d   %5d   %4.1f%%   %5.2f%%   %s\n";
#elif !USE_clock && USE_rdtsc
	char *head = "count   %rdtsc   name";
	char *line = "-----   ------   ----";
	char *form = "%5d   %5d   %5.2f%%   %s\n";
#endif

	fprintf (f, "profiler statistics:\n");
	fprintf (f, "  total records:   %d\n", records);
#if USE_clock
	fprintf (f, "  total ticks:     %d\n", total);
#endif
#if USE_rdtsc
	fprintf (f, "  total rdtsc/1e6: %.3f\n", (double )total64 / 1e6);
#endif

	//if (1)
	{
		fprintf (f, "%s\n", line);
		fprintf (f, "%s\n", head);
		fprintf (f, "%s\n", line);

		for (i = 0; i < records; i++)
		{
			fprintf (f,  form,
				pdata[i].total_count,
#if USE_clock
				pdata[i].total_time,
				total ? pdata[i].total_time * 99.0 / total : 0,
#endif
#if USE_rdtsc
				total64 ? pdata[i].total_time_64 * 99.0 / total64 : 0,
#endif
				pdata[i].id);
		}

		fprintf (f, "%s\n", line);
	}

	fflush(f);
}
