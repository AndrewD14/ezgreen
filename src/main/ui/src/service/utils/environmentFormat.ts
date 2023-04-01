export function formatAll(data: any)
{
   let environments: any = [];

   environments = data.environments.map((environment: any) => {
      environment.sensors = data.sensors
                           .filter((sensor: any) => (sensor.environmentId === environment.id))
                           .map((sensor:any) => {
                              let value = {...sensor};
                              
                              value = {
                                 ...value,
                                 type: {...data.sensorTypes.filter((sensorType:any) => sensorType.id === sensor.typeId)[0]}
                              }

                              return value;
                           });
      environment.sensorType = data.sensorTypes.filter((sensorType: any) => environment.sensorType === sensorType.id)[0];
      
      return environment;
   });

   return environments;
}

export function formatOptions(data: any)
{
   console.log(data)
   let options: any = {
      relayTypes: data.relayTypes,
      sensorTypes: data.sensorTypes
   };

   if(data.sensors.length > 0) options.sensors = structuredClone(data.sensors.map((sensor:any) => {
      let value = {...sensor};
      
      value = {
         ...value,
         type: {...data.sensorTypes.filter((sensorType:any) => sensorType.id === sensor.typeId)[0]}
      }

      return value;
   }));

   if(data.relays.length > 0) options.relays = structuredClone(data.relays.map((relay:any) => {
      let value = {...relay};
      
      value = {
         ...value,
         type: {...data.relayTypes.filter((relayType:any) => relayType.id === relay.typeId)[0]}
      }

      return value;
   }));

   return options;
}

export function formatOne(data: any)
{
   let environment = data.environment;

   if(data.sensors.length > 0) environment.sensors = structuredClone(data.sensors.map((sensor:any) => {
      let value = {...sensor};
      
      value = {
         ...value,
         type: {...data.sensorTypes.filter((sensorType:any) => sensorType.id === sensor.typeId)[0]}
      }

      return value;
   }));

   environment.sensorType = data.sensorType;

   return environment;
}