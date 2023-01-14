import React, { useState } from 'react'

import Controls from './components/controls'
import Score from './components/score'
import ScoreExplanation from './components/score-explanation'
import LandingTimeline from './components/landing-timeline'

const appStyle = {
    paddingTop: '30px',
    width: '100%',
    height: '100%',
}

const App = () => {
    const [ solution, setSolution ] = useState({})
    const [ solutionExplanation, setSolutionExplanation ] = useState({})
    const solutionFound = Object.keys(solution).length > 0
    const solutionExplanationFound = Object.keys(solutionExplanation).length > 0

    return (
        <div style={appStyle}>
            <Controls setSolution={setSolution} setSolutionExplanation={setSolutionExplanation} />
            { solutionFound && <Score solution={solution} /> }
            { solutionExplanationFound && <ScoreExplanation solutionExplanation={solutionExplanation} /> }
            { solutionFound && <LandingTimeline solution={solution} /> }
        </div>
    )
}

export default App
