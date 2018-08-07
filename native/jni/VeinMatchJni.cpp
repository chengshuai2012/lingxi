//
// Created by linc on 16-3-23.
//
#include "com_wedone_sdk_VeinMatchLib.h"
#include "veinmatch.h"
#include <android/log.h>
#define  LOG_TAG    "WEDONE:"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmInit
 * Signature: (IS)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmInit
  (JNIEnv *, jclass, jint dwMaxUserNum, jshort wUserMaxTemplateNum)
  {
	int nResultVal = 0;
    LOGD("VM_Init:initializing......\n");
    nResultVal = VM_Init(dwMaxUserNum,wUserMaxTemplateNum);
    LOGD("VM_Init: nResultVal=%d\n",nResultVal);
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmStop
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmStop
  (JNIEnv *, jclass)
  {
	int nResultVal = 0;
    nResultVal = VM_Stop();
    LOGD("VM_Stop:nResultVal=%d\n", nResultVal);
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmAddUserForMatch
 * Signature: ([BS[BSS)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmAddUserForMatch
  (JNIEnv *, jclass, jbyteArray, jshort, jbyteArray, jshort, jshort)
  {
	int nResultVal = 0;
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmDeleteUserForMatch
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmDeleteUserForMatch
  (JNIEnv *, jclass, jbyteArray)
  {
	int nResultVal = 0;
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchWithAll
 * Signature: ([BS[BSB)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchWithAll
  (JNIEnv *, jclass, jbyteArray, jshort, jbyteArray, jshort, jbyte)
  {
	int nResultVal = 0;
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchWithAllEx
 * Signature: ([BS[BSB[S)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchWithAllEx
  (JNIEnv *, jclass, jbyteArray, jshort, jbyteArray, jshort, jbyte, jshortArray)
  {
	int nResultVal = 0;
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchInDepartment
 * Signature: (S[BS[BSB)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchInDepartment
  (JNIEnv *, jclass, jshort, jbyteArray, jshort, jbyteArray, jshort, jbyte)
  {
	int nResultVal = 0;
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchByUserID
 * Signature: ([B[BS[BSB)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchByUserID
  (JNIEnv *, jclass, jbyteArray, jbyteArray, jshort, jbyteArray, jshort, jbyte)
  {
	int nResultVal = 0;
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchByCardNo
 * Signature: ([B[BS[BSB)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchByCardNo
  (JNIEnv *, jclass, jbyteArray, jbyteArray, jshort, jbyteArray, jshort, jbyte)
  {
	int nResultVal = 0;
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchTemplate
 * Signature: ([B[BB)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchTemplate
  (JNIEnv *env, jclass, jbyteArray bPtrTemplateLeft, jbyteArray bPtrTemplateRight, jbyte bFlag)
  {
	int nResultVal = 0;
    LOGD("VM_MatchTemplate: entry\n");
    jbyte *jbTemplateLeft = env->GetByteArrayElements(bPtrTemplateLeft,NULL);
    jbyte *jbTemplateRight = env->GetByteArrayElements(bPtrTemplateRight,NULL);
    nResultVal = VM_MatchTemplate((unsigned char*)jbTemplateLeft,(unsigned char*)jbTemplateRight, bFlag);
    LOGD("VM_MatchTemplate:nResultVal=%d\n",nResultVal);
    env->ReleaseByteArrayElements(bPtrTemplateLeft, jbTemplateLeft, JNI_ABORT);
    env->ReleaseByteArrayElements(bPtrTemplateRight, jbTemplateRight, JNI_ABORT);
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchTemplateEx
 * Signature: ([B[BB[S)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchTemplateEx
  (JNIEnv *env, jclass, jbyteArray bPtrTemplateLeft, jbyteArray bPtrTemplateRight, jbyte bFlag, jshortArray wScore)
  {
    int nResultVal = 0;
    LOGD("VM_MatchTemplateEx: entry\n");
    jbyte *jbTemplateLeft = env->GetByteArrayElements(bPtrTemplateLeft,NULL);
    jbyte *jbTemplateRight = env->GetByteArrayElements(bPtrTemplateRight,NULL);
    jshort *jwScore = env->GetShortArrayElements(wScore,NULL);
    nResultVal = VM_MatchTemplateEx((unsigned char*)jbTemplateLeft,(unsigned char*)jbTemplateRight, bFlag, (WORD *)jwScore);
    LOGD("VM_MatchTemplateEx:nResultVal=%d\n",nResultVal);
    env->ReleaseByteArrayElements(bPtrTemplateLeft, jbTemplateLeft, JNI_ABORT);
    env->ReleaseByteArrayElements(bPtrTemplateRight, jbTemplateRight, JNI_ABORT);
    env->ReleaseShortArrayElements(wScore, jwScore, JNI_ABORT);
    return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchTemplates
 * Signature: ([B[BSB)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchTemplates
  (JNIEnv *env, jclass, jbyteArray bPtrTemplateLeft, jbyteArray bPtrTemplateRight, jshort wRightTemplateNum, jbyte bFlag)
  {
	int nResultVal = 0;
    LOGD("VM_MatchTemplates: entry\n");
    jbyte *jbTemplateLeft = env->GetByteArrayElements(bPtrTemplateLeft,NULL);
    jbyte *jbTemplateRight = env->GetByteArrayElements(bPtrTemplateRight,NULL);
    nResultVal = VM_MatchTemplates((unsigned char*)jbTemplateLeft,(unsigned char*)jbTemplateRight, wRightTemplateNum, bFlag);
    LOGD("VM_MatchTemplates:nResultVal=%d\n",nResultVal);
    env->ReleaseByteArrayElements(bPtrTemplateLeft, jbTemplateLeft, JNI_ABORT);
    env->ReleaseByteArrayElements(bPtrTemplateRight, jbTemplateRight, JNI_ABORT);
	return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmMatchTemplatesEx
 * Signature: ([B[BSB[S)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmMatchTemplatesEx
  (JNIEnv *env, jclass, jbyteArray bPtrTemplateLeft, jbyteArray bPtrTemplateRight, jshort wRightTemplateNum, jbyte bFlag, jshortArray wScore)
  {
    int nResultVal = 0;
    LOGD("VM_MatchTemplatesEx: entry\n");
    jbyte *jbTemplateLeft = env->GetByteArrayElements(bPtrTemplateLeft,NULL);
    jbyte *jbTemplateRight = env->GetByteArrayElements(bPtrTemplateRight,NULL);
    jshort *jwScore = env->GetShortArrayElements(wScore,NULL);
    nResultVal = VM_MatchTemplatesEx((unsigned char*)jbTemplateLeft,(unsigned char*)jbTemplateRight, wRightTemplateNum, bFlag, (WORD *)jwScore);
    LOGD("VM_MatchTemplatesEx:nResultVal=%d\n",nResultVal);
    env->ReleaseByteArrayElements(bPtrTemplateLeft, jbTemplateLeft, JNI_ABORT);
    env->ReleaseByteArrayElements(bPtrTemplateRight, jbTemplateRight, JNI_ABORT);
    env->ReleaseShortArrayElements(wScore, jwScore, JNI_ABORT);
    return nResultVal;
  }

/*
 * Class:     com_wedone_sdk_VeinMatchLib
 * Method:    vmSetSecurityLevel
 * Signature: (SS)I
 */
JNIEXPORT jint JNICALL Java_com_wedone_sdk_VeinMatchLib_vmSetSecurityLevel
  (JNIEnv *, jclass, jshort wSecurityLevel_1Vn, jshort wSecurityLevel_1V1)
  {
    int nResultVal = 0;
    LOGD("VM_SetSecurityLevel: entry\n");
    nResultVal = VM_SetSecurityLevel(wSecurityLevel_1Vn, wSecurityLevel_1V1);
    LOGD("VM_SetSecurityLevel:nResultVal=%d\n",nResultVal);
    return nResultVal;
  }
