import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import moment from 'moment-timezone';
import MyTable from '../common/table/MyTable';
import { plantRoutes } from '../../service/ApiService';

interface Column {
   id: 'name' | 'size' | 'type' | 'dateObtain' | 'highMoisture' | 'lowMoisture' | 'dead' | 'updateBy' | 'updateTs';
   label: string;
   minWidth?: number;
   align?: 'right';
   format?: (value: any, id: any) => any;
}

const columns: readonly Column[] = [
   { 
      id: 'name',
      label: 'Name',
      minWidth: 170,
      format: (row, id) => <Link to={'/plant/' + row['id']} state={{plantId: row['id']}}>{row['number'] ? row[id] + ' (' + row['number'] + ')' : row[id]}</Link>,
   },
   { id: 'size', label: 'Pot size', minWidth: 100 },
   {
      id: 'type',
      label: 'Sensor type',
      minWidth: 170,
      format: (row, id) => row[id]
   },
   {
      id: 'dateObtain',
      label: 'Date Obtain',
      minWidth: 170,
   },
   {
      id: 'highMoisture',
      label: 'High Moisture (%)',
      minWidth: 170,
      align: 'right',
   },
   {
      id: 'lowMoisture',
      label: 'Low Moisture (%)',
      minWidth: 170,
      align: 'right',
   },
   {
      id: 'dead',
      label: 'Dead',
      minWidth: 170,
      format: (row, id) => row[id] === 1 ? 'true' : 'false',
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
   }
];

function Home() {
   const [plants, setPlants] = useState<any[]>([]);

   const fetchData = async () => {
      let data = [];
      
      try
      {
         data = await plantRoutes.fetchPlantsWithDetails();

         setPlants(data);
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
      <MyTable value={plants} columns={columns} />
   );
}

export default Home;