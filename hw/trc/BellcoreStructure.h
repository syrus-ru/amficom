#ifndef BELLCORE_STRUCTURE_H
#define BELLCORE_STRUCTURE_H

class BellcoreStructure {
	public:
		static const unsigned char MAP;
		static const unsigned char GENPARAMS;
		static const unsigned char SUPPARAMS;
		static const unsigned char FXDPARAMS;
		static const unsigned char KEYEVENTS;
		static const unsigned char LNKPARAMS;
		static const unsigned char DATAPOINTS;
		static const unsigned char SPECIAL;
		static const unsigned char CKSUM;

		static const char* MAP_STR;
		static const char* GENPARAMS_STR;
		static const char* SUPPARAMS_STR;
		static const char* FXDPARAMS_STR;
		static const char* KEYEVENTS_STR;
		static const char* LNKPARAMS_STR;
		static const char* DATAPOINTS_STR;
		static const char* SPECIAL_STR;
		static const char* CKSUM_STR;

		//------------------ Map -------------------//
		class Map {
			public:
				unsigned short MRN;	// Map Revision Number
				int MBS;	// Map Block Size (in bytes)
				short NB;	// Number of Blocks (including Map)

				// Block Info:
				char** B_id;	// Block ID
				unsigned short* B_rev;	// Block Revision Number
				int* B_size;	// Block Size (in bytes)

				Map(BellcoreStructure* bs);
				virtual ~Map();
				unsigned int get_size() const;
		};

		//----------- General Parameters ------------//
		class GenParams {
			public:
				static const int LENGTH_LC;
				static const int LENGTH_CDF;

				char* LC;	// Language Code (2 bytes)
				char* CID;	// Cable ID
				char* FID;	// Fiber ID
				short FT;	// Fiber Type
				short NW;	// Nominal Wavelength
				char* OL;	// Originating Location
				char* TL;	// Terminating Location
				char* CCD;	// Cable Code
				char* CDF;	// Current Data Flag (2 bytes)
				int UO;		// User Offset
				int UOD;	// User Offset Distance
				char* OP;	// Operator
				char* CMT;	// Comment

				GenParams(const char* CID,
						const char* FID,
						const short FT,
						const short NW,
						const char* OL,
						const char* TL,
						const char* CCD,
						const char* CDF,
						const char* OP,
						const char* CMT);
				virtual ~GenParams();
				unsigned int get_size() const;
		};

		//----------- Supplier Parameters ------------//
		class SupParams {
			public:
				char* SN;	// Supplier Name
				char* MFID;	// OTDR Mainframe ID
				char* OTDR;	// OTDR Mainframe SerNum
				char* OMID;	// Optical Module ID
				char* OMSN;	// Optical Module SerNum
				char* SR;	// Software Revision
				char* OT;	// Other

				SupParams(const char* SN,
						const char* MFID,
						const char* OTDR,
						const char* OMID,
						const char* OMSN,
						const char* SR,
						const char* OT);
				virtual ~SupParams();
				unsigned int get_size() const;
		};

		//----------- Fixed Parameters ------------//
		class FxdParams	{
			public:
				static const int LENGTH_UD;

				unsigned int DTS;	// Date/Time Stamp (in ms)
				char* UD;		// Units of Distanse (2 bytes)
				short AW;		// Actual Wavelength
				int AO;			// Acquision Offset
				int AOD;		// Acquision Offset Distance
				short TPW;		// Total Number of Pulse Width Used
				short* PWU;		// Pulse Width Used
				int* DS;		// Data Spacing
				int* NPPW;		// Number of Data Points for Each Pulse Width
				int GI;			// Group Index
				short BC;		// Backscatter Coefficient
				int NAV;		// Number of Averages
				int AT;			// Averaging Time --- ??
				int AR;			// Acquision Range
				int ARD;		// Acquision Range Distance
				int FPO;		// Front Panel Offset
				unsigned short NF;	// Noise Floor Level
				unsigned short NFSF;	// Noise Floor Scale Factor
				unsigned short PO;	// Power Offset First Point
				unsigned short LT;	// Loss Threshold
				unsigned short RT;	// Reflectance Threshold
				unsigned short ET;	// End-of-Fiber Threshold
				char* TT;		// Trace Type -- ??
				int* WC;		// Window Coordinates

				FxdParams(const unsigned int DTS,
						const char* UD,
						const short AW,
						const int AO,
						const short TPW,
						const short* PWU,
						const int* DS,
						const int* NPPW,
						const int GI,
						const int NAV,
						const int AR);
				virtual ~FxdParams();
				unsigned int get_size() const;
		};

		//----------- Key Events ------------//
		class KeyEvents	{
			public:
				static const int LENGTH_EC;
				static const int LENGTH_LMT;
				static const int LENGTH_ELMP;
				static const int LENGTH_RLMP;

