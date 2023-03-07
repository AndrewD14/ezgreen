import React, { useState, useEffect } from 'react';
import MyTable from '../common/table/MyTable';
import { environmentRoutes } from '../../service/ApiService';

interface Column {
   id: 'location' | 'sensorId';
   label: string;
   minWidth?: number;
   align?: 'right';
   format?: (value: number) => string;
}

const columns: readonly Column[] = [
   { id: 'location', label: 'Location', minWidth: 100, },
   { id: 'sensorId', label: 'Sensor Id', minWidth: 170, align: 'right', },
];

function Environment()
{
   const [environments, setEnvironments] = useState([]);

   const fetchData = async () => {
      let data = [];
      
      try
      {
         data = await environmentRoutes.fetchEnvironments();

         setEnvironments(data);
         console.log(data);
      }
      catch(error: any)
      {
         console.log(error.message);
         console.log(error.stack);
      }
   };

   useEffect(
      () => {
         fetchData();
      },
      []
   );

   return (
      <MyTable value={environments} columns={columns}/>
   );
}

export default Environment;