export type SeriesType = { data: DataType[]; name: string }

export type DataType = { x: string; y: number[] }

export type AnalyticsResponseT = {
  timelineDataList: ResponseDataT[]
  name: string
}

export type ResponseDataT = {
  customerId: string
  startTimestamp: string
  endTimestamp: string
}
