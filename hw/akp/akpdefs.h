#ifndef AKPDEFS_H
#define AKPDEFS_H


#define MAX_SEGMENT_LENGTH (unsigned int)0x40000
#define INTSIZE sizeof(int)

typedef enum SegmentType {SEGMENT_UNKNOWN,
							SEGMENT_KIS_INFO,
							SEGMENT_MCM_INFO,
							SEGMENT_MEASUREMENT,
							SEGMENT_RESULT};

#endif //AKPDEFS_H
