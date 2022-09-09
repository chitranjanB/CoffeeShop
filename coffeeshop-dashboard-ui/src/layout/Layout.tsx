import axios from 'axios'
import { useCallback, useEffect, useState } from 'react'
import Card from '../card/Card'
import Chart from '../chart/Chart'
import './Layout.css'

function Layout() {
  const [healthStatus, setHealthStatus] = useState<'DOWN' | 'UP'>('DOWN')
  const [ordersPending, setOrdersPending] = useState<number>(0)
  const [ordersCompleted, setOrdersCompleted] = useState<number>(0)
  const [ordersFailed, setOrdersFailed] = useState<number>(0)
  const [beansStock, setBeansStock] = useState<number>(0)
  const [milkStock, setMilkStock] = useState<number>(0)

  const fetchHealthCallback = useCallback(() => {
    axios.get('http://localhost:8080/actuator/health').then((res) => {
      setHealthStatus(res.data.status)
    })
  }, [])

  const fetchOrdersPendingCallback = useCallback(() => {
    axios.get('http://localhost:8080/orders?status=PENDING').then((res) => {
      setOrdersPending(res.data.length)
    })
  }, [])

  const fetchOrdersCompletedCallback = useCallback(() => {
    axios.get('http://localhost:8080/orders?status=COMPLETE').then((res) => {
      setOrdersCompleted(res.data.length)
    })
  }, [])

  const fetchOrdersFailedCallback = useCallback(() => {
    axios.get('http://localhost:8080/orders?status=ERROR').then((res) => {
      setOrdersFailed(res.data.length)
    })
  }, [])

  const fetchBeanStockCallback = useCallback(() => {
    axios.get('http://localhost:8080/stock/beans').then((res) => {
      setBeansStock(res.data)
    })
  }, [])

  const fetchMilkStockCallback = useCallback(() => {
    axios.get('http://localhost:8080/stock/milk').then((res) => {
      setMilkStock(res.data)
    })
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
    <div className="container">
      <div className="brand-bar">
        <div className="brand-name"> Coffee Dashboard UI</div>
      </div>
      <div className="card-container">
        <Card title="Health Status" content={healthStatus} />
        <Card title="Beans Stock" content={`${beansStock}`} />
        <Card title="Milk Stock" content={`${milkStock}`} />
        <Card title="Orders Pending" content={`${ordersPending}`} />
        <Card title="Orders Completed" content={`${ordersCompleted}`} />
        <Card title="Orders Failed" content={`${ordersFailed}`} />
      </div>
      <div className="body-container">
        <div className="left-nav">
          <ul className="left-nav-container">
            <li>Real Time Dashboard</li>
            <li>Real Time Dashboard</li>
            <li>Real Time Dashboard</li>
            <li>Real Time Dashboard</li>
          </ul>
        </div>
        <div className="content">
          <Chart />
        </div>
      </div>
    </div>
  )
}

export default Layout
