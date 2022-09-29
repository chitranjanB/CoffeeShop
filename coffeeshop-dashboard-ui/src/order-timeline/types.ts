import React from 'react'

export type OrderType = {
  customerId: string
  orderCreatedDate: string
  orderEndedDate: string
  orderId: string
  status: string
  transactions: TransactionType[]
}

type TransactionType = {
  customerId: string
  status: string
  timeEnded: string
  timeStarted: string
  transactionId: string
}
