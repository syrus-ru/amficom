#include <stdio.h>
#include "QP1640OTDRController.h"
#include "OTDRContainer.h"
#include "OTDRReportListener.h"

/*	Выполнить поиск всех плат вида QP1640.
	Найденные проинициализировать и зарегистрировать в OTDRContainer.
	При ошибке возвратить false, при отсутствии ошибок - true.*/
BOOL initQP1640Cards(const OTDRReportListener* otdrReportListener,
			OTDRContainer* otdrContainer,
			const unsigned int timewait) {
	WORD* cards = new WORD[MAX_CARDS];
	QPOTDRGetCards(cards);

	for (OTDRId id = 0; id < MAX_CARDS; id++) {
		if (cards[id] == 0xFFFF) {
			continue;
		}

		const int errorcode = QPOTDRInitialize(cards[id]);
		if (errorcode == 0) {
			printf("QP1640OTDRController | Init card %u - success", id);
			QP1640OTDRController* otdrController = new QP1640OTDRController(id,
				otdrReportListener,
				timewait);
			otdrController->init();
			otdrContainer->registerOTDRController(otdrController);
		} else {
			printf("QP1640OTDRController | Init OTDR card %u - failure; error code: %d\n", id, errorcode);
		}
	}

	return TRUE;
}
