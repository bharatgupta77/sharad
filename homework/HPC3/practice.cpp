#include<iostream>
#include<omp.h>
#include<stdlib.h>
using namespace std;

void bubblesort(int *a,int N)
{
	for (int i=0;i<N-1;i++)
	{
		for(int j=0;j<N-i-1;j++)
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

void parallelbubblesort(int *a,int N)

{
	for (int i=0;i<N-1;i++)
	{
		#pragma omp parallel for
		for(int j=0;j<N-i-1;j++)
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
      int b[size];
      int temp;
          printf("enter the elements:");
          for (int i=0;i<size;i++)
          {

             cin>>temp;
             a[i]=temp;
             b[i]=temp;

          }

      int N=size;

      double start,end,start1,end1;

      start1=omp_get_wtime();
      bubblesort(a,N);
      end1=omp_get_wtime();


       for (int i=0;i<size;i++)
          {
             cout<<a[i]<<"\t";
          }

      cout<<"\n----Time Required for serial= "<<end1-start1<<endl;


      start=omp_get_wtime();
      parallelbubblesort(b,N);
      end=omp_get_wtime();


       for (int i=0;i<size;i++)
          {
             cout<<b[i]<<"\t";
          }

      cout<<"\n----Time Required for parallel="<<end-start<<endl;


}
