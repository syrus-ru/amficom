#include "BellcoreWriter.h"
#include <string.h>
#include <stdio.h>

BellcoreWriter::BellcoreWriter() {
}

BellcoreWriter::~BellcoreWriter() {
	delete this->idos;
}

void BellcoreWriter::write(BellcoreStructure* bs) {
	this->bs = bs;
	this->idos = new IntelDataOutputStream(bs->get_size());
	
	this->write_map();
	int i = 1;
	int j = 0;
	while (i < this->bs->map->NB) {
		if (strcmp(this->bs->map->B_id[i], BellcoreStructure::GENPARAMS_STR) == 0)
			write_genParams(i++);
		else if (strcmp(this->bs->map->B_id[i], BellcoreStructure::SUPPARAMS_STR) == 0)
			write_supParams(i++);
		else if (strcmp(this->bs->map->B_id[i], BellcoreStructure::FXDPARAMS_STR) == 0)
			write_fxdParams(i++);
		else if (strcmp(this->bs->map->B_id[i], BellcoreStructure::KEYEVENTS_STR) == 0)
			write_keyEvents(i++);
		else if (strcmp(this->bs->map->B_id[i], BellcoreStructure::LNKPARAMS_STR) == 0)
			write_lnkParams(i++);
		else if (strcmp(this->bs->map->B_id[i], BellcoreStructure::DATAPOINTS_STR) == 0)
			write_dataPts(i++);
		else if (strcmp(this->bs->map->B_id[i], BellcoreStructure::CKSUM_STR) == 0)
			write_cksum(i++);
		else if (this->bs->specials > 0)
			write_special(j++, i++);
		else
			printf("BellcoreWriter | ERROR: Unknown block!\n");
	}
}

unsigned char* BellcoreWriter::get_data() const {
	return this->idos->get_data();
}

unsigned int BellcoreWriter::get_data_size() const {
	return this->idos->get_real_size();
}

int BellcoreWriter::write_map() {
	int ret = 0x00000001;
	ret &= this->idos->write_unsigned_short(this->bs->map->MRN);
	ret &= this->idos->write_int(this->bs->map->MBS);
	ret &= this->idos->write_short(this->bs->map->NB);
	for (int i = 1; i < this->bs->map->NB; i++) {
		ret &= this->idos->write_string(this->bs->map->B_id[i]);
		ret &= this->idos->write_unsigned_short(this->bs->map->B_rev[i]);
		ret &= this->idos->write_int(this->bs->map->B_size[i]);
	}
	return ret;
}

int BellcoreWriter::write_genParams(int i) {
	if (this->bs->map->B_size[i] == 0)
		return 0;
	int ret = 0x00000001;
	ret &= this->idos->write_char(this->bs->genParams->LC[0]);
	ret &= this->idos->write_char(this->bs->genParams->LC[1]);
	ret &= this->idos->write_string(this->bs->genParams->CID);
	ret &= this->idos->write_string(this->bs->genParams->FID);
	ret &= this->idos->write_short(this->bs->genParams->NW);
	ret &= this->idos->write_string(this->bs->genParams->OL);
	ret &= this->idos->write_string(this->bs->genParams->TL);
	ret &= this->idos->write_string(this->bs->genParams->CCD);
	ret &= this->idos->write_char(this->bs->genParams->CDF[0]);
	ret &= this->idos->write_char(this->bs->genParams->CDF[1]);
	ret &= this->idos->write_int(this->bs->genParams->UO);
	ret &= this->idos->write_string(this->bs->genParams->OP);
	ret &= this->idos->write_string(this->bs->genParams->CMT);
	return ret;
}

int BellcoreWriter::write_supParams(int i) {
	if (this->bs->map->B_size[i] == 0)
		return 0;
	int ret = 0x00000001;
	ret &= this->idos->write_string(this->bs->supParams->SN);
	ret &= this->idos->write_string(this->bs->supParams->MFID);
	ret &= this->idos->write_string(this->bs->supParams->OTDR);
	ret &= this->idos->write_string(this->bs->supParams->OMID);
	ret &= this->idos->write_string(this->bs->supParams->OMSN);
	ret &= this->idos->write_string(this->bs->supParams->SR);
	ret &= this->idos->write_string(this->bs->supParams->OT);
	return ret;
}

int BellcoreWriter::write_fxdParams(int i) {
	if (this->bs->map->B_size[i] == 0)
		return 0;
	int ret = 0x00000001;
	ret &= this->idos->write_unsigned_int(this->bs->fxdParams->DTS);
	ret &= this->idos->write_char(this->bs->fxdParams->UD[0]);
	ret &= this->idos->write_char(this->bs->fxdParams->UD[1]);
	ret &= this->idos->write_short(this->bs->fxdParams->AW);
	ret &= this->idos->write_int(this->bs->fxdParams->AO);
	ret &= this->idos->write_short(this->bs->fxdParams->TPW);
	int k;
	for (k = 0; k < this->bs->fxdParams->TPW; k++)
		ret &= this->idos->write_short(this->bs->fxdParams->PWU[k]);
	for (k = 0; k < this->bs->fxdParams->TPW; k++)
		ret &= this->idos->write_int(this->bs->fxdParams->DS[k]);
	for (k = 0; k < this->bs->fxdParams->TPW; k++)
		ret &= this->idos->write_int(this->bs->fxdParams->NPPW[k]);
	ret &= this->idos->write_int(this->bs->fxdParams->GI);
	ret &= this->idos->write_short(this->bs->fxdParams->BC);
	ret &= this->idos->write_int(this->bs->fxdParams->NAV);
	ret &= this->idos->write_int(this->bs->fxdParams->AR);
	ret &= this->idos->write_int(this->bs->fxdParams->FPO);
	ret &= this->idos->write_unsigned_short(this->bs->fxdParams->NF);
	ret &= this->idos->write_unsigned_short(this->bs->fxdParams->NFSF);
	ret &= this->idos->write_unsigned_short(this->bs->fxdParams->PO);
	ret &= this->idos->write_unsigned_short(this->bs->fxdParams->LT);
	ret &= this->idos->write_unsigned_short(this->bs->fxdParams->RT);
	ret &= this->idos->write_unsigned_short(this->bs->fxdParams->ET);
	return ret;
}

