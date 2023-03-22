import React, { useState, useReducer, useEffect} from 'react';
import { CircularProgress, Checkbox,
         Fab, FormControl, FormLabel,
         MenuItem, Select, Stack, TextField } from '@mui/material';
import { DesktopDatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Link, useLocation } from 'react-router-dom';
import { plantRoutes } from '../../service/ApiService';
import { formatOne } from '../../service/utils/plantFormat';
import moment from 'moment-timezone';

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

   const save = async () => {
      try
      {
         let newErrors: any[] = [];

         if(plant.name === '') newErrors.push("name");
         if(plant.potSizeId === '') newErrors.push("pot");
         if(parseFloat(plant.lowMoisture) === 0 || parseFloat(plant.highMoisture) <= parseFloat(plant.lowMoisture)) newErrors.push("low");
         if(parseFloat(plant.highMoisture) <= parseFloat(plant.lowMoisture)) newErrors.push("high");
         

         setError(newErrors);
         setPageError("");

         console.log(newErrors);
         console.log(plant)

         if(newErrors.length > 0) return;

         // await Api.thresholds.save(group, insurance, state.queue.cccQueueName, state.customScore, state.advisorScore, state.guidelineScore, state.insuranceScore, user.username, cancelToken, state.id);

         // history.push("/thresholds");
      }
      catch(error)
      {
         setPageError("Failed to update database.");
         console.log(error);
      }
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
         <Grid2 container direction="column" justifyContent="flex-start" alignItems="flex-start" style={{minHeight: '100%'}}>
            <Grid2 xs={2}>
               <h1>{(id) ? "Edit Plant" : "Create plant"}</h1>
            </Grid2>
            <Grid2 xs={3}>
               {(pageError) ? <div className="error-message">{pageError}</div> : null}
            </Grid2>
            {(loading) ?
               <Grid2 justifyContent="center" alignItems="center" style={{height: '30rem'}}>
                  <Grid2 xs={5}>
                     <CircularProgress />
                  </Grid2>
               </Grid2>
            :
               <React.Fragment>
                  <Grid2 xs={2}>
                     <h2>Plant info</h2>
                  </Grid2>
                  <Grid2 xs container direction="column" justifyContent="flex-end" alignItems="flex-start" style={{width: '100%'}}>
                     <Grid2 xs>
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
                           <Grid2 container className="error-text">
                              {(errors.indexOf("name") !== -1) ? <span>You enter a name for the plant.</span> : null}
                           </Grid2>
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
                           <Grid2 container className="error-text">
                              {(errors.indexOf("pot") !== -1) ? <span>You must select a pot size.</span> : null}
                           </Grid2>
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
                           <Grid2 container className="error-text">
                              {(errors.indexOf("low") !== -1) ? <span>You must enter a value greater than 0, but less than the high moisture.</span> : null}
                           </Grid2>
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
                           <Grid2 container className="error-text">
                              {(errors.indexOf("high") !== -1) ? <span>You must enter a value greater than low moisture value.</span> : null}
                           </Grid2>
                           <FormControl key={'monitor'}>
                              <Grid2 display="flex" justifyContent="flex-start" alignItems="center">
                                 <FormLabel>Monitor</FormLabel>
                                 <Checkbox
                                    id='setMonitor'
                                    checked={(plant.monitor === 1) ? true : false}
                                    onChange={onMonitor}
                                    inputProps={{ 'aria-label': 'controlled' }}/>
                              </Grid2>
                           </FormControl>
                        </Stack>
                     </Grid2>
                     <Grid2 xs={3} style={{width: '100%'}}>
                        <Grid2 justifyContent="center">
                           <Grid2 container direction="row" className="error-text">
                              <Link to={{pathname:`/`}} style={{ textDecoration: 'none' }}>
                                 <Fab variant='extended' value="178">Cancel</Fab>
                              </Link>
                              <Grid2 style={{ flex: 1 }}>
                                 <Fab variant='extended' color="primary" onClick={undo}>Undo</Fab>
                              </Grid2>
                              <Link to={{pathname: ''}} style={{ textDecoration: 'none' }} onClick={save}>
                                 <Fab variant='extended' color="primary">Save</Fab>
                              </Link>
                           </Grid2>
                        </Grid2>
                     </Grid2>
                  </Grid2>
               </React.Fragment>
            }
         </Grid2>
            
      </React.Fragment>
   );
}

export default EditPlant;