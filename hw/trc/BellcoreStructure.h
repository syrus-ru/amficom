#ifndef BELLCORE_STRUCTURE_H
#define BELLCORE_STRUCTURE_H

class BellcoreStructure {
	public:
		static const unsigned char MAP = 1;
		static const unsigned char GENPARAMS = 2;
		static const unsigned char SUPPARAMS = 3;
		static const unsigned char FXDPARAMS = 4;
		static const unsigned char KEYEVENTS = 5;
		static const unsigned char LNKPARAMS = 6;
		static const unsigned char DATAPOINTS = 7;
		static const unsigned char SPECIAL = 8;
		static const unsigned char CKSUM = 9;

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
				const char** B_id;	// Block ID
				unsigned short* B_rev;	// Block Revision Number
				int* B_size;	// Block Size (in bytes)

				Map();
				virtual ~Map();
				unsigned int get_size() const;
		};

		//----------- General Parameters ------------//
		class GenParams {
			public:
				const char* LC;	// Language Code (2 bytes)
				const char* CID;	// Cable ID
				const char* FID;	// Fiber ID
				short FT;	// Fiber Type
				short NW;	// Nominal Wavelength
				const char* OL;	// Originating Location
				const char* TL;	// Terminating Location
				const char* CCD;	// Cable Code
				const char* CDF;	// Current Data Flag (2 bytes)
				int UO;		// User Offset
				int UOD;	// User Offset Distance
				const char* OP;	// Operator
				const char* CMT;	// Comment

				GenParams();
				virtual ~GenParams();
				unsigned int get_size() const;
		};

		//----------- Supplier Parameters ------------//
		class SupParams {
			public:
				const char* SN;	// Supplier Name
				const char* MFID;	// OTDR Mainframe ID
				const char* OTDR;	// OTDR Mainframe SerNum
				char OMID[9];	// Optical Module ID
				const char* OMSN;	// Optical Module SerNum
				const char* SR;	// Software Revision
				const char* OT;	// Other

				SupParams();
				virtual ~SupParams();
				unsigned int get_size() const;
		};

		//----------- Fixed Parameters ------------//
		class FxdParams	{
			public:
				unsigned int DTS;	// Date/Time Stamp (in ms)
				const char* UD;		// Units of Distanse (2 bytes)
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
				const char* TT;		// Trace Type -- ??
				int* WC;		// Window Coordinates

				FxdParams();
				virtual ~FxdParams();
				unsigned int get_size() const;
		};

		//----------- Key Events ------------//
		class KeyEvents	{
			public:
				short TNKE;		// Number of Key Events
				short* EN;		// Event Number
				int* EPT;		// Event Propagation Time
				short* ACI;		// Attenuation Coefficient Lead-in-Fiber
				short* EL;		// Event Loss
				int* ER;		// Event Reflectance
				char** EC;		// Event Code (6 bytes)
				char** LMT;		// Loss Measurement Event (2bytes)
				char** CMT;		// Comment
				int EEL;		// End-to-End Loss
				int* ELMP;		// End-toEnd Marker Positions
				unsigned short ORL;	// Optical Return Loss
				int* RLMP;		// Optical Return Loss Marker Positions

				KeyEvents();
				virtual ~KeyEvents();
				unsigned int get_size() const;
		};

		//----------- Link Parameters ------------//
		class LnkParams	{
			public:
				short TNL;	// Total Number of Landmarks
				short* LMN;	// Landmark Number
				const char** LMC;	// Landmark Code (2 bytes)
				int* LML;	// Landmark Location
				short* REN;	// Related Event Number
				int** GPA;	// GPS Info - longitude, latitude (2 ints)
				short* FCI;	// Fiber Correction Factor Lead-in-Fiber
				int* SMI;	// Stealth Marker Entering Landmark
				int* SML;	// Stealth Marker Leaving Landmark
				const char** USML;	// Units of Stealth Marker Leaving Landmark (2 bytes)
				short* MFDL;	// Mode Field Diameter Leaving Landmark
				const char** CMT;	// Comment

				LnkParams();
				virtual ~LnkParams();
				unsigned int get_size() const;
		};

		//----------- Data Points ------------//
		class DataPts {
			public:
				int TNDP;	// Number of Data Points
				short TSF;	// Total number Scale Factor Used
				int* TPS;	// Total Data Points Using Scale Factor i
				short* SF;	// Scale Factor i
				unsigned short** DSF;	// Data

				DataPts();
				virtual ~DataPts();
				unsigned int get_size() const;
		};

		//----------- Checksum ------------//
		class Cksum {
			public:
				unsigned short CSM;	// Checksum

				Cksum();
				virtual ~Cksum();
				unsigned int get_size() const;
		};

		//----------- Special Field ------------//
		class Special {
			public:
				unsigned int size;		// Size Of Special Field
				char* spec_data;	// Special Data Field

				Special();
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
		void addField (unsigned char type);
		unsigned int get_size() const;
};

#endif

