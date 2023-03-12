import React, { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import moment from 'moment-timezone';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import FormatColorResetIcon from '@mui/icons-material/FormatColorReset';
import ShowerIcon from '@mui/icons-material/Shower';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { CircularProgress, Stack } from '@mui/material';
import MyTable from '../common/table/MyTable';
import Alert from '../common/alert/Alert';
import { plantRoutes } from '../../service/ApiService';
import { formatAll } from '../../service/utils/plantformat';

interface Column {
   id: 'name' | 'size' | 'type' | 'dateObtain' | 'highMoisture' | 'lowMoisture' | 'monitor' | 'dead' | 'updateBy' | 'updateTs' | 'actions';
   label: string;
   minWidth?: number;
   align?: 'right';
   format?: (value: any, id: any) => any;
}

function Home() {
   const [plants, setPlants] = useState<any[]>([]);
   const [alert, setAlert] = useState<any>(null);
   const [loading, setLoading] = useState(true);
   const mounted = useRef(true);

   const columns: readonly Column[] = [
      { 
         id: 'name',
         label: 'Name',
         minWidth: 170,
         format: (row, id) => <Link to={'/plant/' + row['id']} state={{plantId: row['id']}}>{row['number'] ? row[id] + ' (' + row['number'] + ')' : row[id]}</Link>,
      },
      {
         id: 'size',
         label: 'Pot size',
         minWidth: 100,
         format: (row, id) => row.potSize[id]
      },
      {
         id: 'type',
         label: 'Sensor type',
         minWidth: 170,
         format: (row, id) => row.sensor?.id
      },
      {
         id: 'dateObtain',
         label: 'Date Obtain',
         minWidth: 170,
      },
      {
         id: 'highMoisture',
         label: 'High Moisture (%)',
         minWidth: 170,
         align: 'right',
      },
      {
         id: 'lowMoisture',
         label: 'Low Moisture (%)',
         minWidth: 170,
         align: 'right',
      },
      {
         id: 'monitor',
         label: 'Monitored',
         minWidth: 170,
         format: (row, id) => row[id] === 1 ? <CheckIcon style={{color: '#7db856'}}/> : <BlockIcon style={{color: '#e3272b'}}/>
      },
      {
         id: 'dead',
         label: 'Dead',
         minWidth: 170,
         format: (row, id) => row[id] === 1 ? <CheckIcon style={{color: '#7db856'}}/> : <BlockIcon style={{color: '#e3272b'}}/>,
      },
      {
         id: 'updateBy',
         label: 'Updated By',
         minWidth: 170,
      },
      {
         id: 'updateTs',
         label: 'Updated (' + Intl.DateTimeFormat().resolvedOptions().timeZone + ')',
         minWidth: 170,
         align: 'right',
         format: (row, id) => moment.utc(row[id]).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A')
      },
      {
         id: 'actions',
         label: '',
         minWidth: 170,
         align: 'right',
         format: (row, id) => {
            return(
               <React.Fragment>
                  <Link key={"monitor-" + row['id']}
                     style={{ textDecoration: 'none', color: '#1e1e1e', pointerEvents: row['dead'] === 1 ? 'none' : 'auto'}}
                     to={''}
                     title="Monitor/Disable plant"
                     onClick={() => {
                        let save = async () => {
                           try
                           {
                              row['monitor'] === 1 ? await plantRoutes.deactivate(row['id'], 'a.damico')
                              :
                              await plantRoutes.activate(row['id'], 'a.damico')
                              
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
                                 title={row['monitor'] === 1 ? "Deactivate monitoring?" : 'Acttivate monitoring?' }
                                 closeText="Cancel"
                                 saveText="Confirm"
                                 open={true}
                                 onClose={() => setAlert(null)}
                                 onSave={save}
                                 style={{minWidth: '33rem'}}
                              >
                                 <p>Would you like to {row['monitor'] === 1 ? 'deactivate' : 'activate'} the monitoring for {row['name']}{row['number'] ? ' (' + row['number'] + ')' : ''}?</p>
                              </Alert>
                           );
                        }
                     }}
                  >
                     { row['monitor'] === 1 ? <FormatColorResetIcon className={row['dead'] === 1 ? 'disabled-link ' : ''}/> : <ShowerIcon className={row['dead'] === 1 ? 'disabled-link ' : ''}/> }
                  </Link>
                  <Link key={"edit-" + row['id']}
                     style={{ textDecoration: 'none', color: '#1e1e1e' }}
                     to={'/plant/edit/' + row['id']}
                     state={{plantId: row['id']}}
                     title="Edit plant"
                  >
                     <EditIcon />
                  </Link>
                  <Link key={'delete-' + row['id']}
                     to={''}
                     title="Delete plant"
                     style={{ textDecoration: 'none', color: '#1e1e1e' }}
                     onClick={() => {
                        let save = async () => {
                           try
                           {
                              await plantRoutes.delete(row['id'], 'a.damico');
                     
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
                                       <Grid2 xs>{row['name']}{row['number'] ? ' (' + row['number'] + ')' : ''}</Grid2>
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
      },
   
   ];

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
            <MyTable value={plants} columns={columns} />
         </React.Fragment>
         }
      </React.Fragment>
   );
}

export default Home;