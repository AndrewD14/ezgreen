import React, { useState, useEffect } from 'react';
import { Outlet, useLocation } from "react-router-dom";
import { Container } from '@mui/material';
import Header from './features/common/header/Header';
import Navbar from './features/common/navbar/Navbar';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './App.css';

function App() {
   const [stompClient, setStomp] = useState<any>();
   const location = useLocation();

   const connect = () => {
      if(stompClient !== undefined && stompClient.connected) return;

      let socket = new SockJS('http:localhost:5000/ezgreen');
      let client = Stomp.over(socket);
      
      client.connect({}, (frame: any) => {

      });

      setStomp(client);
   };

   const disconnect = () => {
      if(stompClient !== undefined) stompClient.disconnect();
   };

   useEffect(() => {
      connect();

      return(() => {
         disconnect();
      });
   },
   // eslint-disable-next-line
   []);

   return (
      <Container maxWidth={false} sx={{ height:'85vh'}}>
         {(location.pathname !== "/test") ? <Header /> : null}
         {(location.pathname !== "/test") ? <Navbar /> : null}
         <Outlet context={stompClient}/>
      </Container>
  );
}

export default App;
