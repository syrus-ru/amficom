#ifndef AKPDEFS_H
#define AKPDEFS_H

#define INTSIZE sizeof(int)

typedef enum SegmentType {SEGMENT_UNKNOWN,
							SEGMENT_KIS_INFO,
							SEGMENT_MCM_INFO,
							SEGMENT_MEASUREMENT,
							SEGMENT_RESULT};

#ifdef _WIN32
typedef __int64 long64;
#endif

#ifdef __unix__
typedef long long long64;
#endif

#endif
