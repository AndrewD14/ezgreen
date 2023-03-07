import { Outlet, useLocation } from "react-router-dom";
import { Container } from '@mui/material';
import Grid from '@mui/material/Unstable_Grid2';
import Header from './features/common/header/Header';
import Navbar from './features/common/navbar/Navbar';
import './App.css';

function App() {
   const location = useLocation();

   return (
      <Container maxWidth={false}>
         {(location.pathname !== "/test") ? <Header /> : null}
         {(location.pathname !== "/test") ? <Navbar /> : null}
         <Grid container id="wrapper">
            <Outlet />
         </Grid>
      </Container>
  );
}

export default App;
