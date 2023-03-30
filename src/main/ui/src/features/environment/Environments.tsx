import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardContent, CardActions,
   CircularProgress, Stack } from '@mui/material';
import Button from '@mui/joy/Button';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { Link } from 'react-router-dom';
import moment from 'moment-timezone';
import { environmentRoutes } from '../../service/ApiService';
import { formatAll } from '../../service/utils/environmentFormat';

function Environment()
{
   const [environments, setEnvironments] = useState([]);
   const [alert, setAlert] = useState<any>(null);
   const [loading, setLoading] = useState(true);

   const actions = (environment: any) => {
      return(
         <React.Fragment>
            <Link key={"edit-" + environment['id']}
               style={{ textDecoration: 'none', color: '#1e1e1e' }}
               to={'/environment/edit/' + environment['id']}
               state={{environmentId: environment['id']}}
               title="Edit environment"
            >
               <EditIcon />
            </Link>
            <Link key={'delete-' + environment['id']}
               to={''}
               title="Delete environment"
               style={{ textDecoration: 'none', color: '#1e1e1e' }}
               // onClick={() => {
               //    let save = async () => {
               //       try
               //       {
               //          await plantRoutes.delete(plant['id'], 'a.damico');
               
               //          await fetchData();
               
               //          setAlert(null);
               //       }
               //       catch(error)
               //       {
               //          if(mounted.current)
               //          {
               //             console.log(error);
               //             setAlert(null);
               //          }
               //       }
               //    }
   
               //    if(mounted.current)
               //    {
               //       setAlert(
               //          <Alert
               //             title="Delete plant?"
               //             closeText="Cancel"
               //             saveText="Confirm"
               //             open={true}
               //             onClose={() => setAlert(null)}
               //             onSave={save}
               //             style={{minWidth: '33rem'}}
               //          >
               //             <p>Would you like to delete the plant?<br/>Please note, that once you delete the plant, you cannot undo the change without contacting support.</p>
               //             <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
               //                <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
               //                   <Grid2 xs={2}><label className='labels alert-label'>Name</label></Grid2>
               //                   <Grid2 xs>{plant['name']}{plant['number'] ? ' (' + plant['number'] + ')' : ''}</Grid2>
               //                </Grid2>
               //             </Stack>
               //          </Alert>
               //       );
               //    }
               // }}
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
         data = formatAll(await environmentRoutes.fetchEnvironmentsWithDetail());

         setEnvironments(data);
         setLoading(false);
         console.log(data);
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
      }
   };

   useEffect(
      () => {
         fetchData();
      },
      []
   );

   return (
      <React.Fragment>
         {(loading) ?
            <Grid2 justifyContent="center" alignItems="center" style={{height: '30rem'}}>
               <Grid2  xs={5}>
                  <CircularProgress />
               </Grid2>
            </Grid2>
         : 
         <React.Fragment>
            {(alert) ? alert : null}
            <Grid2 container justifyContent="flex-end">
               <Link to={'/environment/edit/'} style={{ textDecoration: 'none' }}><Button>Add Environment</Button></Link>
            </Grid2>
            <Grid2 container columnSpacing={{ xs: 0, md: 1 }}>
               {environments.map((environment: any) => 
                  <Grid2 key={'environment-' + environment['id']} xs={12} md>
                     <Card sx={{ maxWidth: 375 }} raised={true}>
                        <CardHeader title ={
                           <Link to={'/environment/' + environment['id']} state={{environmentId: environment['id']}}>{environment['location'] }</Link>}
                           />
                        <CardContent>
                           <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" minWidth="100%">
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Sensor</label></Grid2>
                                 <Grid2 xs={7}>{environment?.sensor ? <Link to={'/sensor/' + environment['sensor']['id']} state={{sensorId: environment['sensor']['id']}}>{environment['sensor']['type']}</Link> : null}</Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Last Watered</label></Grid2>
                                 <Grid2 xs={7}></Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Last Updated By</label></Grid2>
                                 <Grid2 xs={7}>{environment?.updateBy}</Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={4}><label className='labels'>Last Updated</label></Grid2>
                                 <Grid2 xs={8}>{environment?.updateTs !== '' ? moment.utc(environment?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
                              </Grid2>
                           </Stack>
                        </CardContent>
                        <CardActions>
                           {actions(environment)}
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

export default Environment;