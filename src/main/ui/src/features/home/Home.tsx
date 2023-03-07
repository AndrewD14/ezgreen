import React, { useState, useEffect } from 'react';
import Table from '../common/MyTable';
import { plantRoutes } from '../../service/ApiService';

interface Column {
   id: 'name' | 'number' | 'potSizeId' | 'dateObtain' | 'dead' | 'delete';
   label: string;
   minWidth?: number;
   align?: 'right';
   format?: (value: number) => string;
}

const columns: readonly Column[] = [
   { id: 'name', label: 'Name', minWidth: 170 },
   { id: 'number', label: 'Number', minWidth: 100 },
   {
      id: 'potSizeId',
      label: 'Pot Size',
      minWidth: 170,
      align: 'right',
   },
   {
      id: 'dateObtain',
      label: 'Date Obtain',
      minWidth: 170,
      align: 'right',
      format: (value: number) => value.toLocaleString('en-US'),
   },
   {
      id: 'dead',
      label: 'Dead',
      minWidth: 170,
      align: 'right',
   },
   {
      id: 'delete',
      label: 'Deleted',
      minWidth: 170,
      align: 'right',
   }
];

function Home() {
   const [plants, setPlants] = useState<any[]>([]);

   const fetchData = async () => {
      let data = [];
      
      try
      {
         data = await plantRoutes.fetchPlants();

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
      <Table value={plants} columns={columns} />
   );
}

export default Home;