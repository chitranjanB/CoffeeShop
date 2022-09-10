import { Typography } from '@mui/material'
import './Card.css'

interface CardProps {
  title: string
  content: string
}
function Card({ title, content }: CardProps) {
  return (
    <div className="card">
      <Typography variant="h6" className="title" gutterBottom>
        {title}
      </Typography>
      <Typography variant="h3" component={'p'} className="data">
        {content}
      </Typography>
    </div>
  )
}

export default Card
