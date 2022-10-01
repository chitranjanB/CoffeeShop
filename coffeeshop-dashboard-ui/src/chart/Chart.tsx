import axios from 'axios'
import { useCallback, useEffect, useState } from 'react'
import ReactApexChart from 'react-apexcharts'
import {
  AnalyticsResponseT,
  DataType,
  ResponseDataT,
  SeriesType,
} from './types'
import './Chart.css'
import { Typography } from '@mui/material'

const Chart = () => {
  const [series, setSeries] = useState<SeriesType[] | undefined>(undefined)

  const [options]: [ApexCharts.ApexOptions, Function] = useState({
    chart: {
      height: 350,
      type: 'rangeBar',
    },
    plotOptions: {
      bar: {
        horizontal: true,
        barHeight: '50%',
        rangeBarGroupRows: true,
      },
    },
    colors: [
      '#FF4560',
      '#8D5B4C',
      '#1B998B',
      '#008FFB',
      '#00E396',
      '#FEB019',
      '#775DD0',
      '#3F51B5',
      '#546E7A',
      '#D4526E',
      '#F86624',
      '#D7263D',
      '#2E294E',
      '#F46036',
      '#E2C044',
    ],
    fill: {
      type: 'solid',
    },
    xaxis: {
      type: 'datetime',
    },
    legend: {
      position: 'right',
    },
    tooltip: {
      x: {
        formatter: function (val) {
          const date = new Date(val)
          const ss = '0' + date.getSeconds()
          const mms = date.getMilliseconds()

          return `${ss.substr(-2)}:${mms}`
        },
      },
    },
  })

  const buildApexDateCallback = useCallback((timestamp: string) => {
    let dateTime = timestamp.split('T')

    let yyyymmddArray = dateTime[0].split('-')
    let HH_MM_SSMSSZONE = dateTime[1].split(':')
    let [yyyy, mo, dd] = yyyymmddArray //.join(",");
    let [hh, mm] = HH_MM_SSMSSZONE
    let [ss, mss] = [
      HH_MM_SSMSSZONE[2].split('.')[0],
      HH_MM_SSMSSZONE[2].split('.')[1].split('+')[0],
    ]

    return new Date(
      +parseInt(yyyy),
      parseInt(mo) - 1,
      +parseInt(dd),
      +parseInt(hh),
      +parseInt(mm),
      +parseInt(ss),
      +parseInt(mss)
    ).getTime()
  }, [])

  const buildNodeCallback = useCallback(
    (e: ResponseDataT): DataType => {
      const x = e.customerId
      // eslint-disable-next-line max-len
      const y = [
        buildApexDateCallback(e.startTimestamp),
        buildApexDateCallback(e.endTimestamp),
      ]
      return { x, y }
    },
    [buildApexDateCallback]
  )

  const fetchAnalyticsCallback = useCallback(() => {
    axios
      .post('http://localhost:8080/analytics/system-benchmark')
      .then((res) => {
        const response: AnalyticsResponseT[] = res.data

        const seriesData: SeriesType[] = [...response].map((d) => {
          const data: DataType[] = d.timelineDataList.map((e) => {
            return buildNodeCallback(e)
          })
          const name = d.name
          return { data, name }
        })
        setSeries(seriesData)
      })
  }, [buildNodeCallback])

  useEffect(() => {
    fetchAnalyticsCallback()
    const timer = setInterval(fetchAnalyticsCallback, 3 * 1000)

    return () => {
      clearInterval(timer)
    }
  }, [fetchAnalyticsCallback])

  return (
    <div id="chart">
      {series ? (
        <ReactApexChart
          options={options}
          series={series}
          type="rangeBar"
          height={350}
        />
      ) : (
        <Typography variant="h5" className="waiting" alignContent="center">
          Waiting for data...
        </Typography>
      )}
    </div>
  )
}

export default Chart
