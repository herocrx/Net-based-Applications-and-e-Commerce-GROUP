/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <iostream>
#include <fstream>
using namespace std;
void error(const char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{

    while(1){
          int sockfd, newsockfd, portno;
          socklen_t clilen;
          char buffer[256];
          struct sockaddr_in serv_addr, cli_addr;
          int n;
          if (argc < 2) {
              fprintf(stderr,"ERROR, no port provided\n");
              exit(1);
          }
          sockfd = socket(AF_INET, SOCK_STREAM, 0);
          if (sockfd < 0) 
             error("ERROR opening socket");
          bzero((char *) &serv_addr, sizeof(serv_addr));
          portno = atoi(argv[1]);
          serv_addr.sin_family = AF_INET;
          serv_addr.sin_addr.s_addr = INADDR_ANY;
          serv_addr.sin_port = htons(portno);
          if (bind(sockfd, (struct sockaddr *) &serv_addr,
                   sizeof(serv_addr)) < 0) 
                   error("ERROR on binding");
         long int length_of_file;
         int length_of_packet;
         int nr_received_packet = 1; 
         listen(sockfd,5);
         clilen = sizeof(cli_addr);
         newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
         if (newsockfd < 0) 
              error("ERROR on accept");
         fstream fs;
         int Nr_Received_Bytes = 0;
         int Loading_Previous = 0;
         char*NameOfFile;

         double procent;
         do{
             int cnt = 0;
             int i = 0;
             switch(nr_received_packet){
                case 1:
                    bzero(buffer,256); 
                    n = read(newsockfd,buffer,256);
                    if (n < 0) error("ERROR reading from socket");  
                    cnt = 0;
                    i = 0;
                    while(buffer[cnt] != '\0'){    
                        cnt++;
                    }
                    cout << "Liczba liter: " << cnt << endl;
                    NameOfFile = new char[cnt];
                    while(buffer[i] != '\0'){
                        NameOfFile[i] = buffer[i]; 
                        i++;
                    }
                    NameOfFile[i] = '\0';
                    cout << "Name of a file: " << NameOfFile << endl;
                    fs.open(NameOfFile,fstream::in | fstream::out | fstream::app);
                     delete [] NameOfFile;
                     if( fs.good() == true ){
                      cout << "There's an access to a file!" << endl; 
                    }
                    break;
                case 2:
                    bzero(buffer,256); 
                    n = read(newsockfd,buffer,256);
                    if (n < 0) error("ERROR reading from socket");  
                    length_of_file = atoi(buffer);
                    cout << "Size of a file: " << buffer << endl;
                    break;
                case 3:
                    bzero(buffer,256); 
                    n = read(newsockfd,buffer,256);
                    if (n < 0) error("ERROR reading from socket");  
                    length_of_packet =  atoi(buffer);
                    cout <<"Size of incoming packets: " << length_of_packet << endl;
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
        cout << endl <<"Nr_Received_bytes " << Nr_Received_Bytes<< endl;
        fs.close();
        nr_received_packet = 1;
        Nr_Received_Bytes = 0;
        n = write(newsockfd,"I got your message",18);
        if (n < 0) error("ERROR writing to socket");
     //   int mask = 0xFF;
     //   int i = 0;
     //   int result[4];
     //   while(i<4){
     //       result[i] = mask&int(cli_addr.sin_addr.s_addr);
     //       i++;
     //       mask = mask<<8;
      //  }
    //    cout << result[0] << "." << result[1] << "." << result[2] <<"." << result[3]<<":" << int(cli_addr.sin_port  ) <<  endl; 
        close(newsockfd);
        close(sockfd);
        // UDP
        cout << "////////////////////////////" << endl;
        cout << "/////////// UDP ////////////" << endl;
        cout << "////////////////////////////" << endl;
        int sock;
        int bytes_sent;
        /* create an Internet, datagram, socket using UDP */
        sock = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP);
        if (sock == -1) {
        /* if socket failed to initialize, exit */
            printf("Error Creating Socket");
            exit(EXIT_FAILURE);
         }
         /* Zero out socket address */
       // memset(&sa, 0, sizeof sa);
        /* The address is IPv4 */
        //sa.sin_family = AF_INET;
        /* IPv4 adresses is a uint32_t, convert a string representation of the octets to the appropriate value */
        //sa.sin_addr.s_addr = inet_addr("127.0.0.1");
        /* sockets are unsigned shorts, htons(x) ensures x is in network byte order, set the port to 7654 */
       // sa.sin_port = htons(7654);
        fs.open(NameOfFile,fstream::in | fstream::out | fstream::app);
        int count_bits = 0;
        cout <<length_of_file << " " << length_of_packet << " " << count_bits << endl;
        Nr_Received_Bytes = 0;
        while(length_of_file-length_of_packet>count_bits){
            count_bits = count_bits + length_of_packet;
            bzero(buffer,length_of_packet);;
            fs.read(buffer,length_of_packet); // each read the pointer shifts
            bytes_sent = sendto(sock, buffer, strlen(buffer), 0,(struct sockaddr*)&cli_addr, sizeof cli_addr);
            cout << buffer;
            if (bytes_sent < 0) {
                error("UDP -> ERROR writing to the socket");
                exit(EXIT_FAILURE);
            }
            procent = double(Nr_Received_Bytes)/double(length_of_file)*100;
            if(Loading_Previous != int(procent))
                cout << "|";
                Loading_Previous = (int)procent;
            }
        bzero(buffer,length_of_packet);;
        fs.read(buffer,length_of_packet); // each read the pointer shifts
        bytes_sent = sendto(sock, buffer, strlen(buffer), 0,(struct sockaddr*)&cli_addr, sizeof cli_addr);
        cout << buffer;
        if (bytes_sent < 0){
            error("UDP -> ERROR writing to the socket");
            exit(EXIT_FAILURE); 
        }                         
        cout << "Wykonano operacje jedziemy dalej!" << endl;
         close(sock); /* close the socket */
         fs.close();
    }
    return 0; 
  }
