#ifndef BELLCORE_WRITER_H
#define BELLCORE_WRITER_H

#include "BellcoreStructure.h"
#include "IntelDataOutputStream.h"

class BellcoreWriter {
	public:
		BellcoreWriter();
		virtual ~BellcoreWriter();
		void write(BellcoreStructure* bs);
		unsigned char* get_data() const;
		unsigned int get_data_size() const;

	private:
		BellcoreStructure* bs;
		IntelDataOutputStream* idos;

		int write_map();
		int write_genParams(int i);
		int write_supParams(int i);
		int write_fxdParams(int i);
		int write_keyEvents(int i);
		int write_lnkParams(int i);
		int write_dataPts(int i);
		int write_cksum(int i);
		int write_special(int j, int i);
};

#endif
