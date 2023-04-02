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

      environment.relays = data.relays
                           .filter((relay: any) => (relay.environmentId === environment.id))
                           .map((relay:any) => {
                              let value = {...relay};
                              
                              value = {
                                 ...value,
                                 type: {...data.relayTypes.filter((relayType:any) => relayType.id === relay.typeId)[0]}
                              }
   
                              return value;
                           });

      environment.plants = data.plants
                           .filter((plant: any) => (plant.environmentId === environment.id))
                           .map((plant:any) => {
                              let value = {...plant};
   
                              return value;
                           });

         environment.sensorType = data.sensorTypes.filter((sensorType: any) => environment.sensorType === sensorType.id)[0];
      
      return environment;
   });

   return environments;
}

export function formatOptions(data: any)
{
   let options: any = {
      relayTypes: data.relayTypes,
      sensorTypes: data.sensorTypes
   };

   if(data.sensors.length > 0) options.sensors = data.sensors.map((sensor:any) => {
      let value = {...sensor};
      
      value = {
         ...value,
         type: {...data.sensorTypes.filter((sensorType:any) => sensorType.id === sensor.typeId)[0]}
      }

      return value;
   });

   options.sensors.sort((a: any, b: any) => {
      if(a.type.type < b.type.type) return -1;
      if(a.type.type > b.type.type) return 1;
      if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);

      return 0;
   });

   if(data.relays.length > 0) options.relays = data.relays.map((relay:any) => {
      let value = {...relay};
      
      value = {
         ...value,
         type: {...data.relayTypes.filter((relayType:any) => relayType.id === relay.typeId)[0]}
      }

      return value;
   });

   options.relays.sort((a: any, b: any) => {
      if(a.type.type < b.type.type) return -1;
      if(a.type.type > b.type.type) return 1;
      if(a.type.type === b.type.type) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);

      return 0;
   });

   if(data.plants.length > 0) options.plants = data.plants.map((plant:any) => {
      let value = {...plant};

      return value;
   });

   options.plants.sort((a: any, b: any) => {
      if(a.name < b.name) return -1;
      if(a.name > b.name) return 1;
      if(a.name === b.name) return (a.number < b.number ? -1 : a.number > b.number ? 1 : 0);

      return 0;
   });

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

   if(data.relays.length > 0) environment.relays = structuredClone(data.relays.map((relay:any) => {
      let value = {...relay};
      
      value = {
         ...value,
         type: {...data.relayTypes.filter((relayType:any) => relayType.id === relay.typeId)[0]}
      }

      return value;
   }));

   if(data.plants.length > 0) environment.plants = structuredClone(data.plants.map((plant:any) => {
      let value = {...plant};

      return value;
   }));

   environment.sensorType = data.sensorType;

   return environment;
}