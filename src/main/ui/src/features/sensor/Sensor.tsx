import React, { useState, useEffect } from 'react';
import BlockIcon from '@mui/icons-material/Block';
import CheckIcon from '@mui/icons-material/Check';
import _ from 'lodash';
import moment from 'moment-timezone';
import MyTable from '../common/table/MyTable';
import { sensorRoutes } from '../../service/ApiService';
import { formatAll } from '../../service/utils/sensorFomat';

interface Column {
   id: 'type' | 'board' | 'port' | 'inUse' | 'lowCalibration' | 'highCalibration' | 'updateBy' | 'updateTs' | 'actions';
   label: string;
   minWidth?: number;
   align?: 'right';
   format?: (value: any, id: any) => any;
}

const columns: readonly Column[] = [
   { id: 'type', label: 'Type', minWidth: 170, },
   { id: 'board', label: 'Board', minWidth: 100, align: 'right', },
   { id: 'port', label: 'Port', minWidth: 170, align: 'right', },
   {
      id: 'inUse',
      label: 'In use',
      minWidth: 170,
      align: 'right', 
      format: (row, id) => _.isEmpty(row['plant']) ? <BlockIcon style={{color: '#e3272b'}}/> : <CheckIcon style={{color: '#7db856'}}/>
   },
   {
      id: 'lowCalibration',
      label: 'Low Calibration',
      minWidth: 170,
      align: 'right',
      format: (row, id) => !isNaN(row[id]) ? parseFloat(row[id]).toFixed(2) : ''
   },
   {
      id: 'highCalibration',
      label: 'High Calibration',
      minWidth: 170,
      align: 'right',
      format: (row, id) => !isNaN(row[id]) ? parseFloat(row[id]).toFixed(2) : ''
   },
   {
      id: 'updateBy',
      label: 'Updated By',
      minWidth: 170,
   },
   {
      id: 'updateTs',
      label: 'Updated (' + Intl.DateTimeFormat().resolvedOptions().timeZone + ')',
      minWidth: 170,
      align: 'right',
      format: (row, id) => moment.utc(row[id]).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('YYYY-MM-DD hh:mm:ss A')
   },
];

function Sensors() {
   const [sensors, setSensors] = useState<any[]>([]);

   const fetchData = async () => {
      let data = [];
      
      try
      {
         data = await sensorRoutes.fetchSensorsWithPlants();

         setSensors(formatAll(data));
         console.log(formatAll(data));
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