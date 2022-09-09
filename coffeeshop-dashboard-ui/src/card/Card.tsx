import React from 'react'
import './Card.css'

interface CardProps {
  title: string
  content: string
}
function Card({ title, content }: CardProps) {
  return (
    <div className="card">
      <h3 className="title">{title}</h3>
      <p className="data">{content}</p>
    </div>
  )
}

export default Card
