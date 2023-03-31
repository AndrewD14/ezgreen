import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Stack } from '@mui/material';
import moment from 'moment-timezone';
import { environmentRoutes } from '../../service/ApiService';
import { formatOne } from '../../service/utils/environmentFormat';

function Environment() {
   const [environment, setEnvironment] = useState<any>();
   let { state } = useLocation();

   const fetchData = async () => {
      let data = {};
      
      try
      {
         data = await environmentRoutes.fetchOneEnvironmentWithDetail(state.environmentId);

         setEnvironment(formatOne(data));
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
         <h3>Environment: {environment?.name}</h3>
         <Grid2 container xl={6}>
            <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Sensors</label></Grid2>
                  <Grid2 xs>{environment?.sensors.map((sensor: any) => <Grid2 key={'sensor-' + sensor['id']}><Link to={'/sensor/' + sensor['id']} state={{sensorId: sensor['id']}}>{sensor.type.type + ' (' + sensor['number'] + ')'}</Link></Grid2>)}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Zone</label></Grid2>
                  <Grid2 xs>{environment?.zoneId}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated By</label></Grid2>
                  <Grid2 xs>{environment?.updateBy}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated</label></Grid2>
                  <Grid2 xs>{environment?.updateTs !== '' ? moment.utc(environment?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
               </Grid2>
            </Stack>
         </Grid2>
         <Grid2 xs={6}>

         </Grid2>
      </div>
   );
}

export default Environment;