# react-native-wtfssd-remind
# 向系统日历中添加自定义提醒事件

## Installation
`npm install react-native-wtfssd-remind --save`

# Usage
# 权限
- ios 添加 `NSRemindersUsageDescription`和`NSCalendarsUsageDescription` 到 `info.plist`
- android 添加 `    <uses-permission android:name="android.permission.READ_CALENDAR" />
    <uses-permission android:name="android.permission.WRITE_CALENDAR" />` 到 `AndroidManifest.xml`
    
    
# example
    
```
import Remind from 'react-native-wtfssd-remind';

 Remind.addEvent({
            title:"i am title",
            location:"i am address",
            startDate:new Date(),
            endDate:new Date(),
            URL:"https://github.com/WTFSSD/react-native-wtfssd-remind.git",
            notes:"iam description",
            allDay:true,
        }).then((e)=>{
            console.log('完成',e);
        }).catch(e=>{
            console.log('错误',e);
        });
```
