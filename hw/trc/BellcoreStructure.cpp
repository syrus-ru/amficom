#include "BellcoreStructure.h"
#include <string.h>

const unsigned char BellcoreStructure::MAP = 1;
const unsigned char BellcoreStructure::GENPARAMS = 2;
const unsigned char BellcoreStructure::SUPPARAMS = 3;
const unsigned char BellcoreStructure::FXDPARAMS = 4;
const unsigned char BellcoreStructure::KEYEVENTS = 5;
const unsigned char BellcoreStructure::LNKPARAMS = 6;
const unsigned char BellcoreStructure::DATAPOINTS = 7;
const unsigned char BellcoreStructure::SPECIAL = 8;
const unsigned char BellcoreStructure::CKSUM = 9;

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

void BellcoreStructure::add_field_map() {
	if (!this->hasMap) {
		this->map = new BellcoreStructure::Map(this);
		this->hasMap = 1;
	}
}

void BellcoreStructure::add_field_gen_params(const char* CID,
		const char* FID,
		const short FT,
		const short NW,
		const char* OL,
		const char* TL,
		const char* CCD,
		const char* CDF,
		const char* OP,
		const char* CMT) {
	if (!this->hasGen) {
		this->genParams = new BellcoreStructure::GenParams(CID,
				FID,
				FT,
				NW,
				OL,
				TL,
				CCD,
				CDF,
				OP,
				CMT);
		this->hasGen = 1;
	}
}

void BellcoreStructure::add_field_sup_params(const char* SN,
		const char* MFID,
		const char* OTDR,
		const char* OMID,
		const char* OMSN,
		const char* SR,
		const char* OT) {
	if (!this->hasSup) {
		this->supParams = new BellcoreStructure::SupParams(SN,
				MFID,
				OTDR,
				OMID,
				OMSN,
				SR,
				OT);
		this->hasSup = 1;
	}
}

void BellcoreStructure::add_field_fxd_params(const unsigned int DTS,
		const char* UD,
		const short AW,
		const int AO,
		const short TPW,
		const short* PWU,
		const int* DS,
		const int* NPPW,
		const int GI,
		const int NAV,
		const int AR) {
	if (!this->hasFxd) {
		this->fxdParams = new BellcoreStructure::FxdParams(DTS,
				UD,
				AW,
				AO,
				TPW,
				PWU,
				DS,
				NPPW,
				GI,
				NAV,
				AR);
		this->hasFxd = 1;
	}
}

void BellcoreStructure::add_field_key_events(const short TNKE,
		const short* EN,
		const int* EPT,
		const short* ACI,
		const short* EL,
		const int* ER,
		const char** EC,
		const char** LMT,
		const char** CMT,
		const int EEL,
		const int* ELMP,
		const unsigned short ORL,
		const int* RLMP) {
	if (!this->hasKey) {
		this->keyEvents = new BellcoreStructure::KeyEvents(TNKE,
				EN,
				EPT,
				ACI,
				EL,
				ER,
				EC,
				LMT,
				CMT,
				EEL,
				ELMP,
				ORL,
				RLMP);
		this->hasKey = 1;
	}
}

void BellcoreStructure::add_field_lnk_params(const short TNL,
		const short* LMN,
		const char** LMC,
		const int* LML,
		const short* REN,
		const int** GPA,
		const short* FCI,
		const int* SMI,
		const int* SML,
		const char** USML,
		const short* MFDL,
		const char** CMT) {
	if (!this->hasLnk) {
		this->lnkParams = new BellcoreStructure::LnkParams(TNL,
				LMN,
				LMC,
				LML,
				REN,
				GPA,
				FCI,
				SMI,
				SML,
				USML,
				MFDL,
				CMT);
		this->hasLnk = 1;
	}
}

void BellcoreStructure::add_field_data_pts(const int TNDP,
		const short TSF,
		const int* TPS,
		const short* SF,
		unsigned short** DSF) {
	if (!this->hasData) {
		this->dataPts = new BellcoreStructure::DataPts(TNDP,
				TSF,
				TPS,
				SF,
				DSF);
		this->hasData = 1;
	}
}

void BellcoreStructure::add_field_cksum(const unsigned short CSM) {
	if (!this->hasMap) {
		this->cksum = new BellcoreStructure::Cksum(CSM);
	}
}

