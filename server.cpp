/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <iostream>
#include <fstream>
using namespace std;

#define MAXBUFSIZE 65536

void error(const char *msg)
{
    perror(msg);
    exit(1);
}

class Server{
    private:  
          int sockfd, newsockfd;
          int PortNumber;
          socklen_t serlen;
          char *buffer;
          struct sockaddr_in serv_addr, cli_addr;
          int n;
    public: 
      Server(int PN):PortNumber(PN){}
          ~Server();
          bool ReceiveUDPinfo();
};

Server::~Server(){
    cout << "Clean everything" << endl;
}
bool Server::ReceiveUDPinfo(){
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    serv_addr.sin_port = htons(PortNumber);
    if (bind(sockfd, (struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
      error("ERROR on binding");
  listen(sockfd,5);
    serlen = sizeof(serv_addr);
    cout << "Wait for the message!" << endl;
    int status;
    status = recvfrom(sockfd, buffer,MAXBUFSIZE, 0, (struct sockaddr *)&serv_addr,&serlen);
    if (status < 0 )
        cout << "Cannot get an UDP message" << endl;
     cout << "TWOJ BUFEREK: " << *buffer <<" rozmiar: "  << status <<  endl;
     status = recvfrom(sockfd, buffer, MAXBUFSIZE, 0, (struct sockaddr *)&serv_addr,&serlen);
    if (status < 0 )
        cout << "Cannot get an UDP message" << endl;
    cout << "TWOJ BUFEREK: " << *buffer << endl;
      status = recvfrom(sockfd, buffer, MAXBUFSIZE, 0, (struct sockaddr *)&serv_addr,&serlen);
    if (status < 0 )
        cout << "Cannot get an UDP message" << endl;
    cout << "TWOJ BUFEREK: " << buffer << endl;
   return true;
 }


int main(int argc, char *argv[])
{
      if (argc < 2) {
          fprintf(stderr,"ERROR, no port provided\n");
          exit(1);
      }
      while(1){
         fstream fs;
         int Convert_Value = atoi(argv[1]);
         cout << Convert_Value << endl;
         Server Server1 = Server(Convert_Value);
         Server1.ReceiveUDPinfo();
         cout << "Wykonano operacje jedziemy dalej!" << endl;
      }                             
    return 0; 
  }
