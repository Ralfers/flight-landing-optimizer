import React, { useState } from 'react'

import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

const containerStyle = {
    marginLeft: '30px',
    marginTop: '14px',
}

const labelStyle = {
    marginRight: '14px',
}

const inputStyle = {
    width: '350px',
    display: 'inline-block',
}

const buttonStyle = {
    display: 'block',
    marginTop: '14px',
}

const Controls = props => {
    const { setSolution, setSolutionExplanation } = props

    const [ solutionId, setSolutionId ] = useState('')

    const baseTimestamp = 1673697600000

    const onChange = event => {
        setSolutionId(event.target.value)
    }

    const onClick = event => {
        fetch('/landing-plan/solution/' + solutionId)
            .then(response => response.json())
            .then(data => setSolution(data))

        fetch('/landing-plan/solution/' + solutionId + '/explanation')
            .then(response => response.json())
            .then(data => setSolutionExplanation(data))
    }

    return (
        <div style={containerStyle}>
            <Form.Label style={labelStyle}>Solution ID:</Form.Label>
            <Form.Control
                style={inputStyle}
                type="text"
                value={solutionId}
                onChange={onChange}
            />
            <Button
                style={buttonStyle}
                variant="primary"
                onClick={onClick}
            >
                Refresh
            </Button>
        </div>
    )
}

export default Controls
