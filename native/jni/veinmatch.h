#ifndef VEINMATCHSDK_H
#define VEINMATCHSDK_H

#ifndef VEIN_TYPE_DEFINED
#define VEIN_TYPE_DEFINED
typedef char            CHAR;
typedef unsigned char   BYTE;
typedef unsigned short  WORD;
typedef unsigned int    DWORD;
typedef unsigned int    UInt32;
typedef unsigned short  UInt16;
typedef unsigned char   UInt8;
typedef int				Int32;
typedef short			Int16;
typedef char			Int8;
typedef long            LONG;
typedef BYTE			*LPBYTE;
typedef unsigned long   *LPUint;
typedef const void      *LPCVOID;
typedef void            *HANDLE;
typedef void            *LPVOID;
typedef CHAR            *NPSTR, *LPSTR, *PSTR;
typedef signed int      INTPOINTER;
#endif

#ifndef NULL
#define NULL 0
#endif

//★★★★★ 错误代码定义 ★★★★★
#define SDK_ERRCODE_SUCCESS                     0x00                       //SDK调用成功
#define SDK_ERRCODE_START                       SDK_ERRCODE_SUCCESS        //错误代码的起始编号

#define SDK_ERRCODE_PARAMETER_ERROR             (SDK_ERRCODE_START + 0x01) //参数错误
#define SDK_ERRCODE_SDK_INITIALIZED             (SDK_ERRCODE_START + 0x02) //SDK已经被初始化了
#define SDK_ERRCODE_SDK_STARTED                 (SDK_ERRCODE_START + 0x03) //SDK已经被启动了
#define SDK_ERRCODE_SDK_NOT_INITIALIZED         (SDK_ERRCODE_START + 0x04) //SDK尚未初始化
#define SDK_ERRCODE_SDK_NOT_STARTED             (SDK_ERRCODE_START + 0x05) //SDK尚未启动

#define SDK_ERRCODE_VEINMATCH_NOT_INITIALIZED   (SDK_ERRCODE_START + 0x40) //SDK未启动，比对接口未初始化
#define SDK_ERRCODE_VEINMATCH_DATABUFF_ISNULL   (SDK_ERRCODE_START + 0x41) //参数中的某个数据缓冲区为空
#define SDK_ERRCODE_VEINMATCH_DATASIZE_ISWRONG  (SDK_ERRCODE_START + 0x42) //参数中的数据大小设置错误
#define SDK_ERRCODE_VEINMATCH_VEINDB_ERROR      (SDK_ERRCODE_START + 0x43) //比对用的数据缓冲区错误
#define SDK_ERRCODE_VEINMATCH_USER_EXISTED      (SDK_ERRCODE_START + 0x44) //登记的用户数据已经存在
#define SDK_ERRCODE_VEINMATCH_SUBMIT_ERROR      (SDK_ERRCODE_START + 0x45) //提交比对数据时发生错误
#define SDK_ERRCODE_VEINMATCH_MATCH_FAILED      (SDK_ERRCODE_START + 0x46) //静脉特征比对失败
#define SDK_ERRCODE_VEINMATCH_TEMPLATES_OVER    (SDK_ERRCODE_START + 0x47) //登记的静脉模板数量太多

#define SDK_ERRCODE_INVALID_TEMPLATE            (SDK_ERRCODE_START + 0x50) //无效的指静脉模板

#define  SDK_MATCH_RESULT_PASS      (0) //验证通过
#define  SDK_MATCH_RESULT_FAIL      (1) //验证失败

#ifndef SSDKAPI
#if defined(_WIN32) //VS2008环境永远存在的定义，也即Windows平台的VS2008环境
    #if defined(VEINMATCH_LIBRARY)  //在VS2008工程中定义
        #define SSDKAPI     __declspec(dllexport)
    #else
        #define SSDKAPI     __declspec(dllimport)
    #endif
#else
    #if defined(VEINMATCH_LIBRARY_WIN) //在SDK的QT工程中定义，Windows平台下有效，也即Windows平台的QT环境
        #if defined(VEINMATCH_LIBRARY) //在SDK的QT工程中定义
            #define SSDKAPI     __declspec(dllexport)
        #else
            #define SSDKAPI     __declspec(dllimport)
        #endif
    #else	//非Windows平台的环境
        #define SSDKAPI     __attribute__((visibility("default")))
    #endif
#endif
#endif

