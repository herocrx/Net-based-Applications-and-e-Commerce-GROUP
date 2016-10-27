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

int main(int argc, char *argv[])
{   
    bool flag = true;
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;
    char buffer[256];
    if(flag){
        cout << "Liczba argumentow: " << argc << endl;
        for ( int i = 0 ; i < argc;  i++) 
         cout << "Argument " << i <<" to: " << argv[i] << endl; 
    }
    flag = false;
    if (argc < 3) {
       fprintf(stderr,"usage %s hostname port\n", argv[0]);
       exit(0);
    }
    portno = atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
         (char *)&serv_addr.sin_addr.s_addr,
         server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");
    printf("Please enter the message: ");
    /*
     *
     *
     *Send file
     *
     *
     */
    fstream fs;
    fs.open ("bash_cheat_sheet.pdf", fstream::in | fstream::out | fstream::app);
    if( fs.good() == true ){
        cout << "There's an access to a file!" << endl;

    }
    else 
        cout << "Access to the file is forbidden!"<< endl;
    long int length = fs.tellg();
    fs.seekg (0,ios::end);
    length = fs.tellg() - length;
    fs.seekg(0, ios::beg);  
    cout << "The variabe size has value: " << length << endl;
    const int size_of_packet = length;
    double procent;
    int count_bits = 0;
    cout << "START SENDNING DATA" << endl;
    cout << "--------------------------------------" << endl;
    while(length>count_bits){
        if(fs.read(buffer,size_of_packet) == "-1")
            cout << "error!" << endl;
        else
            cout << "no error!" << endl;
        n = write(sockfd,buffer,size_of_packet);
        if (n < 0) 
             error("ERROR writing to socket");
        count_bits = count_bits + size_of_packet;
        procent = (double(count_bits)/length)*100;
        cout << int(procent) << "% ";
    }
    if(count_bits>length){
        n = write(sockfd,buffer,(count_bits-length));
    }
    cout << procent << "100%" << endl;
    cout << "--------------------------------------" << endl;
    fs.close();
    bzero(buffer,256);
    n = read(sockfd,buffer,255);
    if (n < 0) 
         error("ERROR reading from socket");
    printf("%s\n",buffer);
    close(sockfd);
    return 0;
    }
