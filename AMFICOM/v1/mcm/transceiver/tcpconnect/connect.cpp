#include "connect.h"

void init ()
{
  WSADATA wsadata;
  WSAStartup (MAKEWORD(2,2),&wsadata);
}

//������� ��������� ����� �� char � unsigned long,
//��� ���� ����������� �������� ������ �� ������������
int inet_aton (char * cp, in_addr * pin)
{
  int rc;

  rc = inet_addr(cp);
  //������ inet_addr(...) ����� INADDR_NONE (0xffffffff)
  //� ������ ��������� ������ - �� ��, ��� � ���
  //broadcoast ������.
  if (rc == -1 && strcmp (cp,"255.255.255.255"))
    return 1;

  pin->s_addr = rc;
  return 0;
}

void close (SOCKET connected_socket)
{
  if ( closesocket ( connected_socket ) )
    cout << errno << "������ ������ close";
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