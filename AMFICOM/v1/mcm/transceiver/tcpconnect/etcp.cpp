#include "etcp.h"

/**
*	������� �� ����� �����(IP-������) � ����� ������
*	(��� ������ ����� + ���������) ��������� ��������
*	��� �������� ��������� ����� - sockaddr_in.
*/
void set_address (char * hostName, char * serviceName,
                  sockaddr_in * sap, char * protocol)
{
  bzero(sap,sizeof(*sap)); //�������� ���������
  
  sap->sin_family = AF_INET; //�������� ������������ - Internet

  if (hostName != NULL)
  {
    if (inet_aton(hostName,&sap->sin_addr))
	//����� ���������, ������� ��������, ������� � ���� 
	//������ ��������� sockaddr_in.
    {
	//�������� ������������� ��� ����� � IP-�����
      hostent * hostInfo = gethostbyname(hostName);
      if (hostInfo == NULL)
        printf ("Unknown host ",hostName);
      else
        sap->sin_addr = *(in_addr *) hostInfo->h_addr;
    }
  }
  else
	//������ � ����� ������� �����������
	sap->sin_addr.s_addr = htonl(INADDR_ANY);

  //�������� � ������� strtol(...) �������� �������� �����
  //(������ ������������� �� '\0')
  char * endptr;  
  short port = strtol (serviceName,&endptr,0);

  if (*endptr == '\0')
    sap->sin_port = htons(port);//����������� � big-endian �������
  else
  {
	//���� ��� �� ����, ������� ��� ��� ������������� ��� ������
	//(�������� � etc/services ����� ������ klmn 767/udp)
    servent * sp = getservbyname (serviceName,protocol);
    if (sp == NULL)
      printf ("unknown service: ",serviceName);
    sap->sin_port = sp->s_port;
  }
}

/**
* ���������� ��� ��������� ���� ����� � ������
* �������������� �����.
*/
SOCKET tcp_server (char * hostName,char * serviceName)
{
  //��������� ��������� sockaddr
  sockaddr_in server_peer;
  set_address (hostName,serviceName,&server_peer,"tcp");

  //������ � ������� ����� ��� ����������� ����������.
  SOCKET listened_socket = socket(AF_INET,SOCK_STREAM,0);

  if (listened_socket == INVALID_SOCKET)
  {
	printf ("Error calling socket(..) for server socket!\n");
    return -1;
  }

  //��������� TCP ������ ������ �������� � ���
  //������������� ����� - ����������� ��� �������.
  const int on = 1;
  if (setsockopt (listened_socket,
                  SOL_SOCKET,
                  SO_REUSEADDR,
                  (char *)&on,
                  sizeof(on)))
  {
    printf ("Error calling setsockopt!\n");
    return -1;
  }
  //����������� ����� ���������� � ����� ����� � ��������������� ������
  int testValue =
	  bind(listened_socket, (struct sockaddr *)&server_peer,sizeof(server_peer));
  if (testValue != 0)
  {
	printf ("Error binding interface address to the socket.\n");
    return -1;
  }

  //�������� ����� ��� ��������������, 5 - ����� ��������� � ������� ����������
  testValue = listen(listened_socket, 5);
  if (testValue != 0)
  {
	printf ("Error calling listen.\n");
    return -1;
  }

  return listened_socket;
}


SOCKET tcp_client (char * hname,char * sname)
{
  sockaddr_in peer;
  SOCKET client_socket;

  set_address (hname,sname,&peer,"tcp");
  client_socket = socket(AF_INET,SOCK_STREAM,0);
  if (client_socket == INVALID_SOCKET)
  {
    printf ("Error calling socket(...) for client socket!\n");
    return -1;
  }

  if (connect (client_socket,(sockaddr *)&peer,sizeof(peer)))
  {
    printf ("Error calling connect(...) for client socket! ");
	printf (".\n");
    return -1;
  }

  return client_socket;
}

//Receives parts of data with maximum interval of 3 seconds
int readNBytesFromTCP (SOCKET s, char *buff, int size, int flags)
{
	int bytesRead = 0;
	while (bytesRead < size)
	{
		int rc;
		timeval tv;
		tv.tv_sec = TIMEOUT;
		tv.tv_usec = 0;

		fd_set in;
		FD_ZERO (&in);
		FD_SET (s,&in);

		rc = select (s + 1, &in, NULL, NULL, &tv);
		if (rc){
			rc = recv(s,buff + bytesRead,size - bytesRead,flags);
			if (rc > 0)
				bytesRead += rc;
			else break;
		} else break;
	}

	return bytesRead;
}

//Sends data by parts
int sendNBytesToTCP (SOCKET s, char * buff, int size, int flags)
{
	int bytesSent = 0;
	int rc;
	while (bytesSent < size)
	{
		rc = send(s,buff + bytesSent,size - bytesSent,flags);

		if (rc <= 0)
			break;

		bytesSent += rc;
	}

	return bytesSent;
}
