#include "BellcoreStructure.h"
#include <string.h>

const char* BellcoreStructure::MAP_STR = "Map";
const char* BellcoreStructure::GENPARAMS_STR = "GenParams";
const char* BellcoreStructure::SUPPARAMS_STR = "SupParams";
const char* BellcoreStructure::FXDPARAMS_STR = "FxdParams";
const char* BellcoreStructure::KEYEVENTS_STR = "KeyEvents";
const char* BellcoreStructure::LNKPARAMS_STR = "LnkParams";
const char* BellcoreStructure::DATAPOINTS_STR = "DataPts";
const char* BellcoreStructure::SPECIAL_STR = "Special";
const char* BellcoreStructure::CKSUM_STR = "Cksum";

BellcoreStructure::BellcoreStructure() {
	this->hasMap = 0;
	this->hasGen = 0;
	this->hasSup = 0;
	this->hasFxd = 0;
	this->hasKey = 0;
	this->hasLnk = 0;
	this->hasData = 0;
	this->hasSpecial = 0;
	this->specials = 0;
}

BellcoreStructure::~BellcoreStructure() {
	if (this->hasMap)
		delete this->map;
	if (this->hasGen)
		delete this->genParams;
	if (this->hasSup)
		delete this->supParams;
	if (this->hasFxd)
		delete this->fxdParams;
	if (this->hasKey)
		delete this->keyEvents;
	if (this->hasLnk)
		delete this->lnkParams;
	if (this->hasData)
		delete this->dataPts;
	if (this->hasSpecial) {
		for (int i = 0; i < this->specials; i++)
			delete this->special[i];
		delete[] this->special;
	}
}

void BellcoreStructure::addField (unsigned char type) {
	switch (type) {
		case BellcoreStructure::MAP:
			if (!this->hasMap) {
				this->map = new BellcoreStructure::Map();
				this->hasMap = 1;
			}
			break;
		case BellcoreStructure::GENPARAMS:
			if (!this->hasGen) {
				this->genParams = new BellcoreStructure::GenParams();
				this->hasGen = 1;
			}
			break;
		case BellcoreStructure::SUPPARAMS:
			if (!this->hasSup) {
				this->supParams = new BellcoreStructure::SupParams();
				this->hasSup = 1;
			}
			break;
		case BellcoreStructure::FXDPARAMS:
			if (!this->hasFxd) {
				this->fxdParams = new BellcoreStructure::FxdParams();
				this->hasFxd = 1;
			}
			break;
		case BellcoreStructure::KEYEVENTS:
			if (!this->hasKey) {
				this->keyEvents = new BellcoreStructure::KeyEvents();
				this->hasKey = 1;
			}
			break;
		case BellcoreStructure::LNKPARAMS:
			if (!this->hasLnk) {
				this->lnkParams = new BellcoreStructure::LnkParams();
				this->hasLnk = 1;
			}
			break;
		case BellcoreStructure::DATAPOINTS:
			if (!this->hasData) {
				this->dataPts = new BellcoreStructure::DataPts();
				this->hasData = 1;
			}
			break;
		case BellcoreStructure::SPECIAL:
			this->special[this->specials++] = new BellcoreStructure::Special();
			hasSpecial = 1;
			break;
		case BellcoreStructure::CKSUM:
			if (!this->hasMap) {
				this->cksum = new BellcoreStructure::Cksum();
			}
	}
}

unsigned int BellcoreStructure::get_size() const {
	unsigned int size = 0;
	for (int i = 0; i < this->specials; i++)
		size += this->special[i]->get_size();
	if (this->hasMap)
		size += this->map->get_size();
	if (this->hasGen)
		size += this->genParams->get_size();
	if (this->hasSup)
		size += this->supParams->get_size();
	if (this->hasFxd)
		size += this->fxdParams->get_size();
	if (this->hasKey)
		size += this->keyEvents->get_size();
	if (this->hasLnk)
		size += this->lnkParams->get_size();
	if (this->hasData)
		size += this->dataPts->get_size();
	size += this->cksum->get_size();
	return size;
}

BellcoreStructure::Map::Map() {
	this->MRN = 0;
	this->MBS = 0;
	this->NB = 1;
}

BellcoreStructure::Map::~Map() {
	delete[] this->B_id;
	delete[] this->B_rev;
	delete[] this->B_size;
}

