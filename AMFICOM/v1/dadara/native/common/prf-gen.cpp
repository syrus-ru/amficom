#include <time.h>
#include <stdio.h>
#include <assert.h>
#include "prf.h"

static int clock_cur;
static int cur_id = -1;

const int debug_to_console = 0;

static int clock_after_last_print;
const int clock_loop_print = CLK_TCK * 1;

#define MAX_ID 50

struct PRF_DATA
{
	char *id; // value of zero means 'premature end of list'
	int total_time;
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
		int dtime = clock() - clock_cur;
		if (dtime < 0)
			dtime = 0;
		pdata[cur_id].total_time += dtime;
		pdata[cur_id].total_count++;
		clock_after_last_print += dtime;
	}
	// автоматический вывод статистики
	if (clock_after_last_print > clock_loop_print)
	{
		printf("clock_after_last_print: %d, clock_loop_print: %d\n",
		clock_after_last_print, clock_loop_print);
		prf_print();
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
			pdata[i].id = id;
			pdata[i].total_time = 0;
			pdata[i].total_count = 0;
			if (i != MAX_ID - 1)
				pdata[i + 1].id = 0;
		} // fall through to next if()
		if (pdata[i].id == id) // найден существующий блок
		{
			cur_id = i;
			clock_cur = clock();
			return; // NB: досрочный return
		}
	}
	// кончилось место!
	assert(0); // харакири
	cur_id = -1; // если и это не удалось, то ничего не делаем
}

void prf_print(FILE *f) // f == 0 is default (stdout)
{
	clock_after_last_print = 0;

	if (f == 0)
		f = stdout;

	int i;

	int total = 0;
	for (i = 0; i < MAX_ID && pdata[i].id; i++)
		total += pdata[i].total_time;

	int records = i;

	char *line = "-----   -----   -----   ----\n";

	fprintf (f, "profiler statistics:\n");
	fprintf (f, "  total records: %d\n", records);
	fprintf (f, "  total time:    %d\n", total);

	if (total)
	{
		fprintf (f, line);
		fprintf (f, "count   ticks   %%time   name\n");
		fprintf (f, line);

		for (i = 0; i < records; i++)
		{
			fprintf (f, "%5d   %5d   %4.1f%%   %s\n",
				pdata[i].total_count,
				pdata[i].total_time,
				(int )(pdata[i].total_time * 999.0 / total) / 10.0,
				pdata[i].id);
		}

		fprintf (f, line);
	}

	fflush(f);
}

