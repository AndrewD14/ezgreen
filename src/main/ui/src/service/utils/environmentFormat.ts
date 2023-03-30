import _ from 'lodash';

export function formatAll(data: any)
{
   console.log(data)
   let environments: any = [];

   environments = data.environments.map((environment: any) => {
      let cursor = 0;

      if(environment.sensorId >= 1)
      {
         do
         {
            if(data.sensors[cursor].id === environment.sensorId) environment.sensor = structuredClone(data.sensors[cursor]);

            cursor = cursor + 1;
         }while(cursor < data.sensors.length && _.isEmpty(environment.sensor));
      }

      return environment;
   });

   return environments;
}

export function formatOne(data: any)
{
   let environment = data.environment;

   if(!_.isEmpty(data.sensor)) environment.sensor = structuredClone(data.sensor);

   return environment;
}