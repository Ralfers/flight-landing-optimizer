import React, { useState } from 'react'

import Form from 'react-bootstrap/Form'

const containerStyle = {
    marginLeft: '30px',
    marginTop: '50px',
    borderStyle: 'dotted',
    width: '200px',
}

const labelStyle = {
    marginLeft: '16px',
}

const Score = (props) => {
    const { solution } = props

    const score = solution.score

    return (
        <div style={containerStyle}>
            <Form.Label style={labelStyle}>Score: {score}</Form.Label><br/>
        </div>
    )
}

export default Score
