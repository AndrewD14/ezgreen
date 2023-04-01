import React, { useState, useReducer, useEffect} from 'react';
import { CircularProgress,
         Fab, FormControl, FormLabel,
         MenuItem, Select, Stack, TextField } from '@mui/material';
import Button from '@mui/joy/Button';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { sensorRoutes } from '../../service/ApiService';
import { formatOne, formatOptions } from '../../service/utils/sensorFormat';

const initialState: any = {
   id: null,
   typeId: '',
   bus: '',
   boardInfo: {},
   board: '',
   port: 0,
   lowCalibration: 0,
   highCalibration: 0
}

function reducer(state: any, action: any)
{
   switch(action.type)
   {
      case 'setup': return {...state, ...action.payload}
      case 'setType': return {...state, typeId: action.payload.type, sensorType: {...action.payload.sensorType}};
      case 'setBus': return {...state, bus: action.payload};
      case 'setBoard': return {...state, board: action.payload.number, boardInfo: {...action.payload.board}};
      case 'setPort': return {...state, port: parseInt(action.payload)};
      case 'setLowCalibration': return {...state, lowCalibration: parseFloat(action.payload).toFixed(2)};
      case 'setHighCalibration': return {...state, highCalibration: parseFloat(action.payload).toFixed(2)};
      default: throw new Error(action.type + " is not supported.");
   }
}

