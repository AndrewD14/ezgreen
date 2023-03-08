import React, { useState, useEffect } from 'react';
import MyTable from '../common/table/MyTable';
import { sensorRoutes } from '../../service/ApiService';

interface Column {
   id: 'type' | 'board' | 'port' | 'lowCalibration' | 'highCalibration';
   label: string;
   minWidth?: number;
   align?: 'right';
   format?: (value: number) => string;
}

const columns: readonly Column[] = [
   { id: 'type', label: 'Type', minWidth: 170, },
   { id: 'board', label: 'Board', minWidth: 100, align: 'right', },
   { id: 'port', label: 'Port', minWidth: 170, align: 'right', },
   {
      id: 'lowCalibration',
      label: 'Low Calibration',
      minWidth: 170,
      align: 'right',
   },
   {
      id: 'highCalibration',
      label: 'High Calibration',
      minWidth: 170,
      align: 'right',
   }
];

function Sensors() {
   const [sensors, setSensors] = useState<any[]>([]);

   const fetchData = async () => {
      let data = [];
      
      try
      {
         data = await sensorRoutes.fetchSensors();

         setSensors(data);
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
      <MyTable value={sensors} columns={columns} />
   );
}

export default Sensors;