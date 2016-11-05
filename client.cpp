#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <iostream>
#include <fstream>
using namespace std;


void error(const char *msg)
{
    perror(msg);
    exit(0);
}

class client{ 
    private:
        char buffer[256];
        bool UDPmessage;
        char NameOfFile[];
        char  *host_name;
        int Port_Number;
        string IP_Client;
    public:
        bool SendUDPmessage();
        void GetDataToServer();
        client(char*,char*);
        bool EstablishConnection();
        bool ReceiveFile();
        ~client(); 
};

bool client::ReceiveFile(){
     int sockfd;
     struct sockaddr_in serv_addr;
     struct hostent *server;
     server = gethostbyname(host_name);
     if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
     }
     sockfd = socket(AF_INET, SOCK_STREAM, 0);
     if (sockfd < 0) 
        error("ERROR opening socket");
     bzero((char *) &serv_addr, sizeof(serv_addr));
     serv_addr.sin_family = AF_INET;
     bcopy((char *)server->h_addr, 
          (char *)&serv_addr.sin_addr.s_addr,
          server->h_length);
     serv_addr.sin_port = htons(Port_Number);
     if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");   fstream fs;
     fs.open (NameOfFile, fstream::in | fstream::out | fstream::app);
     if( fs.good() == true ){
         cout << "There's an access to a file!" << endl;
     }
     else 
        cout << "Access to the file is forbidden!"<< endl; 

    close(sockfd); 
}

client::client(char *host_name, char *Port):host_name(host_name){
   Port_Number = atoi(Port);
   UDPmessage = true;      
}
client::~client(){}

bool client::EstablishConnection(){
}

void client::GetDataToServer(){
    cout << "Give name of the file: ";
    cin >> NameOfFile;
    cout <<endl << "Give port number: ;";
    cin >> Port_Number;
    IP_Client = "127.0.0.1";
}


bool client::SendUDPmessage(){
    int sockfd;
    int sockfd_new;
    struct sockaddr_in serv_addr;
    struct hostent *server;
    cout << host_name <<endl;
    server = gethostbyname(host_name);
    if (server == NULL) {
         fprintf(stderr,"ERROR, no such host\n");
         exit(0);
    }
    sockfd = socket(AF_INET,SOCK_DGRAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,(char *)&serv_addr.sin_addr.s_addr,server->h_length);
    serv_addr.sin_port = htons(Port_Number);
    //sockfd_new = connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr));
       // error("ERROR connecting");
     if(sendto(sockfd,&NameOfFile,sizeof(NameOfFile),0,(struct sockaddr *) &serv_addr,sizeof(serv_addr))<0){
        printf("Error while sending the name of the file \n");
        UDPmessage = false;
     }
     // Send Host name of Client 
    if(sendto(sockfd,&Port_Number,sizeof(Port_Number),0,(struct sockaddr *) &serv_addr,sizeof(serv_addr))<0){
        printf("Error while sending the host name\n");
        UDPmessage = false;
    }
     // Send Port number of server socket
    if(sendto(sockfd,&Port_Number,sizeof(Port_Number),0,(struct sockaddr *) &serv_addr,sizeof(serv_addr))<0){
        printf("Error while sending the port number\n");
        UDPmessage = false;
    }
    if(UDPmessage == false){
        printf("Unable to set the connection with the server \n");
    }

}

int main(int argc, char *argv[])
{   
    bool flag = true;
    if(flag){
        cout << "Liczba argumentow: " << argc << endl;
        for ( int i = 0 ; i < argc;  i++) 
         cout << "Argument " << i <<" to: " << argv[i] << endl; 
    }
    // argv1 = Ip_address of the server
    // argv2 = Port Number of the answer
    client ClientNr1(argv[1],argv[2]);    
    ClientNr1.GetDataToServer();
    ClientNr1.SendUDPmessage();
    //ClientNr1.ReceiveFile();
    ClientNr1.~client();
    return 0;    

     
}
