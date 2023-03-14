import React, { useState, useEffect} from 'react';
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

function EditPlant(props: any) {
   const [plant, setPlant] = useState<any>({});
   const [initPlant, setInitPlant] = useState<any>({});
   const [options, setOptions] = useState<any>({});
   const [loading, setLoading] = useState(true);
   const [errors, setError] = useState<any[]>([]);
   const [pageError, setPageError] = useState<string>("");
   const location: any = useLocation();

   let id = location.state?.plantId || null;

   const fetchData = async () => {
      let data = {};
      let edit = null;
      console.log("id: " + id)
      try
      {
         data = await plantRoutes.fetchPlantOptions();

         if(id != null)
         {
            edit = formatOne(await plantRoutes.fetchOnePlantWithDetails(id));

            setInitPlant(edit);
            setPlant(edit);
            console.log(edit);
         }

         console.log(data);
         setOptions(data);
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
   },
   //eslint-disable-next-line
   []);

   return (
      <React.Fragment>
            <h1>{(id) ? "Edit Plant" : "Create plant"}</h1>
            {(pageError) ? <div className="error-message">{pageError}</div> : null}
            {(loading) ?
               <Grid2 justifyContent="center" alignItems="center" style={{height: '30rem'}}>
                  <Grid2  xs={5}>
                     <CircularProgress />
                  </Grid2>
               </Grid2>
            :
               <React.Fragment>
                  <h2>Plant info</h2>
                  <Grid2 container xl={6}>
                     <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={0.5} minWidth="100%">
                        <FormControl>
                           <FormLabel required>Plant name</FormLabel>
                           <TextField
                              id="name"
                              value={plant.name}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           <FormLabel>Number</FormLabel>
                           <TextField
                              id="number"
                              type="number"
                              value={plant.number}
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           <FormLabel required>Pot size</FormLabel>
                           <Select
                              id="potSizeId"
                              // onChange={handleChange}
                              value={plant.potSizeId}
                           >
                              {id != null ? <MenuItem value={initPlant?.potSizeId}>{initPlant?.potSize?.size}</MenuItem> : null}
                              {options.potSizes?.map((potSize: any) => <MenuItem value={potSize.id}>{potSize.size}</MenuItem>)}
                           </Select>
                        </FormControl>
                        <FormControl>
                           <FormLabel required>Sensor</FormLabel>
                           <Select
                              id="sensorId"
                              // value={age}
                              // onChange={handleChange}
                              value={plant.sensorId}
                           >
                              {(id != null && initPlant?.sensor ) ? <MenuItem value={initPlant?.sensor?.id}>{initPlant?.sensor?.id + ' port: ' + initPlant?.sensor?.port + ' board: ' + initPlant?.sensor?.board}</MenuItem> : null}
                              {options.sensors?.map((sensor: any) => <MenuItem value={sensor.id}>{sensor.id + ' port: ' + sensor.port + ' board: ' + sensor.board}</MenuItem>)}
                           </Select>
                        </FormControl>
                        <FormControl>
                           <FormLabel>Date obtained</FormLabel>
                           <LocalizationProvider dateAdapter={AdapterMoment} adapterLocale="en">
                              <DesktopDatePicker
                                 value={plant.dateObtain ? moment(plant.dateObtain) : null}
                              />
                           </LocalizationProvider>
                        </FormControl>
                        <FormControl>
                           <FormLabel required>Low moisture (%)</FormLabel>
                           <TextField
                              id="lowMoisture"
                              type="number"
                              value={plant.lowMoisture}
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           <FormLabel required>High moisture (%)</FormLabel>
                           <TextField
                              id="highMoisture"
                              type="number"
                              value={plant.highMoisture}
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           <FormLabel required>Monitor</FormLabel>
                           <Checkbox
                              id='monitor'
                              checked={(plant.monitor === 1) ? true : false}
                              // onChange={handleChange}
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
                           <Fab variant='extended' color="primary" /*onClick={}*/>Undo</Fab>
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