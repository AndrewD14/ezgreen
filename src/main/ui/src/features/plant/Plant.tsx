import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Stack } from '@mui/material';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import moment from 'moment-timezone';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { plantRoutes } from '../../service/ApiService';
import { formatOne } from '../../service/utils/plantFormat';
import LineChart from '../common/chart/LineChart';

function Plant() {
   const [plant, setPlant] = useState<any>();
   const [stompClient, setStomp] = useState<any>();
   let { state } = useLocation();

   const fetchData = async () => {
      let data = {};
      
      try
      {
         data = await plantRoutes.fetchOnePlantWithDetails(state.plantId);

         console.log(formatOne(data))
         setPlant(formatOne(data));
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
      }
   };

   const subscribe = () => {
      console.log(plant)
      if(plant === undefined || plant.id === undefined) return;

      let socket = new SockJS('http:localhost:5000/ezgreen');
      let stompClient = Stomp.over(socket);

      stompClient.connect({}, (frame: any) => {
         setStomp(stompClient);

         stompClient.subscribe('/topic/plant/' + plant.id, updateData);
      });
   };

   const unsubscribe = () => {
      if (stompClient !== undefined) {
         stompClient.disconnect();
     }
   };

   const updateData = (data: any) => {
      console.log(data)
      fetchData();
   };

   useEffect(() => {
      fetchData();

      return () => {
         unsubscribe()
      };
   },
   //eslint-disable-next-line
   []);

   useEffect(() => {
      subscribe();
   },
   //eslint-disable-next-line
   [plant?.id]);

   return (
      <div>
         <h3>Plant: {plant?.name}</h3>
         <Grid2 container>
            <Grid2 container xs={12} lg={4}>
               <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Name</label></Grid2>
                     <Grid2 xs>{plant?.name}{plant?.number ? ' (' + plant?.number + ')' : ''}</Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Plant type</label></Grid2>
                     <Grid2 xs>{plant?.plantType.name}</Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Date Obtained</label></Grid2>
                     <Grid2 xs>{plant?.dateObtain}</Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Last Watered</label></Grid2>
                     <Grid2 xs></Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Montiored</label></Grid2>
                     <Grid2 xs>{plant?.monitor === 1 ? <CheckIcon style={{color: '#7db856'}}/> : <BlockIcon style={{color: '#e3272b'}}/>}</Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Sensor</label></Grid2>
                     <Grid2 xs>{plant?.sensorType?.type +' (' + plant?.sensor.number + ')'}</Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Pot Size</label></Grid2>
                     <Grid2 xs>{plant?.potSize.name + " (" + plant?.potSize.size +")"}</Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Last Updated By</label></Grid2>
                     <Grid2 xs>{plant?.updateBy}</Grid2>
                  </Grid2>
                  <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={3}><label className='labels'>Last Updated</label></Grid2>
                     <Grid2 xs>{plant?.updateTs !== '' ? moment.utc(plant?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
                  </Grid2>
               </Stack>
            </Grid2>
            <Grid2 container xs={12} lg={8}>
               <Grid2 xs>
                  {plant?.histories !== undefined && plant?.histories.length > 0 ? <LineChart readings={plant?.histories}/> : null}
               </Grid2>
            </Grid2>
         </Grid2>
         
      </div>
   );
}

export default Plant;