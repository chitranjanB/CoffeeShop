import { Button, Stack, TextField, Typography } from '@mui/material'

import React from 'react'

const Kiosk = () => {
  const orderCoffee = () => {
    console.log('ordering coffee')
  }
  return (
    <>
      <Typography variant="h5" className="waiting" alignContent="center">
        Kiosk
      </Typography>
      <Stack>
        <TextField label="Customer Name"></TextField>
        <TextField label="No of Orders"></TextField>
        <Button variant="outlined" onClick={orderCoffee}>
          Order Coffee
        </Button>
      </Stack>
    </>
  )
}

export default Kiosk
