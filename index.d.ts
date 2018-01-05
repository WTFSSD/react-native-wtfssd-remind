
interface RemindParams{
    title?:string
    location?:string
    allDay?:boolean
    startDate?:string
    endDate?:string
    URL?:string
    notes?:string
}

declare class Remind{
    /**
     * 添加时间
     * @param {RemindParams} params
     * @return {Promise<any>}
     */
    static addEvent  (params:RemindParams):Promise<any>
}