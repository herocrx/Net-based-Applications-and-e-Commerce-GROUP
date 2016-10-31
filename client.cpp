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
     char NameOfFile[256] = "Karta studenta Erasmus+.pdf"; 
   // char NameOfFile[256]  = "text.txt";
    fs.open (NameOfFile, fstream::in | fstream::out | fstream::app);
    if( fs.good() == true ){
        cout << "There's an access to a file!" << endl;

    }
    else 
        cout << "Access to the file is forbidden!"<< endl;
    long int length = fs.tellg();

     cout << "Nazwa pliku: " << NameOfFile << endl;
    fs.seekg (0,ios::end);
    length = fs.tellg() - length;
    fs.seekg(0, ios::beg);  
    cout << "The variabe size has value: " << length << endl;
    const int size_of_packet = 255;
    double procent;
    int procent_previous;
    int count_bits = 0;
    cout << "START SENDNING DATA" << endl;
    cout << "--------------------------------------" << endl;
    /*Send the general information about the file
     *1. Name of a file
     *2. Size of a file
     *3. Packet size
     */
     int counter =0;
     while(NameOfFile[counter] != '\0'){
        buffer[counter] = NameOfFile[counter];
         counter++;
     }
     buffer[counter] = '\0';
     write(sockfd,buffer,counter);
     sprintf(buffer,"%ld", length);
     write(sockfd,buffer,256);
     sprintf(buffer,"%ld",(long)size_of_packet);
     write(sockfd,buffer,256);
     while(length-size_of_packet>count_bits){
        count_bits = count_bits + size_of_packet;
        bzero(buffer,size_of_packet);;
        fs.read(buffer,size_of_packet); // each read the pointer shifts
        n = write(sockfd,buffer,size_of_packet);
        if (n < 0) 
             error("ERROR writing to socket");
        procent = double(count_bits)/double(length)*100;
        if(int(procent) != procent_previous)
            cout <<"|";
        procent_previous = procent; 
    }
    cout << endl;
    bzero(buffer,size_of_packet);;
    fs.read(buffer,size_of_packet); // each read the pointer shifts
    n = write(sockfd,buffer,length-count_bits);
    if (n < 0) 
    error("ERROR writing to socket");
    cout << "--------------------------------------" << endl;
    fs.close();
    bzero(buffer,255);
    n = read(sockfd,buffer,255);
    if (n < 0) 
         error("ERROR reading from socket");
    printf("%s\n",buffer);
    cout << "Modulo ( liczba pozostalych pakietow): " << length%size_of_packet << endl;
    close(sockfd);
    return 0;
    }
