import React, { useState, useEffect } from 'react';
import { useLocation, useOutletContext } from 'react-router-dom';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Stack } from '@mui/material';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import moment from 'moment-timezone';
import { plantRoutes } from '../../service/ApiService';
import { formatOne } from '../../service/utils/plantFormat';
import LineChart from '../common/chart/LineChart';

function Plant(props: any) {
   const [plant, setPlant] = useState<any>();
   const [annotations, setAnnotations] = useState<any>({});
   const [subscription, setSub] = useState<any>();
   const stompClient: any = useOutletContext();
   let { state } = useLocation();

   let xaxis: any  = {
      type: "datetime",
      labels: {
         format: 'MM/dd/yyyy HH:mm',
       }
   };
   
   let yaxis: any = {
      min: 0,
      max: 100
   };

   const fetchData = async () => {
      let data:any = {};
      
      try
      {
         data = formatOne(await plantRoutes.fetchOnePlantWithDetails(state.plantId));

         setAnnotations({
            yaxis: [
               {
                  y: data.highMoisture,
                  borderColor: '#e81010',
                  label: {
                     borderColor: '#e81010',
                     style: {
                        color: '#fff',
                        background: '#e81010',
                     },
                     text: 'Over watered',
                  }
               },
               {
                  y: data.lowMoisture,
                  borderColor: '#e81010',
                  label: {
                     borderColor: '#e81010',
                     style: {
                        color: '#fff',
                        background: '#e81010',
                     },
                     text: 'Under watered',
                  }
               },
            ],
         });

         console.log(data)
         setPlant(data);
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
      }
   };

   const subscribe = () => {
      if(plant === undefined || plant.id === undefined) return;
      if(stompClient === undefined) return;

      let sub = stompClient.subscribe('/topic/plant/' + plant.id, updateData, {id: 'plant'});

      setSub(sub);
   };

   const unsubscribe = () => {
      if(subscription !== undefined) subscription.unsubscribe();
      if(stompClient !== undefined) stompClient.unsubscribe('plant');
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
                  {plant?.histories !== undefined && plant?.histories.length > 0 ? 
                     <LineChart readings={plant?.histories}
                        annotations={annotations}
                        xaxis={xaxis}
                        yaxis={yaxis}
                     />
                  : null}
               </Grid2>
            </Grid2>
         </Grid2>
         
      </div>
   );
}

export default Plant;