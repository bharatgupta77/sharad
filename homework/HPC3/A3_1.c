#include<stdio.h>
#include<omp.h>
#include<stdlib.h>


void bubble(int *a,int N)
{

          for(int i=0;i<N-1;i++)
          {
               
               for (int j=0;j<N-i-1;j++)
               {
                  int temp;
                   if(a[j]>a[j+1])
                   {
                    temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                   }
               }
          }
      
}
int main(int argc,char *argv[])
{
      int size= 8;
      int a[size];
          printf("enter the elements:");
          for (int i=0;i<size;i++)
          {
             scanf("%d",&a[i]);
          }

      int N=size;

      double start,end,start1,end1;

      start1=omp_get_wtime();
      bubble(a,N);
      end1=omp_get_wtime();


       for (int i=0;i<size;i++)
          {
             printf("%d \t",a[i]);
          }

      printf("\n--------\nTime Require0d for serial=%f\n",end1-start1);

      





      start=omp_get_wtime();
          for(int i=0;i<N-1;i++)
          {
               #pragma omp parallel for
               for (int j=0;j<N-i-1;j++)
               {
                  int temp;
                   if(a[j]>a[j+1])
                   {
                    temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                   }
               }
          }
      end=omp_get_wtime();




      for (int i=0;i<size;i++)
          {
             printf("%d \t",a[i]);
          }

      printf("\n--------\nTime Required for parallel=%f",end-start);
}


