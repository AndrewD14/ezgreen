import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Stack } from '@mui/material';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import moment from 'moment-timezone';
import { sensorRoutes } from '../../service/ApiService';
import { formatOne } from '../../service/utils/sensorFormat';

function Sensor() {
   const [sensor, setSensor] = useState<any>();
   let { state } = useLocation();

   const sensorsWithCal: string[] = ['Soil moisture', 'Water level'];

   const fetchData = async () => {
      let data = {};
      
      try
      {
         data = await sensorRoutes.fetchOneSensorWithDetails(state.sensorId);

         console.log(formatOne(data))
         setSensor(formatOne(data));
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
      }
   };

   useEffect(() => {
      fetchData();
   },
   //eslint-disable-next-line
   []);

   return (
      <div>
         <h3>Sensor: {sensor?.type}</h3>
         <Grid2 container xl={6}>
            <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Type</label></Grid2>
                  <Grid2 xs>{sensor?.type}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Board</label></Grid2>
                  <Grid2 xs>{sensor?.board}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Port</label></Grid2>
                  <Grid2 xs>{sensor?.port}</Grid2>
               </Grid2>
               {sensorsWithCal.indexOf(sensor?.type) !== -1 ?
                  [<Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={2}><label className='labels'>Low calibration</label></Grid2>
                     <Grid2 xs>{sensor?.lowCalibration}</Grid2>
                  </Grid2>,
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={2}><label className='labels'>High calibration</label></Grid2>
                     <Grid2 xs>{sensor?.highCalibration}</Grid2>
                  </Grid2>]
               :
                  null
               }
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>In use</label></Grid2>
                  <Grid2 xs>{(sensor?.environment || sensor?.plant) ? <CheckIcon style={{color: '#7db856'}}/> : <BlockIcon style={{color: '#e3272b'}}/>}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated By</label></Grid2>
                  <Grid2 xs>{sensor?.updateBy}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated</label></Grid2>
                  <Grid2 xs>{sensor?.updateTs !== '' ? moment.utc(sensor?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
               </Grid2>
            </Stack>
         </Grid2>
         <Grid2 xs={6}>

         </Grid2>
      </div>
   );
}

export default Sensor;