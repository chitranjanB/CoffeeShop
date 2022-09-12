import {
  AppBar,
  Avatar,
  Badge,
  Box,
  Breadcrumbs,
  Button,
  Grid,
  IconButton,
  Link,
  Paper,
  SpeedDial,
  SpeedDialAction,
  SpeedDialIcon,
  Stack,
  ToggleButton,
  ToggleButtonGroup,
  Toolbar,
  Typography,
} from '@mui/material'

import axios from 'axios'
import { useCallback, useEffect, useState } from 'react'
import MyCard from '../card/MyCard'
import Chart from '../chart/Chart'
import {
  Apps,
  Dashboard,
  Coffee,
  NavigateNext,
  Share,
  Print,
} from '@mui/icons-material'
import './Layout.css'
import Kiosk from '../kiosk/Kiosk'

function Layout() {
  const [healthStatus, setHealthStatus] = useState<'DOWN' | 'UP'>('DOWN')
  const [ordersPending, setOrdersPending] = useState<number>(0)
  const [ordersCompleted, setOrdersCompleted] = useState<number>(0)
  const [ordersFailed, setOrdersFailed] = useState<number>(0)
  const [beansStock, setBeansStock] = useState<number>(0)
  const [milkStock, setMilkStock] = useState<number>(0)

  const [healthLoading, setHealthLoading] = useState<boolean>(false)
  const [ordersPendingLoading, setOrdersPendingLoading] =
    useState<boolean>(false)
  const [ordersCompletedLoading, setOrdersCompletedLoading] =
    useState<boolean>(false)
  const [ordersFailedLoading, setOrdersFailedLoading] = useState<boolean>(false)
  const [beansStockLoading, setBeansStockLoading] = useState<boolean>(false)
  const [milkStockLoading, setMilkStockLoading] = useState<boolean>(false)

  const [activeNav, setActiveNav] = useState<string>('dashboard')

  const fetchHealthCallback = useCallback(() => {
    setHealthLoading(true)
    axios
      .get('http://localhost:8080/actuator/health')
      .then((res) => {
        setHealthStatus(res.data.status)
      })
      .catch(() => {
        setHealthLoading(false)
      })
    setHealthLoading(false)
  }, [])

  const fetchOrdersPendingCallback = useCallback(() => {
    setOrdersPendingLoading(true)
    axios
      .get('http://localhost:8080/orders?status=PENDING')
      .then((res) => {
        setOrdersPending(res.data.length)
      })
      .catch(() => {
        setOrdersPendingLoading(false)
      })
    setOrdersPendingLoading(false)
  }, [])

  const fetchOrdersCompletedCallback = useCallback(() => {
    setOrdersCompletedLoading(true)
    axios
      .get('http://localhost:8080/orders?status=COMPLETE')
      .then((res) => {
        setOrdersCompleted(res.data.length)
      })
      .catch(() => {
        setOrdersCompletedLoading(false)
      })
    setOrdersCompletedLoading(false)
  }, [])

  const fetchOrdersFailedCallback = useCallback(() => {
    setOrdersFailedLoading(true)
    axios
      .get('http://localhost:8080/orders?status=ERROR')
      .then((res) => {
        setOrdersFailed(res.data.length)
      })
      .catch(() => {
        setOrdersFailedLoading(false)
      })
    setOrdersFailedLoading(false)
  }, [])

  const fetchBeanStockCallback = useCallback(() => {
    setBeansStockLoading(true)
    axios
      .get('http://localhost:8080/stock/beans')
      .then((res) => {
        setBeansStock(res.data)
      })
      .catch(() => {
        setBeansStockLoading(false)
      })
    setBeansStockLoading(false)
  }, [])

  const fetchMilkStockCallback = useCallback(() => {
    setMilkStockLoading(true)
    axios
      .get('http://localhost:8080/stock/milk')
      .then((res) => {
        setMilkStock(res.data)
      })
      .catch(() => {
        setMilkStockLoading(false)
      })
    setMilkStockLoading(false)
  }, [])

  useEffect(() => {
    fetchHealthCallback()
    fetchOrdersPendingCallback()
    fetchOrdersCompletedCallback()
    fetchOrdersFailedCallback()
    fetchBeanStockCallback()
    fetchMilkStockCallback()
    const timer = setInterval(() => {
      fetchHealthCallback()
      fetchOrdersPendingCallback()
      fetchOrdersCompletedCallback()
      fetchOrdersFailedCallback()
      fetchBeanStockCallback()
      fetchMilkStockCallback()
    }, 1000)

    return () => {
      clearInterval(timer)
    }
  }, [
    fetchHealthCallback,
    fetchOrdersPendingCallback,
    fetchOrdersCompletedCallback,
    fetchOrdersFailedCallback,
    fetchBeanStockCallback,
    fetchMilkStockCallback,
  ])

  return (
    <>
      <AppBar position="sticky" className="brand-bar">
        <Toolbar>
          <IconButton>
            <Coffee />
          </IconButton>
          <Typography variant="h6" className="brand-name" sx={{ flexGrow: 1 }}>
            <IconButton color="inherit">Coffee Dashboard UI</IconButton>
          </Typography>
          <Stack direction="row" spacing={2}>
            <Button color="inherit">Getting Started</Button>
            <Button color="inherit">Documentation</Button>
            <Button>
              <Badge badgeContent={4} color="secondary">
                <Avatar src="https://i.pravatar.cc/50" alt="Chitranjan" />
              </Badge>
            </Button>
          </Stack>
        </Toolbar>
      </AppBar>
      <Box m={2}>
        <Breadcrumbs
          maxItems={3}
          itemsAfterCollapse={2}
          separator={<NavigateNext />}
        >
          <Link href="#" underline="hover">
            Home
          </Link>
          <Link href="#" underline="hover">
            Getting Started
          </Link>
        </Breadcrumbs>
      </Box>
      <div className="container">
        <Stack direction="row" className="card-container">
          <MyCard
            title="Health Status"
            content={healthStatus}
            loading={healthLoading}
          />
          <MyCard
            title="Beans Stock"
            content={`${beansStock}`}
            loading={beansStockLoading}
          />
          <MyCard
            title="Milk Stock"
            content={`${milkStock}`}
            loading={milkStockLoading}
          />
          <MyCard
            title="Orders Pending"
            content={`${ordersPending}`}
            loading={ordersPendingLoading}
          />
          <MyCard
            title="Orders Completed"
            content={`${ordersCompleted}`}
            loading={ordersCompletedLoading}
          />
          <MyCard
            title="Orders Failed"
            content={`${ordersFailed}`}
            loading={ordersFailedLoading}
          />
        </Stack>
        <Grid container>
          <Grid item xs={12} md={2}>
            <Stack direction="column">
              <ToggleButtonGroup
                value={activeNav}
                onChange={(
                  e: React.MouseEvent<HTMLElement, MouseEvent>,
                  val: string
                ) => {
                  setActiveNav(val)
                }}
                orientation="vertical"
                exclusive
                color="success"
                size="large"
              >
                <ToggleButton value="dashboard">
                  <IconButton size="large" disableRipple>
                    <Stack
                      direction="row"
                      sx={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '10px',
                      }}
                    >
                      <Dashboard />
                      <Typography variant="h6">Order Dashboard</Typography>
                    </Stack>
                  </IconButton>
                </ToggleButton>
                <ToggleButton value="order">
                  <IconButton size="large" disableRipple>
                    <Stack
                      direction="row"
                      sx={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '10px',
                      }}
                    >
                      <Apps />
                      <Typography variant="h6">Order Coffee</Typography>
                    </Stack>
                  </IconButton>
                </ToggleButton>
                <ToggleButton value="Serve">
                  <IconButton size="large" disableRipple>
                    <Dashboard />
                  </IconButton>
                </ToggleButton>

                <ToggleButton value="todo">
                  <IconButton size="large" disableRipple>
                    <Dashboard />
                  </IconButton>
                </ToggleButton>
              </ToggleButtonGroup>
            </Stack>
          </Grid>
          <Grid item xs={12} md={10}>
            <Paper className="body-container">
              {activeNav && activeNav === 'dashboard' && <Chart />}
              {activeNav && activeNav === 'order' && <Kiosk />}
            </Paper>
          </Grid>
        </Grid>
      </div>
      <SpeedDial
        sx={{ position: 'absolute', bottom: 16, right: 16 }}
        ariaLabel="speed-dial"
        icon={<SpeedDialIcon />}
      >
        <SpeedDialAction icon={<Share />} tooltipTitle="Share" />
        <SpeedDialAction icon={<Print />} tooltipTitle="Print" />
      </SpeedDial>
    </>
  )
}

export default Layout
