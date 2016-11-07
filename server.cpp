/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <iostream>
#include <fstream>
using namespace std;
void error(const char *msg)
{
    perror(msg);
    exit(1);
}

class Server{
    private:  
          void AssignReceivedPacket();
          int PortNumber;
          char buffer[256];
          int n;
          char NameOfFile[256];
          int TcpPortConnection;
          char ClientIp[256];
    public: 
          Server(){
             PortNumber = 2000;
          }
          ~Server();
          bool ReceiveUDPinfo();
          bool SendFile();
};

void Server::AssignReceivedPacket(){
    if(buffer[0] == ','){
        int i = 1;
        int counter =0;
while(buffer[i] != ','){
            NameOfFile[counter] = buffer[i] ;
            i++;
            counter++;
        }
        i++;
        NameOfFile[counter] = '\0';
        counter = 0;
        char Port[256];
        while(buffer[i] != ','){
            Port[counter] = buffer[i];
            i++;
            counter++;
        }
        counter = 0;
        TcpPortConnection = atoi(Port);
        i++;
        while(buffer[i] != ','){
           ClientIp[counter] = buffer[i];
           i++;
           counter++;
        }
        ClientIp[counter] = '\0';
        cout << "Received data:" << endl;
        cout << "   Name of file: " << NameOfFile << endl;
        cout << "   Port of TCP connection: " << TcpPortConnection << endl;
        cout << "   Client IP address: " << ClientIp << endl;
    }
}


bool Server::SendFile(){
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    hostent *server;
    char buffer[256];
    portno = TcpPortConnection;
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    server = gethostbyname(ClientIp);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,(char *)&serv_addr.sin_addr.s_addr,server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");
    fstream fs;
    fs.open (NameOfFile, fstream::in | fstream::out | fstream::app);
    if( fs.good() == true ){
        cout << "There's an access to a file!" << endl;
    }
    else 
        cout << "Access to the file is forbidden!"<< endl;
    long int length = fs.tellg();
    fs.seekg (0,ios::end);
    length = fs.tellg() - length;
    fs.seekg(0, ios::beg);  
    const int size_of_packet = 255;
    double procent;
    int procent_previous;
    int count_bits = 0;
    bzero(buffer,256);
    sprintf(buffer,"%ld", length);
    write(sockfd,buffer,256);
    while(length-size_of_packet>count_bits){
        count_bits = count_bits + size_of_packet;
        bzero(buffer,size_of_packet);
        fs.read(buffer,size_of_packet); // each read the pointer shifts
        n = write(sockfd,buffer,size_of_packet);
        if (n < 0) 
            error("ERROR writing to socket");
        procent = double(count_bits)/double(length)*100;
        if(int(procent) != procent_previous)
            cout <<"|";
         procent_previous = procent; 
    }
    bzero(buffer,size_of_packet);
    fs.read(buffer,size_of_packet); // each read the pointer shifts
    n = write(sockfd,buffer,length-count_bits);
    if (n < 0) 
        error("ERROR writing to socket");
    fs.close();
    bzero(buffer,255);
    n = read(sockfd,buffer,255);
    if (n < 0) 
        error("ERROR reading from socket");
    close(sockfd); 
    return true;
}


Server::~Server(){
   // cout << endl << "Clean everything" << endl;
}

bool Server::ReceiveUDPinfo(){
    socklen_t serlen;
    struct sockaddr_in serv_addr;
    int sockfd;
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0){     
        cout <<"ERROR opening socket" << endl;
        return false;
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(PortNumber);
    if (bind(sockfd, (struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
        cout << "ERROR on binding" << endl;
    listen(sockfd,5);
    serlen = sizeof(serv_addr);
    int status;
    status = recvfrom(sockfd,buffer,255, 0, (struct sockaddr *)&serv_addr,&serlen);
    if (status < 0 )
        cout << "Cannot get an UDP message" << endl;
    AssignReceivedPacket();
    close(sockfd); 
    return true;
 }


int main(int argc, char *argv[])
{
    while(1){
         fstream fs;
         Server Server1 = Server();
         Server1.ReceiveUDPinfo();
         Server1.SendFile();
      }                             
    return 0; 
  }