//服务器后台比对SDK使用的API接口
extern "C"
{
    //★★★★★SDK启动与关闭等整体控制的接口★★★★★
    //初始化SDK,指定SDK中可以比较的最大用户数。
    SSDKAPI UInt32 VM_Init(UInt32 dwMaxUserNum = 10000, WORD wUserMaxTemplateNum = 6);
    //停止SDK，释放SDK中用来保存用户数据的缓存
    SSDKAPI UInt32 VM_Stop();
    //获取SDK的版本号
    SSDKAPI UInt32 VM_GetVersion(BYTE *bVersion);

    //★★★★★静脉对比相关接口函数★★★★★
    //确认模板是否为有效的模板
    SSDKAPI UInt32 VM_IsValidTemplate(BYTE *bTemplateBuff, BYTE bFlag = 0);
    //添加用户及注册的模板数据到比对的缓冲区中，供后续的比对时使用
    SSDKAPI UInt32 VM_AddUserForMatch(BYTE *bUserHeader, WORD wHeaderLen, BYTE *bTemplatesBuff, WORD dBuffSize, WORD wFlag = 0);
    //从比对的缓冲区中删除指定用户的信息，用户被删除后无法继续通过比对。
    SSDKAPI UInt32 VM_DeleteUserForMatch(BYTE *bUserID);
    //把指定的模板与缓冲区中的所有注册模板进行比对，识别用户的身份。
    SSDKAPI UInt32 VM_MatchWithAll(BYTE *bTemplateBuff, WORD wTemplateBuffSize, BYTE *bMatchResultBuff, WORD wResultBuffSize, BYTE bFlag = 0x00);
    SSDKAPI UInt32 VM_MatchWithAllEx(BYTE *bTemplateBuff, WORD wTemplateBuffSize, BYTE *bMatchResultBuff, WORD wResultBuffSize, BYTE bFlag = 0x00, WORD *Reserved = NULL);
    //把指定的模板与缓冲区中的指定模板编号对应的注册模板进行比对，识别出用户的身份。
    SSDKAPI UInt32 VM_MatchInDepartment(WORD wDepNo, BYTE *bTemplateBuff, WORD wTemplateBuffSize, BYTE *bMatchResultBuff, WORD wResultBuffSize, BYTE bFlag = 0x00);
    //把指定的模板与缓冲区中的指定编号对应用户的注册模板进行比对，验证该特征模板是否属于这个用户的。
    SSDKAPI UInt32 VM_MatchByUserID(BYTE *bUserID, BYTE *bTemplateBuff, WORD wTemplateBuffSize, BYTE *bMatchResultBuff, WORD wResultBuffSize, BYTE bFlag = 0x00);
    //把指定的模板与缓冲区中的指定卡号对应用户的注册模板进行比对，验证该特征模板是否属于这个用户的。
    SSDKAPI UInt32 VM_MatchByCardNo(BYTE *bCardNo, BYTE *bTemplateBuff, WORD wTemplateBuffSize, BYTE *bMatchResultBuff, WORD wResultBuffSize, BYTE bFlag = 0x00);
    //两个特征模板直接进行验证，确认是否是同一根手指。
    SSDKAPI UInt32 VM_MatchTemplate(BYTE* bPtrTemplateLeft, BYTE* bPtrTemplateRight, BYTE bFlag = 0);
    SSDKAPI UInt32 VM_MatchTemplateEx(BYTE* bPtrTemplateLeft, BYTE* bPtrTemplateRight, BYTE bFlag = 0, WORD *wDiffer = NULL);
    //一个特征模板与多个特征模板进行比对，确认该模板是否与指定的某个模板属于同一根手指。
    SSDKAPI UInt32 VM_MatchTemplates(BYTE *bPtrTemplateLeft, BYTE *bPtrTemplateRight, WORD wRightTemplateNum, BYTE bFlag = 0x00);
    SSDKAPI UInt32 VM_MatchTemplatesEx(BYTE *bPtrTemplateLeft, BYTE *bPtrTemplateRight, WORD wRightTemplateNum, BYTE bFlag = 0x00, WORD *wDiffer = NULL);
    //设置静脉模板比对的安全级别, wSecurityLevel 为 0至10之间的任一整数,数字越小越安全相对通过率会有所降低.
    //第一个参数为1：N的安全级别，默认为6，第二个参数为1：1的安全级别默认为8。
    SSDKAPI UInt32 VM_SetSecurityLevel(WORD wSecurityLevel_1Vn, WORD wSecurityLevel_1V1);

    //把指静脉的模板数据转换成静脉图片
    SSDKAPI UInt32 VM_Template2Img(BYTE* bTemplateBuff, BYTE* bImgBuff, WORD *wImgBuffLen, BYTE bFlag = 0);
}

#endif // VEINMATCHSDK_H