int BellcoreWriter::write_keyEvents(int i) {
	if (this->bs->map->B_size[i] == 0)
		return 0;
	int ret = 0x00000001;
	ret &= this->idos->write_short(this->bs->keyEvents->TNKE);
	int k, l;
	for (k = 0; k < this->bs->keyEvents->TNKE; k++) {
		ret &= this->idos->write_short(this->bs->keyEvents->EN[k]);
		ret &= this->idos->write_int(this->bs->keyEvents->EPT[k]);
		ret &= this->idos->write_short(this->bs->keyEvents->ACI[k]);
		ret &= this->idos->write_short(this->bs->keyEvents->EL[k]);
		ret &= this->idos->write_int(this->bs->keyEvents->ER[k]);
		for (l = 0; l < 6; l++)
			ret &= this->idos->write_char(this->bs->keyEvents->EC[k][l]);
		ret &= this->idos->write_char(this->bs->keyEvents->LMT[k][0]);
		ret &= this->idos->write_char(this->bs->keyEvents->LMT[k][1]);
		ret &= this->idos->write_string(this->bs->keyEvents->CMT[k]);
	}
	ret &= this->idos->write_int(this->bs->keyEvents->EEL);
	ret &= this->idos->write_int(this->bs->keyEvents->ELMP[0]);
	ret &= this->idos->write_int(this->bs->keyEvents->ELMP[1]);
	ret &= this->idos->write_unsigned_short(this->bs->keyEvents->ORL);
	ret &= this->idos->write_int(this->bs->keyEvents->RLMP[0]);
	ret &= this->idos->write_int(this->bs->keyEvents->RLMP[1]);
	return ret;
}

int BellcoreWriter::write_lnkParams(int i) {
	if (this->bs->map->B_size[i] == 0)
		return 0;
	int ret = 0x00000001;
	ret &= this->idos->write_short(this->bs->lnkParams->TNL);
	int k;
	for (k = 0; k < this->bs->lnkParams->TNL; k++) {
		ret &= this->idos->write_short(this->bs->lnkParams->LMN[k]);
		ret &= this->idos->write_char(this->bs->lnkParams->LMC[k][0]);
		ret &= this->idos->write_char(this->bs->lnkParams->LMC[k][1]);
		ret &= this->idos->write_int(this->bs->lnkParams->LML[k]);
		ret &= this->idos->write_short(this->bs->lnkParams->REN[k]);
		ret &= this->idos->write_int(this->bs->lnkParams->GPA[k][0]);
		ret &= this->idos->write_int(this->bs->lnkParams->GPA[k][1]);
		ret &= this->idos->write_short(this->bs->lnkParams->FCI[k]);
		ret &= this->idos->write_int(this->bs->lnkParams->SMI[k]);
		ret &= this->idos->write_int(this->bs->lnkParams->SML[k]);
		ret &= this->idos->write_char(this->bs->lnkParams->USML[k][0]);
		ret &= this->idos->write_char(this->bs->lnkParams->USML[k][1]);
		ret &= this->idos->write_short(this->bs->lnkParams->MFDL[k]);
		ret &= this->idos->write_string(this->bs->lnkParams->CMT[k]);
	}
	return ret;
}

int BellcoreWriter::write_dataPts(int i) {
	if (this->bs->map->B_size[i] == 0)
		return 0;
	int ret = 0x00000001;
	ret &= this->idos->write_int(this->bs->dataPts->TNDP);
	ret &= this->idos->write_short(this->bs->dataPts->TSF);
	int k, l;
	for (k = 0; k < this->bs->dataPts->TSF; k++) {
		ret &= this->idos->write_int(this->bs->dataPts->TPS[k]);
		ret &= this->idos->write_short(this->bs->dataPts->SF[k]);
		for (l = 0; l < this->bs->dataPts->TPS[k]; l++)
			ret &= this->idos->write_unsigned_short(this->bs->dataPts->DSF[k][l]);
	}
	return ret;
}

int BellcoreWriter::write_cksum(int i) {
	//טת...
	return 1;
}

int BellcoreWriter::write_special(int j, int i) {
	if (this->bs->map->B_size[i] == 0)
		return 0;
	int ret = 0x00000001;
	unsigned int k;
	for (k = 0; k < this->bs->special[j]->get_size(); k++)
		ret &= this->idos->write_char(this->bs->special[j]->spec_data[k]);
	return ret;
}
