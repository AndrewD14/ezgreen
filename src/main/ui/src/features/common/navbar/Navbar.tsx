import React from 'react';
import Box from '@mui/material/Box';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import { Link } from 'react-router-dom';

export default function Navbar(props: any) {
   const [value, setValue] = React.useState(0);

   const handleChange = (event: React.SyntheticEvent, newValue: number) => {
      setValue(newValue);
   };

   return (
      <Box sx={{ width: '100%' }}>
         <Tabs value={value} onChange={handleChange} aria-label="nav tabs">
            <Tab component={Link} label="Plants" to="/" />
            <Tab component={Link} label="History" to="/history" />
            <Tab component={Link} label="Enviornment" to="/environments" />
            <Tab component={Link} label="Sensors" to="/sensors" />
         </Tabs>
      </Box>
   );
}