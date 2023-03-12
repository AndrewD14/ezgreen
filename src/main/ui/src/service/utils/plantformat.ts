import _ from 'lodash';

export function formatAll(data: any)
{
   let plants: any = [];

   plants = data.plants.map((plant: any) => {
      let cursor = 0;

      if(plant.sensorId >= 1)
      {
         do
         {
            if(data.sensors[cursor].id === plant.sensorId) plant.sensor = structuredClone(data.sensors[cursor]);

            cursor = cursor + 1;
         }while(cursor < data.sensors.length && _.isEmpty(plant.sensor));
      }

      cursor = 0;

      do
      {
         if(data.potSizes[cursor].id === plant.potSizeId) plant.potSize = structuredClone(data.potSizes[cursor]);

         cursor = cursor + 1;
      }while(cursor < data.potSizes.length && _.isEmpty(plant.potSize));

      return plant;
   });

   console.log(plants)
   return plants;
}

export function formatOne(data: any)
{
   let plant = data.plant;

   if(!_.isEmpty(data.sensor)) plant.sensor = structuredClone(data.sensor);
   
   plant.potSize = structuredClone(data.potSize);

   return plant;
}