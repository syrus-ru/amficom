#ifndef __ETCP_H__
#define __ETCP_H__

#include "connect.h"

#ifdef __SVR4
#define SOCKET int
//#define bzero(b,n) memset((b), 0, (n))
#endif

#define MSG_PULSE 0
#define MSG_ERROR 1

typedef void (*tofunc_t) (void *);

class KISMessage
{
  public:
	int message_length;
	int message_type;

	char * message;

	KISMessage()
    {
		message_type = 0;
        message_length = 0;
		message = NULL;
    }
};

void set_address (char * hname, char * sname,
                  sockaddr_in * sap, char * protocol);

SOCKET tcp_server (char * hname,char * sname);

SOCKET tcp_client (char * hname,char * sname);

#endif