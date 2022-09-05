//
// Created by Zenny Chen on 2017/10/27.
//

#include <jni.h>
#include <stdio.h>
#include <string.h>



/**
 * 测试内联汇编，分别根据AArch32架构以及AArch64架构来实现一个简单的减法计算
 * @param a 被减数
 * @param b 减数
 * @return 减法得到的差值
 */
//extern "C" JNIEXPORT int  JNICALL
static int __attribute__((naked, pure)) MyASMTest(int a, int b)
{


    asm(".thumb");
    asm(".syntax unified");

    asm("sub r0, r0, r1");
    asm("add r0, r0, #1");  // 为了区分当前用的是AArch32还是AArch64，这里对于AArch32情况下再加1
    asm("bx lr");


}
// Java_com_example_myapplicationnewhbahha_MainActivity_stringFromJNI
//extern "C" enhgwocvzhendehshiqlegegualehalhah
  JNIEXPORT jstring JNICALL
Java_com_example_myapplication116_MainActivity_stringFromJNI(JNIEnv *env, jobject this )
{
char strBuf[128];

sprintf(strBuf, "Hello from C! ASM test result: %d", MyASMTest(6, 4));

return  (*env  )->NewStringUTF( this,strBuf);
}