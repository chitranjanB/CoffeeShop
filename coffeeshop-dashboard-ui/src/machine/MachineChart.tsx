import { Box, Typography } from '@mui/material'
import axios from 'axios'
import { useCallback, useEffect, useMemo, useState } from 'react'
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
  const [data, setData] = useState<MachineEffiencyType[] | undefined>()
  const [token, setToken] = useState<string>('')

  type ConfigType = { headers: { Authorization: string } }

  const config: ConfigType = useMemo(() => {
    return {
      headers: {
        Authorization: token,
      },
    }
  }, [token])

  const fetchAuthToken = () => {
    axios
      .post('http://localhost:8080/auth/authenticate', {
        emailId: 'app@coffeeshop.com',
        password: 'DUMMY',
      })
      .then((res) => {
        const {
          data: {
            data: { accessToken },
          },
        } = res
        setToken(accessToken)
      })
  }

  const fetchMachineEfficiencyCallback = useCallback(() => {
    axios
      .get('http://localhost:8080/analytics/machine-efficiency', config)
      .then((res) => {
        setData(res.data)
      })
      .catch(() => {
        setLoading(false)
      })
    setLoading(false)
  }, [config])

  useEffect(() => {
    !token && fetchAuthToken()
    token && fetchMachineEfficiencyCallback()
    const timer = setInterval(() => {
      token && fetchMachineEfficiencyCallback()
    }, 3 * 1000)

    return () => {
      clearInterval(timer)
    }
  }, [token, fetchMachineEfficiencyCallback])

  return (
    <Box>
      {loading ? (
        <Typography variant="h6">Loading...</Typography>
      ) : (
        <ResponsiveContainer width="100%" aspect={4}>
          <LineChart
            width={500}
            height={300}
            data={data}
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
