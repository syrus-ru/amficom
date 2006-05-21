#ifndef AKP_CROSSPLATF_H
#define AKP_CROSSPLATF_H



#ifdef _WIN32

#include <winsock.h>

typedef __int64 long64;
typedef unsigned int uint32_t;

#endif



#ifdef __unix__

typedef long long long64;
#include <netinet/in.h>

#endif



typedef union {
	char bytes[sizeof(uint32_t)];
	uint32_t value;
} uint_frame;

#endif //AKP_CROSSPLATF_H

