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
import { useState } from 'react'

const Kiosk = () => {
  const [dialogOpen, setDialogOpen] = useState<boolean>(false)
  const [isSubmitted, setIsSubmitted] = useState<boolean>(false)

  const orderCoffee = () => {
    setDialogOpen(true)
  }
  return (
    <>
      <Typography
        variant="h5"
        className="waiting"
        sx={{ display: 'flex', justifyContent: 'center' }}
      >
        Kiosk
      </Typography>
      <Stack direction="row">
        <TextField label="Customer Name"></TextField>
        <TextField label="No of Orders"></TextField>
        <Button variant="outlined" onClick={orderCoffee}>
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
            <Button
              onClick={() => {
                setDialogOpen(false)
                setIsSubmitted(true)
              }}
              autoFocus
            >
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
    </>
  )
}

export default Kiosk
