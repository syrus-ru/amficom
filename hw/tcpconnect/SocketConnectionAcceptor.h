// SocketConnectionAcceptor.h: interface for the SocketConnectionAcceptor class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SOCKETCONNECTIONACCEPTOR_H__AC07B00A_332B_4018_A4AC_8DAEA184BBC2__INCLUDED_)
#define AFX_SOCKETCONNECTIONACCEPTOR_H__AC07B00A_332B_4018_A4AC_8DAEA184BBC2__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "tcpconnect.h"

class SocketConnectionAcceptor {
	public:
		SocketConnectionAcceptor(SOCKET lsockfd);
		virtual ~SocketConnectionAcceptor();

		void accept_connection(const unsigned int timewait);
		SOCKET get_communication_socket() const;
		int communication_available() const;
		void close_communication();

	private:
		SOCKET lsockfd;
		SOCKET csockfd;

};

#endif // !defined(AFX_SOCKETCONNECTIONACCEPTOR_H__AC07B00A_332B_4018_A4AC_8DAEA184BBC2__INCLUDED_)
