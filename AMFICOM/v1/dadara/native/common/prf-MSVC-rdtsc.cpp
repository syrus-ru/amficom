#include <time.h>
#include <stdio.h>
#include <assert.h>
#include "prf.h"

#ifdef __unix__
#define __int64 long long
#define CLK_TCK CLOCKS_PER_SEC
#define USE_clock 1
#else
#define USE_clock 1
#define USE_rdtsc 1
#endif

static int clock_cur;
static __int64 clock_cur_64;
static int cur_id = -1;
static int isModified = 0;

const int debug_to_console = 0;
const int dump_regularly = 0;
const int print_only_if_modified = 1;

#if USE_clock
const int clock_loop_print = CLK_TCK * 5;
static time_t clock_at_next_print = clock_loop_print;
#else
const __int64 clock_loop_print = (__int64) 2e9;
static __int64 clock_at_next_print = clock_loop_print;
#endif

#if USE_rdtsc
const double rdtsc_divisor = 3e6; // XXX: for easiest debugging on *my* CPU
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

	// ????????? ?????????? ????
	if(cur_id >= 0)
	{
#if USE_rdtsc
		__int64 cur_64 = rdtsc();
		__int64 dtime64 = cur_64 - clock_cur_64;
		if (dtime64 > 0)
			pdata[cur_id].total_time_64 += dtime64;
#endif
		// ?????????? ???????,
		// ???? ?????? ??? ???????????? ?????,
		// ?????????????? ????? ??????????.	
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
		isModified = 1;
	}
	// ???? ?? ???? ???????? ?????? ????, ???????????
	if (id == 0)
	{
		cur_id = -1;
		return;
	}
	// ????? ??? ??????? ????
	int i;
	for (i = 0; i < MAX_ID; i++)
	{
		if (pdata[i].id == 0) // ???????? ????? ????
		{
			PRF_DATA tmp  = { id }; // ?????????????? ????????? ??? 0
			pdata[i] = tmp;
			if (i != MAX_ID - 1)
				pdata[i + 1].id = 0;
		} // fall through to next if()
		if (pdata[i].id == id) // ?????? ???????????? ????
		{
			cur_id = i;
#if USE_clock
			clock_cur = clock();
#endif
#if USE_rdtsc
			clock_cur_64 = rdtsc();
#endif
			return; // NB: ????????? return
		}
	}
	// ????????? ?????!
	assert(0); // ????????
	cur_id = -1; // ???? ? ??? ?? ???????, ?? ?????? ?? ??????
}

void prf_print(FILE *f) // f == 0 is default (stdout)
{
	if (f == 0)
		f = stdout;

	if (!isModified)
		return;

	isModified = 0;

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
	char *form = "%5d   %5d   %4.1f%%   %s%s\n";
#elif USE_clock && USE_rdtsc
	char *head = "count   ticks   %tick    rdtsc    %rdtsc   name";
	char *line = "-----   -----   -----   -------   ------   ----";
	char *form = "%5d   %5d   %4.1f%%   %7.2f   %5.2f%%   %s%s\n";
#elif !USE_clock && USE_rdtsc
	char *head = "count    rdtsc    %rdtsc   name";
	char *line = "-----   -------   ------   ----";
	char *form = "%5d   %7.2f   %5.2f%%   %s%s\n";
#endif

	fprintf (f, "profiler statistics:\n");
	fprintf (f, "  total records: %d\n", records);
#if USE_clock
	fprintf (f, "  total ticks:   %d\n", total);
#endif
#if USE_rdtsc
	fprintf (f, "  total rdtsc/%g: %.3f\n", rdtsc_divisor, (double )total64 / rdtsc_divisor);
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
				pdata[i].total_time_64 / rdtsc_divisor,
				total64 ? pdata[i].total_time_64 * 99.0 / total64 : 0,
#endif
				cur_id == i ? "--> " : "", // ???? ?????-?? ???? ??? ?? ??????????, ???????? ???
				pdata[i].id);
		}

		fprintf (f, "%s\n", line);
	}

	fflush(f);
}

void prf_print()
{
	prf_print(0);
}