//!!! Not implemented
void BellcoreStructure::add_field_special(unsigned int size, char* data) {
//	this->special[this->specials++] = new BellcoreStructure::Special(size, data);
//	hasSpecial = 1;
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

BellcoreStructure::Map::Map(BellcoreStructure* bs) {
	this->MRN = 100;

	this->NB = 2;	//Map and Cksum
	if (bs->hasGen) this->NB ++;
	if (bs->hasSup) this->NB ++;
	if (bs->hasFxd) this->NB ++;
	if (bs->hasKey) this->NB ++;
	if (bs->hasLnk) this->NB ++;
	if (bs->hasData) this->NB ++;
	this->NB += bs->specials;

	this->B_id = new char*[this->NB];
	this->B_rev = new unsigned short[this->NB];
	this->B_size = new int[this->NB];

	int nb = 0;
	copy_string(this->B_id[nb], MAP_STR);
	this->B_rev[nb] = 100;
	nb ++;
	if (bs->hasGen) {
		copy_string(this->B_id[nb], GENPARAMS_STR);
		this->B_rev[nb] = 100;
		this->B_size[nb] = bs->genParams->get_size();
		nb ++;
	}
	if (bs->hasSup) {
		copy_string(this->B_id[nb], SUPPARAMS_STR);
		this->B_rev[nb] = 100;
		this->B_size[nb] = bs->supParams->get_size();
		nb ++;
	}
	if (bs->hasFxd) {
		copy_string(this->B_id[nb], FXDPARAMS_STR);
		this->B_rev[nb] = 100;
		this->B_size[nb] = bs->fxdParams->get_size();
		nb ++;
	}
	if (bs->hasKey) {
		copy_string(this->B_id[nb], KEYEVENTS_STR);
		this->B_rev[nb] = 100;
		this->B_size[nb] = bs->keyEvents->get_size();
		nb ++;
	}
	if (bs->hasLnk) {
		copy_string(this->B_id[nb], LNKPARAMS_STR);
		this->B_rev[nb] = 100;
		this->B_size[nb] = bs->lnkParams->get_size();
		nb ++;
	}
	if (bs->hasData) {
		copy_string(this->B_id[nb], DATAPOINTS_STR);
		this->B_rev[nb] = 100;
		this->B_size[nb] = bs->dataPts->get_size();
		nb ++;
	}
	copy_string(this->B_id[nb], CKSUM_STR);
	this->B_size[nb] = bs->cksum->get_size();
	this->B_rev[nb] = 100;
	nb ++;
	for (int i = 0; i < bs->specials; i++) {
		copy_string(this->B_id[nb], SPECIAL_STR);
		this->B_rev[nb] = 100;
		this->B_size[nb] = bs->special[i]->get_size();
		nb ++;
	}

	this->MBS = this->get_size();
}

BellcoreStructure::Map::~Map() {
	for (int i = 0; i < this->NB; i++)
		delete[] this->B_id[i];
	delete[] this->B_id;
	delete[] this->B_rev;
	delete[] this->B_size;
}

unsigned int BellcoreStructure::Map::get_size() const {
	unsigned int size = sizeof(unsigned short)		//MRN
		+ sizeof(int)				//MBS
		+ sizeof(short);			//NB
	for (int i = 1; i < this->NB; i++)
		size += strlen(this->B_id[i]) + 1	//B_id
			+ sizeof(unsigned short)	//B_rev[i]
			+ sizeof(int);			//B_size[i]
	
	return size;
}

const int BellcoreStructure::GenParams::LENGTH_LC = 2;
const int BellcoreStructure::GenParams::LENGTH_CDF = 2;

BellcoreStructure::GenParams::GenParams(const char* CID,
		const char* FID,
		const short FT,
		const short NW,
		const char* OL,
		const char* TL,
		const char* CCD,
		const char* CDF,
		const char* OP,
		const char* CMT) {
	this->LC = new char[LENGTH_LC];
	memcpy(this->LC, "EN", LENGTH_LC * sizeof(char));
	copy_string(this->CID, CID);
	copy_string(this->FID, FID);
	this->FT = FT;
	this->NW = NW;
	copy_string(this->OL, OL);
	copy_string(this->TL, TL);
	copy_string(this->CCD, CCD);
	this->CDF = new char[LENGTH_CDF];
	memcpy(this->CDF, CDF, LENGTH_CDF * sizeof(char));
	this->UO = 0;
	this->UOD = 0;
	copy_string(this->OP, OP);
	copy_string(this->CMT, CMT);
}

BellcoreStructure::GenParams::~GenParams() {
	delete[] this->LC;
	delete[] this->CID;
	delete[] this->FID;
	delete[] this->OL;
	delete[] this->TL;
	delete[] this->CCD;
	delete[] this->CDF;
	delete[] this->OP;
	delete[] this->CMT;
}

unsigned int BellcoreStructure::GenParams::get_size() const {
	return (LENGTH_LC * sizeof(char)		//LC
			+ strlen(this->CID) + 1		//CID
			+ strlen(this->FID) + 1		//FID
			+ sizeof(short)			//NW
			+ strlen(this->OL) + 1		//OL
 			+ strlen(this->TL) + 1		//TL
			+ strlen(this->CCD) + 1		//CCD
			+ LENGTH_LC * sizeof(char)	//CDF
			+ sizeof(int)			//UO
			+ strlen(this->OP) + 1		//OP
			+ strlen(this->CMT) + 1);	//CMT
}

BellcoreStructure::SupParams::SupParams(const char* SN,
		const char* MFID,
		const char* OTDR,
		const char* OMID,
		const char* OMSN,
		const char* SR,
		const char* OT) {
	copy_string(this->SN, SN);
	copy_string(this->MFID, MFID);
	copy_string(this->OTDR, OTDR);
	copy_string(this->OMID, OMID);
	copy_string(this->OMSN, OMSN);
	copy_string(this->SR, SR);
	copy_string(this->OT, OT);
}

BellcoreStructure::SupParams::~SupParams() {
	delete[] this->SN;
	delete[] this->MFID;
	delete[] this->OTDR;
	delete[] this->OMID;
	delete[] this->OMSN;
	delete[] this->SR;
	delete[] this->OT;
}

unsigned int BellcoreStructure::SupParams::get_size() const {
	return (strlen(this->SN) + 1
			+ strlen(this->MFID) + 1
			+ strlen(this->OTDR) + 1
			+ strlen(this->OMID) + 1
			+ strlen(this->OMSN) + 1
			+ strlen(this->SR) + 1
			+ strlen(this->OT) + 1);
}

const int BellcoreStructure::FxdParams::LENGTH_UD = 2;

BellcoreStructure::FxdParams::FxdParams(const unsigned int DTS,
		const char* UD,
		const short AW,
		const int AO,
		const short TPW,
		const short* PWU,
		const int* DS,
		const int* NPPW,
		const int GI,
		const int NAV,
		const int AR) {
	this->DTS = DTS;
	this->UD = new char[LENGTH_UD];
	memcpy(this->UD, UD, LENGTH_UD * sizeof(char));
	this->AW = AW;
	this->AO = AO;
	this->AOD = 0;

	this->TPW = TPW;
	this->PWU = new short[this->TPW];
	this->DS = new int[this->TPW];
	this->NPPW = new int[this->TPW];
	for (int i = 0; i < this->TPW; i++) {
		this->PWU[i] = PWU[i];
		this->DS[i] = DS[i];
		this->NPPW[i] = NPPW[i];
	}

	this->GI = GI;
	this->NAV = NAV;
	this->AR = AR;
	this->BC = 800;
	this->FPO = 0;
	this->NF = 40000;
	this->NFSF = 1000;
	this->PO = 0;
	this->LT = 200;
	this->RT = 40000;
	this->ET = 3000;
	copy_string(this->TT, "ST");
	this->WC = new int[4];
}

BellcoreStructure::FxdParams::~FxdParams() {
	delete[] this->UD;
	delete[] this->PWU;
	delete[] this->DS;
	delete[] this->NPPW;
	delete[] this->TT;
	delete[] this->WC;
}

unsigned int BellcoreStructure::FxdParams::get_size() const {
	return (sizeof(int)				//DTS
			+ LENGTH_UD * sizeof(char)	//UD
			+ sizeof(short)			//AW
			+ sizeof(int)			//AO
			+ sizeof(short)			//TPW
			+ this->TPW * sizeof(short)	//PWU
			+ this->TPW * sizeof(int)	//DS
			+ this->TPW * sizeof(int)	//NPPW
			+ sizeof(int)			//GI
			+ sizeof(short)			//BC
			+ sizeof(int)			//NAV
			+ sizeof(int)			//AR
			+ sizeof(int)			//FPO
			+ sizeof(unsigned short)	//NF
			+ sizeof(unsigned short)	//NFSF
			+ sizeof(unsigned short)	//PO
			+ sizeof(unsigned short)	//LT
			+ sizeof(unsigned short)	//RT
			+ sizeof(unsigned short));	//ET
}

const int BellcoreStructure::KeyEvents::LENGTH_EC = 6;
const int BellcoreStructure::KeyEvents::LENGTH_LMT = 2;
const int BellcoreStructure::KeyEvents::LENGTH_ELMP = 2;
const int BellcoreStructure::KeyEvents::LENGTH_RLMP = 2;

BellcoreStructure::KeyEvents::KeyEvents(const short TNKE,
		const short* EN,
		const int* EPT,
		const short* ACI,
		const short* EL,
		const int* ER,
		const char** EC,
		const char** LMT,
		const char** CMT,
		const int EEL,
		const int* ELMP,
		const unsigned short ORL,
		const int* RLMP) {
	this->TNKE = TNKE;
	this->EN = new short[this->TNKE];
	this->EPT = new int[this->TNKE];
	this->ACI = new short[this->TNKE];
	this->EL = new short[this->TNKE];
	this->ER = new int[this->TNKE];
	this->EC = new char*[this->TNKE];
	this->LMT = new char*[this->TNKE];
	this->CMT = new char*[this->TNKE];

	int i;

	for (i = 0; i < this->TNKE; i++) {
		this->EN[i] = EN[i];
		this->EPT[i] = EPT[i];
		this->ACI[i] = ACI[i];
		this->EL[i] = EL[i];
		this->ER[i] = ER[i];
		this->EC[i] = new char[LENGTH_EC];
		memcpy(this->EC[i], EC[i], LENGTH_EC * sizeof(char));
		this->LMT[i] = new char[LENGTH_LMT];
		memcpy(this->LMT[i], LMT[i], LENGTH_LMT * sizeof(char));
		copy_string(this->CMT[i], CMT[i]);
	}
	this->EEL = EEL;
	this->ELMP = new int[LENGTH_ELMP];
	for (i = 0; i < LENGTH_ELMP; i++)
		this->ELMP[i] = ELMP[i];
	this->ORL = ORL;
	this->RLMP = new int[LENGTH_RLMP];
	for (i = 0; i < LENGTH_RLMP; i++)
		this->RLMP[i] = RLMP[i];
}

BellcoreStructure::KeyEvents::~KeyEvents() {
	delete[] this->EN;
	delete[] this->EPT;
	delete[] this->ACI;
	delete[] this->EL;
	delete[] this->ER;
	for (int i = 0; i < this->TNKE; i++) {
		delete[] this->EC[i];
		delete[] this->LMT[i];
		delete[] this->CMT[i];
	}
	delete[] this->EC;
	delete[] this->LMT;
	delete[] this->CMT;
}

unsigned int BellcoreStructure::KeyEvents::get_size() const {
//!!! Id old version was: 
//!!!	int size = 26;
//!!! Now size became == 24;
	unsigned int size = sizeof(short)		//TNKE
		+ sizeof(int)				//EEL
		+ LENGTH_ELMP * sizeof(int)		//ELMP
		+ sizeof(unsigned short)		//ORL
		+ LENGTH_RLMP * sizeof(int);		//RLMP
	for (int i = 0; i < this->TNKE; i++)
		size += sizeof(short)			//EN
			+ sizeof(int)			//EPT
			+ sizeof(short)			//ACI
			+ sizeof(short)			//EL
			+ sizeof(int)			//ER
			+ LENGTH_EC * sizeof(char)	//EC
			+ LENGTH_LMT * sizeof(char)	//LMT
			+ strlen(this->CMT[i]) + 1;	//CMT
	return size;
}

const int BellcoreStructure::LnkParams::LENGTH_LMC = 2;
const int BellcoreStructure::LnkParams::LENGTH_GPA = 2;
const int BellcoreStructure::LnkParams::LENGTH_USML = 2;

BellcoreStructure::LnkParams::LnkParams(const short TNL,
		const short* LMN,
		const char** LMC,
		const int* LML,
		const short* REN,
		const int** GPA,
		const short* FCI,
		const int* SMI,
		const int* SML,
		const char** USML,
		const short* MFDL,
		const char** CMT) {
	this->TNL = TNL;
	this->LMN = new short[this->TNL];
	this->LMC = new char*[this->TNL];
	this->LML = new int[this->TNL];
	this->REN = new short[this->TNL];
	this->GPA = new int*[this->TNL];
	this->FCI = new short[this->TNL];
	this->SMI = new int[this->TNL];
	this->SML = new int[this->TNL];
	this->USML = new char*[this->TNL];
	this->MFDL = new short[this->TNL];
	this->CMT = new char*[this->TNL];
	for (int i = 0; i < this->TNL; i++) {
		this->LMN[i] = LMN[i];
		this->LMC[i] = new char[LENGTH_LMC];
		memcpy(this->LMC[i], LMC[i], LENGTH_LMC * sizeof(char));
		this->LML[i] = LML[i];
		this->REN[i] = REN[i];
		this->GPA[i] = new int[LENGTH_GPA];
		memcpy(this->GPA[i], GPA[i], LENGTH_GPA * sizeof(int));
		this->FCI[i] = FCI[i];
		this->SMI[i] = SMI[i];
		this->SML[i] = SML[i];
		this->USML[i] = new char[LENGTH_USML];
		memcpy(this->USML[i], USML[i], LENGTH_USML * sizeof(char));
		this->MFDL[i] = MFDL[i];
		copy_string(this->CMT[i], CMT[i]);
	}
}

BellcoreStructure::LnkParams::~LnkParams() {
	delete[] this->LMN;
	delete[] this->LML;
	delete[] this->REN;
	delete[] this->FCI;
	delete[] this->SMI;
	delete[] this->SML;
	delete[] this->MFDL;
	for (int i = 0; i < this->TNL; i++) {
		delete[] this->LMC[i];
		delete[] this->GPA[i];
		delete[] this->USML[i];
		delete[] this->CMT[i];
	}
	delete[] this->LMC;
	delete[] this->GPA;
	delete[] this->USML;
	delete[] this->CMT;


}

unsigned int BellcoreStructure::LnkParams::get_size() const {
	unsigned int size = sizeof(short);		//TNL
	for (int i = 0; i < this->TNL; i++)
		size += sizeof(short)			//LMN
			+ LENGTH_LMC * sizeof(char)	//LMC
			+ sizeof(int)			//LML
			+ sizeof(short)			//REN
			+ LENGTH_GPA * sizeof(int)	//GPA
			+ sizeof(short)			//FCI
			+ sizeof(int)			//SMI
			+ sizeof(int)			//SML
			+ LENGTH_USML * sizeof(char)	//USML
			+ sizeof(short)			//MFDL
			+ strlen(this->CMT[i]) + 1;	//CMT
	return size;
}

BellcoreStructure::DataPts::DataPts(const int TNDP,
		const short TSF,
		const int* TPS,
		const short* SF,
		unsigned short** DSF) {
	this->TNDP = TNDP;
	this->TSF = TSF;
	this->TPS = new int[this->TSF];
	this->SF = new short[this->TSF];

//!!!	Не создавать новый массив. Использовать ссылку на уже созданный.
//	Модуль, создавший массив DSF, не должен его удалять!
//	Это делает деструктор данного класса.
//	
//	this->DSF = new unsigned short*[this->TSF];
//	for (int i = 0; i < this->TSF; i++) {
//		this->TPS[i] = TPS[i];
//		this->SF[i] = SF[i];
//		this->DSF[i] = new unsigned short[this->TPS[i]];
//		for (int k = 0; k < this->TPS[i]; k++)
//			this->DSF[i][k] = DSF[i][k];
//	}
	for (int i = 0; i < this->TSF; i++) {
		this->TPS[i] = TPS[i];
		this->SF[i] = SF[i];
	}
	this->DSF = DSF;
}

BellcoreStructure::DataPts::~DataPts() {
	delete[] this->TPS;
	delete[] this->SF;
	for (int i = 0; i < this->TSF; i++)
		delete[] this->DSF[i];
	delete[] this->DSF;
}

unsigned int BellcoreStructure::DataPts::get_size() const {
	unsigned int size = sizeof(int)	//TNDP
		+ sizeof(short)			//TSF
		+ this->TSF * sizeof(int)	//TPS
		+ this->TSF * sizeof(short)	//SF
		+ this->TNDP * sizeof(short);	//DSF
	return size;
}

BellcoreStructure::Cksum::Cksum(const unsigned short CSM) {
	this->CSM = CSM;
}

BellcoreStructure::Cksum::~Cksum() {}

unsigned int BellcoreStructure::Cksum::get_size() const {
	return sizeof(unsigned short);
}

BellcoreStructure::Special::Special(unsigned int size, char* data) {
	this->size = size;
	this->data = new char[this->size];
	memcpy(this->data, data, this->size);
}

BellcoreStructure::Special::~Special() {
	delete[] this->data;
}

unsigned int BellcoreStructure::Special::get_size() const {
	return this->size;
}

void BellcoreStructure::copy_string(char* copy_str, const char* str) {
	size_t length = strlen(str);
	copy_str = new char[length + 1];
	memcpy(copy_str, str, (length + 1) * sizeof(char));
}

