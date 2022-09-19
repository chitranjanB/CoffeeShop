import {
  Timeline,
  TimelineItem,
  TimelineSeparator,
  TimelineConnector,
  TimelineContent,
  TimelineOppositeContent,
  TimelineDot,
} from '@mui/lab'
import {
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Stack,
} from '@mui/material'
import {
  Coffee,
  CoffeeMaker,
  Factory,
  Microwave,
  Person,
} from '@mui/icons-material'
import { useCallback, useEffect, useState } from 'react'
import axios from 'axios'

type AuditLogType = {
  stepTransactionId: {
    step: 'GRIND_COFFEE' | 'MAKE_ESPRESSO' | 'STEAM_MILK'
    transactionId: string
  }
  customerId: string
  machineName: string
  threadName: string
  timeElapsed: number
  timeStarted: string
  timeEnded: string
}

type OrderTimelineType = {
  grindCoffeeAuditLog: AuditLogType
  makeEspressoAuditLog: AuditLogType
  steamMilkAuditLog: AuditLogType
}

const convertToDate = (dateStr: string | undefined) => {
  const options: Intl.DateTimeFormatOptions = {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  }
  return dateStr ? new Date(dateStr).toLocaleString('en-US', options) : ''
}

export default function OrderTimeline() {
  const [loading, setLoading] = useState<boolean>(false)
  const [data, setData] = useState<OrderTimelineType | undefined>()
  const [transactions, setTransactions] = useState<string[]>([])
  const [activeTransaction, setActiveTransaction] = useState<
    string | undefined
  >()

  const fetchOrderTimelineCallback = useCallback(() => {
    if (activeTransaction) {
      axios
        .get(
          `http://localhost:8080/analytics/timeline?transactionId=${activeTransaction}`
        )
        .then((res) => {
          setData(res.data)
        })
        .catch(() => {
          setLoading(false)
        })
      setLoading(false)
    }
  }, [activeTransaction])

  const fetchOrderIdsCallback = useCallback(() => {
    axios
      .get('http://localhost:8080/orders/all')
      .then((res) => {
        setTransactions(res.data)
      })
      .catch(() => {
        setLoading(false)
      })
    setLoading(false)
  }, [])

  useEffect(() => {
    fetchOrderTimelineCallback()
    fetchOrderIdsCallback()
    const timer = setInterval(() => {
      fetchOrderTimelineCallback()
      fetchOrderIdsCallback()
    }, 3 * 1000)

    return () => {
      clearInterval(timer)
    }
  }, [fetchOrderTimelineCallback, fetchOrderIdsCallback])

  return (
    <>
      <Stack
        sx={{
          marginTop: '24px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <FormControl sx={{ width: '200px' }}>
          <InputLabel>Transaction Id</InputLabel>
          <Select
            label="Transaction Id"
            value={activeTransaction}
            onChange={(e) => {
              setActiveTransaction(e.target.value)
            }}
          >
            {transactions.map((transaction) => (
              <MenuItem key={transaction} value={transaction}>
                {transaction}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        {data && (
          <Timeline position="alternate">
            <TimelineItem>
              <TimelineOppositeContent
                sx={{ m: 'auto 0' }}
                align="right"
                variant="body2"
                color="text.secondary"
              >
                {convertToDate(data?.grindCoffeeAuditLog.timeStarted)}
              </TimelineOppositeContent>
              <TimelineSeparator>
                <TimelineConnector />
                <TimelineDot color="primary">
                  <Person />
                </TimelineDot>
                <TimelineConnector />
              </TimelineSeparator>
              <TimelineContent sx={{ py: '12px', px: 2 }}>
                <Typography variant="h6" component="span">
                  Order
                </Typography>
                <Typography>
                  Transaction ID -
                  {data?.grindCoffeeAuditLog.stepTransactionId.transactionId}
                </Typography>
                <Typography>
                  Created by {data?.grindCoffeeAuditLog.customerId}
                </Typography>
              </TimelineContent>
            </TimelineItem>
            <TimelineItem>
              <TimelineOppositeContent
                sx={{ m: 'auto 0' }}
                align="right"
                variant="body2"
                color="text.secondary"
              >
                {convertToDate(data?.grindCoffeeAuditLog.timeStarted)}
              </TimelineOppositeContent>
              <TimelineSeparator>
                <TimelineConnector />
                <TimelineDot color="primary">
                  <Factory />
                </TimelineDot>
                <TimelineConnector />
              </TimelineSeparator>
              <TimelineContent sx={{ py: '12px', px: 2 }}>
                <Typography variant="h6" component="span">
                  Grind Coffee
                </Typography>
                <Typography>{data?.grindCoffeeAuditLog.machineName}</Typography>
                <Typography>
                  took {data?.grindCoffeeAuditLog.timeElapsed} ms
                </Typography>
              </TimelineContent>
            </TimelineItem>
            <TimelineItem>
              <TimelineOppositeContent
                sx={{ m: 'auto 0' }}
                align="right"
                variant="body2"
                color="text.secondary"
              >
                {convertToDate(data?.makeEspressoAuditLog.timeStarted)}
              </TimelineOppositeContent>
              <TimelineSeparator>
                <TimelineConnector />
                <TimelineDot color="primary">
                  <CoffeeMaker />
                </TimelineDot>
                <TimelineConnector />
              </TimelineSeparator>
              <TimelineContent sx={{ py: '12px', px: 2 }}>
                <Typography variant="h6" component="span">
                  Make Espresso
                </Typography>

                <Typography>{data?.grindCoffeeAuditLog.machineName}</Typography>
                <Typography>
                  took {data?.makeEspressoAuditLog.timeElapsed} ms
                </Typography>
              </TimelineContent>
            </TimelineItem>
            <TimelineItem>
              <TimelineOppositeContent
                sx={{ m: 'auto 0' }}
                variant="body2"
                color="text.secondary"
              >
                {convertToDate(data?.steamMilkAuditLog.timeStarted)}
              </TimelineOppositeContent>
              <TimelineSeparator>
                <TimelineConnector />
                <TimelineDot color="primary">
                  <Microwave />
                </TimelineDot>
                <TimelineConnector />
              </TimelineSeparator>
              <TimelineContent sx={{ py: '12px', px: 2 }}>
                <Typography variant="h6" component="span">
                  Steam Milk
                </Typography>

                <Typography>{data?.grindCoffeeAuditLog.machineName}</Typography>
                <Typography>
                  took {data?.steamMilkAuditLog.timeElapsed} ms
                </Typography>
              </TimelineContent>
            </TimelineItem>
            <TimelineItem>
              <TimelineOppositeContent
                sx={{ m: 'auto 0' }}
                align="right"
                variant="body2"
                color="text.secondary"
              >
                {convertToDate(data?.steamMilkAuditLog.timeEnded)}
              </TimelineOppositeContent>
              <TimelineSeparator>
                <TimelineConnector />
                <TimelineDot color="primary">
                  <Coffee />
                </TimelineDot>
                <TimelineConnector sx={{ bgcolor: 'secondary.main' }} />
              </TimelineSeparator>
              <TimelineContent sx={{ py: '12px', px: 2 }}>
                <Typography variant="h6" component="span">
                  Delivered
                </Typography>
                <Typography>Thanks</Typography>
              </TimelineContent>
            </TimelineItem>
          </Timeline>
        )}
      </Stack>
    </>
  )
}
