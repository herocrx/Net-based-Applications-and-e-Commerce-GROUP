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
        char *buffer;
        bool UDPmessage;
        char NameOfFile[256];
        char  *host_name;
        char  *Port_Number;
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
      int socketfd, newsockfd, portno;
      socklen_t clilen;
      char buffer[256];
      struct sockaddr_in serv_addr, cli_addr;
      int n;
      socketfd = socket(AF_INET, SOCK_STREAM, 0);
      if (socketfd < 0) 
         error("ERROR opening socket");
      bzero((char *) &serv_addr, sizeof(serv_addr));
      portno = atoi(Port_Number);
      serv_addr.sin_family = AF_INET;
      serv_addr.sin_addr.s_addr = INADDR_ANY;
      serv_addr.sin_port = htons(portno);
      if (bind(socketfd, (struct sockaddr *) &serv_addr,
               sizeof(serv_addr)) < 0) 
               error("ERROR on binding");
    long int length_of_file = 0;
     int length_of_packet = 255;
     int nr_received_packet = 1; 
     listen(socketfd,5);
     clilen = sizeof(cli_addr);
     newsockfd = accept(socketfd, (struct sockaddr *) &cli_addr, &clilen);
     if (newsockfd < 0) 
          error("ERROR on accept");
     fstream fs;
     int Nr_Received_Bytes = -1;
     int Loading_Previous = 0;

     double procent;
     do{
         int cnt = 0;
         switch(nr_received_packet){
            case 1:
                cout <<"Create a program called: " <<NameOfFile << endl;
                fs.open(NameOfFile,fstream::in | fstream::out | fstream::app);
                if( fs.good() == true ){
                   cout << "There's an access to a file!" << endl; 
                }
                bzero(buffer,256); 
                n = read(newsockfd,buffer,256);
                if (n < 0) error("ERROR reading from socket");  
                length_of_file = atoi(buffer);
                cout << "Size of a file: " << buffer << endl;
                break;
            default:   
                bzero(buffer,length_of_packet); 
                Nr_Received_Bytes += length_of_packet;
                n = read(newsockfd,buffer,length_of_packet);
                if (n < 0) error("ERROR reading from socket");  
                if(Nr_Received_Bytes < length_of_file)
                     fs.write(buffer,length_of_packet);
                else{
                    while(buffer[cnt]!='\0')
                     cnt++;
                    fs.write(buffer,cnt);
                }
                procent = double(Nr_Received_Bytes)/double(length_of_file)*100;
                if(Loading_Previous != int(procent))
                    cout << "|";
                Loading_Previous = (int)procent;
                break;
     }
    nr_received_packet++;
} 
while(Nr_Received_Bytes<length_of_file);    
    cout << endl;
    fs.close();
    nr_received_packet = 1;
    Nr_Received_Bytes = 0;
    n = write(newsockfd,"I got your message",18);
    if (n < 0) error("ERROR writing to socket");
    close(newsockfd);
    close(socketfd);
    return true;
}

client::client(char *host_name,char*Port):host_name(host_name){
   Port_Number = Port;
}
client::~client(){}

bool client::EstablishConnection(){
    return true;
}

void client::GetDataToServer(){
    cout << "Give name of the file: ";
    cin >> NameOfFile;  
}


bool client::SendUDPmessage(){
    int sockfd;
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
    serv_addr.sin_port = htons(2000);
    string MSG = ',' + string(NameOfFile) + ',' + Port_Number + ',' + host_name + ','; 
    char *Message = new char[MSG.length()];
    for(unsigned int i = 0; i<MSG.length(); i++)
        Message[i] = MSG[i];
    cout << "UDP message is: " << Message << endl;
     if(sendto(sockfd,Message,200,0,(struct sockaddr *) &serv_addr,sizeof(serv_addr))<0){
        printf("Error while sending the name of the file \n");
        UDPmessage = false;
     }
     close(sockfd);
     return true;
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
    ClientNr1.ReceiveFile();
    ClientNr1.~client();
    return 0;    

     
}
