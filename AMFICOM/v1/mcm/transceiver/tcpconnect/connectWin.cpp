#ifdef _WIN32

#define EMSGSIZE WSAEMSGSIZE

#include "connect.h"

void init ()
{
  WSADATA wsadata;
  WSAStartup (MAKEWORD(2,2),&wsadata);
}

//Функция переводит адрес из char в unsigned long,
//при этом осуществляя проверку адреса на легитимность
int inet_aton (char * cp, in_addr * pin)
{
  int rc;

  rc = inet_addr(cp);
  //Просто inet_addr(...) вернёт INADDR_NONE (0xffffffff)
  //в случае неверного адреса - то же, что и для
  //broadcoast адреса.
  if (rc == -1 && strcmp (cp,"255.255.255.255"))
    return 1;

  pin->s_addr = rc;
  return 0;
}

void closeExt (SOCKET connected_socket)
{
  if ( closesocket ( connected_socket ) )
    cout << errno << "Ошибка вызова close";
}

void exitConnection(int code)
{
  WSACleanup();
  exit(code);
}

void bzero(void * b, int n)
{
  memset (b,0,n);
}
#endif