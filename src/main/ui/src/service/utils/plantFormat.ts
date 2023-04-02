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
         if(data.plantTypes[cursor].id === plant.plantTypeId) plant.plantType = structuredClone(data.plantTypes[cursor]);

         cursor = cursor + 1;
      }while(cursor < data.plantTypes.length && _.isEmpty(plant.plantTypes));

      cursor = 0;

      do
      {
         if(data.potSizes[cursor].id === plant.potSizeId) plant.potSize = structuredClone(data.potSizes[cursor]);

         cursor = cursor + 1;
      }while(cursor < data.potSizes.length && _.isEmpty(plant.potSize));

      return plant;
   });

   return plants;
}

export function formatOption(data: any)
{
   let options: any = {
      plantTypes: data.plantTypes,
      plants: data.plants,
      potSizes: data.potSizes,
      relayTypes: data.relayTypes,
   };

   options.relays = data.relays.map((relay: any) => {
      relay.type = data.relayTypes.filter((relayType: any) => relayType.id === relay.typeId)[0];

      return relay;
   });

   options.sensors = data.sensors.map((sensor: any) => {
      sensor.type = data.sensorTypes.filter((sensorType: any) => sensorType.id === sensor.typeId)[0];
      sensor.board = data.boards.filter((board: any) => sensor.boardId === board.id)[0];

      return sensor;
   });

   return options;
}

export function formatOne(data: any)
{
   console.log(data)
   let plant = data.plant;

   if(!_.isEmpty(data.sensor)) plant.sensor = structuredClone(data.sensor);
   if(!_.isEmpty(data.sensorType)) plant.sensorType = structuredClone(data.sensorType);
   if(!_.isEmpty(data.relay)) plant.relay = structuredClone(data.relay);
   if(!_.isEmpty(data.relayType)) plant.relay.type = structuredClone(data.relayType);
   
   plant.plantType = structuredClone(data.plantType);
   plant.potSize = structuredClone(data.potSize);

   return plant;
}