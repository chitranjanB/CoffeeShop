import { Box, Typography } from '@mui/material'
import axios from 'axios'
import { useCallback, useEffect, useState } from 'react'
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts'

type MachineEffiencyType = {
  transactionId: string
  timeTakenByGrinderMachine: number
  timeTakenByEspressoMachine: number
  timeTakenBySteamerMachine: number
}
const MachineChart = () => {
  const [loading, setLoading] = useState<boolean>(false)
  const [data1, setData1] = useState<MachineEffiencyType[] | undefined>()

  const fetchMachineEfficiencyCallback = useCallback(() => {
    axios
      .get('http://localhost:8080/analytics/machine-efficiency')
      .then((res) => {
        console.log(res.data)
        setData1(res.data)
      })
      .catch(() => {
        setLoading(false)
      })
    setLoading(false)
  }, [])

  useEffect(() => {
    fetchMachineEfficiencyCallback()
    const timer = setInterval(() => {
      fetchMachineEfficiencyCallback()
    }, 3 * 1000)

    return () => {
      clearInterval(timer)
    }
  }, [fetchMachineEfficiencyCallback])

  return (
    <Box>
      {loading ? (
        <Typography variant="h6">Loading...</Typography>
      ) : (
        <ResponsiveContainer width="100%" aspect={4}>
          <LineChart
            width={500}
            height={300}
            data={data1}
            margin={{
              top: 5,
              right: 30,
              left: 20,
              bottom: 5,
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="transactionId" />
            <YAxis tickCount={10} domain={[975, 1025]} />
            <Tooltip />
            <Legend />

            <Line
              type="monotone"
              dataKey="timeTakenByGrinderMachine"
              stroke="#82ca9d"
            />
            <Line
              type="monotone"
              dataKey="timeTakenByEspressoMachine"
              stroke="#8884d8"
            />
            <Line
              type="monotone"
              dataKey="timeTakenBySteamerMachine"
              stroke="#8884d8"
              activeDot={{ r: 8 }}
            />
          </LineChart>
        </ResponsiveContainer>
      )}
    </Box>
  )
}

export default MachineChart
