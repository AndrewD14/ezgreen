import React, { useState, useEffect} from 'react';
import { Card, CardContent, CircularProgress, Checkbox,
         Fab, FormControl, FormControlLabel, InputLabel,
         MenuItem, Select, Stack, TextField } from '@mui/material';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Link, useLocation } from 'react-router-dom';


function EditPlant(props: any) {
   const [plant, setPlant] = useState<any>([]);
   const [loading, setLoading] = useState(false);
   const [errors, setError] = useState<any[]>([]);
   const [pageError, setPageError] = useState<string>("");
   const location: any = useLocation();

   return (
      <React.Fragment>
            <h1>{(props.location?.state?.id) ? "Edit Plant" : "Create plant"}</h1>
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
                           <TextField
                              id="name"
                              label="Plant name"
                              defaultValue="Default Value"
                              helperText="Some important text"
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           <TextField
                              id="number"
                              label="Number"
                              type="number"
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           pot size
                        </FormControl>
                        <FormControl>
                           <InputLabel id="sensor-label" variant='standard'>Sensor</InputLabel>
                           <Select
                              labelId="sensor-label"
                              id="sensorId"
                              // value={age}
                              label="Age"
                              // onChange={handleChange}
                           >
                              <MenuItem value={10}>10</MenuItem>
                              <MenuItem value={20}>20</MenuItem>
                              <MenuItem value={30}>30</MenuItem>
                           </Select>
                        </FormControl>
                        <FormControl>
                           date obtained
                        </FormControl>
                        <FormControl>
                           <TextField
                              id="lowMoisture"
                              label="Low moisture (%)"
                              type="number"
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           <TextField
                              id="highMoisture"
                              label="High moisture (%)"
                              type="number"
                              InputLabelProps={{
                                 shrink: true,
                              }}
                              variant="standard"
                           />
                        </FormControl>
                        <FormControl>
                           <FormControlLabel
                              value="start"
                              control={<Checkbox
                                          // checked={checked}
                                          // onChange={handleChange}
                                          inputProps={{ 'aria-label': 'controlled' }}/>
                                       }
                              label="Monitor"
                              labelPlacement="start"
                           />
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