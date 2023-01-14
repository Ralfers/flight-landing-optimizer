import React, { useState } from 'react'

import Form from 'react-bootstrap/Form'

const containerStyle = {
    marginLeft: '30px',
    marginTop: '50px',
    borderStyle: 'dotted',
    width: '800px',
}

const elementContainerStyle = {
    borderBottom: '1px solid',
    width: '794px',
}

const labelStyle = {
    marginLeft: '16px',
}

const ScoreExplanation = (props) => {
    const { solutionExplanation } = props

    const getExplanations = () => {
        const components = []

        Object.keys(solutionExplanation)
        .filter(planeId => {
            const score = solutionExplanation[planeId].score
            return score.hardScore < 0 || score.softScore < 0
        })
        .forEach(planeId => {
            const planeExplanation = solutionExplanation[planeId]
            const constraintMatchSet = planeExplanation.constraintMatchSet

            constraintMatchSet
            .filter(constraintMatch => {
                const score = constraintMatch.score
                return score.hardScore < 0 || score.softScore < 0
            })
            .forEach(constraintMatch => {
                const score = constraintMatch.score
                const reasonParts = constraintMatch.identificationString.split('/')
                const reason = reasonParts[1] + ' (' + reasonParts[2] + ')'

                const element = <div style={elementContainerStyle}>
                    <Form.Label style={labelStyle}>Hard score: {score.hardScore}</Form.Label><br/>
                    <Form.Label style={labelStyle}>Soft score: {score.softScore}</Form.Label><br/>
                    <Form.Label style={labelStyle}>Reason: {reason}</Form.Label>
                </div>
                components.push(element)
            })
        })

        return components
    }

    return (
        <div style={containerStyle}>
            {getExplanations()}
        </div>
    )
}

export default ScoreExplanation
