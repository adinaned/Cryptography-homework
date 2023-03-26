// #define plainTextFile "plaintext.txt"
// #define cryptoTextFile "cryptotext.txt"
// #define decryptedTextFile "decrypted.txt"
 
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
#include <ctype.h>
#include <stdbool.h>
 
char englishAlphabet[] = "abcdefghijklmnopqrstuvwxyz";
float englishFrequencies[] = { 8.12, 1.49, 2.71, 4.32, 12.02, 2.30, 2.03, 5.92, 7.31, 0.10, 0.69, 3.98, 2.61, 6.95, 7.68, 1.82, 0.11, 6.02, 6.28, 9.10, 2.88, 1.11, 2.09, 0.17, 2.11, 0.07 };
char* plaintext = "These are some random words.";
char* key, * adjustedKey;
int keyLength, fr[26];
char cryptotext[256];

// int getIntputAndOutput(){
//     FILE *fptr;
//     fptr = fopen(plainTextFile, "r");

//     if(fptr == NULL)
//     {
//         perror("Error: ");
//         return(-1);             
//     }

//     plaintext = (char*)malloc(sizeof(char));
//     int i = 1;

//     while(fptr != NULL){
//         i += 1;
//         plaintext = (char*)realloc(plaintext, sizeof(char) * i);
//         fscanf(fptr, "%c", &plaintext[i]);
//     }

//     for (int i = 0; i < strlen(plaintext); i++)
//     {
//         printf("Number is: %s", plaintext);
//     }

//     fclose(fptr);

// }

void getRandomKey() {
    srand(time(NULL));
    keyLength = rand() % 26 + 1;
    printf("The key length is: %d\n", keyLength);
 
    char randLetter;
    int i = 0;
    int copyKeyLength = keyLength;
 
    srand(time(NULL));
 
    while (copyKeyLength > 0) {
        randLetter = englishAlphabet[rand() % 26];
        key = (char*)realloc(key, sizeof(char));
        key[i] = (char)randLetter;
        i += 1;
        copyKeyLength -= 1;
    }
 
    int multiplyKey = 1;
 
    while (strlen(plaintext) > keyLength * multiplyKey) {
        multiplyKey += 1;
    }
 
    adjustedKey = (char*)malloc(sizeof(multiplyKey * keyLength));
    printf("The key is: ");
 
    for (int i = 0; i < keyLength * multiplyKey; i++) {
        if (keyLength <= i) {
            adjustedKey[i] = key[i % keyLength];
        }
        else {
            adjustedKey[i] = key[i];
        }
        printf("%c ", toupper(adjustedKey[i]));
    }
 
    printf("\n");
}
 
void getCryptotext() {
 
    int shift;
    char copyOfPlaintext[strlen(plaintext) + 1];
    strcpy(copyOfPlaintext, plaintext);
 
    for (int i = 0; i <= strlen(copyOfPlaintext); i++) {
        if ((tolower(copyOfPlaintext[i]) < 'a' || tolower(copyOfPlaintext[i]) > 'z') && i < strlen(plaintext)) {
                strcpy(copyOfPlaintext + i, copyOfPlaintext + i + 1);
        }       
    }
 
    printf("The plaintext is: ");
 
    for (int i = 0; i < strlen(copyOfPlaintext) + 1; i++) {
        // printf("%c[%d] ", tolower(copyOfPlaintext[i]), i);
        printf("%c ", tolower(copyOfPlaintext[i]));
    }
 
    printf("\nThe shift cypher: ");
    int i;
 
    for (int i = 0; i < strlen(copyOfPlaintext); i++) {

        if(copyOfPlaintext[i] != ' '){

            for (int j = 0; j < strlen(englishAlphabet); j++) {
                if (adjustedKey[i] == englishAlphabet[j]) {
                    shift = j;
                    // printf("%d[%d] ", shift, i);
                    printf("%d ", shift);
                    break;
                }
            }
    
            for (int j = 0; j < strlen(copyOfPlaintext); j++) {
                if (tolower(copyOfPlaintext[i]) == englishAlphabet[j]) {
                    cryptotext[i] = englishAlphabet[(j + shift) % 26];
                    break;
                }
            }
        }else{
            break;
        }    
    }
    printf("\nThe cryptotext is: ");
    for (int i = 0; i < strlen(copyOfPlaintext) + 1; i++) {
        // printf("%c[%d] ", toupper(cryptotext[i]), i);
        printf("%c ", toupper(cryptotext[i]));
    }
 
    printf("\n");
}
 
void getFrequency() {
 
    int maxFrequency = 0;
    char maxFrequencyLetter;
 
    for (int i = 0; i < strlen(cryptotext); i++) {
        fr[cryptotext[i]]++;
 
        if (fr[cryptotext[i]] > maxFrequency) {
            maxFrequency = fr[cryptotext[i]];
            maxFrequencyLetter = cryptotext[i];
        }
    }
    
    printf("The most frequent character is %c (%d)\n", toupper(maxFrequencyLetter), maxFrequency);
    
}

// int determineKeyLength(){
//     bool found = false;
//     int possibleLength = 0;
//     char substring[256];
    
//     while(!found){
//         possibleLength += 1;
//         for(int i = 1; i <= possibleLength && possibleLength <=26; i++){
//             bzero(substring, 256);
//             int k = 0;


//             for(int j = i - 1; j <= strlen(cryptotext); j++){
//                 substring[k] = cryptotext[possibleLength * k + i - 1];
//                 k += 1;
//             }

//             printf("Subsirul %d de dimensiune %d: %s\n", i, possibleLength, substring);

//             if(abs(indexOfCoincidence(substring)) - 0.068 >= 0.02){
//                 found = false;
//                 break;
//             }
//         }
//     }

//     return possibleLength;
// }

// int indexOfCoincidence(char *text){
//     int ic = 0, aux;

//     for(int i = 0; i < 26; i++){
//         aux = fr[i] / strlen(text);

//         if(strlen(text) > 1){
//             aux *= (fr[i] - 1) / (strlen(text) - 1);
//         }
//         ic += aux;
//     }

//     return ic;
// }

// int mutualIndexOfCoincidence(){
//     int mic = 0, aux;

//     for(int i = 0; i < 26; i++){
//         aux = englishFrequencies[i] / 100;
//         aux *= fr[i] / strlen(cryptotext);
//         mic += aux;
//     }

//     return mic;
// }

int main()
{
    // getIntputAndOutput();
    getRandomKey();
    getCryptotext();
    getFrequency();
    // determineKeyLength();
    return 0;
}