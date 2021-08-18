package com.cathay.exam.mvc.utils

import com.cathay.exam.mvc.data.database.ExamRoomDao

object DataUtil {

    fun createData():MutableList<ExamRoomDao>{
        val data = mutableListOf<ExamRoomDao>()
        val exam1 = ExamRoomDao()
        exam1.topic = "請問下列何者是電腦唯一認識的兩個數字？(選擇二項)"
        exam1.userAns = ",,,"
        exam1.ans = "1,2"
        exam1.options = "[數字0,數字1,數字9,數字10]"

        val exam2 = ExamRoomDao()
        exam2.topic = "下列日常生活應用中，何者是遊戲機？"
        exam2.userAns = ",,,"
        exam2.ans = "3"
        exam2.options = "[數位相機,省電日光燈,Xbox,三速電風扇]"

        val exam3 = ExamRoomDao()
        exam3.topic = "下列日常生活應用中，何者會使用到藍芽？"
        exam3.userAns = ",,,"
        exam3.ans = "3"
        exam3.options = "[腳踏車,提款機,冷氣機,點滴注射]"

        val exam4 = ExamRoomDao()
        exam4.topic = "下列日常生活應用中，那些會用到電？(選擇三項)"
        exam4.userAns = ",,,"
        exam4.ans = "1,2,4"
        exam4.options = "[電腦桌,鍵盤,售票機,手機]"

        val exam5 = ExamRoomDao()
        exam5.topic = "下列日常生活應用中，何者會使用到電腦？(選擇二項) "
        exam5.userAns = ",,,"
        exam5.ans = "1,4"
        exam5.options = "[悠遊卡(Mifare)讀卡機,發電機,電腦用耳機,飲料販賣機]"

        val exam6 = ExamRoomDao()
        exam6.topic = "下列何者主要應用於智慧型手機、平板電腦、GPS 車用導航電腦等？"
        exam6.userAns = ",,,"
        exam6.ans = "1,4"
        exam6.options = "[Android,Windows7,WindowsXP,WindowsMobile]"

        val exam7 = ExamRoomDao()
        exam7.topic = "下列何者可做為智慧型手機之作業系統？(選擇二項)?"
        exam7.userAns = ",,,"
        exam7.ans = "1,2"
        exam7.options = "[Android,iOS,WindowsXP,WindowsMobile]"

        val exam8 = ExamRoomDao()
        exam8.topic = "下列何者比較適合安裝在智慧型手機？(選擇二項)"
        exam8.userAns = ",,,"
        exam8.ans = "1,4"
        exam8.options = "[WindowsPhone8,UNIX,MacOS,iOS]"

        val exam9 = ExamRoomDao()
        exam9.topic = "下列關於作業系統之敘述何者有誤？(選擇二項)"
        exam9.userAns = ",,,"
        exam9.ans = "1,3"
        exam9.options = "[Windows7適用於智慧型手機等小型裝置,MacOS是Apple公司Macintosh電腦的作業系統，是最早採用圖形使用者介面(GUI)的作業系統,Solaris是一套UNIX-like的作業系統，最大的特點是其原始碼公開且免費使用,UNIX是美國AT&T公司所發展的作業系統，適合多人多工作業]"

        val exam10 = ExamRoomDao()
        exam10.topic = "當 CPU 從隨機存取記憶體(RAM)取得資料後，資料可能被儲存在下列何處？(選擇二項)?"
        exam10.userAns = ",,,"
        exam10.ans = "3,4"
        exam10.options = "[快閃記憶體,唯讀記憶體,快取記憶體,暫存器]"

        data.add(exam1)
        data.add(exam2)
        data.add(exam3)
        data.add(exam4)
        data.add(exam5)
        data.add(exam6)
        data.add(exam7)
        data.add(exam8)
        data.add(exam9)
        data.add(exam10)

        return data
    }

    fun sortOutData(data:String?):String{
         data?.let {
            val array = it.split(",")
            val string = StringBuilder()

            array.mapIndexed { index, s ->
                if (s.isNotEmpty()){
                    if (index < array.size-1) string.append(s+"\n")  else  string.append(s)
                }
            }
            var finalString = ""
            finalString = if (string.endsWith("\n")){
                string.substring(0..string.length-2)
            }else{
                string.toString()
            }
            return finalString
        }
        return ""
    }
}