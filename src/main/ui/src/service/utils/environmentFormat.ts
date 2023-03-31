export function formatAll(data: any)
{
   let environments: any = [];

   environments = data.environments.map((environment: any) => {
      environment.sensors = data.sensors
                           .filter((sensor: any) => (sensor.zoneId === environment.zoneId))
                           .map((sensor:any) => {
                              let value = {...sensor};
                              
                              value = {
                                 ...value,
                                 type: {...data.sensorTypes.filter((sensorType:any) => sensorType.id === sensor.typeId)[0]}
                              }

                              return value;
                           });
      return environment;
   });

   return environments;
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

   return environment;
}