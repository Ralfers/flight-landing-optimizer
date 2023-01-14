import React, { useState, useEffect } from 'react'

import Form from 'react-bootstrap/Form'
import { Timeline } from 'react-svg-timeline'
import AutoSizer from 'react-virtualized-auto-sizer'

const containerStyle = {
    marginTop: '30px',
}

const labelStyle = {
    marginLeft: '10px',
    fontSize: '20px',
    fontWeight: 'bold',
}

const LandingTimeline = props => {
    const { solution } = props

    const [width, setWidth]   = useState(window.innerWidth)
    const [height, setHeight] = useState(window.innerHeight)

    const updateDimensions = () => {
        setWidth(window.innerWidth)
        setHeight(window.innerHeight)
    }

    useEffect(() => {
        window.addEventListener("resize", updateDimensions);
        return () => window.removeEventListener("resize", updateDimensions);
    }, [])

    const baseTimestamp = 1673697600000
    const endTimestamp =  1673784000000
    const dateFormat = (ms) => new Date(ms).toUTCString()

    const getLanes = planeList => {
        const lanes = []
        planeList.sort((plane1, plane2) => plane1.landingTime - plane2.landingTime)

        planeList.forEach(plane => {
            const laneId = 'lane-plane-' + plane.id
            lanes.push({
                laneId: laneId,
                label: 'Plane ' + plane.id
            })
        })

        console.log('lanes:')
        console.log(JSON.stringify(lanes))

        return lanes
    }

    const getEvents = planeList => {
        const events = []
        planeList.sort((plane1, plane2) => plane1.landingTime - plane2.landingTime)

        planeList.forEach((plane, index) => {
            const laneId = 'lane-plane-' + plane.id
            const eventId = 'event-plane-' + plane.id
            const planeLabel = 'plane ' + plane.id
            var randomColor = '#' + Math.floor(Math.random() * 16777215).toString(16);

            const nextPlaneId = index + 1 < planeList.length
                        ? planeList[index + 1].id
                        : undefined
            const separationTime = nextPlaneId
                    ? plane.separationTimes[nextPlaneId - 1]
                    : undefined

            const landingIntervalStartMillis = baseTimestamp + plane.earliestLandingTime * 60000
            const landingIntervalEndMillis = baseTimestamp + plane.latestLandingTime * 60000
            const landingIntervalTooltip = 'Landing interval: ' + planeLabel
                    + '\nStart: ' + new Date(landingIntervalStartMillis).toUTCString()
                    + '\nEnd: ' + new Date(landingIntervalEndMillis).toUTCString()
            events.push({
                eventId: 'interval-' + eventId,
                tooltip: landingIntervalTooltip,
                laneId: laneId,
                color: randomColor,
                startTimeMillis: landingIntervalStartMillis,
                endTimeMillis: landingIntervalEndMillis,
            })

            if (separationTime) {
                const separationTimeStartMillis = baseTimestamp + plane.landingTime * 60000
                const separationTimeEndMillis = baseTimestamp + (plane.landingTime + separationTime) * 60000
                const separationTimeTooltip = 'Separation interval: ' + planeLabel
                        + '\nStart: ' + new Date(separationTimeStartMillis).toUTCString()
                        + '\nEnd: ' + new Date(separationTimeEndMillis).toUTCString()
                events.push({
                    eventId: 'separation-' + eventId,
                    tooltip: separationTimeTooltip,
                    laneId: laneId,
                    color: randomColor,
                    startTimeMillis: separationTimeStartMillis,
                    endTimeMillis: separationTimeEndMillis,
                })
            }

            const arrivalTimeStartMillis = baseTimestamp + plane.arrivalTime * 60000
            const arrivalTimeTooltip = 'Arrival: ' + planeLabel
                    + '\nOn: ' + new Date(arrivalTimeStartMillis).toUTCString()
            events.push({
                eventId: 'arrival-' + eventId,
                tooltip: arrivalTimeTooltip,
                laneId: laneId,
                color: randomColor,
                startTimeMillis: arrivalTimeStartMillis,
            })

            const targetTimeStartMillis = baseTimestamp + plane.targetLandingTime * 60000
            const targetTimeTooltip = 'Target: ' + planeLabel
                    + '\nOn: ' + new Date(targetTimeStartMillis).toUTCString()
            events.push({
                eventId: 'target-' + eventId,
                tooltip: targetTimeTooltip,
                laneId: laneId,
                color: randomColor,
                startTimeMillis: targetTimeStartMillis,
            })

            const landingTimeStartMillis = baseTimestamp + plane.landingTime * 60000
            const landingTooltip = (plane.targetLandingTime === plane.landingTime ? 'Landing (on target): ' : 'Landing: ')
                    + planeLabel
                    + '\nOn: ' + new Date(landingTimeStartMillis).toUTCString()
            events.push({
                eventId: 'landing-' + eventId,
                tooltip: landingTooltip,
                laneId: laneId,
                color: randomColor,
                startTimeMillis: landingTimeStartMillis
            })
        })

        console.log('events:')
        console.log(JSON.stringify(events))
        
        return events
    }

    const getTimeline = runway => {
        const runwayPlanes = solution.planeList.filter(plane => plane.runway?.id === runway.id)
        return <React.Fragment>
            <Form.Label style={labelStyle}>Runway: {runway.id}</Form.Label>
            <Timeline
                width={width}
                height={height}
                lanes={getLanes(runwayPlanes)}
                events={getEvents(runwayPlanes)}
                dateFormat={dateFormat}
                customRange={[baseTimestamp, endTimestamp]}
            />
        </React.Fragment>
    }

    return (
        <div style={containerStyle}>
            {solution.runwayList.map(runway => getTimeline(runway))}
        </div>
    )
}

export default LandingTimeline
