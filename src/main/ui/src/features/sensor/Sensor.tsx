import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import Button from '@mui/joy/Button';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { CircularProgress } from '@mui/material';
import _ from 'lodash';
import moment from 'moment-timezone';
import MyTable from '../common/table/MyTable';
import { sensorRoutes } from '../../service/ApiService';
import { formatAll } from '../../service/utils/sensorFormat';

interface Column {
   id: 'type' | 'board' | 'port' | 'inUse' | 'lowCalibration' | 'highCalibration' | 'usedBy' | 'updateBy' | 'updateTs' | 'actions';
   label: string;
   minWidth?: number;
   align?: 'right';
   format?: (value: any, id: any) => any;
}

const columns: readonly Column[] = [
   {
      id: 'type',
      label: 'Type',
      minWidth: 170,
      format: (row, id) => <Link to={'/sensor/' + row['id']} state={{sensorId: row['id']}}>{row['type']}</Link>
   },
   { id: 'board', label: 'Board', minWidth: 100, align: 'right', },
   { id: 'port', label: 'Port', minWidth: 170, align: 'right', },
   {
      id: 'inUse',
      label: 'In use',
      minWidth: 170,
      align: 'right', 
      format: (row, id) => !_.isEmpty(row['plant']) || !_.isEmpty('environment') ? <CheckIcon style={{color: '#7db856'}}/> : <BlockIcon style={{color: '#e3272b'}}/>
   },
   {
      id: 'lowCalibration',
      label: 'Low Calibration',
      minWidth: 170,
      align: 'right',
      format: (row, id) => !isNaN(row[id]) ? parseFloat(row[id]).toFixed(2) : ''
   },
   {
      id: 'highCalibration',
      label: 'High Calibration',
      minWidth: 170,
      align: 'right',
      format: (row, id) => !isNaN(row[id]) ? parseFloat(row[id]).toFixed(2) : ''
   },
   {
      id: 'usedBy',
      label: 'Used by',
      minWidth: 170,
      format: (row, id) => row['plant'] ? <Link to={'/plant/' + row['plant']['id']} state={{plantId: row['plant']['id']}}>{row['plant']['number'] ? row['plant']['name'] + ' (' + row['plant']['number'] + ')' : row['plant']['name']}</Link>
                           :
                           row['environment'] ? <Link to={'/environment/' + row['environment']['id']} state={{environmentId: row['environment']['id']}}>{row['environment']['location']}</Link>
                           :
                           null
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
               <Link key={"edit-" + row['id']}
                  style={{ textDecoration: 'none', color: '#1e1e1e' }}
                  to={'/sensor/edit/' + row['id']}
                  state={{sensorId: row['id']}}
                  title="Edit sensor"
               >
                  <EditIcon />
               </Link>
               <Link key={'delete-' + row['id']}
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
   },
];

function Sensors() {
   const [sensors, setSensors] = useState<any[]>([]);
   const [loading, setLoading] = useState(true);

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
               <MyTable value={sensors} columns={columns} />
            </React.Fragment>
         }
      </React.Fragment>
   );
}

export default Sensors;