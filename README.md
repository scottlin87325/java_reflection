java reflection : 

說明 : 

1. 使用JAVA模擬序列化/反序列化的基本流程及技術細節。

2. 將目前java類別及屬性值輸出成ini檔案(自訂輸出格式)，需要反序列化時再讀入ini檔案，依照輸出規則反向解析檔案，還原原本存在的類別及其屬性值。

執行流程 : 

1. 執行main檔 : 建立一個 Scene2 物件 → 使用兩種序列化方法（ini 檔案、SQLite 資料庫）來存取這個物件 → 再還原（deserialize）這個物件 → 檢查還原後的資料是否正確。

2. console輸出 : 顯示反序列化結果及SQLite的原始資料。
