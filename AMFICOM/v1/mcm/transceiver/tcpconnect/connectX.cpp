#ifdef __unix__

#include "connect.h"

void init ()
{
}

void closeExt (SOCKET connected_socket)
{
  if ( close ( connected_socket ) )
    cout << errno << "Ошибка вызова close";
}

void exitConnection(int code)
{
  exit(code);
}

void bzero(void * b, int n)
{
  memset (b,0,n);
}
#endif