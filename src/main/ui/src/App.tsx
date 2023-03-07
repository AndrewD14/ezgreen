import { Outlet, useLocation } from "react-router-dom";
import { Grid } from '@mui/material';
import Header from './features/common/header/Header';
import Navbar from './features/common/navbar/Navbar';
import './App.css';

function App() {
   const location = useLocation();

   return (
      <Grid container >
         {(location.pathname !== "/test") ? <Header /> : null}
         {(location.pathname !== "/test") ? <Navbar /> : null}
         <Grid container id="wrapper">
            <Outlet />
         </Grid>
      </Grid>
  );
}

export default App;
