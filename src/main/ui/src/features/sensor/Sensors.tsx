import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Card, CardHeader, CardContent, CardActions,
   CircularProgress, Stack } from '@mui/material';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import Button from '@mui/joy/Button';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import _ from 'lodash';
import moment from 'moment-timezone';
import { sensorRoutes } from '../../service/ApiService';
import { formatAll } from '../../service/utils/sensorFormat';

function Sensors() {
   const [sensors, setSensors] = useState<any[]>([]);
   const [loading, setLoading] = useState(true);

   const actions = (sensor: any) => {
      return(
         <React.Fragment>
            <Link key={"edit-" + sensor['id']}
               style={{ textDecoration: 'none', color: '#1e1e1e' }}
               to={'/sensor/edit/' + sensor['id']}
               state={{sensorId: sensor['id']}}
               title="Edit sensor"
            >
               <EditIcon />
            </Link>
            <Link key={'delete-' + sensor['id']}
               to={''}
               title="Delete sensor"
               style={{ textDecoration: 'none', color: '#1e1e1e' }}
               // onClick={() => {
               //    let save = async () => {
               //       try
               //       {
               //          await plantRoutes.delete(row['id'], 'a.damico');
               
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
               //                   <Grid2 xs>{row['name']}{row['number'] ? ' (' + row['number'] + ')' : ''}</Grid2>
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
         data = await sensorRoutes.fetchSensorsWithDetails();

         setSensors(formatAll(data));
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
               <Grid2 container justifyContent="flex-end">
                  <Link to={'/sensor/edit/'} style={{ textDecoration: 'none' }}><Button>Add Sensor</Button></Link>
               </Grid2>
               <Grid2 container spacing={{ xs: 0, md: 1 }}>
               {sensors.map((sensor: any) => 
                  <Grid2 key={'sensor-' + sensor['id']} xs={12} sm={4} md={2}>
                     <Card sx={{ maxWidth: 375 }} raised={true}>
                        <CardHeader title ={<Link to={'/sensor/' + sensor['id']} state={{sensorId: sensor['id']}}>{sensor?.sensorType['type'] + ' (' + sensor['number'] + ')'}</Link>}
                           />
                        <CardContent>
                           <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" minWidth="100%">
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>In use</label></Grid2>
                                 <Grid2 xs={7}>{!_.isEmpty(sensor['plant']) || !_.isEmpty(sensor['environment']) ? <CheckIcon style={{color: '#7db856'}}/> 
                                                :
                                                sensor.sensorType['arduino'] !== 'w' ? <BlockIcon style={{color: '#e3272b'}}/>
                                                :
                                                'N/A'
                                                }
                                 </Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Used by</label></Grid2>
                                 <Grid2 xs={7}>{sensor['plant'] ? <Link to={'/plant/' + sensor['plant']['id']} state={{plantId: sensor['plant']['id']}}>{sensor['plant']['number'] ? sensor['plant']['name'] + ' (' + sensor['plant']['number'] + ')' : sensor['plant']['name']}</Link>
                                                :
                                                sensor['environment'] ? <Link to={'/environment/' + sensor['environment']['id']} state={{environmentId: sensor['environment']['id']}}>{sensor['environment']['name']}</Link>
                                                :
                                                sensor['zoneId'] ? 'Zone ' + sensor['zoneId']
                                                :
                                                null}
                                 </Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={5}><label className='labels'>Last Updated By</label></Grid2>
                                 <Grid2 xs={7}>{sensor?.updateBy}</Grid2>
                              </Grid2>
                              <Grid2 xs={12} justifyContent="space-between" alignItems="flex-start" display="inline-flex">
                                 <Grid2 xs={4}><label className='labels'>Last Updated</label></Grid2>
                                 <Grid2 xs={8}>{sensor?.updateTs !== '' ? moment.utc(sensor?.updateTs).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A zz') : ''}</Grid2>
                              </Grid2>
                           </Stack>
                        </CardContent>
                        <CardActions>
                           {actions(sensor)}
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

export default Sensors;