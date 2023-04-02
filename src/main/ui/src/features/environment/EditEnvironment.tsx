import React, { useState, useReducer, useEffect} from 'react';
import { Button, Card, CardHeader, CircularProgress, Checkbox,
         Divider, Fab, FormControl, FormLabel, List, ListItem,
         ListItemText, ListItemIcon, MenuItem, Select, Stack,
         TextField } from '@mui/material';
import { TimePicker , LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import _ from 'lodash';
import { environmentRoutes } from '../../service/ApiService';
import { formatOne, formatOptions } from '../../service/utils/environmentFormat';

const initialState: any = {
   id: null,
   name: '',
   sensors: [],
   relays: [],
   sensorTypeId: '',
   highDesire: 0,
   lowDesire: 0,
   target: 0,
   humidity: 0,
   timeStart: null,
   timeEnd: null
}

function reducer(state: any, action: any)
{
   switch(action.type)
   {
      case 'setup': return {...state, ...action.payload}
      case 'setName': return {...state, name: action.payload};
      case 'setSensors': return {...state, sensors: [...action.payload]};
      case 'setRelays': return {...state, relays: [...action.payload]};
      case 'setSensorType': return {...state, sensorTypeId: action.payload};
      case 'setHighDesire': return {...state, highDesire: action.payload};
      case 'setLowDesire': return {...state, lowDesire: action.payload};
      case 'setTarget': return {...state, target: action.payload};
      case 'setHumidity': return {...state, humidity: action.payload};
      case 'setTimeStart': return {...state, timeStart: action.payload};
      case 'setTimeEnd': return {...state, timeEnd: action.payload};
      default: throw new Error(action.type + " is not supported.");
   }
}

function not(a: any[], b: any[]) {
   return a.filter((value) => b.indexOf(value) === -1);
}

function intersection(a: any[], b: any[]) {
   return a.filter((value) => b.indexOf(value) !== -1);
}

function union(a: any[], b: any[]) {
   return [...a, ...not(b, a)];
}

function EditEnvironment(props: any) {
   const [environment, setEnvironment] = useReducer(reducer, initialState);
   const [initEnvironment, setInitEnvironment] = useState<any>(initialState);
   const [options, setOptions] = useState<any>({});
   const [checkedSensor, setSensorChecked] = React.useState<any[]>([]);
   const [leftSensor, setSensorLeft] = React.useState<any[]>([]);
   const [checkedRelay, setRelayChecked] = React.useState<any[]>([]);
   const [leftRelay, setRelayLeft] = React.useState<any[]>([]);
   const [loading, setLoading] = useState(true);
   const [errors, setError] = useState<any[]>([]);
   const [pageError, setPageError] = useState<string>("");
   const [saving, setSaving] = useState<boolean>(false);
   const location: any = useLocation();
   const navigate: any = useNavigate();   

   const leftSensorChecked = intersection(checkedSensor, leftSensor);
   const rightSensorChecked = intersection(checkedSensor, environment.sensors);
   const leftRelayChecked = intersection(checkedRelay, leftRelay);
   const rightRelayChecked = intersection(checkedRelay, environment.relays);

   const sensorToRelay: any = {
      '1': ['W'],
      '2': ['T', 'F', 'H'],
      '3': ['L']
   };

   const validSensorStypes: number[] = [2, 3];

   const handleSensorToggle = (value: any) => () => {
      const currentIndex = checkedSensor.indexOf(value);
      const newChecked = [...checkedSensor];

      if (currentIndex === -1) {
         newChecked.push(value);
      } else {
         newChecked.splice(currentIndex, 1);
      }

      setSensorChecked(newChecked);
   };

   const handleRelayToggle = (value: any) => () => {
      const currentIndex = checkedRelay.indexOf(value);
      const newChecked = [...checkedRelay];

      if (currentIndex === -1) {
         newChecked.push(value);
      } else {
         newChecked.splice(currentIndex, 1);
      }

      setRelayChecked(newChecked);
   };

   const numberOfCheckedSensor = (items: any[]) => intersection(checkedSensor, items).length;
   const numberOfCheckedRelay = (items: any[]) => intersection(checkedRelay, items).length;

   const handleToggleAllSensor = (items: any[]) => () => {
      if (numberOfCheckedSensor(items) === items.length) setSensorChecked(not(checkedSensor, items));
      else setSensorChecked(union(checkedSensor, items));
   };

   const handleToggleAllRelay = (items: any[]) => () => {
      if (numberOfCheckedRelay(items) === items.length) setRelayChecked(not(checkedRelay, items));
      else setRelayChecked(union(checkedRelay, items));
   };

   const handleSensorCheckedRight = () => {
      let left: any[] = not(leftSensor, leftSensorChecked);
      let right: any[] = environment.sensors.concat(leftSensorChecked);

      left.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);

         return 0;
      });

      right.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);
         
         return 0;
      });

      setEnvironment({
         type: 'setSensors',
         payload: right
      });
      setSensorLeft(left);
      setSensorChecked(not(checkedSensor, leftSensorChecked));
   };

   const handleSensorCheckedLeft = () => {
      let left: any[] = leftSensor.concat(rightSensorChecked);
      let right: any[] = not(environment.sensors, rightSensorChecked);

      left.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);

         return 0;
      });

      right.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);
         
         return 0;
      });

      setSensorLeft(left);
      setEnvironment({
         type: 'setSensors',
         payload: right
      });
      setSensorChecked(not(checkedSensor, rightSensorChecked));
   };

   const handleRelayCheckedRight = () => {
      let left: any[] = not(leftRelay, leftRelayChecked);
      let right: any[] = environment.relays.concat(leftRelayChecked);

      left.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);

         return 0;
      });

      right.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);
         
         return 0;
      });

      setEnvironment({
         type: 'setRelays',
         payload: right
      });
      setRelayLeft(left);
      setRelayChecked(not(checkedRelay, leftRelayChecked));
   };

   const handleRelayCheckedLeft = () => {
      let left: any[] = leftRelay.concat(rightRelayChecked);
      let right: any[] = not(environment.relays, rightRelayChecked);

      left.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);

         return 0;
      });

      right.sort((a: any, b: any) => {
         if(a.type.type < b.type.type) return -1;
         if(a.type.type > b.type.type) return 1;
         if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);
         
         return 0;
      });

      setRelayLeft(left);
      setEnvironment({
         type: 'setRelays',
         payload: right
      });
      setRelayChecked(not(checkedRelay, rightRelayChecked));
   };

   const filterLeftSensors = () => {
      let available: any[] = [];
      
      if(options?.sensors !== undefined)
      {
         //eslint-disable-next-line
         available = options.sensors.filter((sensor: any) => sensor.typeId == environment.sensorTypeId)
                                    .filter((sensor: any) => sensor.environmentId === undefined);
      }

      setSensorLeft(available);
   };

   const filterRightSensors = () => {      
      let selected: any[] = [];
      
      if(options?.sensors !== undefined)
      {
         //eslint-disable-next-line
         selected = options.sensors.filter((sensor: any) => sensor.typeId == environment.sensorTypeId)
                                    .filter((sensor: any) => sensor.environmentId === environment.id);
      }

      setEnvironment({
         type: 'setSensors',
         payload: selected
      });
   };

   const filterLeftRelays = () => {      
      let available: any[] = [];
      
      if(options?.relays !== undefined)
      {
         available = options.relays.filter((relay: any) => sensorToRelay[environment.sensorTypeId].indexOf(relay?.type.arduino) >= 0)
                                    .filter((relay: any) => relay.environmentId === undefined);
      }

      setRelayLeft(available);
   };

   const filterRightRelays = () => {      
      let selected: any[] = [];

      if(options?.relays !== undefined)
      {
         selected = options.relays.filter((relay: any) => sensorToRelay[environment.sensorTypeId].indexOf(relay.type.arduino) >= 0)
                                  .filter((relay: any) => relay.environmentId === environment.id);
      }

         setEnvironment({
            type: 'setRelays',
            payload: selected
         });
   };

   //update sensor and relay list for the transfer list component
   useEffect(() => {
      filterLeftSensors();
      filterRightSensors();
      filterLeftRelays();
      filterRightRelays();
   },
   [environment.sensorType]);

   const onChange = (event: any) => {

      setEnvironment({
         type: event.target.id,
         payload: event.target.value
      });
   };

   const onSensorTypeChange = (event: any) => {
      let value = event.target.value;

      setEnvironment({type: 'setSensorType', payload: '' + value});
      setError([]);
      setPageError("");

      if(value !== 2)
      {
         setEnvironment({
            type: 'setHighDesire',
            payload: 0
         });

         setEnvironment({
            type: 'setLowDesire',
            payload: 0
         });

         setEnvironment({
            type: 'setHumidity',
            payload: 0
         });
      }
      else if(value !== 3)
      {
         setEnvironment({
            type: 'setTimeStart',
            payload: null
         });

         setEnvironment({
            type: 'setTimeEnd',
            payload: null
         });
      }
   };

   const onTimeChange = (type: string, event: any) => {
      setEnvironment({
         type: type,
         payload: event
      });
   };

   const undo = () => {
      setEnvironment({
         type: 'setup',
         payload: {
            ...initEnvironment,
         }
      });

      setError([]);
      setPageError("");
   };

   const save = async () => {
      if(saving || !checkChange()) return;

      setSaving(true);

      try
      {
         let timeStart = null;
         let timeEnd = null;
         let newErrors: any[] = [];

         if(environment?.timeStart !== null) timeStart = environment.timeStart.utc().format('HH:mm:ss');
         if(environment?.timeEnd !== null) timeEnd = environment.timeEnd.utc().format('HH:mm:ss');

         if(environment.sensorType === '2')
         {
            if(environment.highDesire === 0) newErrors.push('high');
            if(environment.lowDesire === 0) newErrors.push('low');
            if(environment.target === 0) newErrors.push('target');
            if(environment.humidity === 0) newErrors.push('humidity');
         }
         else if(environment.sensorType === '3')
         {
            if(timeStart === null) newErrors.push('start');
            if(timeEnd === null) newErrors.push('end');
            if(environment.target === 0) newErrors.push('target');
         }
         
         if(environment.name === '') newErrors.push('name');
         if(_.isEmpty(environment.sensors)) newErrors.push('sensors');
         

         setError(newErrors);
         setPageError("");

         if(newErrors.length > 0)
         {
            setSaving(false);
            return;
         }

         await environmentRoutes.save(environment.name, environment.sensorTypeId, environment.lowDesire, environment.highDesire, environment.target,
            environment.humidity, environment.timeStart, environment.timeEnd, environment.sensors, environment.relays, 'adamico', environment.id);

         navigate("/environment");

         setSaving(false);
      }
      catch(error)
      {
         setPageError("Failed to update database.");
         setSaving(false);
         console.log(error);
      }
   };

   const checkChange = () => {
      let timeStart = null;
      let initTimeStart = null;
      let timeEnd = null;
      let initTimeEnd = null;

      if(environment?.timeStart !== null && environment?.timeStart !== undefined) timeStart = environment.timeStart.utc().format('HH:mm:ss');
      if(initEnvironment?.timeStart !== null && initEnvironment?.timeStart !== undefined) initTimeStart = initEnvironment.timeStart.utc().format('HH:mm:ss');
      if(environment?.timeEnd !== null && environment?.timeEnd !== undefined) timeEnd = environment.timeEnd.utc().format('HH:mm:ss');
      if(initEnvironment?.timeEnd !== null && initEnvironment?.timeEnd !== undefined) initTimeEnd = initEnvironment.timeEnd.utc().format('HH:mm:ss');
      if(timeStart !== initTimeStart) return true;
      if(timeEnd !== initTimeEnd) return true;

      if(environment.name !== initEnvironment.name) return true;
      if(environment.sensorType !== initEnvironment.sensorType) return true;
      if(environment.highDesire !== initEnvironment.highDesire) return true;
      if(environment.lowDesire !== initEnvironment.lowDesire) return true;
      if(environment.target !== initEnvironment.target) return true;
      if(environment.humidity !== initEnvironment.humidity) return true;

      if(!_.isEqual(environment.sensors, initEnvironment.sensors)) return true;
      if(!_.isEqual(environment.relays, initEnvironment.relays)) return true;

      return false;
   }

   const fetchData = async () => {
      let data: any = {};
      let edit: any = {...initialState};
      let id = location?.state?.environmentId || null;

      try
      {
         data = formatOptions(await environmentRoutes.fetchEnvironmentsConfigOption());

         //sets the valid sensor types for environments
         data.sensorTypes = data.sensorTypes.filter((sensorType: any) => validSensorStypes.indexOf(sensorType.id) !== -1);

         if(id != null)
         {
            edit = formatOne(await environmentRoutes.fetchOneEnvironmentWithDetail(id));

            edit = {
               ...edit,
               sensorTypeId: edit.sensorType.id,
               sensors: (edit.sensors !== undefined ? [...edit.sensors] : []),
               relays: (edit.relays !== undefined ? [...edit.relays] : [])
            };

            setEnvironment({
               type: 'setup',
               payload: {
                  ...edit
               }
            });
         }

         setInitEnvironment(edit);
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

   const customListSensor = (title: React.ReactNode, items: any[]) => (
      <Card>
        <CardHeader
          sx={{ px: 2, py: 1 }}
          avatar={
            <Checkbox
              onClick={handleToggleAllSensor(items)}
              checked={numberOfCheckedSensor(items) === items.length && items.length !== 0}
              indeterminate={
                numberOfCheckedSensor(items) !== items.length && numberOfCheckedSensor(items) !== 0
              }
              disabled={items.length === 0}
              inputProps={{
                'aria-label': 'all items selected',
              }}
            />
          }
          title={title}
          subheader={`${numberOfCheckedSensor(items)}/${items.length} selected`}
        />
        <Divider />
        <List
          sx={{
            minWidth: 200,
            height: 230,
            bgcolor: 'background.paper',
            overflow: 'auto',
          }}
          dense
          component="div"
          role="list"
        >
          {items.map((value: any) => {  
            return (
              <ListItem
                key={'sensor-' + value.id}
                role="listitem"
                onClick={handleSensorToggle(value)}
              >
                <ListItemIcon>
                  <Checkbox
                    checked={checkedSensor.indexOf(value) !== -1}
                    tabIndex={-1}
                    disableRipple
                  />
                </ListItemIcon>
                <ListItemText id={value?.id} primary={value?.type.type + ' (' + value?.number + ')'} />
              </ListItem>
            );
          })}
        </List>
      </Card>
   );

   const customListRelay = (title: React.ReactNode, items: any[]) => (
      <Card>
        <CardHeader
          sx={{ px: 2, py: 1 }}
          avatar={
            <Checkbox
              onClick={handleToggleAllRelay(items)}
              checked={numberOfCheckedRelay(items) === items.length && items.length !== 0}
              indeterminate={
                numberOfCheckedRelay(items) !== items.length && numberOfCheckedRelay(items) !== 0
              }
              disabled={items.length === 0}
              inputProps={{
                'aria-label': 'all items selected',
              }}
            />
          }
          title={title}
          subheader={`${numberOfCheckedRelay(items)}/${items.length} selected`}
        />
        <Divider />
        <List
          sx={{
            minWidth: 200,
            height: 230,
            bgcolor: 'background.paper',
            overflow: 'auto',
          }}
          dense
          component="div"
          role="list"
        >
          {items.map((value: any) => {  
            return (
              <ListItem
                key={'relay-' + value.id}
                role="listitem"
                onClick={handleRelayToggle(value)}
              >
                <ListItemIcon>
                  <Checkbox
                    checked={checkedRelay.indexOf(value) !== -1}
                    tabIndex={-1}
                    disableRipple
                  />
                </ListItemIcon>
                <ListItemText id={value?.id} primary={value?.type.type + ' (' + value?.number + ')'} />
              </ListItem>
            );
          })}
        </List>
      </Card>
   );

   return (
      <React.Fragment>
         <Grid2 container direction="column" justifyContent="flex-start" alignItems="flex-start" style={{minHeight: '100%'}}>
            <Grid2 xs={2}>
               <h1>{(environment.id) ? "Edit environment" : "Create environment"}</h1>
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
                     <h2>Environment info</h2>
                  </Grid2>
                  <Grid2 xs container direction="column" justifyContent="flex-end" alignItems="flex-start" style={{width: '100%'}}>
                     <Grid2 xs>
                        <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
                           <FormControl key={'name'}>
                              <FormLabel required>Enviornment name</FormLabel>
                              <TextField
                                 id="setName"
                                 value={environment.name}
                                 onChange={onChange}
                                 variant="standard"
                              />
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("name") !== -1) ? <span>Name for the environment is required.</span> : null}
                           </Grid2>
                           <FormControl key={'sensorType'}>
                              <FormLabel required>Sensor type</FormLabel>
                              <Select
                                 onChange={onSensorTypeChange}
                                 value={environment.sensorTypeId}
                              >
                                 <MenuItem key={'sensorType-'} value={''}>Remove</MenuItem>
                                 {options.sensorTypes?.map((sensorType: any) => <MenuItem key={'sensorType-' + sensorType.id} value={sensorType.id}>{sensorType.type}</MenuItem>)}
                              </Select>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("type") !== -1) ? <span>Sensor type cannot be blank.</span> : null}
                           </Grid2>
                           {(environment?.sensorType === '2' || environment?.sensorType === '3')
                           ?
                              <React.Fragment>
                                 <FormControl key={'target'}>
                                    <FormLabel required>Target {environment?.sensorType === '2' ? 'temparature' : 'luminosity'}</FormLabel>
                                    <TextField
                                       id="setTarget"
                                       type='number'
                                       value={environment.target}
                                       onChange={onChange}
                                       variant="standard"
                                    />
                                 </FormControl>
                                 <Grid2 container className="error-text">
                                    {(errors.indexOf("target") !== -1) ? <span>Please enter a value for the target.</span> : null}
                                 </Grid2>
                              </React.Fragment>
                           :
                              null
                           }
                           {(environment?.sensorType === '2')
                           ?
                              <React.Fragment>
                                 <FormControl key={'lowDesire'}>
                                    <FormLabel required>Low temparture range</FormLabel>
                                    <TextField
                                       id="setLowDesire"
                                       type='number'
                                       value={environment.lowDesire}
                                       onChange={onChange}
                                       variant="standard"
                                    />
                                 </FormControl>
                                 <Grid2 container className="error-text">
                                    {(errors.indexOf("low") !== -1) ? <span>Please enter a value for the low temparature range.</span> : null}
                                 </Grid2>
                                 <FormControl key={'highDesire'}>
                                    <FormLabel required>High temparature range</FormLabel>
                                    <TextField
                                       id="setHighDesire"
                                       type='number'
                                       value={environment.highDesire}
                                       onChange={onChange}
                                       variant="standard"
                                    />
                                 </FormControl>
                                 <Grid2 container className="error-text">
                                    {(errors.indexOf("high") !== -1) ? <span>Please enter a value for the high temparature range.</span> : null}
                                 </Grid2>
                                 <FormControl key={'humidity'}>
                                    <FormLabel required>Humidity desire</FormLabel>
                                    <TextField
                                       id="setHumidity"
                                       type='number'
                                       value={environment.humidity}
                                       onChange={onChange}
                                       variant="standard"
                                    />
                                 </FormControl>
                                 <Grid2 container className="error-text">
                                    {(errors.indexOf("humidity") !== -1) ? <span>Please enter a value for the desired humidity.</span> : null}
                                 </Grid2>
                              </React.Fragment>
                           :
                              null
                           }
                           {(environment?.sensorType === '3')
                           ?
                              <React.Fragment>
                                 <FormControl key={'timeStart'}>
                                    <FormLabel required>Time start</FormLabel>
                                    <LocalizationProvider dateAdapter={AdapterMoment} adapterLocale="en">
                                       <TimePicker
                                          value={environment.timeStart}
                                          onChange={(event: any, validation: any) => (validation.validationError !== 'invalidDate') ? onTimeChange('setTimeStart', event) : null}
                                       />
                                    </LocalizationProvider>
                                 </FormControl>
                                 <Grid2 container className="error-text">
                                    {(errors.indexOf("start") !== -1) ? <span>Please enter a value for the time lights can turn on.</span> : null}
                                 </Grid2>
                                 <FormControl key={'timeEnd'}>
                                    <FormLabel required>Time start</FormLabel>
                                    <LocalizationProvider dateAdapter={AdapterMoment} adapterLocale="en">
                                       <TimePicker
                                          value={environment.timeEnd}
                                          onChange={(event: any, validation: any) => (validation.validationError !== 'invalidDate') ? onTimeChange('setTimeEnd', event) : null}
                                       />
                                    </LocalizationProvider>
                                 </FormControl>
                                 <Grid2 container className="error-text">
                                    {(errors.indexOf("end") !== -1) ? <span>Please enter a value for the time lights should turn off.</span> : null}
                                 </Grid2>
                              </React.Fragment>
                           :
                              null
                           }
                           <FormControl key={'sensors'}>
                              <FormLabel required>Sensors</FormLabel>
                              <Grid2 container spacing={2} justifyContent="center" alignItems="center">
                                 <Grid2>{customListSensor('Choices', leftSensor)}</Grid2>
                                 <Grid2>
                                    <Grid2 container direction="column" alignItems="center">
                                       <Button
                                          sx={{ my: 0.5 }}
                                          variant="outlined"
                                          size="small"
                                          onClick={handleSensorCheckedRight}
                                          disabled={leftSensorChecked.length === 0}
                                          aria-label="move selected right"
                                       >
                                          &gt;
                                       </Button>
                                       <Button
                                          sx={{ my: 0.5 }}
                                          variant="outlined"
                                          size="small"
                                          onClick={handleSensorCheckedLeft}
                                          disabled={rightSensorChecked.length === 0}
                                          aria-label="move selected left"
                                       >
                                          &lt;
                                       </Button>
                                    </Grid2>
                                 </Grid2>
                                 <Grid2>{customListSensor('Chosen', environment.sensors)}</Grid2>
                              </Grid2>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("sensors") !== -1) ? <span>Please select at least 1 sensor.</span> : null}
                           </Grid2>
                           <FormControl key={'relays'}>
                              <FormLabel>Actuators</FormLabel>
                              <Grid2 container spacing={2} justifyContent="center" alignItems="center">
                                 <Grid2>{customListRelay('Choices', leftRelay)}</Grid2>
                                 <Grid2>
                                    <Grid2 container direction="column" alignItems="center">
                                       <Button
                                          sx={{ my: 0.5 }}
                                          variant="outlined"
                                          size="small"
                                          onClick={handleRelayCheckedRight}
                                          disabled={leftRelayChecked.length === 0}
                                          aria-label="move selected right"
                                       >
                                          &gt;
                                       </Button>
                                       <Button
                                          sx={{ my: 0.5 }}
                                          variant="outlined"
                                          size="small"
                                          onClick={handleRelayCheckedLeft}
                                          disabled={rightRelayChecked.length === 0}
                                          aria-label="move selected left"
                                       >
                                          &lt;
                                       </Button>
                                    </Grid2>
                                 </Grid2>
                                 <Grid2>{customListRelay('Chosen', environment.relays)}</Grid2>
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
                                 <Fab variant='extended' color="primary" disabled={saving || !checkChange()}>Save</Fab>
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

export default EditEnvironment;