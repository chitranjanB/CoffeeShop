import {
  Card,
  CardContent,
  CardMedia,
  Skeleton,
  Typography,
} from '@mui/material'
import './InfoCard.css'

interface CardProps {
  title: string
  content: string
  loading: boolean
}
function InfoCard({ title, content, loading }: CardProps) {
  return (
    <Card sx={{ width: '15rem' }}>
      {loading ? (
        <Skeleton variant="rectangular" height="140px" />
      ) : (
        <CardMedia
          component="img"
          height="140"
          image="https://picsum.photos/200"
        ></CardMedia>
      )}
      <CardContent
        sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}
      >
        {loading ? (
          <Skeleton width="150px" />
        ) : (
          <Typography variant="h6" className="title">
            {title}
          </Typography>
        )}
        {loading ? (
          <Skeleton width="50px" />
        ) : (
          <Typography variant="h3" component={'p'} className="data">
            {content}
          </Typography>
        )}
      </CardContent>
    </Card>
  )
}

export default InfoCard
