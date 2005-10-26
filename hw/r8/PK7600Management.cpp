#include <stdio.h>
#include "PK7600OTDRController.h"
#include "OTDRContainer.h"
#include "OTDRReportListener.h"


/*	Эта вспомогательная функция используется для получения строки состояния рефлектометра*/
const int PROGRESS_MESSAGE_SIZE = 500;
char progressMessage[PROGRESS_MESSAGE_SIZE];
void _cdecl otdrCardProgressIndicate(char *s) {
	memcpy(progressMessage, s, PROGRESS_MESSAGE_SIZE);
}

/*	Выполнить поиск всех плат вида PK7600.
	Найденные проинициализировать и зарегистрировать в OTDRContainer.
	При ошибке возвратить false, при отсутствии ошибок - true.*/
BOOL initPK7600Cards(OTDRReportListener* otdrReportListener,
			OTDRContainer* otdrContainer,
			const unsigned int timewait) {
	tPK7600OTDRFound foundOTDRs[MAX_OTDRS];
	DWORD foundCount;
	if (PK7600FindOTDRs(foundOTDRs, &foundCount) != 0) {
		printf("RTU | ERROR: Failed to find OTDR cards\n");
		return FALSE;
	}

	for (OTDRId id = 0; id < foundCount; id++) {
		const int errorcode = PK7600Initialize(id, otdrCardProgressIndicate);
		if (errorcode == 0) {
			printf("PK7600OTDRController | Init card %u - success; report: '%s'\n",
				id,
				progressMessage);
			PK7600OTDRController* otdrController = new PK7600OTDRController(id,
				otdrReportListener,
				timewait,
				foundOTDRs[id].cardType);
			otdrController->init();
			otdrContainer->registerOTDRController(otdrController);
		} else {
			printf("PK7600OTDRController | Init OTDR card %u - failure; error code: %d; report: '%s'\n",
				id,
				errorcode,
				progressMessage);
		}
	}

	return TRUE;
}