import { Outlet, useLocation } from "react-router-dom";
import { Container } from '@mui/material';
import Header from './features/common/header/Header';
import Navbar from './features/common/navbar/Navbar';
import './App.css';

function App() {
   const location = useLocation();

   return (
      <Container maxWidth={false} sx={{ height:'85vh'}}>
         {(location.pathname !== "/test") ? <Header /> : null}
         {(location.pathname !== "/test") ? <Navbar /> : null}
         <Outlet />
      </Container>
  );
}

export default App;
