export function formatAll(data: any)
{
   console.log(data)
   let sensors: any = [];

   sensors = data.sensors.map((sensor: any) => {
      let plants: any = {};

      for(let p of data.plants) plants[p.sensorId] = p;
      for(let s of data.sensors) s.plant = structuredClone(plants[s.id]);

      return sensor;
   });

   console.log(sensors)
   return sensors;
}

export function formatOne(data: any)
{
   //???
}