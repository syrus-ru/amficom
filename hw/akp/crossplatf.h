#ifndef AKP_CROSSPLATF_H
#define AKP_CROSSPLATF_H



#ifdef _WIN32

#include <winsock.h>

typedef unsigned int uint32_t;

#endif



#ifdef __unix__

#include <netinet/in.h>

#endif



#endif //AKP_CROSSPLATF_H