				short TNKE;		// Number of Key Events
				short* EN;		// Event Number
				int* EPT;		// Event Propagation Time
				short* ACI;		// Attenuation Coefficient Lead-in-Fiber
				short* EL;		// Event Loss
				int* ER;		// Event Reflectance
				char** EC;		// Event Code (6 bytes)
				char** LMT;		// Loss Measurement Event (2 bytes)
				char** CMT;		// Comment
				int EEL;		// End-to-End Loss
				int* ELMP;		// End-toEnd Marker Positions (2 ints)
				unsigned short ORL;	// Optical Return Loss
				int* RLMP;		// Optical Return Loss Marker Positions (2 ints)

				KeyEvents(const short TNKE,
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
						const int* RLMP);
				virtual ~KeyEvents();
				unsigned int get_size() const;
		};

		//----------- Link Parameters ------------//
		class LnkParams	{
			public:
				static const int LENGTH_LMC;
				static const int LENGTH_GPA;
				static const int LENGTH_USML;

				short TNL;	// Total Number of Landmarks
				short* LMN;	// Landmark Number
				char** LMC;	// Landmark Code (2 bytes)
				int* LML;	// Landmark Location
				short* REN;	// Related Event Number
				int** GPA;	// GPS Info - longitude, latitude (2 ints)
				short* FCI;	// Fiber Correction Factor Lead-in-Fiber
				int* SMI;	// Stealth Marker Entering Landmark
				int* SML;	// Stealth Marker Leaving Landmark
				char** USML;	// Units of Stealth Marker Leaving Landmark (2 bytes)
				short* MFDL;	// Mode Field Diameter Leaving Landmark
				char** CMT;	// Comment

				LnkParams(const short TNL,
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
						const char** CMT);
				virtual ~LnkParams();
				unsigned int get_size() const;
		};

		//----------- Data Points ------------//
		class DataPts {
			public:
				int TNDP;		// Number of Data Points
				short TSF;		// Total number Scale Factor Used
				int* TPS;		// Total Data Points Using Scale Factor i
				short* SF;		// Scale Factor i
				unsigned short** DSF;	// Data

				DataPts(const int TNDP,
						const short TSF,
						const int* TPS,
						const short* SF,
						unsigned short** DSF);
				virtual ~DataPts();
				unsigned int get_size() const;
		};

		//----------- Checksum ------------//
		class Cksum {
			public:
				unsigned short CSM;	// Checksum

				Cksum(const unsigned short CSM);
				virtual ~Cksum();
				unsigned int get_size() const;
		};

		//----------- Special Field ------------//
		class Special {
			public:
				unsigned int size;	// Size Of Special Field
				char* data;	// Special Data Field

				Special(unsigned int size, char* data);
				virtual ~Special();
				unsigned int get_size() const;
		};

		// флаг, показывающий есть ли в файле определенное поле
		char hasMap;
		char hasGen;
		char hasSup;
		char hasFxd;
		char hasKey;
		char hasLnk;
		char hasData;
		char hasSpecial;
		int specials;	// число полей specials (может быть произвольным)

		// экземпляры классов, представляющих собой поля данных в формате bellcore
		Map* map;
		GenParams* genParams;
		SupParams* supParams;
		FxdParams* fxdParams;
		KeyEvents* keyEvents;
		LnkParams* lnkParams;
		DataPts* dataPts;
		Cksum* cksum;
		Special** special;

		BellcoreStructure();
		virtual ~BellcoreStructure();
//		void addField (unsigned char type);
		void add_field_map();
		void add_field_gen_params(const char* CID,
				const char* FID,
				const short FT,
				const short NW,
				const char* OL,
				const char* TL,
				const char* CCD,
				const char* CDF,
				const char* OP,
				const char* CMT);
		void add_field_sup_params(const char* SN,
				const char* MFID,
				const char* OTDR,
				const char* OMID,
				const char* OMSN,
				const char* SR,
				const char* OT);
		void add_field_fxd_params(const unsigned int DTS,
				const char* UD,
				const short AW,
				const int AO,
				const short TPW,
				const short* PWU,
				const int* DS,
				const int* NPPW,
				const int GI,
				const int NAV,
				const int AR);
		void add_field_key_events(const short TNKE,
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
				const int* RLMP);
		void add_field_lnk_params(const short TNL,
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
				const char** CMT);
		void add_field_data_pts(const int TNDP,
				const short TSF,
				const int* TPS,
				const short* SF,
				unsigned short** DSF);
		void add_field_cksum(const unsigned short CSM);
		void add_field_special(unsigned int size, char* data);
		unsigned int get_size() const;

//	private:
		static void copy_string(char* copy_str, const char* str);
};

#endif

