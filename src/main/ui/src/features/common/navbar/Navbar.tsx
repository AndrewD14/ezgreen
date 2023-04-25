import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import { Link, useLocation } from 'react-router-dom';

export default function Navbar(props: any)
{
   const [value, setValue] = useState(0);
   const location = useLocation();

   useEffect(() => {
      let path = location.pathname;
      let value = 0;

      if(path === '/' || path.startsWith("/plant")) value = 0;
      else if(path.startsWith("/history")) value = 1;
      else if(path.startsWith("/environment")) value = 2;
      else if(path.startsWith("/sensor")) value = 3;
      else if(path.startsWith("/relay")) value = 4;

      setValue(value);
   },
   [location]
   );

   return (
      <Box sx={{ width: '100%' }}>
         <Tabs value={value} aria-label="nav tabs">
            <Tab component={Link} label="Plants" to="/"/>
            <Tab component={Link} label="History" to="/history"/>
            <Tab component={Link} label="Environment" to="/environment"/>
            <Tab component={Link} label="Sensors" to="/sensor"/>
            <Tab component={Link} label="Actuators" to="/relay"/>
         </Tabs>
      </Box>
   );
}