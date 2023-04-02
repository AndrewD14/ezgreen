import React, { useState, useReducer, useEffect} from 'react';
import { CircularProgress, Fab, FormControl, FormLabel,
         MenuItem, Select, Stack, TextField } from '@mui/material';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { relayRoutes } from '../../service/ApiService';
import { formatOne, formatOption } from '../../service/utils/relayFormat';

const initialState: any = {
   id: null,
   bus: '',
   board: '',
   boardInfo: {},
   number: '',
   relay: 0,
   typeId: '',
}

function reducer(state: any, action: any)
{
   switch(action.type)
   {
      case 'setup': return {...state, ...action.payload}
      case 'setNumber': return {...state, number: action.payload};
      case 'setBus': return {...state, bus: action.payload};
      case 'setBoard': return {...state, board: action.payload.number, boardInfo: {...action.payload.boardInfo}};
      case 'setRelay': return {...state, relay: action.payload};
      case 'setType': return {...state, typeId: action.payload};
      default: throw new Error(action.type + " is not supported.");
   }
}

function EditRelay(props: any) {
   const [relay, setRelay] = useReducer(reducer, initialState);
   const [initRelay, setInitRelay] = useState<any>(initialState);
   const [options, setOptions] = useState<any>({});
   const [loading, setLoading] = useState(true);
   const [errors, setError] = useState<any[]>([]);
   const [pageError, setPageError] = useState<string>("");
   const [saving, setSaving] = useState<boolean>(false);
   const location: any = useLocation();
   const navigate: any = useNavigate();

   const onIntChange = (event: any) => {
      let value = event.target.value;
      let id = event.target.id;

      if(value !== '' && parseInt(value) < 0) return;

      setRelay({
         type: id,
         payload: (value !== '' ? parseInt(value) : value)
      });
   };

   const onTypeChange = (event: any) => {
      let value = event.target.value;
      let num = options.maxNum[value] + 1;

      if(initRelay.type === value) num = initRelay.number;

      setRelay({type: 'setType', payload: value});
      setRelay({type: 'setNumber', payload: num});
   };

   const onBoardChange = (event: any) => {
      let number = event.target.value;
      //eslint-disable-next-line
      let filter = options.boards.filter((board: any) => (board.bus == relay.bus && board.number == number));
      let boardInfo = filter.length > 0 ? filter[0] : {};

      setRelay({
         type: 'setBoard',
         payload: {
            number: event.target.value,
            boardInfo: boardInfo
         }
      });
   };

   const undo = () => {
      setRelay({
         type: 'setup',
         payload: {
            ...initRelay,
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

         if(relay.typeId === '') newErrors.push("type");
         if(relay.bus === '') newErrors.push("bus");
         if(relay.board === '') newErrors.push("board");
         if(relay.relay === 0) newErrors.push("relay");
         if(options.portsUsed[relay.bus][relay.board].indexOf(relay.relay) !== -1) newErrors.push("relayCombo");

         setError(newErrors);
         setPageError("");

         if(newErrors.length > 0)
         {
            setSaving(false);
            return;
         }

         await relayRoutes.save(relay.typeId, relay.number, relay.boardInfo.id, relay.relay, 'adamico', relay.id);

         navigate("/relay");
      }
      catch(error)
      {
         setPageError("Failed to update database.");
         setSaving(false);
         console.log(error);
      }
   };

   const checkChange = () => {
      if(relay.typeId !== initRelay.typeId) return true;
      if(relay.number !== initRelay.number) return true;
      if(relay.bus !== initRelay.bus) return true;
      if(relay.board !== initRelay.board) return true;
      if(relay.relay !== initRelay.relay) return true;

      return false;
   }

   const fetchData = async () => {
      let data: any = {};
      let edit: any = {...initialState};
      let id = location?.state?.relayId || null;

      try
      {
         data = await relayRoutes.fetchRelayConfigOption();

         if(id != null)
         {
            edit = formatOne(await relayRoutes.fetchOneRelayWithDetail(id));

            setInitRelay({...edit});
            setRelay({
               type: 'setup',
               payload: {
                  ...edit,
                  boardInfo: {...edit.board},
                  board: edit.board.number,
                  bus: edit.board.bus
               }
            });
         }

         setOptions(formatOption(data));
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
               <h1>{(relay.id) ? "Edit actuator" : "Create actuator"}</h1>
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
                     <h2>Actuator info</h2>
                  </Grid2>
                  <Grid2 xs container direction="column" justifyContent="flex-end" alignItems="flex-start" style={{width: '100%'}}>
                     <Grid2 xs>
                        <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
                           <FormControl key={'relayType'}>
                              <FormLabel required>Actuator type</FormLabel>
                              <Select
                                 id='setType'
                                 onChange={onTypeChange}
                                 value={relay.typeId}
                              >
                                 {options.relayTypes?.map((relayType: any) => <MenuItem key={'relayType-' + relayType.id} value={relayType.id}>{relayType.type}</MenuItem>)}
                              </Select>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("type") !== -1) ? <span>Please select a actuator type.</span> : null}
                           </Grid2>
                           <FormControl key={'number'}>
                              <FormLabel>Number</FormLabel>
                              <TextField
                                 type="number"
                                 value={relay.number}
                                 variant="standard"
                                 inputProps={{ readOnly: true }}
                              />
                           </FormControl>
                           <FormControl key={'bus'}>
                              <FormLabel required>Serial bus</FormLabel>
                              <Select
                                 onChange={(event: any) => setRelay({type: 'setBus', payload: event.target.value})}
                                 value={relay.bus}
                              >
                                 {options.serialBus?.map((bus: any) => <MenuItem key={'bus-' + bus} value={bus}>{bus}</MenuItem>)}
                              </Select>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("bus") !== -1) ? <span>Please select a serial bus.</span> : null}
                           </Grid2>
                           <FormControl key={'board'}>
                              <FormLabel required>Board</FormLabel>
                              <Select
                                 id="setBoard"
                                 onChange={onBoardChange}
                                 value={relay.board}
                                 disabled={relay.bus === ''}
                              >
                                 <MenuItem key={'board-'} value=''>Remove</MenuItem>
                                 {relay.bus !== '' ? options.possibleBoards[relay.bus].map((board: any) => <MenuItem key={'board-' + board} value={'' + board}>{board}</MenuItem>) : null}
                              </Select>
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("board") !== -1) ? <span>Board number is required.</span> : null}
                           </Grid2>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("relayCombo") !== -1) ? <span>Board and relay combo is in use or not valid.</span> : null}
                           </Grid2>
                           <FormControl key={'relay'}>
                              <FormLabel required>Relay</FormLabel>
                              <TextField
                                 id="setRelay"
                                 type="number"
                                 value={relay.relay}
                                 onChange={onIntChange}
                                 InputLabelProps={{
                                    shrink: true,
                                 }}
                                 variant="standard"
                                 disabled={relay.board > 0 ? false : true}
                                 helperText="Input a board number first."
                              />
                           </FormControl>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("relay") !== -1) ? <span>Relay number is required.</span> : null}
                           </Grid2>
                           <Grid2 container className="error-text">
                              {(errors.indexOf("relayCombo") !== -1) ? <span>Board and relay combo is in use or not valid.</span> : null}
                           </Grid2>
                        </Stack>
                     </Grid2>
                     <Grid2 xs={3} style={{width: '100%'}}>
                        <Grid2 justifyContent="center">
                           <Grid2 container direction="row" className="error-text">
                              <Link to={{pathname:`/relay`}} style={{ textDecoration: 'none' }}>
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

export default EditRelay;