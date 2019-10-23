
#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<math.h>

void init_array(float *a, const int N);
__global__
void sum(float* input)
{
 const int tid = threadIdx.x;
 int no_threads = blockDim.x;

 int step_size =1;

 while(no_threads>0)
 {
   if(tid<no_threads)
   {
      const int fst = tid*step_size*2;
      const int snd = fst + step_size;
      input[fst] += input[snd];
   }
   step_size <<=1;   //increase by 2
   no_threads >>= 1;  //decrease by 2

 }
}

__global__
void min(float* input)
{
 const int tid = threadIdx.x;
 int no_threads = blockDim.x;

 int step_size =1;

 while(no_threads>0)
 {
   if(tid<no_threads)
   {
      const int fst = tid*step_size*2;
      const int snd = fst + step_size;
      if(input[snd]<input[fst])
         input[fst]  = input[snd];
   }
   step_size <<=1;
   no_threads >>= 1;

 }


}

__global__
void max(float* input)
{
 const int tid = threadIdx.x;
 int no_threads = blockDim.x;

 int step_size =1;

 while(no_threads>0)
 {
   if(tid<no_threads)
   {
      const int fst = tid*step_size*2;
      const int snd = fst + step_size;
      if(input[snd]>input[fst])
         input[fst]  = input[snd];
   }
   step_size <<=1;
   no_threads >>= 1;

 }


}


__global__
void std_(float* input,float avg)
{
 const int tid = threadIdx.x;
 int no_threads = blockDim.x;

 int step_size =1;

 while(no_threads>0)
 {
   if(tid<no_threads)
   {
      const int fst = tid*step_size*2;
      const int snd = fst + step_size;
      input[fst] = (input[fst]-avg)*(input[fst]-avg);
      input[snd] = (input[snd]-avg)*(input[snd]-avg);
      input[fst] += input[snd];
   }
   step_size <<=1;
   no_threads >>= 1;

 }


}


int main()
{

 srand(time(NULL));
// This random generation is referred to as a pseudo-random generation.  These created values are not truly "random" because a mathematical formula is used to generate the values.
//makes use of the computer's internal clock to control the choice of the seed.  Since time is continually changing, the seed is forever changing.  Remember, if the seed number remains the same, the sequence of numbers will be repeated for each run of the program.
 const int N = 8;

 const int size = N*sizeof(float);

 float *a;		//CPU Pointer

 float *d_a,*d_b;	//GPU Pointer

 float result, avg;

 double time_taken;
 
   //A is cpu pointer so memory is allocated by malloc.
 a = (float*)malloc(sizeof(float)*N);

 //initialising the array
 init_array(a,N);

 //printing the array
 for(int i=0;i<N;i++)
   printf("%f   ",a[i]);

//Allocating memory to GPU
 cudaMalloc(&d_a,size);
//Copying contents of CPU to GPU
 cudaMemcpy(d_a,a,size,cudaMemcpyHostToDevice);

 cudaMalloc(&d_b,size);
 cudaMemcpy(d_b,a,size,cudaMemcpyHostToDevice);
 
 //----------------Sum--------------------------
 clock_t t;
//The clock() function returns the approximate processor time that is consumed by the program.
 t = clock();
//Launching Kernel for sum
 sum<<<1,N/2>>>(d_a);
 cudaMemcpy(&result,d_a,sizeof(float),cudaMemcpyDeviceToHost);
 t = clock() - t;
//formula
 time_taken = ((double)t)/CLOCKS_PER_SEC; //in seconds

 printf("   Sum:  %f",result);
 printf(" Time taken by sum :%f",time_taken);
    
 //----------------Min--------------------------
 t = clock();
 min<<<1,N/2>>>(d_a);
 cudaMemcpy(&result,d_a,sizeof(float),cudaMemcpyDeviceToHost);
 t = clock() - t;
 time_taken = ((double)t)/CLOCKS_PER_SEC; //in seconds
 printf("   Min:  %f",result);
 printf(" Time taken by min :%f",time_taken);
      
 //----------------Max--------------------------
 t = clock();
 max<<<1,N/2>>>(d_a); 
 t = clock() - t;
 time_taken = ((double)t)/CLOCKS_PER_SEC; //in seconds
 printf(" Time taken by max :%f",time_taken);
 cudaMemcpy(&result,d_a,sizeof(float),cudaMemcpyDeviceToHost);
 printf("   Max:  %f",result);
 printf(" Time taken by max :%f",time_taken);
    
 //----------------Average--------------------
 t = clock();
 sum<<<1,N/2>>>(d_a);
 cudaMemcpy(&result,d_a,sizeof(float),cudaMemcpyDeviceToHost);
 avg = result/N;
 t = clock() - t;
 time_taken = ((double)t)/CLOCKS_PER_SEC; //in seconds
 printf("   Avg:  %f",avg);
 printf(" Time taken by avg :%f",time_taken);
 
 
    
 //----------------Standard deviation-------------
  t = clock();
 std_<<<1,N/2>>>(d_a,avg);
 float std;
 cudaMemcpy(&std,d_b,sizeof(float),cudaMemcpyDeviceToHost);
 std =std/N;
 std = sqrt(std);
 t = clock() - t;
 time_taken = ((double)t)/CLOCKS_PER_SEC; //in seconds
 printf(" STD IS:%f",std);
 printf(" Time taken by std :%f",time_taken);
 

 
 cudaFree(d_a);
 cudaFree(d_b);
 delete[] a;

 return 0;
}


void init_array(float*a,const int N)
{
  for(int i=0;i<N;i++)
     a[i] = rand()%N + 1;
}
