const {NativeModules} = require('react-native');
const remind = NativeModules.Remind

export default  class Remind{
    static addEvent = (params)=>{
        return remind.addEvent(params)
    }
}