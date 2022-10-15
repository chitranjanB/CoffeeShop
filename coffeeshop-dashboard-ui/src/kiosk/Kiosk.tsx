import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Snackbar,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import './kiosk.css'
import { useCallback, useEffect, useMemo, useState } from 'react'
import axios from 'axios'

const Kiosk = () => {
  const [dialogOpen, setDialogOpen] = useState<boolean>(false)
  const [isSubmitted, setIsSubmitted] = useState<boolean>(false)

  const [customerName, setCustomerName] = useState<string | null>(null)
  const [orderQty, setOrderQty] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState<boolean>(false)
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
      .post('/coffeeshop-api/auth/authenticate', {
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

  const createOrder = () => {
    setDialogOpen(false)
    setIsLoading(true)
    const order = { customerId: customerName, orders: orderQty }
    axios
      .post('/coffeeshop-api/process', order, config)
      .then((res) => {
        setIsSubmitted(true)
      })
      .catch(() => {
        setIsLoading(false)
        setIsSubmitted(false)
      })
    setIsLoading(false)
    setIsSubmitted(true)
  }

  useEffect(() => {
    !token && fetchAuthToken()
  }, [token])

  return (
    <div className="kiosk">
      <Typography variant="h5" className="waiting" sx={{ fontWeight: 400 }}>
        Order Coffee
      </Typography>
      <Stack direction="row">
        <TextField
          label="Customer Name"
          onChange={(e) => setCustomerName(e.target.value)}
        ></TextField>
        <TextField
          label="No of Orders"
          onChange={(e) => setOrderQty(e.target.value)}
        ></TextField>
        <Button variant="outlined" onClick={() => setDialogOpen(true)}>
          Order Coffee
        </Button>
        <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
          <DialogTitle>Submit</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Are you sure you want to submit this Order?
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
            <Button onClick={token ? createOrder : () => {}} autoFocus>
              Submit
            </Button>
          </DialogActions>
        </Dialog>

        <Snackbar
          message="Form submitted successfully"
          open={isSubmitted}
          onClose={() => setIsSubmitted(false)}
          autoHideDuration={1000}
        />
      </Stack>
    </div>
  )
}

export default Kiosk