unsigned int BellcoreStructure::Map::get_size() const {
	int size = 8;
	for (int i = 1; i < this->NB; i++)
		size = size + strlen(this->B_id[i]) + 7;
	return size;
}

BellcoreStructure::GenParams::GenParams() {
	this->LC = "EN";
	this->CID = " ";
	this->FID = " ";
	this->FT = 652;
	this->NW = 1310;
	this->OL = " ";	
	this->TL = " ";	
	this->CCD = " ";
	this->CDF = "BC";
	this->UO = 0;
	this->UOD = 0;
	this->OP = " ";	
	this->CMT = " ";
}

BellcoreStructure::GenParams::~GenParams() {}

unsigned int BellcoreStructure::GenParams::get_size() const {
	return (10 + 7
			+ strlen(this->CID) + strlen(this->FID)
			+ strlen(this->OL) + strlen(this->TL)// + 6
			+ strlen(this->CCD) + strlen(this->OP) + strlen(this->CMT));
}

BellcoreStructure::SupParams::SupParams() {
	this->SN = " ";
	this->MFID = " ";
	this->OTDR = " ";
	this->OMSN = " ";
	this->SR = " ";
	this->OT = " ";
}

BellcoreStructure::SupParams::~SupParams() {}

unsigned int BellcoreStructure::SupParams::get_size() const {
	return (7 + strlen(this->SN) + strlen(this->MFID)
			+ strlen(this->OTDR) + strlen(this->OMID)
			+ strlen(this->OMSN) + strlen(this->SR) + strlen(this->OT));
}

BellcoreStructure::FxdParams::FxdParams() {
	this->DTS = 0;
	this->UD = "km";
	this->AW = 13100;
	this->AO = 0;
	this->AOD = 0;
	this->TPW = 0;
	this->GI = 146800;
	this->BC = 800;
	this->FPO = 0;
	this->NF = 40000;
	this->NFSF = 1000;
	this->PO = 0;
	this->LT = 200;
	this->RT = 40000;
	this->ET = 3000;
	this->TT = "ST";
	this->WC = new int[4];	//!
}

BellcoreStructure::FxdParams::~FxdParams() {
	delete[] this->WC;
	delete[] this->PWU;
	delete[] this->DS;
	delete[] this->NPPW;
}

unsigned int BellcoreStructure::FxdParams::get_size() const {
	return (44 + TPW * 10);
}

BellcoreStructure::KeyEvents::KeyEvents() {
	this->EEL = 0;
	this->ELMP = new int[2];	//!
	this->RLMP = new int[2];	//!
}

BellcoreStructure::KeyEvents::~KeyEvents() {
	delete[] this->ELMP;
	delete[] this->RLMP;
}

unsigned int BellcoreStructure::KeyEvents::get_size() const {
	int size = 26;
	for (int i = 0; i < this->TNKE; i++)
		size = size + 22 + strlen(this->CMT[i]) + 1;
	return size;
}

BellcoreStructure::LnkParams::LnkParams() {}

BellcoreStructure::LnkParams::~LnkParams() {}

unsigned int BellcoreStructure::LnkParams::get_size() const {
	int size = 2;
	for (int i = 0; i < this->TNL; i++)
		size = size + 32 + strlen(this->CMT[i]) + 1;
	return size;
}

BellcoreStructure::DataPts::DataPts() {}

BellcoreStructure::DataPts::~DataPts() {
	delete[] this->TPS;
	delete[] this->SF;
	short i;
	for (i = 0; i < this->TSF; i++)
		delete[] this->DSF[i];
	delete[] this->DSF;
}

unsigned int BellcoreStructure::DataPts::get_size() const {
	int size = 6 + this->TSF * 6 + this->TNDP * 2;
	//for (int i = 0; i < TSF; i++)
	//		size += TPS[i] * 2;
	return size;
}

BellcoreStructure::Cksum::Cksum() {}

BellcoreStructure::Cksum::~Cksum() {}

unsigned int BellcoreStructure::Cksum::get_size() const {
	return 2;
}

BellcoreStructure::Special::Special() {}

BellcoreStructure::Special::~Special() {}

unsigned int BellcoreStructure::Special::get_size() const {
	return this->size;
}

