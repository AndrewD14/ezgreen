import React, { useState, useReducer, useEffect} from 'react';
import { CircularProgress, Checkbox,
         Fab, FormControl, FormControlLabel, FormLabel,
         MenuItem, Select, Stack, TextField } from '@mui/material';
import { DesktopDatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Link, useLocation } from 'react-router-dom';
import { plantRoutes } from '../../service/ApiService';
import { formatOne } from '../../service/utils/plantFormat';
import moment from 'moment-timezone';
import _ from 'lodash';

const initialState: any = {
   dateObtain: null,
   dead: 0,
   delete: 0,
   highMoisture: 0,
   id: null,
   lowMoisture: 0,
   monitor: 0,
   name: '',
   number: '',
   potSizeId: '',
   sensorId: ''
}

function reducer(state: any, action: any)
{
   switch(action.type)
   {
      case 'setup': return {...state, ...action.payload}
      case 'setName': return {...state, name: action.payload};
      case 'setNumber': return {...state, number: action.payload};
      case 'setLowMoisture': return {...state, lowMoisture: action.payload};
      case 'setHighMoisture': return {...state, highMoisture: action.payload};
      case 'setMonitor': return {...state, monitor: action.payload};
      case 'setPotSizeId': return {...state, potSizeId: action.payload};
      case 'setSensorId': return {...state, sensorId: action.payload};
      case 'setDateObtain': return {...state, dateObtain: action.payload};
      default: throw new Error(action.type + " is not supported.");
   }
}

function EditPlant(props: any) {
   const [plant, setPlant] = useReducer(reducer, initialState);
   const [initPlant, setInitPlant] = useState<any>(initialState);
   const [options, setOptions] = useState<any>({});
   const [loading, setLoading] = useState(true);
   const [errors, setError] = useState<any[]>([]);
   const [pageError, setPageError] = useState<string>("");
   const location: any = useLocation();

   let id = location.state?.plantId || null;

   const onChange = (event: any) => {
      console.log(event)
      setPlant({
         type: event.target.id,
         payload: event.target.value
      });
   }

   const onMonitor = (event: any) => {
      setPlant({
         type: event.target.id,
         payload: event.target.checked ? 1 : 0
      });
   }

   const onDateChange = (event: any) => {
      setPlant({
         type: 'setDateObtain',
         payload: event
      });
   }

   const undo = () => {
      setPlant({
         type: 'setup',
         payload: {
            ...initPlant
         }
      });

      setError([]);
      setPageError("");
   }

   const fetchData = async () => {
      let data = {};
      let edit = {};

      try
      {
         data = await plantRoutes.fetchPlantOptions();

         if(id != null)
         {
            edit = formatOne(await plantRoutes.fetchOnePlantWithDetails(id));

            setInitPlant({...initialState, ...edit});
            setPlant({
               type: 'setup',
               payload: {
                  ...edit
               }
            });
         }
         else
         {
            edit = {...initPlant};
         }

         setOptions(data);
         setLoading(false);
         setPageError("");
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
         setPageError("There was an error retrieving data.");
      }
   };

   useEffect(() => {
      fetchData();
   },
   //eslint-disable-next-line
   []);

   return (
      <React.Fragment>
            <h1>{(id) ? "Edit Plant" : "Create plant"}</h1>
            {(pageError) ? <div className="error-message">{pageError}</div> : null}
            {(loading) ?
               <Grid2 justifyContent="center" alignItems="center" style={{height: '30rem'}}>
                  <Grid2 xs={5}>
                     <CircularProgress />
                  </Grid2>
               </Grid2>
            :
               <React.Fragment>
                  <h2>Plant info</h2>
                  <Grid2 container xl={6}>
                     <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
                        <FormControl key={'name'}>
                           <FormLabel required>Plant name</FormLabel>
                           <TextField
                              id="setName"
                              value={plant.name}
                              onChange={onChange}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl key={'number'}>
                           <FormLabel>Number</FormLabel>
                           <TextField
                              id="setNumber"
                              type="number"
                              value={plant.number}
                              onChange={onChange}
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl key={'potSize'}>
                           <FormLabel required>Pot size</FormLabel>
                           <Select
                              id="setPotSizeId"
                              onChange={(event: any) => setPlant({type: 'setPotSizeId', payload: event.target.value})}
                              value={plant.potSizeId}
                           >
                              {options.potSizes?.map((potSize: any) => <MenuItem key={'potSize-' + potSize.id} value={potSize.id}>{potSize.size}</MenuItem>)}
                           </Select>
                        </FormControl>
                        <FormControl key={'sensor'}>
                           <FormLabel required>Sensor</FormLabel>
                           <Select
                              id="setSensorId"
                              onChange={(event: any) => setPlant({type: 'setSensorId', payload: event.target.value})}
                              value={plant.sensorId}
                           >
                              {(id != null && initPlant?.sensor ) ? <MenuItem key={'sensor-' + initPlant?.sensor?.id} value={initPlant?.sensor?.id}>{initPlant?.sensor?.id + ' port: ' + initPlant?.sensor?.port + ' board: ' + initPlant?.sensor?.board}</MenuItem> : null}
                              {options.sensors?.map((sensor: any) => <MenuItem key={'sensor-' + sensor.id} value={sensor.id}>{sensor.id + ' port: ' + sensor.port + ' board: ' + sensor.board}</MenuItem>)}
                           </Select>
                        </FormControl>
                        <FormControl key={'dateObtain'}>
                           <FormLabel>Date obtained</FormLabel>
                           <LocalizationProvider dateAdapter={AdapterMoment} adapterLocale="en">
                              <DesktopDatePicker
                                 value={plant.dateObtain ? moment(plant.dateObtain) : null}
                                 onChange={onDateChange}
                              />
                           </LocalizationProvider>
                        </FormControl>
                        <FormControl key={'lowMoisture'}>
                           <FormLabel required>Low moisture (%)</FormLabel>
                           <TextField
                              id="setLowMoisture"
                              type="number"
                              onChange={onChange}
                              value={plant.lowMoisture}
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl key={'highMoisture'}>
                           <FormLabel required>High moisture (%)</FormLabel>
                           <TextField
                              id="setHighMoisture"
                              type="number"
                              value={plant.highMoisture}
                              onChange={onChange}
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl key={'monitor'}>
                           <FormLabel required>Monitor</FormLabel>
                           <Checkbox
                              id='setMonitor'
                              checked={(plant.monitor === 1) ? true : false}
                              onChange={onMonitor}
                              inputProps={{ 'aria-label': 'controlled' }}/>
                        </FormControl>
                     </Stack>
                  </Grid2>
                  <Grid2 className="edit-rule-buttons" justifyContent="center">
                     <Grid2 container direction="row" className="padding1rem">
                        <Link to={{pathname:`/`}} style={{ textDecoration: 'none' }}>
                           <Fab variant='extended' value="178">Cancel</Fab>
                        </Link>
                        <Grid2 style={{ flex: 1 }}>
                           <Fab variant='extended' color="primary" onClick={undo}>Undo</Fab>
                        </Grid2>
                        <Link to={{pathname: ''}} style={{ textDecoration: 'none' }} /*onClick={}*/>
                           <Fab variant='extended' color="primary">Save</Fab>
                        </Link>
                     </Grid2>
                  </Grid2>
               </React.Fragment>
            }
      </React.Fragment>
   );
}

export default EditPlant;