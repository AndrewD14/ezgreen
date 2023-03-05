import React, { useState, useEffect } from 'react';
import logo from './logo.svg';
//import { Counter } from './features/counter/Counter';
import './App.css';

import { configRoutes } from './service/ApiService';

function App() {
   const [configs, setConfigs] = useState([]);

   const fetchData = async () => {
      let data = [];
      
      try
      {
         data = await configRoutes.fetchFields();

         setConfigs(data);
         console.log(data);
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
      }
    };

   useEffect(() => {
      fetchData();
   }, []);

   return (
      <div className="App">
         <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
         </header>
         <div>
            <ul>
               {configs.map((config:any) => (<li key={config.id}>{config.name}</li>))}
            </ul>
         </div>
      </div>
  );
}

export default App;