function EditSensor(props: any) {
   const [sensor, setSensor] = useReducer(reducer, initialState);
   const [initSensor, setInitSensor] = useState<any>(initialState);
   const [options, setOptions] = useState<any>({});
   const [loading, setLoading] = useState(true);
   const [errors, setError] = useState<any[]>([]);
   const [pageError, setPageError] = useState<string>("");
   const [saving, setSaving] = useState<boolean>(false);
   const location: any = useLocation();
   const navigate: any = useNavigate();

   const sensorsWithCal: string[] = ['1', '4'];

   const onIntChange = (event: any) => {
      let value = event.target.value;
      let id = event.target.id;

      if(isNaN(value)) return;
      if(value < 0) return;

      value = parseInt(value);

      setSensor({
         type: id,
         payload: value
      });
   };

   const checkPort = (type: string, bus: string, board: string, port: number) => {
      if(port === 0) return false;

      if(type === 'Light' && port !== 4 && port !== 3) return false;
      if(type === 'Soil moisture'&& (port % 2 === 0 || port === 3)) return false;
      if(type === 'Water level' && port % 2 === 0 ) return false;
      if(type === 'Humidity/Temperature' && port !== 6 && port !== 8) return false;

      if(type !== 'Light')
      {
         if(initSensor.id !== undefined && 
            !((initSensor.bus === bus && initSensor.board === board && initSensor.port === port) || 
               (options[bus][board][port] !== undefined && initSensor.bus === bus && initSensor.board !== board && initSensor.port !== port))
         ) return false;
         else if(initSensor.id === undefined && options[bus][board][port] !== undefined) return false;
      }
      else if(type === 'Light' && options[bus][board][port].type !== 'Light') return false;

      return true;
   }

   const onTypeChange = (event: any) => {
      let type = event.target.value;
      //eslint-disable-next-line
      let filter = options.sensorTypes.filter((sensorType: any) => sensorType.id == type);
      let sensorType = filter.length > 0 ? filter[0] : {};

      if(sensorsWithCal.indexOf(type) === -1)
      {
         setSensor({
            type: 'setLowCalibration',
            payload: 0
         });

         setSensor({
            type: 'setHighCalibration',
            payload: 0
         });
      }

      setSensor({
         type: 'setType',
         payload: {
            type: type,
            sensorType: sensorType
         }
      });

      if(!checkPort(sensorType.type, sensor.bus, sensor.board, sensor.port))
         setSensor({
            type: 'setPort',
            payload: 0
         });
   };

   const onBusChange = (event: any) => {
      setSensor({
         type: 'setBus',
         payload: event.target.value
      });
   };

   const onBoardChange = (event: any) => {
      let number = event.target.value;
      //eslint-disable-next-line
      let filter = options.boardInfo.filter((board: any) => (board.bus == sensor.bus && board.number == number));
      let boardInfo = filter.length > 0 ? filter[0] : {};

      setSensor({
         type: 'setBoard',
         payload: {
            number: event.target.value,
            boardInfo: boardInfo
         }
      });
   };

   const getCalibration = async (event: any) => {
      let value: any = parseFloat((await sensorRoutes.fetchSensorCalibration(sensor.sensorType.arduino, sensor.board, sensor.port, sensor.bus))['responseMessage']).toFixed(2);

      setSensor({
         type: event.target.id,
         payload: value
      })
   };

   const undo = () => {
      setSensor({
         type: 'setup',
         payload: {
            ...initSensor,
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
         let newErrors: any[] = [];

         if(sensor.typeId === '') newErrors.push("type");
         if(sensor.bus === '') newErrors.push("bus");
         if(sensor.board === '') newErrors.push("board");
         if(sensor.port === 0) newErrors.push("port");
         if(!checkPort(sensor.type, sensor.bus, sensor.board, sensor.port)) newErrors.push("portCombo");
         if(sensorsWithCal.indexOf(sensor.type) >= 0 && !(sensor.lowCalibration > 0)) newErrors.push("low");
         if(sensorsWithCal.indexOf(sensor.type) >= 0 && !(sensor.highCalibration > 0)) newErrors.push("high");

         setError(newErrors);
         setPageError("");

         if(newErrors.length > 0)
         {
            setSaving(false);
            return;
         }
         
         await sensorRoutes.save(sensor.typeId, sensor.boardInfo?.id, sensor.port,
            (sensorsWithCal.indexOf(sensor.typeId) >= 0 ? null : sensor.lowCalibration),
            (sensorsWithCal.indexOf(sensor.typeId) >= 0 ? null : sensor.highCalibration),
            'adamico', sensor.id);

         navigate("/sensor");
      }
      catch(error)
      {
         setPageError("Failed to update database.");
         setSaving(false);
         console.log(error);
      }
   };

   const checkChange = () => {
      if(sensor.typeId !== initSensor.typeId) return true;
      if(sensor.bus !== initSensor.bus) return true;
      if(sensor.port !== initSensor.port) return true;
      if(sensor.board !== initSensor.board) return true;
      if(sensor.lowCalibration !== initSensor.lowCalibration) return true;
      if(sensor.highCalibration !== initSensor.highCalibration) return true;

      return false;
   }

   const fetchData = async () => {
      let data: any = {};
      let edit: any = {};
      let id = location?.state?.sensorId || null;

      try
      {
         data = formatOptions(await sensorRoutes.fetchSensorsWithDetails());

         if(id != null)
         {
            edit = formatOne(await sensorRoutes.fetchOneSensorWithDetails(id));

            edit = {
               ...initialState,
               ...edit,
               bus: edit.boardInfo.bus,
               board: edit.boardInfo.number,
               highCalibration: sensorsWithCal.indexOf(sensor.typeId) >= 0 ? parseFloat(edit.highCalibration).toFixed(2) : 0,
               lowCalibration: sensorsWithCal.indexOf(sensor.typeId) >= 0 ? parseFloat(edit.lowCalibration).toFixed(2) : 0,
            };

            setInitSensor({...edit});
            setSensor({
               type: 'setup',
               payload: {
                  ...edit
               }
            });
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
               <h1>{(sensor.id) ? "Edit sensor" : "Create sensor"}</h1>
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
                     <h2>Sensor info</h2>
                  </Grid2>
                  <Grid2 xs container direction="column" justifyContent="flex-end" alignItems="flex-start" style={{width: '100%'}}>
                     <Grid2 xs>
                        <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
                           <FormControl key={'type'}>
                              <FormLabel required>Sensor type</FormLabel>
                              <Select
                                 id="setType"
                                 onChange={onTypeChange}
                                 value={sensor.typeId}
                              >
                                 <MenuItem key={'type-'} value=''>Remove</MenuItem>
                                 {options.sensorTypes.map((sensorType: any) => <MenuItem key={'type-' + sensorType.id} value={'' + sensorType.id}>{sensorType.type}</MenuItem>)}
                              </Select>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("type") !== -1) ? <span>A type is required.</span> : null}
                           </Grid2>
                           <FormControl key={'bus'}>
                              <FormLabel required>Serial bus</FormLabel>
                              <Select
                                 id="setBus"
                                 onChange={onBusChange}
                                 value={sensor.bus}
                              >
                                 <MenuItem key={'bus-'} value=''>Remove</MenuItem>
                                 {options.serialBus.map((bus: any) => <MenuItem key={'bus-' + bus} value={'' + bus}>{bus}</MenuItem>)}
                              </Select>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("bus") !== -1) ? <span>A serial bus must be selected.</span> : null}
                           </Grid2>
                           <FormControl key={'board'}>
                              <FormLabel required>Board</FormLabel>
                              <Select
                                 id="setBoard"
                                 onChange={onBoardChange}
                                 value={sensor.board}
                                 disabled={sensor.type === '' || sensor.serialBus === ''}
                              >
                                 <MenuItem key={'board-'} value=''>Remove</MenuItem>
                                 {sensor.bus !== '' ? options.boards[sensor.bus].map((board: any) => <MenuItem key={'board-' + board} value={'' + board}>{board}</MenuItem>) : null}
                              </Select>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("board") !== -1) ? <span>Board number is required.</span> : null}
                           </Grid2>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("portCombo") !== -1) ? <span>Board and port combo is in use or not valid.</span> : null}
                           </Grid2>
                           <FormControl key={'port'}>
                              <FormLabel required>Port</FormLabel>
                              <TextField
                                 id="setPort"
                                 type="number"
                                 value={sensor.port}
                                 onChange={onIntChange}
                                 InputLabelProps={{
                                    shrink: true,
                                 }}
                                 variant="standard"
                                 disabled={(sensor.typeId !== '' && sensor.board > 0) ? false : true}
                                 helperText="Select a type and input a board number first."
                              />
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("port") !== -1) ? <span>Port number is required.</span> : null}
                           </Grid2>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("portCombo") !== -1) ? <span>Board and port combo is in use or not valid.</span> : null}
                           </Grid2>
                           <FormControl key={'lowCalibration'}>
                              <FormLabel required={(sensor.typeId === '' || sensorsWithCal.indexOf(sensor.typeId) < 0) ? false : true}>Low calibration</FormLabel>
                              <Grid2>
                                 <TextField
                                    value={sensor.lowCalibration}
                                    inputProps={{ readOnly: true }}
                                    variant="standard"
                                    disabled={(sensor.typeId === '' || sensorsWithCal.indexOf(sensor.typeId) < 0) ? true : false}
                                 />
                                 <Button id="setLowCalibration" disabled={(sensor.typeId === '' || sensorsWithCal.indexOf(sensor.typeId) < 0) ? true : false} onClick={getCalibration}>Calibrate</Button>
                              </Grid2>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("low") !== -1) ? <span>Please run the calibration to get the low configuration.</span> : null}
                           </Grid2>
                           <FormControl key={'highCalibration'}>
                              <FormLabel required={(sensor.typeId === '' || sensorsWithCal.indexOf(sensor.typeId) < 0) ? false : true}>High calibration</FormLabel>
                              <Grid2>
                                 <TextField
                                    value={sensor.highCalibration}
                                    inputProps={{ readOnly: true }}
                                    variant="standard"
                                    disabled={(sensor.typeId === '' || sensorsWithCal.indexOf(sensor.typeId) < 0) ? true : false}
                                 />
                                 <Button id="setHighCalibration" disabled={(sensor.typeId === '' || sensorsWithCal.indexOf(sensor.typeId) < 0) ? true : false} onClick={getCalibration}>Calibrate</Button>
                              </Grid2>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("high") !== -1) ? <span>Please run the calibration to get the high configuration.</span> : null}
                           </Grid2>
                        </Stack>
                     </Grid2>
                     <Grid2 xs={3} style={{width: '100%'}}>
                        <Grid2 justifyContent="center">
                           <Grid2 container direction="row" className="error-text">
                              <Link to={{pathname:`/sensor`}} style={{ textDecoration: 'none' }}>
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

export default EditSensor;