export function formatAll(data: any)
{
   let sensors: any = [];

   sensors = data.sensors.map((sensor: any) => {
      let plants: any = {};
      let environments: any = {};

      for(let p of data.plants) plants[p.sensorId] = p;
      for(let e of data.environments) environments[e.sensorId] = e;

      for(let s of data.sensors)
      {
         if(plants.hasOwnProperty(s.id)) s.plant = structuredClone(plants[s.id]);
         else if(environments.hasOwnProperty(s.id)) s.environment = structuredClone(environments[s.id]);
      }

      return sensor;
   });

   return sensors;
}

export function formatOne(data: any)
{
   //???
}