import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Stack } from '@mui/material';
//import moment from 'moment';
import moment from 'moment-timezone';
import { plantRoutes } from '../../service/ApiService';

function Plant() {
   const [plant, setPlant] = useState<any>();
   let { state } = useLocation();

   const fetchData = async () => {
      let data = {};
      
      try
      {
         data = await plantRoutes.fetchOnePlantWithDetails(state.plantId);

         console.log(data);
         setPlant(data);
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
         <h3>Plant: {plant?.name}</h3>
         <Grid2 container xl={6}>
            <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Name</label></Grid2>
                  <Grid2 xs>{plant?.name}{' ('}{plant?.number}{')'}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Date Obtained</label></Grid2>
                  <Grid2 xs>{plant?.dateObtain}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Watered</label></Grid2>
                  <Grid2 xs></Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Montiored</label></Grid2>
                  <Grid2 xs></Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Sensor</label></Grid2>
                  <Grid2 xs>{plant?.type}{' ('}{plant?.sensorId}{')'}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Pot Size</label></Grid2>
                  <Grid2 xs>{plant?.size}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated By</label></Grid2>
                  <Grid2 xs>{plant?.updateBy}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated</label></Grid2>
                  <Grid2 xs>{plant?.updateTs !== '' ? moment.utc(plant?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
               </Grid2>
            </Stack>
         </Grid2>
         <Grid2 xs={6}>

         </Grid2>
      </div>
   );
}

export default Plant;