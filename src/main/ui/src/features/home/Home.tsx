import React, { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import moment from 'moment-timezone';
import { Card, CardHeader, CardContent, CardActions,
   CircularProgress, Stack } from '@mui/material';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import Button from '@mui/joy/Button';
import FormatColorResetIcon from '@mui/icons-material/FormatColorReset';
import ShowerIcon from '@mui/icons-material/Shower';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import Alert from '../common/alert/Alert';
import { plantRoutes } from '../../service/ApiService';
import { formatAll } from '../../service/utils/plantFormat';

function Home() {
   const [plants, setPlants] = useState<any[]>([]);
   const [alert, setAlert] = useState<any>(null);
   const [loading, setLoading] = useState(true);
   const mounted = useRef(true);

   const actions = (plant: any) => {
      return(
         <React.Fragment>
            <Link key={"monitor-" + plant['id']}
               style={{ textDecoration: 'none', color: '#1e1e1e', pointerEvents: (plant['dead'] === 1 || !plant['sensorId']) ? 'none' : 'auto'}}
               to={''}
               title="Monitor/Disable plant"
               onClick={() => {
                  let save = async () => {
                     try
                     {
                        plant['monitor'] === 1 ? await plantRoutes.deactivate(plant['id'], 'a.damico')
                        :
                        await plantRoutes.activate(plant['id'], 'a.damico')
                        
                        await fetchData();
               
                        setAlert(null);
                     }
                     catch(error)
                     {
                        if(mounted.current)
                        {
                           console.log(error);
                           setAlert(null);
                        }
                     }
                  }
   
                  if(mounted.current)
                  {
                     setAlert(
                        <Alert
                           title={plant['monitor'] === 1 ? "Deactivate monitoring?" : 'Acttivate monitoring?' }
                           closeText="Cancel"
                           saveText="Confirm"
                           open={true}
                           onClose={() => setAlert(null)}
                           onSave={save}
                           style={{minWidth: '33rem'}}
                        >
                           <p>Would you like to {plant['monitor'] === 1 ? 'deactivate' : 'activate'} the monitoring for {plant['name']}{plant['number'] ? ' (' + plant['number'] + ')' : ''}?</p>
                        </Alert>
                     );
                  }
               }}
            >
               { plant['monitor'] === 1 ? <FormatColorResetIcon /> : <ShowerIcon className={(plant['dead'] === 1 || !plant['sensorId']) ? 'disabled-link ' : ''}/> }
            </Link>
            <Link key={"edit-" + plant['id']}
               style={{ textDecoration: 'none', color: '#1e1e1e' }}
               to={'/plant/edit/' + plant['id']}
               state={{plantId: plant['id']}}
               title="Edit plant"
            >
               <EditIcon />
            </Link>
            <Link key={'delete-' + plant['id']}
               to={''}
               title="Delete plant"
               style={{ textDecoration: 'none', color: '#1e1e1e' }}
               onClick={() => {
                  let save = async () => {
                     try
                     {
                        await plantRoutes.delete(plant['id'], 'a.damico');
               
                        await fetchData();
               
                        setAlert(null);
                     }
                     catch(error)
                     {
                        if(mounted.current)
                        {
                           console.log(error);
                           setAlert(null);
                        }
                     }
                  }
   
                  if(mounted.current)
                  {
                     setAlert(
                        <Alert
                           title="Delete plant?"
                           closeText="Cancel"
                           saveText="Confirm"
                           open={true}
                           onClose={() => setAlert(null)}
                           onSave={save}
                           style={{minWidth: '33rem'}}
                        >
                           <p>Would you like to delete the plant?<br/>Please note, that once you delete the plant, you cannot undo the change without contacting support.</p>
                           <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={2}><label className='labels alert-label'>Name</label></Grid2>
                                 <Grid2 xs>{plant['name']}{plant['number'] ? ' (' + plant['number'] + ')' : ''}</Grid2>
                              </Grid2>
                           </Stack>
                        </Alert>
                     );
                  }
               }}
            >
               <DeleteIcon />
            </Link>
         </React.Fragment>
      )
   }

   const fetchData = async () => {
      let data = [];
      
      try
      {
         data = await plantRoutes.fetchPlantsWithDetails();

         setPlants(formatAll(data));
         setLoading(false);
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
      }
   };

   useEffect(() => {
      fetchData();
   }, []);

   return (
      <React.Fragment>
         {(loading) ?
            <Grid2 justifyContent="center" alignItems="center" style={{height: '30rem'}}>
               <Grid2  xs={5}>
                  <CircularProgress />
               </Grid2>
            </Grid2>
         : <React.Fragment>
            {(alert) ? alert : null}
            <Grid2 container justifyContent="flex-end">
               <Link to={'/plant/edit/'} style={{ textDecoration: 'none' }}><Button>Add Plant</Button></Link>
            </Grid2>
            <Grid2 container columnSpacing={{ xs: 0, md: 1 }}>
               {plants.map((plant: any) => 
                  <Grid2 key={'plant-' + plant['id']} xs={12} md>
                     <Card sx={{ maxWidth: 375 }} raised={true}>
                        <CardHeader title ={
                           <Link to={'/plant/' + plant['id']} state={{plantId: plant['id']}}>{plant['number'] ? plant['name'] + ' (' + plant['number'] + ')' : plant['name']}</Link>}
                           />
                        <CardContent>
                           <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" minWidth="100%">
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Montiored</label></Grid2>
                                 <Grid2 xs={7}>{plant?.monitor === 1 ? <CheckIcon style={{color: '#7db856'}}/> : <BlockIcon style={{color: '#e3272b'}}/>}</Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Last Watered</label></Grid2>
                                 <Grid2 xs={7}></Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Last Updated By</label></Grid2>
                                 <Grid2 xs={7}>{plant?.updateBy}</Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={4}><label className='labels'>Last Updated</label></Grid2>
                                 <Grid2 xs={8}>{plant?.updateTs !== '' ? moment.utc(plant?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
                              </Grid2>
                           </Stack>
                        </CardContent>
                        <CardActions>
                           {actions(plant)}
                        </CardActions>
                     </Card>
                  </Grid2>
               )}
            </Grid2>
         </React.Fragment>
         }
      </React.Fragment>
   );
}

export default Home;