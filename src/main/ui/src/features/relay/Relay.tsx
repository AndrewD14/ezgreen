import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Stack } from '@mui/material';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import moment from 'moment-timezone';
import _ from 'lodash';
import { relayRoutes } from '../../service/ApiService';
import { formatOne } from '../../service/utils/relayFormat';

function Relay() {
   const [relay, setRelay] = useState<any>();
   let { state } = useLocation();

   const sensorsWithCal: string[] = ['Soil moisture', 'Water level'];

   const fetchData = async () => {
      let data = {};
      
      try
      {
         data = await relayRoutes.fetchOneRelayWithDetail(state.relayId);

         setRelay(formatOne(data));
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
         <h3>Actuator: {relay?.type.type + ' (' + relay?.number + ')'}</h3>
         <Grid2 container xl={6}>
            <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Serial bus</label></Grid2>
                  <Grid2 xs>{relay?.board.bus}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Board</label></Grid2>
                  <Grid2 xs>{relay?.board.id}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Relay</label></Grid2>
                  <Grid2 xs>{relay?.relay}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Environment</label></Grid2>
                  <Grid2 xs>{relay?.environment ? <Link to={'/environment/' + relay?.environment['id']} state={{environmentId: relay?.environment['id']}}>{relay?.environment.name}</Link> : null}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated By</label></Grid2>
                  <Grid2 xs>{relay?.updateBy}</Grid2>
               </Grid2>
               <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                  <Grid2 xs={2}><label className='labels'>Last Updated</label></Grid2>
                  <Grid2 xs>{relay?.updateTs !== '' ? moment.utc(relay?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
               </Grid2>
            </Stack>
         </Grid2>
         <Grid2 xs={6}>

         </Grid2>
      </div>
   );
}

export default Relay;