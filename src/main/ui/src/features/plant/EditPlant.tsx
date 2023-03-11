import React, { useState, useEffect} from 'react';
import { Card, CardContent, Container, CircularProgress, Fab } from '@mui/material';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import { Link, useLocation } from 'react-router-dom';


function EditPlant(props: any) {
   const [plant, setPlant] = useState<any>([]);
   const [loading, setLoading] = useState(true);
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
                  <Card variant="outlined" className="boxshadow card-spacing">
                     <CardContent>
                        <h2>Plant info</h2>
                     </CardContent>
                  </Card>
               </React.Fragment>
            }
            {(loading) ? null
            :
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
            }
      </React.Fragment>
   );
}

export default EditPlant;