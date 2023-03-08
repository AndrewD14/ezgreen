import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { plantRoutes } from '../../service/ApiService';

function Plant() {
   const [plant, setPlant] = useState<any>();
   let { state } = useLocation();

   const fetchData = async () => {
      let data = {};
      
      try
      {
         data = await plantRoutes.fetchOnePlantWithDetails(state.plantId);

         console.log(data);
         setPlant(data);
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
      <div></div>
   );
}

export default Plant;