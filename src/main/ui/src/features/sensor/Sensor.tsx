import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Stack } from '@mui/material';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import moment from 'moment-timezone';
import _ from 'lodash';
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
                  <Grid2 xs>{sensor?.sensorType.type}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Serial bus</label></Grid2>
                  <Grid2 xs>{sensor?.boardInfo.bus}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Board</label></Grid2>
                  <Grid2 xs>{sensor?.boardInfo.id}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Port</label></Grid2>
                  <Grid2 xs>{sensor?.port}</Grid2>
               </Grid2>
               {sensorsWithCal.indexOf(sensor?.sensorType.type) !== -1 ?
                  [<Grid2 key='low' xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={2}><label className='labels'>Low calibration</label></Grid2>
                     <Grid2 xs>{sensor?.lowCalibration !== undefined ? parseFloat(sensor?.lowCalibration).toFixed(2) : null}</Grid2>
                  </Grid2>,
                  <Grid2 key='high' xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                     <Grid2 xs={2}><label className='labels'>High calibration</label></Grid2>
                     <Grid2 xs>{sensor?.highCalibration !== undefined ? parseFloat(sensor?.highCalibration).toFixed(2) : null}</Grid2>
                  </Grid2>]
               :
                  null
               }
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>In use</label></Grid2>
                  <Grid2 xs>{!_.isEmpty(sensor?.plant) || !_.isEmpty(sensor?.environment) ? <CheckIcon style={{color: '#7db856'}}/> 
                              :
                              sensor?.sensorType['arduino'] !== 'w' ? <BlockIcon style={{color: '#e3272b'}}/>
                              :
                              'N/A'
                              }
                  </Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Used by</label></Grid2>
                  <Grid2 xs>{sensor?.plant !== undefined ? <Link to={'/plant/' + sensor['plant']['id']} state={{plantId: sensor['plant']['id']}}>{sensor['plant']['number'] ? sensor['plant']['name'] + ' (' + sensor['plant']['number'] + ')' : sensor['plant']['name']}</Link>
                              :
                              sensor?.environment !== undefined ? <Link to={'/environment/' + sensor['environment']['id']} state={{environmentId: sensor['environment']['id']}}>{sensor['environment']['name']}</Link>
                              :
                              sensor?.zoneId ? 'Zone ' + sensor['zoneId']
                              :
                              null
                           }
                  </Grid2>
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